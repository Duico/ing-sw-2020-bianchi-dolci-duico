package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import it.polimi.ingsw.model.strategy.BuildStrategy;
import it.polimi.ingsw.model.strategy.MoveStrategy;
import it.polimi.ingsw.model.strategy.OpponentStrategy;
import it.polimi.ingsw.model.strategy.WinStrategy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Manages a game from start to end, handles the creation and advancement of turns
 */
public class Game extends ModelEventEmitter implements Serializable{
    private Turn turn;
    private ArrayList<Player> players = new ArrayList<>();
    boolean useCards = false;
    private CardDeck cardDeck;


    private ArrayList<Card> chosenCards;
    private Player firstPlayer;
    private final int numWorkers;
    private Board board;
    private UndoBlob undoBlob;

    public Game() {
        this(5, 5, 2);
    }

    /**
     * Create a new Game <b>without starting it</b>
     * @param width Width of the board
     * @param height Height of the board
     * @param numWorkers Number of workers for each player
     */
    public Game(int width, int height, int numWorkers) {
        Position.setSize(width, height);
        this.numWorkers = numWorkers;
    }

    public static boolean validateGame(Game readGame) {
        //TODO improve
        return readGame != null &&
                readGame.board != null &&
                readGame.players != null &&
                readGame.players.size() >=2 &&
                readGame.numWorkers > 0;
    }

    /**
     * Start this Game with chosen nicknames for the players
     * @param players Array of the players, in display order
     * @param useCards  True if the game will be using cards
     * @return
     */
    public void startGame(ArrayList<Player> players, boolean useCards) {

        this.useCards = useCards;
        this.firstPlayer=null;
        int numPlayers = players.size();

        if(numPlayers <2 || numPlayers >3)
            throw new RuntimeException("Number of Players out of range");

        this.players = players;


        if (this.useCards) {
            createCardDeck();

        } else {
            dealDefaultCard(numPlayers);
        }

        for(Player player: players){
           player.initWorkers(numWorkers);
        }

        board = new Board();
        chosenCards = new ArrayList<>();
        Player challenger = pickFirstPlayer();
        challenger.setIsChallenger(true);
        if(!useCards)
            setFirstPlayer(challenger);

        initTurn(challenger);
        //make an exceptional first turn that calls setWorker
    }


    private void createCardDeck() {
        try {
            this.cardDeck = new CardDeck();
        } catch (Exception e) {
            System.err.println("Error reading XML configuration file");
        }
    }

   /* private ArrayList<Card> dealCards(int numPlayers) {
        ArrayList<Card> randomDeck = cardDeck.pickRandom(numPlayers);
        return randomDeck;
    }*/

    private void dealDefaultCard(int numPlayers){
        Card defaultCard = Card.getDefaultCard();
        for (int n = 0; n < numPlayers; n++) {
            players.get(n).setCard(defaultCard);

        }

    }

    public boolean setChosenCards(ArrayList<String> cards){
        for(int i=0; i<players.size(); i++){
            chosenCards.add(cardDeck.getCardByName(cards.get(i)));
        }
        return true;
    }


    public void setPlayerCards(String nameCard){
        for(int i=0; i<chosenCards.size(); i++){
            Card card = chosenCards.get(i);
            if (card.getName().equals(nameCard)){
                Player currentPlayer = turn.getCurrentPlayer();
                currentPlayer.setCard(card);
                chosenCards.remove(card);
                if(chosenCards.size()>0)
                    return;
                else{
                    //notify view per challenger restituisco la lista dei player
                    return;
                }
            }
        }
    }


    public Turn getTurn() {
        return turn;
    }

    public ArrayList<Card> getChosenCards() {
        return (ArrayList<Card>)chosenCards.clone();
    }


    public void initTurn(Player player) {
        turn = new ChoseCardsTurn(player);
        //notify view
        //event for the challenger view for choose the three or two cards
    }
    public void createTurn(){
        Card previousTurnCard = turn.getCurrentPlayer().getCard();
        Player nextPlayer = this.getNextPlayer();
        nextPlayer.resetAllWorkers();
        boolean blockNextPlayer = turn.isBlockNextPlayer();
        turn = new NormalTurn(nextPlayer, previousTurnCard, blockNextPlayer);
    }

