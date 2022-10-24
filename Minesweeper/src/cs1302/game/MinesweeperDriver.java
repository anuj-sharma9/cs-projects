package cs1302.game;

import java.io.File;
import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * This {@code MinesweeperDriver} class runs the {@code MinesweeperGame}
 * class. It invokes the methods and runs the game.
 */
public class MinesweeperDriver {

    /**
     * The {@code main} method runs the game and inputs the command line
     * argument to set up the game.
     *
     * @param args is the command line argument where the path to the seed file
     * is inputted.
     */
    public static void main(String[] args) {

        // If there are no args OR more than one element in args,
        // it will throw an InvalidParameterException
        try {
            Scanner input = new Scanner(System.in);
            MinesweeperGame game1 = new MinesweeperGame(input, args[0]);
            if (args.length != 1) {
                throw new IllegalArgumentException();
            } // if

            // Activates the game
            game1.readSeedFile();
            game1.printWelcome();
            game1.setMineField();
            game1.play();

        } catch (ArrayIndexOutOfBoundsException aioobe) {
            int exitStatus = 1;
            System.err.println();
            System.err.println("Usage: MinesweeperDriver SEED_FILE_PATH");
            System.exit(exitStatus);
        } catch (IllegalArgumentException ipe) {
            int exitStatus = 1;
            System.err.println();
            System.err.println("Usage: MinesweeperDriver SEED_FILE_PATH");
            System.exit(exitStatus);
        } catch (InputMismatchException ime) {
            int exitStatus = 3;
            System.err.println();
            System.err.println("Seed File Malformed Error: Incorrect Datatype Detected");
            System.exit(exitStatus);
        } catch (Exception e) {
            int exitStatus = 3;
            System.err.println();
            System.err.println("Seed File Malformed Error: " + e.getMessage());
            System.exit(exitStatus);
        } // try

    } // main

} // MinesweeperDriver
