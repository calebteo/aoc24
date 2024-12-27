package adventofcode24.day11;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Problem11 {
    
    public static void main(String[] args) {
        Problem11 p = new Problem11();
        p.solve();
    }

    String inputFile = "/Users/calebcheehongteo/code/adventOfCode24/app/src/main/java/adventofcode24/day11/input.txt";
    String initStones;
    ArrayList<String> stones = new ArrayList<>();
    Map<String, Long> occurances = new HashMap<>(); 

    public void solve() {
        readFile();
        System.out.println(stones.toString());
        int sizeAfter25Blinks = runForNumberOfBlinks(25, stones); 
        System.out.println("Answer Part 1: " + sizeAfter25Blinks);

        Long sumOf75Blinks = 0L;
        for (String s : stones) {
            sumOf75Blinks += fullRecursion(75, s);
        }
        System.out.println(occurances.toString());

        System.out.println("Answer Part 2: " + sumOf75Blinks);
    }

    public void readFile() {
        File file = new File(inputFile); 
        try (BufferedReader buff = new BufferedReader(new FileReader(file))) {
            initStones = buff.readLine();
            for (String s : initStones.split(" ")) {
                stones.add(s);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    // Takes one stone
    // Calculate what happen to single stone
    // Returns the stone as list.

    public ArrayList<String> stonesAfterBlink(String stone) {
        ArrayList<String> result = new ArrayList<>();

        if (stone.equals("0")) {
            result.add("1");
        } else if (stone.length() % 2 == 0) {
            int halfWay = stone.length() / 2; 
            result.add(stone.substring(0, halfWay));
            // Need to get rid of leading zeros. Convert to int (might need to use long)
            Long secondHalf = Long.valueOf(stone.substring(halfWay, stone.length()));
            result.add(String.valueOf(secondHalf));
        } else {
            result.add(String.valueOf(Long.parseLong(stone) * 2024));
        }

        return result;
    }

    public int runForNumberOfBlinks(int numberOfBlinks, ArrayList<String> listOfStones) {
        ArrayList<String> intermediate = new ArrayList<>();

        for (int iterations = 0; iterations < numberOfBlinks; iterations++) {
            for (int index = 0; index < listOfStones.size(); index++) {
                ArrayList<String> afterBlink = stonesAfterBlink(listOfStones.get(index));
                intermediate.addAll(afterBlink);
            } 
            listOfStones = intermediate;
            intermediate = new ArrayList<>();
        }

        return listOfStones.size();
    }

    // Take single number approach rather than full list. 
    public int runForNumberOfBlinksForSingleStone(int numberOfBlinks, String stone) {
        ArrayList<String> intermediate = new ArrayList<>();
        ArrayList<String> runningList = new ArrayList<>(Arrays.asList(stone));

        for (int iterations = 0; iterations < numberOfBlinks; iterations++) {
            for (int index = 0; index < runningList.size(); index++) {
                ArrayList<String> afterBlink = stonesAfterBlink(runningList.get(index));
                intermediate.addAll(afterBlink);
            } 
            runningList = intermediate;
            intermediate = new ArrayList<>();
        }

        return runningList.size();
    }

    public Long fullRecursion(int numberOfBlinks, String stone) {
        if (numberOfBlinks == 0) {
            return 1L;
        } else {
            if (occurances.containsKey(constructKey(stone, numberOfBlinks))) {
                return occurances.get(constructKey(stone, numberOfBlinks));
            } else {
                ArrayList<String> afterBlink = stonesAfterBlink(stone);
                Long sum = 0L;
                for (String s : afterBlink) {
                    Long oc = fullRecursion(numberOfBlinks - 1, s);
                    occurances.put(constructKey(s, numberOfBlinks - 1), oc);
                    sum += oc;
                }
                return sum;
            }
        }
    }

    public String constructKey(String num, int blink) {
        return num + ":" + blink;
    }
}
