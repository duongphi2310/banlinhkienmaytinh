package com.example.do_an_android.Model;

import com.example.do_an_android.BuildConfig;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {
    public void sendEmail(String recieverEmail, String subject, String text) {
        try {
            String stringSenderEmail = BuildConfig.SENDER_SMTP_EMAIL;
            String stringReceiverEmail = recieverEmail;
            String stringPasswordSenderEmail = BuildConfig.PASSWORD_SMTP_EMAIL;

            String stringHost = "smtp.gmail.com";

            Properties properties = System.getProperties();

            properties.put("mail.smtp.host", stringHost);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setSubject(subject);
            mimeMessage.setContent(text, "text/html");
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            thread.start();
        } catch (
                AddressException e) {
            e.printStackTrace();
        } catch (
                MessagingException e) {
            e.printStackTrace();
        }
    }

}
