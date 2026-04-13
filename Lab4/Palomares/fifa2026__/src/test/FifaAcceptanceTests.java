package test;
import domain.*;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;


public class FifaAcceptanceTests {
    private Fifa fifa;

    @Before
    public void setup() {
        fifa = new Fifa();
    }

    @Test
    public void shouldNotAddAParticipantWithANameAlreadyInUse() {
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

        try {
            fifa.addTeam("participant", "10", "A", "manager", "uniform", "");
            fail("Exception not thrown");
        } catch (FifaException e) {
            assertEquals(FifaException.NAME_ALREADY_USED, e.getMessage());
        }
    }

    @Test
    public void shouldNotAcceptNonNumericValuesForNumericFields() {
        try {
            fifa.addPlayer("participant", "10 minutes", "A", "1 million", "club");
            fail("Exception not thrown");
        } catch (FifaArgumentException e) {
            assertTrue(true);
        } catch (FifaException e) {
            fail("Wrong exception thrown");
        }

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
    public void shouldNotAcceptNegativeValues() {
        try {
            fifa.addPlayer("participant", "-10", "A", "-10", "club2");
            fail("Exception not thrown");
        } catch (FifaArgumentException e) {
            assertTrue(true);
        } catch (FifaException e) {
            fail("Wrong exception thrown");
        }

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
}
