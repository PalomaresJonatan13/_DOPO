package tests;

import domain.*;
import domain.DopoHardestGame.GameMode;
import domain.enemies.Enemy;
import domain.players.*;
import domain.exceptions.DOPOException;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;

import java.util.*;
import java.io.*;

public class DopoHardestGameTest {
    private DopoHardestGame game;

    private static final String MAPS = "src/tests/testMaps/";
    private static final double BASE_SPEED = 0.075;
    private static final double SIDE_LENGTH = 0.6;
    private static final double DELTA = 0.0001;

    /* @Before
    public void setUp() {
        game = new DopoHardestGame();
    } */

    @After
    public void tearDown() {
        game = null;
    }

    /* public void assertInitialState(int expectedWidth, int expectedHeight, GameMode expectedGameMode, double expectedTimeRemaining) {
        assertNotNull(game);

        assertFalse(game.isPaused());
        assertFalse(game.isGameOver());
        assertFalse(game.isVictory());
        assertEquals(expectedGameMode, game.getGameMode());
        assertEquals(expectedTimeRemaining, game.getTimeRemaining(), 0.001);
    } */

    private void assertGameObjectsState(String expectedState, List<GameObject> gameObjects) {
        assertEquals(expectedState, gameObjects.stream()
            .map((obj) -> obj.toString())
            .reduce((a, b) -> a + ";" + b)
            .orElse("")
        );
    }

    public void assertAllGameObjectsState(DopoHardestGame game, String expectedPlayers, String expectedEnemies, String expectedCoins, String expectedCells, String expectedSpecialObjects) {
        assertGameObjectsState(expectedPlayers, new ArrayList<>(game.getPlayers()));
        assertGameObjectsState(expectedEnemies, new ArrayList<>(game.getEnemies()));
        assertGameObjectsState(expectedCoins, new ArrayList<>(game.getCoins()));
        assertGameObjectsState(expectedCells, new ArrayList<>(game.getCells()));
        assertGameObjectsState(expectedSpecialObjects, new ArrayList<>(game.getSpecialObjects()));
    }

    public void assertGameState(DopoHardestGame game, GameMode expectedGameMode, String expectedPlayers, String expectedEnemies, String expectedCoins, String expectedCells, String expectedSpecialObjects, String expectedDimensions, double expectedTimeRemaining, boolean expectedPaused, boolean expectedGameOver, boolean expectedVictory) {
        assertNotNull(game);
        assertEquals(expectedGameMode, game.getGameMode());
        assertAllGameObjectsState(game, expectedPlayers, expectedEnemies, expectedCoins, expectedCells, expectedSpecialObjects);

        assertEquals(expectedDimensions, String.format("%d,%d", game.getWidth(), game.getHeight()));
        assertEquals(expectedPaused, game.isPaused());
        assertEquals(expectedGameOver, game.isGameOver());
        assertEquals(expectedVictory, game.isVictory());
        assertEquals(expectedTimeRemaining, game.getTimeRemaining(), 0.001);
    }

    // ==================== EXISTING TESTS ====================

