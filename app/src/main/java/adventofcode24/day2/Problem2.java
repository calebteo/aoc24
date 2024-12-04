package adventofcode24.day2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Problem2 {

    public static void main(String[] args) {
        solveProblem2();
    }

    private Integer incorrectIndexLeft = -1;
    private Integer incorrectIndexRight = -1;

    public static void solveProblem2() {
        Problem2 p2 = new Problem2();
        p2.readFile();

        // p2.printOutValues();

        Integer numOfSafeLevels = p2.calculateSafeReports();
        System.out.println("Safe Levels = " + numOfSafeLevels.toString());
        
    }

    ArrayList<Integer[]> listOfLevels = new ArrayList();

    public void readFile() {
        File inputFile = new File("/Users/calebcheehongteo/code/adventOfCode24/app/src/main/java/adventofcode24/day2/input.txt");
        try (BufferedReader TSVReader = new BufferedReader(new FileReader(inputFile))){
            String line;
            while ((line = TSVReader.readLine()) != null) {
                String[] lineItems = line.split(" ");
                Integer[] integerItems = new Integer[lineItems.length];
                for (int i = 0; i < lineItems.length; i++) {
                    integerItems[i] = Integer.valueOf(lineItems[i]);
                }
                listOfLevels.add(integerItems);
            }
        } catch (Exception e) {
            System.err.println("Exception");
            System.err.println(e.getMessage());
        }
    }

    public void printOutValues() {
        System.out.println("Printing Input");
        for (Integer[] list : listOfLevels) {
            for (Integer i : list) {
                System.out.print(i.toString() + " ");
            }
            System.out.println("");
        }
    }

    public Integer calculateSafeReports() {
        Integer countOfSafe = 0; 
        int testCounter = 0;
        
        for (Integer[] report : listOfLevels) {
            testCounter++;
            // System.err.println(testCounter);

            boolean safe = checkReport(report);
            
            if(!safe) {
                Integer numOfSafe = 0; 
                // boolean removedSameNumber = false;
                boolean removedTopBottomSafe = false;
                for (int index = 0; index < report.length - 1; index++) {
                    if (index != 0 && report[index] == report[index - 1]){
                        // removedSameNumber = true;
                        continue;
                    }
                    Integer[] updatedArray = updateArray(report, index);
                    if (checkReport(updatedArray)) {
                        numOfSafe++;
                        if (index == 0 || index == report.length - 1) {
                            removedTopBottomSafe = !removedTopBottomSafe;
                        }
                    }
                }
                // Integer numOfSafe = 0;
                // Integer[] removedLeftArray = updateArray(report, incorrectIndexLeft);
                // if (checkReport(removedLeftArray)) {
                //     numOfSafe++;
                // }
                // if (incorrectIndexLeft + 1 < report.length - 1) {
                //     Integer[] removedRightArray = updateArray(report, incorrectIndexRight);
                //     if (checkReport(removedRightArray)){
                //         numOfSafe++;
                //     }
                // }
                // if (numOfSafe >= 1) {
                //     safe = true;
                // }
                
                if (numOfSafe == 1) {
                    safe = true;
                }
            }
            if (safe) {
                countOfSafe++; 
            } 
            // else {
            //     for(Integer i : report){
            //         System.err.print(i.toString() + " ");
            //     }
            //     System.err.println("");
            // }
        }
        
        return countOfSafe;
    }

    public Integer[] updateArray(Integer[] array, Integer indexToRemove) {
        Integer[] likeItNeverHappened = new Integer[array.length - 1];

        if (indexToRemove == array.length - 2) {
            System.arraycopy(array, 0, likeItNeverHappened, 0, likeItNeverHappened.length);
        } else {
            for (int i = 0, j = 0; j < array.length; i++, j++) {
                if (j == indexToRemove) {
                    j = j + 1;
                }
                likeItNeverHappened[i] = array[j];
            }
        }
        return likeItNeverHappened;
    }

    public boolean checkReport(Integer[] report) {
        boolean increasing = false;
        boolean safe = true;
        for (int index = 0; index < report.length - 1; index++) {
            if (index == 0) {
                // First value to determine increasing or decreasing
                if (report[index] < report[index+1]) {
                    increasing = true;
                }
            }

            boolean twoLevelIsSafe = isTwoLevelsSafe(report, index, index + 1, increasing);

            if (!twoLevelIsSafe) {
                incorrectIndexLeft = index;
                safe = false;
                break;
            }
            // if (!twoLevelIsSafe) {
                
            //     if (index + 2 > report.length - 1) {
            //         break;
            //     }
            //     // Check if remove of next value is safe
            //     boolean isSkipLevelSafe = isTwoLevelsSafe(report, index, index + 2, increasing);
            //     if (!isSkipLevelSafe) {
            //         safe = false;
            //         break;
            //     }
            //     index = index + 1;
            // }
        }
        return safe;
    }

    public boolean isTwoLevelsSafe(Integer[] report, Integer indexLevelOne, Integer indexLevelTwo, boolean increasing) {
        Integer diff = report[indexLevelOne] - report[indexLevelTwo];
        if (Math.abs(diff) > 3 || diff == 0) {
            // Change is outside safe
            return false;
        }

        // If diff is greater than 0, next index is less. Therefore decreasing
        // If diff is less than 0, next index is greater. Therefore increasing
        if ((diff > 0 && increasing) || diff < 0 && !increasing) {
            // We are no following increasing or decreasing pattern
            return false;
        }

        return true;
    }
}