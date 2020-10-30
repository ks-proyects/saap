package org.ec.jap.bo.sistema.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.ec.jap.bo.saap.ParametroBO;
import org.ec.jap.bo.sistema.BackupDBBO;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class BackupDBBOImpl implements BackupDBBO {

	@EJB
	ParametroBO parameter;

	/**
	 * Default constructor.
	 */
	public BackupDBBOImpl() {
	}

	public String executeCommand() throws Exception {

		String fullPathOutput = "";
		String dbName = parameter.getString("", 1, "DB_NAME");
		String operacion = parameter.getString("", 1, "DB_BACKUP_TYPE");
		String host = parameter.getString("", 1, "DB_HOST");
		String port = parameter.getString("", 1, "DB_PORT");
		String user = parameter.getString("", 1, "DB_USER");
		String password = parameter.getString("", 1, "DB_PASSWORD");
		String pathServer = parameter.getString("", 1, "DB_SERVER_PATH");
		String pathOutput = parameter.getString("", 1, "DB_BACKUP_PATH");
		File pgdump = new File(pathServer.concat("pg_dump.exe"));

		if (pgdump.exists()) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
			pathOutput = pathOutput.concat(File.separator)
					.concat(sdf.format(GregorianCalendar.getInstance().getTime()));
			File backupFilePath = new File(pathOutput);
			if (!backupFilePath.exists()) {
				File dir = backupFilePath;
				dir.mkdirs();
			}

			sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			String backupFileName = "backup".concat("_").concat(sdf.format(GregorianCalendar.getInstance().getTime()))
					.concat(".backup");
			fullPathOutput = pathOutput.concat(File.separator).concat(backupFileName);
			List<String> commands = getPgComands(pathServer, fullPathOutput, operacion, host, port, user, dbName);
			if (!commands.isEmpty()) {
				try {
					ProcessBuilder pb = new ProcessBuilder(commands);
					pb.environment().put("PGPASSWORD", password);
					Process process = pb.start();
					try (BufferedReader buf = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
						String line = buf.readLine();
						while (line != null) {
							System.err.println(line);
							line = buf.readLine();
						}
						buf.close();
					}
					process.waitFor();
					process.destroy();
					System.out.println("===> Success on " + operacion + " process.");
				} catch (IOException ex) {
					System.out.println("Exception: " + ex);
					fullPathOutput = "";
				}
			} else {
				System.out.println("Error: Invalid params.");
			}
		}
		return fullPathOutput;

	}

	private List<String> getPgComands(String pathServer, String fullPathOutput, String operacion, String host,
			String port, String user, String dbName) {

		ArrayList<String> commands = new ArrayList<>();
		switch (operacion) {
		case "backup":
			commands.add(pathServer.concat("pg_dump.exe"));
			commands.add("-f");
			commands.add(fullPathOutput);
			commands.add("-F");
			commands.add("c");
			commands.add("-v");
			commands.add("-b");
			commands.add("-h");
			commands.add(host);
			commands.add("-p");
			commands.add(port);
			commands.add("-U");
			commands.add(user);
			commands.add(dbName);

			break;
		case "restore":
			commands.add(pathServer.concat("pg_restore.exe"));
			commands.add("-v");
			commands.add(fullPathOutput);
			break;
		default:
			return Collections.emptyList();
		}
		return commands;
	}

}
