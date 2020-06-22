package it.polimi.ingsw.model;

import it.polimi.ingsw.model.event.*;
import it.polimi.ingsw.model.exception.IllegalTurnPhaseException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Manages a game from start to end, handles the creation and advancement of turns
 */
public class Game extends ModelEventEmitter implements Serializable{
    private Turn turn;
    private List<Player> players = new ArrayList<>();
    boolean useCards = false;
    private CardDeck cardDeck;

    private ArrayList<Card> chosenCards;
    private Player firstPlayer;
    private final int numWorkers;
    private Board board;
    private transient UndoBlob undoBlob;
    private transient ScheduledExecutorService undoExecutorService = Executors.newScheduledThreadPool(2);
    private boolean active;


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
     * @param readGame current game
     * @return true if game is correctly set
     */
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
    public void startGame(List<Player> players, boolean useCards) {
        this.active = true;
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

        int n = 0;
        for(Player player: players){
           player.setColor(PlayerColor.fromInt(n));
           player.initWorkers(numWorkers);
           ++n;
        }

        board = new Board();
        Player challenger = pickFirstPlayer();
        challenger.setIsChallenger(true);
        //emitEvent(new FullInfoModelEvent(challenger, players, board, cardDeck));

        initTurn(challenger);

    }

    /**
     * @return True ii card deck is correctly created
     */
    private boolean createCardDeck() {
        try {
            this.cardDeck = new CardDeck("./card-config.xml");
            return true;
        } catch (Exception e) {
            System.err.println("Error reading XML configuration file. Loading default deck.");
            try {
                this.cardDeck = new CardDeck(getClass().getResourceAsStream("/xml/card-config.xml"));
            } catch (Exception f) {
                f.printStackTrace();
//                System.err.println("Missing fallback card-config.xml. Aborting");
                throw new RuntimeException("Missing fallback card-config.xml. Aborting");
            }
            return false;
        }
    }


    /**
     * sets a Default card to each player
     * @param numPlayers number of players in the game
     */
    private void dealDefaultCard(int numPlayers){
        Card defaultCard = Card.getDefaultCard();
        for (int n = 0; n < numPlayers; n++) {
            players.get(n).setCard(defaultCard);

        }

    }

