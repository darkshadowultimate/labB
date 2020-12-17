package com.insubria.it.paroliere.base.classes;


import com.insubria.it.paroliere.base.interfaces.JavaMail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * The JavaMailController class implements the JavaMail interface and contains a single method that is invoked when a mail needs to be sent to a client
 * The procedure to send the email has been copied to the example uploaded to the e-learning platform
 */
public class JavaMailController implements JavaMail {
    /**
     * This method defines the properties to send the email, creates the Message object, and then send it using the university's SMTP server
     * 
     * @return Nothing
     * @param toEmail - it represents the email of the reciever
     * @param subject - it represents the email subject
     * @param emailBody - it represents the body message of the email
     * @throws MessagingException - If an exception happens while the creation of the message or while the send, this exception will be thrown
     */
    public void sendEmail (
            String toEmail,
            String subject,
            String emailBody
    ) throws MessagingException {
        System.out.println("Preparing the mail to be sent...");

        Properties props = System.getProperties();
        props.put("mail.smtp.host", JavaMail.host);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", 587);
        Session session = Session.getInstance(props);

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(JavaMail.emailSender));
        msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(toEmail, false));
        msg.setSubject(subject);
        msg.setText(emailBody);

        Transport.send(msg, JavaMail.emailSender, JavaMail.passwordSender);
        System.out.println("Email was sent successfully");
    }
}
