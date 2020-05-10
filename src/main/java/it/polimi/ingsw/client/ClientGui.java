
package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import it.polimi.ingsw.view.event.*;

import java.util.ArrayList;
import java.util.Scanner;

public class ClientGui extends /*Observable<LobbyViewEvent>*/ ClientViewEventObservable implements Runnable {
    public boolean creatorGame;
    public String nickName;
    // ^ use a Player

    public ClientGui(){
        this.creatorGame=false;
    }

    public void generateLobbyEvent(String userName, int numPlayer) {

        //notify(new LobbyGuiEvent(userName, numPlayer));
    }


    public void generateMovementEvent(int x1, int y1, int x2, int y2){
        try {
            Position startPosition = new Position(x1, y1);
            Position destPosition = new Position(x2, y2);
//            notify(new MovementGuiEvent(startPosition, destPosition));
        }catch (PositionOutOfBoundsException e){

        }

    }

    public void generateBuildEvent(int x1, int y1, int x2, int y2){
        try {
            Position startPosition = new Position(x1, y1);
            Position destPosition = new Position(x2, y2);
//            notify(new BuildGuiEvent(startPosition, destPosition, false));
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

            if (inputs[0].equals("Name"))
                generateLobbyEvent(inputs[1], Integer.parseInt(inputs[2]));

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
                ArrayList<String> cards = new ArrayList<>();
                for(int i=1; i< inputs.length ;i++)
                    cards.add(inputs[i]);
                generateChalCardsEvent(cards);
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
        notify(new FirstPlayerViewEvent(player));
    }

    private void generateSetCardEvent(String card1) {
        notify(new CardViewEvent(card1));
    }

    private void generateChalCardsEvent(ArrayList<String> cards) {
        notify(new ChallengerCardViewEvent(cards));
    }

    private void generateUndoEven() {
        notify(new UndoViewEvent(null));
    }

    private void generateEndTurnEven() {
        notify(new EndTurnViewEvent());
    }

    private void generatePlaceEvent(int x, int y) {
        try {
            Position position = new Position(x, y);
            notify(new PlaceViewEvent(position));
        }catch (PositionOutOfBoundsException e){

        }
    }
}