    @Test
    public void shouldThrowExceptionWhenLoadingNullGameFile() {
        try {
            new DopoHardestGame(null);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void shouldThrowExceptionWhenLoadingGameFileWithWrongExtension() {
        try {
            new DopoHardestGame(new File("invalid.txt"));
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid saved game file. Only .dopo files are admitted.", e.getMessage());
        }
    }

    @Test
    public void shouldThrowExceptionWithMessageWhenLoadingMapFileWithWrongExtension() {
        try {
            new DopoHardestGame(new File("notamap.dopo"), GameMode.PLAYER);
            fail("Expected a DOPOException to be thrown");
        } catch (DOPOException e) {
            assertEquals("Invalid map file. Only .txt files are admitted.", e.getMessage());
        } catch (Exception e) {
            fail("Expected DOPOException but got: " + e.getClass().getName());
        }
    }

    /* @Test
    public void shouldLoadValidGameFileCorrectly() {
        try {
            new DopoHardestGame(new File("valid.dopo"));
        } catch (IllegalArgumentException e) {
            fail("Expected no exception to be thrown");
        }
    } */

    // ==================== MAP LOADING TESTS ====================

    @Test
    public void shouldThrowExceptionWhenLoadingNullMapFile() {
        try {
            new DopoHardestGame((File) null, GameMode.PLAYER);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Expected IllegalArgumentException but got: " + e.getClass().getName());
        }
    }

    @Test
    public void shouldThrowExceptionWhenLoadingMapFileWithWrongExtension() {
        try {
            new DopoHardestGame(new File("notamap.dopo"), GameMode.PLAYER);
            fail("Expected an exception to be thrown");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void shouldThrowExceptionWhenLoadingAMapWithIncorrectNameOfGameObjectType() {
        try {
            new DopoHardestGame(
                new File(MAPS + "shouldThrowExceptionWhenLoadingAMapWithIncorrectNameOfGameObjectType.txt"),
                GameMode.PLAYER
            );
            fail("Expected an exception to be thrown");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void shouldThrowExceptionWhenLoadingAMapWithIncorrectFormatForTypesOfGameObject() {
        try {
            new DopoHardestGame(
                new File(MAPS + "shouldThrowExceptionWhenLoadingAMapWithIncorrectFormatForTypesOfGameObject.txt"),
                GameMode.PLAYER
            );
            fail("Expected an exception to be thrown");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void shouldThrowExceptionWhenLoadingAMapWithIncorrectNumberOfCoordinatesForGameObject() {
        try {
            new DopoHardestGame(
                new File(MAPS + "shouldThrowExceptionWhenLoadingAMapWithIncorrectNumberOfCoordinatesForGameObject.txt"),
                GameMode.PLAYER
            );
            fail("Expected an exception to be thrown");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void shouldThrowExceptionWhenLoadingAMapWithIncorrectFormatForCoordinatesOfGameObject() {
        try {
            new DopoHardestGame(
                new File(MAPS + "shouldThrowExceptionWhenLoadingAMapWithIncorrectFormatForCoordinatesOfGameObject.txt"),
                GameMode.PLAYER
            );
            fail("Expected an exception to be thrown");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void shouldThrowExceptionWhenLoadingAMapWithIncorrectFormatForDimensionsOfGameObject() {
        try {
            new DopoHardestGame(
                new File(MAPS + "shouldThrowExceptionWhenLoadingAMapWithIncorrectFormatForDimensionsOfGameObject.txt"),
                GameMode.PLAYER
            );
            fail("Expected an exception to be thrown");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void shouldThrowExceptionWhenLoadingAMapWithIncorrectFormatForTimeRemaining() {
        try {
            new DopoHardestGame(
                new File(MAPS + "shouldThrowExceptionWhenLoadingAMapWithIncorrectFormatForTimeRemaining.txt"),
                GameMode.PLAYER
            );
            fail("Expected an exception to be thrown");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void shouldThrowExceptionWhenLoadingAMapIfTheGameModeIsNull() {
        try {
            new DopoHardestGame(new File(MAPS + "shouldLoadValidMapFileCorrectly.txt"), null);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Expected IllegalArgumentException but got: " + e.getClass().getName());
        }
    }

    @Test
    public void shouldLoadValidMapFileCorrectly() throws Exception {
        // Map: 1 enemy, 1 coin, 5 cells, 1 special object, TimeRemaining=90
        game = new DopoHardestGame(
            new File(MAPS + "shouldLoadValidMapFileCorrectly.txt"),
            GameMode.PLAYER
        );

        assertGameState(game,
            GameMode.PLAYER,
            "Player{name='P1', type=DefaultState, x=0,50, y=0,50, width=0,60, height=0,60, alive=true, coins=0, extraLives=0, checkpointX=0,50, checkpointY=0,50, deaths=0}", 
            "Enemy{type=NormalEnemy, x=2,50, y=0,50, active=true}", 
            "Coin{type=RED, x=3,50, y=0,50, active=true}", 
            "Cell{type=START, x=0,50, y=0,50, active=true};Cell{type=NORMAL, x=1,50, y=0,50, active=true};Cell{type=NORMAL, x=2,50, y=0,50, active=true};Cell{type=NORMAL, x=3,50, y=0,50, active=true};Cell{type=FINAL, x=4,50, y=0,50, active=true}", 
            "SpecialObject{type=LIFE, x=3,50, y=0,50, active=true}",
            "5,1",
            90.0,
            false,
            false,
            false
        );
    }

    // ==================== GAME FLOW TESTS ====================

    @Test
    public void shouldFinishGameIfTheTimeRunsOut() throws Exception {
        game = new DopoHardestGame(
            new File(MAPS + "shouldFinishGameIfTheTimeRunsOut.txt"),
            GameMode.PLAYER
        );
        assertFalse(game.isGameOver());
        game.update();
        assertTrue(game.isGameOver());
    }

    @Test
    public void shouldFinishGameAndDoNothingTryingToUpdateTheGameAfterTheGameIsOver() throws Exception {
        game = new DopoHardestGame(
            new File(MAPS + "shouldFinishGameAndDoNothingTryingToUpdateTheGameAfterTheGameIsOver.txt"),
            GameMode.PLAYER
        );
        game.update();
        assertTrue(game.isGameOver());
        double timeAfterGameOver = game.getTimeRemaining();

        game.update(); // should do nothing
        assertEquals(timeAfterGameOver, game.getTimeRemaining(), DELTA);
        assertTrue(game.isGameOver());
    }

    // ==================== PAUSE TESTS ====================

    @Test
    public void shouldPauseAndDoNothingTryingToUpdateTheGameWhileTheGameIsPaused() throws Exception {
        game = new DopoHardestGame(
            new File(MAPS + "shouldPauseAndDoNothingTryingToUpdateTheGameWhileTheGameIsPaused.txt"),
            GameMode.PLAYER
        );
        double timeBefore = game.getTimeRemaining();
        Player player = game.getPlayers().get(0);
        double xBefore = player.getCenterX();

        game.setPlayerDirection(0, 1, 0);
        game.togglePause();
        assertTrue(game.isPaused());

        game.update(); // must do nothing
        assertEquals(timeBefore, game.getTimeRemaining(), DELTA);
        assertEquals(xBefore, player.getCenterX(), DELTA);
    }

    @Test
    public void shouldAllowToUpdateTheGameAfterPausingAndResumingTheGame() throws Exception {
        game = new DopoHardestGame(
            new File(MAPS + "shouldAllowToUpdateTheGameAfterPausingAndResumingTheGame.txt"),
            GameMode.PLAYER
        );
        double timeBefore = game.getTimeRemaining();

        game.togglePause();
        game.update();
        assertEquals(timeBefore, game.getTimeRemaining(), DELTA);

        game.togglePause(); // resume
        game.update();
        assertEquals(timeBefore - 0.02, game.getTimeRemaining(), DELTA);
    }

    @Test
    public void shouldAllowToSetPlayerDirectionAfterPausingAndResumingTheGame() throws Exception {
        game = new DopoHardestGame(
            new File(MAPS + "shouldAllowToSetPlayerDirectionAfterPausingAndResumingTheGame.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);
        double xBefore = player.getCenterX();

        game.togglePause();
        game.setPlayerDirection(0, 1, 0);
        game.update();
        assertEquals(xBefore, player.getCenterX(), DELTA);

        game.togglePause();
        game.setPlayerDirection(0, 1, 0);
        game.update(); // player moves right
        assertTrue(player.getCenterX() > xBefore);
    }

    // ==================== PLAYER MOVEMENT TESTS ====================

    @Test
    public void shouldNotMoveThePlayerWhileUpdatingTheGameIfByMovingToTheTopItMovesOutOfTheCellsBounds() throws Exception {
        // Player at y=0.35; top edge=0.05. Moving up places top edge at -0.025, blocked
        game = new DopoHardestGame(
            new File(MAPS + "shouldNotMoveThePlayerWhileUpdatingTheGameIfByMovingToTheTopItMovesOutOfTheCellsBounds.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);
        double yBefore = player.getCenterY();

        game.setPlayerDirection(0, 0, -1);
        game.update();
        assertEquals(yBefore, player.getCenterY(), DELTA);
    }

    @Test
    public void shouldNotMoveThePlayerWhileUpdatingTheGameIfByMovingToTheRightItMovesOutOfTheCellsBounds() throws Exception {
        // Player at x=0.65; right edge=0.95. Moving right places right edge at 1.025, blocked
        game = new DopoHardestGame(
            new File(MAPS + "shouldNotMoveThePlayerWhileUpdatingTheGameIfByMovingToTheRightItMovesOutOfTheCellsBounds.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);
        double xBefore = player.getCenterX();

        game.setPlayerDirection(0, 1, 0);
        game.update();
        assertEquals(xBefore, player.getCenterX(), DELTA);
    }

    @Test
    public void shouldNotMoveThePlayerWhileUpdatingTheGameIfByMovingToTheBottomItMovesOutOfTheCellsBounds() throws Exception {
        // Player at y=0.65; bottom edge=0.95. Moving down places bottom edge at 1.025, blocked
        game = new DopoHardestGame(
            new File(MAPS + "shouldNotMoveThePlayerWhileUpdatingTheGameIfByMovingToTheBottomItMovesOutOfTheCellsBounds.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);
        double yBefore = player.getCenterY();

        game.setPlayerDirection(0, 0, 1);
        game.update();
        assertEquals(yBefore, player.getCenterY(), DELTA);
    }

    @Test
    public void shouldNotMoveThePlayerWhileUpdatingTheGameIfByMovingToTheLeftItMovesOutOfTheCellsBounds() throws Exception {
        // Player at x=0.35; left edge=0.05. Moving left places left edge at -0.025, blocked
        game = new DopoHardestGame(
            new File(MAPS + "shouldNotMoveThePlayerWhileUpdatingTheGameIfByMovingToTheLeftItMovesOutOfTheCellsBounds.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);
        double xBefore = player.getCenterX();

        game.setPlayerDirection(0, -1, 0);
        game.update();
        assertEquals(xBefore, player.getCenterX(), DELTA);
    }

    @Test
    public void shouldMoveThePlayerToTheTopIfThereIsNoCollisionWhileMoving() throws Exception {
        // 3x3 grid, player at center (1.5,1.5). Moving up, y decreases by BASE_SPEED
        game = new DopoHardestGame(
            new File(MAPS + "shouldMoveThePlayerToTheTopIfThereIsNoCollisionWhileMoving.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);
        double yBefore = player.getCenterY();

        game.setPlayerDirection(0, 0, -1);
        game.update();
        assertEquals(yBefore - BASE_SPEED, player.getCenterY(), DELTA);
    }

    @Test
    public void shouldMoveThePlayerToTheRightIfThereIsNoCollisionWhileMoving() throws Exception {
        game = new DopoHardestGame(
            new File(MAPS + "shouldMoveThePlayerToTheRightIfThereIsNoCollisionWhileMoving.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);
        double xBefore = player.getCenterX();

        game.setPlayerDirection(0, 1, 0);
        game.update();
        assertEquals(xBefore + BASE_SPEED, player.getCenterX(), DELTA);
    }

    @Test
    public void shouldMoveThePlayerToTheBottomIfThereIsNoCollisionWhileMoving() throws Exception {
        game = new DopoHardestGame(
            new File(MAPS + "shouldMoveThePlayerToTheBottomIfThereIsNoCollisionWhileMoving.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);
        double yBefore = player.getCenterY();

        game.setPlayerDirection(0, 0, 1);
        game.update();
        assertEquals(yBefore + BASE_SPEED, player.getCenterY(), DELTA);
    }

    @Test
    public void shouldMoveThePlayerToTheLeftIfThereIsNoCollisionWhileMoving() throws Exception {
        game = new DopoHardestGame(
            new File(MAPS + "shouldMoveThePlayerToTheLeftIfThereIsNoCollisionWhileMoving.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);
        double xBefore = player.getCenterX();

        game.setPlayerDirection(0, -1, 0);
        game.update();
        assertEquals(xBefore - BASE_SPEED, player.getCenterX(), DELTA);
    }

    // ==================== PLAYER STATE TESTS ====================

    @Test
    public void shouldHaveABiggerPlayerIfThePlayerIsInBlueState() throws Exception {
        // Blue coin at spawn: after update player is in BlueState, width = SIDE_LENGTH * 1.5
        game = new DopoHardestGame(
            new File(MAPS + "shouldHaveABiggerPlayerIfThePlayerIsInBlueState.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);
        assertEquals(SIDE_LENGTH, player.getWidth(), DELTA); // default before update

        game.update(); // collects blue coin
        assertEquals(1, player.getCoinCount());
        assertEquals("BlueState", player.getCurrentState().getClass().getSimpleName());
        assertEquals(SIDE_LENGTH * 1.5, player.getWidth(), DELTA);
        assertEquals(SIDE_LENGTH * 1.5, player.getHeight(), DELTA);
    }

    @Test
    public void shouldHaveASpeedOf1_5IfThePlayerIsInBlueState() throws Exception {
        // Blue coin at spawn: after update, player speed=BASE_SPEED*1.5
        game = new DopoHardestGame(
            new File(MAPS + "shouldHaveASpeedOf1_5IfThePlayerIsInBlueState.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);
        game.update();

        double xBefore = player.getCenterX();
        game.setPlayerDirection(0, 1, 0);
        game.update();
        assertEquals(xBefore + BASE_SPEED * 1.5, player.getCenterX(), DELTA);
    }

    @Test
    public void shouldHaveASpeedOf2IfThePlayerIsInGreenState() throws Exception {
        // Green coin at spawn: after update, player speed=BASE_SPEED*2
        game = new DopoHardestGame(
            new File(MAPS + "shouldHaveASpeedOf2IfThePlayerIsInGreenState.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);
        game.update(); // collects green coin

        double xBefore = player.getCenterX();
        game.setPlayerDirection(0, 1, 0);
        game.update();
        assertEquals(xBefore + BASE_SPEED * 2, player.getCenterX(), DELTA);
    }

    // ==================== PLAYER DEATH TESTS ====================

    @Test
    public void shouldResetTheCoinsCollectedByThePlayerWhenThePlayerDies() throws Exception {
        // red coin + bomb at player spawn.
        // Update: coin collected (count=1), bomb triggers death (count=0).
        game = new DopoHardestGame(
            new File(MAPS + "shouldResetTheCoinsCollectedByThePlayerWhenThePlayerDies.txt"),
            GameMode.PLAYER
        );
        game.update();

        Player player = game.getPlayers().get(0);
        assertEquals(0, player.getCoinCount());
        assertTrue(game.getCoins().get(0).isActive()); // coin was re-enabled
    }

    @Test
    public void shouldResetThePlayerToTheLastCheckpointWhenThePlayerDies() throws Exception {
        // Checkpoint at (1.5,0.5). Bomb at x=2.15, out of range initially.
        // Moving right 1 step brings player to 1.575 which overlaps bomb, then death, then respawn at checkpoint.
        game = new DopoHardestGame(
            new File(MAPS + "shouldResetThePlayerToTheLastCheckpointWhenThePlayerDies.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);
        double checkpointX = player.getCenterX();
        double checkpointY = player.getCenterY();

        game.setPlayerDirection(0, 1, 0);
        game.update(); // player moves right, hits bomb, dies, respawns at checkpoint

        assertEquals(checkpointX, player.getCenterX(), DELTA);
        assertEquals(checkpointY, player.getCenterY(), DELTA);
    }

    @Test
    public void shouldNotResetTheCoinsCollectedByThePlayerWhenThePlayerHasExtraLivesAndDies() throws Exception {
        // red coin + life + bomb at spawn.
        // Order: coin collected (count=1), life gives extraLife, bomb triggers death.
        // Extra life absorbs death, coinCount stays 1, coins NOT reset.
        game = new DopoHardestGame(
            new File(MAPS + "shouldNotResetTheCoinsCollectedByThePlayerWhenThePlayerHasExtraLivesAndDies.txt"),
            GameMode.PLAYER
        );
        game.update();

        Player player = game.getPlayers().get(0);
        assertEquals(1, player.getCoinCount());
        assertFalse(game.getCoins().get(0).isActive()); // coin was not re-enabled
    }

    @Test
    public void shouldRemoveAnExtraLifeWhenThePlayerHasExtraLivesAndDies() throws Exception {
        // life + bomb at spawn. Life gives extraLife, bomb triggers death.
        // Extra life absorbed: deaths stays 0, extraLives goes from 1 to 0.
        game = new DopoHardestGame(
            new File(MAPS + "shouldRemoveAnExtraLifeWhenThePlayerHasExtraLivesAndDies.txt"),
            GameMode.PLAYER
        );
        game.update();

        Player player = game.getPlayers().get(0);
        assertEquals(0, player.getDeaths());
        assertTrue(player.isAlive());
    }

    @Test
    public void shouldNotResetThePlayerToTheLastCheckpointWhenThePlayerIsInGreenStateAndHasTheShieldAndDies() throws Exception {
        // green coin + enemy at spawn.
        // Update 1: enemy kills (DEFAULT state), respawn at checkpoint; then coin collected, GreenState.
        // Update 2: enemy still near player, hits GREEN+shield player, shield absorbs, player STAYS in place.
        game = new DopoHardestGame(
            new File(MAPS + "shouldNotResetThePlayerToTheLastCheckpointWhenThePlayerIsInGreenStateAndHasTheShieldAndDies.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);
        game.update(); // update 1

        double xAfterFirstUpdate = player.getCenterX();
        double yAfterFirstUpdate = player.getCenterY();

        game.update(); // update 2
        assertEquals(xAfterFirstUpdate, player.getCenterX(), DELTA);
        assertEquals(yAfterFirstUpdate, player.getCenterY(), DELTA);
    }

    @Test
    public void shouldNotResetTheCoinsCollectedWhenThePlayerIsInGreenStateAndHasTheShieldAndDies() throws Exception {
        // green coin at spawn + red coin far right (uncollected) + enemy at spawn.
        // Update 1: enemy kills (count=0), green coin collected (count=1).
        // Update 2: enemy hits green+shield, shield absorbs. coinCount stays 1, resetCoins() not called.
        game = new DopoHardestGame(
            new File(MAPS + "shouldNotResetTheCoinsCollectedWhenThePlayerIsInGreenStateAndHasTheShieldAndDies.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);
        game.update(); // update 1
        game.update(); // update 2

        assertEquals(1, player.getCoinCount());
    }

    @Test
    public void shouldRemoveTheShieldWhenThePlayerIsInGreenStateAndHasTheShieldAndDies() throws Exception {
        // green coin + enemy at spawn.
        // After update 1, player is in GreenState with shield.
        // Update 2: enemy hits, shield removed.
        game = new DopoHardestGame(
            new File(MAPS + "shouldRemoveTheShieldWhenThePlayerIsInGreenStateAndHasTheShieldAndDies.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);
        game.update(); // update 1
        game.update(); // update 2

        assertFalse(((GreenState) player.getCurrentState()).hasShield());
    }

    @Test
    public void shouldChangeTheSpeedTo0_7IfThePlayerIsInGreenStateAndHasTheShieldAndDies() throws Exception {
        // green coin + enemy at spawn.
        // After update 1, player is in GreenState with shield.
        // Update 2: enemy hits, after shield is removed speed is BASE_SPEED * 0.7
        game = new DopoHardestGame(
            new File(MAPS + "shouldChangeTheSpeedTo0_7IfThePlayerIsInGreenStateAndHasTheShieldAndDies.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);
        game.update(); // update 1
        game.update(); // update 2

        double xBefore = player.getCenterX();
        game.setPlayerDirection(0, -1, 0);
        game.update();
        assertEquals(xBefore - BASE_SPEED * 0.7, player.getCenterX(), DELTA);
    }

    // ==================== PLAYER - CHECKPOINTS ====================

    @Test
    public void shouldUpdateCheckpointWhenPlayerIntersectsAStartCellOrASafeZoneCell() throws Exception {
        // player in normal cell (1.5,0.5); safezone cell at (2.5,0.5).
        // Moving right 3 steps partially enters safezone, setCheckpoint called, checkpoint = (2.5, 0.5).
        // Dying after that: player respawns at (2.5, 0.5).
        game = new DopoHardestGame(
            new File(MAPS + "shouldUpdateCheckpointWhenPlayerIntersectsAStartCellOrASafeZoneCell.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);

        game.setPlayerDirection(0, 1, 0);
        game.update();
        game.update();
        game.update(); // after 3 steps player reaches safezone

        // Now trigger a death
        player.die();
        // checkpoint was updated
        assertEquals(2.5, player.getCenterX(), DELTA);
        assertEquals(0.5, player.getCenterY(), DELTA);
    }

    @Test
    public void shouldNotLetThePlayerDieWhenIntersectingAStartCellOrSafeZoneCell() throws Exception {
        // player in start cell; enemy at same position.
        // isPlayerInSafeZone=true, enemy check skipped, player stays alive.
        game = new DopoHardestGame(
            new File(MAPS + "shouldNotLetThePlayerDieWhenIntersectingAStartCellOrSafeZoneCell.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);
        game.update();

        assertTrue(player.isAlive());
        assertEquals(0, player.getDeaths());
    }

    @Test
    public void shouldBeVictoryWhenThePlayerIntersectsAFinalCellIfAllCoinsAreCollected() throws Exception {
        // start cell at (0.5,0.5) adjacent to final cell at (1.5,0.5). No coins.
        // Moving right 3 steps brings player into final cell; allCoinsCollected=true, victory.
        game = new DopoHardestGame(
            new File(MAPS + "shouldBeVictoryWhenThePlayerIntersectsAFinalCellIfAllCoinsAreCollected.txt"),
            GameMode.PLAYER
        );
        assertFalse(game.isVictory());
        assertTrue(game.allCoinsCollected());

        game.setPlayerDirection(0, 1, 0);
        game.update();
        game.update();
        game.update();

        assertTrue(game.isVictory());
    }

    @Test
    public void shouldNotBeVictoryWhenThePlayerIntersectsAFinalCellIfNotAllCoinsAreCollected() throws Exception {
        // red coin at (0.5,1.5) (player never reaches it moving right).
        // Player enters final cell but allCoinsCollected=false, no victory.
        game = new DopoHardestGame(
            new File(MAPS + "shouldNotBeVictoryWhenThePlayerIntersectsAFinalCellIfNotAllCoinsAreCollected.txt"),
            GameMode.PLAYER
        );
        game.setPlayerDirection(0, 1, 0);
        game.update();
        game.update();
        game.update();

        assertFalse(game.isVictory());
        assertFalse(game.allCoinsCollected());
    }

    // ==================== PLAYER - COIN COLLISIONS ====================

    @Test
    public void shouldCollectCoinWhenPlayerIntersectsAnActiveCoin() throws Exception {
        // Red coin at player spawn, collected on first update.
        game = new DopoHardestGame(
            new File(MAPS + "shouldCollectCoinWhenPlayerIntersectsAnActiveCoin.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);
        assertEquals(0, player.getCoinCount());

        game.update();
        assertEquals(1, player.getCoinCount());
    }

    @Test
    public void shouldNotCollectCoinWhenPlayerIntersectsACoinIfTheCoinIsAlreadyCollected() throws Exception {
        // Red coin at spawn: collected on update 1, inactive on update 2.
        game = new DopoHardestGame(
            new File(MAPS + "shouldNotCollectCoinWhenPlayerIntersectsACoinIfTheCoinIsAlreadyCollected.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);
        game.update(); // collects coin
        assertEquals(1, player.getCoinCount());

        game.update(); // coin already inactive
        assertEquals(1, player.getCoinCount());
    }

    @Test
    public void shouldChangeStateToGreenWhenPlayerIntersectsAGreenCoin() throws Exception {
        game = new DopoHardestGame(
            new File(MAPS + "shouldChangeStateToGreenWhenPlayerIntersectsAGreenCoin.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);
        game.update(); // collects green coin, GreenState

        // GreenState: speed = BASE_SPEED * 2, size unchanged (SIDE_LENGTH)
        assertEquals(SIDE_LENGTH, player.getWidth(), DELTA);
        double xBefore = player.getCenterX();
        game.setPlayerDirection(0, 1, 0);
        game.update();
        assertEquals(xBefore + BASE_SPEED * 2, player.getCenterX(), DELTA);
    }

    @Test
    public void shouldChangeStateToBlueWhenPlayerIntersectsABlueCoin() throws Exception {
        game = new DopoHardestGame(
            new File(MAPS + "shouldChangeStateToBlueWhenPlayerIntersectsABlueCoin.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);
        game.update(); // collects blue coin, BlueState

        // BlueState: width = SIDE_LENGTH * 1.5, speed = BASE_SPEED * 1.5
        assertEquals(SIDE_LENGTH * 1.5, player.getWidth(), DELTA);
    }

    @Test
    public void shouldChangeStateToRedWhenPlayerIntersectsARedCoin() throws Exception {
        game = new DopoHardestGame(
            new File(MAPS + "shouldChangeStateToRedWhenPlayerIntersectsARedCoin.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);
        game.update(); // collects red coin, RedState

        // RedState: speed = BASE_SPEED, size = SIDE_LENGTH
        assertEquals(SIDE_LENGTH, player.getWidth(), DELTA);
        double xBefore = player.getCenterX();
        game.setPlayerDirection(0, 1, 0);
        game.update();
        assertEquals(xBefore + BASE_SPEED, player.getCenterX(), DELTA);
    }

    @Test
    public void shouldNotChangeStateWhenPlayerIntersectsARedCoin() throws Exception {
        // Red coin used.
        // A red coin does not change the default state's size or base speed.
        game = new DopoHardestGame(
            new File(MAPS + "shouldNotChangeStateWhenPlayerIntersectsARedCoin.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);
        double widthBefore = player.getWidth();
        game.update();

        assertEquals(widthBefore, player.getWidth(), DELTA);
    }

    @Test
    public void shouldNotChangeStateWhenPlayerIntersectsACoinIfTheCoinIsAlreadyCollected() throws Exception {
        // Red coin at spawn: collected on update 1 (BlueState).
        // On update 2 the coin is inactive → no additional state change.
        game = new DopoHardestGame(
            new File(MAPS + "shouldNotChangeStateWhenPlayerIntersectsACoinIfTheCoinIsAlreadyCollected.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);
        game.update(); // collects red coin
        double widthAfterFirst = player.getWidth();

        game.update(); // coin inactive, no change
        assertEquals(widthAfterFirst, player.getWidth(), DELTA);
    }

    @Test
    public void shouldDisableTheCoinWhenPlayerIntersectsACoin() throws Exception {
        game = new DopoHardestGame(
            new File(MAPS + "shouldDisableTheCoinWhenPlayerIntersectsACoin.txt"),
            GameMode.PLAYER
        );
        Coin coin = game.getCoins().get(0);
        assertTrue(coin.isActive());

        game.update();
        assertFalse(coin.isActive());
    }

    // ==================== PLAYER - SPECIAL OBJECT COLLISIONS ====================

    @Test
    public void shouldDoNothingWhenPlayerIntersectsASpecialObjectThatIsNotActive() throws Exception {
        // Bomb at spawn: update 1 triggers it (dies). Update 2: bomb inactive, nothing happens.
        game = new DopoHardestGame(
            new File(MAPS + "shouldDoNothingWhenPlayerIntersectsASpecialObjectThatIsNotActive.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);
        game.update(); // bomb triggers → player dies and respawns; bomb disabled

        int deathsAfterFirst = player.getDeaths();
        game.update(); // bomb inactive, no further death
        assertEquals(deathsAfterFirst, player.getDeaths());
    }

    @Test
    public void shouldAddAnExtraLifeToThePlayerWhenIntersectingALifeSpecialObject() throws Exception {
        // Life object at spawn: collecting it via addExtraLife increments extraLives.
        // Verified indirectly: player dies once (extra life absorbed, deaths=0), dies again (deaths=1).
        game = new DopoHardestGame(
            new File(MAPS + "shouldAddAnExtraLifeToThePlayerWhenIntersectingALifeSpecialObject.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);
        game.update(); // life collected

        // First die
        player.die();
        assertEquals(0, player.getDeaths());

        // Second die
        player.die();
        assertEquals(1, player.getDeaths());
    }

    @Test
    public void shouldKillThePlayerWhenIntersectingAnActiveBombSpecialObject() throws Exception {
        game = new DopoHardestGame(
            new File(MAPS + "shouldKillThePlayerWhenIntersectingAnActiveBombSpecialObject.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);
        SpecialObject bomb = game.getSpecialObjects().get(0);
        assertTrue(bomb.isActive());

        game.update(); // active bomb, player dies
        assertEquals(1, player.getDeaths());
    }

    @Test
    public void shouldNotKillThePlayerWhenIntersectingAnInactiveBombSpecialObject() throws Exception {
        game = new DopoHardestGame(
            new File(MAPS + "shouldNotKillThePlayerWhenIntersectingAnInactiveBombSpecialObject.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);
        SpecialObject bomb = game.getSpecialObjects().get(0);
        bomb.disable(); // disable before any update

        game.update(); // inactive bomb, player safe
        assertEquals(0, player.getDeaths());
    }

    @Test
    public void shouldDisableTheSpecialObjectWhenPlayerIntersectsIt() throws Exception {
        game = new DopoHardestGame(
            new File(MAPS + "shouldDisableTheSpecialObjectWhenPlayerIntersectsIt.txt"),
            GameMode.PLAYER
        );
        SpecialObject life = game.getSpecialObjects().get(0);
        assertTrue(life.isActive());

        game.update(); // player collects life, object disabled
        assertFalse(life.isActive());
    }

    // ==================== ENEMY TESTS ====================

    @Test
    public void shouldKillThePlayerWhenAnActiveEnemyIntersectsThePlayer() throws Exception {
        // Enemy and player both start at (1.5,0.5) in a normal cell (no safe zone).
        // On update the enemy moves slightly but still overlaps, player dies.
        game = new DopoHardestGame(
            new File(MAPS + "shouldKillThePlayerWhenAnActiveEnemyIntersectsThePlayer.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);
        Enemy enemy = game.getEnemies().get(0);
        assertTrue(enemy.isActive());

        game.update();
        assertEquals(1, player.getDeaths());
    }

    @Test
    public void shouldNotKillThePlayerWhenAnInactiveEnemyIntersectsThePlayer() throws Exception {
        game = new DopoHardestGame(
            new File(MAPS + "shouldNotKillThePlayerWhenAnInactiveEnemyIntersectsThePlayer.txt"),
            GameMode.PLAYER
        );
        Player player = game.getPlayers().get(0);
        Enemy enemy = game.getEnemies().get(0);
        enemy.disable(); // make it inactive

        game.update(); // inactive enemy, no kill
        assertEquals(0, player.getDeaths());
    }

    @Test
    public void shouldMoveTheEnemyUpOrDownIfTheEnemyMovesVertically() throws Exception {
        // Vertical normal enemy (horizontal=false, toRightTop=false, speedY=+0.075) at (0.5,2.5)
        game = new DopoHardestGame(
            new File(MAPS + "shouldMoveTheEnemyUpOrDownIfTheEnemyMovesVertically.txt"),
            GameMode.PLAYER
        );
        Enemy enemy = game.getEnemies().get(0);
        double yBefore = enemy.getCenterY();

        game.update();
        // speedY = 0.075 (down), centerY increases
        assertEquals(yBefore + BASE_SPEED, enemy.getCenterY(), DELTA);
    }

    @Test
    public void shouldMoveTheEnemyLeftOrRightIfTheEnemyMovesHorizontally() throws Exception {
        // Horizontal normal enemy (horizontal=true, toRightTop=true, speedX=+0.075) at (2.5,0.5)
        game = new DopoHardestGame(
            new File(MAPS + "shouldMoveTheEnemyLeftOrRightIfTheEnemyMovesHorizontally.txt"),
            GameMode.PLAYER
        );
        Enemy enemy = game.getEnemies().get(0);
        double xBefore = enemy.getCenterX();

        game.update();
        assertEquals(xBefore + BASE_SPEED, enemy.getCenterX(), DELTA);
    }

    @Test
    public void shouldHaveSpeed2IfTheEnemyIsFast() throws Exception {
        // Fast enemy (horizontal=true, toRightTop=true, speedX=+0.15) at (2.5,0.5)
        game = new DopoHardestGame(
            new File(MAPS + "shouldHaveSpeed2IfTheEnemyIsFast.txt"),
            GameMode.PLAYER
        );
        Enemy enemy = game.getEnemies().get(0);
        double xBefore = enemy.getCenterX();

        game.update();
        assertEquals(xBefore + BASE_SPEED * 2, enemy.getCenterX(), DELTA);
    }

    @Test
    public void shouldMoveTheHorizontalEnemyInTheOppositeDirectionIfTheEnemyMovesOutOfTheCellsBounds() throws Exception {
        // Horizontal enemy at (2.5,0.5) moving right; cells end at x=3. After enough updates it bounces.
        game = new DopoHardestGame(
            new File(MAPS + "shouldMoveTheHorizontalEnemyInTheOppositeDirectionIfTheEnemyMovesOutOfTheCellsBounds.txt"),
            GameMode.PLAYER
        );
        Enemy enemy = game.getEnemies().get(0);

        // Run enough updates for the enemy to reach the right wall and bounce
        for (int i = 0; i < 10; i++) game.update();
        double xAfterBounce = enemy.getCenterX();

        // Run one more update; if speed reversed, enemy now moves LEFT
        game.update();
        assertTrue(enemy.getCenterX() <= xAfterBounce);
    }

    @Test
    public void shouldMoveTheVerticalEnemyInTheOppositeDirectionIfTheEnemyMovesOutOfTheCellsBounds() throws Exception {
        // Vertical enemy at (0.5,2.5) moving DOWN; cells end at y=3. After enough updates it bounces.
        game = new DopoHardestGame(
            new File(MAPS + "shouldMoveTheVerticalEnemyInTheOppositeDirectionIfTheEnemyMovesOutOfTheCellsBounds.txt"),
            GameMode.PLAYER
        );
        Enemy enemy = game.getEnemies().get(0);

        for (int i = 0; i < 10; i++) game.update();
        double yAfterBounce = enemy.getCenterY();

        game.update();
        assertTrue(enemy.getCenterY() <= yAfterBounce);
    }

    @Test
    public void shouldMoveTheEnemyInCirclesIfTheEnemyIsAPatrolEnemy() throws Exception {
        // Patrol enemy at circle-center (2.0,2.0), radius=0.5, angle=0, starts at (2.5,2.0).
        // Each update advances angle by angularSpeed=0.03 rad; after full circle enemy returns near start.
        game = new DopoHardestGame(
            new File(MAPS + "shouldMoveTheEnemyInCirclesIfTheEnemyIsAPatrolEnemy.txt"),
            GameMode.PLAYER
        );
        Enemy enemy = game.getEnemies().get(0);
        double startX = enemy.getCenterX();
        double startY = enemy.getCenterY();

        // One full circle is approximately 2π / 0.03, approximately 210 updates
        for (int i = 0; i < 210; i++) game.update();

        assertEquals(startX, enemy.getCenterX(), 0.05);
        assertEquals(startY, enemy.getCenterY(), 0.05);
    }
}
