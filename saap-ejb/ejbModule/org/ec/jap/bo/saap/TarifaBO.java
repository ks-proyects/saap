package org.ec.jap.bo.saap;

import javax.ejb.Local;

import org.ec.jap.dao.saap.TarifaDAO;
import org.ec.jap.entiti.saap.Usuario;

@Local
public interface TarifaBO extends TarifaDAO {

	/**
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	Double getValorMulta(Usuario user) throws Exception;

}
