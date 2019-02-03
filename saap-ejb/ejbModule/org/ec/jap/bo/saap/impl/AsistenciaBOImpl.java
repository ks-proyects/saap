package org.ec.jap.bo.saap.impl;

import java.util.List;

import javax.ejb.Stateless;

import org.ec.jap.bo.saap.AsistenciaBO;
import org.ec.jap.dao.saap.impl.AsistenciaDAOImpl;
import org.ec.jap.entiti.saap.Asistencia;
import org.ec.jap.entiti.saap.Usuario;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class AsistenciaBOImpl extends AsistenciaDAOImpl implements AsistenciaBO {

	/**
	 * Default constructor.
	 */
	public AsistenciaBOImpl() {
	}

	@Override
	public void actualizarAsistencias(Usuario usuario, List<Asistencia> list) throws Exception {

		for (Asistencia asistencia : list) {
			if(asistencia.getNumeroRayas().equals(0.0)&&asistencia.getAsistio()){
				asistencia.setNumeroRayas(1.0);
			}
			update(usuario, asistencia);
		}
	}
}
