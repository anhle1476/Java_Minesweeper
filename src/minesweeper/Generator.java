package minesweeper;

import java.util.Random;

public class Generator {
    public char[][] fieldGenerate(int mines, int firstX, int firstY) {
        // randomly create mines
        char[][] field = new char[9][9];
        Random random = new Random();
        int x, y;
        for (int i = 1; i <= mines; i++) {
            do {
                int slot = random.nextInt(81);
                x = slot / 9;
                y = slot % 9;
            } while (field[x][y] == 'X' || (x == firstX && y == firstY));
            field[x][y] = 'X';
        }
        // fill empty slots with '/' or numbers
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (field[i][j] != 'X') {
                    int minesAround = countMinesAround(field, i, j);
                    field[i][j] = minesAround == 0 ? '/' : Character.forDigit(minesAround, 10);
                }
            }
        }
        return field;
    }

    private int countMinesAround(char[][] field, int dX, int dY) {
        int counter = 0;
        int thisX, thisY;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                thisX = dX + i;
                thisY = dY + j;
                if (thisX < 0 || thisY < 0 || thisX > 8 || thisY > 8) continue;
                if (field[thisX][thisY] == 'X') counter++;
            }
        }
        return counter;
    }
}
