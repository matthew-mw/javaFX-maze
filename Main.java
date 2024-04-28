import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

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

public class Main extends Application {

    private int rows = 50;
    private int cols = 50;
    private int cellSize = 2;
    private Maze maze;

    @Override
    public void start(Stage primaryStage) {

        VBox root = new VBox();

        TextField rowsField = new TextField("The number of rows");
        TextField colsField = new TextField("The number of columns");
        TextField cellSizeField = new TextField("The size of the cells");

        Button setDimensionsButton = new Button("Set Dimensions");
        setDimensionsButton.setOnAction(e -> {
            try {
                rows = Integer.parseInt(rowsField.getText());
                cols = Integer.parseInt(colsField.getText());
                cellSize = Integer.parseInt(cellSizeField.getText());
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid input, make sure to enter integers only");
                alert.showAndWait();
            }
        });

        GridPane mazePane = new GridPane();

        Button newMazeButton = new Button("New Maze");
        newMazeButton.setOnAction(e -> {

            mazePane.getChildren().clear();
            maze = new Maze(rows, cols);
            maze.makeOk();

            ArrayList<Cell> cells = maze.getCells();

            for (int row = 0; row <= rows * 2; row++) {
                for (int col = 0; col <= cols * 2; col++) {
                    mazePane.add(
                            new Rectangle(cellSize, cellSize, row % 2 == 0 || col % 2 == 0 ? Color.BLACK : Color.WHITE),
                            col, row);
                }
            }

            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    int index = row * cols + col;
                    Cell cell = cells.get(index);
                    if (cell.connectedLeft) {
                        mazePane.add(new Rectangle(cellSize, cellSize, Color.WHITE), (col * 2), (row * 2) + 1);
                    }
                    if (cell.connectedUp) {
                        mazePane.add(new Rectangle(cellSize, cellSize, Color.WHITE), (col * 2) + 1, (row * 2));
                    }
                }
            }
        });

        Button solveMazeButton = new Button("Solve Maze (DFS)");
        solveMazeButton.setOnAction(e -> {

            if (maze == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No maze to solve");
                alert.showAndWait();
                return;
            }

            ArrayList<Cell> cells = maze.getCells();

            int count = cells.size();

            Stack<Cell> stack = new Stack<>();

            stack.push(cells.get(0));

            while (!stack.isEmpty()) {
                Cell cell = stack.pop();
                cell.visited = true;

                mazePane.add(new Rectangle(cellSize, cellSize, Color.LIGHTGRAY), (cell.col * 2) + 1,
                        (cell.row * 2) + 1);

                if (cell.index == count - 1) {
                    break;
                }

                /*
                 * Biased towards the right, then down, then left, then up
                 * (The stack is a LIFO data structure, so the order of the if statements is
                 * important)
                 */
                if (cell.connectedUp) {
                    if (!cell.up.visited) {
                        cell.up.parent = cell;
                        stack.push(cell.up);
                    } else {
                        mazePane.add(new Rectangle(cellSize, cellSize, Color.LIGHTGRAY), (cell.col * 2) + 1,
                                (cell.row * 2));
                    }
                }
                if (cell.connectedLeft) {
                    if (!cell.left.visited) {
                        cell.left.parent = cell;
                        stack.push(cell.left);
                    } else {
                        mazePane.add(new Rectangle(cellSize, cellSize, Color.LIGHTGRAY), (cell.col * 2),
                                (cell.row * 2) + 1);
                    }
                }
                if (cell.connectedDown) {
                    if (!cell.down.visited) {
                        cell.down.parent = cell;
                        stack.push(cell.down);
                    } else {
                        mazePane.add(new Rectangle(cellSize, cellSize, Color.LIGHTGRAY), (cell.col * 2) + 1,
                                (cell.row * 2) + 2);
                    }
                }
                if (cell.connectedRight) {
                    if (!cell.right.visited) {
                        cell.right.parent = cell;
                        stack.push(cell.right);
                    } else {
                        mazePane.add(new Rectangle(cellSize, cellSize, Color.LIGHTGRAY), (cell.col * 2) + 2,
                                (cell.row * 2) + 1);
                    }
                }
            }

            for (Cell c = cells.get(count - 1); c != null; c = c.parent) {
                mazePane.add(new Rectangle(cellSize, cellSize, Color.RED), (c.col * 2) + 1, (c.row * 2) + 1);
                if (c.parent == null) {
                    break;
                }
                if (c.connectedUp && c.parent.connectedDown && c.parent.down == c) {
                    mazePane.add(new Rectangle(cellSize, cellSize, Color.RED), (c.col * 2) + 1, (c.row * 2));
                }
                if (c.connectedDown && c.parent.connectedUp && c.parent.up == c) {
                    mazePane.add(new Rectangle(cellSize, cellSize, Color.RED), (c.col * 2) + 1, (c.row * 2) + 2);
                }
                if (c.connectedLeft && c.parent.connectedRight && c.parent.right == c) {
                    mazePane.add(new Rectangle(cellSize, cellSize, Color.RED), (c.col * 2), (c.row * 2) + 1);
                }
                if (c.connectedRight && c.parent.connectedLeft && c.parent.left == c) {
                    mazePane.add(new Rectangle(cellSize, cellSize, Color.RED), (c.col * 2) + 2, (c.row * 2) + 1);
                }
            }
        });

        root.getChildren().addAll(rowsField, colsField, cellSizeField, setDimensionsButton, newMazeButton,
                solveMazeButton, mazePane);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Maze Generator and Solver");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}