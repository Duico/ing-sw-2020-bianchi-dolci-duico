package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ModelEventVisitor;
import it.polimi.ingsw.client.cli.CliText;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.TurnPhase;
import it.polimi.ingsw.model.event.*;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.util.List;

public class GuiModelEventVisitor implements ModelEventVisitor {
    private final GuiModel guiModel;
    private final SceneEventEmitter sceneEventEmitter;
    public GuiModelEventVisitor(GuiModel guiModel, SceneEventEmitter sceneEventEmitter){
        super();
        this.guiModel = guiModel;
        this.sceneEventEmitter = sceneEventEmitter;
    }

    @Override
    public void visit(BuildWorkerModelEvent evt) {
        System.out.println("correctBuild");
        guiModel.buildOnTheBoard(evt.getStartPosition(), evt.getDestinationPosition(), evt.isDome());
    }

    @Override
    public void visit(MoveWorkerModelEvent evt) {
        System.out.println("correct move");
        guiModel.moveOnTheBoard(evt.getStartPosition(), evt.getDestinationPosition(), evt.getPushPosition());
    }

    @Override
    public void visit(PlaceWorkerModelEvent evt) {
        Player player = evt.getPlayer();
        Position position = evt.getPlacePosition();
        guiModel.placeWorker(position, player);
    }

    @Override
    public void visit(ChosenCardsModelEvent evt) {
        List<String> cardDeck = evt.getCardDeck();
        List<String> cards = evt.getChosenCards();
        Player player= evt.getPlayer();
        guiModel.setCardDeck(cardDeck);
        if(player.equalsUuid(guiModel.getMyPlayer())) {
            printChooseCard(cards);
        }else{
            guiModel.waitChooseCards();
        }
    }

    @Override
    public void visit(FailModelEvent evt) {

    }

    @Override
    public void visit(FullInfoModelEvent evt) {

    }

    @Override
    public void visit(NewTurnModelEvent evt) {
        TurnPhase turnPhase = evt.getTurnPhase();
        Player currentPlayer = evt.getPlayer();
        changeScene(turnPhase);
        guiModel.setPlayers(evt.getPlayers());
        guiModel.newTurn(currentPlayer, turnPhase);

    }

    private void changeScene(TurnPhase turnPhase){
        if(turnPhase.equals(TurnPhase.CHOSE_CARDS)){
            sceneEventEmitter.emitEvent(new SceneEvent(SceneEvent.SceneType.CHOSE_CARDS));
        } else if (turnPhase.equals(TurnPhase.PLACE_WORKERS) || turnPhase.equals(TurnPhase.NORMAL)) {
            sceneEventEmitter.emitEvent(new SceneEvent(SceneEvent.SceneType.MAIN));
        }
    }

    @Override
    public void visit(PlayerDefeatModelEvent evt) {

    }

    @Override
    public void visit(WinModelEvent evt) {
        Player winner = evt.getPlayer();
        if(guiModel.getMyPlayer().getUuid().equals(winner.getUuid())) {
            guiModel.setMessage("My best compliments, you are the winner!");
        } else{
            guiModel.setMessage("You lose, "+winner.getNickName()+" is the winner!");
        }
    }

    @Override
    public void visit(SetCardModelEvent evt) {
        Player player = evt.getPlayer();
        Card card = evt.getCard();
        guiModel.setCard(player, card);
    }

    @Override
    public void visit(UndoModelEvent evt) {

    }


    private void printChooseCard(List<String> cards){


        List<String> cardDeck = guiModel.getCardDeck();

        if (cards == null) { //pick cards
            if (cardDeck != null) {
                guiModel.setIsChallenger(true);
                guiModel.loadCards(cardDeck);
            } else {
                throw new RuntimeException("Both cards and cardDeck are null");
            }
        }else if(cards.size()==1){
            guiModel.showChooseFirstPlayer();
        }else if(cards.size()>1)
        {
            guiModel.setIsChallenger(false);
            guiModel.loadCards(cards);
        }
    }

//    public void alert(String message){
//        Platform.runLater(()->{
//            Alert alert = new Alert(Alert.AlertType.WARNING);
//            alert.setHeaderText(message);
//            alert.showAndWait();
//        });
//    }
}



