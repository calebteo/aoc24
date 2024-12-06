package adventofcode24.day5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Problem5 {

    public static void main(String[] args) {
        Problem5.solveProblem5();
    }

    String pageOrderFile = "/Users/calebcheehongteo/code/adventOfCode24/app/src/main/java/adventofcode24/day5/pageOrder.txt";
    String rulesFile = "/Users/calebcheehongteo/code/adventOfCode24/app/src/main/java/adventofcode24/day5/rules.txt";

    public class Rules {
        Set<Integer> beforeThis = new HashSet<>();
    }
    Map<Integer, Rules> allRules = new HashMap<>(); 
    ArrayList<ArrayList<Integer>> pageOrders = new ArrayList<>();
    ArrayList<ArrayList<Integer>> invalidOrders = new ArrayList<>();

    public static void solveProblem5() {
        Problem5 p5 = new Problem5(); 
        p5.readRules();
        p5.readPageOrders();

        ArrayList<ArrayList<Integer>> validOrders = p5.validPageOrders(p5.pageOrders);
        Integer sum = p5.calculateSumOfMiddleList(validOrders);
        System.out.println("The total of middle values: " + sum.toString());

        p5.sortInvalidPageOrders();
        Integer sumOfSorted = p5.calculateSumOfMiddleList(p5.invalidOrders);
        System.out.println("The total of middle values after sorting: " + sumOfSorted.toString());
    }

    public static void printOutRule(Integer key, Map<Integer, Rules> allRules) {
        System.out.println(key.toString() + " - Rules: " + allRules.get(key).beforeThis.toString());
    }

    public void readRules() {
        File inputFile = new File(rulesFile);
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))){
            String line;
            while ((line = reader.readLine()) != null) {
                Integer key = Integer.valueOf(line.split("\\|")[0]);
                Integer rule = Integer.valueOf(line.split("\\|")[1]);
                if (allRules.containsKey(key)) {
                    Rules addToRules = allRules.get(key);
                    addToRules.beforeThis.add(rule);
                    allRules.put(key, addToRules);
                } else {
                    Rules newRules = new Rules(); 
                    newRules.beforeThis.add(rule);
                    allRules.put(key, newRules);
                }
            }
        } catch (Exception e) {
            System.err.println("Exception");
            System.err.println(e.getMessage());
        }
    }

    public void readPageOrders() {
        File inputFile = new File(pageOrderFile);
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))){
            String line;
            while ((line = reader.readLine()) != null) {
                String[] splitLine = line.split(",");
                ArrayList<Integer> listToAdd = new ArrayList<>();
                for (String l : splitLine) {
                    listToAdd.add(Integer.valueOf(l));   
                }
                pageOrders.add(listToAdd);
            }
        } catch (Exception e) {
            System.err.println("Exception");
            System.err.println(e.getMessage());
        }
    }

    public ArrayList<ArrayList<Integer>> validPageOrders(ArrayList<ArrayList<Integer>> pageOrdersList) {
        ArrayList<ArrayList<Integer>> listOfValidPageOrder = new ArrayList<>(); 

        for (ArrayList<Integer> order : pageOrdersList) {
            boolean isCorrectOrder = isListInCorrectOrder(order);
            
            if (isCorrectOrder) {
                listOfValidPageOrder.add(order);
            } else {
                invalidOrders.add(order);
            }
        }

        return listOfValidPageOrder; 
    }

    public void sortInvalidPageOrders() {
        for (ArrayList<Integer> order : invalidOrders) {
            boolean isCorrectOrder = isListInCorrectOrder(order);
            while(!isCorrectOrder) {
                isCorrectOrder = isListInCorrectOrder(order);
            }
        }
    }

    public Integer calculateSumOfMiddleList(ArrayList<ArrayList<Integer>> list) {
        Integer sum = 0;
        
        for (ArrayList<Integer> l : list) {
            sum = sum + l.get(l.size()/2);
        }

        return sum;
    }

    public boolean isListInCorrectOrder(ArrayList<Integer> order) {
        boolean isCorrectOrder = true;

        for(int index = 0; index  < order.size(); index++) {
            Integer currentKey = order.get(index); // e.g. 75
            Rules currentRule = allRules.get(currentKey); // e.g. Set of rules 75|3, 75|9 etc. 
            // if there isnt a rule then it's all good. 
            if (currentRule == null) {
                continue;
            }
            Set<Integer> valuesBeforeKey = null;
            Set<Integer> valuesAheadOfKey = null;
            if (index == order.size() - 1) {
                valuesBeforeKey = new HashSet<>(order.subList(0, order.size() - (order.size() - index)));
                valuesAheadOfKey = Set.of();
            } else if (index == 0) {
                valuesAheadOfKey = new HashSet<>(order.subList(index+1, order.size() - 1));
                valuesBeforeKey = Set.of();
            } else {
                valuesAheadOfKey = new HashSet<>(order.subList(index+1, order.size() - 1));
                valuesBeforeKey = new HashSet<>(order.subList(0, order.size() - (order.size() - index)));
            }
            
            // Check Set of values against Rules Set. If empty then valid, otherwise not valid
            if (!valuesAheadOfKey.isEmpty()) {
                valuesAheadOfKey.removeAll(currentRule.beforeThis);
                if (!valuesAheadOfKey.isEmpty()) {
                    isCorrectOrder = false;
                    
                    int indexToShuffle = -1;
                    for (indexToShuffle = order.size() - 1; indexToShuffle > 0; indexToShuffle--) {
                        if (valuesAheadOfKey.contains(order.get(indexToShuffle))) {
                            // found highest value to shuffle back to.
                            break;
                        }
                    }
                     
                    for (int j = index + 1; j <= indexToShuffle; j++) {
                        order.set(j - 1, order.get(j));
                    }
                    order.set(indexToShuffle, currentKey);
                    
                    break;
                }
            }
            // Check values before. If set does not match the copy (i.e. a change) then it does not follow the order.  
            if (!valuesBeforeKey.isEmpty()) {
                Set<Integer> copyOfBefore = new HashSet<>(valuesBeforeKey);
                valuesBeforeKey.removeAll(currentRule.beforeThis);
                if (!valuesBeforeKey.containsAll(copyOfBefore)) {
                    isCorrectOrder = false;
                    
                    // Shift the currentKey back to a suitable spots. Must find the suitable spot with IndexToShuffle. 
                    int indexToShuffle = -1;
                    for (indexToShuffle = 0; indexToShuffle < index; indexToShuffle++) {
                        if (!valuesBeforeKey.contains(order.get(indexToShuffle))) {
                            // found lowest value to shuffle back to.
                            break;
                        }
                    }
                    // Shuffle all values back
                    for (int j = index - 1; j >= indexToShuffle; j--) {
                        order.set(j + 1, order.get(j));
                    }
                    order.set(indexToShuffle, currentKey);
                    
                    break;
                }
            }
        }
        return isCorrectOrder;
    }

    public void swap(ArrayList<Integer> order, Integer indexA, Integer indexB) {
        Integer tmp = order.get(indexA);
        order.set(indexA, order.get(indexB));
        order.set(indexB, tmp);
    }
}