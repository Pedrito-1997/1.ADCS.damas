package es.urjccode.mastercloudapps.adcs.draughts.controllers;

import org.junit.Test;

import es.urjccode.mastercloudapps.adcs.draughts.models.Coordinate;
import es.urjccode.mastercloudapps.adcs.draughts.models.Game;
import es.urjccode.mastercloudapps.adcs.draughts.models.State;
import es.urjccode.mastercloudapps.adcs.draughts.models.Color;
import es.urjccode.mastercloudapps.adcs.draughts.models.GameBuilder;

import static org.junit.Assert.*;

public class PlayControllerTest {

    private PlayController playController;

    @Test
    public void testGivenPlayControllerWhenMoveThenOk() {
        Game game = new GameBuilder().build();
        playController = new PlayController(game, new State());
        Coordinate origin = new Coordinate(5, 0);
        Coordinate target = new Coordinate(4, 1);
        playController.move(origin, target);
        assertEquals(playController.getColor(target), Color.WHITE);
        assertFalse(game.isBlocked());
    }

    @Test
    public void testGivenPlayControllerWhenMoveWithoutPiecesThenIsBlocked() {
        Game game = new GameBuilder().rows(
            "        ",
            "        ",
            "        ",
            "        ",
            " n      ",
            "b       ",
            "        ",
            "        ").build();
        playController = new PlayController(game, new State());
        Coordinate origin = new Coordinate(5, 0);
        Coordinate target = new Coordinate(3, 2);
        playController.move(origin, target);
        assertEquals(playController.getColor(target), Color.WHITE);
        assertTrue(game.isBlocked());
    }

    @Test
    public void testGivenPlayControllerWhenMoveBadThenIsNotBlocked() {
        Game game = new GameBuilder().rows(
            "        ",
            "        ",
            "   n    ",
            "  b b   ",
            "     b  ",
            "b       ",
            "        ",
            "        ").build();
        playController = new PlayController(game, new State());
        Coordinate origin = new Coordinate(3, 2);
        Coordinate target = new Coordinate(3, 4);
        playController.move(origin, target);
        assertEquals(playController.getColor(target), Color.WHITE);
        assertTrue(playController.getPiece(new Coordinate(6, 1)) == null
            || playController.getPiece(new Coordinate(4, 1)) == null);
        assertFalse(game.isBlocked());
    }

    @Test
    public void testGivenPlayControllerWhenMoveThenIsBlocked() {
        Game game = new GameBuilder().rows(
            "        ",
            "        ",
            "        ",
            "n       ",
            " b      ",
            "        ",
            " b      ",
            "        ").build();
        playController = new PlayController(game, new State());
        Coordinate origin = new Coordinate(6, 1);
        Coordinate target = new Coordinate(5, 2);
        playController.move(origin, target);
        assertEquals(playController.getColor(target), Color.WHITE);
        assertTrue(game.isBlocked());
    }

    @Test
    public void testGivenPlayControllerWhenCancelThenOk() {
        Game game = new GameBuilder().build();
        playController = new PlayController(game, new State());
        playController.cancel();
        assertEquals(Color.BLACK, playController.getColor());
        assertFalse(game.isBlocked());
    }

    @Test
    public void testGivenPlayControllerWhenMoveWhitePieceAndCanEatThenRemoveIt() {
        Game game = new GameBuilder().rows(
            "        ",
            "  n     ",
            "   n    ",
            "  b     ",
            "     b  ",
            "b   b   ",
            "        ",
            "        ").build();
        playController = new PlayController(game, new State());
        Coordinate origin = new Coordinate(5, 0);
        Coordinate target = new Coordinate(4, 1);
        playController.move(origin, target);
        assertEquals(playController.getColor(target), Color.WHITE);
        assertNull(playController.getPiece(new Coordinate(3, 2)));
    }

    @Test
    public void testGivenPlayControllerWhenMoveBlackPieceAndCanEatThenRemoveIt() {
        Game game = new GameBuilder().rows(
            "        ",
            "        ",
            "   n    ",
            "        ",
            " b    n ",
            "b       ",
            "        ",
            "  b     ").build();
        playController = new PlayController(game, new State());
        Coordinate origin = new Coordinate(4, 1);
        Coordinate target = new Coordinate(3, 2);
        playController.move(origin, target);
        origin = new Coordinate(4, 6);
        target = new Coordinate(5, 5);
        playController.move(origin, target);
        assertNull(playController.getPiece(new Coordinate(2, 3)));
    }

    @Test
    public void testGivenPlayControllerWhenWhiteDraughtCanEatAndNotEatThenRemoveIt() {
        Game game = new GameBuilder().rows(
            "        ",
            "  n     ",
            "   n    ",
            "  B     ",
            "     b  ",
            "b   b   ",
            "        ",
            "        ").build();
        playController = new PlayController(game, new State());
        Coordinate origin = new Coordinate(5, 0);
        Coordinate target = new Coordinate(4, 1);
        playController.move(origin, target);
        assertEquals(playController.getColor(target), Color.WHITE);
        assertNull(playController.getPiece(new Coordinate(3, 2)));
    }

    @Test
    public void testGivenPlayControllerWhenBlackDraughtCanEatAndNotEatThenRemoveIt() {
        Game game = new GameBuilder().rows(
            "        ",
            "        ",
            "   N    ",
            "        ",
            " b   n  ",
            "B       ",
            "        ",
            "  b     ").build();
        playController = new PlayController(game, new State());
        Coordinate origin = new Coordinate(4, 1);
        Coordinate target = new Coordinate(3, 2);
        playController.move(origin, target);
        origin = new Coordinate(4, 5);
        target = new Coordinate(5, 4);
        playController.move(origin, target);
        assertNull(playController.getPiece(new Coordinate(2, 3)));
    }

    @Test
    public void testGivenPlayControllerWhenWhitePiecesCanEatAndNotEatThenRemoveOne() {
        Game game = new GameBuilder().rows(
            "        ",
            "  n n   ",
            "   n    ",
            "        ",
            " b      ",
            "  n     ",
            " b      ",
            "  b   b ").build();
        playController = new PlayController(game, new State());
        Coordinate origin = new Coordinate(7, 6);
        Coordinate target = new Coordinate(6, 5);
        playController.move(origin, target);
        assertTrue(playController.getPiece(new Coordinate(6, 1)) == null
            || playController.getPiece(new Coordinate(4, 1)) == null);
    }

    @Test
    public void testGivenPlayControllerWhenWhiteDraughtsCanEatAndNotEatThenRemoveOne() {
        Game game = new GameBuilder().rows(
            "        ",
            "  n n   ",
            "   n    ",
            "        ",
            " B      ",
            "  n     ",
            " B      ",
            "  b   b ").build();
        playController = new PlayController(game, new State());
        Coordinate origin = new Coordinate(7, 6);
        Coordinate target = new Coordinate(6, 5);
        playController.move(origin, target);
        assertTrue(playController.getPiece(new Coordinate(6, 1)) == null
            || playController.getPiece(new Coordinate(4, 1)) == null);
    }

}
