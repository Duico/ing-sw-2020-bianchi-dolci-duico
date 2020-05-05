package it.polimi.ingsw.model;

import it.polimi.ingsw.message.InfoType;
import it.polimi.ingsw.model.event.*;
import it.polimi.ingsw.model.exception.IllegalTurnPhaseException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private transient UndoBlob undoBlob;

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
                readGame.players.size() <=3 &&
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
        //emitEvent(new FullInfoModelEvent(challenger, players, board, cardDeck));

        if(!useCards)
            setFirstPlayer(challenger.getNickName());
            //setFirstPlayer(challenger);

        initTurn(challenger);

    }


    private void createCardDeck() {
        try {
            this.cardDeck = new CardDeck();
        } catch (Exception e) {
            System.err.println("Error reading XML configuration file");
        }
    }


    private void dealDefaultCard(int numPlayers){
        Card defaultCard = Card.getDefaultCard();
        for (int n = 0; n < numPlayers; n++) {
            players.get(n).setCard(defaultCard);

        }

    }

    public boolean setChosenCards(List<String> cards){
        if(cards.size() != players.size()){
            return false;
        }
        List<Card> tempCards = new ArrayList<>();
        for(String card : cards){
            Card chosenCard = cardDeck.getCardByName(card);
            if(chosenCard == null)
                return false;
            tempCards.add(chosenCard);
        }
        chosenCards.addAll(tempCards);
        emitEvent( new ChosenCardsModelEvent(getCurrentPlayer(), getChosenCardsNames()) );
        return true;
    }


    public boolean setPlayerCard(String nameCard){
        if(turn.getCurrentPlayer().getCard()==null) {
            for (int i = 0; i < chosenCards.size(); i++) {
                Card card = chosenCards.get(i);
                if (card.getName().equals(nameCard)) {
                    Player currentPlayer = turn.getCurrentPlayer();
                    currentPlayer.setCard(card);
                    emitEvent(new SetCardModelEvent(getCurrentPlayer(), nameCard));
                    chosenCards.remove(card);
//              if(chosenCards.size()>0){
//                    new SetCardModelEvent(currentPlayer, card.getName());
//                }else{
//                    //list of players sent to challenger
//                }
                    //emitEvent( new SetCardModelEvent(currentPlayer, card.getName()) );
                    if (chosenCards.size() > 0) {
                        nextTurn();
                        //emitEvent(new NewChoseCardTurnModelEvent(currentPlayer,  card.getName());
                    } else {
                        nextTurn();
                        //list of players sent to challenger
                    }
                    //emitEvent( new SetCardModelEvent(currentPlayer, card.getName()) );
                    return true;
                }
            }
        }
        return false;
    }

    //todo should become protected in the end
    private Turn getTurn() {
        return turn;
    }

    public ArrayList<Card> getChosenCards() {
        return (ArrayList<Card>)chosenCards.clone();
    }
    public List<String> getChosenCardsNames(){
        return chosenCards.stream().map((card -> card.getName())).collect(Collectors.toList());
    }


    public void initTurn(Player player) {
        if(!useCards){
            startPlaceWorkersTurn(player);
        }else {
            startChoseCardsTurn(player);
            //notify
            //event for the challenger view to choose the three or two cards
        }
    }
    public void startNormalTurn(Player nextPlayer){
        Card previousTurnCard = turn.getCurrentPlayer().getCard();
        nextPlayer.resetAllWorkers();
        boolean blockNextPlayer = turn.isBlockNextPlayer();
        boolean previousBlockNextPlayer = turn.getPreviousBlockNextPlayer();
        if(previousBlockNextPlayer && nextPlayer.getCard()!=turn.getPreviousTurnCard()){
            blockNextPlayer = true;
            previousTurnCard=turn.getPreviousTurnCard();
        }
        turn = new NormalTurn(nextPlayer, previousTurnCard, blockNextPlayer);
        emitEvent(new NewTurnModelEvent(nextPlayer, TurnPhase.NORMAL));
        checkHasLost();

    }

    public void nextTurn(){
        Player nextPlayer = this.getNextPlayer();
        nextTurn(nextPlayer);
    }
    private void nextTurn(Player nextPlayer) { //notify view in every case
        TurnPhase phase = turn.getPhase();

        if(phase == TurnPhase.CHOSE_CARDS){
            if(!isSetFirstPlayer()){
                startChoseCardsTurn(nextPlayer);
            }else{
                startPlaceWorkersTurn(nextPlayer);
            }
        }else if(phase == TurnPhase.PLACE_WORKERS){
            if(isAnyPlayersWorkerNotPlaced()){
                startPlaceWorkersTurn(nextPlayer);
            }else {
                startNormalTurn(nextPlayer);
            }
        }else if(phase == TurnPhase.NORMAL){
            startNormalTurn(nextPlayer);
        }

//        if(useCards && !isSetFirstPlayer()) {
//            startChoseCardsTurn(nextPlayer);
//            //emitEvent()
//        }else if(isAnyPlayersWorkerNotPlaced()){
//            startPlaceWorkersTurn(nextPlayer);
//        }else { /*if(!turn.isAllowedToMove() && !turn.isAllowedToBuild())*/
//            startNormalTurn(nextPlayer);
//            //checkHasLost(); comment for testing
//        }
    }

    private void startPlaceWorkersTurn(Player player){
        turn=new PlaceWorkersTurn(player);
        emitEvent(new NewTurnModelEvent(player, TurnPhase.PLACE_WORKERS));
    }
    private void startChoseCardsTurn(Player player){
        turn = new ChoseCardsTurn(player);
        //emitEvent(new NewTurnModelEvent(player, TurnPhase.CHOSE_CARDS));
        if(chosenCards.size()==0) {
            emitEvent(new NewChoseCardTurnModelEvent(player, TurnPhase.CHOSE_CARDS, cardDeck.getCardNames()));
        }else if(chosenCards.size()==1) {
            Card card = chosenCards.get(0);
            player.setCard(card);
            emitEvent(new SetCardModelEvent(getCurrentPlayer(), card.getName()));
            ArrayList<String> namePlayers = new ArrayList<>();
            for(int i=0;i<players.size();i++)
                namePlayers.add(players.get(i).getNickName());
            emitEvent(new NewChoseCardTurnModelEvent(player, TurnPhase.CHOSE_CARDS, namePlayers));
        }else{
            emitEvent(new NewChoseCardTurnModelEvent(player, TurnPhase.CHOSE_CARDS, getChosenCardsNames()));
        }
    }

    //TODO: Chiedere ad ale
    /*public boolean firstTurn(Player player) {
        if(!setFirstPlayer(player)){
            return false;
        }
        emitEvent(new NewTurnModelEvent(player, TurnPhase.NORMAL));
        nextTurn(player);
        return true;
    }

    private boolean setFirstPlayer(Player player){
        if(players.contains(player)) {
            this.firstPlayer = players.get(players.indexOf(player));
            return true;
        }
        return false;
    }
    */

    public boolean firstTurn(String nickname) {
        Player firstPlayer=setFirstPlayer(nickname);
        if(firstPlayer==null){
            return false;
        }
        emitEvent(new FullInfoModelEvent(InfoType.INITGAME, getCurrentPlayer(), players, board.clone()));
        //emitEvent(new NewTurnModelEvent(firstPlayer, TurnPhase.PLACE_WORKERS));
        nextTurn(firstPlayer);
        return true;
    }

    private Player setFirstPlayer(String nickName){
        for(int i=0;i<players.size();i++)
            if(players.get(i).getNickName().equals(nickName)) {
                this.firstPlayer = players.get(i);
                return firstPlayer;
            }
        return null;
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


    private Player getNextPlayer() {
        return scrubPlayers(1);
    }

    private Player scrubPlayers(int direction){
        int index = -1;
        for(int i=0;i<players.size();i++){
            if(turn.isCurrentPlayerUUID(players.get(i).getUuid())){
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


    public void move(Position startPosition, Position destinationPosition){
        if(turn.getPhase() != TurnPhase.NORMAL)
            throw new IllegalTurnPhaseException();

        backupUndo();
        Position pushDestPosition = turn.boardMove(board, startPosition, destinationPosition);
        emitEvent(new MoveWorkerModelEvent(getCurrentPlayer(), startPosition, destinationPosition, pushDestPosition));
        if(turn.isWinningMove(board,startPosition,destinationPosition))
            emitEvent(new WinModelEvent(getCurrentPlayer()));
        if(checkHasLost() && players.size()==1)
                emitEvent(new WinModelEvent(players.get(0)));
    }

    public boolean isAllowedToMove(){
        return turn.isAllowedToMove();
    }
    public boolean isAllowedToMove(Position workerPosition){
        return turn.isAllowedToMove(workerPosition);
    }
    public boolean isRequiredToMove(){
        return turn.isRequiredToMove();
    }
    public boolean isAllowedToBuild(){ return turn.isAllowedToBuild(); }
    public boolean isAllowedToBuild(Position workerPosition){ return turn.isAllowedToBuild(workerPosition); }
    public boolean isRequiredToBuild(){ return turn.isRequiredToBuild(); }
    public boolean isFeasibleMove(Position startPosition, Position destinationPosition){
        return turn.isFeasibleMove(board, startPosition, destinationPosition);
    }
    public boolean isFeasibleBuild(Position startPosition, Position buildPosition, boolean isDome){
        return turn.isFeasibleBuild(board, startPosition, buildPosition, isDome);
    }



    public boolean isBlockedMove(Position startPosition, Position destinationPosition){
        boolean blocked = false;
        if(turn.getPreviousBlockNextPlayer()) {
            if(turn.isBlockedMove(board, startPosition, destinationPosition)){
                blocked = true;
            }
        }
        return blocked;
    }


    public void build(Position startPosition, Position destinationPosition, boolean isDome){
        if(turn.getPhase() != TurnPhase.NORMAL)
            throw new IllegalTurnPhaseException();
        backupUndo();
        turn.boardBuild(board, startPosition, destinationPosition, isDome);
        emitEvent(new BuildWorkerModelEvent(getCurrentPlayer(), startPosition, destinationPosition, isDome) );
        if(checkHasLost() && players.size()==1)
            emitEvent(new WinModelEvent(players.get(0)));

    }



    public int place(Position placePosition) {
        if(turn.getPhase() != TurnPhase.PLACE_WORKERS)
            throw new IllegalTurnPhaseException();
        backupUndo();

        int workerId = turn.boardPlace(board, placePosition);
        if(workerId>=0) {
            emitEvent(new PlaceWorkerModelEvent(getCurrentPlayer(), placePosition));
        }
        return workerId;
    }

    public boolean isAnyWorkerNotPlaced() {
        return turn.isAnyWorkerNotPlaced();
    }

    public boolean isAnyPlayersWorkerNotPlaced(){
        for(Player player:players){
            if(player.isAnyWorkerNotPlaced())
                return true;
        }
        return false;
    }

    public TurnPhase getTurnPhase(){
        return turn.getPhase();
    }


    public Player getCurrentPlayer(){
        if(turn.getCurrentPlayer() == null){
            return null;
        }
        return turn.getCurrentPlayer().clone();
    }

    public boolean checkCurrentWorker(Position currentWorkerPosition) {
        return turn.checkCurrentWorker(currentWorkerPosition);
    }


    public boolean checkHasLost(){
        if(hasLost()){
            //notify view
            Player currentPlayer = turn.getCurrentPlayer();
            for(int i=0; i<numWorkers; i++){
                Position workerPosition = currentPlayer.getWorkerPosition(i);
                board.removeWorker(workerPosition);
            }
            emitEvent(new PlayerDefeatModelEvent(getCurrentPlayer(), false));
            players.remove(currentPlayer);
            return true;
        }
        return false;
    }

    private boolean hasLost(){
        if(!isAnyPlayersWorkerNotPlaced()) {
            if (!turn.isUndoAvailable) {
                return turn.isLoseCondition(board);
            } else {
                if(turn.isLoseCondition(board))
                    emitEvent(new PlayerDefeatModelEvent(getCurrentPlayer(), true));
                return false;
            }
        }else{
            return false;
        }
    }

    public boolean isUndoAvailable(){
        return turn.isUndoAvailable;
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
        //TODO event
        emitEvent(new UndoModelEvent(getCurrentPlayer(), board, players));
        return true;
    }

    private void backupUndo(){
        //serialize turn, players
        this.undoBlob = new UndoBlob(turn, board, players);
        turn.isUndoAvailable = true;
        //return true;
    }

    public void regenerateUndo(){
        backupUndo();
    }
//
//    //for persistency
//    public PersistencyEvent generatePersistencyEvent() {
//        List<Player> clonedPlayers = (List<Player>) players.clone();
//        Turn clonedTurn = turn.clone();
//        //useCards
//        //firstPlayer;
//        // int numWorkers;
//        Board clonedBoard = board.clone();
//        private UndoBlob undoBlob;
//        return new PersistencyEvent();
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        boolean playersEquals = true;
       // return useCards == game.useCards &&
                //Objects.equals(getTurn(), game.getTurn()) &&
               //equals(getPreviousTurn(), game.getPreviousTurn()) &&
                //Objects.equals(getPlayers(), game.getPlayers()) &&
                //Objects.equals(cardDeck, game.cardDeck);
        for(int i=0; i<players.size(); i++){
            playersEquals = playersEquals && players.get(i).equals( game.players.get(i) );
        }
        return useCards == game.useCards &&
                numWorkers == game.numWorkers &&
                Objects.equals(board, game.board) &&
                Objects.equals(turn, game.turn) &&
                Objects.equals(cardDeck, game.cardDeck) &&
                //undoBlob
                playersEquals;

    }

//    @Override
//    protected Game clone() {
//        Game newGame = null;
//        try {
//            newGame = (Game) super.clone();
//            newGame.board.cleanWorkers();
//
//        }catch (CloneNotSupportedException e){
//            e.printStackTrace();
//        }
//        return newGame;
//    }
}