import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by mmiz318 on 1/02/2018.
 */
public class EmailAuthentication {
    //send true to register servlet if email successfully sent
    public boolean sendEmail (String email) {
        //smart cookies gmail account
        String from = "smartcookies2018@gmail.com";
        String recipient = email;

        Properties properties = System.getProperties();

        properties.setProperty("mail.smtp.user", from);
        properties.setProperty("mail.smtp.password", "miki2018");

        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.port", "465");

        properties.put("mail.smtp.socketFactory.port", 465);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");


        Session session = Session.getDefaultInstance(properties, null);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

            //message body
            message.setSubject("Email Reset for your Smart Cookies Account");
            message.setText("Please go to the link below within 15 minutes to reset your password for your Smart Cookies Account." +
                    " http://sporadic.nz/smartCookieTpre939/RegisterServlet?emailReset=" + email +
                    " Thanks, from the Smart Cookies Team :)");

            Transport transport = session.getTransport("smtps");
            transport.connect("smtp.gmail.com", 465, from, "miki2018");
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

            return true;
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return false;
    }
}
