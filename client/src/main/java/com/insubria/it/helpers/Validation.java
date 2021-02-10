package com.insubria.it.helpers;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * The Validation class contains method to validate input fields
 */
public class Validation {

  /**
   * returns true if the string is not empty; false otherwise
   *
   * @param fieldValue - String contained in a input field (theoretically)
   */
  public static boolean isFieldFilled(String fieldValue) {
    return fieldValue.length() > 0;
  }

  /**
   * returns true if the email is valid; false otherwise
   *
   * @param email - email
   */
  public static boolean validateEmail(String email) {
    try {
      InternetAddress internetAddress = new InternetAddress(email);
      // throws the AddressException if the email is not valid
      internetAddress.validate();
      return true;
    } catch (AddressException e) {
      System.out.println("EXECEPTION FOR EMAIL VALIDATION");
      return false;
    }
  }
}
