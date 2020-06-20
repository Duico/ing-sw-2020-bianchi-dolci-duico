//package it.polimi.ingsw.client.cli;
//import it.polimi.ingsw.model.Player;
//import it.polimi.ingsw.model.Position;
//import it.polimi.ingsw.model.TurnPhase;
//import it.polimi.ingsw.model.event.*;
//
//import java.util.List;
//
//public class CliModelEventVisitor extends Cli implements ModelEventVisitor{
//
//    @Override
//    public void visit(BuildWorkerModelEvent evt) {
//        System.out.println(Color.YELLOW_BOLD.escape("Build"));
//        Player player = evt.getPlayer();
//        Position startPosition = evt.getStartPosition();
//        Position buildPosition = evt.getDestinationPosition();
//        boolean isDome = evt.isDome();
//        cliController.buildWorker(startPosition, buildPosition, isDome);
//    }
//
//    @Override
//    public void visit(MoveWorkerModelEvent evt) {
//        System.out.println(Color.YELLOW_BOLD.escape("Move"));
//        Player player = evt.getPlayer();
//        Position startPosition = evt.getStartPosition();
//        Position destinationPosition = evt.getDestinationPosition();
//        Position pushPostion = evt.getPushPosition();
//        if(pushPostion!=null){
//            cliController.moveWorker(destinationPosition, pushPostion);
//        }
//        cliController.moveWorker(startPosition, destinationPosition);
//
//    }
//
//    @Override
//    public void visit(PlaceWorkerModelEvent evt) {
//        Player player = evt.getPlayer();
//        //evt.getPlacePosition();
//        //TEMP
//        cliController.updatePlayer(player);
//        nextOperation();
//    }
//
//    @Override
//    synchronized public void visit(ChosenCardsModelEvent evt) {
//        //System.out.println(Color.YELLOW_BOLD.escape("Chosen Cards"));
//        List<String> cardDeck = evt.getCardDeck();
//        List<String> cards = evt.getChosenCards();
//        Player player = evt.getPlayer();
////        System.err.println("ChosenCards "+evt.toString());
//
////        System.err.println(evt);
////        System.err.println("Event's player:"+ evt.getPlayer().getUuid());
////        System.err.println("My player"+cliController.getMyPlayer().getUuid());
//        cliController.setCards(cards);
//        cliController.setCardDeck(cardDeck);
////        cliController.choseCardPlayer = evt.getPlayer();
//        //TODO REMOVE
//        synchronized (cliController) {
//            cliController.isTurnOK = true;
//            cliController.notifyAll();
//        }
////        if(!player.equalsUuid(cliController.getMyPlayer())){
////            //not my turn
////            return;
////        }
////        if(cards == null){ //pick cards
////            if(cardDeck != null){
////                askChallCards(cardDeck);
////            }else{
////                //error
////            }
////        }else if(cards.size() == 1){
////            //server will automatically pick last card for you
////            //ASK firstPlayer
//////            synchronized(askFirstPlayerLock) {
//////                    while (cliController.getPlayerCard(cliController.getMyPlayer()) == null) {
//////                        askFirstPlayerLock.wait();
//////                    }
////                    askFirstPlayer();
////
////
////        }else if(cards.size() > 1){
////            askCard(cards);
////        }
//
//    }
//
//    @Override
//    public void visit(FailModelEvent evt) {
//        System.out.println(Color.YELLOW_BOLD.escape("Fail"));
//    }
//
//    @Override
//    public void visit(FullInfoModelEvent evt) {
//        cliController.board = evt.getBoard();
//        cliController.players = evt.getPlayers();
//    }
//
//    @Override
//    public void visit(NewTurnModelEvent evt) {
//        //synchronized (out) {
//            Player player = evt.getPlayer();
//            TurnPhase turnPhase = evt.getTurnPhase();
//            cliController.setTurnPhase(turnPhase);
//            cliController.setPlayersIfNotSet(evt.getPlayers());
//            cliController.setCurrentPlayer(player);
//            boolean myTurn = player.equalsUuid(cliController.getMyPlayer());
////            System.err.println("NewTurn "+evt.toString());
//
//
//
////            if (player.equalsUuid(cliController.getMyPlayer())) {
////                myTurn = true;
////                switch (turnPhase) {
////                    case CHOSE_CARDS:
////                        break;
////                    case PLACE_WORKERS:
////                    case NORMAL:
////                        //do nothing (cliController.setTurnPhase is enough)
////                        //wait for command to be input
////                        break;
////                }
////            } else {
////                myTurn = false;
////            }
//            //hasPrintedTurnMessage = true;
//            //out.notifyAll();
//            nextOperation(true, myTurn);
////            //TODO REMOVE
//            if(evt.getTurnPhase() == TurnPhase.CHOSE_CARDS){
//                cliController.isTurnOK = false;
//            }
////        //}
//    }
//
//    @Override
//    public void visit(PlayerDefeatModelEvent evt) {
//        System.out.println(Color.YELLOW_BOLD.escape("PlayerDefeat"));
//    }
//
//    @Override
//    public void visit(WinModelEvent evt) {
//        System.out.println(Color.YELLOW_BOLD.escape("Win"));
//    }
//
//    @Override
//    public void visit(SetCardModelEvent evt) {
//        cliController.setPlayerCard(evt.getPlayer(), evt.getCardName());
//
////        System.err.println("SetCard "+evt.toString());
//        lockOut( () -> {
//            CliText cliText;
//            if(cliController.getMyPlayer().equalsUuid(evt.getPlayer())){
//                cliText = CliText.SET_CARD_OWN;
//            }else{
//                cliText = CliText.SET_CARD_OTHER;
//            }
//
//            //TODO generalize
//                infoOut.println();
//                infoOut.print(cliText.toString(evt.getCardName(), evt.getPlayer().getNickName()));
//        });
////        askFirstPlayerLock.notifyAll();
//
//    }
//
//    @Override
//    public void visit(UndoModelEvent evt) {
//        System.out.println(Color.YELLOW_BOLD.escape("Undo"));
//    }
//
//}