    /**
     * @param cards list of card names related to cards chosen by challenger player
     * @return True if chosen cards are correctly set
     */
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
        //TODO DA DIRE AD ALE
        /*ModelEvent evt = new ChosenCardsModelEvent(getCurrentPlayer(), null, getChosenCardsNames());
        emitEvent(evt);*/
        return true;
    }


    /**
     * sets Card to current player
     * @param nameCard name related to Card
     * @return
     */
    public boolean setPlayerCard(String nameCard){
        if(turn.getCurrentPlayer().getCard()==null) {
            for (int i = 0; i < chosenCards.size(); i++) {
                Card card = chosenCards.get(i);
                if (card.getName().equals(nameCard)) {
                    Player currentPlayer = turn.getCurrentPlayer();
                    currentPlayer.setCard(card);
                    //ModelEvent evt = new SetCardModelEvent(getCurrentPlayer(), nameCard);
                    ModelEvent evt = new SetCardModelEvent(getCurrentPlayer(), card);
                    emitEvent(evt);
                    chosenCards.remove(card);
                    /*ModelEvent evt2 = new ChosenCardsModelEvent(getCurrentPlayer(), getChosenCardsNames());
                    emitEvent(evt2);*/
//
//                    if(chosenCards.size() == 1){
//                        setPlayerCard(new SetCardModelEvent())
//                    }
//                    if (chosenCards.size() > 0) {
//                        nextTurn();
//                        //emitEvent(new NewChoseCardTurnModelEvent(currentPlayer,  card.getName());
//                    } else {
//                        //nextTurn();
//                        //list of players sent to challenger
//                    }
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
        return (ArrayList<Card>) chosenCards.clone();
    }
    public List<String> getChosenCardsNames(){
        if(chosenCards==null)
            return null;
        return chosenCards.stream().map((card -> card.getName())).collect(Collectors.toList());
    }


    /**
     * updates game Turn checking if current Game is played with cards or not
     * @param challenger challenger player of the game
     */
    public void initTurn(Player challenger) {
        if(!useCards){
            setFirstPlayer(challenger);
            startPlaceWorkersTurn(challenger);
        }else{
            startChoseCardsTurn(challenger);
            //notify
            //event for the challenger view to choose the three or two cards
        }
    }

    /**
     * updates current game Turn to NormalTurn
     * @param nextPlayer
     */
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
        ModelEvent evt = new NewTurnModelEvent(nextPlayer, TurnPhase.NORMAL, null);
        emitEvent(evt);
        checkHasLost();

    }

    /**
     * moves on to the next Turn
     */
    public void nextTurn(){
        Player nextPlayer = this.getNextPlayer();
        nextTurn(nextPlayer);
    }

    /**
     * updates next Turn based on current TurnPhase
     * @param nextPlayer
     */
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

    }

    /**
     * updates current Turn to PlaceWorkersTurn
     * @param player new current Player
     */
    private void startPlaceWorkersTurn(Player player){
        turn=new PlaceWorkersTurn(player);
        ModelEvent evt = new NewTurnModelEvent(player, TurnPhase.PLACE_WORKERS, null);
        emitEvent(evt);
    }

    /**
     * updates current Turn to ChoseCardsTurn
     * @param player
     */
    private void startChoseCardsTurn(Player player){
        turn = new ChoseCardsTurn(player);
        emitEvent(new NewTurnModelEvent(player, TurnPhase.CHOSE_CARDS, players));
        List<String> old_chosenCards = getChosenCardsNames();
        //it is FUNDAMENTAL that NuwTurnModelEvent is emitted before
        if(chosenCards==null) {
            //initialize here to distinguish first ChoseCardsTurn from last (is first when chosenCards == null)
            chosenCards = new ArrayList<>();
        }else if(chosenCards.size()==1) {
            //automatically set last card
            //setPlayerCard here to notify player before asking first player
            setPlayerCard(chosenCards.get(0).getName());
        }
        emitEvent(new ChosenCardsModelEvent(player, cardDeck.getCardNames(), old_chosenCards));
    }

    /**
     * set first player of the game, chosen from the challenger player
     * @param player
     * @return True if first player is correctly set
     */
    public boolean firstTurn(Player player) {
        Player firstPlayer = setFirstPlayer(player);
        if(firstPlayer == null){
            return false;
        }
        //emitEvent(new NewTurnModelEvent(firstPlayer, TurnPhase.PLACE_WORKERS));
        nextTurn(firstPlayer);
        //moved below
        ModelEvent evt = new FullInfoModelEvent(FullInfoModelEvent.InfoType.INIT_GAME, getCurrentPlayer(), players, board.clone());
        emitEvent(evt);
        return true;
    }


    private Player setFirstPlayer(Player player){
//        for(int i=0;i<players.size();i++)
//            if(players.get(i).getNickName().equals(nickName)) {
//                this.firstPlayer = players.get(i);
//                return firstPlayer;
//            }
        for(Player p: players){
            if(p.equalsUuid(player)){
                this.firstPlayer = p;
                return p;
            }
        }
        return null;
    }

    public boolean isSetFirstPlayer(){
        return firstPlayer != null;
    }

    /**
     * generates random number in order to pick first player
     * @return
     */
    private Player pickFirstPlayer() {
            int rand = (int) Math.floor( Math.random() * (double) players.size() );
            Player randPlayer=players.get(rand);
            return randPlayer;
    }


    /**
     * gets next player from order generated
     * @return next player
     */
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

    /**
     * removed the worker situated in the start position BoardCell and moves it to the destination position BoardCell
     * @param startPosition position of the current worker
     * @param destinationPosition position where the worker is moving to
     */
    public void move(Position startPosition, Position destinationPosition){
        if(turn.getPhase() != TurnPhase.NORMAL)
            throw new IllegalTurnPhaseException();

        backupUndo();
        startTimerUndo();
        boolean isWinner = turn.isWinningMove(board,startPosition,destinationPosition);
        Position pushDestPosition = turn.boardMove(board, startPosition, destinationPosition);
        ModelEvent evt = new MoveWorkerModelEvent(getCurrentPlayer(), startPosition, destinationPosition, pushDestPosition);
        emitEvent(evt);

        if(isWinner){
            winEvent(getCurrentPlayer());
        }
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


    /**
     * updates BoardCell related to destination position
     * @param startPosition position of the current worker
     * @param destinationPosition position where the worker is building
     * @param isDome specifies if is a dome build
     */
    public void build(Position startPosition, Position destinationPosition, boolean isDome){
        if(turn.getPhase() != TurnPhase.NORMAL)
            throw new IllegalTurnPhaseException();
        backupUndo();
        startTimerUndo();
        turn.boardBuild(board, startPosition, destinationPosition, isDome);
        ModelEvent evt = new BuildWorkerModelEvent(getCurrentPlayer(), startPosition, destinationPosition, isDome);
        emitEvent(evt);
        //TODO investigate
        //checkHasLost is false beacuse the undo is still available
        //when the time for undoing is over we should check again (if currentTurn == savedTurn) for checkHasLost
        /*if(checkHasLost() && players.size()==1) {
            ModelEvent evt2 = new WinModelEvent(players.get(0));
            emitEvent(evt2);
        }*/

    }


    /**
     * updates BoardCell related to placePosition where a worker is set
     * @param placePosition position where a worker is placed
     * @return worker ID
     */
    public Optional<Integer> place(Position placePosition) {
        if(turn.getPhase() != TurnPhase.PLACE_WORKERS)
            throw new IllegalTurnPhaseException();
        backupUndo();
        startTimerUndo();

        Optional<Integer> workerId = turn.boardPlace(board, placePosition);
        if(workerId.isPresent()) {
            ModelEvent evt = new PlaceWorkerModelEvent(getCurrentPlayer(), placePosition);
            emitEvent(evt);
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

    /**
     * check if current player is in loose condition
     * @return true if current player has lost the game
     */
    public boolean checkHasLost(){

        if(hasLost()){

            //notify view
            Player currentPlayer = turn.getCurrentPlayer();
            for(int i=0; i<numWorkers; i++){
                Position workerPosition = currentPlayer.getWorkerPosition(i);
                board.removeWorker(workerPosition);
            }

            if(players.size()==2) {
                players.remove(currentPlayer);
                winEvent(players.get(0));
            }else {
                Player nextPlayer = this.getNextPlayer();
                ModelEvent evt = new PlayerDefeatModelEvent(currentPlayer, false);
                emitEvent(evt);
                emitPlayerDefeat(currentPlayer);
                players.remove(currentPlayer);
                nextTurn(nextPlayer);
            }
            //Check if player has lost then, if there is another player, emit win event


            return true;
        }
        return false;
    }

    /**
     * emit Win event
     * @param winnerPlayer player that has won the game
     */
    private void winEvent(Player winnerPlayer){
        ModelEvent winModelEvent = new WinModelEvent(winnerPlayer);
        emitEvent(winModelEvent);
        active = false;
        System.out.println("GAME OVER. "+winnerPlayer.getNickName()+" wins!");
    }

    public boolean isActive(){
        return active;
    }

    /**
     *
     * @return True if loose condition occurred on current turn
     */
    private boolean hasLost(){
        if(!isAnyPlayersWorkerNotPlaced()) {
            if (!turn.isUndoAvailable) {
                return turn.isLoseCondition(board);
            } else {
                if(turn.isLoseCondition(board)) {
                    return true;
                }
                return false;
            }
        }else{
            return false;
        }
    }

    public boolean isUndoAvailable(){
        return turn.isUndoAvailable;
    }

    /**
     * brings the game back to his previous state, before last operation occurred
     * @return true if undo is successfull
     */
    public boolean undo(){
        if(!turn.isUndoAvailable){
            return false;
        }
        //deserialize turn, players
        Turn undoTurn = undoBlob.getTurn();
        Board undoBoard = undoBlob.getBoard();
        List<Player> undoPlayers = undoBlob.getPlayers();
        if(undoTurn == null || undoBoard == null ||undoPlayers == null){
            return false;
        }
        turn = undoTurn;
        board = undoBoard;
        players = undoPlayers;
        //change isUndoAvailable in currentPlayer
        turn.isUndoAvailable = false;
        ModelEvent evt = new UndoModelEvent(getCurrentPlayer(), board, players);
        emitEvent(evt);
        return true;
    }

    /**
     * reactivates undo, setting at true available property
     */
    private void backupUndo(){
        this.undoBlob = new UndoBlob(turn, board, players);
        turn.isUndoAvailable = true;
    }

    public void regenerateUndo(){
        backupUndo();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        boolean playersEquals = true;
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

    /**
     * starts undo timer of 5 seconds
     */
    public void startTimerUndo() {
        checkUndoExecutorService();
        if(turn.undoTimer != null) {
            turn.undoTimer.cancel(true);
        }
        Turn undoTurn = turn;
        turn.undoTimer = undoExecutorService.schedule(() -> {
                if(undoTurn == null){
                    System.out.print("Revoking undo after turn has been reset");
                }
                undoTurn.isUndoAvailable = false;
                checkHasLost();
        }, 5, TimeUnit.SECONDS);
    }

    private void checkUndoExecutorService() {
        if(undoExecutorService == null){
            undoExecutorService = Executors.newScheduledThreadPool(2);
        }
    }

    /**
     * resume Game state after player disconnection
     */
    public void resumeGame(){
        //undoBlob can't be serialized
       turn.isUndoAvailable = false;
       checkHasLost();
       emitEvent(makePersistencyEvent());
    }


    private PersistencyEvent makePersistencyEvent(){
        return new PersistencyEvent(getCurrentPlayer(), getPlayers(), board, getTurnPhase());
    }

    /**
     * @return list of cloned players
     */
    public List<Player> getPlayers(){
        List<Player> clonedPlayers = new ArrayList<>();
        for(Player p : players){
            clonedPlayers.add(p.clone());
        }
        return clonedPlayers;
    }
}