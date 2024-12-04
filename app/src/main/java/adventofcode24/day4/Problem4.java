package adventofcode24.day4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Problem4 {

    public static void main (String[] args) {
        solveProblem4();
    }

    public Map<String, String> crossPairingsFound = new HashMap<>();
    String leftUpEncoding = "LU";
    String leftDownEncoding = ":LD";
    String rightUpEncoding = "RU";
    String rightDownEncoding = "RD";

    public class WordWithDirection {
        Integer row;
        Integer column;
        String direction; 
        String word;

        Integer XCoordinatesRow;
        Integer XCoordinatesColumn;

        public WordWithDirection(Integer row, Integer column, String direction, String word) {
            this.row = row;
            this.column = column;
            this.direction = direction;
            this.word = word;
        }

        public Integer getRow() {
            return this.row;
        }
        public Integer getColumn() {
            return this.column;
        }
        public String getWord() {
            return this.word;
        }
        public String getDirection() {
            return this.direction;
        }
        public void setXCoordinates(Integer row, Integer column) {
            this.XCoordinatesRow = row;
            this.XCoordinatesColumn = column;
        }
        public Integer getXCoordinatesRow() {
            return this.XCoordinatesRow;
        }
        public Integer getXCoordinatesColumn() {
            return this.XCoordinatesColumn;
        }
    }

    public static void solveProblem4() {
        // Create 2D array for data
        // ArrayList<ArrayList<String>> grid = new ArrayList<>(); 
        // grid.add(new ArrayList<>(Arrays.asList("A", "B", "C")));
        // grid.add(new ArrayList<>(Arrays.asList("A", "B", "C")));
        // grid.add(new ArrayList<>(Arrays.asList("A", "B", "C")));
        // System.out.println(grid.get(1).get(2));

        Problem4 p4 = new Problem4();
        ArrayList<ArrayList<String>> grid = p4.readFile();
        Integer countOfXmasWord = p4.findAllXmas(grid);

        System.out.println("The word Xmas is found: " + countOfXmasWord.toString());

        Integer countofXMASOccurances = p4.findAllMAS(grid);
        System.out.println("The word MAS in X occurs: " + countofXMASOccurances.toString());
    }

    public ArrayList<ArrayList<String>> readFile() {
        ArrayList<ArrayList<String>> inputGrid = new ArrayList<>();
        File inputFile = new File("/Users/calebcheehongteo/code/adventOfCode24/app/src/main/java/adventofcode24/day4/input.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))){
            String line;
            while ((line = reader.readLine()) != null) {
                ArrayList<String> lineAsArray = new ArrayList<>();
                for (int i = 0; i < line.length(); i++) {
                    char letter = line.charAt(i);
                    lineAsArray.add(String.valueOf(letter));
                }
                inputGrid.add(lineAsArray);
            }
        } catch (Exception e) {
            System.err.println("Exception");
            System.err.println(e.getMessage());
        }
        return inputGrid;
    }

    public void printGrid(ArrayList<ArrayList<String>> grid) {
        for (int row = 0; row < grid.size(); row++) {
            for (int column = 0; column < grid.get(row).size(); column++) {
                System.out.print(grid.get(row).get(column));
            }
            System.out.println("");
        }
    }

    public Integer findAllXmas(ArrayList<ArrayList<String>> grid) {
        Integer countOfXmas = 0;

        // Cycle through grid. If find X then check all directions for the remain 'MAS'
        for (int row = 0; row < grid.size(); row++) {
            // At the row level 
            Integer rowLength = grid.get(row).size();
            for (int column = 0; column < rowLength; column++) {
                // At the key level
                String letter = grid.get(row).get(column); 
                if ("X".equals(letter)) {
                    // Check all directions
                    countOfXmas = countOfXmas + foundXAmount(grid, row, column);
                }
            }
            
        }
        
        return countOfXmas;
    }

    public Integer findAllMAS(ArrayList<ArrayList<String>> grid) {
        Integer countOfMAS = 0;

        // Cycle through grid. If find X then check all directions for the remain 'MAS'
        for (int row = 0; row < grid.size(); row++) {
            // At the row level 
            Integer rowLength = grid.get(row).size();
            for (int column = 0; column < rowLength; column++) {
                // At the key level
                String letter = grid.get(row).get(column); 
                if ("M".equals(letter)) {
                    // Check all directions
                    countOfMAS = countOfMAS + findMASDiagonals(grid, row, column);
                }
            }
            
        }

        return countOfMAS; 
    }

    public Integer foundXAmount(ArrayList<ArrayList<String>> grid, Integer row, Integer column) {
        Integer countOfXmasFound = 0;

        ArrayList<String> allDirections = new ArrayList<>(); 
        Integer leftBoundary = 0;
        Integer rightBoundary = grid.get(0).size();
        Integer upperBoundary = 0;
        Integer bottomBoundary = grid.size();

        // 8 directions, forwards, backwards, up, down, Left-Up Diagonal, Left-Down Diagonal, Right-Up Diagonal, Right-Down Diagonal
        //Forwards
        if (column + 3 < rightBoundary){
            allDirections.add(grid.get(row).get(column) + grid.get(row).get(column + 1) + grid.get(row).get(column + 2) + grid.get(row).get(column + 3));
        }
        // Backwards
        if (column - 3 >= leftBoundary) {
            allDirections.add(grid.get(row).get(column) + grid.get(row).get(column - 1) + grid.get(row).get(column - 2) + grid.get(row).get(column - 3));
        }        
        // Up
        if (row - 3 >= upperBoundary) {
            allDirections.add(grid.get(row).get(column) + grid.get(row - 1).get(column) + grid.get(row - 2).get(column) + grid.get(row - 3).get(column));
        }
        // Down
        if (row + 3 < bottomBoundary) {
            allDirections.add(grid.get(row).get(column) + grid.get(row + 1).get(column) + grid.get(row + 2).get(column) + grid.get(row + 3).get(column));
        }
        // Left-Up Diagonal
        if (row - 3 >= upperBoundary && column - 3 >= leftBoundary) {
            allDirections.add(grid.get(row).get(column) + grid.get(row - 1).get(column - 1) + grid.get(row - 2).get(column - 2) + grid.get(row - 3).get(column - 3));
        }
        // Right-Down Diagonal
        if (row + 3 < bottomBoundary && column + 3 < rightBoundary) {
            allDirections.add(grid.get(row).get(column) + grid.get(row + 1).get(column + 1) + grid.get(row + 2).get(column + 2) + grid.get(row + 3).get(column + 3));
        }
        // Left-Down Diagonal
        if (row + 3 < bottomBoundary && column - 3 >= leftBoundary) {
            allDirections.add(grid.get(row).get(column) + grid.get(row + 1).get(column - 1) + grid.get(row + 2).get(column - 2) + grid.get(row + 3).get(column - 3));
        }
        // Right-Up Diagonal
        if (row - 3 >= upperBoundary && column + 3 < rightBoundary) {
            allDirections.add(grid.get(row).get(column) + grid.get(row - 1).get(column + 1) + grid.get(row - 2).get(column + 2) + grid.get(row - 3).get(column + 3));
        }

        for (String word : allDirections) {
            if ("XMAS".equals(word)) {
                countOfXmasFound++; 
            }
        }
        
        return countOfXmasFound;
    }

    public Integer findMASDiagonals(ArrayList<ArrayList<String>> grid, Integer row, Integer column) {
        Integer countOfMASFound = 0;
    
        ArrayList<WordWithDirection> allDirections = new ArrayList<>();
        Integer leftBoundary = 0;
        Integer rightBoundary = grid.get(0).size();
        Integer upperBoundary = 0;
        Integer bottomBoundary = grid.size();

        // 4 diagonal directions, Left-Up Diagonal, Left-Down Diagonal, Right-Up Diagonal, Right-Down Diagonal
    
        // Left-Up Diagonal
        if (row - 2 >= upperBoundary && column - 2 >= leftBoundary) {
            String word = (grid.get(row).get(column) + grid.get(row - 1).get(column - 1) + grid.get(row - 2).get(column - 2));
            allDirections.add(new WordWithDirection(row, column, leftUpEncoding, word));
        }
        // Right-Down Diagonal
        if (row + 2 < bottomBoundary && column + 2 < rightBoundary) {
            String word = grid.get(row).get(column) + grid.get(row + 1).get(column + 1) + grid.get(row + 2).get(column + 2);
            allDirections.add(new WordWithDirection(row, column, rightDownEncoding, word));
        }
        // Left-Down Diagonal
        if (row + 2 < bottomBoundary && column - 2 >= leftBoundary) {
            String word = grid.get(row).get(column) + grid.get(row + 1).get(column - 1) + grid.get(row + 2).get(column - 2);
            allDirections.add(new WordWithDirection(row, column, leftDownEncoding, word));
        }
        // Right-Up Diagonal
        if (row - 2 >= upperBoundary && column + 2 < rightBoundary) {
            String word = grid.get(row).get(column) + grid.get(row - 1).get(column + 1) + grid.get(row - 2).get(column + 2);
            allDirections.add(new WordWithDirection(row, column, rightUpEncoding, word));
        }

        for (WordWithDirection word : allDirections) {
            if ("MAS".equals(word.getWord())) {
                if (isNewXMas(grid, word)) {
                    // Occurance - Check if already mapped.
                    String keyToCheck = constructKey(word.getXCoordinatesRow(), word.getXCoordinatesColumn());
                    if (!crossPairingsFound.containsKey(keyToCheck)) {
                        // Does not contain reverse look up. Then count and add normal
                        crossPairingsFound.put(constructKey(word.row, word.column), keyToCheck);
                        countOfMASFound++;
                    }
                } 
            }
        }

        return countOfMASFound;
    }

    public boolean isNewXMas(ArrayList<ArrayList<String>> grid, WordWithDirection word) {
        boolean isAnOccurance = false;

        // 4 diagonal directions, Left-Up Diagonal, Left-Down Diagonal, Right-Up Diagonal, Right-Down Diagonal
        if (leftUpEncoding.equals(word.direction)) {
            // We know A is common. So we only need to check M and S in right order
            String checkRow = grid.get(word.row - 2).get(word.column);
            String checkColumn = grid.get(word.row).get(word.column - 2);
            if (checkRow.equals("S") && checkColumn.equals("M") || checkRow.equals("M") && checkColumn.equals("S")) {
                isAnOccurance = true;
                if (checkRow.equals("M")) {
                    word.setXCoordinates(word.row - 2, word.column);
                } else {
                    word.setXCoordinates(word.row, word.column - 2);
                }
            }
        } else if (leftDownEncoding.equals(word.direction)) {
            String checkRow = grid.get(word.row + 2).get(word.column);
            String checkColumn = grid.get(word.row).get(word.column - 2);
            if (checkRow.equals("S") && checkColumn.equals("M") || checkRow.equals("M") && checkColumn.equals("S")) {
                isAnOccurance = true;
                if (checkRow.equals("M")) {
                    word.setXCoordinates(word.row + 2, word.column);
                } else {
                    word.setXCoordinates(word.row, word.column - 2);
                }
            }
        } else if (rightUpEncoding.equals(word.direction)) {
            String checkRow = grid.get(word.row - 2).get(word.column);
            String checkColumn = grid.get(word.row).get(word.column + 2);
            if (checkRow.equals("S") && checkColumn.equals("M") || checkRow.equals("M") && checkColumn.equals("S")) {
                isAnOccurance = true;
                if (checkRow.equals("M")) {
                    word.setXCoordinates(word.row - 2, word.column);
                } else {
                    word.setXCoordinates(word.row, word.column + 2);
                }
            }
        } else if (rightDownEncoding.equals(word.direction)) {
            String checkRow = grid.get(word.row + 2).get(word.column);
            String checkColumn = grid.get(word.row).get(word.column + 2);
            if (checkRow.equals("S") && checkColumn.equals("M") || checkRow.equals("M") && checkColumn.equals("S")) {
                isAnOccurance = true;
                if (checkRow.equals("M")) {
                    word.setXCoordinates(word.row + 2, word.column);
                } else {
                    word.setXCoordinates(word.row, word.column + 2);
                }
            }
        }

        return isAnOccurance;
    } 

    public String constructKey(Integer row, Integer column) {
        return row.toString() + ", " + column.toString(); 
    }
}