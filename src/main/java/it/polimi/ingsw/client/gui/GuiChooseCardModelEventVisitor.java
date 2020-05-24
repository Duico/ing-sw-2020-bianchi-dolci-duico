package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.model.event.*;

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
//        List<String> cardDeck = evt.getCardDeck();
//        List<String> cards = evt.getChosenCards();
//        if (cards == null) { //pick cards
//            if (cardDeck != null) {
//
//            } else {
//                throw new RuntimeException("Both cards and cardDeck are null");
//            }
//        }else if(cards.size()==1){
//                Manager.getInstance().setChosenCard();
//                chooseCardController.showChooseFirstPlayer();
//        }else if(cards.size()>1)
//        {
//            chooseCardController.initGridNotChallenger(cards);
//        }
//
//        else{
//
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
//        if(evt.getTurnPhase().equals(TurnPhase.CHOSE_CARDS)){
//            //load scene chooseCard fxml
//        }
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
}
