package org.ec.jap.bo.sistema;

import java.util.List;

import javax.ejb.Local;

@Local
public interface EmailBO {

	/**
	 * Método que envi un email
	 * 
	 * @param msg
	 * @param files
	 * @param tos
	 * @param ccs
	 * @param subject
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	Boolean enviarEmail(String msg, List<String> files, List<String> tos, List<String> ccs, String subject,
			String filePath) throws Exception;

	/**
	 * 
	 * @param pathFile
	 * @return
	 * @throws Exception
	 */
	void enviarBackupDB(String pathFile) throws Exception;

}
