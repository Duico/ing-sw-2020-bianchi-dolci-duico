package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TurnPhase;
import it.polimi.ingsw.model.event.*;
import it.polimi.ingsw.view.event.CardViewEvent;
import javafx.application.Platform;

import java.util.List;

public class GuiChooseCardModelEventVisitor extends GuiModelEventVisitor {

    private final ChooseCardController chooseCardController;

    public GuiChooseCardModelEventVisitor(ChooseCardController chooseCardController) {
        this.chooseCardController = chooseCardController;
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
        List<String> cardDeck = evt.getCardDeck();
        List<String> cards = evt.getChosenCards();
        Player player= evt.getPlayer();
        GuiModel.getInstance().setCardDeck(cardDeck);
        if(player.getNickName().equals(GuiModel.getInstance().getCurrentUsername())) {
            Platform.runLater(()->{
                printChooseCard(cards);
            });
        }else{
            Platform.runLater(()->{
                chooseCardController.waitChooseCards();
            });
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
        if(evt.getTurnPhase().equals(TurnPhase.PLACE_WORKERS)){
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

    private void printChooseCard(List<String> cards){
        List<String> cardDeck = GuiModel.getInstance().getCardDeck();

        if (cards == null) { //pick cards
            if (cardDeck != null) {
                chooseCardController.setIsChallenger(true);
                chooseCardController.loadCards();
            } else {
                throw new RuntimeException("Both cards and cardDeck are null");
            }
        }else if(cards.size()==1){
//                GuiModel.getInstance().setChosenCard();
            GuiModel.getInstance().setCurrentCard(cards.get(0));
            emitViewEvent(new CardViewEvent(cards.get(0)));
            if(GuiModel.getInstance().getNumPlayers()==2)
            {
                //emitViewEvent(new FirstPlayerViewEvent)
            }
            else
                    chooseCardController.showChooseFirstPlayer();
        }else if(cards.size()>1)
        {
            chooseCardController.setIsChallenger(false);
            chooseCardController.initChosenCardsChallenger(cards);
            chooseCardController.waitLabel.setVisible(false);
            chooseCardController.loadCards();
        }
    }


}
