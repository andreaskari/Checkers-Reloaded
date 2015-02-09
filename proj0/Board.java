import java.lang.Math;

public class Board {
	private Piece[][] boardPieces;

	private boolean firesTurn;
	private boolean madeMove;
	private Piece selectedPiece;


	public static void main(String[] args) {
		// Missing Code
		// StdDrawPlus.init();
	}

	public Board(boolean shouldBeEmpty) {
		this.boardPieces = new Piece[8][8];
		
		if (!shouldBeEmpty) {
			String[] pieceTypes = {"Regular-Type", "Shield-Type", "Bomb-Type"};
			boolean[] suitTypes = {true, false};

			for (int i = 0; i < 6; i++) {
				this.placeTypePiecesAtTypeIndexes(pieceTypes[i % 3], suitTypes[i / 3], i % 2,  Math.abs(i / 3 * 7 - i % 3));
			}
		}

		this.firesTurn = true;
		this.madeMove = false;
		this.selectedPiece = null;
	}

	private void placeTypePiecesAtTypeIndexes(String piece, boolean suit, int startingIndex, int y) {
		int x = startingIndex;
		for (; x < 8; x += 2) {
			Piece p = new Piece(suit, this, x, y, piece);
			this.place(p, x, y);
		}
	}

	public Piece pieceAt(int x, int y) {
		return this.boardPieces[x][y];
	}

	public boolean canSelect(int x, int y) {
		// Missing Code
		Piece p = this.pieceAt(x,y);
		if (p == null) {
			if (validMove)
		}
		if (p.isFire() != this.firesTurn) {
			return false;
		}
		return true;
	}

	private boolean validMove(int xi, int yi, int xf, int yf) {
		// Missing Code
		return true;
	}

	public void select(int x, int y) {
		// Missing Code

	}

	public void place(Piece p, int x, int y) {
		this.boardPieces[x][y] = p;
	}

	public Piece remove(int x, int y) {
		Piece removed = pieceAt(x, y);
		this.place(null, x, y);
		return removed;
	}

	public boolean canEndTurn() {
		for (Piece[] rowPieces: this.boardPieces) {
			for (Piece p: rowPieces) {
				if (p.hasCaptured()) {
					return true;
				}
			}
		}
		return madeMove;
	}

	public void endturn() {
		if (!canEndTurn()) {
			return;
		}
		for (Piece[] rowPieces: this.boardPieces) {
			for (Piece p: rowPieces) {
				if (this.firesTurn == p.isFire()) {
					p.doneCapturing();
				}
			}
		}
		this.firesTurn = !this.firesTurn;
		this.madeMove = false;
		this.selectedPiece = null;
	}

	public String winner() {
		int numFire = 0;
		int numWater = 0;
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				Piece p = this.pieceAt(x, y);
				if (p != null) {
					numFire += 1 - p.side();
					numWater += p.side();
				}
			}
		}
		if (numFire > 0 && numWater > 0) {
			return null;
		} else if (numFire > 0) {
			return "Fire";
		} else if (numWater > 0) {
			return "Water";
		} else {
			return "No one";
		}

	}
}