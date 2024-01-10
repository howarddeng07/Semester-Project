import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
public class Main {

    //ASCII art of 7 hangman stages
    private static final String[] hangmanStages = {
            " +---+\n     |\n     |\n     |\n    ===",
            " +---+\n O   |\n     |\n     |\n    ===",
            " +---+\n O   |\n |   |\n     |\n    ===",
            " +---+\n O   |\n/|   |\n     |\n    ===",
            " +---+\n O   |\n/|\\  |\n     |\n    ===",
            " +---+\n O   |\n/|\\  |\n/    |\n    ===",
            " +---+\n O   |\n/|\\  |\n/ \\  |\n    ==="
    };

    private static final int standardTries = 7;
    public static final long startTime = System.currentTimeMillis();
    public static void main(String[] args) {

        Scanner inputScanner = new Scanner(System.in);
        List<String> wordList = readWordsFromFile("HangmanWordsList.txt");
        String chosenWord = chooseRandomWord(wordList).toLowerCase();


        System.out.print("Please enter the number of tries you want (default is 7): ");
        int triesLeft = standardTries;
        String triesInput = inputScanner.nextLine();

        //Use ASCII Art only if the number of tries is 7
        boolean useAsciiArt = triesInput.isEmpty() || "7".equals(triesInput.trim());

        if (triesInput.matches("\\d+")) {//only positive numbers accepted
            triesLeft = Integer.parseInt(triesInput);
        } else if (!triesInput.trim().isEmpty()) {
            System.out.println("Invalid input. Number of tries set to default (7).");
        }

        ArrayList<Character> wrongGuesses = new ArrayList<>();
        ArrayList<Character> correctGuesses = new ArrayList<>();
        boolean gameWon = false;

        System.out.println("Welcome to Hangman!");
        System.out.println(chosenWord);

        while (triesLeft > 0 && !gameWon) {
            if (useAsciiArt) {
                //display ASCII Art corresponding to the number of tries left
                System.out.println(hangmanStages[standardTries - triesLeft]);
            }
            printCurrentState(chosenWord, correctGuesses);
            System.out.println("Tries left: " + triesLeft);
            System.out.println("Wrong guesses so far: " + wrongGuesses);
            System.out.print("Guess a letter or the whole word ('/' to quit): ");
            String guess = inputScanner.nextLine().toLowerCase();

            if ("/".equals(guess)) {//allowing user to exit game
                System.out.println("Exiting game...");
                break;
            }

            if (!guess.trim().matches("[a-zA-Z]+")){//allowing any alphabetical input
                System.out.println("Invalid input. Please try again.");
                continue;
            }
            if (guess.length() == 1) {
                char guessedLetter = guess.charAt(0);
                if (wrongGuesses.contains(guessedLetter) || correctGuesses.contains(guessedLetter)) {
                    System.out.println("You already guessed that letter!");
                } else if (chosenWord.contains(guess)) {
                    correctGuesses.add(guessedLetter);
                    System.out.println("Good guess!");
                } else {
                    wrongGuesses.add(guessedLetter);
                    triesLeft--;
                    System.out.println("Wrong guess!");
                }
            } else if (guess.length() == chosenWord.length()) { //guessing the whole word
                if (chosenWord.equals(guess)) {
                    gameWon = true;
                } else {
                    triesLeft--;
                    System.out.println("Wrong guess!");
                }
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        }

        if (gameWon) {//revealing word, end of game statements, printing time taken
            System.out.println("Congratulations! You won! The word was: " + chosenWord);
            System.out.println("You took "+(System.currentTimeMillis()-startTime)/1000.0+" seconds.");
        } else {
            if (useAsciiArt) {
                if (triesLeft != 0){
                    System.out.println(hangmanStages[standardTries - triesLeft]); // Added line
                }
            }
            System.out.println("Game over! The word was: " + chosenWord);
            System.out.println("You took "+(System.currentTimeMillis()-startTime)/1000.0+" seconds.");
        }

        inputScanner.close();
    }

    private static List<String> readWordsFromFile(String filename) {//file error handling
        List<String> words = new ArrayList<>();
        try {
            Scanner fileScanner = new Scanner(new File(filename));
            while (fileScanner.hasNextLine()) {
                words.add(fileScanner.nextLine().trim());
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Words file not found.");
            return null;
        }
        return words;
    }

    private static String chooseRandomWord(List<String> words) {
        if (words == null || words.isEmpty()) {
            return null;
        }
        Random random = new Random();
        return words.get(random.nextInt(words.size()));
    }

    private static void printCurrentState(String chosenWord, ArrayList<Character> correctGuesses) {
        for (char letter : chosenWord.toCharArray()) {
            if (correctGuesses.contains(letter)) {
                System.out.print(letter);
            } else {
                System.out.print("_");
            }
            System.out.print(" ");
        }
        System.out.println();
    }

    private static void displayHangman(int triesLeft) {
        if (triesLeft < 0 || triesLeft >= hangmanStages.length) {
            return; //out of bounds
        }
        System.out.println(hangmanStages[hangmanStages.length - triesLeft - 1]);
    }
}