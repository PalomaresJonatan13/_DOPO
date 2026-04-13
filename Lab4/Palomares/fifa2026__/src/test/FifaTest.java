package test;
import domain.*;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;


public class FifaTest {
    private Fifa fifa;

    @Before
    public void setup() {
        fifa = new Fifa();
    }

    @Test
    public void shouldAddPlayer() {
        try {
            fifa.addPlayer("player1", "10", "A", "0", "club");
            Participant player = fifa.consult("player1");

            assertEquals("player1", player.name());
            try {assertEquals(10, player.minutes());}
            catch (FifaException e) {fail("Exception thrown");}
            assertEquals('A', player.position());
        } catch (FifaException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void shouldAddTeam() {
        try {
            String[][] players= {
                {"L.DIAZ", "690", "A", "760000000", "Bayer"},
                {"JAMES", "516", "M", "2200000", "Minnesota"},
                {"BORRE", "445", "A", "4400000", "Sport Club"},
                {"LUCUMI", "1250", "D", "125000000", "Bologna"},
                {"VARGAS", "1160", "P", "540000", "Atlas"}
            };
            for (String[] p : players){
                fifa.addPlayer(p[0], p[1], p[2], p[3], p[4]);
            }
            fifa.addTeam("team1", "1620", "K", "Lorenzo", "Amarill-Rojo-Azul", "L.DIAZ\nJAMES\nBORRE\nLUCUMI\nVARGAS");
            Participant team = fifa.consult("team1");

            assertEquals("team1", team.name());
            try {assertEquals(1620, team.minutes());}
            catch (FifaException e) {fail("Exception thrown");}
            assertEquals('K', team.position());
        } catch (FifaException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void shouldNotAddAPlayerWithANameAlreadyInUse() {
        try {
            fifa.addPlayer("participant", "10", "A", "0", "club");
        } catch (FifaException e) {
            fail("Exception thrown");
        }

        try {
            fifa.addPlayer("participant", "10", "A", "0", "club2");
            fail("Exception not thrown");
        } catch (FifaException e) {
            assertEquals(FifaException.NAME_ALREADY_USED, e.getMessage());
        }
    }

    @Test
    public void shouldNotAddATeamWithANameAlreadyInUse() {
        try {
            fifa.addPlayer("participant", "10", "A", "0", "club");
        } catch (FifaException e) {
            fail("Exception thrown");
        }

        try {
            fifa.addTeam("participant", "10", "A", "manager", "uniform", "");
            fail("Exception not thrown");
        } catch (FifaException e) {
            assertEquals(FifaException.NAME_ALREADY_USED, e.getMessage());
        }
    }

    @Test
    public void shouldNotAcceptNonNumericValuesForNumericFieldsWhenAddingAPlayer() {
        try {
            fifa.addPlayer("participant", "10 minutes", "A", "1 million", "club");
            fail("Exception not thrown");
        } catch (FifaArgumentException e) {
            assertTrue(true);
        } catch (FifaException e) {
            fail("Wrong exception thrown");
        }
    }

    @Test
    public void shouldNotAcceptNonNumericValuesForNumericFieldsWhenAddingATeam() {
        try {
            fifa.addTeam("participant", "10 minutes", "A", "manager", "uniform", "");
            fail("Exception not thrown");
        } catch (FifaArgumentException e) {
            assertTrue(true);
        } catch (FifaException e) {
            fail("Wrong exception thrown");
        }
    }

    @Test
    public void shouldNotAcceptNegativeValuesWhileAddingAPlayer() {
        try {
            fifa.addPlayer("participant", "-10", "A", "-10", "club2");
            fail("Exception not thrown");
        } catch (FifaArgumentException e) {
            assertTrue(true);
        } catch (FifaException e) {
            fail("Wrong exception thrown");
        }
    }

    @Test
    public void shouldNotAcceptNegativeValuesWhileAddingATeam() {
        try {
            fifa.addTeam("participant", "-10", "A", "manager", "uniform", "");
            fail("Exception not thrown");
        } catch (FifaArgumentException e) {
            assertTrue(true);
        } catch (FifaException e) {
            fail("Wrong exception thrown");
        }
    }

    @Test
    public void shouldNotAddTeamIfSomeOfItsPlayersDoNotExist() {
        try {
            fifa.addTeam("participant", "10", "A", "manager", "uniform", "player");
            fail("Exception not thrown");
        } catch (FifaException e) {
            assertEquals(FifaException.INVALID_PLAYER_FOR_TEAM, e.getMessage());
        }
    }


    /* @Test
    public void shouldAddPlayerIfThePositionIsNotASingleCharacter() {
        fifa.addPlayer("player1", "10", "ABC", "0", "club");
        Participant player = fifa.consult("player1");

        assertEquals('A', player.position());
    } */

    // player names should be an array
}