/*public class GuiModelEventVisitor extends ModelEventVisitor {
    private final GuiModel guiModel;
    private final SceneEventEmitter sceneEventEmitter;
    public GuiModelEventVisitor(GuiModel guiModel, SceneEventEmitter sceneEventEmitter){
        super();
        this.guiModel = guiModel;
        this.sceneEventEmitter = sceneEventEmitter;
    }

    @Override
    public void visit(BuildWorkerModelEvent evt) {

    }

    @Override
    public void visit(MoveWorkerModelEvent evt) {

    }

    @Override
    public void visit(PlaceWorkerModelEvent evt) {
        Player player = evt.getPlayer();
        Position position = evt.getPlacePosition();
        guiModel.placeWorker(position, player);
    }

    @Override
    public void visit(ChosenCardsModelEvent evt) {
        List<String> cardDeck = evt.getCardDeck();
        List<String> cards = evt.getChosenCards();
        Player player= evt.getPlayer();
        guiModel.setCardDeck(cardDeck);
        if(player.equalsUuid(guiModel.getMyPlayer())) {
                printChooseCard(cards);
        }else{
                guiModel.waitChooseCards();
        }
    }

    @Override
    public void visit(FailModelEvent evt) {

    }

    @Override
    public void visit(FullInfoModelEvent evt) {

    }

    @Override
    public void visit(NewTurnModelEvent evt) {
        TurnPhase turnPhase = evt.getTurnPhase();
        guiModel.setPlayers(evt.getPlayers());
            if(turnPhase.equals(TurnPhase.CHOSE_CARDS)){
                guiModel.setTurnPhase(TurnPhase.CHOSE_CARDS);
                sceneEventEmitter.emitEvent(new SceneEvent(SceneEvent.SceneType.CHOSE_CARDS));
            } else if (turnPhase.equals(TurnPhase.PLACE_WORKERS)) {
                sceneEventEmitter.emitEvent(new SceneEvent(SceneEvent.SceneType.MAIN));
                guiModel.updatePlayers();
                guiModel.setTurnPhase(TurnPhase.PLACE_WORKERS);
            }else if(turnPhase.equals(TurnPhase.NORMAL))
            {
                //update scene buttons
            }
    }

    @Override
    public void visit(PlayerDefeatModelEvent evt) {

    }

    @Override
    public void visit(WinModelEvent evt) {

    }

    @Override
    public void visit(SetCardModelEvent evt) {
        Player player = evt.getPlayer();
        Card card = evt.getCard();
        guiModel.setCard(player, card);
    }

    @Override
    public void visit(UndoModelEvent evt) {

    }


    private void printChooseCard(List<String> cards){
        List<String> cardDeck = guiModel.getCardDeck();

        if (cards == null) { //pick cards
            if (cardDeck != null) {
                guiModel.setIsChallenger(true);
                guiModel.loadCards(cardDeck);
            } else {
                throw new RuntimeException("Both cards and cardDeck are null");
            }
        }else if(cards.size()==1){
            guiModel.showChooseFirstPlayer();
        }else if(cards.size()>1)
        {
            guiModel.setIsChallenger(false);
            guiModel.loadCards(cards);
        }
    }

}

/*public class GuiModelEventVisitor implements ModelEventVisitor {
    private final GuiModel guiModel;
    private final SceneEventEmitter sceneEventEmitter;
    public GuiModelEventVisitor(GuiModel guiModel, SceneEventEmitter sceneEventEmitter){
        super();
        this.guiModel = guiModel;
        this.sceneEventEmitter = sceneEventEmitter;
    }

    @Override
    public void visit(BuildWorkerModelEvent evt) {

    }

    @Override
    public void visit(MoveWorkerModelEvent evt) {

    }

    @Override
    public void visit(PlaceWorkerModelEvent evt) {
        Player player = evt.getPlayer();
        Position position = evt.getPlacePosition();
        guiModel.placeWorker(position, player);
    }

    @Override
    public void visit(ChosenCardsModelEvent evt) {
        List<String> cardDeck = evt.getCardDeck();
        List<String> cards = evt.getChosenCards();
        Player player= evt.getPlayer();
        guiModel.setCardDeck(cardDeck);
        if(player.equalsUuid(guiModel.getMyPlayer())) {
                printChooseCard(cards);
        }else{
                guiModel.waitChooseCards();
        }
    }

    @Override
    public void visit(FailModelEvent evt) {

    }

    @Override
    public void visit(FullInfoModelEvent evt) {

    }

    @Override
    public void visit(NewTurnModelEvent evt) {
        TurnPhase turnPhase = evt.getTurnPhase();
            if(turnPhase.equals(TurnPhase.CHOSE_CARDS)){
                guiModel.setPlayers(evt.getPlayers());
                sceneEventEmitter.emitEvent(new SceneEvent(SceneEvent.SceneType.CHOSE_CARDS));
            } else if (turnPhase.equals(TurnPhase.PLACE_WORKERS) || turnPhase.equals(TurnPhase.NORMAL)) {
                sceneEventEmitter.emitEvent(new SceneEvent(SceneEvent.SceneType.MAIN));
            }
    }

    @Override
    public void visit(PlayerDefeatModelEvent evt) {

    }

    @Override
    public void visit(WinModelEvent evt) {

    }

    @Override
    public void visit(SetCardModelEvent evt) {
        Player player = evt.getPlayer();
        Card card = evt.getCard();
        guiModel.setCard(player, card);
    }

    @Override
    public void visit(UndoModelEvent evt) {

    }


    private void printChooseCard(List<String> cards){
        List<String> cardDeck = guiModel.getCardDeck();

        if (cards == null) { //pick cards
            if (cardDeck != null) {
                guiModel.setIsChallenger(true);
                guiModel.loadCards();
            } else {
                throw new RuntimeException("Both cards and cardDeck are null");
            }
        }else if(cards.size()==1){
            guiModel.showChooseFirstPlayer();
        }else if(cards.size()>1)
        {
            guiModel.setIsChallenger(false);
            guiModel.initChosenCardsChallenger(cards);
            guiModel.setWaitLabelVisible(false);
            guiModel.loadCards();
        }
    }

}

/*public class GuiModelEventVisitor extends ModelEventVisitor {
    private final GuiModel guiModel;
    private final SceneEventEmitter sceneEventEmitter;
    public GuiModelEventVisitor(GuiModel guiModel, SceneEventEmitter sceneEventEmitter){
        super();
        this.guiModel = guiModel;
        this.sceneEventEmitter = sceneEventEmitter;
    }

    @Override
    public void visit(BuildWorkerModelEvent evt) {

    }

    @Override
    public void visit(MoveWorkerModelEvent evt) {

    }

    @Override
    public void visit(PlaceWorkerModelEvent evt) {
        Player player = evt.getPlayer();
        Position position = evt.getPlacePosition();
        guiModel.placeWorker(position, player);
    }

    @Override
    public void visit(ChosenCardsModelEvent evt) {
        List<String> cardDeck = evt.getCardDeck();
        List<String> cards = evt.getChosenCards();
        Player player= evt.getPlayer();
        guiModel.setCardDeck(cardDeck);
        if(player.equalsUuid(guiModel.getMyPlayer())) {
                printChooseCard(cards);
        }else{
                guiModel.waitChooseCards();
        }
    }

    @Override
    public void visit(FailModelEvent evt) {

    }

    @Override
    public void visit(FullInfoModelEvent evt) {

    }

    @Override
    public void visit(NewTurnModelEvent evt) {
        TurnPhase turnPhase = evt.getTurnPhase();
            if(turnPhase.equals(TurnPhase.CHOSE_CARDS)){
                guiModel.setPlayers(evt.getPlayers());
                sceneEventEmitter.emitEvent(new SceneEvent(SceneEvent.SceneType.CHOSE_CARDS));
            } else if (turnPhase.equals(TurnPhase.PLACE_WORKERS) || turnPhase.equals(TurnPhase.NORMAL)) {
                sceneEventEmitter.emitEvent(new SceneEvent(SceneEvent.SceneType.MAIN));
            }
    }

    @Override
    public void visit(PlayerDefeatModelEvent evt) {

    }

    @Override
    public void visit(WinModelEvent evt) {

    }

    @Override
    public void visit(SetCardModelEvent evt) {
        Player player = evt.getPlayer();
        Card card = evt.getCard();
        guiModel.setCard(player, card);
    }

    @Override
    public void visit(UndoModelEvent evt) {

    }


    private void printChooseCard(List<String> cards){
        List<String> cardDeck = guiModel.getCardDeck();

        if (cards == null) { //pick cards
            if (cardDeck != null) {
                guiModel.setIsChallenger(true);
                guiModel.loadCards();
            } else {
                throw new RuntimeException("Both cards and cardDeck are null");
            }
        }else if(cards.size()==1){
            guiModel.showChooseFirstPlayer();
        }else if(cards.size()>1)
        {
            guiModel.setIsChallenger(false);
            guiModel.initChosenCardsChallenger(cards);
            guiModel.setWaitLabelVisible(false);
            guiModel.loadCards();
        }
    }

}*/
