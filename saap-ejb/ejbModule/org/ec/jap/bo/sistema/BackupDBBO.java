package org.ec.jap.bo.sistema;

import javax.ejb.Local;

@Local
public interface BackupDBBO {

	/**
	 * 
	 * @param databaseName
	 * @param databasePassword
	 * @param type backup or restore
	 * @return 
	 * @throws Exception 
	 */
	public String executeCommand() throws Exception;

}
