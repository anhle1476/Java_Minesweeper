package minesweeper;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Generator generator = new Generator();
        System.out.print("How many mines do you want on the field? ");
        int mines = scanner.nextInt();
        if (mines < 1 || mines >= 81) {
            System.out.println("Number invalid");
            return;
        }
        Game game = new Game(generator, mines, scanner);
        
    }
}
