package org.ec.jap.bo.saap.impl;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

import org.ec.jap.bo.saap.ActividadBO;
import org.ec.jap.bo.saap.AsientoBO;
import org.ec.jap.bo.saap.AsistenciaBO;
import org.ec.jap.bo.saap.CabeceraPlanillaBO;
import org.ec.jap.bo.saap.CuentaBO;
import org.ec.jap.bo.saap.DetallePlanillaBO;
import org.ec.jap.bo.saap.GastoBO;
import org.ec.jap.bo.saap.LecturaBO;
import org.ec.jap.bo.saap.LibroDiarioBO;
import org.ec.jap.bo.saap.LlaveBO;
import org.ec.jap.bo.saap.ParametroBO;
import org.ec.jap.bo.saap.PeriodoPagoBO;
import org.ec.jap.bo.saap.RangoConsumoBO;
import org.ec.jap.bo.saap.RegistroEconomicoBO;
import org.ec.jap.bo.saap.TipoRegistroBO;
import org.ec.jap.bo.saap.UsuarioBO;
import org.ec.jap.bo.sistema.CambioEstadoBO;
import org.ec.jap.dao.saap.impl.CabeceraPlanillaDAOImpl;
import org.ec.jap.entiti.saap.Actividad;
import org.ec.jap.entiti.saap.CabeceraPlanilla;
import org.ec.jap.entiti.saap.DetallePlanilla;
import org.ec.jap.entiti.saap.Gasto;
import org.ec.jap.entiti.saap.Lectura;
import org.ec.jap.entiti.saap.Llave;
import org.ec.jap.entiti.saap.Parametro;
import org.ec.jap.entiti.saap.PeriodoPago;
import org.ec.jap.entiti.saap.RegistroEconomico;
import org.ec.jap.entiti.saap.TipoRegistro;
import org.ec.jap.entiti.saap.Usuario;
import org.ec.jap.enumerations.EstadoCabeceraPlanilla;
import org.ec.jap.utilitario.Constantes;
import org.ec.jap.utilitario.Utilitario;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class CabeceraPlanillaBOImpl extends CabeceraPlanillaDAOImpl implements CabeceraPlanillaBO {

	/**
	 * Default constructor.
	 */
	@EJB
	protected UsuarioBO usuarioBO;
	@EJB
	protected PeriodoPagoBO periodoPagoBO;
	@EJB
	protected LecturaBO lecturaBO;
	@EJB
	protected ParametroBO parametroBO;
	@EJB
	protected TipoRegistroBO tipoRegistroBO;
	@EJB
	protected RegistroEconomicoBO registroEconomicoBO;
	@EJB
	protected CambioEstadoBO cambioEstadoBO;
	@EJB
	protected RangoConsumoBO rangoConsumoBO;
	@EJB
	protected DetallePlanillaBO detallePlanillaBO;
	@EJB
	protected LlaveBO llaveBO;
	@EJB
	protected CuentaBO cuentaBO;
	@EJB
	protected AsistenciaBO asistenciaBO;
	@EJB
	protected ActividadBO actividadBO;
	@EJB
	protected AsientoBO asientoBO;
	@EJB
	protected LibroDiarioBO libroDiarioBO;
	@EJB
	protected GastoBO gastoBO;

	public CabeceraPlanillaBOImpl() {
	}

	@SuppressWarnings("unused")
	@Override
	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public void iniciarCabeceraPlanilla(Usuario usuario, Integer idPeriodoPago) throws Exception {

		// Obtenemos todas las llaves activas de los usuarios
		List<Llave> llaves = llaveBO.findAllByNamedQuery("Llave.findOfUsuariosActivos");
		// Obtenemos el periodo de pago
		PeriodoPago periodoPago = periodoPagoBO.findByPk(idPeriodoPago);

		// Obtenemos el numero de factura
		Parametro parametro = parametroBO.findByPk("NUMFACT");
		Integer numeroFactura = parametro.getValorEntero();
		// Tamaño de numeral de la factura definido acorde al actual formato
		String path = "0000000000000";
		// Inasistencias los creamos al inicio para que en el transcurso
		// registre ello
		RegistroEconomico asistenciaRE = registroEconomicoBO.inicializar(periodoPago, "INASIS", "Multa Inasistencia " + periodoPago.getDescripcion(), 1, usuario);
		// Gastos, lo creamos uno para que pueda registrar los gastos en todo
		// este ciclo
		RegistroEconomico gastoRE = registroEconomicoBO.inicializar(periodoPago, "GAST", "Gasto " + periodoPago.getDescripcion(), 1, usuario);
		// Cuentas Por Pagar, para que las registre la cuentas por pagar de este
		// mes
		RegistroEconomico gastoCD = registroEconomicoBO.inicializar(periodoPago, "CUEPAG", "Valor Por Pagar " + periodoPago.getDescripcion(), 1, usuario);
		for (Llave idLlave : llaves) {
			numeroFactura++;
			CabeceraPlanilla planillaNueva = new CabeceraPlanilla();
			planillaNueva.setEstado(EstadoCabeceraPlanilla.ING.toString());
			planillaNueva.setFechaRegistro(Calendar.getInstance().getTime());
			planillaNueva.setDescuento(0.0);
			planillaNueva.setBase(0.0);
			planillaNueva.setValorPagado(0.0);
			planillaNueva.setAbonoUsd(0.0);
			planillaNueva.setValorPagadoAbono(0.0);
			planillaNueva.setSubtotal(0.0);
			planillaNueva.setTotal(0.0);
			planillaNueva.setCambioUsd(0.0);

			planillaNueva.setIdLlave(idLlave);
			planillaNueva.setObservacion(path.substring(0, path.length() - numeroFactura.toString().length()) + numeroFactura.toString());
			planillaNueva.setIdPeriodoPago(periodoPagoBO.findByPk(idPeriodoPago));
			save(usuario, planillaNueva);
			cambioEstadoBO.cambiarEstadoSinVerificar(18, usuario, planillaNueva.getIdCabeceraPlanilla(), "");

			// Obtenermos las planillas no pagadas y trasladamos el valor del
			// detalle al mes actual y actualizamos como traspazado del mes
			// anterior
			HashMap<String, Object> p = new HashMap<>();
			p.put("estado", "NOPAG");
			p.put("idLlave", idLlave);
			List<CabeceraPlanilla> planillasNoPagadas = findAllByNamedQuery("CabeceraPlanilla.findAllNoPag", p);
			for (CabeceraPlanilla planillaNoPagada : planillasNoPagadas) {
				p = new HashMap<>(0);
				p.put("idCabeceraPlanilla", planillaNoPagada);
				List<DetallePlanilla> detallePlanillasNoPagadas = detallePlanillaBO.findAllByNamedQuery("DetallePlanilla.findByCabecaraNoPag", p);
				for (DetallePlanilla detallePlanilla : detallePlanillasNoPagadas) {
					DetallePlanilla detallePlanillaNuevo = new DetallePlanilla();
					detallePlanillaNuevo.setIdLectura(detallePlanilla.getIdLectura());
					detallePlanillaNuevo.setIdRegistroEconomico(detallePlanilla.getIdRegistroEconomico());
					detallePlanillaNuevo.setEstado("ING");
					detallePlanillaNuevo.setValorTotal(detallePlanilla.getValorTotal());
					detallePlanillaNuevo.setFechaRegistro(detallePlanilla.getFechaRegistro() != null ? detallePlanilla.getFechaRegistro() : Calendar.getInstance().getTime());
					detallePlanillaNuevo.setValorUnidad(detallePlanilla.getValorUnidad());
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

					detallePlanillaNuevo.setValorPagado(0.0);
					detallePlanillaNuevo.setValorPendiente(detallePlanillaNuevo.getValorTotal());
					detallePlanillaNuevo.setIdCabeceraPlanilla(planillaNueva);
					detallePlanillaNuevo.setDescripcion(detallePlanilla.getDescripcion());
					detallePlanillaNuevo.setValorTotalOrigen(detallePlanilla.getValorTotalOrigen() == null ? detallePlanilla.getValorTotal() : detallePlanilla.getValorTotalOrigen());
					if (Constantes.origen_mes_Actual.equals(detallePlanilla.getOrigen())) {
						detallePlanillaNuevo.setOrigen(Constantes.origen_no_pagado);
					} else {
						detallePlanillaNuevo.setOrigen(detallePlanilla.getOrigen());
					}
					detallePlanillaBO.save(usuario, detallePlanillaNuevo);
					planillaNueva.setSubtotal(planillaNueva.getSubtotal() + detallePlanillaNuevo.getValorTotal());
					planillaNueva.setTotal(planillaNueva.getTotal() + detallePlanillaNuevo.getValorTotal());

				}
				cambioEstadoBO.cambiarEstadoSinVerificar(22, usuario, planillaNoPagada.getIdCabeceraPlanilla(), "");
				Query queryDetalle = em().createQuery("UPDATE DetallePlanilla SET estado='TRAS' where estado='NOPAG' AND idCabeceraPlanilla=:idCabeceraPlanilla");
				queryDetalle.setParameter("idCabeceraPlanilla", planillaNoPagada);
				queryDetalle.executeUpdate();
			}

			// Obtenemos las planillas pagadas Incompletas
			p = new HashMap<>();
			p.put("estado", "INC");
			p.put("idLlave", idLlave);
			planillasNoPagadas = findAllByNamedQuery("CabeceraPlanilla.findAllNoPag", p);
			for (CabeceraPlanilla planillasIncompletas : planillasNoPagadas) {
				p = new HashMap<>(0);
				p.put("idCabeceraPlanilla", planillasIncompletas);
				List<DetallePlanilla> detalleIncomepletos = detallePlanillaBO.findAllByNamedQuery("DetallePlanilla.findByCabecaraNoPagInc", p);
				for (DetallePlanilla detalleIncompleto : detalleIncomepletos) {

					DetallePlanilla detallePlanillaNuevo = new DetallePlanilla();
					detallePlanillaNuevo.setIdLectura(detalleIncompleto.getIdLectura());
					detallePlanillaNuevo.setIdRegistroEconomico(detalleIncompleto.getIdRegistroEconomico());
					detallePlanillaNuevo.setEstado("ING");
					detallePlanillaNuevo.setValorTotal(detalleIncompleto.getValorPendiente());
					detallePlanillaNuevo.setFechaRegistro(detalleIncompleto.getFechaRegistro() != null ? detalleIncompleto.getFechaRegistro() : Calendar.getInstance().getTime());
					detallePlanillaNuevo.setValorPendiente(detallePlanillaNuevo.getValorTotal());
					detallePlanillaNuevo.setValorPagado(0.0);
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
					detallePlanillaBO.save(usuario, detallePlanillaNuevo);
					planillaNueva.setSubtotal(planillaNueva.getSubtotal() + detallePlanillaNuevo.getValorTotal());
					planillaNueva.setTotal(planillaNueva.getTotal() + detallePlanillaNuevo.getValorTotal());
				}

				cambioEstadoBO.cambiarEstadoSinVerificar(36, usuario, planillasIncompletas.getIdCabeceraPlanilla(), "");
				// Actualizamos el estado de los detalles
				Query queryDetalle = em().createQuery("UPDATE DetallePlanilla SET estado='TRAS' where  idCabeceraPlanilla=:idCabeceraPlanilla AND estado IN ('ING','INC')");
				queryDetalle.setParameter("idCabeceraPlanilla", planillasIncompletas);
				queryDetalle.executeUpdate();

				// Actualizamos el estado de las lecturas a traspazadas
				Query queryLecturas = em().createQuery(" UPDATE Lectura SET estado='TRAS' where idLectura IN (SELECT dp.idLectura.idLectura FROM DetallePlanilla dp WHERE dp.idCabeceraPlanilla=:idCabeceraPlanilla)");
				queryLecturas.setParameter("idCabeceraPlanilla", planillasIncompletas);
				queryLecturas.executeUpdate();
			}

			// REGISTRAMOS LA LECTURA CON VALOR EN CERO PARA QUE LUEGO LA
			// INGRESE
			TipoRegistro registro = tipoRegistroBO.findByPk("CONS");
			HashMap<String, Object> mapAux = new HashMap<>();
			mapAux.put("idLlave", idLlave);
			mapAux.put("usuarioNuevo", true);
			Integer idLecturaAnteriorIngresada = lecturaBO.findIntegerByNamedQuery("Lectura.findLastByPeridoAndLlave", mapAux);

			Lectura lecturaAnterior = null;
			if (idLecturaAnteriorIngresada != null)
				lecturaAnterior = lecturaBO.findByPk(idLecturaAnteriorIngresada);
			Lectura lectura = new Lectura();
			lectura.setTipoRegistro(registro);
			lectura.setIdLlave(idLlave);
			lectura.setSinLectura(false);
			lectura.setUsuarioNuevo(false);
			lectura.setEsModificable(false);
			lectura.setIdPeriodoPago(periodoPago);
			lectura.setEstado("ING");
			lectura.setDescripcion("Lectura de " + Utilitario.mes(periodoPago.getMes()) + " - " + periodoPago.getAnio().toString());
			lectura.setMetros3(0.0);
			lectura.setFechaRegistro(Calendar.getInstance().getTime());
			lectura.setLecturaIngresada(0.0);
			lectura.setValorMetro3Exceso(0.0);
			lectura.setMetros3Exceso(0.0);
			lectura.setValorMetro3(0.0);
			lectura.setValorBasico(Utilitario.redondear(lectura.getIdLlave().getIdTarifa().getBasicoPago()));

			if (lecturaAnterior != null) {
				Double metros3Anterior = Utilitario.redondear(lecturaAnterior.getMetros3() + lecturaAnterior.getMetros3Exceso());
				lectura.setMetros3Anterior(metros3Anterior);
				if (!lecturaAnterior.getSinLectura())
					lectura.setLecturaAnterior(lecturaAnterior != null ? lecturaAnterior.getLecturaIngresada() : 0.0);
				else
					lectura.setLecturaAnterior(lecturaAnterior != null ? lecturaAnterior.getLecturaAnterior() : 0.0);
			} else
				lectura.setLecturaAnterior(0.0);

			lectura.setNumeroMeses(lecturaAnterior != null ? (Utilitario.numeroMeses(lecturaAnterior.getIdPeriodoPago().getFechaInicio(), periodoPago.getFechaInicio())) : 1);
			lecturaBO.save(usuario, lectura);
			planillaNueva.setValorPendiente(planillaNueva.getTotal());
			update(usuario, planillaNueva);

		}
		parametro.setValorEntero(numeroFactura);
		parametroBO.update(usuario, parametro);
	}

	@Override
	public void regenerarPlanillasDePeriodo(Usuario usuario, Integer idPeriodoPago) throws Exception {
		PeriodoPago periodoPago = periodoPagoBO.findByPk(idPeriodoPago);

		// Obtenemos las llaves que no estan generadas las facturas del periodo
		// actual
		map = new HashMap<>();
		map.put("idPeriodoPago", periodoPago);
		List<Llave> llaves = llaveBO.findAllByNamedQuery("Llave.findOfUsuariosActivosAndNotPeriodo", map);
		// Obtenemos el numero de factura
		Parametro parametro = parametroBO.findByPk("NUMFACT");
		Integer numeroFactura = parametro.getValorEntero();
		String path = "0000000000000";

		for (Llave idLlave : llaves) {

			// Verificamos si el usuario de la JAAP es nuevo
			numeroFactura++;
			CabeceraPlanilla cabeceraPlanilla = new CabeceraPlanilla();
			cabeceraPlanilla.setEstado("ING");
			cabeceraPlanilla.setFechaRegistro(Calendar.getInstance().getTime());
			cabeceraPlanilla.setDescuento(0.0);
			cabeceraPlanilla.setBase(0.0);
			cabeceraPlanilla.setIdLlave(idLlave);
			cabeceraPlanilla.setObservacion(path.substring(0, path.length() - numeroFactura.toString().length()) + numeroFactura.toString());
			cabeceraPlanilla.setSubtotal(0.0);
			cabeceraPlanilla.setTotal(0.0);
			cabeceraPlanilla.setValorPagado(0.0);
			cabeceraPlanilla.setValorPagadoAbono(0.0);
			cabeceraPlanilla.setIdPeriodoPago(periodoPagoBO.findByPk(idPeriodoPago));
			save(usuario, cabeceraPlanilla);
			cambioEstadoBO.cambiarEstadoSinVerificar(18, usuario, cabeceraPlanilla.getIdCabeceraPlanilla(), "");

			// Obtenemos las planillas no pagadas
			HashMap<String, Object> p = new HashMap<>();
			p.put("estado", "NOPAG");
			p.put("idLlave", idLlave);
			List<CabeceraPlanilla> planillasNoPagadas = findAllByNamedQuery("CabeceraPlanilla.findAllNoPag", p);
			for (CabeceraPlanilla planillaNoPagada : planillasNoPagadas) {
				p = new HashMap<>(0);
				p.put("idCabeceraPlanilla", planillaNoPagada);
				List<DetallePlanilla> detallePlanillas = detallePlanillaBO.findAllByNamedQuery("DetallePlanilla.findByCabecaraNoPag", p);
				for (DetallePlanilla detallePlanilla : detallePlanillas) {
					DetallePlanilla detallePlanillaNuevo = new DetallePlanilla();
					detallePlanillaNuevo.setIdLectura(detallePlanilla.getIdLectura());
					detallePlanillaNuevo.setIdRegistroEconomico(detallePlanilla.getIdRegistroEconomico());
					detallePlanillaNuevo.setEstado("ING");
					detallePlanillaNuevo.setValorTotal(detallePlanilla.getValorTotal());
					detallePlanillaNuevo.setFechaRegistro(detallePlanilla.getFechaRegistro() != null ? detallePlanilla.getFechaRegistro() : Calendar.getInstance().getTime());
					detallePlanillaNuevo.setValorPendiente(detallePlanillaNuevo.getValorTotal());
					detallePlanillaNuevo.setValorPagado(0.0);
					detallePlanillaNuevo.setValorUnidad(detallePlanilla.getValorUnidad());
					detallePlanillaNuevo.setIdCabeceraPlanilla(cabeceraPlanilla);
					detallePlanillaNuevo.setDescripcion(detallePlanilla.getDescripcion());
					if (detallePlanilla.getIdRegistroEconomico() != null) {
						String tipoRegistro = detallePlanilla.getIdRegistroEconomico().getTipoRegistro().getTipoRegistro();
						if ("CONS".equals(tipoRegistro)) {
							detallePlanillaNuevo.setOrdenStr("BB");
						} else if ("MULAGU".equals(tipoRegistro) || "BASCON".equalsIgnoreCase(tipoRegistro)) {
							detallePlanillaNuevo.setOrdenStr("AA");
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
						detallePlanilla.setOrdenStr(detallePlanilla.getOrdenStr() + detallePlanilla.getOrdenStr().substring(0, 1));
					}
					detallePlanillaNuevo.setValorTotalOrigen(detallePlanilla.getValorTotalOrigen() == null ? detallePlanilla.getValorTotal() : detallePlanilla.getValorTotalOrigen());

					if (Constantes.origen_mes_Actual.equals(detallePlanilla.getOrigen())) {
						detallePlanillaNuevo.setOrigen(Constantes.origen_no_pagado);
					} else {
						detallePlanillaNuevo.setOrigen(detallePlanilla.getOrigen());
					}
					detallePlanillaBO.save(usuario, detallePlanillaNuevo);
					cabeceraPlanilla.setSubtotal(cabeceraPlanilla.getSubtotal() + detallePlanillaNuevo.getValorTotal());
					cabeceraPlanilla.setTotal(cabeceraPlanilla.getTotal() + detallePlanillaNuevo.getValorTotal());
				}

				cambioEstadoBO.cambiarEstadoSinVerificar(22, usuario, planillaNoPagada.getIdCabeceraPlanilla(), "");
				Query queryDetalle = em().createQuery("UPDATE DetallePlanilla SET estado='TRAS' where estado='NOPAG' AND idCabeceraPlanilla=:idCabeceraPlanilla");
				queryDetalle.setParameter("idCabeceraPlanilla", planillaNoPagada);
				queryDetalle.executeUpdate();
			}

			// Obtenemos las planillas pagadas Incompletas
			p = new HashMap<>();
			p.put("estado", "INC");
			p.put("idLlave", idLlave);
			planillasNoPagadas = findAllByNamedQuery("CabeceraPlanilla.findAllNoPag", p);
			for (CabeceraPlanilla planillasIncompletas : planillasNoPagadas) {
				p = new HashMap<>(0);
				p.put("idCabeceraPlanilla", planillasIncompletas);
				List<DetallePlanilla> detalleIncomepletos = detallePlanillaBO.findAllByNamedQuery("DetallePlanilla.findByCabecaraNoPagInc", p);
				for (DetallePlanilla detalleIncompleto : detalleIncomepletos) {
					DetallePlanilla detallePlanillaNuevo = new DetallePlanilla();
					detallePlanillaNuevo.setIdLectura(detalleIncompleto.getIdLectura());
					detallePlanillaNuevo.setIdRegistroEconomico(detalleIncompleto.getIdRegistroEconomico());
					detallePlanillaNuevo.setEstado("ING");
					detallePlanillaNuevo.setValorTotal(detalleIncompleto.getValorPendiente());
					detallePlanillaNuevo.setFechaRegistro(detalleIncompleto.getFechaRegistro() != null ? detalleIncompleto.getFechaRegistro() : Calendar.getInstance().getTime());
					detallePlanillaNuevo.setValorPendiente(detallePlanillaNuevo.getValorTotal());
					detallePlanillaNuevo.setValorPagado(0.0);
					detallePlanillaNuevo.setValorUnidad(detalleIncompleto.getValorUnidad());
					detallePlanillaNuevo.setIdCabeceraPlanilla(cabeceraPlanilla);
					detallePlanillaNuevo.setDescripcion(detalleIncompleto.getDescripcion());
					if (detalleIncompleto.getIdRegistroEconomico() != null) {
						String tipoRegistro = detalleIncompleto.getIdRegistroEconomico().getTipoRegistro().getTipoRegistro();
						if ("CONS".equals(tipoRegistro)) {
							detallePlanillaNuevo.setOrdenStr("BB");
						} else if ("MULAGU".equals(tipoRegistro) || "BASCON".equalsIgnoreCase(tipoRegistro)) {
							detallePlanillaNuevo.setOrdenStr("AA");
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
						detallePlanillaNuevo.setOrdenStr(detalleIncompleto.getOrdenStr() + detalleIncompleto.getOrdenStr());
					}

					detallePlanillaNuevo.setValorTotalOrigen(detalleIncompleto.getValorTotalOrigen() == null ? detalleIncompleto.getValorTotal() : detalleIncompleto.getValorTotalOrigen());
					detallePlanillaNuevo.setOrigen(Constantes.origen_pagado_incompleto);
					detallePlanillaBO.save(usuario, detallePlanillaNuevo);
					cabeceraPlanilla.setSubtotal(cabeceraPlanilla.getSubtotal() + detallePlanillaNuevo.getValorTotal());
					cabeceraPlanilla.setTotal(cabeceraPlanilla.getTotal() + detallePlanillaNuevo.getValorTotal());
				}

				cambioEstadoBO.cambiarEstadoSinVerificar(36, usuario, planillasIncompletas.getIdCabeceraPlanilla(), "");
				// Actualizamos el estado de los detalles
				Query queryDetalle = em().createQuery("UPDATE DetallePlanilla SET estado='TRAS' where  idCabeceraPlanilla=:idCabeceraPlanilla AND estado IN ('ING','INC')");
				queryDetalle.setParameter("idCabeceraPlanilla", planillasIncompletas);
				queryDetalle.executeUpdate();

				// Actualizamos el estado de las lecturas a traspazadas
				Query queryLecturas = em().createQuery(" UPDATE Lectura SET estado='TRAS' where idLectura IN (SELECT dp.idLectura.idLectura FROM DetallePlanilla dp WHERE dp.idCabeceraPlanilla=:idCabeceraPlanilla)");
				queryLecturas.setParameter("idCabeceraPlanilla", planillasIncompletas);
				queryLecturas.executeUpdate();
			}

			// REGISTRAMOS LA LECTURA CON VALOR EN CERO PARA QUE LUEGO LA
			// INGRESE
			TipoRegistro registro = tipoRegistroBO.findByPk("CONS");
			map = new HashMap<>();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(periodoPago.getFechaInicio());
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			map.put("mes", calendar.get(Calendar.MONTH));
			map.put("anio", calendar.get(Calendar.YEAR));
			map.put("llave", idLlave);
			Lectura lecturaAnterior = lecturaBO.findByNamedQuery("Lectura.findByAnioAndMes", map);
			Lectura lectura = new Lectura();
			lectura.setTipoRegistro(registro);
			lectura.setIdLlave(idLlave);
			lectura.setSinLectura(false);
			lectura.setUsuarioNuevo(false);
			lectura.setIdPeriodoPago(periodoPago);
			lectura.setEstado("ING");
			lectura.setEsModificable(false);
			lectura.setDescripcion("Lectura " + periodoPago.getDescripcion());
			lectura.setMetros3(0.0);
			lectura.setFechaRegistro(Calendar.getInstance().getTime());
			lectura.setLecturaIngresada(0.0);
			lectura.setLecturaAnterior(lecturaAnterior != null ? lecturaAnterior.getLecturaIngresada() : lectura.getLecturaAnterior() != null ? lectura.getLecturaAnterior() : 0.0);
			lecturaBO.save(usuario, lectura);

			update(usuario, cabeceraPlanilla);

		}
		
		parametro.setValorEntero(numeroFactura);
		parametroBO.update(usuario, parametro);

	}

	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public void cerrarLecturasAPlanilla(Usuario usuario, Integer idPeriodoPago) throws Exception {

		HashMap<String, Object> map = new HashMap<>();

		// Verificamos que todas las lecturas esten correctamente ingresadas
		map.put("idPeriodoPago", idPeriodoPago);
		map.put("usuarioNuevo", true);
		map.put("sinLectura", true);
		List<Lectura> lecturas = lecturaBO.findAllByNamedQuery("Lectura.findErroneoByPeriodo", map);
		if (!lecturas.isEmpty())
			throw new Exception("Existen lecturas mal ingresadas, revice las lecturas.");

		// Obtenemos el periodo de pago
		PeriodoPago periodoPago = periodoPagoBO.findByPk(idPeriodoPago);

		// Obtenemso las lecturas del mes, incluido la llave y las facturas
		map.clear();
		map.put("estado", "ING");
		map.put("idPeriodoPago", idPeriodoPago);
		List<Object[]> objects = lecturaBO.findObjects("Lectura.findByPerido", map);

		// Obtenemos las actividades
		map.clear();
		map.put("idPeriodoPago", periodoPago);
		List<Actividad> actividads = actividadBO.findAllByNamedQuery("Actividad.findAllByPeriodo", map);

		// Obtenemos el registro economico de las Inasistencias
		map.clear();
		map.put("idPeriodoPago", periodoPago);
		map.put("tipoRegistro", "INASIS");
		RegistroEconomico registroEconomicoInasistencias = registroEconomicoBO.findByNamedQuery("RegistroEconomico.findByType", map);

		// Cantidad de usuarios CON EL BASICO
		Integer cantidadMultaAtrazosAplicados = 0;
		RegistroEconomico registroEconomicoBasico = registroEconomicoBO.inicializar(periodoPago, "BASCON", " " + periodoPago.getDescripcion(), 0, usuario);

		// Variable que indica para que se elimine el registro economico de
		// enasistencia en caso de que no exista ningun registro de ello
		Boolean eliminarRegistroEconomicoInasistencias = true;

		for (Object[] object : objects) {
			CabeceraPlanilla cp = (CabeceraPlanilla) object[0];
			Llave llave = (Llave) object[1];
			// Obtenemos la lectura de la llave
			map = new HashMap<>();
			map.put("idLlave", llave.getIdLlave());
			map.put("idPeriodoPago", periodoPago);
			Lectura lectura = lecturaBO.findByNamedQuery("Lectura.findByPeridoAndLlave", map);

			Boolean debeRegistrarDetalle = true;
			if (lectura != null) {
				DetallePlanilla detallePlanilla = new DetallePlanilla();

				detallePlanilla.setEstado("ING");
				detallePlanilla.setIdLectura(lectura);
				detallePlanilla.setIdCabeceraPlanilla(cp);
				detallePlanilla.setValorPagado(0.0);
				detallePlanilla.setValorPendiente(0.0);
				detallePlanilla.setValorTotal(0.0);
				detallePlanilla.setFechaRegistro(Calendar.getInstance().getTime());
				detallePlanilla.setValorUnidad(0.0);
				detallePlanilla.setOrdenStr("B");

				DetallePlanilla detallePlanillaBasico = new DetallePlanilla();
				detallePlanillaBasico.setEstado("ING");
				detallePlanillaBasico.setIdCabeceraPlanilla(cp);
				detallePlanillaBasico.setIdRegistroEconomico(registroEconomicoBasico);
				detallePlanillaBasico.setFechaRegistro(Calendar.getInstance().getTime());
				detallePlanillaBasico.setValorUnidad(Utilitario.redondear(lectura.getValorBasico()));
				detallePlanillaBasico.setValorPagado(0.0);
				detallePlanillaBasico.setValorTotal(Utilitario.redondear(lectura.getValorBasico()));
				detallePlanillaBasico.setValorPendiente(detallePlanillaBasico.getValorTotal());
				detallePlanillaBasico.setDescripcion("Básico " + periodoPago.getDescripcion());
				detallePlanillaBasico.setOrdenStr("B");
				detallePlanillaBasico.setValorTotalOrigen(detallePlanillaBasico.getValorTotal());
				detallePlanillaBasico.setOrigen(Constantes.origen_mes_Actual);
				cp.setSubtotal(Utilitario.redondear(cp.getSubtotal() + detallePlanillaBasico.getValorTotal()));
				cp.setTotal(Utilitario.redondear(cp.getTotal() + detallePlanillaBasico.getValorTotal()));
				registroEconomicoBasico.setValor(registroEconomicoBasico.getValor() + detallePlanillaBasico.getValorTotal());
				detallePlanillaBO.save(usuario, detallePlanillaBasico);
				cantidadMultaAtrazosAplicados++;

				if (lectura.getUsuarioNuevo()) {
					debeRegistrarDetalle = false;
				} else if (lectura.getSinLectura()) {
					debeRegistrarDetalle = false;
				} else {
					// Si existe valor en la lectura existe consumo caso
					// contrario solo se cobra el valor basico acorde al tipo de
					// llave
					if (lectura.getMetros3() > 0) {
						detallePlanilla.setValorUnidad(Utilitario.redondear(lectura.getValorMetro3()));
						detallePlanilla.setValorTotal(Utilitario.redondear((lectura.getMetros3() != null && lectura.getValorMetro3() != null ? (lectura.getMetros3() * lectura.getValorMetro3()) : 0.0) + (lectura.getMetros3Exceso() != null && lectura.getValorMetro3Exceso() != null && lectura.getMetros3Exceso() > 0 && lectura.getValorMetro3Exceso() > 0 ? (lectura.getMetros3Exceso() * lectura.getValorMetro3Exceso()) : 0.0)));
						detallePlanilla.setValorPagado(0.0);
						detallePlanilla.setValorPendiente(detallePlanilla.getValorTotal());
						detallePlanilla.setDescripcion(Utilitario.redondear(lectura.getMetros3()) + " m3" + " " + periodoPago.getDescripcion());
					}
				}
				if (debeRegistrarDetalle) {
					if (lectura.getMetros3() > 0) {
						detallePlanilla.setValorTotalOrigen(detallePlanilla.getValorTotal());
						detallePlanilla.setOrigen(Constantes.origen_mes_Actual);
						detallePlanillaBO.save(usuario, detallePlanilla);
						cp.setSubtotal(Utilitario.redondear(cp.getSubtotal() + detallePlanilla.getValorTotal()));
						cp.setTotal(Utilitario.redondear(cp.getTotal() + detallePlanilla.getValorTotal()));
					}
				}
			}

		}
		registroEconomicoBasico.setCantidadAplicados(cantidadMultaAtrazosAplicados);
		registroEconomicoBO.update(usuario, registroEconomicoBasico);
		if (eliminarRegistroEconomicoInasistencias) {
			cambioEstadoBO.eliminarEntidad(8, registroEconomicoInasistencias.getIdRegistroEconomico());
		} else {
			// Cambiamos el estado a las actividades y ponemos en aplicado para
			// que no s epueda editar
			for (Actividad actividad : actividads) {
				cambioEstadoBO.cambiarEstadoSinVerificar(43, usuario, actividad.getActividad(), "");
			}

			registroEconomicoBO.update(usuario, registroEconomicoInasistencias);
			cambioEstadoBO.cambiarEstadoSinVerificar(40, usuario, registroEconomicoInasistencias.getIdRegistroEconomico(), "");

		}

		// Cambiamos el estado de las cuotas
		map.clear();
		map.put("idPeriodoPago", periodoPago);
		map.put("tipoRegistro", tipoRegistroBO.findByPk("CUO"));
		List<RegistroEconomico> registroEconomicos = registroEconomicoBO.findAllByNamedQuery("RegistroEconomico.findByPeriodoAndTipo", map);
		for (RegistroEconomico registroEconomico : registroEconomicos) {
			cambioEstadoBO.cambiarEstadoSinVerificar(40, usuario, registroEconomico.getIdRegistroEconomico(), "");
		}

	}

	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public void regenerarPeriodoCerrado(Usuario usuario, Integer idPeriodoPago) throws Exception {
		HashMap<String, Object> map = new HashMap<>();
		map.clear();
		map.put("estado", "ING");
		map.put("idPeriodoPago", idPeriodoPago);
		map.put("esModificable", false);
		// Obtenemos las planillas llaves en las cuales la planilla aun no ha
		// sido pagada
		List<Object[]> objects = lecturaBO.findObjects("Lectura.findByPeridoCerrModificable", map);
		PeriodoPago periodoPago = periodoPagoBO.findByPk(idPeriodoPago);
		
		map.clear();
		map.put("idPeriodoPago", periodoPago);
		map.put("tipoRegistro", "BASCON");
		RegistroEconomico registroEconomicoBasico = registroEconomicoBO.findByNamedQuery("RegistroEconomico.findByType", map);
		// Cantidad de usuarios CON EL BASIC
		Integer cantidadUsuariosConBasico = registroEconomicoBasico.getCantidadAplicados();
		for (Object[] object : objects) {

			CabeceraPlanilla cp = (CabeceraPlanilla) object[0];
			Llave llave = (Llave) object[1];
			// Obtenemos la lectura
			map = new HashMap<>();
			map.put("idLlave", llave.getIdLlave());
			map.put("idPeriodoPago", periodoPago);
			Lectura lectura = lecturaBO.findByNamedQuery("Lectura.findByPeridoAndLlave", map);

			if (lectura != null) {
				HashMap<String, Object> pama = new HashMap<>();
				pama.put("idLectura", lectura);

				DetallePlanilla detallePlanilla = detallePlanillaBO.findByNamedQuery("DetallePlanilla.findByLectura", pama);
				DetallePlanilla detallePlanillaBasico = null;
				if (detallePlanilla == null) {
					detallePlanilla = new DetallePlanilla();
					detallePlanilla.setEstado("ING");
					detallePlanilla.setOrdenStr("B");
					detallePlanilla.setIdLectura(lectura);
					detallePlanilla.setIdCabeceraPlanilla(cp);
					detallePlanilla.setValorPagado(0.0);
					detallePlanilla.setValorPendiente(0.0);
					detallePlanilla.setValorTotal(0.0);
					detallePlanilla.setFechaRegistro(Calendar.getInstance().getTime());
					detallePlanilla.setValorUnidad(0.0);
					pama = new HashMap<>();
					pama.put("regeco", registroEconomicoBasico);
					pama.put("cp", cp);
					pama.put("desc", "Básico");
					DetallePlanilla dpb = detallePlanillaBO.findByNamedQuery("DetallePlanilla.findByBasico", pama);
					if(dpb==null){
						detallePlanillaBasico = new DetallePlanilla();
						detallePlanillaBasico.setEstado("ING");
						detallePlanillaBasico.setIdCabeceraPlanilla(cp);
						detallePlanillaBasico.setIdRegistroEconomico(registroEconomicoBasico);
						detallePlanillaBasico.setFechaRegistro(Calendar.getInstance().getTime());
						detallePlanillaBasico.setValorUnidad(Utilitario.redondear(lectura.getValorBasico()));
						detallePlanillaBasico.setValorPagado(0.0);
						detallePlanillaBasico.setValorTotal(Utilitario.redondear(lectura.getValorBasico()));
						detallePlanillaBasico.setValorPendiente(detallePlanillaBasico.getValorTotal());
						detallePlanillaBasico.setDescripcion("Básico " + periodoPago.getDescripcion());
						detallePlanillaBasico.setOrdenStr("B");
						detallePlanillaBasico.setValorTotalOrigen(detallePlanillaBasico.getValorTotal());
						detallePlanillaBasico.setOrigen(Constantes.origen_mes_Actual);
						cp.setSubtotal(Utilitario.redondear(cp.getSubtotal() + detallePlanillaBasico.getValorTotal()));
						cp.setTotal(Utilitario.redondear(cp.getTotal() + detallePlanillaBasico.getValorTotal()));
						registroEconomicoBasico.setValor(registroEconomicoBasico.getValor() + detallePlanillaBasico.getValorTotal());
						detallePlanillaBasico.setValorTotalOrigen(detallePlanillaBasico.getValorTotal());
						detallePlanillaBasico.setOrigen(Constantes.origen_mes_Actual);
						detallePlanillaBO.save(usuario, detallePlanillaBasico);
						cantidadUsuariosConBasico++;
					}
					
				}
				// Si es mayor a cero realizamos el calculo caso contrario
				// cobramos el basico
				if (lectura.getMetros3() > 0) {
					detallePlanilla.setValorUnidad(Utilitario.redondear(lectura.getValorMetro3()));
					detallePlanilla.setValorTotal(Utilitario.redondear((lectura.getMetros3() != null && lectura.getValorMetro3() != null ? (lectura.getMetros3() * lectura.getValorMetro3()) : 0.0) + (lectura.getMetros3Exceso() != null && lectura.getValorMetro3Exceso() != null && lectura.getMetros3Exceso() > 0 && lectura.getValorMetro3Exceso() > 0 ? (lectura.getMetros3Exceso() * lectura.getValorMetro3Exceso()) : 0.0)));
					detallePlanilla.setValorPendiente(detallePlanilla.getValorTotal());
					detallePlanilla.setValorPagado(0.0);
					detallePlanilla.setDescripcion(Utilitario.redondear(lectura.getMetros3()) + " m3 " + periodoPago.getDescripcion());
				}

				if (lectura.getMetros3() > 0)
					if (detallePlanilla.getIdDetallePlanilla() == null) {
						detallePlanilla.setValorTotalOrigen(detallePlanilla.getValorTotal());
						detallePlanilla.setOrigen(Constantes.origen_mes_Actual);
						detallePlanillaBO.save(usuario, detallePlanilla);
					} else
						detallePlanillaBO.update(usuario, detallePlanilla);

				// Recalculamos el valos de la planilla
				map.clear();
				map.put("idCabeceraPlanilla", cp);
				List<DetallePlanilla> detallePlanillas = detallePlanillaBO.findAllByNamedQuery("DetallePlanilla.findByCabecara", map);
				Double total = 0.0;
				for (DetallePlanilla dp : detallePlanillas) {
					total = total + dp.getValorTotal();
				}
				cp.setTotal(Utilitario.redondear(total));
				cp.setSubtotal(Utilitario.redondear(total));

				if (cp.getValorPagado() > cp.getTotal()) {
					cp.setAbonoUsd(cp.getAbonoUsd() + (cp.getValorPagado() - cp.getTotal()));
					cp.setValorPagado(cp.getTotal());
				}
				update(usuario, cp);
			}

		}
		registroEconomicoBasico.setCantidadAplicados(cantidadUsuariosConBasico);
		registroEconomicoBO.update(usuario, registroEconomicoBasico);
	}

	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	@Override
	public void finalizarPlanilla(Usuario usuario, Integer idPeriodoPago) throws Exception {
		PeriodoPago periodoPago = periodoPagoBO.findByPk(idPeriodoPago);
		HashMap<String, Object> p = new HashMap<>();

		// OBTENEMOS LAS PLANILLAS Y SI EL VALOR PENDIENTE DEL DETALLE ES CERO
		// LA PONEMOS COMO PAGAD

		// AQUI PAGAMOS LAS PLANILAS QUE NO FUERON PAGADAS Y QUE ANTERIORMENTE
		// TENIAN UN MONTO DE ABONO LAS PODEMOS PAGARLAS PARCIALEMTE O EN SU
		// TOTALIDAD

		p.put("estado", "ING");
		p.put("estado2", "ING");
		p.put("idPeriodoPago", idPeriodoPago);
		List<CabeceraPlanilla> planillasPagadasConAbono = findAllByNamedQuery("CabeceraPlanilla.findSinPagar", p);
		for (CabeceraPlanilla planillaActual : planillasPagadasConAbono) {
			CabeceraPlanilla planillaAnterior = getAbonoMesAnterior(planillaActual.getIdLlave(), planillaActual);

			if (planillaAnterior != null && planillaAnterior.getAbonoUsd() != 0.0) {
				Double abonoUsd = planillaAnterior.getAbonoUsd();

				planillaAnterior.setAbonoUsd(0.0);
				planillaActual.setAbonoUsd(abonoUsd);

				update(usuario, planillaActual);
				update(usuario, planillaAnterior);
			}

		}

		// Actualizamos de estado a las planillas que no fueron pagadas
		p.put("estado", "ING");
		p.put("estado2", "ING");
		p.put("idPeriodoPago", idPeriodoPago);
		List<CabeceraPlanilla> planillasNoPagadas = findAllByNamedQuery("CabeceraPlanilla.findNoPag", p);
		for (CabeceraPlanilla cp : planillasNoPagadas) {
			cambioEstadoBO.cambiarEstadoSinVerificar(20, usuario, cp.getIdCabeceraPlanilla(), "");
			Query queryDetalle = em().createQuery(" UPDATE DetallePlanilla SET estado='NOPAG' where idCabeceraPlanilla=:idCabeceraPlanilla");
			queryDetalle.setParameter("idCabeceraPlanilla", cp);
			Query queryLecturas = em().createQuery(" UPDATE Lectura SET estado='NOPAG' where idLectura IN (SELECT dp.idLectura.idLectura FROM DetallePlanilla dp WHERE dp.idCabeceraPlanilla=:idCabeceraPlanilla)");
			queryLecturas.setParameter("idCabeceraPlanilla", cp);
			queryDetalle.executeUpdate();
			queryLecturas.executeUpdate();
		}
		Double valorDevuelto = 0.0;
		// AQUI VA EL CALCULO DEL VALOR TOMADO DE LOS ABONOS PARA REDUCIR EL
		// VALOR DE LAS CUENTAS POR PAGAR
		p.put("estado", "PAG");
		p.put("estado2", "INC");
		p.put("idPeriodoPago", idPeriodoPago);
		List<CabeceraPlanilla> planillasPagadas = findAllByNamedQuery("CabeceraPlanilla.findConAbono", p);
		Double valorRestantePorDevolver = 0.0;
		Double valorPagadoAbonoIngresado = 0.0;
		RegistroEconomico registroEconomicoPorDevolverAnterior = null;

		Calendar fechaInicioAnterior = new GregorianCalendar();
		Calendar fechaFinalAnterior = new GregorianCalendar();
		fechaInicioAnterior.setTime(periodoPago.getFechaInicio());
		fechaInicioAnterior.add(Calendar.MONTH, -1);
		fechaInicioAnterior.set(Calendar.DAY_OF_MONTH, 1);

		fechaFinalAnterior.setTime(periodoPago.getFechaInicio());
		fechaFinalAnterior.set(Calendar.DAY_OF_MONTH, 1);
		fechaFinalAnterior.add(Calendar.DAY_OF_YEAR, -1);

		HashMap<String, Object> parametros = new HashMap<>();
		parametros.put("fechaInicio", fechaInicioAnterior.getTime());
		parametros.put("fechaFin", fechaFinalAnterior.getTime());
		PeriodoPago periodoPagoAn = periodoPagoBO.findByNamedQuery("PeriodoPago.findByFechas", parametros);

		map.clear();
		map.put("idPeriodoPago", periodoPagoAn);
		map.put("tipoRegistro", "CUEPAG");
		registroEconomicoPorDevolverAnterior = registroEconomicoBO.findByNamedQuery("RegistroEconomico.findByType", map);

		for (CabeceraPlanilla planillaActual : planillasPagadas) {
			valorDevuelto += Utilitario.redondear(planillaActual.getValorPagadoAbono());
			if (registroEconomicoPorDevolverAnterior != null) {
				valorPagadoAbonoIngresado += Utilitario.redondear(registroEconomicoPorDevolverAnterior.getValor() - planillaActual.getValorPagadoAbono());
			}
		}
		if (registroEconomicoPorDevolverAnterior != null) {
			valorRestantePorDevolver = Utilitario.redondear(registroEconomicoPorDevolverAnterior.getValor() - valorPagadoAbonoIngresado);
			registroEconomicoPorDevolverAnterior.setValor(0.0);
			registroEconomicoBO.update(usuario, registroEconomicoPorDevolverAnterior);
		} else {

		}
		// Calculamos el valor por devolver y que se encontrara en caja como
		// capital
		p.put("estado", "PAG");
		p.put("estado2", "INC");
		p.put("idPeriodoPago", idPeriodoPago);
		planillasPagadas = findAllByNamedQuery("CabeceraPlanilla.findNoPag", p);

		Double totalIngresoPeriodo = 0.0;
		Double totalPorDevolver = 0.0;
		for (CabeceraPlanilla cabeceraPlanilla : planillasPagadas) {
			totalIngresoPeriodo = totalIngresoPeriodo + (cabeceraPlanilla.getValorPagado() != null ? cabeceraPlanilla.getValorPagado() : 0.0);
			totalPorDevolver = totalPorDevolver + (cabeceraPlanilla.getAbonoUsd() != null ? cabeceraPlanilla.getAbonoUsd() : 0.0);
		}

		// AL Valor Total de este mes actual tambien sumamos el valor pendiente
		// por devolver del mes pasado
		if (valorRestantePorDevolver > 0.0)
			totalPorDevolver += valorRestantePorDevolver;
		// Registramos en caja por parte de servicios el valor pagado del
		// periodo
		cuentaBO.registrarAsiento(Constantes.numeroCaja, Constantes.cuentaCaja, Constantes.numeroServicio, Constantes.cuentaServicio, Utilitario.redondear(totalIngresoPeriodo), usuario);

		// Registramos en las cuentas por pagar de pero que estan en caja
		cuentaBO.registrarAsiento(Constantes.numeroCuentasPorPagar, Constantes.cuentaCuentasPorPagar, Constantes.numeroCaja, Constantes.cuentaCaja, Utilitario.redondear(totalPorDevolver), usuario);

		// Registramos en caja el valor total de los gastos

		map.clear();
		map.put("idPeriodoPago", idPeriodoPago);
		List<Gasto> gastos = gastoBO.findAllByNamedQuery("Gasto.findAllByUser", map);
		Double totalGastos = 0.0;
		for (Gasto gasto : gastos) {
			totalGastos = totalGastos + (gasto.getValor() != null ? gasto.getValor() : 0.0);
		}

		// Tambiena actualizamos en el registro economico de gastos
		map.clear();
		map.put("idPeriodoPago", periodoPagoBO.findByPk(idPeriodoPago));
		map.put("tipoRegistro", "GAST");
		RegistroEconomico registroEconomico = registroEconomicoBO.findByNamedQuery("RegistroEconomico.findByType", map);
		if (registroEconomico != null) {
			// Si no existe gastos eliminamos la entidad
			if (gastos.isEmpty()) {
				cambioEstadoBO.eliminarEntidad(8, registroEconomico.getIdRegistroEconomico());
			} else {
				registroEconomico.setValor(totalGastos);
				registroEconomicoBO.update(usuario, registroEconomico);
				cambioEstadoBO.cambiarEstadoSinVerificar(40, usuario, registroEconomico.getIdRegistroEconomico(), "");
			}
		}

		// Actualizamos el registro economico del valor por devolver
		map.clear();
		map.put("idPeriodoPago", periodoPagoBO.findByPk(idPeriodoPago));
		map.put("tipoRegistro", "CUEPAG");

		RegistroEconomico registroEconomicoPorDevolver = registroEconomicoBO.findByNamedQuery("RegistroEconomico.findByType", map);
		if (registroEconomicoPorDevolver != null) {
			if (totalPorDevolver.equals(0.0)) {
				cambioEstadoBO.eliminarEntidad(8, registroEconomicoPorDevolver.getIdRegistroEconomico());
			} else {
				registroEconomicoPorDevolver.setValor(totalPorDevolver);
				registroEconomicoBO.update(usuario, registroEconomicoPorDevolver);
				cambioEstadoBO.cambiarEstadoSinVerificar(40, usuario, registroEconomicoPorDevolver.getIdRegistroEconomico(), "");
			}
		}

		// Registramos los gastos en el libro diario
		cuentaBO.registrarAsiento(Constantes.numeroGasto, Constantes.cuentaGasto, Constantes.numeroCaja, Constantes.cuentaCaja, Utilitario.redondear(totalGastos), usuario);

		// Registramos en caja el valor devuelto con pago mediante abono en los
		// pagos por motivo de
		// abonos
		cuentaBO.registrarAsiento(Constantes.numeroCaja, Constantes.cuentaCaja, Constantes.numeroCuentasPorPagar, Constantes.cuentaCuentasPorPagar, Utilitario.redondear(valorDevuelto), usuario);

	}

	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	@Override
	public void pagarPlanilla(Usuario usuario, Integer idCabeceraPlanilla) throws Exception {
		CabeceraPlanilla cabeceraPlanilla = findByPk(idCabeceraPlanilla);
		cabeceraPlanilla.setFechaPago(Calendar.getInstance().getTime());
		update(usuario, cabeceraPlanilla);
		Query queryDetalle = em().createQuery(" UPDATE DetallePlanilla SET estado='PAG',valorPendiente=0.0,valorPagado=valorTotal where idCabeceraPlanilla.idCabeceraPlanilla=:idCabeceraPlanilla");
		queryDetalle.setParameter("idCabeceraPlanilla", idCabeceraPlanilla);
		Query queryLecturas = em().createQuery(" UPDATE Lectura SET estado='PAG' where idLectura IN (SELECT dp.idLectura.idLectura FROM DetallePlanilla dp WHERE dp.idCabeceraPlanilla.idCabeceraPlanilla=:idCabeceraPlanilla)");
		queryLecturas.setParameter("idCabeceraPlanilla", idCabeceraPlanilla);
		queryDetalle.executeUpdate();
		queryLecturas.executeUpdate();
	}

	@Override
	public CabeceraPlanilla getAbonoMesAnterior(Llave llave, CabeceraPlanilla actual) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("llave", llave);
		hashMap.put("cp", actual);

		List<CabeceraPlanilla> planillas = findAllByNamedQuery("CabeceraPlanilla.findAbono", hashMap);
		CabeceraPlanilla cabeceraPlanilla = new CabeceraPlanilla();
		// cabeceraPlanilla.setAbonoUsd(4.5);
		cabeceraPlanilla.setAbonoUsd(0.0);
		if (planillas.size() > 0)
			return planillas.get(0);
		return cabeceraPlanilla;
	}

	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public void descartarPago(Usuario usuario, Integer idPeriodoPago) throws Exception {
		CabeceraPlanilla cp = findByPk(idPeriodoPago);

		CabeceraPlanilla anterior = getAbonoMesAnterior(cp.getIdLlave(), cp);

		map.clear();
		map.put("idPeriodoPago", anterior.getIdPeriodoPago());
		map.put("tipoRegistro", "CUEPAG");
		RegistroEconomico registroEconomicoPorDevolver = registroEconomicoBO.findByNamedQuery("RegistroEconomico.findByType", map);
		Double valorAbonoAnterior = Utilitario.redondear(anterior.getAbonoUsd());

		if (registroEconomicoPorDevolver != null) {
			registroEconomicoPorDevolver.setValor(Utilitario.redondear(registroEconomicoPorDevolver.getValor() + valorAbonoAnterior));
			registroEconomicoBO.update(usuario, registroEconomicoPorDevolver);
		}

		cp.setValorCancelado(0.0);
		cp.setValorPagado(0.0);
		cp.setAbonoUsd(0.0);
		cp.setValorPagadoAbono(0.0);
		cp.setCambioUsd(0.0);
		cp.setValorPendiente(cp.getTotal());
		update(usuario, cp);
		detallePlanillaBO.descartarPago(cp, usuario);
		// REGRESAMOA A LA LECTURA EL CAMPO MODIFIFICABLE EN TRUE
		map.clear();
		map.put("idPeriodoPago", cp.getIdPeriodoPago());
		map.put("idLlave", cp.getIdLlave());

		List<Lectura> lecturas = lecturaBO.findAllByNamedQuery("Lectura.findByPeriod", map);
		if (!lecturas.isEmpty()) {
			Lectura lectura = lecturas.get(0);
			lectura.setEsModificable(false);
			lecturaBO.update(usuario, lectura);
		}

	}

	@Override
	public void guardarPlanilla(Usuario usuario, Double valorAPagar, CabeceraPlanilla cp, CabeceraPlanilla anterior, Double valorAbono, Integer idCambioEstado, Object idDocumento) throws Exception {
		Double valorMaximoAPagar = Utilitario.redondear(cp.getValorPendiente());
		if (valorAPagar > valorMaximoAPagar) {
			throw new Exception("El valor ingresado es mayor al valor pendiente");
		}
		Double valorAbonoPagado = 0.0;
		if (anterior.getAbonoUsd() > cp.getValorPendiente()) {
			valorAbonoPagado = Utilitario.redondear(cp.getValorPendiente());
		} else {
			valorAbonoPagado = Utilitario.redondear(anterior.getAbonoUsd());
		}
		cp.setValorPagadoAbono(Utilitario.redondear(cp.getValorPagadoAbono() + valorAbonoPagado));
		cp.setValorPagado(Utilitario.redondear(cp.getValorPagado() + valorAPagar));
		cp.setAbonoUsd(Utilitario.redondear(cp.getAbonoUsd() + valorAbono));
		cp.setValorPendiente(Utilitario.redondear(cp.getTotal() - cp.getValorPagado()));
		cp.setFechaPago(Calendar.getInstance().getTime());
		map.clear();
		map.put("idPeriodoPago", anterior.getIdPeriodoPago());
		map.put("tipoRegistro", "CUEPAG");
		RegistroEconomico registroEconomicoPorDevolver = registroEconomicoBO.findByNamedQuery("RegistroEconomico.findByType", map);
		if (registroEconomicoPorDevolver != null) {
			if (registroEconomicoPorDevolver.getValor() > 0)
				registroEconomicoPorDevolver.setValor(Utilitario.redondear(registroEconomicoPorDevolver.getValor() - anterior.getAbonoUsd()));
			else
				// Si la cuenta ya no tiene ningun valor le seteamos con valor
				// cero
				registroEconomicoPorDevolver.setValor(0.0);
			registroEconomicoBO.update(usuario, registroEconomicoPorDevolver);
		}

		update(usuario, cp);
		// DEJAMOS EL ABONO ANTERIOR EN CERO
		anterior.setAbonoUsd(0.0);
		if (anterior.getIdCabeceraPlanilla() != null) {
			update(usuario, anterior);
		}
		if (cp.getValorPagado().equals(Utilitario.redondear(cp.getTotal()))) {
			cambioEstadoBO.cambiarEstadoMandatory(idCambioEstado, usuario, idDocumento);

		} else {
			map.clear();
			map.put("idCabeceraPlanilla", cp);
			List<DetallePlanilla> detallePlanillas = detallePlanillaBO.findAllByNamedQuery("DetallePlanilla.findByCabecaraSinPagar", map);

			for (DetallePlanilla detallePlanilla : detallePlanillas) {
				if (valorAPagar != 0.0) {
					if (valorAPagar >= Utilitario.redondear(detallePlanilla.getValorTotal())) {
						detallePlanilla.setEstado("PAG");
						detallePlanilla.setValorPagado(Utilitario.redondear(detallePlanilla.getValorTotal()));
						valorAPagar = Utilitario.redondear(valorAPagar - Utilitario.redondear(detallePlanilla.getValorTotal()));
					} else {
						detallePlanilla.setEstado("INC");
						detallePlanilla.setValorPagado(valorAPagar);
						valorAPagar = 0.0;
					}
					detallePlanilla.setValorPendiente(Utilitario.redondear(detallePlanilla.getValorTotal() - detallePlanilla.getValorPagado()));
					detallePlanillaBO.update(usuario, detallePlanilla);
				}
			}
			cambioEstadoBO.cambiarEstadoMandatory(35, usuario, idDocumento);
		}
	}

	@Override
	public void recalcularPlanilla(Usuario usuario, CabeceraPlanilla cabeceraPlanilla, Lectura lectura) throws Exception {

		if ("ING".equalsIgnoreCase(cabeceraPlanilla.getEstado())) {
			lectura = lecturaBO.recalcularLectura(usuario, lectura);
			CabeceraPlanilla cp = cabeceraPlanilla;
			lecturaBO.update(usuario, lectura);
			HashMap<String, Object> pama = new HashMap<>();
			pama.put("idLectura", lectura);
			pama.put("idCabeceraPlanilla", cabeceraPlanilla);
			DetallePlanilla detallePlanilla = detallePlanillaBO.findByNamedQuery("DetallePlanilla.findByLecturaAndCabcera", pama);

			// Si es mayor a cero realizamos el calculo caso contrario
			// cobramos el basico
			if (lectura.getMetros3() > 0) {
				detallePlanilla.setValorUnidad(Utilitario.redondear(lectura.getValorMetro3()));
				detallePlanilla.setValorTotal(Utilitario.redondear((lectura.getMetros3() != null && lectura.getValorMetro3() != null ? (lectura.getMetros3() * lectura.getValorMetro3()) : 0.0) + (lectura.getMetros3Exceso() != null && lectura.getValorMetro3Exceso() != null && lectura.getMetros3Exceso() > 0 && lectura.getValorMetro3Exceso() > 0 ? (lectura.getMetros3Exceso() * lectura.getValorMetro3Exceso()) : 0.0)));
				detallePlanilla.setValorPendiente(detallePlanilla.getValorTotal());
				detallePlanilla.setValorPagado(0.0);
				detallePlanilla.setDescripcion(Utilitario.redondear(lectura.getMetros3()) + " m3 " + Utilitario.descricionMes(lectura.getFechaRegistro()));
			}
			if (lectura.getMetros3() > 0)
				detallePlanillaBO.update(usuario, detallePlanilla);
			// Recalculamos el valos de la planilla
			map.clear();
			map.put("idCabeceraPlanilla", cp);
			List<DetallePlanilla> detallePlanillas = detallePlanillaBO.findAllByNamedQuery("DetallePlanilla.findByCabecara", map);
			Double total = 0.0;
			for (DetallePlanilla dp : detallePlanillas) {
				total = total + dp.getValorTotal();
			}
			cp.setTotal(Utilitario.redondear(total));
			cp.setSubtotal(Utilitario.redondear(total));
			if (cp.getValorPagado() > cp.getTotal()) {
				cp.setAbonoUsd(cp.getAbonoUsd() + (cp.getValorPagado() - cp.getTotal()));
				cp.setValorPagado(cp.getTotal());
				cp.setValorPendiente(0.0);
			} else {
				cp.setValorPendiente(Utilitario.redondear(cp.getTotal()) - Utilitario.redondear(cp.getValorPagado()));
			}
			update(usuario, cp);

		}else{
			lectura = lecturaBO.recalcularLectura(usuario, lectura);
			lecturaBO.update(usuario, lectura);
			HashMap<String, Object> pama = new HashMap<>();
			pama.put("idLectura", lectura);
			pama.put("idCabeceraPlanilla", cabeceraPlanilla);
			DetallePlanilla detallePlanilla = detallePlanillaBO.findByNamedQuery("DetallePlanilla.findByLecturaAndCabcera", pama);
			// Si es mayor a cero realizamos el calculo caso contrario
			// cobramos el basico
			if (lectura.getMetros3() > 0) {
				detallePlanilla.setDescripcion(Utilitario.redondear(lectura.getMetros3()) + " m3 " + Utilitario.descricionMes(lectura.getFechaRegistro()));
			}
			if (lectura.getMetros3() > 0)
				detallePlanillaBO.update(usuario, detallePlanilla);
		}
	}

}
