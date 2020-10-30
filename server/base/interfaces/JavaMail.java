package server.base.interfaces;


import javax.mail.MessagingException;


public interface JavaMail {
    String host = "smtp.office365.com";
    String emailSender = "scascavilla1@studenti.uninsubria.com";
    String passwordSender = "";

    void sendEmail (
            String toEmail,
            String subject,
            String emailBody
    ) throws MessagingException;
}
