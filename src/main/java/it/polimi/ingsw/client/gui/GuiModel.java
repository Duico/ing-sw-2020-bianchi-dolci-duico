
package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientEventEmitter;
import it.polimi.ingsw.client.gui.event.GuiEventListener;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.TurnPhase;

import java.util.ArrayList;
import java.util.List;

public class GuiModel extends ClientEventEmitter implements GuiEventListener {
//    private String currentCard;
//    private Cell[][] board;
//    private List<String> cards= new ArrayList<>();
//    private List<String> cardDeck= new ArrayList<>();
//    private List<String> playerNames = new ArrayList<>();
    private SceneEvent.SceneType sceneType;
    private Board board;
    private Player myPlayer;
    private Player currentPlayer;
    private List<Player> players = new ArrayList<>();
    private boolean isAllowedToMove;
    private boolean isAllowedToBuild;

    private LoginController loginController;
    private ChooseCardController chooseCardController;
    private MainController mainController;

    private TurnPhase turnPhase;


    //FROM MainController

    public GuiModel( ){

    }

//    public static GuiModel getInstance(){
////        if(instance==null)
////            instance=new GuiModel();
////        return instance;
//    }


    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Player> getPlayers() {
        return players;
    }

//    public void setPlayerNames(List<Player> names){
//        for(Player player:names){
//            this.playerNames.add(player.getNickName());
//        }
//
//        this.setNumPlayers(names.size());
//    }



//    public String getCurrentUsername(){
//        return this.currentUsername;
//    }
//    public void setCurrentCard(String name){
//        this.currentCard =name;
//    }
//    public List<String> getCardDeck(){
//        return this.cardDeck;
//    }
//
//    public void setCards(List<String> cards){
//        this.cards=cards;
//    }
//
//    public List<String> getCards(){
//        return this.cards;
//    }
//    public void setCardDeck(List<String> card){
//        for(String newCard:card){
//            if(!this.cardDeck.contains(newCard))
//                cardDeck.add(newCard);
//        }
//    }
//
//    public TurnPhase getTurnPhase() {
//        return turnPhase;
//    }
//
//    public void setTurnPhase(TurnPhase turnPhase) {
//        this.turnPhase = turnPhase;
//    }
//
//
//    public String getCurrentCard(){
//        return this.currentCard;
//    }
//
//    public void addCard(String card){
//        this.cards.add(card);
//    }
//
//    public String getCard(int i){
//        return this.cards.get(i);
//    }

//    public void addPlayer(String name){
//        this.playerNames.add(name);
//    }

//    public String getPlayer(int i){
//        return this.playerNames.get(i);
//    }

//
//    private boolean checkDistance(Position start, Position destination){
//        int dx = start.getX()-destination.getX();
//        int dy = start.getY()-destination.getY();
//        if(dx<=1 && dx>=-1 && dy<=1 && dy>=-1)
//            return true;
//        return false;
//    }

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

//    public boolean isSetLoginController(){
//        return loginController !=null;
//    }
//    public boolean isSetChosecardController(){
//        return chooseCardController !=null;
//    }
//    public boolean isSetMainController(){
//        return mainController !=null;
//    }


    public SceneEvent.SceneType getSceneType() {
        return sceneType;
    }

    public void setSceneType(SceneEvent.SceneType sceneType) {
        this.sceneType = sceneType;
    }

    @Override
    public void makeMove(Position startPosition, Position destPosition) {

    }

    @Override
    public void makeBuild(Position workerPosition, Position buildPosition, boolean isDome) {

    }

    @Override
    public void makePlace(Position workerPosition) {

    }

    @Override
    public void endTurn() {

    }
}
