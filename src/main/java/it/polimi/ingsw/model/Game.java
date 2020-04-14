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
public class Game implements Serializable{
    private Turn turn;
    private Turn previousTurn;
    private ArrayList<Player> players = new ArrayList<>();
    boolean useCards = false;
    CardDeck cardDeck;
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

    /**
     * Start this Game with chosen nicknames for the players
     * @param nicknames Array of the players nicknames, in display order
     * @param useCards  True if the game will be using cards
     * @return
     */
    public void startGame(ArrayList<String> nicknames, boolean useCards) {

        this.useCards = useCards;

        for (int n = 0; n < nicknames.size(); n++) {
            Player newPlayer = new Player(nicknames.get(n), numWorkers);
            players.add(newPlayer);
        }

        if (this.useCards) {
            //createCardDeck();  TO FIX
            //dealCards();
        } else {
            //assign default Card to each player
        }
        initTurn();
        //make an exceptional first turn that calls setWorker
    }

    private void createCardDeck() {
        try {
            this.cardDeck = new CardDeck();
        } catch (Exception e) {
            System.err.println("Error reading XML configuration file");
        }
    }

    public void dealCards() {
        ArrayList<Card> randomDeck = cardDeck.pickRandom(players.size());
        for (int i = 0; i < players.size(); i++) {
            Card randomCard = randomDeck.get(i);
            players.get(i).setCard(randomCard);
        }
    }

//    public Player getCurrentPlayer(){
//        return turn.getCurrentPlayer();
//        //todo clone
//    }

//FIX remove
    public Turn getTurn() {
        return turn;
    }

    public void setTurn(Turn turn) {
        this.turn = turn;
    }

    public Turn getPreviousTurn() {
        return previousTurn;
    }

    public int getNumWorkers(){
        return this.numWorkers;
    }

    public void initTurn() {
        Card previousCard = getPreviousPlayer().getCard();
        //we need previousCard to check opponentStrategy
        if(previousCard == null){
            throw new NullPointerException("Previous Card is not set");
        }

        turn = new Turn( this.pickFirstPlayer(), previousCard, false);
    }



    public void nextTurn() {
        //TODO
        //reset currentWorker's moves-builds-operations
        Card previousTurnCard = turn.getPlayerCard();
        Player nextPlayer = this.getNextPlayer();
        nextPlayer.resetAllWorkers();

        boolean blockNextPlayer = turn.isBlockNextPlayer();
        previousTurn = turn;
        turn = new Turn(nextPlayer, previousTurnCard, blockNextPlayer);
    }

    private Player pickFirstPlayer() {
            int rand = (int) Math.floor( Math.random() * (double) players.size() );
            Player randPlayer=players.get(rand);
            return randPlayer;
    }

    private Player getPreviousPlayer() {
        return scrubPlayers(-1);
    }
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

    public Player getPlayer(int n) {
        return players.get(n);
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public void move(int workerId, Position destinationPosition) {
        backupUndo();
        boardMove(workerId, destinationPosition);
        //return undoStatus;
    }

    public void build(int workerId, Position destinationPosition, boolean isDome){
        backupUndo();
        boardBuild(workerId, destinationPosition, isDome);
        //return undoStatus;
    }

    public int place(Position placePosition) {
        backupUndo();
        return boardPlace(placePosition);
        //return undoStatus;
    }
    private int boardPlace(Position placePosition){
        Player currentPlayer = turn.getCurrentPlayer();
        Worker newWorker = new Worker();
        if(board.setWorker(newWorker, placePosition)) {
            return currentPlayer.addWorker(newWorker);
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
            turn.updateCurrentWorker(workerId);
        }catch (Exception e){
            e.printStackTrace();
        }

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
        Card card = currentPlayer.getCard();
        return card.getBlockStrategy().isBlockMove( startPosition, destinationPosition, board);
    }


    public boolean blockNextPlayer(int workerId, Position destinationPosition) {
        Player currentPlayer = turn.getCurrentPlayer();
        Position startPosition = currentPlayer.getWorkerCurrentPosition(workerId);
        Card card = currentPlayer.getCard();

        return card.getBlockStrategy().blockNextPlayer(startPosition, destinationPosition, board);
    }

    public void boardBuild(int workerId, Position destinationPosition, boolean isDome){
        Position startPosition = turn.getCurrentPlayer().getWorkerCurrentPosition(turn.getCurrentWorkerId());
        board.build(startPosition, destinationPosition, isDome);
        turn.updateCurrentWorker(workerId);
    }

    public boolean isWinningMove(Position destinationPosition){
        Player currentPlayer = turn.getCurrentPlayer();
        Card card = currentPlayer.getCard();
        Position startPosition = currentPlayer.getWorkerCurrentPosition(turn.getCurrentWorkerId());
        WinStrategy winStrategy = card.getWinStrategy();
        return winStrategy.isWinningMove(startPosition, destinationPosition, board );
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

    private boolean canDoOperation(int workerId){
        boolean isRequiredToMove = turn.isRequiredToMove(workerId);
        boolean isRequiredToBuild = turn.isRequiredToBuild(workerId);

        if(isRequiredToBuild && isRequiredToMove){
            if(!canBuild(workerId) && !canMove(workerId))
                return false;
        }else if(isRequiredToBuild){
            if(!canBuild(workerId))
                return false;
        }else if(isRequiredToMove){//should be impossible
            if(!canMove(workerId))
                return false;
        }
        return true;
    }

    public boolean checkLoseCondition() {
        Player currentPlayer = turn.getCurrentPlayer();
        Card card = currentPlayer.getCard();
        boolean loseCondition = true;
        if(turn.isSetCurrentWorker()){
            loseCondition = !canDoOperation(turn.getCurrentWorkerId()); //&& loseCondition

        }else {//first operation of the turn can have workerId not set
            for (int workerId = 0; workerId < currentPlayer.getNumWorkers(); workerId++) {
                loseCondition = !canDoOperation(workerId) && loseCondition;
            }
        }
        return loseCondition;
    }

    @Override
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
            playersEquals = playersEquals && getPlayer(i).getNickName().equals( game.getPlayer(i).getNickName() );
        }
        return useCards == game.useCards && playersEquals;

    }
}