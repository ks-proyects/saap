package org.ec.jap.util;

import java.util.List;

public class SendMailThread extends Thread {

	private String username;
	private String password;
	private String host;
	private String port;
	private String securePort;
	private boolean active = false;
	private List<String> tos;
	private String from;
	private List<String> ccs;
	private String subject;
	private String msg;
	private String filePath;

	public SendMailThread(String username, String password, String host, String port, String securePort,
			List<String> tos, String from, List<String> ccs, String subject, String msg, String filePath) {
		super();
		this.username = username;
		this.password = password;
		this.host = host;
		this.port = port;
		this.securePort = securePort;
		this.tos = tos;
		this.from = from;
		this.ccs = ccs;
		this.subject = subject;
		this.msg = msg;
		this.filePath = filePath;
	}

	public void run() {
		MailService.sendEmail(username, password, host, port, securePort, from, tos, ccs, subject, msg, filePath);
		this.active = true;

	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}
}
