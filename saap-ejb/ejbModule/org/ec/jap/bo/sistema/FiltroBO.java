package org.ec.jap.bo.sistema;

import javax.ejb.Local;

import org.ec.jap.dao.sistema.FiltroDAO;
import org.ec.jap.entiti.saap.Usuario;
import org.ec.jap.entiti.sistema.ElementoSistema;
import org.ec.jap.entiti.sistema.Filtro;

@Local
public interface FiltroBO extends FiltroDAO {

	/**
	 * 
	 * @param idUser
	 * @param tipoFiltro
	 * @param elementoSistema
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public Filtro getFiltro(Usuario idUser, String tipoFiltro, ElementoSistema elementoSistema, Object value) throws Exception;

	/**
	 * 
	 * @param idUser
	 * @param tipoFiltro
	 * @param elementoSistema
	 * @param valueDefect
	 * @param filtro
	 * @param isParame
	 * @return
	 * @throws Exception
	 */
	Filtro getFiltro(Usuario idUser, String tipoFiltro, ElementoSistema elementoSistema, Object valueDefect, Filtro filtro, Boolean isParame) throws Exception;
}
