package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ModelEventVisitor;
import it.polimi.ingsw.model.TurnPhase;
import it.polimi.ingsw.model.event.*;
import javafx.scene.Scene;

public class GuiModelEventVisitor extends ModelEventVisitor {
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

    }

    @Override
    public void visit(ChosenCardsModelEvent evt) {
//        List<String> cardDeck = evt.getCardDeck();
//        List<String> cards = evt.getChosenCards();
//        Player player= evt.getPlayer();
//        GuiModel.getInstance().setCardDeck(cardDeck);
//        if(player.getNickName().equals(GuiModel.getInstance().getCurrentUsername())) {
//            Platform.runLater(()->{
//                printChooseCard(cards);
//            });
//        }else{
//            Platform.runLater(()->{
//                chooseCardController.waitChooseCards();
//            });
//        }
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

    }

    @Override
    public void visit(UndoModelEvent evt) {

    }

    //
//    private void printChooseCard(List<String> cards){
//        List<String> cardDeck = GuiModel.getInstance().getCardDeck();
//
//        if (cards == null) { //pick cards
//            if (cardDeck != null) {
//                chooseCardController.setIsChallenger(true);
//                chooseCardController.loadCards();
//            } else {
//                throw new RuntimeException("Both cards and cardDeck are null");
//            }
//        }else if(cards.size()==1){
////                GuiModel.getInstance().setChosenCard();
//            GuiModel.getInstance().setCurrentCard(cards.get(0));
//            emitViewEvent(new CardViewEvent(cards.get(0)));
//            if(GuiModel.getInstance().getNumPlayers()==2)
//            {
//                //emitViewEvent(new FirstPlayerViewEvent)
//            }
//            else
//                    chooseCardController.showChooseFirstPlayer();
//        }else if(cards.size()>1)
//        {
//            chooseCardController.setIsChallenger(false);
//            chooseCardController.initChosenCardsChallenger(cards);
//            chooseCardController.waitLabel.setVisible(false);
//            chooseCardController.loadCards();
//        }
//    }
//
}
