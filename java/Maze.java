import java.util.ArrayList;
import java.util.Random;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.Scanner;

import javax.swing.GroupLayout.Group;

public class Maze {

    private ArrayList<Cell> cells = new ArrayList<Cell>();
    private ArrayList<SetHandler> groups = new ArrayList<SetHandler>();

    public Maze(int rows, int cols) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Cell currentCell = new Cell(row, col, row * cols + col);

                if (row != 0) {
                    Cell cellAbove = cells.get((row - 1) * cols + col);
                    cellAbove.down = currentCell;
                    currentCell.up = cellAbove;
                }

                if (col != 0) {
                    Cell cellLeft = cells.get(row * cols + (col - 1));
                    cellLeft.right = currentCell;
                    currentCell.left = cellLeft;
                }

                cells.add(currentCell);
                groups.add(new SetHandler(currentCell));
            }
        }
    }

    public void makeOk() {
        /*
         * I don't know if there is a similar algorithm to this one, but I will try to explain it
         * Proof of correctness: We will create rows * cols cells, each one with a different index.
         * We will create a group for each cell, and we will add the cell to the group.
         * We will select a random group, and a random cell from that group.
         * We will select a random direction (up, down, left, right).
         * If the cell in that direction is not in the same group, we will connect the two cells and merge the two groups.
         * For this reason, we will be able to ensure that all cells are connected by one path only.
         * We will repeat the process until all cells are in the same group.
         * The proof of correctness is that we will always merge two groups that are not in the same group, so we will always merge two different groups.
         * Since all groups will have cells that are adjacent to cells in other groups, we will always merge all groups.
         */
        Random rand = new Random();
        int realGroups = groups.size();
        int maxLen = 1;
        while (maxLen != realGroups) {
            int group = rand.nextInt(groups.size());
            ArrayList<Cell> groupArrayList = groups.get(group).cellSet;
            int randomCellIndex = rand.nextInt(groupArrayList.size());
            Cell randomCell = groupArrayList.get(randomCellIndex);

            int UDLR = rand.nextInt(4);

            boolean ok = false;

            int newCellIdx = -1;

            switch (UDLR) {
                case 0:
                    if (randomCell.up != null && groups.get(randomCell.up.index).cellSet != groupArrayList) {
                        randomCell.connectedUp = true;
                        randomCell.up.connectedDown = true;
                        newCellIdx = randomCell.up.index;
                        ok = true;
                    }
                    break;
                case 1:
                    if (randomCell.down != null && groups.get(randomCell.down.index).cellSet != groupArrayList) {
                        randomCell.connectedDown = true;
                        randomCell.down.connectedUp = true;
                        newCellIdx = randomCell.down.index;
                        ok = true;
                    }
                    break;
                case 2:
                    if (randomCell.left != null && groups.get(randomCell.left.index).cellSet != groupArrayList) {
                        randomCell.connectedLeft = true;
                        randomCell.left.connectedRight = true;
                        newCellIdx = randomCell.left.index;
                        ok = true;
                    }
                    break;
                case 3:
                    if (randomCell.right != null && groups.get(randomCell.right.index).cellSet != groupArrayList) {
                        randomCell.connectedRight = true;
                        randomCell.right.connectedLeft = true;
                        newCellIdx = randomCell.right.index;
                        ok = true;
                    }
                    break;
            }

            if (!ok) {
                continue;
            }

            ArrayList<Cell> tempList = new ArrayList<Cell>();

            for (Cell cell : groupArrayList) {
                tempList.add(cell);
            }

            ArrayList<Cell> indexCellArrayList = groups.get(newCellIdx).cellSet;

            for (Cell cell : indexCellArrayList) {
                tempList.add(cell);
            }

            for (Cell cell : tempList) {
                groups.get(cell.index).cellSet = tempList;
            }

            maxLen = Math.max(maxLen, tempList.size());
        }
    }

    public ArrayList getCells() {
        return cells;
    }
}