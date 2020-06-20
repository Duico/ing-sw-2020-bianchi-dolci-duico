package it.polimi.ingsw.client.cli;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorTest {

    @Test
    void escape() {
        Color c = Color.YELLOW_UNDERLINED;
        assertEquals("\033[4;33mCiao\033[0;00mCiao", c.escape("Ciao")+"Ciao");
    }
}