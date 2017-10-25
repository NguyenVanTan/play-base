package services;

import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;
import javax.inject.Inject;
import java.io.File;
import org.apache.commons.mail.EmailAttachment;

public class MailerService {
    @Inject MailerClient mailerClient;

    public void sendEmail(String fileName, File file) {
        String cid = "1234";
        Email email = new Email()
                .setSubject("Simple email")
                .setFrom("Framgiaplaytest FROM <framgiaplaytest@gmail.com>")
                .addTo("XXX TO <tran.anh.vu@framgia.com>")
                // adds attachment
                .addAttachment(fileName, file)
                // adds inline attachment from byte array
//                .addAttachment("data.txt", "data".getBytes(), "text/plain", "Simple data", EmailAttachment.INLINE)
                // adds cid attachment
//                .addAttachment("image.jpg", new File("/some/path/image.jpg"), cid)
                // sends text, HTML or both...
                .setBodyText("A text message")
                .setBodyHtml("<html><body><p>An <b>html</b> message </p></body></html>");
        mailerClient.send(email);
    }
}