    /*public void nextTurn() {
            Card previousTurnCard = turn.getCurrentPlayer().getCard();
            Player nextPlayer = this.getNextPlayer();
            nextPlayer.resetAllWorkers();
            boolean blockNextPlayer = turn.isBlockNextPlayer();
            turn = new Turn(nextPlayer, previousTurnCard, blockNextPlayer);
    }*/


    public void nextTurn() {
        if(!isSetFirstPlayer()) {
            Card previousTurnCard = turn.getCurrentPlayer().getCard();
            Player nextPlayer = this.getNextPlayer();
            initTurn(nextPlayer);
            //notify view
        }else if(){ //PlaceWorkersTurn
        }else /*if(!turn.isAllowedToMove() && !turn.isAllowedToBuild())*/ {
            createTurn();
            //checkHasLost(); comment for testing
        }
    }


    public void firstTurn(Player player) {
        setFirstPlayer(player);
        turn = new NormalTurn(this.firstPlayer, null, false);
        //notify view of new turn;
    }

    private void setFirstPlayer(Player player){
            if(players.contains(player)) {
                this.firstPlayer = players.get(players.indexOf(player));
            }
    }

    public boolean isSetFirstPlayer(){
        if(firstPlayer==null)
            return false;
        else
            return true;
    }

    private Player pickFirstPlayer() {
            int rand = (int) Math.floor( Math.random() * (double) players.size() );
            Player randPlayer=players.get(rand);
            return randPlayer;
    }

//    private Player getPreviousPlayer() {
//        return scrubPlayers(-1);
//    }

    private Player getNextPlayer() {
        return scrubPlayers(1);
    }

    private Player scrubPlayers(int direction){
        UUID id = turn.getCurrentPlayerUUID();
        int index = -1;
        for(int i=0;i<players.size();i++){
            if(players.get(i).getUuid() == id){
                index = i;
                break;
            }
        }
        if (index>=0) {
            int size = players.size();
            return players.get( (index + direction) % size );
        }else {
            throw new RuntimeException("Current player was not found in Game player List");
        }
    }


    /*public ArrayList<Player> getPlayers() {
        return this.players;
    }*/

    public void move(int workerId, Position destinationPosition){
        //FIX check is NormalTurn
        backupUndo();
        boardMove(workerId, destinationPosition);
        //checkHasLost(); for testing
    }

    public void build(int workerId, Position destinationPosition, boolean isDome){
        backupUndo();
        boardBuild(workerId, destinationPosition, isDome);
        checkHasLost();
    }

    public int place(Position placePosition) {
        //FIX check is SetWorkerTurn
        backupUndo();
        return boardPlace(placePosition);
        //return undoStatus;
    }
    private int boardPlace(Position placePosition){
        Player currentPlayer = turn.getCurrentPlayer();
        Worker newWorker = new Worker();
        if(board.setWorker(newWorker, placePosition)) {
            int workerId = currentPlayer.addWorker(newWorker);
            //comment for testing
            //emitEvent(new PlaceWorkerModelEvent(currentPlayer.getUuid(), workerId, placePosition));
            return workerId;
        }else
            return -1;
    }

