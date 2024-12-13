package adventofcode24.day7;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Problem7 {

    public static void main (String[] args) {
        Problem7 p7 = new Problem7();
        p7.solveProblem7();
    }

    String inputFile = "/Users/calebcheehongteo/code/adventOfCode24/app/src/main/java/adventofcode24/day7/input.txt";

    public ArrayList<Long> answers = new ArrayList<>(); 
    public ArrayList<ArrayList<Long>> listOfVariables = new ArrayList<>(); 
    public ArrayList<ArrayList<OPERATOR>> listOfOperators = new ArrayList<>(); 
    public ArrayList<Long> correctAnswers = new ArrayList<>();

    public enum OPERATOR {
        PLUS("1"), MINUS("2"), MULTIPLE("3"), DIVIDE("4");

        String key;

        OPERATOR(String key) { this.key = key;}
        OPERATOR() {}

        public static OPERATOR getValue(String x) {
            switch (x) {
                case "1": 
                    return PLUS;
                case "2": 
                    return MINUS;
                case "3": 
                    return MULTIPLE;
                case "4": 
                    return DIVIDE;
                default:
                    throw new AssertionError();
            }
        }
    }   

    public static Long doOperation(Long v1, Long v2, OPERATOR operator) {
        switch (operator) {
            case OPERATOR.PLUS:
                return v1 + v2; 
            case OPERATOR.MINUS:
                return v1 - v2; 
            case OPERATOR.MULTIPLE:
                return v1 * v2; 
            case OPERATOR.DIVIDE:
                return v1 / v2;
            default:
                throw new AssertionError();
        }
    }

    class RunnableDemo implements Runnable {
        private Thread t;
        private String threadName;
        private ArrayList<ArrayList<Long>> variables;
        private ArrayList<Long> answers;
        public Problem7 p = new Problem7();

        
        RunnableDemo(String name, ArrayList<ArrayList<Long>> variables, ArrayList<Long> answers) {
            this.variables = variables;
            this.answers = answers;
            this.threadName = name;

        }
        
        public void run() {
            System.out.println("Running " +  threadName );
            try {
                for (int index = 0; index < this.answers.size(); index++) {
                    ArrayList<OPERATOR> validOperations = this.p.getListOfValidOperator(this.variables.get(index), this.answers.get(index));
                    if (!validOperations.isEmpty()) {
                        this.p.correctAnswers.add(this.answers.get(index));
                    }
            }
            } catch (Exception e) {
                System.err.println("Some Exception:");
                System.err.println(e.getMessage());
            }
            System.out.println("Thread " +  threadName + " exiting.");
        }
        
        public void start () {
            System.out.println("Starting " +  threadName );
            if (t == null) {
                t = new Thread (this, threadName);
                t.start ();
            }
        }
    }

    public void solveProblem7() {
        Problem7 p7 = new Problem7(); 
        p7.readFile();
        // for (int i = 0; i < p7.answers.size(); i++) {
        //     System.out.println(p7.answers.get(i) + " : " + p7.listOfVariables.get(i).toString());
        // }

        boolean runMulti = false;

        if(runMulti){
            runMultiThreaded(p7);
        } else {
            // runSingleThread(p7);
            Long currentSum = 0L;
            for (int i = 0; i < p7.answers.size(); i++) {
                if (foundOperationsToAnswer(0, p7.listOfVariables.get(i), 0L, p7.answers.get(i))){
                    currentSum = currentSum + p7.answers.get(i);
                }
            }
            System.out.println("Answer: " + currentSum.toString());
        }
        
    }

    public void runSingleThread(Problem7 p7) {
        Long sumAnswer = 0L;
        for (int index = 0; index < p7.answers.size(); index++) {
            ArrayList<OPERATOR> validOperations = p7.getListOfValidOperator(p7.listOfVariables.get(index), p7.answers.get(index));
            if (!validOperations.isEmpty()) {
                p7.correctAnswers.add(p7.answers.get(index));
                sumAnswer = sumAnswer + p7.answers.get(index);
            }
        }
        System.out.println("Answer for sum of correct lines = " + sumAnswer.toString());
    }

    public void runMultiThreaded(Problem7 p7) {
        ArrayList<RunnableDemo> listOfThreads = new ArrayList<>();
        int segments = 10;

        for (int split = 0; split < segments; split++) {
            int dividor = p7.answers.size() / segments; 
            int startIndex = split * dividor; 
            int endIndex = startIndex + dividor;
            ArrayList<ArrayList<Long>> subSet = new ArrayList<>(p7.listOfVariables.subList(startIndex, endIndex));
            ArrayList<Long> subSetAnswers = new ArrayList<>(p7.answers.subList(startIndex, endIndex));
            RunnableDemo runnableSlice = new RunnableDemo(String.valueOf(split), subSet, subSetAnswers);
            listOfThreads.add(runnableSlice);
            runnableSlice.start();
        }

        for(int i = 0; i < segments; i++) {
            try {
                Thread thread = listOfThreads.get(i).t;
                if (thread != null) {
                    thread.join();
                } else {
                    System.out.println("No thread for " + i + ". Skipping for now.");
                }
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted.");
            }
        }
        Long sum = 0L;
        for (RunnableDemo r : listOfThreads) {
            for (Long answer : r.p.correctAnswers) {
                sum = sum + answer;
            }
        }
        System.out.println("Answer for sum of correct lines = " + sum.toString());
    }

    public void readFile() {
        File file = new File(inputFile);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line; 
            while ((line = reader.readLine()) != null) {
                String[] answerAndVariables = line.split(":");
                answers.add(Long.valueOf(answerAndVariables[0]));
                
                String[] variables = answerAndVariables[1].split(" ");
                ArrayList<Long> list = new ArrayList<>();
                for (String v : variables) {
                    if (v.isBlank()) {
                        continue;
                    }
                    list.add(Long.valueOf(v));
                }
                listOfVariables.add(list);
            }
        } catch (Exception e) {
            System.err.println("Exception");
            System.err.println(e.getMessage());
        }
    }

    public boolean foundOperationsToAnswer(int n, ArrayList<Long> variables, Long currentSum, Long target) {
        if (n == 0) {
            return foundOperationsToAnswer(1, variables, variables.get(0), target);
        }

        if (n == variables.size() - 1) {
            return (
                currentSum + variables.get(n) == target || 
                currentSum *  variables.get(n) == target || 
                concatNum(currentSum, variables.get(n)).equals(target));
        }

        return (
            foundOperationsToAnswer(n + 1, variables, currentSum + variables.get(n), target) || 
            foundOperationsToAnswer(n + 1, variables, currentSum * variables.get(n), target) || 
            foundOperationsToAnswer(n + 1, variables, concatNum(currentSum, variables.get(n)), target));
    }

    public Long concatNum(Long num, Long addTo) {
        return ((num * 10) + addTo);
    }

    public Long concatNumWithString(Long num, Long addTo) {
        String numString = String.valueOf(num);
        String addToString = String.valueOf(addTo);
        return (Long.valueOf(numString + addToString));
    }

    public ArrayList<OPERATOR> getListOfValidOperator(ArrayList<Long> variables, Long answer) {
        ArrayList<OPERATOR> validOperator = new ArrayList<>();
        Map<String, ArrayList<OPERATOR>> allPossiblePermutation = new HashMap<>();
        // Go through every permutation of operators until find the right answer.
        // Only have to go through + and x
        // Loop 1 - construct permutation 
        ArrayList<OPERATOR> operatorsToUse = new ArrayList<>(Arrays.asList(OPERATOR.PLUS, OPERATOR.MULTIPLE));
        // for (int plus = 0; plus < operatorsToUse.size(); plus++) {
        //     for (int mut = 0; mut < operatorsToUse.size(); mut++) {
        //         for (int index = 0; index < variables.size() - 1; index++) {
        //             ArrayList<OPERATOR> permutation = new ArrayList<>();
        //             for (int until = 0; until < index; until++) {
        //                 permutation.add(operatorsToUse.get(plus));
        //             }
        //             for (int end = index; end < variables.size() - 1; end++) {
        //                 permutation.add(operatorsToUse.get(mut));
        //             }
        //             if ((constructKey(permutation).indexOf('1') != -1) && (constructKey(permutation).indexOf('3') != -1)) {
        //                 recursiveGetPermutation(permutation.size(), permutation, allPossiblePermutation);
        //             } else {
        //                 allPossiblePermutation.put(constructKey(permutation), permutation);
        //             }
        //         }
        //     }
        // }

        for (int index = 0; index < variables.size() - 1; index++) {
            ArrayList<OPERATOR> permutation = new ArrayList<>();
            for (int until = 0; until < index; until++) {
                permutation.add(operatorsToUse.get(0));
            }
            for (int end = index; end < variables.size() - 1; end++) {
                permutation.add(operatorsToUse.get(1));
            }
            if ((constructKey(permutation).indexOf('1') != -1) && (constructKey(permutation).indexOf('3') != -1)) {
                recursiveGetPermutation(permutation.size(), permutation, allPossiblePermutation);
            } else {
                String key = constructKey(permutation);
                if (key.indexOf('1') == -1 ) { // Multiple only scenario. Short Circuit check.
                    Long sum = variables.get(0);
                    for (int check = 1; check < variables.size(); check++) {
                        sum = doOperation(sum, variables.get(check), permutation.get(check - 1));
                    }
                    if (sum.equals(answer)) {
                        return permutation;
                    } 
                    // else if (sum > answer) { //BUG FOUND... SUM < ANSWER..
                    //     return validOperator;
                    // }
                }
                allPossiblePermutation.put(key, permutation);    
            }
        }


        for (ArrayList<OPERATOR> operations : allPossiblePermutation.values()) {
            Long sum = variables.get(0);
            for (int index = 1; index < variables.size(); index++) {
                sum = doOperation(sum, variables.get(index), operations.get(index - 1));
            }
            if (sum.equals(answer)){
                // Break after one and move on.
                validOperator = operations;
                return validOperator;
            }
        }

        return validOperator;
    }



    public void swap(ArrayList<OPERATOR> list, Integer a, Integer b) {
        OPERATOR tmp = list.get(a);
        list.set(a, list.get(b));
        list.set(b, tmp);
    }

    public void recursiveGetPermutation(int n, ArrayList<OPERATOR> elements, Map<String, ArrayList<OPERATOR>> allPossiblePerm) {
        if(n == 1) {
            if (!checkIfInMap(elements, allPossiblePerm)) {
                ArrayList<OPERATOR> copy = new ArrayList(elements);
                allPossiblePerm.put(constructKey(elements), copy);
            }
        } else {
            for(int i = 0; i < n-1; i++) {
                recursiveGetPermutation(n - 1, elements, allPossiblePerm);
                if(n % 2 == 0) {
                    swap(elements, i, n-1);
                } else {
                    swap(elements, 0, n-1);
                }
            }
            recursiveGetPermutation(n - 1, elements, allPossiblePerm);
        }
    }

    public boolean checkIfInMap(ArrayList<OPERATOR> elements, Map<String, ArrayList<OPERATOR>> allPossiblePerm) {
        return allPossiblePerm.containsKey(constructKey(elements));
    }

    public String constructKey(ArrayList<OPERATOR> elements) {
        String key = "";
        for (OPERATOR e : elements) {
            key = key + e.key + "-";
        }
        return key;
    }
} 

/*
    Answer for sum of correct lines = 663 613 325 064 (TooLow)

    Answer Part 1: 636211973358 (without the short circuit of over the value)
    Answer Part 1: 663613490587 (Right)
    Answer Part 2: 848109496206 (too low)
    Answer Part 2: 936292510338 (too low)
*/
