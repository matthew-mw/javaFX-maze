class Cell:
    def __init__(self, row: int, col: int, id : int) -> None:
        self.row = row
        self.col = col
        self.id = id
        self.up = None
        self.upConnected = False
        self.down = None
        self.downConnected = False
        self.left = None
        self.leftConnected = False
        self.right = None
        self.rightConnected = False
        self.visited = False
        self.parent = None
