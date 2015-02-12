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
		return this.type.equals("Bomb-Type");
	}

	public boolean isShield() {
		return this.type.equals("Shield-Type");
	}

	public void move(int x, int y) {
		if (Math.abs(this.x - x) == 2) {
			this.hasCaptured = true;
			int xCaptured = (this.x + x) / 2;
			int yCaptured = (this.y + y) / 2;
			board.remove(xCaptured, yCaptured);
		}
		if (Math.abs(this.side() - 1) * 7 == y) {
			this.isCrowned = true;
		}
		if (this.isCrowned) {
			System.out.println("KING!");
		}
		this.x = x;
		this.y = y;
	}

	public boolean hasCaptured() {
		return this.hasCaptured;
	}

	public void doneCapturing() {
		if (this.hasCaptured) {
			this.hasCaptured = false;
			System.out.println(x + " " + y + " done capturing");
		}
	}
}