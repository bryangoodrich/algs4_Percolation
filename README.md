Percolation
================

A composite material percolates if a proportion of that 
composite material contains percolating materials. For
instance, how much of a porus material needs to be porus
for water to drain through it? How much of a conductive
wire needs to be conductive material for electricity to
pass through the wire? 

To abstractly model this situation, we can use a 2D grid.
Each cell of this grid is either open or closed. This
system percolates if there is a path from the top of the
grid to the bottom consisting of open cells adjacent to
each other. Thus, each cell is full if the system
percolates to that cell, intuitively capturing the idea
of water pouring down and filling the cells. 

Below is an example of this model. A cell is black if it
is closed, white if it is open, and blue if full. The
simulation stops when the system percolates. 

[insert gif]

Note: this code is incomplete. Notice how cells at the
lower left do not meet the definition of being full, yet
they color blue. This is because of *backwash*. The cells
are still logically linked to the "top" by being linked to
the "bottom" that also links to the top. 

The API and Data Structures
============================
The API is pretty straight-forward. We create the grid, 
open cells, and check if the system percolates. To support
that, we need to be able to check if neighboring cells are
open and if they are full. 

```
public class Percolation {
    public Percolation(int N)            // create an N-by-N grid, all cells blocked
    public void open(int i, int j)       // open site (row i, column j), if not already open
    public boolean isOpen(int i, int j)  // check if site (row i, column j) is open
    public boolean isFull(int i, int j)  // check if site (row i, column j) is full
    public boolean percolates()          // check if does the system percolate
}
```

The challenge is how do we do this *efficiently*. As things
go with grids, brute force algorithms tend to be O(n^2). We
can do better. The Union-Find (or disjoint-set) data 
structure keeps track of connected objects (or disjoint
sets). 

The union-find data structure is used to keep track
of whether a path from the top percolates through the 
system. This is done by allowing 2 virtual cells to exist 
above and below the grid. The top row of cells are
initialized to belonging to that set. If they become open,
they become filled. The bottom row is attached to a virtual
bottom. The system percolates if the bottom becomes full,
which happens whenever any cells at the bottom become full.

As noted above, this model suffers the backwash bug. To
correct it, I believe it requires holding two union-find
structures. One will monitor flow from above, the other
will monitor from below to prevent backwash.


Application: Monte Carlo Simulation
===================================

To put this data structure to use, we will resolve the 
simulation suggested initially. What proportion of the 
system needs to be open for it to percolate? We'll call
this the percolation threshold of the system. This problem
does not have a closed-form mathematical solution. Instead,
we can use this model to simulate instances over, and over
and monitor what proportion of cells need to be open for
each instance to percolate. The average of all these
instances gives an estimate of the answer to our problem.

This process of running multiple instances and using
statistics over the variety of solutions is known as
doing a *monte carlo simulation*. To facilitate this,
we create another API for running the simulation. This
data structure will use the percolation model as the 
object of each instance. This API will then do the
statistical calculations to derive the answer to our
problem. 

```
public class PercolationStats {
    public PercolationStats(int N, int T)  // perform T independent experiments on an N-by-N grid
    public double mean()                   // sample mean of percolation threadshold
    public double stddev()                 // sample standard deviation of percolation threshold
    public double confidenceLo()           // low endpoint of 95% confidence interval
    public double confidenceHi()           // high endpoint of 95% confidence interval
}
```


Conclusions
================

Of all programming classes I've taken, this was my favorite
assignment. The task was complicated enough to be
challenging, yet we were provided the tools to truly
explore the API we were building. If we met the
requirements of the API, then the provided visualization
program would allow us to see the program processs the
data. Another visualization program let us interact with
the input grid to provide the input dynamically. Then
we got to see how to build our own tool to use the API we
built. Additionally, the tool we made showed how data
structures can be used to support doing statistical data
analysis efficiently.

This repo provides the code I developed for the API, the
monte carlo simulation, and the union-find data structure.
We used the built-in one that came with the class, but that
can easily be changed to use the ones I made. I cannot
provide the visualization tools, as that code is not mine.
Lastly, the code included proper Java Doc commenting, so 
I've included the resulting documentation folder. 

Lastly, this task inspired me to build the union-find and 
percolation model in Python. I wrote a gist about it. That
code is also included here. The Python method, while I 
attempted it to be efficient, was incredibly slow. This may
work if I approached it entirely using Python set data
structures and modeled a union-find (disjoint-sets) that
way. 



