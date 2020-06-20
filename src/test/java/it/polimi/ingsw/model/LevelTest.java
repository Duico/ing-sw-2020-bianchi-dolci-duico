package it.polimi.ingsw.model;

import it.polimi.ingsw.client.gui.BuildingHeight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevelTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void fromIntToLevel() {
        assertEquals(Level.fromIntToLevel(0), Level.EMPTY);
        assertEquals(Level.fromIntToLevel(1), Level.BASE);
        assertEquals(Level.fromIntToLevel(2), Level.MID);
        assertEquals(Level.fromIntToLevel(3), Level.TOP);

    }

    @Test
    void fromLevelToHeight() {
        assertEquals(Level.fromLevelToBuildingHeight(Level.fromIntToLevel(0)), BuildingHeight.EMPTY);
        assertEquals(Level.fromLevelToBuildingHeight(Level.fromIntToLevel(1)), BuildingHeight.BASE);
        assertEquals(Level.fromLevelToBuildingHeight(Level.fromIntToLevel(2)), BuildingHeight.MID);
        assertEquals(Level.fromLevelToBuildingHeight(Level.fromIntToLevel(3)), BuildingHeight.TOP);

    }
}