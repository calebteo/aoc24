package adventofcode24.day10;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Problem10 {

    public static void main(String[] args) {
        Problem10 p = new Problem10();
        p.solve();
    }

    String inputFile = "/Users/calebcheehongteo/code/adventOfCode24/app/src/main/java/adventofcode24/day10/input.txt";
    ArrayList<ArrayList<Integer>> topology = new ArrayList<>();

    public void solve() {
        readFile();
        printTopology(this.topology);
        findAllTrailHeads(false);
        findAllTrailHeads(true);
    }

    public void readFile() {
        File file = new File(inputFile); 
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                ArrayList<Integer> row = new ArrayList<>();
                for (int i = 0; i < line.length(); i++) {
                    row.add(Integer.valueOf(line.charAt(i)) - 48);
                }
                topology.add(row);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void printTopology(ArrayList<ArrayList<Integer>> topology) {
        for(ArrayList<Integer> l : topology) {
            for (Integer i : l) {
                System.out.print(i);
            }
            System.out.println("");
        }
    }

/*
 * Go to every square.
 *  If 0 then start checking for trailheads.
 *      Walk in every direction and check if up by 1. 
 *      Recursive through until reaches 9 or. . . 
 *      If 9 then count as trail
 *      If not then return 0. 
 *      Add up recursive runs.
 */

    public void findAllTrailHeads(boolean part2) {
        int sum = 0;
        for (int y = 0; y < topology.size(); y++) {
            for (int x = 0; x < topology.get(y).size(); x++) {
                if (topology.get(y).get(x) == 0) {
                    Map<String, Coordinate> foundPeaks = new HashMap<>();
                    int debugResult = checkForTrailHead(x, y, -1, -1, foundPeaks, part2);
                    sum += debugResult;
                }
            }
        }
        String part = part2 ? "part-2" : "part-1";
        System.out.println("Answer " + part + ": " + sum);
    }
    // Up, Down, Left Right
    // (0, 1), (0, -1), (-1, 0), (1, 0)

    ArrayList<Integer> xMovement = new ArrayList<>(Arrays.asList(0, 0, -1, 1));
    ArrayList<Integer> yMovement = new ArrayList<>(Arrays.asList(1, -1, 0, 0));
    ArrayList<ArrayList<Coordinate>> foundPaths = new ArrayList<>();

    public class Coordinate {
        int x;
        int y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Coordinate(Coordinate c) {
            this.x = c.x;
            this.y = c.y;
        }
    }

    public ArrayList<Coordinate> deepCopy(ArrayList<Coordinate> original) {
        ArrayList<Coordinate> copy = new ArrayList<>();
        for (Coordinate c : original) {
            copy.add(new Coordinate(c)); 
        }
        return copy; 
    }

    public void printFoundPaths() {
        for (ArrayList<Coordinate> p : foundPaths) { 
            for (Coordinate c : p) {
                System.out.print("(" + c.x + "," + c.y +  ") ");
            }
            System.out.println("");
        }
    }

/*
 * Return checkTrailHead(Up) + checkTrailHead(Down) + checkTrailHead(Right) + checkTrailHead(Left) 
 */

    public int checkForTrailHead(int x, int y, int prevX, int prevY, Map<String, Coordinate> foundPeaks, boolean part2) {
        if ( prevX == -1 && prevY == -1) {
            return (checkForTrailHead(x + xMovement.get(0), y + yMovement.get(0), x, y, foundPeaks, part2) + 
                checkForTrailHead(x + xMovement.get(1), y + yMovement.get(1), x, y, foundPeaks, part2) + 
                checkForTrailHead(x + xMovement.get(2), y + yMovement.get(2), x, y, foundPeaks, part2) + 
                checkForTrailHead(x + xMovement.get(3), y + yMovement.get(3), x, y, foundPeaks, part2));
        }

        if (!onMap(x, y)){
            return 0;
        }
            
        if (topology.get(prevY).get(prevX) + 1 != topology.get(y).get(x)) {
            return 0;
        }

        // Used for debugging paths
            // ArrayList<Coordinate> updatedPath = deepCopy(path);
            // updatedPath.add(new Coordinate(prevX, prevY));
        
        if (part2) {
            if (topology.get(y).get(x) == 9) {
                return 1;
            }
        } else {
            if (topology.get(y).get(x) == 9 && !foundPeaks.containsKey(constructLocationKey(x, y))) {
                // Used for debugging paths
                    // updatedPath.add(new Coordinate(x, y));
                    // foundPaths.add(updatedPath);
                foundPeaks.put(constructLocationKey(x, y), new Coordinate(x, y));
                return 1;
            } 
        }
        
        return (checkForTrailHead(x + xMovement.get(0), y + yMovement.get(0), x, y, foundPeaks, part2) + 
                checkForTrailHead(x + xMovement.get(1), y + yMovement.get(1), x, y, foundPeaks, part2) + 
                checkForTrailHead(x + xMovement.get(2), y + yMovement.get(2), x, y, foundPeaks, part2) + 
                checkForTrailHead(x + xMovement.get(3), y + yMovement.get(3), x, y, foundPeaks, part2));
    }

    public boolean onMap (int x, int y) {
        return (x >= 0 && x < topology.get(0).size() && y >= 0 && y < topology.size());
    }

    public String constructLocationKey(Integer X, Integer Y) {
        return X.toString() + ", " + Y.toString();
    }
}