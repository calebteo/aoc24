package adventofcode24.day3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Problem3 {

    public static void main(String[] args) {
        solveProblem3();
    }

    String corruptedMemory;

    public static void solveProblem3() {
        Problem3 p3 = new Problem3();

        p3.readFile();
        // System.out.println(p3.corruptedMemory);

        ArrayList<String> correctMemory = p3.findCorrectMemory(p3.corruptedMemory);
        // for (String memory : correctMemory) {
        //     System.out.println(memory);
        // }
        Integer sumOfMemory = p3.calculateMemoryResult(correctMemory);
        System.out.println("Sum of corrected memory: " + sumOfMemory.toString());
        
        String augmentedMemory = p3.corruptedMemory;
        ArrayList<String> correctMemoryWithEnablement = p3.findCorrectMemoryUsingFlags(augmentedMemory);
        // System.out.println("Printing memory with enablement");
        // for (String memory : correctMemoryWithEnablement) {
        //     System.out.println(memory);
        // }
        Integer sumOfMemoryWithEnablement = p3.calculateMemoryResult(correctMemoryWithEnablement);
        
        System.out.println("Sum of correct memory with enablement: " + sumOfMemoryWithEnablement.toString());

    }

    public void readFile() {
        File inputFile = new File("/Users/calebcheehongteo/code/adventOfCode24/app/src/main/java/adventofcode24/day3/input.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))){
            String line;
            while ((line = reader.readLine()) != null) {
                corruptedMemory = corruptedMemory + line;
            }
        } catch (Exception e) {
            System.err.println("Exception");
            System.err.println(e.getMessage());
        }
    }

    public ArrayList<String> findCorrectMemory(String corrupt) {
        ArrayList<String> correctMemory = new ArrayList<>(); 

        Pattern pattern = Pattern.compile("(mul\\([0-9]+,[0-9]+\\))");
        Matcher matcher = pattern.matcher(corrupt);
        while (matcher.find()) {
            correctMemory.add(matcher.group());
        }

        return correctMemory;
    }

    public ArrayList<String> findCorrectMemoryWithEnablement(String corrupt) {
        String enabledMemory = "";

        // Pattern pattern = Pattern.compile("(?=do\\(\\))(mul\\([0-9]+,[0-9]+\\))(?=don\\'t\\(\\))");
        // Pattern pattern = Pattern.compile("(?<=do\\(\\))[^d]*?mul\\((\\d+),\\s*(\\d+)\\)[^d]*?(?=don't\\(\\))");
        Pattern pattern = Pattern.compile("(?<=do\\(\\)).*(?=don't\\(\\))");
        // Pattern pattern = Pattern.compile("(?=don't\\(\\)).*(?<=do\\(\\))"); //Reverse - get values we don't want.
        Matcher matcher = pattern.matcher(corrupt);
        while(matcher.find()) {
            enabledMemory = enabledMemory + matcher.group();
        }

        return findCorrectMemory(enabledMemory);
    }

    public ArrayList<String> findCorrectMemoryUsingFlags(String corrupt) {
        ArrayList<String> correctMemory = new ArrayList<>(); 
        boolean enable = true;

        Pattern pattern = Pattern.compile("(mul\\([0-9]+,[0-9]+\\))|(do\\(\\))|(don't\\(\\))");
        
        Matcher matcher = pattern.matcher(corrupt);
        while(matcher.find()) {
            String value = matcher.group();
            if ("don't()".equals(value)) {
                enable = false;
            } else if ("do()".equals(value)) {
                enable = true;
            } else {
                if (enable) {
                    correctMemory.add(matcher.group());        
                }
            }
        }
        return correctMemory;
    }

    public Integer calculateMemoryResult(ArrayList<String> fullMemory) {
        Integer sum = 0; 
        for (String memory : fullMemory) {
            // Pattern numPattern = Pattern.compile("([0-9]+)");
            // Matcher matcher = numPattern.matcher(memory);
            // Integer firstNum = Integer.valueOf(matcher.group());
            // Integer secondNum = Integer.valueOf(matcher.group());
            Integer firstNum = Integer.valueOf(memory.substring(memory.indexOf("(") + 1, memory.indexOf(",")));
            Integer secondNum = Integer.valueOf(memory.substring(memory.indexOf(",") + 1, memory.indexOf(")")));

            sum = sum + (firstNum * secondNum);
        }
        return sum;
    }
}

/* 
Answer tried:
    Sum of corrected memory: 155955228
    Sum of correct memory with enablement: 51441820 (too low)

    (didn't try - too low)
    Sum of corrected memory: 155955228
    Sum of correct memory with enablement: 50833945

    Sum of corrected memory: 155955228
    Sum of correct memory with enablement: 154956673 (too high)

    add 607707?
    Try: 52049527 (too low)

    Gotten by subtracting disabled values (didn't try - wayyyy too low)
    5168422

    Answer:
    Sum of corrected memory: 155955228
    Sum of correct memory with enablement: 100189366
*/