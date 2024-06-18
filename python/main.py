import matplotlib.pyplot as plt
from matplotlib.colors import ListedColormap
from maze import Maze
import numpy as np
import imageio.v2 as imageio
import io

cmap = ListedColormap(["#ffffff", "#000000"])

rows = 10
cols = 10

fps = 15

make_gif = True

maze = Maze(rows, cols)

images = []

grid = np.array([[1 if (col % 2 == 0 or row % 2 == 0) else 0 for col in range(cols * 2 + 1)] for row in range(rows * 2 + 1)])

for (cell, neighbor) in maze.make_maze():
    cell_row = cell.row * 2 + 1
    cell_col = cell.col * 2 + 1
    neighbor_row = neighbor.row * 2 + 1
    neighbor_col = neighbor.col * 2 + 1

    grid[(cell_row + neighbor_row) // 2, (cell_col + neighbor_col) // 2] = 0

    if make_gif:
        buf = io.BytesIO()
        plt.imsave(buf, grid, cmap=cmap, format="png")
        buf.seek(0)
        images.append(imageio.imread(buf))
        buf.close()

if make_gif:
    imageio.mimsave("maze_generation.gif", images, fps=fps)
else:
    buf = io.BytesIO()
    plt.imsave(buf, grid, cmap=cmap, format="png")
    buf.seek(0)
    image = imageio.imread(buf)
    buf.close()
    imageio.imwrite("maze_generation.png", image)

images = []

cmap = ListedColormap(["#ffffff", "#000000", "#ff0000", "#F1C40F"])
for change in maze.solve_maze(maze.origin, maze.destination):
    if isinstance(change, bool):
        cmap = ListedColormap(["#ffffff", "#000000", "#ff0000", "#F1C40F", "#12ff00"])
        location = maze.destination
        while location != maze.origin:
            next_location = location.parent
            location_row = location.row * 2 + 1
            location_col = location.col * 2 + 1
            next_location_row = next_location.row * 2 + 1
            next_location_col = next_location.col * 2 + 1
            grid[location_row, location_col] = 4
            grid[next_location_row, next_location_col] = 4
            grid[(location_row + next_location_row) // 2, (location_col + next_location_col) // 2] = 4
            location = location.parent

            if make_gif:
                buf = io.BytesIO()
                plt.imsave(buf, grid, cmap=cmap, format="png")
                buf.seek(0)
                images.append(imageio.imread(buf))
                buf.close()
    else:
        current, previous = change
        if not previous:
            continue
        current_row = current.row * 2 + 1
        current_col = current.col * 2 + 1
        previous_row = previous.row * 2 + 1
        previous_col = previous.col * 2 + 1
        grid[current_row, current_col] = 3
        grid[previous_row, previous_col] = 2
        grid[(current_row + previous_row) // 2, (current_col + previous_col) // 2] = 2

        if make_gif:
            buf = io.BytesIO()
            plt.imsave(buf, grid, cmap=cmap, format="png")
            buf.seek(0)
            images.append(imageio.imread(buf))
            buf.close()

        grid[current_row, current_col] = 2

if make_gif:
    imageio.mimsave("maze_solution.gif", images, fps=fps)
else:
    buf = io.BytesIO()
    plt.imsave(buf, grid, cmap=cmap, format="png")
    buf.seek(0)
    image = imageio.imread(buf)
    buf.close()
    imageio.imwrite("maze_solution.png", image)
