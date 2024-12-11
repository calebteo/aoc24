package adventofcode24.day6;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Problem6{

    public static void main(String[] args) {
        Problem6.solveProblem6();
    }

    String floorPlanFile = "/Users/calebcheehongteo/code/adventOfCode24/app/src/main/java/adventofcode24/day6/smallinput.txt";

    public class Man {
        Integer XPosition = null;
        Integer YPosition = null;
        String facingDirection = null;
        Integer numberOfPositions = 0;

        static String UP = "^";
        static String DOWN = "v";
        static String LEFT = "<";
        static String RIGHT = ">";

        public static boolean isAMan(String c) {
            return (c.equals(Man.UP) || c.equals(Man.DOWN) ||  c.equals(Man.LEFT)|| c.equals(Man.RIGHT));
        }

        public boolean stillOnFloor() {
            return (this.XPosition >= 0 && this.XPosition <= floorPlan.get(0).size() - 1) && (this.YPosition >= 0 && this.YPosition <= floorPlan.size() - 1);
        }

        public boolean nextSpotIsOnFloor(Integer YDiff, Integer XDiff) {
            Integer newX = this.XPosition + XDiff;
            Integer newY = this.YPosition + YDiff;
            return (newX >= 0 && newX <= floorPlan.get(0).size() - 1) && (newY >= 0 && newY <= floorPlan.size() - 1);
        }

        public Man copyMan(Man man) {
            this.facingDirection = man.facingDirection;
            this.numberOfPositions = man.numberOfPositions;
            this.XPosition = man.XPosition;
            this.YPosition = man.YPosition;
            return this;
        }
    }

    public class Square {
        String value = null;
        boolean visited = false;
        String whenVisitedFace = ""; 
        Integer totalVisited = 0;

        public static void printRowOfSquares(ArrayList<Square> squares) {
            for (Square s : squares) {
                System.out.print(s.value + " ");
            }
        }

        public static String constructKey(Integer row, Integer column) {
        return row.toString() + ", " + column.toString(); 
    }
    }

    public ArrayList<ArrayList<Square>> floorPlan = new ArrayList<>();
    public Man myMan = new Man(); 
    public Map<String, Square> possibleLoopSpaces = new HashMap<>(); 
    public ArrayList<Square> allPossibleWithRepeats = new ArrayList<>(); 

    public static void solveProblem6() {
        Problem6 p6 = new Problem6();
        p6.readFile();
        // for(ArrayList<Square> l : p6.floorPlan) {
        //     Square.printRowOfSquares(l);
        //     System.out.println("");
        // }
        System.out.println("Man's Position: " + p6.myMan.XPosition + ", " + p6.myMan.YPosition);
        System.out.println("Man's facing: " + p6.myMan.facingDirection);

        p6.moveMan();
        System.out.println("Man's count: " + p6.myMan.numberOfPositions);

        System.out.println("The possible loop count is: " + p6.possibleLoopSpaces.size());
        // for (String key : p6.possibleLoopSpaces.keySet()) {
        //     System.out.println("Key: " + key);
        // }
        System.out.println("All Possible with repeats: " + p6.allPossibleWithRepeats.size());
    }

    public void readFile() {
        File inputFile = new File(floorPlanFile);
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))){
            String line;
            Integer countRow = 0; 
            while ((line = reader.readLine()) != null) {
                ArrayList<Square> currentRow = new ArrayList<>();
                for (int i = 0; i < line.length(); i++) {
                    if (Man.isAMan(String.valueOf(line.charAt(i)))) {
                        myMan.XPosition = i; 
                        myMan.YPosition = countRow;
                        myMan.facingDirection = String.valueOf(line.charAt(i));
                        Square s = new Square();
                        s.value = "."; // Fill in where man is as an free space.
                        currentRow.add(s);
                    } else {
                        Square s = new Square(); 
                        s.value = String.valueOf(line.charAt(i));
                        currentRow.add(s);
                    }
                    
                }
                floorPlan.add(currentRow);
                countRow++;
            }
        } catch (Exception e) {
            System.err.println("Exception");
            System.err.println(e.getMessage());
        }
    }

    public void moveMan() {
        while(myMan.stillOnFloor()) {
            // Mark current position as visited + count path
            Square currentSpot = floorPlan.get(myMan.YPosition).get(myMan.XPosition);
            if (!currentSpot.visited) {
                currentSpot.visited = true;
                currentSpot.whenVisitedFace = myMan.facingDirection;
                myMan.numberOfPositions++;
            }
            currentSpot.totalVisited = currentSpot.totalVisited + 1;
            boolean canMoveForward = false;
            Square nextMove = new Square();

            /*
                List - [Up, down, left, right]
                List - [(0,-1), (0,+1), (-1,0),(1,0) ]
            */


            while(!canMoveForward) {
                // System.out.println("Man's Position: " + myMan.numberOfPositions);
                
                // Check direction, if no obs -> Move in forward direction else turn
                if (myMan.facingDirection.equals(Man.UP)) {
                    if (myMan.YPosition - 1 < 0) { break; }
                    nextMove = floorPlan.get(myMan.YPosition - 1).get(myMan.XPosition); 
                    if (nextMove.value.equals("#")) {
                        // Obsticale + turn 90 i.e. face Right
                        myMan.facingDirection = Man.RIGHT; 
                    } else {
                        canMoveForward = true;
                    }
                } else if (myMan.facingDirection.equals(Man.DOWN)) {
                    if (myMan.YPosition + 1 > floorPlan.size() - 1) { break; }
                    nextMove = floorPlan.get(myMan.YPosition + 1).get(myMan.XPosition);
                    if (nextMove.value.equals("#")) {
                        // Obsticale + turn 90 i.e. face Left
                        myMan.facingDirection = Man.LEFT; 
                    } else {
                        canMoveForward = true;
                    }
                } else if (myMan.facingDirection.equals(Man.LEFT)) {
                    if (myMan.XPosition - 1 < 0) { break; }
                    nextMove = floorPlan.get(myMan.YPosition).get(myMan.XPosition - 1); 
                    if (nextMove.value.equals("#")) {
                        // Obsticale + turn 90 i.e. face Up
                        myMan.facingDirection = Man.UP; 
                    } else {
                        canMoveForward = true;
                    }
                } else if (myMan.facingDirection.equals(Man.RIGHT)) {
                    if (myMan.XPosition + 1 > floorPlan.get(0).size() - 1) { break; }
                    nextMove = floorPlan.get(myMan.YPosition).get(myMan.XPosition + 1); 
                    if (nextMove.value.equals("#")) {
                        // Obsticale + turn 90 i.e. face Down
                        myMan.facingDirection = Man.DOWN; 
                    } else {
                        canMoveForward = true;
                    }
                } else {
                    System.err.println("Man is lost");
                }
            }

            // Make man move in the right direction. . . 
            if (myMan.facingDirection.equals(Man.UP)) {
                myMan.YPosition = myMan.YPosition - 1;
            } else if (myMan.facingDirection.equals(Man.DOWN)) {
                myMan.YPosition = myMan.YPosition + 1;
            } else if (myMan.facingDirection.equals(Man.LEFT)) {
                myMan.XPosition = myMan.XPosition - 1; 
            } else if (myMan.facingDirection.equals(Man.RIGHT)) {
                myMan.XPosition = myMan.XPosition + 1; 
            } else {
                System.err.println("Again. Man is lost");
            }

            // Check if it is a loop state.
            Man imaginateMan = new Man(); 
            imaginateMan = imaginateMan.copyMan(myMan);
            checkIfLoopSpot(imaginateMan);
            
        }
    }

    public void checkIfLoopSpot(Man imaginateMan) {
        // Take Man position and look 90 degrees. 
        // Walk in that direction until obsticale or (visited path and same direction. Check if visited path is in same direction.)
     
        Integer[] coordinateDiff = new Integer[2]; // 0 - row, 1 - column
        Integer[] objectDiff = new Integer[2];
        HashMap<String, Square> visited = new HashMap<>();

        if (imaginateMan.facingDirection.equals(Man.UP)) {
            imaginateMan.facingDirection = Man.RIGHT;
            coordinateDiff[0] = 0;
            coordinateDiff[1] = 1;
            objectDiff[0] = -1;
            objectDiff[1] = 0;
        } else if (imaginateMan.facingDirection.equals(Man.DOWN)) {
            imaginateMan.facingDirection = Man.LEFT;
            coordinateDiff[0] = 0;
            coordinateDiff[1] = -1;
            objectDiff[0] = 1;
            objectDiff[1] = 0;
        } else if (imaginateMan.facingDirection.equals(Man.LEFT)) {
            imaginateMan.facingDirection = Man.UP;
            coordinateDiff[0] = -1;
            coordinateDiff[1] = 0;
            objectDiff[0] = 0;
            objectDiff[1] = -1;
        } else if (imaginateMan.facingDirection.equals(Man.RIGHT)) {
            imaginateMan.facingDirection = Man.DOWN;
            coordinateDiff[0] = 1;
            coordinateDiff[1] = 0;
            objectDiff[0] = 0;
            objectDiff[1] = 1;
        } else {
            System.err.println("Man is lost");
        }

        Integer plantedObjectY = imaginateMan.YPosition + objectDiff[0];
        Integer plantedObjectX = imaginateMan.XPosition + objectDiff[1];
        if (imaginateMan.nextSpotIsOnFloor(objectDiff[0], objectDiff[1])) {
            return;
        }
        if (floorPlan.get(plantedObjectY).get(plantedObjectX).value.equals("#")){
            // Space is already occupied - don't check
            return;
        }

        boolean nextSpotIsTheEdge = false;
        boolean notFirstCheck = false;
        while (!nextSpotIsTheEdge) {
            if (imaginateMan.nextSpotIsOnFloor(coordinateDiff[0], coordinateDiff[1])) {
                Square nextSquare = floorPlan.get(imaginateMan.YPosition + coordinateDiff[0]).get(imaginateMan.XPosition + coordinateDiff[1]);
                if (nextSquare.value.equals("#") || (imaginateMan.YPosition + coordinateDiff[0] == plantedObjectY && imaginateMan.XPosition + coordinateDiff[1] == plantedObjectY)) { 
                    // new rotation
                    if (imaginateMan.facingDirection.equals(Man.UP)) {
                        imaginateMan.facingDirection = Man.RIGHT;
                        coordinateDiff[0] = 0;
                        coordinateDiff[1] = 1;
                    } else if (imaginateMan.facingDirection.equals(Man.DOWN)) {
                        imaginateMan.facingDirection = Man.LEFT;
                        coordinateDiff[0] = 0;
                        coordinateDiff[1] = -1;
                    } else if (imaginateMan.facingDirection.equals(Man.LEFT)) {
                        imaginateMan.facingDirection = Man.UP;
                        coordinateDiff[0] = -1;
                        coordinateDiff[1] = 0;
                    } else if (imaginateMan.facingDirection.equals(Man.RIGHT)) {
                        imaginateMan.facingDirection = Man.DOWN;
                        coordinateDiff[0] = 1;
                        coordinateDiff[1] = 0;
                    } else {
                        System.err.println("Man is lost");
                    }
                } 
                else if (notFirstCheck && 
                        visited.get(Square.constructKey(imaginateMan.YPosition, imaginateMan.XPosition)) != null && 
                        visited.get(Square.constructKey(imaginateMan.YPosition, imaginateMan.XPosition)).whenVisitedFace.equals(imaginateMan.facingDirection)) {
                    // Found spot in which a loop would happen
                    possibleLoopSpaces.put(Square.constructKey(plantedObjectY, plantedObjectX), nextSquare);
                    allPossibleWithRepeats.add(nextSquare);
                    System.out.println("Added a loop");
                    break;
                } 
                // else if (nextSquare.visited && nextSquare.whenVisitedFace.equals(imaginateMan.facingDirection)) {
                //     // Found spot in which a loop would happen
                //     possibleLoopSpaces.put(Square.constructKey(plantedObjectY, plantedObjectX), nextSquare);
                //     allPossibleWithRepeats.add(nextSquare);
                //     break;
                // } 
                else {
                    Square visitedSquare = new Square();
                    visitedSquare.whenVisitedFace = imaginateMan.facingDirection;
                    visited.put(Square.constructKey(imaginateMan.YPosition, imaginateMan.XPosition), visitedSquare);
                    notFirstCheck = true;
                    // Move man forward
                    imaginateMan.YPosition = imaginateMan.YPosition + coordinateDiff[0];
                    imaginateMan.XPosition = imaginateMan.XPosition + coordinateDiff[1];
                    
                }
            } else { nextSpotIsTheEdge = true; } // assumed at edge. Stop walking
        }

    }

    public void checkIfLoopSpot_INCORRECT() {
        // Loop state is if the next sqaure is also visited and the same new direction.
        Square rotatedMove = new Square();
        if (myMan.facingDirection.equals(Man.UP)) {
            if (myMan.XPosition + 1 > floorPlan.get(0).size() - 1) { return; }
            // Rotate
            rotatedMove = floorPlan.get(myMan.YPosition).get(myMan.XPosition + 1); 
            if (rotatedMove.visited && rotatedMove.whenVisitedFace.equals(myMan.RIGHT)) {
                possibleLoopSpaces.put(Square.constructKey(myMan.YPosition - 1, myMan.XPosition), rotatedMove);
            }
            
        } else if (myMan.facingDirection.equals(Man.DOWN)) {
            if (myMan.XPosition - 1 < 0) { return; }
            // Rotate
            rotatedMove = floorPlan.get(myMan.YPosition).get(myMan.XPosition - 1);
            if (rotatedMove.visited && rotatedMove.whenVisitedFace.equals(myMan.LEFT)) {
                possibleLoopSpaces.put(Square.constructKey(myMan.YPosition + 1, myMan.XPosition), rotatedMove);
            }
        } else if (myMan.facingDirection.equals(Man.LEFT)) {
            if (myMan.YPosition - 1 < 0) { return; }
            // Rotate
            rotatedMove = floorPlan.get(myMan.YPosition - 1).get(myMan.XPosition); 
            if (rotatedMove.visited && rotatedMove.whenVisitedFace.equals(myMan.UP)) {
                // put the key as the actual spot for the object
                possibleLoopSpaces.put(Square.constructKey(myMan.YPosition, myMan.XPosition - 1), rotatedMove);
            }
        } else if (myMan.facingDirection.equals(Man.RIGHT)) {
            if (myMan.YPosition + 1 > floorPlan.size() - 1) { return; }
            rotatedMove = floorPlan.get(myMan.YPosition + 1).get(myMan.XPosition); 
            if (rotatedMove.visited && rotatedMove.whenVisitedFace.equals(myMan.DOWN)) {
                possibleLoopSpaces.put(Square.constructKey(myMan.YPosition, myMan.XPosition + 1), rotatedMove);
            }
        } else {
            System.err.println("Man is lost");
        }

    }
}

