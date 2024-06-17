import matplotlib.pyplot as plt
import matplotlib.colors as mcolors
from maze import Maze
import numpy as np
import imageio.v2 as imageio
import io

cmap = mcolors.ListedColormap(['white', 'black'])

rows = 100
cols = 100

make_gif = True

maze = Maze(rows, cols)

images = []

grid = np.array([[not ((col % 2) and (row % 2)) for col in range(cols * 2 + 1)] for row in range(rows * 2 + 1)])

for (cell, neighbor) in maze.make_maze():
    if make_gif:
        cell_row = cell.row * 2 + 1
        cell_col = cell.col * 2 + 1
        neighbor_row = neighbor.row * 2 + 1
        neighbor_col = neighbor.col * 2 + 1

        grid[cell_row, cell_col] = 0
        grid[neighbor_row, neighbor_col] = 0
        grid[(cell_row + neighbor_row)//2, (cell_col + neighbor_col)//2] = 0

        buf = io.BytesIO()
        plt.imsave(buf, grid, cmap=cmap, format="png")
        buf.seek(0)
        images.append(imageio.imread(buf))
        buf.close()

imageio.mimsave("maze.gif", images)
