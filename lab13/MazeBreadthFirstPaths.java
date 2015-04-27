import java.util.Observable;
import java.util.LinkedList;

/** 
 *  @author Josh Hug
 *  @editor Andre Askarinam
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields: 
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
        marked[s] = true;
    }

    /** Conducts a breadth first search of the maze starting at vertex x. */
    private void bfs(int s) {
        /* Your code here. */
        LinkedList<Integer> queue = new LinkedList<Integer>();
        for (int v = 0; v < marked.length; v++) {
            distTo[v] = Integer.MAX_VALUE;
        }
        distTo[s] = 0;
        marked[s] = true;
        queue.add(s);

        while (queue.size() > 0) {
            int v = queue.poll();
            for (int a: maze.adj(v)) {
                if (!marked[a]) {
                    edgeTo[a] = v;
                    distTo[a] = distTo[v] + 1;
                    marked[a] = true;
                    queue.add(a);
                    announce();
                    if (a == t) {
                        return;
                    }
                }
            }
        }
    }


    @Override
    public void solve() {
        bfs(s);
    }
} 

