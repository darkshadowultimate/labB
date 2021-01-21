package com.insubria.it.context;

public class PlayerContextProvider {
  private static String namePlayer;
  private static String surnamePlayer;
  private static String usernamePlayer;
  private static String emailPlayer;

  public static String getNamePlayer() {
    return namePlayer;
  }

  public static String getSurnamePlayer() {
    return surnamePlayer;
  }

  public static String getUsernamePlayer() { return usernamePlayer; }

  public static String getEmailPlayer() { return emailPlayer; }

  public static void setPlayerInfo(String name, String surname, String username, String email) {
    namePlayer = name;
    surnamePlayer = surname;
    usernamePlayer = username;
    emailPlayer = email;
  }
}