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
import org.ec.jap.entiti.saap.CabeceraPlanilla;
import org.ec.jap.entiti.saap.DetallePlanilla;
import org.ec.jap.entiti.saap.PeriodoPago;
import org.ec.jap.entiti.saap.Usuario;
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
		ccs.add("freddy.geovanni@gmail.com");
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

	@Override
	public void enviarEmailUsuario(CabeceraPlanilla cp, List<DetallePlanilla> del, PeriodoPago p) throws Exception {
		if (cp.getIdUsuario() != null) {
			Usuario user = cp.getIdUsuario();
			if (user.getEmail() != null && !user.getEmail().isEmpty()) {
				String tosParam = this.parameter.getString("", 1, "DB_TO_BACKUP");
				String[] arrayTos = tosParam.split(";");
				List<String> tos = new ArrayList<String>();
				for (int i = 0; i < arrayTos.length; i++) {
					tos.add(user.getEmail());
				}

				List<String> ccs = new ArrayList<String>();
				for (String cc : arrayTos) {
					ccs.add(cc);
				}
				String subject = "Notificación Pago Agua Potable ".concat(p.getDescripcion());
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

				email.append("<div");
				email.append(" style=\"");
				email.append("padding: 1em 2em;");
				email.append("background: rgba(255,255,255,1);");
				email.append("text-align: right;");
				email.append("color: #4dacfd;");
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
				email.append("Brindarte una experiencia única en servicio es nuestra prioridad.</div>");

				email.append("<h2>Notificación Pago Agua Potable Chaupiloma</h2>");

				email.append(
						"<span style=\"padding-top:20px;\">" + user.getNombres().concat(" ".concat(user.getApellidos()))
								+ ", a continuación se detalla la planilla del mes indicado.</span>");
				email.append(
						"<p style='padding-top:5px;font-size:1.1em;'>Planilla Número: " + cp.getObservacion() + "</p>");

				email.append("<h3 style='text-align: center;'>Detalles</h3>");

				email.append("<table border=0 margin=0");
				email.append(" style=\"");
				email.append("width: 100%;");
				email.append("justify-self: center;");
				email.append("justify-items: center;");
				email.append("align-items: center;");
				email.append("background: rgba(255,255,255,1);");
				email.append("color: #4dacfd;");
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

				email.append("<tr><th style='text-align:left;'>Descripcion</th>");
				email.append("<th style='text-align:left;'>Valor</th></tr>");
				for (DetallePlanilla det : del) {
					email.append("<tr><td>" + det.getDescripcion() + "</td>");
					email.append("<td>$ " + det.getValorTotal() + "</td></tr>");
				}
				email.append("<tr><td></td>");
				email.append("<td><h3 style='color: #a91010;'>$ " + cp.getTotal() + "</h3></td></tr>");
				email.append("</table>");

				email.append("<p style=\"padding-top:50px;\">SAAP</p>");
				email.append("<div>Sistema de Administración de Agua Potable</div>");
				email.append("<p>Por favor no responder a este correo.</p></div>");

				enviarEmail(email.toString(), new ArrayList<String>(), tos, ccs, subject, null);
			}

		}

	}
}
