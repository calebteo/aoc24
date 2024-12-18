package adventofcode24.day9;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Problem9 {
    
    public static void main(String[] args) {
        Problem9 p9 = new Problem9(); 
        p9.solve();
    }

    String inputFile = "/Users/calebcheehongteo/code/adventOfCode24/app/src/main/java/adventofcode24/day9/input.txt";
    String diskMap = "";
    ArrayList<String> fileSystem = new ArrayList<>(); 

    public void solve() {
        readFile();
        createFileSystem();
        // System.out.println(diskMap);
        // System.out.println(fileSystem.toString());
        // reorderFileBlocks();
        reorderFileBlocksByBlocks();
        System.out.println(fileSystem.toString());
        Long checkSum = calculateCheckSum();
        System.out.println("Checksum: " + checkSum);
    }

    public void readFile() {
        File file = new File(inputFile);
        try (BufferedReader buffer = new BufferedReader(new FileReader(file))) {
            diskMap = buffer.readLine();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void createFileSystem() {
        int fileIDCounter = 0;
        boolean addingFile = true;
        for (int index = 0; index < diskMap.length(); index++) {
            Integer currentSize = Character.getNumericValue(diskMap.charAt(index));
            String characterToAdd = ".";
            if (addingFile) {
                characterToAdd = String.valueOf(fileIDCounter);
                fileIDCounter++;
            }
            
            for (int space = 0; space < currentSize; space++) {
                fileSystem.add(characterToAdd);
            }
            addingFile = !addingFile;
        }
    }

    public void reorderFileBlocks() {
        int start = 0;
        int end = fileSystem.size() - 1; 
        while (start < end) {
            boolean freeSpace = true;
            boolean haveFileBlockToMove = true;
            if (!fileSystem.get(start).equals(".")) {
                freeSpace = false;
                start++;
            }

            if (fileSystem.get(end).equals(".")) {
                haveFileBlockToMove = false;
                end--;
            }

            if (freeSpace && haveFileBlockToMove) {
                swap(fileSystem, start, end);
                start++;
                end--;
            }
        }
    }

/*
    Order by blocks
        Find block of files to move.
        Find first empty space
        if file block fits into space
            yes - move 
            no - next free space
        if end is passed start then can't move this file. Move on. 
*/

    public void reorderFileBlocksByBlocks() {
        int start = 0;
        int end = fileSystem.size() - 1;
        
        while (!fileSystem.get(end).equals("0")) {
            //find first empty
            int freeSpaceStart = findFreeSpace(start);
            int freeSpaceSize = getFreeSpaceSize(freeSpaceStart);

            int fileBlockStart = getOffSetToNextFileBlock(end);
            int fileBlockSize = getFileBlockCount(fileBlockStart);
            boolean fileBlockSpaceFound = false;

            if (freeSpaceStart > fileBlockStart) {
                break;
            }

            while (!fileBlockSpaceFound) {
                if (fileBlockSize <= freeSpaceSize) {
                    for (int i = 0; i < fileBlockSize; i++) {
                        swap(fileSystem, freeSpaceStart, fileBlockStart);
                        freeSpaceStart++;
                        fileBlockStart--; 
                    }
                    fileBlockSpaceFound = true;
                    end = fileBlockStart;
                } else {
                    // find next free space
                    freeSpaceStart = freeSpaceStart + freeSpaceSize;
                    freeSpaceStart = findFreeSpace(freeSpaceStart);
                    if (freeSpaceStart > fileBlockStart) {
                        end = fileBlockStart - fileBlockSize;
                        break;
                    }
                    freeSpaceSize = getFreeSpaceSize(freeSpaceStart);
                     
                }
            }
            
        }
    }

    public int getOffSetToNextFileBlock(int current) {
        boolean onFileBlockToMove = false;
        while (!onFileBlockToMove) {
            if (!fileSystem.get(current).equals(".")){
                onFileBlockToMove = true;
            } else {
                current--; 
            }
        }
        return current;
    }

    public int getFileBlockCount(int current) {
        int fileBlockSize = 1;
        String currentFileId = fileSystem.get(current);
        while (fileSystem.get(current - fileBlockSize).equals(currentFileId)) {
            fileBlockSize++;
        }
        return fileBlockSize;
    }

    public int findFreeSpace(int current) {
        boolean foundFreeSpace = false;
        while (!foundFreeSpace) {
            if (fileSystem.get(current).equals(".")) {
                foundFreeSpace = true;
            } else {
                current++;
            }
        }
        return current;
    }

    public int getFreeSpaceSize(int current) {
        int freeSpaceCounter = 1;
        while (fileSystem.get(current + freeSpaceCounter).equals(".")) {
            freeSpaceCounter++;
        }
        return freeSpaceCounter;
    }

    public void swap(ArrayList<String> list, int a, int b) {
        String temp = list.get(a); 
        list.set(a, list.get(b));
        list.set(b, temp); 
    }

    public Long calculateCheckSum() {
        Long sum = 0L;
        for (int i = 0; i < fileSystem.size(); i++) {
            if (fileSystem.get(i).equals(".")) {
                continue;
            }
            sum = sum + (i * Integer.parseInt(fileSystem.get(i)));
        }
        return sum;
    }
}