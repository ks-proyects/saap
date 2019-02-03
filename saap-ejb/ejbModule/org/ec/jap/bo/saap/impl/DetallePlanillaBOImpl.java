package org.ec.jap.bo.saap.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.ec.jap.bo.saap.CabeceraPlanillaBO;
import org.ec.jap.bo.saap.DetallePlanillaBO;
import org.ec.jap.bo.saap.RegistroEconomicoBO;
import org.ec.jap.dao.saap.impl.DetallePlanillaDAOImpl;
import org.ec.jap.entiti.saap.CabeceraPlanilla;
import org.ec.jap.entiti.saap.DetallePlanilla;
import org.ec.jap.entiti.saap.RegistroEconomico;
import org.ec.jap.entiti.saap.Usuario;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class DetallePlanillaBOImpl extends DetallePlanillaDAOImpl implements DetallePlanillaBO {

	@EJB
	CabeceraPlanillaBO cabeceraPlanillaBO;

	@EJB
	RegistroEconomicoBO registroEconomicoBO;

	/**
	 * Default constructor.
	 */
	public DetallePlanillaBOImpl() {
	}

	@Override
	public void asignarMulta(Usuario usuario, CabeceraPlanilla planilla, RegistroEconomico registroEconomico) throws Exception {

		DetallePlanilla detallePlanilla = new DetallePlanilla();
		detallePlanilla.setEstado("ING");
		detallePlanilla.setIdCabeceraPlanilla(planilla);
		detallePlanilla.setIdRegistroEconomico(registroEconomico);
		detallePlanilla.setValorTotal(registroEconomico.getValor());
		detallePlanilla.setFechaRegistro(Calendar.getInstance().getTime());
		detallePlanilla.setValorPendiente(detallePlanilla.getValorTotal());
		detallePlanilla.setValorUnidad(registroEconomico.getValor());
		detallePlanilla.setDescripcion(registroEconomico.getDescripcion());
		save(usuario, detallePlanilla);
		planilla.setSubtotal(planilla.getSubtotal() + detallePlanilla.getValorTotal());
		planilla.setTotal(planilla.getTotal() + detallePlanilla.getValorTotal());
		cabeceraPlanillaBO.update(usuario, planilla);
		registroEconomico.setCantidadAplicados(registroEconomico.getCantidadAplicados() + 1);
		registroEconomicoBO.update(usuario, registroEconomico);

	}

	@Override
	public void quitarMulta(Usuario usuario, CabeceraPlanilla planilla, RegistroEconomico registroEconomico, DetallePlanilla detallePlanilla) throws Exception {

		planilla.setSubtotal(planilla.getSubtotal() - detallePlanilla.getValorTotal());
		planilla.setTotal(planilla.getTotal() - detallePlanilla.getValorTotal());
		cabeceraPlanillaBO.update(usuario, planilla);
		delete(usuario, detallePlanilla);
		registroEconomico.setCantidadAplicados(registroEconomico.getCantidadAplicados() - 1);
		registroEconomicoBO.update(usuario, registroEconomico);

	}

	@Override
	public Boolean noExisteDetalle(Integer idCabecera) throws Exception {
		map.put("idCabeceraPlanilla", idCabecera);
		List<DetallePlanilla> detallePlanilla = findAllByNamedQuery("DetallePlanilla.findByCabecara", map);
		return detallePlanilla.isEmpty();
	}

	@Override
	public void descartarPago(CabeceraPlanilla planilla, Usuario usuario) throws Exception {
		try {
			map = new HashMap<>();
			map.put("idCabeceraPlanilla", planilla);
			List<DetallePlanilla> dp = findAllByNamedQuery("DetallePlanilla.findByCabecaraForCancelar", map);
			for (DetallePlanilla detallePlanilla : dp) {
				
				detallePlanilla.setValorPagado(0.0);
				detallePlanilla.setValorPendiente(detallePlanilla.getValorTotal());
				detallePlanilla.setEstado("ING");
				detallePlanilla.setValorTotal(detallePlanilla.getValorTotal());
				update(usuario, detallePlanilla);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
