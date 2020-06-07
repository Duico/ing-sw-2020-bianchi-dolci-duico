package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ModelEventVisitor;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.TurnPhase;
import it.polimi.ingsw.model.event.*;

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
        guiModel.requireButtonUpdate();
    }

    @Override
    public void visit(MoveWorkerModelEvent evt) {
        System.out.println("correct move");
        guiModel.moveOnTheBoard(evt.getStartPosition(), evt.getDestinationPosition(), evt.getPushPosition());
        guiModel.requireButtonUpdate();
    }

    @Override
    public void visit(PlaceWorkerModelEvent evt) {
        Player player = evt.getPlayer();
        Position position = evt.getPlacePosition();
        guiModel.placeWorker(position, player);
        guiModel.requireButtonUpdate();
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

    @Override
    public void visit(PersistencyEvent evt) {
        System.out.print("Resuming game");
        sceneEventEmitter.emitEvent(new SceneEvent(SceneEvent.SceneType.MAIN));
        guiModel.setPlayers(evt.getPlayers());
//        guiModel.setCurrentPlayer(evt.getPlayer());
        guiModel.setBoard(evt.getBoard());
        guiModel.setTurnPhase(evt.getTurnPhase());
        guiModel.resumeGame(evt.getPlayer());
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
        Player playerDefeat = evt.getPlayer();
        if(guiModel.getMyPlayer().getUuid().equals(playerDefeat.getUuid())) {
            guiModel.setMessage("You are blocked, you lose");
            guiModel.endGameCondition();
        } else{
            guiModel.setMessage(playerDefeat.getNickName()+" lose the game!");

        }
        guiModel.playerDefeat(playerDefeat);
    }

    @Override
    public void visit(WinModelEvent evt) {
        Player winner = evt.getPlayer();
        if(guiModel.getMyPlayer().getUuid().equals(winner.getUuid())) {
            guiModel.setMessage("My best compliments, you win!");
            guiModel.endGameCondition();
        } else{
            guiModel.setMessage("You lose, "+winner.getNickName()+" wins!");
            guiModel.endGameCondition();
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
        guiModel.setPlayers(evt.getPlayers());
        guiModel.setBoard(evt.getBoard());
        guiModel.undoOnTheBoard();
        guiModel.setMessage(evt.getPlayer().getNickName()+" used the undo function.");
        guiModel.requireButtonUpdate();
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
