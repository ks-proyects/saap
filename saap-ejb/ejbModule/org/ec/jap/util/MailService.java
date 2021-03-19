package org.ec.jap.util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailService {

	public static Logger log = Logger.getLogger(MailService.class.getName());

	/**
	 * Método que envia en email a cualquier servidor
	 * 
	 * @param username
	 * @param password
	 * @param host
	 * @param port
	 * @param securePort
	 * @param from
	 * @param tos
	 * @param ccs
	 * @param subject
	 * @param msg
	 * @param filePath
	 */
	public static void sendEmail(final String username, final String password, final String host, final String port,
			final String securePort, final String from, final List<String> tos, final List<String> ccs,
			final String subject, final String msg, final String filePath) {
		// Esto es lo que va delante de @gmail.com en tu cuenta de correo. Es el
		// remitente también.

		Properties props = System.getProperties();
		props.put("mail.smtp.host", host); // El servidor SMTP de Google
		props.put("mail.smtp.port", port); // El puerto SMTP seguro de Google
		if ((securePort != null) && securePort.equalsIgnoreCase("s")) {
			props.put("mail.smtp.user", username);
			props.put("mail.smtp.starttls.enable", "true"); // Para conectar de manera segura al servidor SMTP
		}
		props.put("mail.smtp.clave", password); // La clave de la cuenta
		props.put("mail.smtp.auth", "true"); // Usar autenticación mediante usuario y clave

		Session session = Session.getDefaultInstance(props);
		MimeMessage message = new MimeMessage(session);
		String emailsTo = "";
		try {
			message.setFrom(new InternetAddress(from));
			for (String to : tos) {
				emailsTo = emailsTo.concat(to).concat(",");
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			}
			for (String cc : ccs) {
				message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
			}

			message.setSubject(subject);
			if (filePath != null && !filePath.isEmpty()) {
				Multipart multipart = new MimeMultipart();
				MimeBodyPart attachmentPart = new MimeBodyPart();
				MimeBodyPart textPart = new MimeBodyPart();
				try {
					File f = new File(filePath);
					attachmentPart.attachFile(f);
					textPart.setText(msg, "ISO-8859-1", "html");
					multipart.addBodyPart(textPart);
					multipart.addBodyPart(attachmentPart);

				} catch (IOException e) {
					e.printStackTrace();
				}
				message.setContent(multipart);
			} else {
				message.setContent(msg, "text/html");
			}

			Transport transport = session.getTransport("smtp");
			transport.connect("smtp.gmail.com", username, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			log.info("Email eviado exitosamente a " + emailsTo);
		} catch (MessagingException me) {
			me.printStackTrace();
		}
	}

}