/*
Part 2 
 Man's Position: 74, 92
 Man's facing: ^
 Man's count: 5129
 The possible loop count is:431 (TOO LOW)
*/ 

/*
 Code to check next proceeding Square after hitting object
String proceedingRotation = "";
Integer[] proceedingDirection = new Integer[2];
if (manRotatedDirection.equals(Man.UP)) {
    proceedingRotation = Man.RIGHT;
    proceedingDirection[0] = 0;
    proceedingDirection[1] = 1;

} else if (manRotatedDirection.equals(Man.DOWN)) {
    proceedingRotation = Man.LEFT;
    proceedingDirection[0] = 0;
    proceedingDirection[1] = -1;

} else if (manRotatedDirection.equals(Man.LEFT)) {
    proceedingRotation = Man.UP;
    proceedingDirection[0] = -1;
    proceedingDirection[1] = 0;

} else if (manRotatedDirection.equals(Man.RIGHT)) {
    proceedingRotation = Man.DOWN;
    proceedingDirection[0] = 1;
    proceedingDirection[1] = 0;
} else {
    System.err.println("Man is lost");
}
if (imaginateMan.nextSpotIsOnFloor(coordinateDiff[0] + proceedingDirection[0], coordinateDiff[1] + proceedingDirection[1])) {
    Square proceedingSquare = floorPlan.get(imaginateMan.YPosition + coordinateDiff[0] + proceedingDirection[0]).get(imaginateMan.XPosition + coordinateDiff[1] + proceedingDirection[1]);
    if (proceedingSquare.visited && proceedingSquare.whenVisitedFace.equals(proceedingRotation)) {
        // then it is a loop
        possibleLoopSpaces.put(Square.constructKey(plantedObjectY, plantedObjectX), nextSquare);
        allPossibleWithRepeats.add(nextSquare);
        break;
    }
}
*/