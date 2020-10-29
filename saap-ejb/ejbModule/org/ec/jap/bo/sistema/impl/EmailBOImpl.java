package org.ec.jap.bo.sistema.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.ec.jap.bo.saap.ParametroBO;
import org.ec.jap.bo.sistema.EmailBO;
import org.ec.jap.util.SendMailThread;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class EmailBOImpl implements EmailBO {

	@EJB
	ParametroBO parameter;

	/**
	 * Default constructor.
	 */
	public EmailBOImpl() {
	}

	@Override
	public Boolean enviarEmail(String msg, List<String> files, List<String> tos, List<String> ccs, String subject,
			String filePath) throws Exception {
		String username = this.parameter.getString("", 1, "EMAIL_USERNAME");
		String password = this.parameter.getString("", 1, "EMAIL_PASSWORD");
		String host = this.parameter.getString("", 1, "EMAIL_SMTP_HOST");
		String port = this.parameter.getString("", 1, "EMAIL_SMTP_PORT");
		String securePort = this.parameter.getString("", 1, "EMAIL_SMTP_SECURE_PORT");
		String from = this.parameter.getString("", 1, "EMAIL_FROM");
		SendMailThread smtCliente = new SendMailThread(username, password, host, port, securePort, tos, from, ccs,
				subject, msg, filePath);
		GregorianCalendar fechaInicio = (GregorianCalendar) GregorianCalendar.getInstance();
		GregorianCalendar fechaTimeOut = (GregorianCalendar) GregorianCalendar.getInstance();
		fechaTimeOut.setTime(fechaInicio.getTime());
		fechaTimeOut.add(Calendar.SECOND, 5);
		GregorianCalendar fechaActual = (GregorianCalendar) GregorianCalendar.getInstance();
		smtCliente.start();
		while (!smtCliente.isActive() && smtCliente.isAlive()) {
			if (!fechaActual.before(fechaTimeOut)) {
				smtCliente.setActive(true);
			}
		}

		if (smtCliente.isAlive()) {
			smtCliente.interrupt();
		}
		return Boolean.TRUE;
	}

	@Override
	public void enviarBackupDB(String pathFile) throws Exception {
		String tosParam = this.parameter.getString("", 1, "DB_TO_BACKUP");
		String[] arrayTos = tosParam.split(";");
		List<String> tos = new ArrayList<String>();
		for (int i = 0; i < arrayTos.length; i++) {
			tos.add(arrayTos[i]);
		}

		List<String> ccs = new ArrayList<String>();
		ccs.add("freddy.geovanni@bayteq.com");
		SimpleDateFormat sdf = new SimpleDateFormat(" yyyy MM dd");
		String subject = "Respaldo Base de Datos SAAP ".concat(sdf.format(GregorianCalendar.getInstance().getTime()));
		StringBuilder email = new StringBuilder();
		email.append("<div");
		email.append(" style=\"");
		email.append("padding: 2em 6em;");
		email.append("background: rgba(255,255,255,1);");
		email.append(
				"background: -moz-linear-gradient(top, rgba(255,255,255,1) 0%, rgba(246,246,246,1) 47%, rgba(237,237,237,1) 100%);");
		email.append(
				"background: -webkit-gradient(left top, left bottom, color-stop(0%, rgba(255,255,255,1)), color-stop(47%, rgba(246,246,246,1)), color-stop(100%, rgba(237,237,237,1)));");
		email.append(
				"background: -webkit-linear-gradient(top, rgba(255,255,255,1) 0%, rgba(246,246,246,1) 47%, rgba(237,237,237,1) 100%);");
		email.append(
				"background: -o-linear-gradient(top, rgba(255,255,255,1) 0%, rgba(246,246,246,1) 47%, rgba(237,237,237,1) 100%);");
		email.append(
				"background: -ms-linear-gradient(top, rgba(255,255,255,1) 0%, rgba(246,246,246,1) 47%, rgba(237,237,237,1) 100%);");
		email.append(
				"background: linear-gradient(to bottom, rgba(255,255,255,1) 0%, rgba(246,246,246,1) 47%, rgba(237,237,237,1) 100%);");
		email.append(
				"filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#ffffff', endColorstr='#ededed', GradientType=0 );");
		email.append("\"");
		email.append(">");
		email.append("<div style=\"font-size:14px;\">Respaldo Base de Datos SAAP</div>");
		email.append("<p style=\"padding-top:20px;\">Srs. Directivos Junta de Agua Potable y Alcantarillado</p>");
		email.append(
				"<p style=\"padding-top:40px;\">Adjunto se encuentra el archivo de respaldo generado automáticamente por el sistema</p>");
		email.append("<p style=\"padding-top:50px;\">SAAP</p>");
		email.append("<div>Sistema de Administración de Agua Potable</div>");
		email.append("</div>");

		enviarEmail(email.toString(), new ArrayList<String>(), tos, ccs, subject, pathFile);

	}
}
