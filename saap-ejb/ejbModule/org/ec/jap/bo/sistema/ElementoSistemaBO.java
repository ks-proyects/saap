package org.ec.jap.bo.sistema;

import java.util.List;

import javax.ejb.Local;

import org.ec.jap.dao.sistema.ElementoSistemaDAO;
import org.ec.jap.entiti.sistema.ElementoSistema;

@Local
public interface ElementoSistemaBO extends ElementoSistemaDAO {

	/**
	 * 
	 * @param idEmpresa
	 * @param idUser
	 * @param tipoElemento
	 * @param idElementoPadre
	 * @return
	 * @throws Exception
	 */
	public List<ElementoSistema> findAllByUser(Integer idEmpresa, Integer idUser, String tipoElemento, Integer idElementoPadre) throws Exception;

	/**
	 * 
	 * @param idEmpresa
	 * @param idUser
	 * @param tipoElemento
	 * @param idElementoPadre
	 * @param accion
	 * @return
	 * @throws Exception
	 */
	public List<ElementoSistema> findControlsByUserAndAccion(Integer idEmpresa, Integer idUser, String tipoElemento, Integer idElementoPadre, String accion) throws Exception;
}
