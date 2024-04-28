public class Cell {
	public Cell up = null;
	public boolean connectedUp = false;
	public Cell down = null;
	public boolean connectedDown = false;
	public Cell left = null;
	public boolean connectedLeft = false;
	public Cell right = null;
	public boolean connectedRight = false;
	public Cell parent = null;
	public boolean visited = false;
	public int row;
	public int col;
	public int index;

	public Cell(int row, int col, int index) {
		this.row = row;
		this.col = col;
		this.index = index;
	}

	public String toString() {
		return "(" + col + ", " + row + ") connectedUp: " + connectedUp + " connectedDown: " + connectedDown
				+ " connectedLeft: " + connectedLeft + " connectedRight: " + connectedRight + " visited: " + visited;
	}
}
