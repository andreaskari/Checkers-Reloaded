public class Piece {

	private boolean isFire;
	private Board board;
	private int x;
	private int y;
	private String type;

	private boolean isCrowned;
	private boolean hasCaptured;

	public Piece(boolean isFire, Board b, int x, int y, String type) {
		this.isFire = isFire;
		this.board = b;
		this.x = x;
		this.y = y;
		this.type = type;

		this.isCrowned = false;
		this.hasCaptured = false;
	}

	public boolean isFire() {
		return this.isFire;
	}

	public int side() {
		if (this.isFire)
			return 0;
		return 1;
	}

	public boolean isKing() {
		return this.isCrowned;
	}

	public boolean isBomb() {
		return this.type.equals("bomb");
	}

	public boolean isShield() {
		return this.type.equals("shield");
	}

	public void move(int x, int y) {
		boolean bombCaptured = false;
		if (Math.abs(this.x - x) == 2) {
			this.hasCaptured = true;
			int xCaptured = (this.x + x) / 2;
			int yCaptured = (this.y + y) / 2;
			board.remove(xCaptured, yCaptured);
			if (this.isBomb()) {
				board.remove(this.x, this.y);
				this.checkForBombCaptures(x, y);
				bombCaptured = true;
			}
		}
		if (Math.abs(this.side() - 1) * 7 == y) {
			this.isCrowned = true;
		}
		if (!bombCaptured) {
			board.place(this, x, y);
		} 
		this.x = x;
		this.y = y;
	}

	private void checkForBombCaptures(int x, int y) {
		if (x < 7) {
			if (y < 7) {
				this.explodeIfNotShield(x + 1, y + 1);
			}
			 if (y > 0) {
				this.explodeIfNotShield(x + 1, y - 1);
			}
		}
		if (x > 0) {
			if (y < 7) {
				this.explodeIfNotShield(x - 1, y + 1);
			} 
			if (y > 0) {
				this.explodeIfNotShield(x - 1, y - 1);
			}
		}
	}

	private void explodeIfNotShield(int x, int y) {
		Piece p = board.pieceAt(x, y);
		if (p != null && this.isFire() != p.isFire() && !p.isShield()) {
			board.remove(x, y);
		}
	}

	public boolean hasCaptured() {
		return this.hasCaptured;
	}

	public void doneCapturing() {
		if (this.hasCaptured) {
			this.hasCaptured = false;
		}
	}
}