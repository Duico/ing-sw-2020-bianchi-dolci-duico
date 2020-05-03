package it.polimi.ingsw.client;

import java.util.ArrayList;
import java.util.List;

public class ObservableGui {
    private final List<MovementEventListener> observersMove = new ArrayList<>();
    private final List<LobbyEventListener> observersLobby = new ArrayList<>();
    private final List<BuildEventListener> observerBuild = new ArrayList<>();
    private final List<PlaceEventListener> observerPlace = new ArrayList<>();
    private final List<EndTurnEventListener> observerEndTurn = new ArrayList<>();
    private final List<UndoGuiEventListener> observerUndo = new ArrayList<>();
    private final List<ChalCardsEventListener> observerChalCards = new ArrayList<>();
    private final List<SetCardEventListener> observerSetCard = new ArrayList<>();
    private final List<FirstPlayerEventListener> observerFirstPlayer = new ArrayList<>();

    public void addObserverFirstPlayer(FirstPlayerEventListener observer){
        synchronized (observerFirstPlayer) {
            observerFirstPlayer.add(observer);
        }
    }

    public void removeObserverFirstPlayer(FirstPlayerEventListener observer){
        synchronized (observerFirstPlayer) {
            observerFirstPlayer.remove(observer);
        }
    }

    protected void notify(FirstPlayerGuiEvent message){
        synchronized (observerFirstPlayer) {
            for(FirstPlayerEventListener observer : observerFirstPlayer){
                observer.firstPlayer(message);
            }
        }
    }

    public void addObserverSetCard(SetCardEventListener observer){
        synchronized (observerSetCard) {
            observerSetCard.add(observer);
        }
    }

    public void removeObserverSetCard(SetCardEventListener observer){
        synchronized (observerChalCards) {
            observerChalCards.remove(observer);
        }
    }

    protected void notify(SetCardGuiEvent message){
        synchronized (observerSetCard) {
            for(SetCardEventListener observer : observerSetCard){
                observer.setCard(message);
            }
        }
    }

    public void addObserverChalCards(ChalCardsEventListener observer){
        synchronized (observerChalCards) {
            observerChalCards.add(observer);
        }
    }

    public void removeObserverChalCards(ChalCardsEventListener observer){
        synchronized (observerChalCards) {
            observerChalCards.remove(observer);
        }
    }

    protected void notify(ChalCardsGuiEvent message){
        synchronized (observerChalCards) {
            for(ChalCardsEventListener observer : observerChalCards){
                observer.chalCards(message);
            }
        }
    }



    public void addObserverMove(MovementEventListener observer){
        synchronized (observersMove) {
            observersMove.add(observer);
        }
    }

    public void removeObserver(MovementEventListener observer){
        synchronized (observersMove) {
            observersMove.remove(observer);
        }
    }

    protected void notify(MovementGuiEvent message){
        synchronized (observersMove) {
            for(MovementEventListener observer : observersMove){
                observer.move(message);
            }
        }
    }

    public void addObserverBuild(BuildEventListener observer){
        synchronized (observerBuild) {
            observerBuild.add(observer);
        }
    }

    public void removeObserverBuild(BuildEventListener observer){
        synchronized (observerBuild) {
            observerBuild.remove(observer);
        }
    }

    protected void notify(BuildGuiEvent message){
        synchronized (observerBuild) {
            for(BuildEventListener observer : observerBuild){
                observer.build(message);
            }
        }
    }

    public void addObserverLobby(LobbyEventListener observer){
        synchronized (observersLobby) {
            observersLobby.add(observer);
        }
    }

    public void removeObserver(LobbyEventListener observer){
        synchronized (observersLobby) {
            observersLobby.remove(observer);
        }
    }

    protected void notify(LobbyGuiEvent message){
        synchronized (observersLobby) {
            for(LobbyEventListener observer : observersLobby){
                observer.sendLobbyEvent(message);
            }
        }
    }


    public void addObserverPlace(PlaceEventListener observer){
        synchronized (observerPlace) {
            observerPlace.add(observer);
        }
    }

    public void removeObserver(PlaceEventListener observer){
        synchronized (observerPlace) {
            observerPlace.remove(observer);
        }
    }

    protected void notify(PlaceGuiEvent message){
        synchronized (observerPlace) {
            for(PlaceEventListener observer : observerPlace){
                observer.place(message);
            }
        }
    }

    public void addObserverEndTurn(EndTurnEventListener observer){
        synchronized (observerEndTurn) {
            observerEndTurn.add(observer);
        }
    }

    public void removeObserverEndTurn(EndTurnEventListener observer){
        synchronized (observerEndTurn) {
            observerEndTurn.remove(observer);
        }
    }

    protected void notify(EndTurnGuiEvent message){
        synchronized (observerEndTurn) {
            for(EndTurnEventListener observer : observerEndTurn){
                observer.endTurn(message);
            }
        }
    }

    public void addObserverUndo(UndoGuiEventListener observer){
        synchronized (observerUndo) {
            observerUndo.add(observer);
        }
    }

    public void removeObserverUndo(UndoGuiEventListener observer){
        synchronized (observerUndo) {
            observerUndo.remove(observer);
        }
    }

    protected void notify(UndoGuiEvent message){
        synchronized (observerUndo) {
            for(UndoGuiEventListener observer : observerUndo){
                observer.undo(message);
            }
        }
    }
}
