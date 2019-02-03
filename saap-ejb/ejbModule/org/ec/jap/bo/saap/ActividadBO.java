package org.ec.jap.bo.saap;

import javax.ejb.Local;

import org.ec.jap.dao.saap.ActividadDAO;
import org.ec.jap.entiti.saap.Usuario;

@Local
public interface ActividadBO extends ActividadDAO {

	/**
	 * Metodo que gaurada y genera o actualiza las asitencias de una actividad
	 * 
	 * @param usuario
	 * @param idActividad
	 * @throws Exception
	 */
	void generarAsistencia(Usuario usuario, Integer idActividad) throws Exception;
}
