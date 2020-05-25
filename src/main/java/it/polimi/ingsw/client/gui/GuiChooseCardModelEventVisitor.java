package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.model.Player;
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
        if(player.getNickName().equals(GuiModel.getInstance().getUsername())) {
            Platform.runLater(()->{
                printChooseCard(cards);
            });
        }else{
            Platform.runLater(()->{
                chooseCardController.waitChooseCards();
            });
        }
    }


//        for(String card:cardDeck){
//            System.out.println(card);
//        }
//        for(String card:cards){
//            System.out.println(card);
//        }




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

    private void printChooseCard(List<String> cards){
        List<String> cardDeck = GuiModel.getInstance().getCardDeck();

        if (cards == null) { //pick cards
            if (cardDeck != null) {
                System.out.println("challenger");
                chooseCardController.setIsChallenger(true);
//                Platform.runLater(()-> {
//                chooseCardController.initCardDeck(cardDeck);
                chooseCardController.loadCards();
//                });
            } else {
                throw new RuntimeException("Both cards and cardDeck are null");
            }
        }else if(cards.size()==1){
//                GuiModel.getInstance().setChosenCard();
            if(GuiModel.getInstance().getNumPlayers()==2)
                emitViewEvent(new CardViewEvent(cards.get(0)));
            else
                    chooseCardController.showChooseFirstPlayer();
        }else if(cards.size()>1)
        {
            System.out.println("not challenger");

            chooseCardController.setIsChallenger(false);
            chooseCardController.initChosenCardsChallenger(cards);
            chooseCardController.waitLabel.setVisible(false);
            chooseCardController.loadCards();
        }
    }


}
