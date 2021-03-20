package org.ec.jap.bo.sistema;

import java.util.List;

import javax.ejb.Local;

import org.ec.jap.entiti.saap.CabeceraPlanilla;
import org.ec.jap.entiti.saap.DetallePlanilla;
import org.ec.jap.entiti.saap.PeriodoPago;

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

	void enviarEmailUsuario(CabeceraPlanilla cp, List<DetallePlanilla> del, PeriodoPago p) throws Exception;

	boolean sendMail(String msg, List<String> files, List<String> tos, List<String> ccs, String subject,
			String filePath);



}
