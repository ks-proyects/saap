package org.ec.jap.bo.saap.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.ec.jap.bo.saap.ActividadBO;
import org.ec.jap.bo.saap.AsistenciaBO;
import org.ec.jap.bo.saap.LlaveBO;
import org.ec.jap.bo.saap.RegistroEconomicoBO;
import org.ec.jap.dao.saap.impl.ActividadDAOImpl;
import org.ec.jap.entiti.saap.Actividad;
import org.ec.jap.entiti.saap.Asistencia;
import org.ec.jap.entiti.saap.Llave;
import org.ec.jap.entiti.saap.RegistroEconomico;
import org.ec.jap.entiti.saap.Usuario;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class ActividadBOImpl extends ActividadDAOImpl implements ActividadBO {

	@EJB
	LlaveBO llaveBO;

	@EJB
	AsistenciaBO asistenciaBO;

	@EJB
	RegistroEconomicoBO registroEconomicoBO; 
	
	/**
	 * Default constructor.
	 */
	public ActividadBOImpl() {
	}

	@Override
	public void generarAsistencia(Usuario usuario, Integer idActividad) throws Exception {

		Actividad actividad=findByPk(idActividad);
		map.clear();
		map.put("actividad", actividad);
		List<Llave> llaves = llaveBO.findAllByNamedQuery("Llave.findByActivoInactivo", map);
		map.clear();
		map.put("idPeriodoPago", actividad.getIdPeriodoPago());
		map.put("tipoRegistro", "INASIS");
		RegistroEconomico registroEconomico=registroEconomicoBO.findByNamedQuery("RegistroEconomico.findByType", map);
		for (Llave llave : llaves) {
			Asistencia asistencia = new Asistencia();
			asistencia.setActividad(actividad);
			asistencia.setAsistio(false);
			asistencia.setIdUsuario(llave.getIdUsuario());
			asistencia.setNumeroRayas(0.0);
			asistencia.setIdRegistroEconomico(registroEconomico);
			asistencia.setNumeroLlave(llave.getNumero());
			asistenciaBO.save(usuario, asistencia);
		}
	}
}
