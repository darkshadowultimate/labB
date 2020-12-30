package com.insubria.it.base.interfaces;

import javax.mail.MessagingException;

/**
 * The interface JavaMail defines the signatures of the methods that will be
 * defined in the JavaMailController class.
 */
public interface JavaMail {
  /**
   * The university SMTP server host name
   */
  String host = "smtp.office365.com";

  /**
   * Email used to send the messages
   */
  String emailSender = "spaglino@studenti.uninsubria.it";

  /**
   * The password of the email used to send the messages
   */
  String passwordSender = "Ungiornofuturo1234.";

  /**
   * The signature of the sendEmail method. This method is defined in the
   * JavaMailController class
   */
  void sendEmail(String toEmail, String subject, String emailBody) throws MessagingException;
}
