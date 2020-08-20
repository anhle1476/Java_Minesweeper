package minesweeper;

import java.util.Arrays;
import java.util.Scanner;

public class Game {
    private char[][] field;
    // map display status as [-1: show] - [0: unknown] - [1: marked]
    private final int[][] displayMap = new int[9][9];

    private final int mines;
    private int score = 0;
    private int cellsOpened = 0;

    private final Generator generator;
    private final Scanner scanner;

    {
        for (int[] row : displayMap) {
            Arrays.fill(row, 0);
        }
    }

    Game(Generator generator, int mines, Scanner scanner) {
        this.generator = generator;
        this.mines = mines;
        this.scanner = scanner;
        print();
        play();
    }

    private void play() {
        scanner.nextLine();
        while (score < mines && score > (-81 + mines + cellsOpened)) {
            System.out.print("Set/unset mines marks or claim a cell as free: ");
            String command = scanner.nextLine();
            getCoordinateThenMove(command);
        }
        if (score > 500) {
            System.out.println("You stepped on a mine and failed!");
        } else {
            System.out.println("Congratulations! You found all mines!");
        }
    }

    private void getCoordinateThenMove(String command) {
        String[] cordInString = command.trim().split(" ");
        if (cordInString.length != 3) {
            System.out.println("Please enter two numbers and \"free\" or \"mine\" command.");
            return;
        }
        try {
            int y = Integer.parseInt(cordInString[0]) - 1;
            int x = Integer.parseInt(cordInString[1]) - 1;
            if (x < 0 || x > 8 || y < 0 || y > 8) throw new NumberFormatException();
            if (!cordInString[2].equals("free") && !cordInString[2].equals("mine")) {
                System.out.println("Please add \"free\" or \"mine\" command");
            }
            // move
            boolean isMineMove = cordInString[2].equals("mine");
            if (cellsOpened == 0) {
                makeMoveBeforeFieldGeneration(x, y, isMineMove);
            } else {
                makeMoveAfterFieldGeneration(x, y, isMineMove);
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter numbers between 1 and 9.");
        }
    }

    private void makeMoveAfterFieldGeneration(int x, int y, boolean isMarkMove) {
        // case 1: cell already showed
        if (displayMap[x][y] == -1) {
            System.out.println("This cell is already showed!");
            return;
        }
        //case 2: cell is unknown
        if (isMarkMove) {
            // mark move -> cell is marked or not
            if (displayMap[x][y] == 0) {
                displayMap[x][y] = 1;
                score += field[x][y] == 'X' ? 1 : -1;
            } else {
                displayMap[x][y] = 0;
                score += field[x][y] == 'X' ? -1 : 1;
            }
        } else {
            // open move -> cell is mine or empty
            if (field[x][y] == 'X') {
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        if (field[i][j] == 'X') {
                            displayMap[i][j] = -1;
                        }
                    }
                }
                score += 1000;
            } else {
                showEmptyCellsAround(x, y);
            }
        }
        print();
    }

    private void showEmptyCellsAround(int x, int y) {
        if (displayMap[x][y] == -1 || field[x][y] == 'X') return;
        displayMap[x][y] = -1;
        cellsOpened++;
        // search for cells around '/' cells
        if (field[x][y] != '/') return;
        int dX, dY;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                dX = x + i;
                dY = y + j;
                if (dX < 0 || dX > 8 || dY < 0 || dY > 8) continue;
                showEmptyCellsAround(dX, dY);
            }
        }
    }

    private void makeMoveBeforeFieldGeneration(int x, int y, boolean isMarkMove) {
        if (isMarkMove) {
            displayMap[x][y] = displayMap[x][y] == 1 ? 0 : 1;
        } else {
            //get field from generator
            field = generator.fieldGenerate(mines, x, y);
            // get score from marked cells
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (displayMap[i][j] == 0) continue;
                    score += field[x][y] == 'X' ? 1 : -1;
                }
            }
            //open the first cell
            showEmptyCellsAround(x, y);
        }
        print();
    }

    private void print() {
        System.out.println("\n │123456789│\n" +
                "—│—————————│");
        for (int i = 0; i < 9; i++) {
            System.out.print((i + 1) + "│");
            for (int j = 0; j < 9; j++) {
                switch (displayMap[i][j]) {
                    case -1:
                        System.out.print(field[i][j]);
                        break;
                    case 1:
                        System.out.print('*');
                        break;
                    default:
                        System.out.print('.');
                }
            }
            System.out.print("│\n");
        }
        System.out.println("—│—————————│");
    }
}
