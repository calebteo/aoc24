package adventofcode24.day1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Objects;

public class Problem1 {

    public static void main(String[] args) {
        solveProblem1();
    }

    ArrayList<Integer> ColumnOne = new ArrayList<>();
    ArrayList<Integer> ColumnTwo = new ArrayList<>();

    public static void solveProblem1() {
        Problem1 p1 = new Problem1();
        p1.readFile();
        
        p1.selectionSort(p1.ColumnOne);
        p1.selectionSort(p1.ColumnTwo);

        Integer answer = p1.calculateDistance();
        System.out.println("Answer 1: " + answer);

        Integer answer2 = p1.calculateSimilarityScore();
        System.out.println("Answer 2: " + answer2);
    }

    
    public Integer calculateDistance(){
        Integer totalDistance = 0; 

        for (Integer index = 0; index < ColumnOne.size(); index++) {
            totalDistance = totalDistance + Math.abs(ColumnOne.get(index) - ColumnTwo.get(index));
        }

        return totalDistance;
    }

    public Integer calculateSimilarityScore() {
        Integer SimilarityScore = 0; 

        for(Integer value : ColumnOne) {
            Integer countOccurance = 0; 
            for (Integer rightColumValue : ColumnTwo) {
                if (Objects.equals(value, rightColumValue)) {
                    countOccurance++;
                }
            }
            SimilarityScore = SimilarityScore + (value * countOccurance);
        }

        return SimilarityScore;
    }

    public void readFile() {
        File inputFile = new File("/Users/calebcheehongteo/code/adventOfCode24/app/src/main/java/adventofcode24/day1/input.txt");
        try (BufferedReader TSVReader = new BufferedReader(new FileReader(inputFile))){
            String line;
            while ((line = TSVReader.readLine()) != null) {
                String[] lineItems = line.split("   ");
                ColumnOne.add(Integer.valueOf(lineItems[0].trim()));
                ColumnTwo.add(Integer.valueOf(lineItems[1].trim()));
            }
        } catch (Exception e) {
            System.err.println("Exception");
            System.err.println(e.getMessage());
        }
    }

        public void selectionSort(ArrayList<Integer> list) {
            for (int i = 0; i < list.size(); i++) {
                Integer smallestValue = list.get(i);
                Integer smallestValueIndex = i; 
                for (int j = i; j < list.size(); j++) {
                    Integer maybeSmallestValue = list.get(j);
                    if (maybeSmallestValue < smallestValue) {
                        smallestValue = maybeSmallestValue;
                        smallestValueIndex = j;
                    }
                }
                swap(list, i, smallestValueIndex); 
            }
        }


    public void swap(ArrayList<Integer> list, int indexA, int indexB) {
        Integer temp = list.get(indexA);
        list.set(indexA, list.get(indexB));
        list.set(indexB, temp);
    }   
}