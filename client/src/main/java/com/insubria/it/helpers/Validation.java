package com.insubria.it.helpers;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class Validation {

  public static boolean isFieldFilled(String fieldValue) {
    return fieldValue.length() > 0;
  }

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
