package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.NotEnoughPlayersException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LobbyTest {
    static Lobby lobby;

    @BeforeEach
    void before() throws NotEnoughPlayersException {
        lobby = new Lobby();
//        Player player1 = new Player("player1");
//        Player player2 = new Player("player2");
//        lobby.addPlayer(player1.getNickName());
//        lobby.addPlayer(player2.getNickName());
//        lobby.startGame();
    }

    /**
     * check if there are no duplicate nicknames in the lobby
     */
    @Test
    void checkAddPlayer(){
        lobby.addPlayer("player1");
        assertTrue(lobby.addPlayer("player1")==null);
    }

    /**
     * check if there are no duplicate nicknames in the lobby
     */
    @Test
    void checkAddPlayer2(){
        lobby.addPlayer("player1");
        assertTrue(lobby.addPlayer("player2")!=null);
    }

    /**
     * check if player's nickname is allowed
     */
    @Test
    void checkValidateNickname1(){
        assertFalse(lobby.validateNickname("player.1"));
    }

    /**
     * check if player's nickname is allowed
     */
    @Test
    void checkValidateNickname2(){
        assertFalse(lobby.validateNickname(""));
    }


    //problem if try to remove player2 (last player added to list)
    @Test
    void checkEliminatePlayer(){
        lobby.addPlayer("player1");
        lobby.addPlayer("player2");
        //System.out.println(lobby.players.size());
        assertEquals(2, lobby.players.size());
        lobby.removePlayerName("player1");
        //System.out.println(lobby.players.size());
        assertEquals(1, lobby.players.size());
    }







}