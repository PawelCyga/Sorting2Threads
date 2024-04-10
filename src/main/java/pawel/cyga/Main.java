package pawel.cyga;

import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //Measuring runtime of the program
        long startTime = System.nanoTime();
        int threadNumber = 2;//Just for output text

        String fileName = "unsorted.csv";

        //Defining the number of rows, and numbers per row of the file
        int rows = 16;
        int numbersPerRow = 1000;

        //2D array to store the numbers
        int[][] numbersArray = new int[rows][numbersPerRow];

        //Reading numbers from the file and filling the 2d array
        readNumbersFromFile(fileName, numbersArray);

        //Splitting the original 2D array into multiple arrays to sort simultaneously by multiple threads
        int[][] part1 = Arrays.copyOfRange(numbersArray, 0, 8);
        int[][] part2 = Arrays.copyOfRange(numbersArray, 8, 16);


        //Create and start threads for sorting each part
        Thread thread1 = new Thread(() -> measureSortingTime(part1));
        Thread thread2 = new Thread(() -> measureSortingTime(part2));
        thread1.start();
        thread2.start();

        //Joining threads so they run simultaneously
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Measuring runtime of the program
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Runtime of the program for " + threadNumber + " threads =" + duration + " nanoseconds");
    }
    //Read numbers from a file and store them in 2D array
    private static void readNumbersFromFile(String fileName, int[][] array) {
        //FileReader - loading the file | BufferedReader - reading data from file
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String row; //Single array as a string
            int rowNumber = 0;
            while ((row = br.readLine()) != null && rowNumber < array.length) {
                String[] numbersString = row.split(",");//Splitting single array(one string) into seperate strings
                for (int i = 0; i < numbersString.length && i < array[rowNumber].length; i++) { //Iterating over array of strings and parsing to int
                    array[rowNumber][i] = Integer.parseInt(numbersString[i]);
                }
                rowNumber++;
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading from the file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Sorting and measuring time only for the sorting part
    private static void measureSortingTime(int[][] array) {
        long startTime = System.nanoTime();

        //Sorting using built in function
        for (int i = 0; i < array.length; i++) {
            Arrays.sort(array[i]);
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Sorting time for " + array.length + " rows = " + duration + " nanoseconds");
    }
}