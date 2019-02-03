package org.ec.jap.bo.saap;

import javax.ejb.Local;

import org.ec.jap.dao.saap.CuentaDAO;
import org.ec.jap.entiti.saap.Usuario;

@Local
public interface CuentaBO extends CuentaDAO {

	/**
	 * REGISTRAMOS EN EL LIBRO DIARIO DEL PRESENTE ANIO
	 * 
	 * @param numCuentaDebe
	 * @param descDeb
	 * @param numCuentaHaber
	 * @param descHaber
	 * @param monto
	 * @param usuario
	 * @throws Exception
	 */
	public void registrarAsiento(String numCuentaDebe, String descDeb, String numCuentaHaber, String descHaber, Double monto, Usuario usuario) throws Exception;
}
