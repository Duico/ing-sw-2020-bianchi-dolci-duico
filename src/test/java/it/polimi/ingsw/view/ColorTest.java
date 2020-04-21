package it.polimi.ingsw.view;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorTest {

    @Test
    void escape() {
        Color c = Color.YELLOW_UNDERLINED;
        assertEquals("\033[4;33mCiao\u001B[0mCiao", c.escape()+"Ciao"+c.RESET+"Ciao");
    }
}