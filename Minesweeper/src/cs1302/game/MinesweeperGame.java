package cs1302.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.InputMismatchException;


/**
 * This class creates all of the instances and methods
 * for setting up and running the minesweeper game.
 */
public class MinesweeperGame {

    // Declaring instance variables
    private final Scanner stdIn;
    private String seedPath;
    private int rows;
    private int cols;
    private int numMines;
    private int rounds = 0;
    private double score;
    private int rowToken;
    private int colToken;
    private int cellsRevealed = 0;
    private int minesMarked = 0;
    private boolean fogUsed;
    private int[][] mineLocations;
    private int[][] gameBoard;
    private int[][] cellStatusBoard;
    private String[][] stringBoard;

    /**
     * {@code MinesweeperGame} constructor constructs the object of
     * the class to run. Instance variables will be initialized here.
     *
     * @param stdIn is the standard input passed through Scanner.
     * @param seedPath is the path where for the seed file containing
     * board info.
     */
    public MinesweeperGame(Scanner stdIn, String seedPath) {
        this.stdIn = stdIn;
        this.seedPath = seedPath;
    } // MinesweeperGame constructor


    /**
     * {@code printWelcome()} prints the welcome text which will only
     * be called once in the beginning of the game.
     */
    public void printWelcome() {
        // catches an exception if "welcome.txt" file is not found
        try {
            String welcomePath = "resources/welcome.txt";
            File welcome = new File(welcomePath);
            Scanner fileReader = new Scanner(welcome);

            // While there is text on each line, it will print
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                System.out.println(line);
            } // while
            System.out.println();
        } catch (FileNotFoundException fnfe) {
            System.err.println();
            System.err.println("File does not exist");
            System.exit(0); // gracefully exits program
        } // try-catch

    } // printWelcome


    /**
     * {@code readSeedFile} reads the input seed file from the command
     * line argument. A try-catch is used to handle any possible exceptions
     * in the file. Any invalid inputs or values assigned to {@code rows},
     * {@code cols}, {@code numMines}, and coordinates stored in
     * {@code  mineLocations} will be handled through error messages and
     * exit the program gracefully with the assigned {@code exitStatus}.
     *
     * @throws InputMismatchException if the seed file contains anything other
     * than an int
     * @throws Exception when a seed file breaks a restriction
     */
    public void readSeedFile() throws InputMismatchException, Exception {
        int exitStatus = 0;
        boolean outOfBoundsMine = false;
        boolean invalidBoundsMine = false;
        boolean tableRestriction = false;
        boolean mineRestriction = false;
        boolean mineCountChecker = false;
        int mineCoordCounter = 0;

        try {
            File seedFile = new File(seedPath);
            Scanner fileReader = new Scanner(seedFile);

            if (fileReader.hasNextInt()) {
                rows = fileReader.nextInt();
                cols = fileReader.nextInt();
                numMines = fileReader.nextInt();
                mineLocations = new int[numMines][2];

                // Restriction conditions for the mine field
                tableRestriction = (rows < 5 || rows > 10) || (cols < 5 || cols > 10);
                mineRestriction = ((numMines < 1) || (numMines > ((rows * cols) - 1)));
                if (tableRestriction) {
                    String m = "Cannot create a mine field with that many rows and/or columns!";
                    throw new Exception(m);
                } // if
                if (mineRestriction) {
                    String m = "Invalid mine count";
                    throw new Exception(m);
                } // if
                // Looping through each mine coordinate array
                for (int i = 0; i < mineLocations.length; i++) {

                    // checks if a mine is out of bounds and stores it in outOfBoundsMine
                    outOfBoundsMine = mineLocations[i][0] >= rows || mineLocations[i][1] >= cols;

                    // Looping through each array in 2D mineLocations array
                    for (int j = 0; j < mineLocations[i].length; j++) {
                        // Every coordinate count gets incremented
                        if (fileReader.hasNextInt()) {
                            mineLocations[i][j] = fileReader.nextInt();
                            mineCoordCounter++;
                        } else {
                            throw new Exception("Missing Value");
                        } // if

                        invalidBoundsMine = mineLocations[i][j] < 0;
                        if (outOfBoundsMine || invalidBoundsMine) {
                            throw new Exception("Mine Coordinate Out of Bounds");
                        } // if
                    } // inner for
                } // outer for
            } // if
        } catch (FileNotFoundException fnfe) {
            exitStatus = 2;
            System.err.println();
            System.err.println("Seed File Not Found Error: " + fnfe.getMessage());
            System.exit(exitStatus);
        } // try
    } // readSeedFile

    /**
     * {@code printMineField} prints the grid of the game based on information
     * from the seed file. Uses 2D Arrays to print to assign the String elements
     * and print the board. It also prints the number of {@code rounds} that
     * have been completed.
     */
    public void printMineField() {
        System.out.println(" Rounds Completed: " + rounds);
        System.out.println();

        // Printing the grid
        for (int i = 0; i < stringBoard.length; i++) {
            System.out.print(" " + i + " "); // Prints the row number
            for (int j = 0; j < stringBoard[i].length; j++) {
                System.out.print(stringBoard[i][j]);
            } // for
            System.out.println("| ");
        } // for

        // Printing the column numbers
        System.out.print("    ");
        for (int i = 0; i < cols - 1; i++) {
            System.out.print(" " + i + "  ");
        } // for
        System.out.println(" " + (cols - 1) + " ");
        System.out.println();
    } // printMineField



    /**
     * Checks each neighboring element of a 2D array and determines the
     * amount of mines it borders.
     *
     * @return neighborMineCounter is the number of mines it borders.
     * @param i is the current row.
     * @param j is the current column.
     * @param a1 is the row starting value to compare.
     * @param a2 is the row ending value to compare.
     * @param b1 is the column starting value to compare.
     * @param b2 is the column ending value to compare.
     */
    private int checkSurroundingMines(int i, int j, int a1, int a2, int b1, int b2) {
        int neighborMineCounter = 0;
        for (int a = a1; a <= a2; a++) {
            for (int b = b1; b <= b2; b++) {
                // If the neighboring cell is a mine, counter is incremented
                if (gameBoard[i + a][j + b] == -1) {
                    neighborMineCounter++;
                } // if
            } // for
        } // for

        return neighborMineCounter;
    } // checkSurroundingMines

    /**
     * {@code setMineField} sets up a 2D int array to represent the
     * {@code mineLocations} and the numbers that represent how many mines
     * it borders. It also declares another 2D int array that tells info about
     * whether a cell has been marked or not. 0 represents unmarked, 1 represents
     * marked (F), 2 represents guess marked (?). This will be later used for the
     * {@code noFog()} method.
     *
     *It also sets up a 2D String array to print later.
     */
    public void setMineField() {
        gameBoard = new int[rows][cols];
        cellStatusBoard = new int[rows][cols];
        stringBoard = new String[rows][cols];
        // Setting up the stringBoard to print later
        for (int i = 0; i < stringBoard.length; i++) {
            for (int j = 0; j < stringBoard[i].length; j++) {
                stringBoard[i][j] = "|   ";
            } // for
        } // for
        // Setting -1 to each mineLocation in the 2D int array
        for (int i = 0; i < mineLocations.length; i++) {
            gameBoard[mineLocations[i][0]][mineLocations[i][1]] = -1;
        } // for
        // Setting up the numbers displaying amount of mines bordered
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                if (gameBoard[i][j] != -1) {
                    boolean innerVertical = (i > 0 && i < gameBoard.length - 1);
                    boolean innerHorizontal = (j > 0 && j < gameBoard[i].length - 1);
                    // Checking num of bordering mines for the inner cells
                    if (innerVertical && innerHorizontal) {
                        // Checks to see how many mines it borders
                        gameBoard[i][j] = checkSurroundingMines(i, j, -1, 1, -1, 1);
                    } else {
                        // Checks the edge cases
                        int neighborMineCounter = 0;
                        if (i == 0 && j == 0) {
                            // top left
                            gameBoard[i][j] = checkSurroundingMines(i, j, 0, 1, 0, 1);
                        } else if (i == 0 && j == gameBoard[i].length - 1) {
                            // top right
                            gameBoard[i][j] = checkSurroundingMines(i, j, 0, 1, -1, 0);
                        } else if (j == gameBoard[i].length - 1 && i == gameBoard.length - 1) {
                            // bottom right
                            gameBoard[i][j] = checkSurroundingMines(i, j, -1, 0, -1, 0);
                        } else if (j == 0 && i == gameBoard.length - 1) {
                            // bottom left
                            gameBoard[i][j] = checkSurroundingMines(i, j, -1, 0, 0, 1);
                        } else if (i == 0) {
                            // top edge
                            gameBoard[i][j] = checkSurroundingMines(i, j, 0, 1, -1, 1);
                        } else if (i == gameBoard.length - 1) {
                            // bottom edge
                            gameBoard[i][j] = checkSurroundingMines(i, j, -1, 0, -1, 1);
                        } else if (j == gameBoard[i].length - 1) {
                            // right edge
                            gameBoard[i][j] = checkSurroundingMines(i, j, -1, 1, -1, 0);
                        } else if (j == 0) {
                            // left edge
                            gameBoard[i][j] = checkSurroundingMines(i, j, -1, 1, 0, 1);
                        } // if else
                    } // if else
                } // if
            } // for
        } // for
    } // setMineField

    /**
     * {@code token} reads the int tokens of a command and determines whether they
     * are valid or invalid inputs from the user.
     *
     * @param scan is the scanner object that reads the inputs
     *
     * @throws Exception if the input is not an integer
     * @throws ArrayIndexOutOfBoundsException if the input is out of bounds of the grid.
     */
    private void readToken(Scanner scan) throws Exception {
        // Checks if the scanner has an int, else throws an exception

        // Checking int tokens
        if (scan.hasNextInt()) {
            rowToken = scan.nextInt();
        } else {
            throw new Exception();
        } // if else
        if (scan.hasNextInt()) {
            colToken = scan.nextInt();
        } else {
            throw new Exception();
        } // if else

        // if there is still another integer in command, it will throw an exception
        if (scan.hasNextInt()) {
            throw new Exception("Command not recognized!");
        } // if

        // Checks if the integers are out of bounds of grid
        boolean rowOutOfBounds = rowToken < 0 || rowToken >= rows;
        boolean colOutOfBounds = colToken < 0 || colToken >= cols;
        if (rowOutOfBounds) {
            throw new Exception("Index " + rowToken + " out of bounds for length " + rows);
        } // if
        if (colOutOfBounds) {
            throw new Exception("Index " + colToken + " out of bounds for length " + cols);
        } // if
    } // readToken

    /**
     * {@code markCell} marks the cell that is prompted by the user and
     * sets the cell status to 1.
     */
    private void markCell() {
        if (gameBoard[rowToken][colToken] == -1) {
            stringBoard[rowToken][colToken] = "| F ";
            minesMarked++;
            rounds++;
        } else {
            stringBoard[rowToken][colToken] = "| F ";
            rounds++;
        } // if else
        cellStatusBoard[rowToken][colToken] = 1;
    } // markCell

    /**
     * {@code guessCell} marks the cell as a guess that is prompted by
     * the user and sets the cell status to 2.
     */
    private void guessCell() {
        stringBoard[rowToken][colToken] = "| ? ";
        rounds++;
        cellStatusBoard[rowToken][colToken] = 2;
    } // guessCell

    /**
     * {@code noFog} iterates through each element in the {@code gameBoard}
     * array and reveals which ones are mines (cheat code).
     */
    private void noFog() {
        fogUsed = true;
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                if (gameBoard[i][j] == -1 && cellStatusBoard[i][j] == 1) {
                    // if the cell is already marked with "mark" command
                    stringBoard[i][j] = "|<F>";
                } else if (gameBoard[i][j] == -1 && cellStatusBoard[i][j] == 2) {
                    // if the cell is already marked with "guess" command
                    stringBoard[i][j] = "|<?>";
                } else if (gameBoard[i][j] == -1) {
                    // if the cell has not been marked yet
                    stringBoard[i][j] = "|< >";
                } // if
            } // for
        } // for
        rounds++;
    } // noFog

    /**
     * {@code fog()} resets the nofog to not appear in the next round after
     * {@code nofog()} has been used in the previous round.
     */
    private void fog() {
        if (fogUsed) {
            for (int i = 0; i < stringBoard.length; i++) {
                for (int j = 0; j < stringBoard[i].length; j++) {
                    // Determines if any cells have "< >", "<F>", or "<?>"
                    if (stringBoard[i][j].equals("|< >")) {
                        stringBoard[i][j] = "|   ";
                    } else if (stringBoard[i][j].equals("|<F>")) {
                        stringBoard[i][j] = "| F ";
                    } else if (stringBoard[i][j].equals("|<?>")) {
                        stringBoard[i][j] = "| ? ";
                    } // if
                } // for
            } // for
        } // if
        fogUsed = false; // Resets the condition to false
    } // fog

    /**
     * {@code printHelpMessage} prints the help message which lists all of
     * the commands the user can input in the prompt.
     */
    private void printHelpMessage() {
        System.out.println();
        System.out.println("Commands Available... ");
        System.out.println(" - Reveal: r/reveal row col ");
        System.out.println(" -   Mark: m/mark   row col ");
        System.out.println(" -  Guess: g/guess  row col ");
        System.out.println(" -   Help: h/help ");
        System.out.println(" -   Quit: q/quit ");
    } // printHelpMessage

    /**
     * {@code printQuitMessage} prints the message when the user
     * decides to quit.
     */
    private void printQuitMessage() {
        System.out.println();
        System.out.println("Quitting the game... ");
        System.out.println("Bye!");
    } // printQuitMessage

    /**
     * {@code checkRedundancy} checks if there are any redundant inputs
     * after help, quit, nofog are inputted to make it so that the program
     * does not recognize the code.
     *
     * @param scan is the full command inputted from Scanner.
     * @throws Exception when there are redundant characters inputted.
     */
    private void checkRedundancy(Scanner scan) throws Exception {
        if (scan.hasNext()) {
            throw new Exception("Command not recognized!");
        } // if
    } // checkRedundancy

    /**
     * {@code promptUser} prompts the user to make a move
     * on the board, ask for help, or quit the game via
     * standard input.
     */
    public void promptUser() {
        System.out.print("minesweeper-alpha: ");
        String fullCommand = stdIn.nextLine(); // full command entered by user
        try {
            // Using Scanner to scan the full command
            Scanner commandScan = new Scanner(fullCommand);
            // Determining initial command
            String initialCommand = commandScan.next();
            fog(); // Removes "< >" if nofog was used in the previous round
            if (initialCommand.equals("reveal") || initialCommand.equals("r")) {
                readToken(commandScan);
                // if the cell revealed is NOT a mine, else game is over
                if (gameBoard[rowToken][colToken] != -1) {
                    int borderValue = gameBoard[rowToken][colToken];
                    // Cell is revealed with border number and number of rounds is incremented
                    stringBoard[rowToken][colToken] = "| " + borderValue + " ";
                    cellsRevealed++;
                    rounds++;
                } else {
                    System.out.println();
                    printLoss();
                    System.exit(0);
                } // if
            } else if (initialCommand.equals("mark") || initialCommand.equals("m")) {
                readToken(commandScan);
                markCell(); // Marks the cell with "F"
            } else if (initialCommand.equals("guess") || initialCommand.equals("g")) {
                readToken(commandScan);
                guessCell(); // Marks the cell with "?"
            } else if (initialCommand.equals("nofog")) {
                checkRedundancy(commandScan); // Checks for redundant input
                noFog(); // Cheat code that displays the mine locations with "< >"
            } else if (initialCommand.equals("help") || initialCommand.equals("h")) {
                checkRedundancy(commandScan); // Checks for redundant input
                printHelpMessage(); // Prints the help message that displays all commands
                rounds++;
            } else if (initialCommand.equals("quit") || initialCommand.equals("q")) {
                checkRedundancy(commandScan); // Checks for redundant input
                printQuitMessage(); // Prints quit message and exits the game
                System.exit(0);
            } else {
                throw new Exception("Command not recognized!");
            } // if else
            System.out.println();
        } catch (Exception e) {
            // If a command is not recognized
            System.err.println();
            System.err.println("Invalid Command: " + e.getMessage());
            System.err.println();
        } // try
    } // promptUser


    /**
     * {@code isWon()} method states whether game is won.
     * @return true if the criteria for a victory is met.
     */
    public boolean isWon() {
        // if all cells are either marked or revealed, game is won
        if ((minesMarked + cellsRevealed) == (rows * cols)) {
            return true;
        } else {
            return false;
        }
    } // isWon


    /**
     * {@code printWin()} prints the victory message when
     * {@code isWon()} is true.
     */
    public void printWin() {
        // Catches an exception if the "gamewon.txt" file is not found
        // Program will then be terminated
        try {
            String gameWonPath = "resources/gamewon.txt";
            File winGame = new File(gameWonPath);
            Scanner fileReader = new Scanner(winGame);
            String line = "";

            // Prints all lines except last line of the file
            for (int i = 0; i < 18; i++) {
                line = fileReader.nextLine();
                System.out.println(line);
            } // for

            // Printing last line
            line = fileReader.nextLine();
            System.out.print(line + " ");

            // Printing the score
            score = 100.0 * rows * cols / rounds;
            System.out.printf("%.2f", score);
            System.out.println();
            System.out.println();
        } catch (FileNotFoundException fnfe) {
            System.err.println();
            System.err.println("File does not exist");
            System.exit(0); // gracefully exits program
        } // try-catch
    } // printWin


    /**
     * {@code printLoss()} prints the game over message
     * when a criteria for a loss is met.
     */
    public void printLoss() {
        // Prints the gameover content when game is lost
        /* If file does not exist, exception will be caught
           program is terminated
        */
        try {
            String gameOverPath = "resources/gameover.txt";
            File lostGame = new File(gameOverPath);
            Scanner fileReader = new Scanner(lostGame);

            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                System.out.println(line);
            } // while
            System.out.println();
        } catch (FileNotFoundException fnfe) {
            System.err.println();
            System.err.println("File does not exist");
            System.exit(0); // gracefully exits program
        } // try-catch
    } // printLoss

    /**
     * {@code play} invokes the other methods in a loop so the
     * game runs until the player, quits, loses, or wins.
     */
    public void play() {
        while (!isWon()) {
            printMineField();
            promptUser();
        } // while
        printWin();
    } // play

} // MinesweeperGame
