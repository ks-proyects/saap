package org.ec.jap.bo.saap;

import java.util.List;

import javax.ejb.Local;

import org.ec.jap.dao.saap.AsistenciaDAO;
import org.ec.jap.entiti.saap.Asistencia;
import org.ec.jap.entiti.saap.Usuario;

@Local
public interface AsistenciaBO extends AsistenciaDAO {

	/**
	 * 
	 * @param usuario
	 * @param list
	 * @throws Exception
	 */
	public void actualizarAsistencias(Usuario usuario, List<Asistencia> list) throws Exception;
}
