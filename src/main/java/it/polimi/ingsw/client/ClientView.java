
package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;

import java.util.ArrayList;
import java.util.Scanner;

public class ClientView extends /*Observable<LobbyViewEvent>*/ ObservableGui implements Runnable {
    public boolean creatorGame;
    public String nickName;

    public ClientView(){
        this.creatorGame=false;
    }

    public void generateLobbyEvent(String userName) {

        notify(new LobbyGuiEvent(userName));
    }


    public void generateMovementEvent(int x1, int y1, int x2, int y2){
        try {
            Position startPosition = new Position(x1, y1);
            Position destPosition = new Position(x2, y2);
            notify(new MovementGuiEvent(startPosition, destPosition));
        }catch (PositionOutOfBoundsException e){

        }

    }

    public void generateBuildEvent(int x1, int y1, int x2, int y2){
        try {
            Position startPosition = new Position(x1, y1);
            Position destPosition = new Position(x2, y2);
            notify(new BuildGuiEvent(startPosition, destPosition, false));
        }catch(PositionOutOfBoundsException e){

        }
    }


    @Override
    public void run() {
        Scanner in;
        String name;
        while (true) {
            in = new Scanner(System.in);  // Create a Scanner object
            String userName = in.nextLine();  // Read user input
            String[] inputs = userName.split(",");

            if (userName.equals("Mattia") || userName.equals("Nico") || userName.equals("Fede"))
                generateLobbyEvent(userName);
            else if(inputs[0].equals("Move")) {
                int x1=Integer.parseInt(inputs[1]);
                int y1=Integer.parseInt(inputs[2]);
                int x2=Integer.parseInt(inputs[3]);
                int y2=Integer.parseInt(inputs[4]);
                generateMovementEvent(x1,y1,x2,y2);
            }else if(inputs[0].equals("Build")) {
                int x1 = Integer.parseInt(inputs[1]);
                int y1 = Integer.parseInt(inputs[2]);
                int x2 = Integer.parseInt(inputs[3]);
                int y2 = Integer.parseInt(inputs[4]);
                generateBuildEvent(x1,y1,x2,y2);
            }else if(inputs[0].equals("Place")) {
                int x=Integer.parseInt(inputs[1]);
                int y=Integer.parseInt(inputs[2]);
                generatePlaceEvent(x, y);
            }
            else if(inputs[0].equals("ChalCards")) {
                String card1=inputs[1];
                String card2=inputs[2];
                String card3=inputs[3];
                generateChalCardsEvent(card1, card2, card3);
            }
            else if(inputs[0].equals("SetCard")) {
                String card1=inputs[1];
                generateSetCardEvent(card1);
            }
            else if(inputs[0].equals("SetPlayer")) {
                String player=inputs[1];
                generateSetFirstPlayer(player);
            }
            else if(userName.equals("EndTurn"))
                generateEndTurnEven();
            else if(userName.equals("Undo"))
                generateUndoEven();
        }
    }

    private void generateSetFirstPlayer(String player) {
        notify(new FirstPlayerGuiEvent(player));
    }

    private void generateSetCardEvent(String card1) {
        notify(new SetCardGuiEvent(card1));
    }

    private void generateChalCardsEvent(String card1, String card2, String card3) {
        ArrayList<String> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);
        cards.add(card3);
        notify(new ChalCardsGuiEvent(cards));
    }

    private void generateUndoEven() {
        notify(new UndoGuiEvent());
    }

    private void generateEndTurnEven() {
        notify(new EndTurnGuiEvent());
    }

    private void generatePlaceEvent(int x, int y) {
        try {
            Position position = new Position(x, y);
            notify(new PlaceGuiEvent(position));
        }catch (PositionOutOfBoundsException e){

        }
    }
}

