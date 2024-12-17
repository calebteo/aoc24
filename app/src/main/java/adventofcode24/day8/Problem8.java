package adventofcode24.day8;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Problem8 {
    public static void main (String[] args) {
        Problem8 p8 = new Problem8();
        p8.solve();
    }

    String inputFile = "/Users/calebcheehongteo/code/adventOfCode24/app/src/main/java/adventofcode24/day8/input.txt";
    /*
        - Map for each frequency
        - 2D array for the map. Source of truth for traversing and occupation
        - Slot/Airspace
            - Value
            - Location
        - Methods to calculate antinode distance when having a matched pair. 
        - Method to traverse diagonally to find all possible pairs. Only need to check half distance to edge(short-cut)
    */

    public class AirSpace {
        String frequencey = "";
        Integer xCoord = -1; 
        Integer yCoord = -1;

        public AirSpace(String f, Integer x, Integer y) {
            this.frequencey = f; 
            this.xCoord = x;
            this.yCoord = y;
        }

        public static String constructKey(Integer X, Integer Y) {
            return (X.toString() + "," + Y.toString());
        }
    }

    ArrayList<ArrayList<AirSpace>> space = new ArrayList<>();
    Map<String, ArrayList<AirSpace>> frequencies = new HashMap<>(); 
    Map<String, AirSpace> antiNodes = new HashMap<>();
    Integer countOfAntiNodes = 0;
    Integer anitNodesPart2 = 0;
    
    public void solve() {
        readFile();
        // printSpace();
        findAntiNodes(false);
        findAntiNodes(true);

        System.out.println("Answer: " + countOfAntiNodes);
        Integer totalCount = countOfAntiNodes + anitNodesPart2;
        System.out.println("Answer: " + totalCount);
        printSpaceWithAnitNodes();
    }

    public void findAntiNodes(boolean part2) {
        // for each frequencey
        // Check if value can be a pair based on distance apart. (Go Diagonally only first). 
        // For every pair check if it fits in map (and check if not occcupied). 
        for (ArrayList<AirSpace> frequency : frequencies.values()) {
            for (int index = 0; index < frequency.size() - 1; index++) {
                for (int next = index + 1; next < frequency.size(); next++) {
                    AirSpace f1 = frequency.get(index);
                    AirSpace f2 = frequency.get(next);
                    if (isPair(frequency.get(index), frequency.get(next))) {
                        Integer anitNodeXDiff = f2.xCoord - f1.xCoord; 
                        Integer antiNodeYDiff = f2.yCoord - f1.yCoord; 

                        Integer antiNodeXCoord_1 = f1.xCoord - anitNodeXDiff;
                        Integer antiNodeYCoord_1 = f1.yCoord - antiNodeYDiff;
                        Integer antiNodeXCoord_2 = f2.xCoord + anitNodeXDiff;
                        Integer antiNodeYCoord_2 = f2.yCoord + antiNodeYDiff;

                        if (isInSpace(antiNodeXCoord_1, antiNodeYCoord_1) && notOccupied(antiNodeXCoord_1, antiNodeYCoord_1) && !alreadyAntiNode(antiNodeXCoord_1, antiNodeYCoord_1)) {
                            // System.out.println("AntiNode-" + f1.frequencey + ": " + antiNodeXCoord_1 + ", " + antiNodeYCoord_1);
                            countOfAntiNodes++;
                        } else {
                            System.out.print("NOT AntiNode-" + f1.frequencey + ": " + antiNodeXCoord_1 + ", " + antiNodeYCoord_1);
                            if (isInSpace(antiNodeXCoord_1, antiNodeYCoord_1)){ System.out.print(" - - (" + space.get(antiNodeYCoord_1).get(antiNodeXCoord_1).frequencey + ")");}
                            System.out.println("");
                        }
                        if (isInSpace(antiNodeXCoord_2, antiNodeYCoord_2) && notOccupied(antiNodeXCoord_2, antiNodeYCoord_2) && !alreadyAntiNode(antiNodeXCoord_2, antiNodeYCoord_2)) {
                            // System.out.println("AntiNode-" + f1.frequencey + ": " + antiNodeXCoord_2 + ", " + antiNodeYCoord_2);
                            countOfAntiNodes++;
                        } else {
                            System.out.print("NOT AntiNode-" + f1.frequencey + ": " + antiNodeXCoord_2 + ", " + antiNodeYCoord_2);
                            if (isInSpace(antiNodeXCoord_2, antiNodeYCoord_2)){ System.out.print(" - - (" + space.get(antiNodeYCoord_2).get(antiNodeXCoord_2).frequencey + ")");}
                            System.out.println("");
                        }

                        if (part2) {
                            boolean outOfSpaceUpWards = false;
                            boolean outOfSpaceDownWards = false;
                            Integer multiplier = 0;
                            while (!outOfSpaceUpWards || !outOfSpaceDownWards) {
                                Integer antiNode_X = f1.xCoord - (anitNodeXDiff * multiplier);
                                Integer antiNode_Y = f1.yCoord - (antiNodeYDiff * multiplier);
                                if (isInSpace(antiNode_X, antiNode_Y)) {
                                    if (!alreadyAntiNode(antiNode_X, antiNode_Y)) {
                                        anitNodesPart2++;
                                    }
                                } else {
                                    outOfSpaceUpWards = true;
                                }

                                Integer antiNode_X_2 = f2.xCoord + (anitNodeXDiff * multiplier);
                                Integer antiNode_Y_2 = f2.yCoord + (antiNodeYDiff * multiplier);
                                if (isInSpace(antiNode_X_2, antiNode_Y_2)) {
                                    if (!alreadyAntiNode(antiNode_X_2, antiNode_Y_2)) {
                                        anitNodesPart2++;
                                    }
                                } else {
                                    outOfSpaceDownWards = true;
                                }
                                multiplier++;
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isPair(AirSpace square1, AirSpace square2) {
        // A pair is as long as they are diagonal from each other.

        Integer xDiff = square2.xCoord - square1.xCoord;
        Integer yDiff = square2.yCoord - square1.yCoord;
        
        // return (xDiff > 0 && yDiff < 0) || (xDiff < 0 && yDiff > 0);
        // return !(xDiff == 0 || yDiff == 0);
        return true;
    }

    public boolean isInSpace(Integer X, Integer Y) {
        return (X < space.get(0).size() && X >= 0 && Y < space.size() && Y >= 0);
    }

    public boolean notOccupied(Integer X, Integer Y) {
        // return (space.get(Y).get(X).frequencey.equals(".")); // Bug - you do count even if there is something there. 
        return true;
    }

    public boolean alreadyAntiNode(Integer X, Integer Y) {
        if (antiNodes.containsKey(AirSpace.constructKey(X, Y))) {
            return true;
        } else {
            antiNodes.put(AirSpace.constructKey(X, Y), new AirSpace("#", X, Y));
            return false;
        }
    }

    public void readFile() {
        File file = new File(inputFile);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line; 
            Integer rowCounter = 0;
            while ((line = reader.readLine()) != null) {
                ArrayList<AirSpace> row = new ArrayList<>();
                String[] characters = line.split("");
                for (int i = 0; i < characters.length; i++) {
                    AirSpace aS = new AirSpace(characters[i], i, rowCounter);
                    if (!characters[i].equals(".")) {
                        addToFrequencyList(characters[i], aS);
                    }
                    row.add(aS);
                }
                space.add(row);
                rowCounter++;
            }
        } catch (Exception e) {
            System.err.println("Exception");
            System.err.println(e.getMessage());
        }
    }

    public void addToFrequencyList(String frequency, AirSpace aS) {
        if (frequencies.containsKey(frequency)){
            frequencies.get(frequency).add(aS);
        } else {
            frequencies.put(frequency, new ArrayList<>(Arrays.asList(aS)));
        }
    }

    public void printSpace() {
        for(ArrayList<AirSpace> row : space) {
            for (AirSpace aS : row) {
                System.out.print(aS.frequencey);
            }
            System.out.println("");
        }
    }

    public void printSpaceWithAnitNodes() {
        for (int row = 0; row < space.size(); row++) {
            ArrayList<AirSpace> currentRow = space.get(row);
            for (int column = 0; column < currentRow.size(); column++) {
                if (antiNodes.containsKey(AirSpace.constructKey(column, row))){
                    System.out.print("# ");
                } else {
                    System.out.print(currentRow.get(column).frequencey + " ");
                }
            }
            System.out.println("");
        }
    }
}