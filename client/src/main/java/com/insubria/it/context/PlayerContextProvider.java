package com.insubria.it.context;

public class PlayerContextProvider {
  private static String namePlayer;
  private static String email;

  public static String getNamePlayer() {
    return namePlayer;
  }

  public static String getEmail() {
    return email;
  }

  public static void setNamePlayer(String updatedNamePlayer) {
    namePlayer = updatedNamePlayer;
  }

  public static void setEmail(String updatedEmail) {
    email = updatedEmail;
  }
}