package org.ec.jap.bo.saap.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

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

	public Logger log = Logger.getLogger(DetallePlanillaDAOImpl.class.getName());
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
	public void asignarMulta(Usuario usuario, CabeceraPlanilla planilla, RegistroEconomico registroEconomico)
			throws Exception {

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
	public Double crearMulta(CabeceraPlanilla planillaNoPagada, CabeceraPlanilla planillaNueva,
			RegistroEconomico multaAtrazoMes, Usuario usuario, Double valorMulta) throws Exception {
		if (valorMulta > 0.0) {
			DetallePlanilla detallePlanilla = new DetallePlanilla();
			detallePlanilla.setEstado("ING");
			detallePlanilla.setIdCabeceraPlanilla(planillaNueva);
			detallePlanilla.setIdRegistroEconomico(multaAtrazoMes);
			detallePlanilla.setValorTotal(valorMulta);
			detallePlanilla.setFechaRegistro(Calendar.getInstance().getTime());
			detallePlanilla.setValorUnidad(valorMulta);
			detallePlanilla.setValorPagado(0.0);
			detallePlanilla.setValorPendiente(valorMulta);
			detallePlanilla.setOrdenStr("AA");
			detallePlanilla.setDescripcion("Multa Consumo " + planillaNoPagada.getIdPeriodoPago().getDescripcion());
			detallePlanilla.setValorTotalOrigen(detallePlanilla.getValorTotal());
			detallePlanilla.setOrigen(Constantes.origen_mes_Actual);
			save(usuario, detallePlanilla);
		}
		return valorMulta;

	}

	@Override
	public void quitarMulta(Usuario usuario, CabeceraPlanilla planilla, RegistroEconomico registroEconomico,
			DetallePlanilla detallePlanilla) throws Exception {

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
	public DetallePlanilla crearDetalleAlcantarillado(Usuario currentUser, CabeceraPlanilla cp, RegistroEconomico rea,
			Integer cantidad, Double valor, String ppDescripcion) throws Exception {
		DetallePlanilla dpa = new DetallePlanilla();
		dpa.initValue();
		dpa.setIdCabeceraPlanilla(cp);
		dpa.setIdRegistroEconomico(rea);
		dpa.setFechaRegistro(Calendar.getInstance().getTime());
		dpa.setValorUnidad(Utilitario.redondear(valor));
		dpa.setValorTotal(Utilitario.redondear(valor * cantidad));
		dpa.setValorPendiente(dpa.getValorTotal());
		dpa.setDescripcion("Servicio de Alcantarillado Lote " + ppDescripcion + "");
		dpa.setOrdenStr("B");
		dpa.setValorTotalOrigen(dpa.getValorTotal());
		dpa.setOrigen(Constantes.origen_mes_Actual);
		save(currentUser, dpa);
		return dpa;
	}

	@Override
	public DetallePlanilla traspasarDetalle(CabeceraPlanilla planillaNueva, DetallePlanilla dpa) {
		DetallePlanilla dpn = new DetallePlanilla();
		dpn.initValue();
		dpn.setIdLectura(dpa.getIdLectura());
		dpn.setIdRegistroEconomico(dpa.getIdRegistroEconomico());
		dpn.setValorTotal(dpa.getValorTotal());
		dpn.setFechaRegistro(
				dpa.getFechaRegistro() != null ? dpa.getFechaRegistro() : Calendar.getInstance().getTime());
		dpn.setValorUnidad(dpa.getValorUnidad());
		dpn.setValorPendiente(dpn.getValorTotal());
		if (dpa.getIdRegistroEconomico() != null) {
			String tipoRegistro = dpa.getIdRegistroEconomico().getTipoRegistro().getTipoRegistro();
			if ("CONS".equals(tipoRegistro)) {
				dpn.setOrdenStr("BB");
			} else if ("MULAGU".equals(tipoRegistro) || "BASCON".equalsIgnoreCase(tipoRegistro)) {
				dpn.setOrdenStr("A");
			} else if ("CUO".equals(tipoRegistro)) {
				dpn.setOrdenStr("FF");
			} else if ("CUOINI".equals(tipoRegistro)) {
				dpn.setOrdenStr("GG");
			} else if ("INASIS".equals(tipoRegistro)) {
				dpn.setOrdenStr("EE");
			} else {
				dpn.setOrdenStr("HH");
			}
		} else if (dpa.getIdLectura() != null) {
			dpn.setOrdenStr("A");
		} else {
			dpn.setOrdenStr(dpa.getOrdenStr() + dpa.getOrdenStr().substring(0, 1));
		}
		dpn.setIdCabeceraPlanilla(planillaNueva);
		dpn.setDescripcion(dpa.getDescripcion());
		dpn.setValorTotalOrigen(dpa.getValorTotalOrigen() == null ? dpa.getValorTotal() : dpa.getValorTotalOrigen());
		return dpn;
	}

	@Override
	public DetallePlanilla traspasarDetalleInconompleto(CabeceraPlanilla planillaNueva, DetallePlanilla dpi) {
		DetallePlanilla dpn = new DetallePlanilla();
		dpn.initValue();
		dpn.setIdLectura(dpi.getIdLectura());
		dpn.setIdRegistroEconomico(dpi.getIdRegistroEconomico());
		dpn.setValorTotal(dpi.getValorPendiente());
		dpn.setFechaRegistro(
				dpi.getFechaRegistro() != null ? dpi.getFechaRegistro() : Calendar.getInstance().getTime());
		dpn.setValorPendiente(dpn.getValorTotal());
		dpn.setValorUnidad(dpi.getValorUnidad());
		dpn.setIdCabeceraPlanilla(planillaNueva);
		dpn.setDescripcion(dpi.getDescripcion());
		if (dpi.getIdRegistroEconomico() != null) {
			String tipoRegistro = dpi.getIdRegistroEconomico().getTipoRegistro().getTipoRegistro();
			if ("CONS".equals(tipoRegistro)) {
				dpn.setOrdenStr("BB");
			} else if ("MULAGU".equals(tipoRegistro) || "BASCON".equalsIgnoreCase(tipoRegistro)) {
				dpn.setOrdenStr("A");
			} else if ("CUO".equals(tipoRegistro)) {
				dpn.setOrdenStr("FF");
			} else if ("CUOINI".equals(tipoRegistro)) {
				dpn.setOrdenStr("GG");
			} else if ("INASIS".equals(tipoRegistro)) {
				dpn.setOrdenStr("EE");
			} else {
				dpn.setOrdenStr("HH");
			}
		} else if (dpi.getIdLectura() != null) {
			dpn.setOrdenStr("A");
		} else {
			dpn.setOrdenStr(dpi.getOrdenStr() + dpi.getOrdenStr().substring(0, 1));
		}

		dpn.setValorTotalOrigen(dpi.getValorTotalOrigen() == null ? dpi.getValorTotal() : dpi.getValorTotalOrigen());
		dpn.setOrigen(Constantes.origen_pagado_incompleto);
		return dpn;
	}

	@Override
	public List<DetallePlanilla> findDetalles(CabeceraPlanilla planillaNoPagada, String namedQuery) throws Exception {
		HashMap<String, Object> p = new HashMap<>(0);
		p.put("idCabeceraPlanilla", planillaNoPagada);
		List<DetallePlanilla> detallePlanillasNoPagadas = findAllByNamedQuery(namedQuery, p);
		return detallePlanillasNoPagadas;
	}

	@Override
	public DetallePlanilla builDetailLectura(PeriodoPago periodoPago, Lectura lec, DetallePlanilla dpls)
			throws Exception {
		Double base = Utilitario.redondear(
				(lec.getMetros3() != null && lec.getValorMetro3() != null ? (lec.getMetros3() * lec.getValorMetro3())
						: 0.0));
		Double exceso = Utilitario.redondear(
				(lec.getMetros3Exceso() != null && lec.getValorMetro3Exceso() != null && lec.getMetros3Exceso() > 0
						&& lec.getValorMetro3Exceso() > 0 ? (lec.getMetros3Exceso() * lec.getValorMetro3Exceso())
								: 0.0));
		Double total = Utilitario.redondear(base + exceso);
		Usuario us = lec.getIdServicio().getIdUsuario();
		String nombre = us.getApellidos() + " " + us.getNombres();
		String format = "........................";
		nombre = nombre.length() > format.length() ? nombre.substring(0, format.length())
				: nombre + format.substring(nombre.length(), format.length());
		log.info(String.format("%4$s ===> Medidor: %5$s Normal: %1$s, Exceso: %2$s, Total: %3$s", base, exceso,
				total, nombre, lec.getIdServicio().getNumero()));
		dpls.setValorUnidad(Utilitario.redondear(lec.getValorMetro3()));
		dpls.setValorTotal(total);
		dpls.setValorPagado(0.0);
		dpls.setValorPendiente(dpls.getValorTotal());
		dpls.setDescripcion(Utilitario.redondear(lec.getMetros3()) + " m3" + " " + periodoPago.getDescripcion());
		return dpls;
	}

	@Override
	public DetallePlanilla buildInitialDetailLectura(CabeceraPlanilla cp, Lectura lec) throws Exception {
		DetallePlanilla dpls = new DetallePlanilla();
		dpls.setEstado("ING");
		dpls.setIdLectura(lec);
		dpls.setIdCabeceraPlanilla(cp);
		dpls.setValorPagado(0.0);
		dpls.setValorPendiente(0.0);
		dpls.setValorTotal(0.0);
		dpls.setFechaRegistro(Calendar.getInstance().getTime());
		dpls.setValorUnidad(0.0);
		dpls.setOrdenStr("B");
		return dpls;
	}
}