    private void boardMove(int workerId, Position destinationPosition) {
        Player currentPlayer = turn.getCurrentPlayer();
        Card card = currentPlayer.getCard();
        Position startPosition = currentPlayer.getWorkerCurrentPosition(turn.getCurrentWorkerId());

        if(turn.getBlockNextPlayer() == false) {
            blockNextPlayer(workerId, destinationPosition);
        }
        try {
            Position pushDestPosition = card.getOpponentStrategy().destinationPosition(startPosition, destinationPosition);
            board.putWorkers(startPosition, destinationPosition, pushDestPosition);
            //comment for testing
            //emitEvent(new PutWorkerModelEvent(currentPlayer.getUuid(), workerId, startPosition, destinationPosition, pushDestPosition));
            turn.updateCurrentWorker(workerId);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void boardBuild(int workerId, Position destinationPosition, boolean isDome){
        Player currentPlayer = turn.getCurrentPlayer();
        Position startPosition = turn.getCurrentPlayer().getWorkerCurrentPosition(turn.getCurrentWorkerId());
        board.build(startPosition, destinationPosition, isDome);
        //comment for testing
        //emitEvent(new BuildWorkerModelEvent(currentPlayer.getUuid(), workerId, startPosition, destinationPosition) );
        turn.updateCurrentWorker(workerId);
    }

    public boolean isFeasibleMove(int workerId, Position destinationPosition){
        Player currentPlayer = turn.getCurrentPlayer();
        Card card = currentPlayer.getCard();
        Position startPosition = currentPlayer.getWorkerCurrentPosition(workerId);

        boolean isOwnWorker = turn.getCurrentPlayer().isOwnWorkerInPosition(destinationPosition);

        MoveStrategy moveStrategy = card.getMoveStrategy();
        OpponentStrategy opponentStrategy = card.getOpponentStrategy();
        boolean isValidMove = moveStrategy.isValidMove(startPosition, destinationPosition, this.board);
        boolean isValidPush = opponentStrategy.isValidPush(startPosition, destinationPosition, isOwnWorker, this.board);

        if( isValidMove == true && isValidPush==true ) {
            return true;
        }else{
            return false;
        }
    }

    public boolean isFeasibleBuild(int workerId, Position destinationPosition, boolean isDome){
        Player currentPlayer = turn.getCurrentPlayer();
        Card card = currentPlayer.getCard();
        Position startPosition = currentPlayer.getWorkerCurrentPosition(workerId);

        BuildStrategy buildStrategy = card.getBuildStrategy();
        boolean isValidBuild = buildStrategy.isValidBuild(startPosition, destinationPosition, isDome, this.board);

        return isValidBuild;
    }

    public boolean isBlockedMove(int workerId, Position destinationPosition){
        Player currentPlayer = turn.getCurrentPlayer();
        Position startPosition = currentPlayer.getWorkerCurrentPosition(workerId);

        return turn.getPreviousTurnCard().getBlockStrategy().isBlockMove( startPosition, destinationPosition, board);
    }


    public boolean blockNextPlayer(int workerId, Position destinationPosition) {
        Player currentPlayer = turn.getCurrentPlayer();
        Position startPosition = currentPlayer.getWorkerCurrentPosition(workerId);
        Card card = currentPlayer.getCard();

        return card.getBlockStrategy().blockNextPlayer(startPosition, destinationPosition, board);
    }

    public boolean isWinningMove(Position destinationPosition){
        Player currentPlayer = turn.getCurrentPlayer();
        Card card = currentPlayer.getCard();
        Position startPosition = currentPlayer.getWorkerCurrentPosition(turn.getCurrentWorkerId());
        WinStrategy winStrategy = card.getWinStrategy();
        return winStrategy.isWinningMove(startPosition, destinationPosition, board );
    }



    /**
     * Checks is the chosen worker can move in any cell in the current operation, respecting all constraints imposed by cards
     * @param workerId WorkerId of the worker of currentPlayer you want to check
     * @return True if an adjacent cell exists where the player can move in the current operation
     */
    private boolean canMove(int workerId) {
        Player currentPlayer = turn.getCurrentPlayer();
        Position position = currentPlayer.getWorkerCurrentPosition(workerId);
        int currentY = position.getY();
        for(int dy= -1; dy<=1; dy++) {
            int positionY=currentY+dy;
            if (positionY < 0 || positionY >= Position.height) {
                continue;
            }
            int currentX = position.getX();
            for (int dx = -1; dx <=1; dx++) {
                int positionX=currentX+dx;
                if (positionX < 0 || positionX >= Position.width) {
                    continue;
                }
                try {
                    Position destPosition = new Position(positionX, positionY);

                    //try {
                    //if destPosition is a good candidate for a move, check if the worker can effectively move in destPosition
                    if(!isBlockedMove(workerId, destPosition) && isFeasibleMove(workerId, destPosition))
                        return true;
                    //}catch (BlockedMoveException e){
                    //    continue;
                    //}
                }catch(PositionOutOfBoundsException e){
                    continue;
                }

            }
        }
        return false;
    }
    /**
     * Checks is the chosen worker can build in any cell in the current operation, respecting all constraints imposed by cards
     * @param workerId WorkerId of the worker of currentPlayer you want to check
     * @return True if an adjacent cell exists where the player can build in the current operation
     */
    private boolean canBuild(int workerId){
        Player currentPlayer = turn.getCurrentPlayer();
        Position position = currentPlayer.getWorkerCurrentPosition(workerId);
        int currentY = position.getY();
        for(int dy= -1; dy<=1; dy++) {
            int positionY=currentY+dy;
            if (positionY < 0 || positionY >= Position.height) {
                continue;
            }
            int currentX = position.getX();
            for (int dx = -1; dx <=1; dx++) {
                int positionX=currentX+dx;
                if (positionX < 0 || positionX >= Position.width) {
                    continue;
                }
                try {
                    Position destPosition = new Position(positionX, positionY);

                    //if destPosition is a good candidate for a move, check if the worker can effectively move in destPosition
                    if(isFeasibleBuild(workerId, destPosition, false) && isFeasibleBuild(workerId, destPosition, true))
                        return true;

                }catch(PositionOutOfBoundsException e){
                    continue;
                }

            }
        }
        return false;
    }

    private boolean cannotMakeRequiredOperation(int workerId){
        boolean isRequiredToMove = turn.isRequiredToMove(workerId);
        boolean isRequiredToBuild = turn.isRequiredToBuild(workerId);

        if(isRequiredToBuild && isRequiredToMove){
            if(!canBuild(workerId) && !canMove(workerId))
                return true;
        }else if(isRequiredToBuild){
            if(!canBuild(workerId))
                return true;
        }else if(isRequiredToMove){//should be impossible
            if(!canMove(workerId))
                return true;
        }
        return false;
    }

    public void checkHasLost(){
        if(hasLost()){
            //notify view
            Player currentPlayer = turn.getCurrentPlayer();
            for(int i=0; i<numWorkers; i++){
                Position workerPosition = currentPlayer.getWorkerCurrentPosition(i);
                board.removeWorker(workerPosition);
            }
            players.remove(currentPlayer);
        }
    }

    private boolean hasLost(){
        if(!turn.isUndoAvailable){
            return isLoseCondition();
        }else{
            return false;
        }
    }

    private boolean isLoseCondition() {
        Player currentPlayer = turn.getCurrentPlayer();
        boolean loseCondition = true;
        if(turn.isSetCurrentWorker()){
            loseCondition = cannotMakeRequiredOperation(turn.getCurrentWorkerId()); //&& loseCondition

        }else {//first operation of the turn can have workerId not set
            for (int workerId = 0; workerId < currentPlayer.getNumWorkers(); workerId++) {
                loseCondition = cannotMakeRequiredOperation(workerId) && loseCondition;
            }
        }
        return loseCondition;
    }

    public boolean undo(){
        if(!turn.isUndoAvailable){
            return false;
        }
        //deserialize turn, players
        Turn undoTurn = undoBlob.getTurn();
        Board undoBoard = undoBlob.getBoard();
        ArrayList<Player> undoPlayers = undoBlob.getPlayers();
        if(undoTurn == null || undoBoard == null ||undoPlayers == null){
            return false;
        }
        turn = undoTurn;
        board = undoBoard;
        players = undoPlayers;
        //change isUndoAvailable in currentPlayer
        turn.isUndoAvailable = false;
        return true;
    }

    private void backupUndo(){
        //serialize turn, players
        this.undoBlob = new UndoBlob(turn, board, players);
        turn.isUndoAvailable = true;
        //return true;
    }

    private boolean isAnyPlayerWorkerNotPlaced(){
        for(Player player: players){
            if(player.isAnyWorkerNotPlaced()){
                return true;
            }
        }
        return false;
    }

   /* @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        boolean playersEquals = true;
        //return useCards == game.useCards &&
                //Objects.equals(getTurn(), game.getTurn()) &&
                //Objects.equals(getPreviousTurn(), game.getPreviousTurn()) &&
                //Objects.equals(getPlayers(), game.getPlayers()) &&
                //Objects.equals(cardDeck, game.cardDeck);
        for(int i=0; i<getPlayers().size(); i++){
            playersEquals = playersEquals && players.get(i).getNickName().equals( game.players.get(i).getNickName() );
        }
        return useCards == game.useCards && playersEquals;

    }*/
}