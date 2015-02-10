import java.lang.Math;

public class Board {

	private Piece[][] boardPieces;

	private boolean firesTurn;
	private boolean madeMove;

	private int xSelected;
	private int ySelected;

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
		this.xSelected = -1;
		this.ySelected = -1;
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
		Piece wantToSelect = this.pieceAt(x,y);
		if (wantToSelect == null) {
			if (xSelected < 0) {
				return false;
			} else if (this.validMove(xSelected, ySelected, x, y)) {
				return true;
			}
		} else if (wantToSelect.isFire() != this.firesTurn) {
			return false;
		} else {
			if (xSelected < 0) {
				return true;
			} else if (!this.madeMove) {
				return true;
			}
			return false;
		}
	}

	private boolean validMove(int xi, int yi, int xf, int yf) {
		// Missing Code
		Piece selected = this.pieceAt(xi, yi);
		if (selected.isKing() || selected.isFire() && yf - yi > 0 || !selected.isFire() && yf - yi < 0) {
			int dx = abs(xf - xi);
			if (dx == 2) {
				Piece captured = this.pieceAt((xi + xf) / 2, (yi + yf) / 2);
				if (selected.isFire() == !captured.isFire()) {
					return true;
				}
				return false;
			} else if (dx == 1) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public void select(int x, int y) {
		// Missing Code
		this.madeMove = true;
		// How to know whether piece capured?
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
		this.xSelected = -1;
		this.ySelected = -1;
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