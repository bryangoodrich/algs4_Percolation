# -*- coding: utf-8 -*-
"""
Spyder Editor

This is a temporary script file.
"""
import array
class WeightedQuickUnion:
    def __init__(self, N):
        self.count = N
        self.id = array.array('i', range(N))
        self.size = array.array('i', [1] * N)
    
    def connected(self, p, q):
        return self.find(p) == self.find(q)
    
    def count(self):
        return self.count
    
    def find(self, p):
        while (p != self.id[p]):
            p = self.id[p]
        return p
    
    def get(self, p):
        return self.id[p]
    
    def union(self, p, q):
        rootP = self.find(p)
        rootQ = self.find(q)
        if (rootP == rootQ):
            return
        
        if (self.size[rootP] < self.size[rootQ]):
            self.id[rootP] = rootQ
            self.size[rootQ] += self.size[rootP]
        else:
            self.id[rootQ] = rootP
            self.size[rootP] += self.size[rootQ]
        self.count -= 1


# %% Percolation
class Percolation:
    def __init__(self, N):
        self.N = N
        self.uf = WeightedQuickUnion(N*N+2)
        self.state = array.array('b', [False] * (N*N+2))
        self.vTop = 0
        self.vBottom = N*N+1
        
        last_row = N * (N-1)
        for k in range(1, N+1):
            self.uf.union(last_row + k, self.vBottom)
            self.uf.union(k, self.vTop)
    
    def open(self, p):
        self.state[p] = True
        
        self.unionNeighbor(p, p - 1)
        self.unionNeighbor(p, p - self.N)
        self.unionNeighbor(p, p + self.N)
        self.unionNeighbor(p, p + 1)
    
    def isOpen(self, p):
        return self.state[p]
    
    def isFull(self, p):
        return self.isOpen(p) and self.uf.connected(self.vTop, p)
    
    def percolates(self):
        return self.uf.connected(self.vTop, self.vBottom)
    
    def valid(self, p):
        if (p <= self.vTop or p >= self.vBottom):
            return False
        
        i,j = self.coordinates(p)        
        if (i <= 0 or j <= 0):
            return False
        else:
            return (i <= self.N and j <= self.N)
    
#    def xyTo1D(self, i, j):
#        N = len(self.grid)
#        # Error checking with valid
#        return (i-1) * N + j
    
    def coordinates(self, p):
        return (p // self.N, p % self.N)
    
    def unionNeighbor(self, p, q):
        if (self.valid(q) and self.isOpen(q)):
            self.uf.union(p, q)


# %% Percolation Stats
import math
import random

class PercolationStats:
    def __init__(self, N, T):
        self.N = N
        self.trials = T
        self.run = [self.simulation(N) for x in range(T)]
        self.mu = sum(self.run) / T
        self.sd = sum([(r-self.mu)**2 for r in self.run]) / T
    
    def mean(self):
        return self.mu
    
    def stddev(self):
        return self.sd
    
    def confidenceLo(self):
        return self.mu - ((1.96 * self.sd) / math.sqrt(self.trials))
    
    def confidenceHi(self):
        return self.mu + ((1.96 * self.sd) / math.sqrt(self.trials))

    def simulation(self, N):
        Perc = Percolation(N)
        count = 0
        while (not Perc.percolates()):
            p = random.randint(1, N*N)
            if (not Perc.isOpen(p)):
                Perc.open(p)
                count += 1
        
        return count * 1.0 / (N**2)

    def __repr__(self):
        return "Percolation Simulation of size {} for {} trials".format(self.N, self.trials)

    def __str__(self):
        return "({:.6}, {:.6}, {:.6})".format(self.confidenceLo(), self.mean(), self.confidenceHi())

