package server.base.classes;


import server.base.interfaces.JavaMail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class JavaMailController implements JavaMail {
    public void sendEmail (
            String toEmail,
            String subject,
            String emailBody
    ) throws MessagingException {
        System.out.println("Preparing the mail to be sent...");

        Properties props = System.getProperties();
        props.put("mail.smtp.host", this.host);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", 587);
        Session session = Session.getInstance(props);

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(this.emailSender));
        msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(toEmail, false));
        msg.setSubject(subject);
        msg.setText(emailBody);

        Transport.send(msg, this.emailSender, this.passwordSender);
        System.out.println("Email was sent successfully");
    }
}
