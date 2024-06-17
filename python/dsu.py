class DSU:
    def __init__(self, n):
        self.parent = list(range(n))
        self.rank = [1] * n
        self.sets = {i: [i] for i in range(n)}  # Track sets

    def find(self, x):
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])  # Path compression
        return self.parent[x]

    def union(self, x, y):
        rootX = self.find(x)
        rootY = self.find(y)

        if rootX != rootY:
            # Union by rank
            if self.rank[rootX] > self.rank[rootY]:
                self.parent[rootY] = rootX
                self.sets[rootX].extend(self.sets[rootY])
                del self.sets[rootY]
            elif self.rank[rootX] < self.rank[rootY]:
                self.parent[rootX] = rootY
                self.sets[rootY].extend(self.sets[rootX])
                del self.sets[rootX]
            else:
                self.parent[rootY] = rootX
                self.sets[rootX].extend(self.sets[rootY])
                del self.sets[rootY]
                self.rank[rootX] += 1

    def connected(self, x, y):
        return self.find(x) == self.find(y)
    
    def get_distinct_sets(self):
        # Return the list of distinct sets
        return list(self.sets.values())
