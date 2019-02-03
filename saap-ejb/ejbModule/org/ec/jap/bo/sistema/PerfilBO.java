package org.ec.jap.bo.sistema;

import javax.ejb.Local;

import org.ec.jap.dao.sistema.PerfilDAO;
import org.ec.jap.entiti.sistema.Perfil;

@Local
public interface PerfilBO extends PerfilDAO {

	/**
	 * 
	 * @param perfil
	 * @throws Exception
	 */
	public void existenUsuariosAsignados(Perfil perfil) throws Exception;

	/**
	 * 
	 * @param perfil
	 * @throws Exception
	 */
	public void existenOpcionesAsignados(Perfil perfil) throws Exception;

}
