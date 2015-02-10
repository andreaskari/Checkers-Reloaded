import static org.junit.Assert.*;
import org.junit.Test;

public class TestBoard {
	/* Missing Proper Tests for:
		- Double capturing
		- Bomb capture
	*/

	@Test
	public void testCreateBoardEmpty() {
		Board board = new Board(true);
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				assertEquals(null, board.pieceAt(x, y));
			}
		}
	}

	@Test
	public void testCreateBoardFull() {
		Board board = new Board(false);
		for (int i = 0; i < 8; i += 2) {
			Piece regularFire = board.pieceAt(i, 0);
			assertEquals(true,  regularFire.isFire());
			assertEquals(false, regularFire.isKing());
			assertEquals(false, regularFire.isShield());
			assertEquals(false, regularFire.isBomb());
		}

		for (int i = 1; i < 8; i += 2) {
			Piece shieldFire = board.pieceAt(i, 1);
			assertEquals(true,  shieldFire.isFire());
			assertEquals(false, shieldFire.isKing());
			assertEquals(true,  shieldFire.isShield());
			assertEquals(false, shieldFire.isBomb());
		}

		for (int i = 0; i < 8; i += 2) {
			Piece bombFire = board.pieceAt(i, 2);
			assertEquals(true,  bombFire.isFire());
			assertEquals(false, bombFire.isKing());
			assertEquals(false, bombFire.isShield());
			assertEquals(true,  bombFire.isBomb());
		}

		for (int i = 1; i < 8; i += 2) {
			Piece regularWater = board.pieceAt(i, 7);
			assertEquals(false, regularWater.isFire());
			assertEquals(false, regularWater.isKing());
			assertEquals(false, regularWater.isShield());
			assertEquals(false, regularWater.isBomb());
		}

		for (int i = 0; i < 8; i += 2) {
			Piece shieldWater = board.pieceAt(i, 6);
			assertEquals(false, shieldWater.isFire());
			assertEquals(false, shieldWater.isKing());
			assertEquals(true,  shieldWater.isShield());
			assertEquals(false, shieldWater.isBomb());
		}

		for (int i = 1; i < 8; i += 2) {
			Piece bombWater = board.pieceAt(i, 5);
			assertEquals(false, bombWater.isFire());
			assertEquals(false, bombWater.isKing());
			assertEquals(false, bombWater.isShield());
			assertEquals(true,  bombWater.isBomb());
		}
	}

	@Test
	public void testSelectMethods() {
		Board board = new Board(true);
		Piece regularFire = new Piece(true, board, 0, 0, "Regular-Type");
		Piece shieldFire  = new Piece(true, board, 1, 1, "Shield-Type");
		Piece bombFire    = new Piece(true, board, 2, 2, "Bomb-Type");

		board.place(regularFire, 0, 0);
		board.place(shieldFire,  1, 1);
		board.place(bombFire,    2, 2);

		assertEquals(true, board.canSelect(0, 0));
		board.select(0, 0);
		assertEquals(true, board.canSelect(1, 1));
		board.select(1, 1);
		assertEquals(true, board.canSelect(2, 2));
		board.select(2, 2);
		assertEquals(true, board.canSelect(3, 3));
		board.select(3, 3);

		assertEquals(true, board.pieceAt(0, 0) == regularFire);
		assertEquals(true, board.pieceAt(1, 1) == shieldFire);
		assertEquals(true, board.pieceAt(3, 3) == bombFire);
		assertEquals(null, board.pieceAt(2, 2));

		assertEquals(true, board.canEndTurn());
		board.endTurn();

		assertEquals(false, board.canSelect(0, 0));

		Piece regularWater = new Piece(false, board, 0, 2, "Regular-Type");
		Piece shieldWater  = new Piece(false, board, 1, 3, "Shield-Type");

		board.place(regularWater, 0, 2);
		board.place(shieldWater,  1, 3);

		assertEquals(true, board.canSelect(1, 3));
		board.select(1, 3);
		assertEquals(true, board.canSelect(0, 2));
		board.select(0, 2);
		assertEquals(true, board.canSelect(2, 0));
		board.select(2, 0);

		assertEquals(true, board.canEndTurn());
		board.endTurn();

		assertEquals(false, board.canSelect(1, 3));
		assertEquals(true, board.canSelect(0, 0));

		assertEquals(true, board.pieceAt(1, 3) == shieldWater);
		assertEquals(true, board.pieceAt(2, 0) == regularWater);
		assertEquals(true, board.pieceAt(0, 0) == regularFire);
		assertEquals(true, board.pieceAt(3, 3) == bombFire);
		assertEquals(null, board.pieceAt(1, 1));
		assertEquals(null, board.pieceAt(2, 2));
	}

	// @Test
	// public void testCpat

	@Test
	public void testPlace() {
		Board board = new Board(true);
		Piece regularFire = new Piece(true, board, 1, 2, "Regular-Type");
		Piece shieldFire  = new Piece(true, board, 3, 4, "Shield-Type");
		Piece bombFire    = new Piece(true, board, 5, 6, "Bomb-Type");

		Piece regularWater = new Piece(false, board, 2, 1, "Regular-Type");
		Piece shieldWater  = new Piece(false, board, 4, 3, "Shield-Type");
		Piece bombWater    = new Piece(false, board, 6, 5, "Bomb-Type");

		board.place(regularFire, 1, 2);
		board.place(shieldFire,  3, 4);
		board.place(bombFire,    5, 6);
		board.place(regularWater, 2, 1);
		board.place(shieldWater,  4, 3);
		board.place(bombWater,    6, 5);

		Piece rF = board.pieceAt(1, 2);
		assertEquals(true,  rF.isFire());
		assertEquals(false, rF.isKing());
		assertEquals(false, rF.isShield());
		assertEquals(false, rF.isBomb());

		Piece bW = board.pieceAt(6, 5);
		assertEquals(false, bW.isFire());
		assertEquals(false, bW.isKing());
		assertEquals(false, bW.isShield());
		assertEquals(true,  bW.isBomb());
	}

	@Test
	public void testRemove() {
		Board board = new Board(true);
		Piece regularFire = new Piece(true, board, 1, 2, "Regular-Type");
		Piece shieldFire  = new Piece(true, board, 3, 4, "Shield-Type");
		Piece bombFire    = new Piece(true, board, 5, 6, "Bomb-Type");

		Piece regularWater = new Piece(false, board, 2, 1, "Regular-Type");
		Piece shieldWater  = new Piece(false, board, 4, 3, "Shield-Type");
		Piece bombWater    = new Piece(false, board, 6, 5, "Bomb-Type");

		board.place(regularFire, 1, 2);
		board.place(shieldFire,  3, 4);
		board.place(bombFire,    5, 6);
		board.place(regularWater, 2, 1);
		board.place(shieldWater,  4, 3);
		board.place(bombWater,    6, 5);

		Piece rF = board.remove(1, 2);
		assertEquals(true,  rF.isFire());
		assertEquals(false, rF.isKing());
		assertEquals(false, rF.isShield());
		assertEquals(false, rF.isBomb());
		assertEquals(null,  board.pieceAt(1, 2));

		Piece bW = board.remove(6, 5);
		assertEquals(false, bW.isFire());
		assertEquals(false, bW.isKing());
		assertEquals(false, bW.isShield());
		assertEquals(true,  bW.isBomb());
		assertEquals(null,  board.pieceAt(6, 5));
	}

	@Test
	public void testWinner() {
		Board board = new Board(true);
		assertEquals("No one", board.winner());

		Piece regularFire  = new Piece(true,  board, 1, 2, "Regular-Type");
		Piece regularWater = new Piece(false, board, 2, 1, "Regular-Type");

		board.place(regularFire, 1, 1);
		assertEquals("Fire", board.winner());

		board.place(regularWater, 2, 2);
		assertEquals(null, board.winner());

		board.remove(1, 1);
		assertEquals("Water", board.winner());
	}

	public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestBoard.class);
    }
}