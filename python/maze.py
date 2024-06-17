import random
from cell import Cell
from dsu import DSU

class Maze:
    def __init__(self, rows: int, cols: int) -> None:
        self.maze = [[Cell(row, col, row * cols + col) for col in range(cols)] for row in range(rows)]
        self.dsu = DSU(rows * cols)
        self.dsu.sets = {cell.id: [cell] for row in self.maze for cell in row}
        self.origin = self.maze[0][0]
        self.destination = self.maze[-1][-1]
        for row in range(rows):
            for col in range(cols):
                if row != 0:
                    self.maze[row][col].up = self.maze[row-1][col]
                    self.maze[row][col].up.down = self.maze[row][col]
                if col != 0:
                    self.maze[row][col].left = self.maze[row][col-1]
                    self.maze[row][col].left.right = self.maze[row][col]

    def make_maze(self) -> list:
        sets = self.dsu.get_distinct_sets()
        while len(sets) > 1:
            randomSet = random.choice(sets)
            randomCell = random.choice(randomSet)
            neighbors = []
            if randomCell.up and not self.dsu.connected(randomCell.id, randomCell.up.id):
                neighbors.append(randomCell.up)
            if randomCell.down and not self.dsu.connected(randomCell.id, randomCell.down.id):
                neighbors.append(randomCell.down)
            if randomCell.left and not self.dsu.connected(randomCell.id, randomCell.left.id):
                neighbors.append(randomCell.left)
            if randomCell.right and not self.dsu.connected(randomCell.id, randomCell.right.id):
                neighbors.append(randomCell.right)
            if neighbors:
                neighbor = random.choice(neighbors)
                self.dsu.union(randomCell.id, neighbor.id)
                if randomCell.up == neighbor:
                    randomCell.upConnected = True
                    neighbor.downConnected = True
                elif randomCell.down == neighbor:
                    randomCell.downConnected = True
                    neighbor.upConnected = True
                elif randomCell.left == neighbor:
                    randomCell.leftConnected = True
                    neighbor.rightConnected = True
                elif randomCell.right == neighbor:
                    randomCell.rightConnected = True
                    neighbor.leftConnected = True
                yield (neighbor, randomCell)
            sets = self.dsu.get_distinct_sets()

    def solve_maze(self, start: Cell, end: Cell) -> list:
        stack = [start]
        parent = {}
        while stack:
            current = stack.pop()
            if current == end:
                path = []
                current = end
                while current != start:
                    path.append(current)
                    current = parent[current]
                yield path[::-1]
            current.visited = True
            if current.up and current.upConnected and not current.up.visited:
                stack.append(current.up)
                parent[current.up] = current
            if current.down and current.downConnected and not current.down.visited:
                stack.append(current.down)
                parent[current.down] = current
            if current.left and current.leftConnected and not current.left.visited:
                stack.append(current.left)
                parent[current.left] = current
            if current.right and current.rightConnected and not current.right.visited:
                stack.append(current.right)
                parent[current.right] = current
            yield self.maze
