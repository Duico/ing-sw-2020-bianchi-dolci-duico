
package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientEventEmitter;
import it.polimi.ingsw.client.gui.event.GuiEventListener;
import it.polimi.ingsw.client.message.SignUpMessage;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import it.polimi.ingsw.view.event.*;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * class which manages GUI behaviour calling methods from MainController, ChooseCardController and LoginController,
 * and launches view events
 */
public class GuiModel extends ClientEventEmitter implements GuiEventListener {

    private SceneEvent.SceneType sceneType;

    private Player myPlayer;
    private Player currentPlayer;
    private List<Player> players= new ArrayList<>();
    private boolean endGame = false;
    private Board board = new Board();
    private boolean isAllowedToMove;
    private boolean isAllowedToBuild;
    private boolean isRequiredToMove;
    private boolean isRequiredToBuild;
    private boolean askNumPlayers = true;
    private boolean askPersistency = true;
    private LoginController loginController;
    private ChooseCardController chooseCardController;
    private MainController mainController;


    private TurnPhase turnPhase;



    public GuiModel( ){

    }

    /**
     * shows message into a message box in game scene
     * @param message message shown in Gui message box
     */
    public void setMessage(String message){
        mainController.setMessage(message);
    }


    public void setCard(Player evtPlayer, Card evtCard){
        for(Player player:players)
        {
            if(player.getUuid().equals(evtPlayer.getUuid())){
                player.setCard(evtCard);
            }
        }
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public boolean isMyTurn(){
        return currentPlayer.equalsUuid(myPlayer);
    }

    /**
     * checks if is current player's turn and asks informations about turn at server
     * in order to update game buttons
     */
    public void requireButtonUpdate(){
        if(isMyTurn()) {
            emitViewEvent(new InfoViewEvent());
        }else{
            mainController.disableAllButtons();
        }
    }

    /**
     * updates game buttons based on allowed/required to move/build properties
     * in order to allow or not player to do movements or buildings
     * @param isAllowedToMove true if current player is allowed to move at the moment
     * @param isAllowedToBuild true if current player is allowed to build at the moment
     * @param isRequiredToMove true if current player is required to move at the moment
     * @param isRequiredToBuild true if current player is required to build at the moment
     */
    public void setButtonInfo(boolean isAllowedToMove, boolean isAllowedToBuild, boolean isRequiredToMove, boolean isRequiredToBuild){
        this.isAllowedToMove = isAllowedToMove;
        this.isAllowedToBuild = isAllowedToBuild;
        this.isRequiredToMove = isRequiredToMove;
        this.isRequiredToBuild = isRequiredToBuild;
        mainController.updateButtons(isMyTurn(), turnPhase, isAllowedToMove, isAllowedToBuild, isRequiredToMove, isRequiredToBuild);
    }

//    private void clearButtonInfo(boolean isPlaceWorkers) {
////        isAllowedToMove = isAllowedToBuild = !isPlaceWorkers;
////        isRequiredToMove = isRequiredToBuild = false;
////    }

    /**
     * puts 3D worker related to a player on a Gui BoardCell
     * @param position position where worker is placed
     * @param player player which places his worker
     */
    public void placeWorker(Position position, Player player){
        board.setWorker(new Worker(), position);
        if(player.equalsUuid(myPlayer))
            mainController.placeWorker(position,true, player.getColor());
        else
            mainController.placeWorker(position,false, player.getColor());
    }


    public void setCardDeck(List<String> cardDeck){
        chooseCardController.setCardDeck(cardDeck);
    }

    public List<String> getCardDeck(){
        return chooseCardController.getCardDeck();
    }

    public boolean getEndGame(){
        return this.endGame;
    }

    public void showChooseFirstPlayer(){
        chooseCardController.showChooseFirstPlayer(getUsernames());
    }

    /**
     * @return list of usernames related to any player in the game
     */
    private List<String> getUsernames(){
        List<String> usernames= new ArrayList<>();
        for(Player player:players){
            if(!myPlayer.getUuid().equals(player.getUuid()))
                usernames.add(player.getNickName());
        }
        return usernames;
    }

    public void setIsChallenger(boolean isChallenger){
        chooseCardController.setIsChallenger(isChallenger);
    }



    public void setWaitLabelVisible(boolean isVisible){
        chooseCardController.waitLabel.setVisible(isVisible);
    }

    public void loadCards(List<String> cards){
        chooseCardController.loadCards(cards);
    }


    public Player getMyPlayer() {
        return myPlayer;
    }

    public void setMyPlayer(Player myPlayer){
        this.myPlayer=myPlayer;
    }

    public void setPlayers(List<Player> players) {
       if(players!=null){
           this.players = players;
           chooseCardController.setNumPlayers(players.size());
       }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setBoard(@NotNull Board board) {
        this.board = board;
    }

    public TurnPhase getTurnPhase() {
        return turnPhase;
    }

    public void setTurnPhase(TurnPhase turnPhase) {
        this.turnPhase = turnPhase;
    }

    /**
     * updates buttons and message Box on Game scene at every new turn
     * @param turnPhase current game's turn phase
     */
    public void newTurn(TurnPhase turnPhase){
        setTurnPhase(turnPhase);
        if(turnPhase.equals(TurnPhase.PLACE_WORKERS) || turnPhase.equals(TurnPhase.NORMAL)){
            if(players !=null) {
                mainController.displayPlayers(new ArrayList<>(players));
            }
            requireButtonUpdate();
            mainController.clearStartPosition();
        }
        if(myPlayer.equalsUuid(currentPlayer)){
            setMessage("Your turn to play");
            setDefaultOperation();
        }else{
            if(!this.endGame)
                setMessage("Wait, it's not your turn...");
        }
    }

    /**
     * removes all workers of a player from GUI board when looses the game
     * and updates left side of game scene deleting his name and card image
     * @param playerDefeat
     */
    public void playerDefeat(Player playerDefeat){

        for(int i=0;i<playerDefeat.getNumWorkers();i++) {
            board.removeWorker(playerDefeat.getWorkerPosition(i));
            mainController.removeWorker(playerDefeat.getWorkerPosition(i), playerDefeat.getColor());
        }

        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).equalsUuid(playerDefeat))
                players.remove(players.get(i));
        }


    }

    /**
     * sets current operation on main Controller related to turnphase
     */
    public void setDefaultOperation() {
        if (turnPhase.equals(TurnPhase.PLACE_WORKERS)) {
            mainController.setOperation(MainController.Operation.PLACE_WORKER);
        } else if (turnPhase.equals(TurnPhase.NORMAL)) {
            mainController.setOperation(MainController.Operation.MOVE);
        }
    }




    public LoginController getLoginController() {
        return loginController;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public ChooseCardController getChooseCardController() {
        return chooseCardController;
    }

    public void setChooseCardController(ChooseCardController chooseCardController) {
        this.chooseCardController = chooseCardController;
    }

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void waitChooseCards(){
        chooseCardController.waitChooseCards();
    }

    public void askSetUpInfo(boolean askNumPlayers, boolean askPersistency){
        this.askNumPlayers = askNumPlayers;
        this.askPersistency = askPersistency;
        loginController.askSetUpInfo(askNumPlayers, askPersistency);
    }

    public void correctSignUp(){
        loginController.correctSignUp();
    }


    public void endGameCondition(){
        this.endGame=true;
        mainController.endGame();
    }

    /**
     *  moves 3D workers on Gui Board
     * @param startPosition position where a worker is located at
     * @param destPosition position where a worker is moving to
     * @param pushPosition position where the worker already located at destination position is pushed to
     */
    public void moveOnTheBoard(Position startPosition, Position destPosition, Position pushPosition){
        if(pushPosition!=null && board.getBoardCell(destPosition).getWorker()!=null)
            mainController.moveWorker(startPosition, destPosition, pushPosition);
        else
            mainController.moveWorker(startPosition, destPosition);

        board.putWorkers(startPosition, destPosition, pushPosition);

    }


    /**
     * adds a 3D building on destPosition on Gui Board
     * @param workerPosition position where a worker is located
     * @param destPosition build position
     * @param isDome true if the worker is building a dome
     */
    public void buildOnTheBoard(Position workerPosition, Position destPosition, boolean isDome){
        board.build(workerPosition, destPosition, isDome);
        Level level = board.getBoardCell(destPosition).getLevel();
        mainController.makeBuild(destPosition, level, isDome);
    }

    /**
     * removes all 3D workers and buildings from Gui Board and then replaces them all again as they were before
     * last operation done
     */
    public void undoOnTheBoard(){
        Platform.runLater( () -> {
            mainController.clearBoard();
            placeAllBuildings();
            placeAllWorkers();
        });
    }

    /**
     * updates startPosition of main Controller at every new turn
     */
    public void failedOperation() {
        if(turnPhase.equals(TurnPhase.PLACE_WORKERS) || turnPhase.equals(TurnPhase.NORMAL)){
            mainController.clearStartPosition();
        }
    }

    /**
     * method that invoke persistency, launching game scene at his last state before being closed
     * caused to a player disconnection
     */
    public void resumeGame() {
        if(!(turnPhase.equals(TurnPhase.PLACE_WORKERS)||turnPhase.equals(TurnPhase.NORMAL))) {
            throw new RuntimeException("Trying to resume from an invalid game... quitting");
        }
        mainController.displayPlayers(new ArrayList<>(players));
        requireButtonUpdate();
        undoOnTheBoard();
        if(myPlayer.equalsUuid(currentPlayer)){
            setMessage("Resumed game: Your turn to play.");
            setDefaultOperation();
        }else{
            setMessage("Resumed game: Not your turn...");
        }

    }


    /**
     * method called for undo event or persistency
     * places all 3D workers on 3D board
     * @return true if workers are successfully placed
     */
    private boolean placeAllWorkers(){
        int count = 0;
        for(Player player: players){
            boolean isMyPlayer = player.equalsUuid(myPlayer);
            for(int i = 0; i<player.getNumWorkers(); i++) {
                Position pos = player.getWorkerPosition(i);
                if(pos!=null){
                    mainController.placeWorker(pos, isMyPlayer, player.getColor());
                    count++;
                }
            }
        }
        if(count>0) return true;
        else return false;
    }

    /**
     * method called for undo event or persistency
     * places all buildings on 3D board
     * @return true if buildings are successfully placed
     */
    private boolean placeAllBuildings(){
        for(int y=0; y<board.getHeight(); y++){
            for(int x=0; x<board.getWidth(); x++){
                Position position;
                try {
                    position = new Position(x, y);
                } catch (PositionOutOfBoundsException e) {
                    e.printStackTrace();
                    return false;
                }
                BoardCell boardCell = board.getBoardCell(position);
                mainController.drawBoardCell(position, boardCell.getLevel(), boardCell.hasDome());
            }
        }
        return true;
    }

    /**
     * shows alert message on current scene
     * @param message message to show
     */
    public void alert(String message){
        Platform.runLater(()->{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(message);
            alert.showAndWait();
        });
    }

    public SceneEvent.SceneType getSceneType() {
        return sceneType;
    }

    public void setSceneType(SceneEvent.SceneType sceneType) {
        this.sceneType = sceneType;
    }

    /**
     * emits move event to server
     * @param startPosition start position of movement
     * @param destPosition destination position of movement
     */
    @Override
    public void onMove(Position startPosition, Position destPosition) {
        emitViewEvent(new MoveViewEvent(startPosition, destPosition));
    }

    /**
     * emits build event to server
     * @param workerPosition position where a worker is located at
     * @param buildPosition position where a worker is building at
     * @param isDome true if worker is building a dome
     */
    @Override
    public void onBuild(Position workerPosition, Position buildPosition, boolean isDome) {
        emitViewEvent(new BuildViewEvent(workerPosition, buildPosition, isDome));
    }

    /**
     * emits undo event to server
     */
    @Override
    public void onUndo(){
        emitViewEvent(new UndoViewEvent());
    }

    /**
     * emits end turn event to server
     */
    @Override
    public void onEndTurn() {
        emitViewEvent(new EndTurnViewEvent());
    }

    /**
     * emits sign un event to server
     * @param username player's entered username
     * @param numPlayers number of players selected
     * @param persistency true if player wants to load a game from disk
     */
    @Override
    public void onLogin(String username, Integer numPlayers, boolean persistency) {
        if(askNumPlayers && numPlayers==null){
            System.err.print("Gui should provide numPlayers");
        }
        emitSignUp(new SignUpMessage(username, numPlayers, persistency));
    }

    /**
     * emits choose card event to server
     * @param chosenCard name of chosen card
     */
    @Override
    public void onChooseCard(String chosenCard) {
        emitViewEvent(new CardViewEvent(chosenCard));
    }

    /**
     * emits challenger card event to server
     * @param challengerCards list of cards chosen by challenger player
     */
    @Override
    public void onChallengeCards(List<String> challengerCards) {
        emitViewEvent(new ChallengerCardViewEvent(challengerCards));
    }

    /**
     * emits first player event to server
     * @param firstPlayer name of first player chosen
     */
    @Override
    public void onFirstPlayer(String firstPlayer) {
        for(Player player:players){
            if(player.getNickName().equals(firstPlayer))
                emitViewEvent(new FirstPlayerViewEvent(player));
        }
    }

    /**
     * emits place worker event to server
     * @param position position where the player wants to place his worker
     */
    @Override
    public void onPlaceWorker(Position position) {
        emitViewEvent(new PlaceViewEvent(position));
    }


}
