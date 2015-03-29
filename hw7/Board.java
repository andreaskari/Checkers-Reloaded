public class Board {

    public static final int SIZE = 8;
    // You can call this variable by Board.SIZE.

	private Piece[][] pieces;
    private boolean isFireTurn;

    public Board() {
        pieces = new Piece[SIZE][SIZE];
        isFireTurn = true;
    }

    /** Makes a custom Board. Not a completely safe operation because you could do
    * some bad stuff here, but this is for the purposes of testing out hash
    * codes so let's forgive the author. 
    */
    public Board(Piece[][] pieces) {
        this.pieces = pieces;
        isFireTurn = true;
    }

	@Override
	public boolean equals(Object o) {
        if (o instanceof Board) {
            return this.hashCode() == ((Board) o).hashCode();
        }
        return false;
	}

    @Override
    public int hashCode() {
        int hash = 0;
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                Piece p = pieces[x][y];
                if (p == null) {
                    hash += x * 64 + y * 512 + 2048;
                } else {
                    hash += p.hashCode();
                }
            }
        }
        if (isFireTurn) {
            hash += 4096;
        }
        return hash;
    }
}