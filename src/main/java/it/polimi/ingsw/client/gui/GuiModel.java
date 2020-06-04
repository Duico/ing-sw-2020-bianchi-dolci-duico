
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

class GuiModel extends ClientEventEmitter implements GuiEventListener {

    private SceneEvent.SceneType sceneType;

    private Player myPlayer;
    private Player currentPlayer;
    private List<Player> players= new ArrayList<>();
    private boolean endGame = false;
    private Board board = new Board();
    private boolean isAllowedToMove;
    private boolean isAllowedToBuild;
    private boolean askNumPlayers = true;
    private boolean askPersistency = true;
    private LoginController loginController;
    private ChooseCardController chooseCardController;
    private MainController mainController;


    private TurnPhase turnPhase;



    public GuiModel( ){

    }

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

    public void updateGameButtons(){
        mainController.updateButtons();
    }

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

//    public void initChosenCardsChallenger(List<String> cards){
//        chooseCardController.initChosenCardsChallenger(cards);
//    }

    public void setWaitLabelVisible(boolean isVisible){
        chooseCardController.waitLabel.setVisible(isVisible);
    }

    public void loadCards(List<String> cards){
        chooseCardController.loadCards(cards);
    }

//    public void setAskNumPlayers(boolean askNumPlayers){
//        this.askNumPlayers = askNumPlayers;
//    }

    public Player getMyPlayer() {
        return myPlayer;
    }

    public void setMyPlayer(Player myPlayer){
        this.myPlayer=myPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer){
        this.currentPlayer=currentPlayer;
    }

    public Player getCurrentPlayer(){
        return this.currentPlayer;
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

    public void newTurn(Player currentPlayer, TurnPhase turnPhase){
        setTurnPhase(turnPhase);
        if(players !=null && (turnPhase.equals(TurnPhase.PLACE_WORKERS) || turnPhase.equals(TurnPhase.NORMAL))) {
            mainController.displayPlayers(players);
        }
        if(myPlayer.equalsUuid(currentPlayer)){
            setMessage("Your turn to play");
            setDefaultOperation();
        }else{
            setMessage("Wait, it's not your turn...");
        }
    }

    public void playerDefeat(Player playerDefeat){

        for(int i=0;i<playerDefeat.getNumWorkers();i++) {
            board.removeWorker(playerDefeat.getWorkerPosition(i));
            mainController.removeWorker(playerDefeat.getWorkerPosition(i), playerDefeat.getColor());
        }

        for(int i=0;i<players.size();i++){
            if(players.get(i).equalsUuid(playerDefeat))
                players.remove(players.get(i));
        }

        mainController.displayPlayers(players);

    }

    public void setDefaultOperation() {
        if (turnPhase.equals(TurnPhase.PLACE_WORKERS)) {
            mainController.setOperation(MainController.Operation.PLACE_WORKER);
        } else if (turnPhase.equals(TurnPhase.NORMAL)) {
            updateGameButtons();
                addGameButtonEvents();
                mainController.setOperation(null);
        }
    }

    public void addGameButtonEvents(){
        mainController.addButtonEvents();
    }



    private boolean checkDistance(Position start, Position destination){
        int dx = start.getX()-destination.getX();
        int dy = start.getY()-destination.getY();
        if(dx<=1 && dx>=-1 && dy<=1 && dy>=-1)
            return true;
        return false;
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

//    public boolean isSetLoginController(){
//        return loginController !=null;
//    }
//    public boolean isSetChosecardController(){
//        return chooseCardController !=null;
//    }
//    public boolean isSetMainController(){
//        return mainController !=null;
//    }

    public void winCondition(){
        this.endGame=true;
        mainController.disableAll();

    }

    public void moveOnTheBoard(Position startPosition, Position destPosition, Position pushPosition){
        if(pushPosition!=null && board.getBoardCell(destPosition).getWorker()!=null)
            mainController.moveWorker(startPosition, destPosition, pushPosition);
        else
            mainController.moveWorker(startPosition, destPosition);

        board.putWorkers(startPosition, destPosition, pushPosition);

    }

    public void buildOnTheBoard(Position workerPosition, Position destPosition, boolean isDome){
        board.build(workerPosition, destPosition, isDome);
        Level level = board.getBoardCell(destPosition).getLevel();
        System.out.println(level);
        System.out.println(isDome);
        mainController.makeBuild(destPosition, level, isDome);
    }

    public void undoOnTheBoard(){
        Platform.runLater( () -> {
            mainController.clearBoard();
            placeAllBuildings();
            placeAllWorkers();
            //TODO infoEvent
//        mainController.updateOperationButtons(true, true);
        });
    }


    public void resumeGame() {
        if(!(turnPhase.equals(TurnPhase.PLACE_WORKERS)||turnPhase.equals(TurnPhase.NORMAL))) {
            throw new RuntimeException("Trying to resume from an invalid game... quitting");
        }
        mainController.displayPlayers(players);
        undoOnTheBoard();
        if(myPlayer.equalsUuid(currentPlayer)){
            setMessage("Resumed game: Your turn to play.");
            setDefaultOperation();
        }else{
            setMessage("Resumed game: Not your turn...");
        }

    }

    //TODO create Class workersMap
    private boolean placeAllWorkers(){
        //TO call above
        //mainController.clearBoard();
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

    @Override
    public void onMove(Position startPosition, Position destPosition) {
        emitViewEvent(new MoveViewEvent(startPosition, destPosition));
    }

    @Override
    public void onBuild(Position workerPosition, Position buildPosition, boolean isDome) {
        emitViewEvent(new BuildViewEvent(workerPosition, buildPosition, isDome));
    }

    @Override
    public void onUndo(){
        emitViewEvent(new UndoViewEvent());
    }

    @Override
    public void onEndTurn() {
        emitViewEvent(new EndTurnViewEvent());
    }

    @Override
    public void onLogin(String username, Integer numPlayers, boolean persistency) {
        if(askNumPlayers && numPlayers==null){
            System.err.print("Gui should provide numPlayers");
        }
        emitSignUp(new SignUpMessage(username, numPlayers, persistency));
    }

    @Override
    public void onChooseCard(String chosenCard) {
        emitViewEvent(new CardViewEvent(chosenCard));
    }

    @Override
    public void onChallengeCards(List<String> challengerCards) {
        emitViewEvent(new ChallengerCardViewEvent(challengerCards));
    }

    @Override
    public void onFirstPlayer(String firstPlayer) {
        for(Player player:players){
            if(player.getNickName().equals(firstPlayer))
                emitViewEvent(new FirstPlayerViewEvent(player));
        }
    }

    @Override
    public void onPlaceWorker(Position position) {
        emitViewEvent(new PlaceViewEvent(position));
    }


}
