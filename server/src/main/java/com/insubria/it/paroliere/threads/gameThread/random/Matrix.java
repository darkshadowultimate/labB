package com.insubria.it.paroliere.threads.gameThread.random;


import java.util.Random;

import com.insubria.it.paroliere.threads.gameThread.random.Dice;


public class Matrix {
  private static final int MATRIX_ROWS = 4;
  private static final int MATRIX_COLS = 4;
  private static final String[][] dicesLetters = {
    { "B", "A", "O", "O", "Qu", "M" },
    { "U", "T", "E", "S", "L", "P" },
    { "I", "G", "E", "N", "V", "T" },
    { "O", "U", "L", "I", "E", "R" },
    { "A", "C", "E", "S", "L", "R" },
    { "R", "A", "T", "I", "B", "L" },
    { "S", "M", "I", "R", "O", "A" },
    { "I", "S", "E", "E", "F", "H" },
    { "S", "O", "T", "E", "N", "D" },
    { "A", "I", "C", "O", "F", "R" },
    { "V", "N", "Z", "D", "A", "E" },
    { "I", "E", "A", "T", "A", "O" },
    { "O", "T", "U", "C", "E", "N" },
    { "N", "O", "L", "G", "U", "E" },
    { "D", "C", "M", "P", "A", "E" },
    { "E", "R", "I", "N", "S", "H" },
  };

  private int[] dicesToChoose;
  private String[][] matrixLetters;

  public Matrix () {
    int indexDicesLettersList = 0;
    dicesToChoose = getFilledIntegerArray(dicesLetters[0].length);
    dicesToChoose = getMixedValuesArray(dicesToChoose);

    this.matrixLetters = new String[MATRIX_ROWS][MATRIX_COLS];

    for(int row = 0; row < MATRIX_ROWS; row++) {
      for(int col = 0; col < MATRIX_COLS; col++) {
        this.matrixLetters[row][col] = Dice.getRandomFaceLetter(dicesLetters[indexDicesLettersList]);
        indexDicesLettersList++;
      }
    }
  }

  private int[] getFilledIntegerArray (int length) {
    int[] array = new int[length];

    for(int i = 0; i < length; i++) {
      array[i] = i;
    }

    return array;
  }

  private int[] getMixedValuesArray (int[] array) {
    int[] mixedArray = array;

    for(int i = 0; i < array.length; i++) {
      int indexToSwitch = (new Random()).nextInt(array.length);
      int tmp = mixedArray[i];
      mixedArray[i] = mixedArray[indexToSwitch];
      mixedArray[indexToSwitch] = tmp;
    }

    return mixedArray;
  }

  public String[][] getRandomMatrix () {
    return this.matrixLetters;
  }
}