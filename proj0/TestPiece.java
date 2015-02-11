import static org.junit.Assert.*;
import org.junit.Test;

public class TestPiece {
	/* Missing Proper Tests for:
		- hasCaptured()
		- doneCapturing()
	*/

	@Test
	public void testRegularPiece() {
		Piece regularFire = new Piece(true, null, 1, 1, "Regular-Type");
		assertEquals(true,  regularFire.isFire());
		assertEquals(0, 	regularFire.side());
		assertEquals(false, regularFire.isKing());
		assertEquals(false, regularFire.isShield());
		assertEquals(false, regularFire.isBomb());

		Piece regularWater = new Piece(false, null, 1, 1, "Regular-Type");
		assertEquals(false, regularWater.isFire());
		assertEquals(1, 	regularWater.side());
		assertEquals(false, regularWater.isKing());
		assertEquals(false, regularWater.isShield());
		assertEquals(false, regularWater.isBomb());
	}

	@Test
	public void testShieldPiece() {
		Piece shieldFire = new Piece(true, null, 1, 1, "Shield-Type");
		assertEquals(true,  shieldFire.isFire());
		assertEquals(0, 	shieldFire.side());
		assertEquals(false, shieldFire.isKing());
		assertEquals(true,  shieldFire.isShield());
		assertEquals(false, shieldFire.isBomb());

		Piece shieldWater = new Piece(false, null, 1, 1, "Shield-Type");
		assertEquals(false, shieldWater.isFire());
		assertEquals(1, 	shieldWater.side());
		assertEquals(false, shieldWater.isKing());
		assertEquals(true,  shieldWater.isShield());
		assertEquals(false, shieldWater.isBomb());
	}

	@Test
	public void testBombPiece() {
		Piece bombFire = new Piece(true, null, 1, 1, "Bomb-Type");
		assertEquals(true,  bombFire.isFire());
		assertEquals(0, 	bombFire.side());
		assertEquals(false, bombFire.isKing());
		assertEquals(false, bombFire.isShield());
		assertEquals(true,  bombFire.isBomb());

		Piece bombWater = new Piece(false, null, 1, 1, "Bomb-Type");
		assertEquals(false, bombWater.isFire());
		assertEquals(1, 	bombWater.side());
		assertEquals(false, bombWater.isKing());
		assertEquals(false, bombWater.isShield());
		assertEquals(true,  bombWater.isBomb());
	}

	public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestPiece.class);
    }
}