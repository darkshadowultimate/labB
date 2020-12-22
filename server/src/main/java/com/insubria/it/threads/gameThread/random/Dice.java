package com.insubria.it.threads.gameThread.random;

import java.util.Random;

public class Dice {
  public static String getRandomFaceLetter(String[] letters) {
    Random random = new Random();

    return letters[random.nextInt(letters.length)];
  }
}