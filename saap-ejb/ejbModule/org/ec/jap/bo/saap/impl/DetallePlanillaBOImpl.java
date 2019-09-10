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
import org.ec.jap.entiti.saap.Lectura;
import org.ec.jap.entiti.saap.PeriodoPago;
import org.ec.jap.entiti.saap.RegistroEconomico;
import org.ec.jap.entiti.saap.Usuario;
import org.ec.jap.utilitario.Constantes;
import org.ec.jap.utilitario.Utilitario;

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

	@Override
	public DetallePlanilla crearDetalleAlcantarillado(Usuario currentUser, CabeceraPlanilla cp, RegistroEconomico registroEconomicoAlcantarillado, Integer cantidad, Double valor, String ppDescripcion) throws Exception {
		DetallePlanilla detallePlanillaBasico = new DetallePlanilla();
		detallePlanillaBasico.initValue();
		detallePlanillaBasico.setIdCabeceraPlanilla(cp);
		detallePlanillaBasico.setIdRegistroEconomico(registroEconomicoAlcantarillado);
		detallePlanillaBasico.setFechaRegistro(Calendar.getInstance().getTime());
		detallePlanillaBasico.setValorUnidad(Utilitario.redondear(valor));
		detallePlanillaBasico.setValorTotal(Utilitario.redondear(valor*cantidad));
		detallePlanillaBasico.setValorPendiente(detallePlanillaBasico.getValorTotal());
		detallePlanillaBasico.setDescripcion("Servicio de Alcantarillado " + ppDescripcion+" ("+cantidad+")");
		detallePlanillaBasico.setOrdenStr("B");
		detallePlanillaBasico.setValorTotalOrigen(detallePlanillaBasico.getValorTotal());
		detallePlanillaBasico.setOrigen(Constantes.origen_mes_Actual);
		save(currentUser, detallePlanillaBasico);
		return detallePlanillaBasico;
	}

	@Override
	public DetallePlanilla traspasarDetalle(CabeceraPlanilla planillaNueva, DetallePlanilla detallePlanilla) {
		DetallePlanilla detallePlanillaNuevo = new DetallePlanilla();
		detallePlanillaNuevo.initValue();
		detallePlanillaNuevo.setIdLectura(detallePlanilla.getIdLectura());
		detallePlanillaNuevo.setIdRegistroEconomico(detallePlanilla.getIdRegistroEconomico());
		detallePlanillaNuevo.setValorTotal(detallePlanilla.getValorTotal());
		detallePlanillaNuevo.setFechaRegistro(detallePlanilla.getFechaRegistro() != null ? detallePlanilla.getFechaRegistro() : Calendar.getInstance().getTime());
		detallePlanillaNuevo.setValorUnidad(detallePlanilla.getValorUnidad());
		detallePlanillaNuevo.setValorPendiente(detallePlanillaNuevo.getValorTotal());
		if (detallePlanilla.getIdRegistroEconomico() != null) {
			String tipoRegistro = detallePlanilla.getIdRegistroEconomico().getTipoRegistro().getTipoRegistro();
			if ("CONS".equals(tipoRegistro)) {
				detallePlanillaNuevo.setOrdenStr("BB");
			} else if ("MULAGU".equals(tipoRegistro) || "BASCON".equalsIgnoreCase(tipoRegistro)) {
				detallePlanillaNuevo.setOrdenStr("A");
			} else if ("CUO".equals(tipoRegistro)) {
				detallePlanillaNuevo.setOrdenStr("FF");
			} else if ("CUOINI".equals(tipoRegistro)) {
				detallePlanillaNuevo.setOrdenStr("GG");
			} else if ("INASIS".equals(tipoRegistro)) {
				detallePlanillaNuevo.setOrdenStr("EE");
			} else {
				detallePlanillaNuevo.setOrdenStr("HH");
			}
		} else if (detallePlanilla.getIdLectura() != null) {
			detallePlanillaNuevo.setOrdenStr("A");
		} else {
			detallePlanillaNuevo.setOrdenStr(detallePlanilla.getOrdenStr() + detallePlanilla.getOrdenStr().substring(0, 1));
		}
		detallePlanillaNuevo.setIdCabeceraPlanilla(planillaNueva);
		detallePlanillaNuevo.setDescripcion(detallePlanilla.getDescripcion());
		detallePlanillaNuevo.setValorTotalOrigen(detallePlanilla.getValorTotalOrigen() == null ? detallePlanilla.getValorTotal() : detallePlanilla.getValorTotalOrigen());
		
		return detallePlanillaNuevo;
	}

	@Override
	public DetallePlanilla traspasarDetalleInconompleto(CabeceraPlanilla planillaNueva, DetallePlanilla detalleIncompleto) {
		DetallePlanilla detallePlanillaNuevo = new DetallePlanilla();
		detallePlanillaNuevo.initValue();
		detallePlanillaNuevo.setIdLectura(detalleIncompleto.getIdLectura());
		detallePlanillaNuevo.setIdRegistroEconomico(detalleIncompleto.getIdRegistroEconomico());
		detallePlanillaNuevo.setValorTotal(detalleIncompleto.getValorPendiente());
		detallePlanillaNuevo.setFechaRegistro(detalleIncompleto.getFechaRegistro() != null ? detalleIncompleto.getFechaRegistro() : Calendar.getInstance().getTime());
		detallePlanillaNuevo.setValorPendiente(detallePlanillaNuevo.getValorTotal());
		detallePlanillaNuevo.setValorUnidad(detalleIncompleto.getValorUnidad());
		detallePlanillaNuevo.setIdCabeceraPlanilla(planillaNueva);
		detallePlanillaNuevo.setDescripcion(detalleIncompleto.getDescripcion());
		if (detalleIncompleto.getIdRegistroEconomico() != null) {
			String tipoRegistro = detalleIncompleto.getIdRegistroEconomico().getTipoRegistro().getTipoRegistro();
			if ("CONS".equals(tipoRegistro)) {
				detallePlanillaNuevo.setOrdenStr("BB");
			} else if ("MULAGU".equals(tipoRegistro) || "BASCON".equalsIgnoreCase(tipoRegistro)) {
				detallePlanillaNuevo.setOrdenStr("A");
			} else if ("CUO".equals(tipoRegistro)) {
				detallePlanillaNuevo.setOrdenStr("FF");
			} else if ("CUOINI".equals(tipoRegistro)) {
				detallePlanillaNuevo.setOrdenStr("GG");
			} else if ("INASIS".equals(tipoRegistro)) {
				detallePlanillaNuevo.setOrdenStr("EE");
			} else {
				detallePlanillaNuevo.setOrdenStr("HH");
			}
		} else if (detalleIncompleto.getIdLectura() != null) {
			detallePlanillaNuevo.setOrdenStr("A");
		} else {
			detallePlanillaNuevo.setOrdenStr(detalleIncompleto.getOrdenStr() + detalleIncompleto.getOrdenStr().substring(0, 1));
		}

		detallePlanillaNuevo.setValorTotalOrigen(detalleIncompleto.getValorTotalOrigen() == null ? detalleIncompleto.getValorTotal() : detalleIncompleto.getValorTotalOrigen());
		detallePlanillaNuevo.setOrigen(Constantes.origen_pagado_incompleto);
		return detallePlanillaNuevo;
	}

	@Override
	public DetallePlanilla crearDetalleBasico(CabeceraPlanilla cp, RegistroEconomico registroEconomicoBasico, Lectura lectura, PeriodoPago periodoPago) throws Exception {
		DetallePlanilla detallePlanillaBasico = new DetallePlanilla();
		detallePlanillaBasico.initValue();
		detallePlanillaBasico.setIdCabeceraPlanilla(cp);
		detallePlanillaBasico.setIdRegistroEconomico(registroEconomicoBasico);
		detallePlanillaBasico.setFechaRegistro(Calendar.getInstance().getTime());
		detallePlanillaBasico.setValorUnidad(Utilitario.redondear(lectura.getValorBasico()));
		detallePlanillaBasico.setValorTotal(Utilitario.redondear(lectura.getValorBasico()));
		detallePlanillaBasico.setValorPendiente(detallePlanillaBasico.getValorTotal());
		detallePlanillaBasico.setDescripcion("Básico " + periodoPago.getDescripcion());
		detallePlanillaBasico.setOrdenStr("B");
		detallePlanillaBasico.setValorTotalOrigen(detallePlanillaBasico.getValorTotal());
		detallePlanillaBasico.setOrigen(Constantes.origen_mes_Actual);
		cp.setSubtotal(Utilitario.redondear(cp.getSubtotal() + detallePlanillaBasico.getValorTotal()));
		cp.setTotal(Utilitario.redondear(cp.getTotal() + detallePlanillaBasico.getValorTotal()));
		registroEconomicoBasico.setValor(registroEconomicoBasico.getValor() + detallePlanillaBasico.getValorTotal());
		return detallePlanillaBasico;
	}

	@Override
	public List<DetallePlanilla> findPlanillasNoPagadas(CabeceraPlanilla planillaNoPagada, String namedQuery) throws Exception {
		HashMap<String, Object> p = new HashMap<>(0);
		p.put("idCabeceraPlanilla", planillaNoPagada);
		List<DetallePlanilla> detallePlanillasNoPagadas = findAllByNamedQuery(namedQuery, p);
		return detallePlanillasNoPagadas;
	}
}
