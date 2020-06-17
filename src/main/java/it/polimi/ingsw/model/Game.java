package it.polimi.ingsw.model;

import it.polimi.ingsw.model.event.*;
import it.polimi.ingsw.model.exception.IllegalTurnPhaseException;

import java.io.Serializable;
import java.util.*;
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


        if (this.useCards && createCardDeck()) {
            //OK
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
        //chosenCards = new ArrayList<>();
        Player challenger = pickFirstPlayer();
        challenger.setIsChallenger(true);
        //emitEvent(new FullInfoModelEvent(challenger, players, board, cardDeck));

        initTurn(challenger);

    }


    private boolean createCardDeck() {
        try {
            this.cardDeck = new CardDeck();
            return true;
        } catch (Exception e) {
            System.err.println("Error reading XML configuration file. Playing without cards.");
            return false;
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
        //TODO DA DIRE AD ALE
        /*ModelEvent evt = new ChosenCardsModelEvent(getCurrentPlayer(), null, getChosenCardsNames());
        emitEvent(evt);*/
        return true;
    }


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
        ModelEvent evt = new NewTurnModelEvent(player, TurnPhase.PLACE_WORKERS, null);
        emitEvent(evt);
    }
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


    public boolean checkHasLost(){

        if(hasLost()){

            //notify view
            Player currentPlayer = turn.getCurrentPlayer();
            for(int i=0; i<numWorkers; i++){
                Position workerPosition = currentPlayer.getWorkerPosition(i);
                board.removeWorker(workerPosition);
            }
            players.remove(currentPlayer);
            if(players.size()==1) {
                winEvent(players.get(0));
            }else {
                Player nextPlayer = this.getNextPlayer();
                ModelEvent evt = new PlayerDefeatModelEvent(currentPlayer, false);
                emitEvent(evt);
                emitPlayerDefeat(currentPlayer);
                nextTurn(nextPlayer);
            }
            //Check if player has lost then, if there is another player, emit win event


            return true;
        }
        return false;
    }

    private void winEvent(Player winnerPlayer){
        ModelEvent winModelEvent = new WinModelEvent(winnerPlayer);
        emitEvent(winModelEvent);
        active = false;
        System.out.println("GAME OVER. "+winnerPlayer.getNickName()+" wins!");
    }

    public boolean isActive(){
        return active;
    }

    private boolean hasLost(){
        if(!isAnyPlayersWorkerNotPlaced()) {
            if (!turn.isUndoAvailable) {
                return turn.isLoseCondition(board);
            } else {
                if(turn.isLoseCondition(board)) {
                    return true;
                    /*List<Position> positions = new ArrayList<>();
                    for(int i=0;i<getCurrentPlayer().getNumWorkers();i++){
                        positions.add(getCurrentPlayer().getWorkerPosition(i));
                    }
                    ModelEvent evt = new PlayerDefeatModelEvent(getCurrentPlayer(), positions, true);
                    emitEvent(evt);
                    playerDefeat(getCurrentPlayer());*/
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

    public void resumeGame(){
       startTimerUndo();
       emitEvent(makePersistencyEvent());
    }
    private PersistencyEvent makePersistencyEvent(){
        return new PersistencyEvent(getCurrentPlayer(), getPlayers(), board, getTurnPhase());
    }

    public List<Player> getPlayers(){
        List<Player> clonedPlayers = new ArrayList<>();
        for(Player p : players){
            clonedPlayers.add(p.clone());
        }
        return clonedPlayers;
    }
}