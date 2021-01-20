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

import org.apache.log4j.Logger;
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
import org.ec.jap.entiti.saap.Usuario;
import org.ec.jap.enumerations.EstadoCabeceraPlanilla;
import org.ec.jap.utilitario.Constantes;
import org.ec.jap.utilitario.Utilitario;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class CabeceraPlanillaBOImpl extends CabeceraPlanillaDAOImpl implements CabeceraPlanillaBO {

	private static final Logger log = Logger.getLogger(CabeceraPlanillaBOImpl.class.getName());
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

	public List<CabeceraPlanilla> findPlanillasByLLaveAndStatus(Llave llave, String estatus) throws Exception {
		HashMap<String, Object> p = new HashMap<>();
		p.put("estado", estatus);
		p.put("idLlave", llave);
		List<CabeceraPlanilla> planillasNoPagadas = findAllByNamedQuery("CabeceraPlanilla.findAllNoPag", p);
		return planillasNoPagadas;
	}

	public List<CabeceraPlanilla> findPlanillasByUserAndStatus(Usuario userAlcan, String status) throws Exception {
		HashMap<String, Object> p = new HashMap<>();
		p = new HashMap<>();
		p.put("estado", status);
		p.put("idUser", userAlcan);
		List<CabeceraPlanilla> planillasNoPagadasAlca = findAllByNamedQuery(
				"CabeceraPlanilla.findAllNoPagAlcantiralado", p);
		return planillasNoPagadasAlca;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public void abrirPeriodoPago(Usuario usuario, Integer idPeriodoPago) throws Exception {
		log.info("----------------abrirPeriodoPago----------------");
		// Usuarios que poseen llaves
		log.info("Obtiene llaves de usuarios ACTIVOS o en estado EDISION");
		List<Llave> llaves = llaveBO.findAllByNamedQuery("Llave.findOfUsuariosActivos");
		log.info(String.format("Cantidad de llaves por usuariso activo %s", llaves.size()));
		log.info("Obtiene los usuarios que poseen servicio de alcantarillado");
		// Usuarios solo con el servicio de alcantarillado
		List<Usuario> usuariosSLLSA = usuarioBO.findAllByNamedQuery("Usuario.findBySinLLave");
		log.info(String.format("Cantidad de usuarios con alcantarillado %s", usuariosSLLSA.size()));
		// Obtenemos el periodo de pago
		PeriodoPago periodoPago = periodoPagoBO.findByPk(idPeriodoPago);
		Parametro parametro = parametroBO.findByPk("NUMFACT");
		Integer numeroFactura = parametro.getValorEntero();
		String path = "0000000000000";
		numeroFactura = iniciarPlanillas(periodoPago, llaves, usuario, numeroFactura, path);
		// Planillas para alcantarillado
		for (Usuario userAlcan : usuariosSLLSA) {
			numeroFactura++;
			CabeceraPlanilla planillaNueva = iniciarCabecera(idPeriodoPago, null, userAlcan, path, numeroFactura);
			save(usuario, planillaNueva);
			cambioEstadoBO.cambiarEstadoSinVerificar(18, usuario, planillaNueva.getIdCabeceraPlanilla(), "");

			// Obtenemos las planillas no pagadas
			List<CabeceraPlanilla> planillasNoPagadas = findPlanillasByUserAndStatus(userAlcan, "NOPAG");
			for (CabeceraPlanilla planillaNoPagada : planillasNoPagadas) {
				List<DetallePlanilla> detallePlanillasNoPagadas = detallePlanillaBO.findDetalles(planillaNoPagada,
						"DetallePlanilla.findByCabecaraNoPag");
				for (DetallePlanilla detallePlanilla : detallePlanillasNoPagadas) {
					DetallePlanilla detallePlanillaNuevo = detallePlanillaBO.traspasarDetalle(planillaNueva,
							detallePlanilla);
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
				Query queryDetalle = em().createQuery(
						"UPDATE DetallePlanilla SET estado='TRAS' where estado='NOPAG' AND idCabeceraPlanilla=:idCabeceraPlanilla");
				queryDetalle.setParameter("idCabeceraPlanilla", planillaNoPagada);
				queryDetalle.executeUpdate();
			}

			// Obtenemos las planillas pagadas incompletas
			List<CabeceraPlanilla> planillasNoPagadasAlca = findPlanillasByUserAndStatus(userAlcan, "INC");
			for (CabeceraPlanilla planillasIncompletas : planillasNoPagadasAlca) {
				List<DetallePlanilla> detalleIncomepletos = detallePlanillaBO.findDetalles(planillasIncompletas,
						"DetallePlanilla.findByCabecaraNoPagInc");
				for (DetallePlanilla detalleIncompleto : detalleIncomepletos) {
					DetallePlanilla detallePlanillaNuevo = detallePlanillaBO.traspasarDetalleInconompleto(planillaNueva,
							detalleIncompleto);
					detallePlanillaNuevo.setOrigen(Constantes.origen_pagado_incompleto);
					detallePlanillaBO.save(usuario, detallePlanillaNuevo);
					planillaNueva.setSubtotal(planillaNueva.getSubtotal() + detallePlanillaNuevo.getValorTotal());
					planillaNueva.setTotal(planillaNueva.getTotal() + detallePlanillaNuevo.getValorTotal());
				}
				cambioEstadoBO.cambiarEstadoSinVerificar(36, usuario, planillasIncompletas.getIdCabeceraPlanilla(), "");
				// Actualizamos el estado de los detalles
				Query queryDetalle = em().createQuery(
						"UPDATE DetallePlanilla SET estado='TRAS' where  idCabeceraPlanilla=:idCabeceraPlanilla AND estado IN ('ING','INC')");
				queryDetalle.setParameter("idCabeceraPlanilla", planillasIncompletas);
				queryDetalle.executeUpdate();
				// Actualizamos el estado de las lecturas a traspazadas
				Query queryLecturas = em().createQuery(
						" UPDATE Lectura SET estado='TRAS' where idLectura IN (SELECT dp.idLectura.idLectura FROM DetallePlanilla dp WHERE dp.idCabeceraPlanilla=:idCabeceraPlanilla)");
				queryLecturas.setParameter("idCabeceraPlanilla", planillasIncompletas);
				queryLecturas.executeUpdate();
			}
			planillaNueva.setValorPendiente(planillaNueva.getTotal());
			update(usuario, planillaNueva);
			Runtime.getRuntime().gc();
		}

		parametro.setValorEntero(numeroFactura);
		parametroBO.update(usuario, parametro);
		Runtime.getRuntime().gc();
	}

	@Override
	public void regenerarPeriodoPago(Usuario usuario, Integer idPeriodoPago) throws Exception {
		log.info("----------------regenerarPeriodoPago----------------");
		PeriodoPago periodoPago = periodoPagoBO.findByPk(idPeriodoPago);
		Parametro parametro = parametroBO.findByPk("NUMFACT");
		Integer numeroFactura = parametro.getValorEntero();
		String path = "0000000000000";
		map = new HashMap<>();
		map.put("idPeriodoPago", periodoPago);
		List<Llave> llaves = llaveBO.findAllByNamedQuery("Llave.findOfUsuariosActivosAndNotPeriodo", map);
		numeroFactura = iniciarPlanillas(periodoPago, llaves, usuario, numeroFactura, path);
		parametro.setValorEntero(numeroFactura);
		parametroBO.update(usuario, parametro);
		Runtime.getRuntime().gc();

	}

	public void regenerarPlanillasPeriodoCerrado(Usuario usuario, Integer idPeriodoPago) throws Exception {
		log.info("----------------regenerarPlanillasPeriodoCerrado----------------");
		PeriodoPago periodoPago = periodoPagoBO.findByPk(idPeriodoPago);
		map = new HashMap<>();
		map.put("idPeriodoPago", periodoPago);
		List<Llave> llaves = llaveBO.findAllByNamedQuery("Llave.findOfUsuariosActivosAndNotFactura", map);
		Parametro parametro = parametroBO.findByPk("NUMFACT");
		Integer numeroFactura = parametro.getValorEntero();
		String path = "0000000000000";
		numeroFactura = iniciarPlanillas(periodoPago, llaves, usuario, numeroFactura, path);
		parametro.setValorEntero(numeroFactura);
		parametroBO.update(usuario, parametro);
		Runtime.getRuntime().gc();
	}

	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public void cerrarPeriodoPago(Usuario usuario, Integer idPeriodoPago) throws Exception {
		log.info("----------------cerrarPeriodoPago----------------");
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
		RegistroEconomico rEconomicoI = registroEconomicoBO.findByNamedQuery("RegistroEconomico.findByType", map);

		// Cantidad de usuarios con el basico
		Integer cantidadBasicoAplicados = 0;
		Integer cantidadAlcantarilladosAplicados = 0;
		RegistroEconomico registroEconomicoBasico = registroEconomicoBO.inicializar(periodoPago, "BASCON",
				" " + periodoPago.getDescripcion(), 0, usuario);

		RegistroEconomico registroEconomicoAlcantarillado = registroEconomicoBO.inicializar(periodoPago, "ALCANCON",
				" " + periodoPago.getDescripcion(), 0, usuario);

		// Variable que indica para que se elimine el registro economico de
		// enasistencia en caso de que no exista ningun registro de ello
		Boolean eliminarRegistroEconomicoInasistencias = true;

		for (Object[] object : objects) {
			CabeceraPlanilla cp = (CabeceraPlanilla) object[0];
			Llave llave = (Llave) object[1];
			Double vAlcan = cp.getIdUsuario().getTarifa() != null
					? cp.getIdUsuario().getTarifa().getValorAlcantarillado()
					: 0.0;
			// Obtenemos la lectura de la llave
			if (llave != null) {
				log.info(String.format("Llave: %s", llave.getNumero()));
				map = new HashMap<>();
				map.put("idLlave", llave.getIdLlave());
				map.put("idPeriodoPago", periodoPago);
				Lectura lec = lecturaBO.findByNamedQuery("Lectura.findByPeridoAndLlave", map);
				if (cp.getIdUsuario() != null && cp.getIdUsuario().getPoseeAlcant() != null
						&& cp.getIdUsuario().getPoseeAlcant() && vAlcan > 0) {
					DetallePlanilla detalleAlcanta = detallePlanillaBO.crearDetalleAlcantarillado(usuario, cp,
							registroEconomicoAlcantarillado, cp.getIdUsuario().getCantAlcant(), vAlcan,
							periodoPago.getDescripcion());
					cp.setSubtotal(Utilitario.redondear(cp.getSubtotal() + detalleAlcanta.getValorTotal()));
					cp.setTotal(Utilitario.redondear(cp.getTotal() + detalleAlcanta.getValorTotal()));
					registroEconomicoAlcantarillado
							.setValor(registroEconomicoBasico.getValor() + detalleAlcanta.getValorTotal());
					detallePlanillaBO.save(usuario, detalleAlcanta);
					cantidadAlcantarilladosAplicados++;
				}
				Boolean debeRegistrarDetalle = true;
				if (lec != null) {
					DetallePlanilla dpls = detallePlanillaBO.buildInitialDetailLectura(cp, lec);
					if (lec.getUsuarioNuevo()) {
						debeRegistrarDetalle = false;
					} else if (lec.getSinLectura()) {
						debeRegistrarDetalle = false;
					} else {
						if (lec.getMetros3() > 0) {
							dpls = detallePlanillaBO.builDetailLectura(periodoPago, lec, dpls);
						}
					}
					if (debeRegistrarDetalle) {
						if (lec.getMetros3() > 0) {
							dpls.setValorTotalOrigen(dpls.getValorTotal());
							dpls.setOrigen(Constantes.origen_mes_Actual);
							detallePlanillaBO.save(usuario, dpls);
							cp.setSubtotal(Utilitario.redondear(cp.getSubtotal() + dpls.getValorTotal()));
							cp.setTotal(Utilitario.redondear(cp.getTotal() + dpls.getValorTotal()));
						}
					}
				}
			} else {
				if (cp.getIdUsuario() != null && cp.getIdUsuario().getPoseeAlcant() != null
						&& cp.getIdUsuario().getPoseeAlcant() && vAlcan > 0) {
					DetallePlanilla alcantarillado = detallePlanillaBO.crearDetalleAlcantarillado(usuario, cp,
							registroEconomicoAlcantarillado, cp.getIdUsuario().getCantAlcant(), vAlcan,
							periodoPago.getDescripcion());
					cp.setSubtotal(Utilitario.redondear(cp.getSubtotal() + alcantarillado.getValorTotal()));
					cp.setTotal(Utilitario.redondear(cp.getTotal() + alcantarillado.getValorTotal()));
					registroEconomicoAlcantarillado
							.setValor(registroEconomicoBasico.getValor() + alcantarillado.getValorTotal());
					detallePlanillaBO.save(usuario, alcantarillado);
					cantidadAlcantarilladosAplicados++;
				}
			}
			Runtime.getRuntime().gc();
		}
		registroEconomicoBasico.setCantidadAplicados(cantidadBasicoAplicados);
		registroEconomicoBO.update(usuario, registroEconomicoBasico);

		registroEconomicoAlcantarillado.setCantidadAplicados(cantidadAlcantarilladosAplicados);
		registroEconomicoBO.update(usuario, registroEconomicoAlcantarillado);
		if (eliminarRegistroEconomicoInasistencias && rEconomicoI != null) {
			cambioEstadoBO.eliminarEntidad(8, rEconomicoI.getIdRegistroEconomico());
		} else if (rEconomicoI != null) {
			// Cambiamos el estado a las actividades y ponemos en aplicado para
			// que no s epueda editar
			for (Actividad actividad : actividads) {
				cambioEstadoBO.cambiarEstadoSinVerificar(43, usuario, actividad.getActividad(), "");
			}
			registroEconomicoBO.update(usuario, rEconomicoI);
			cambioEstadoBO.cambiarEstadoSinVerificar(40, usuario, rEconomicoI.getIdRegistroEconomico(), "");

		}

		// Cambiamos el estado de las cuotas
		map.clear();
		map.put("idPeriodoPago", periodoPago);
		map.put("tipoRegistro", tipoRegistroBO.findByPk("CUO"));
		List<RegistroEconomico> registroEconomicos = registroEconomicoBO
				.findAllByNamedQuery("RegistroEconomico.findByPeriodoAndTipo", map);
		for (RegistroEconomico registroEconomico : registroEconomicos) {
			cambioEstadoBO.cambiarEstadoSinVerificar(40, usuario, registroEconomico.getIdRegistroEconomico(), "");
		}
		Runtime.getRuntime().gc();
	}

	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public void regenerarPeriodoCerrado(Usuario usuario, Integer idPeriodoPago) throws Exception {
		regenerarPlanillasPeriodoCerrado(usuario, idPeriodoPago);
		HashMap<String, Object> map = new HashMap<>();
		map.clear();
		map.put("estado", "ING");
		map.put("idPeriodoPago", idPeriodoPago);
		map.put("esModificable", false);
		// Obtenemos las planillas llaves en las cuales la planilla aun no ha
		// sido pagada
		log.info("Obtenemso las llaves de las lecturas creadas");
		List<Object[]> objects = lecturaBO.findObjects("Lectura.findByPeridoCerrModificable", map);
		log.info(String.format("Cantidad Registros: %s", objects.size()));
		PeriodoPago periodoPago = periodoPagoBO.findByPk(idPeriodoPago);

		map.clear();
		map.put("idPeriodoPago", periodoPago);
		map.put("tipoRegistro", "BASCON");
		RegistroEconomico registroEconomicoBasico = registroEconomicoBO.findByNamedQuery("RegistroEconomico.findByType",
				map);
		// Cantidad de usuarios CON EL BASIC
		Integer cantidadUsuariosConBasico = registroEconomicoBasico.getCantidadAplicados();
		for (Object[] object : objects) {
			CabeceraPlanilla cp = (CabeceraPlanilla) object[0];
			Llave llave = (Llave) object[1];
			if (llave != null) {

				// Obtenemos la lectura
				map = new HashMap<>();
				map.put("idLlave", llave.getIdLlave());
				map.put("idPeriodoPago", periodoPago);
				Lectura lec = lecturaBO.findByNamedQuery("Lectura.findByPeridoAndLlave", map);
				if (lec != null) {
					log.info(String.format("Calcula consumo : %s", llave.getNumero()));
					HashMap<String, Object> pama = new HashMap<>();
					pama.put("idLectura", lec);
					DetallePlanilla dpls = detallePlanillaBO.findByNamedQuery("DetallePlanilla.findByLectura", pama);
					if (dpls == null)
						dpls = detallePlanillaBO.buildInitialDetailLectura(cp, lec);
					if (lec.getMetros3() > 0)
						dpls = detallePlanillaBO.builDetailLectura(periodoPago, lec, dpls);
					if (lec.getMetros3() > 0)
						if (dpls.getIdDetallePlanilla() == null) {
							dpls.setValorTotalOrigen(dpls.getValorTotal());
							dpls.setOrigen(Constantes.origen_mes_Actual);
							detallePlanillaBO.save(usuario, dpls);
						} else
							detallePlanillaBO.update(usuario, dpls);
					map.clear();
					map.put("idCabeceraPlanilla", cp);
					List<DetallePlanilla> detallePlanillas = detallePlanillaBO
							.findAllByNamedQuery("DetallePlanilla.findByCabecara", map);
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
			Runtime.getRuntime().gc();
		}
		registroEconomicoBasico.setCantidadAplicados(cantidadUsuariosConBasico);
		registroEconomicoBO.update(usuario, registroEconomicoBasico);
		Runtime.getRuntime().gc();
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
		log.info(String.format("Cantidad de Planillas no Pagadas:%s", planillasPagadasConAbono.size()));
		int contador = 0;
		for (CabeceraPlanilla planillaActual : planillasPagadasConAbono) {
			CabeceraPlanilla planillaAnterior = getAbonoMesAnterior(planillaActual.getIdLlave(), planillaActual);
			if (planillaAnterior != null && planillaAnterior.getAbonoUsd() != 0.0) {
				Double abonoUsd = planillaAnterior.getAbonoUsd();
				planillaAnterior.setAbonoUsd(0.0);
				planillaActual.setAbonoUsd(abonoUsd);
				update(usuario, planillaActual);
				update(usuario, planillaAnterior);
				contador++;
			}
		}
		log.info(String.format("Planillas con bono :%s", contador));
		// Actualizamos de estado a las planillas que no fueron pagadas
		p.put("estado", "ING");
		p.put("estado2", "ING");
		p.put("idPeriodoPago", idPeriodoPago);
		List<CabeceraPlanilla> planillasNoPagadas = findAllByNamedQuery("CabeceraPlanilla.findNoPag", p);
		log.info(String.format("Cantidad de Planillas no pagadas :%s", planillasNoPagadas.size()));
		for (CabeceraPlanilla cp : planillasNoPagadas) {
			cambioEstadoBO.cambiarEstadoSinVerificar(20, usuario, cp.getIdCabeceraPlanilla(), "");
			Query queryDetalle = em().createQuery(
					" UPDATE DetallePlanilla SET estado='NOPAG' where idCabeceraPlanilla=:idCabeceraPlanilla");
			queryDetalle.setParameter("idCabeceraPlanilla", cp);
			Query queryLecturas = em().createQuery(
					" UPDATE Lectura SET estado='NOPAG' where idLectura IN (SELECT dp.idLectura.idLectura FROM DetallePlanilla dp WHERE dp.idCabeceraPlanilla=:idCabeceraPlanilla)");
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
		log.info(String.format("Cantidad de Planillas pagadas con abono :%s", planillasPagadas.size()));
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
		registroEconomicoPorDevolverAnterior = registroEconomicoBO.findByNamedQuery("RegistroEconomico.findByType",
				map);

		for (CabeceraPlanilla planillaActual : planillasPagadas) {
			valorDevuelto += Utilitario.redondear(planillaActual.getValorPagadoAbono());
			if (registroEconomicoPorDevolverAnterior != null) {
				valorPagadoAbonoIngresado += Utilitario.redondear(
						registroEconomicoPorDevolverAnterior.getValor() - planillaActual.getValorPagadoAbono());
			}
		}
		log.info(String.format("Valor Recaudado por abono: %s", valorPagadoAbonoIngresado));
		if (registroEconomicoPorDevolverAnterior != null) {
			valorRestantePorDevolver = Utilitario
					.redondear(registroEconomicoPorDevolverAnterior.getValor() - valorPagadoAbonoIngresado);
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
			totalIngresoPeriodo = totalIngresoPeriodo
					+ (cabeceraPlanilla.getValorPagado() != null ? cabeceraPlanilla.getValorPagado() : 0.0);
			totalPorDevolver = totalPorDevolver
					+ (cabeceraPlanilla.getAbonoUsd() != null ? cabeceraPlanilla.getAbonoUsd() : 0.0);
		}

		// AL Valor Total de este mes actual tambien sumamos el valor pendiente
		// por devolver del mes pasado
		if (valorRestantePorDevolver > 0.0)
			totalPorDevolver += valorRestantePorDevolver;
		log.info(String.format("Valor Pendiente Abono Devolución: %s", totalPorDevolver));
		// Registramos en caja por parte de servicios el valor pagado del
		// periodo
		cuentaBO.registrarAsiento(Constantes.numeroCaja, Constantes.cuentaCaja, Constantes.numeroServicio,
				Constantes.cuentaServicio, Utilitario.redondear(totalIngresoPeriodo), usuario);

		// Registramos en las cuentas por pagar de pero que estan en caja
		cuentaBO.registrarAsiento(Constantes.numeroCuentasPorPagar, Constantes.cuentaCuentasPorPagar,
				Constantes.numeroCaja, Constantes.cuentaCaja, Utilitario.redondear(totalPorDevolver), usuario);

		// Registramos en caja el valor total de los gastos

		map.clear();
		map.put("idPeriodoPago", idPeriodoPago);
		List<Gasto> gastos = gastoBO.findAllByNamedQuery("Gasto.findAllByUser", map);
		Double totalGastos = 0.0;
		for (Gasto gasto : gastos) {
			totalGastos = totalGastos + (gasto.getValor() != null ? gasto.getValor() : 0.0);
		}

		log.info(String.format("Valor Total Gastos: %s", totalPorDevolver));
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

		RegistroEconomico registroEconomicoPorDevolver = registroEconomicoBO
				.findByNamedQuery("RegistroEconomico.findByType", map);
		if (registroEconomicoPorDevolver != null) {
			if (totalPorDevolver.equals(0.0)) {
				cambioEstadoBO.eliminarEntidad(8, registroEconomicoPorDevolver.getIdRegistroEconomico());
			} else {
				registroEconomicoPorDevolver.setValor(totalPorDevolver);
				registroEconomicoBO.update(usuario, registroEconomicoPorDevolver);
				cambioEstadoBO.cambiarEstadoSinVerificar(40, usuario,
						registroEconomicoPorDevolver.getIdRegistroEconomico(), "");
			}
		}

		// Registramos los gastos en el libro diario
		cuentaBO.registrarAsiento(Constantes.numeroGasto, Constantes.cuentaGasto, Constantes.numeroCaja,
				Constantes.cuentaCaja, Utilitario.redondear(totalGastos), usuario);

		// Registramos en caja el valor devuelto con pago mediante abono en los
		// pagos por motivo de
		// abonos
		cuentaBO.registrarAsiento(Constantes.numeroCaja, Constantes.cuentaCaja, Constantes.numeroCuentasPorPagar,
				Constantes.cuentaCuentasPorPagar, Utilitario.redondear(valorDevuelto), usuario);
		Runtime.getRuntime().gc();
	}

	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	@Override
	public void pagarPlanilla(Usuario usuario, Integer idCabeceraPlanilla) throws Exception {
		CabeceraPlanilla cabeceraPlanilla = findByPk(idCabeceraPlanilla);
		cabeceraPlanilla.setFechaPago(Calendar.getInstance().getTime());
		update(usuario, cabeceraPlanilla);
		Query queryDetalle = em().createQuery(
				" UPDATE DetallePlanilla SET estado='PAG',valorPendiente=0.0,valorPagado=valorTotal where idCabeceraPlanilla.idCabeceraPlanilla=:idCabeceraPlanilla");
		queryDetalle.setParameter("idCabeceraPlanilla", idCabeceraPlanilla);
		Query queryLecturas = em().createQuery(
				" UPDATE Lectura SET estado='PAG' where idLectura IN (SELECT dp.idLectura.idLectura FROM DetallePlanilla dp WHERE dp.idCabeceraPlanilla.idCabeceraPlanilla=:idCabeceraPlanilla)");
		queryLecturas.setParameter("idCabeceraPlanilla", idCabeceraPlanilla);
		queryDetalle.executeUpdate();
		queryLecturas.executeUpdate();
		Runtime.getRuntime().gc();
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
		RegistroEconomico registroEconomicoPorDevolver = registroEconomicoBO
				.findByNamedQuery("RegistroEconomico.findByType", map);
		Double valorAbonoAnterior = Utilitario.redondear(anterior.getAbonoUsd());

		if (registroEconomicoPorDevolver != null) {
			registroEconomicoPorDevolver
					.setValor(Utilitario.redondear(registroEconomicoPorDevolver.getValor() + valorAbonoAnterior));
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
		Runtime.getRuntime().gc();

	}

	@Override
	public void guardarPlanilla(Usuario usuario, Double valorAPagar, CabeceraPlanilla cp, CabeceraPlanilla anterior,
			Double valorAbono, Integer idCambioEstado, Object idDocumento) throws Exception {
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
		RegistroEconomico registroEconomicoPorDevolver = registroEconomicoBO
				.findByNamedQuery("RegistroEconomico.findByType", map);
		if (registroEconomicoPorDevolver != null) {
			if (registroEconomicoPorDevolver.getValor() > 0)
				registroEconomicoPorDevolver.setValor(
						Utilitario.redondear(registroEconomicoPorDevolver.getValor() - anterior.getAbonoUsd()));
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
			List<DetallePlanilla> detallePlanillas = detallePlanillaBO
					.findAllByNamedQuery("DetallePlanilla.findByCabecaraSinPagar", map);

			for (DetallePlanilla detallePlanilla : detallePlanillas) {
				if (valorAPagar != 0.0 && !"PAG".equalsIgnoreCase(detallePlanilla.getEstado())
						&& !"INC".equalsIgnoreCase(detallePlanilla.getEstado())) {
					if (valorAPagar >= Utilitario.redondear(detallePlanilla.getValorTotal())) {
						detallePlanilla.setEstado("PAG");
						detallePlanilla.setValorPagado(Utilitario.redondear(detallePlanilla.getValorTotal()));
						valorAPagar = Utilitario
								.redondear(valorAPagar - Utilitario.redondear(detallePlanilla.getValorTotal()));
					} else {
						detallePlanilla.setEstado("INC");
						detallePlanilla.setValorPagado(valorAPagar);
						valorAPagar = 0.0;
					}
					detallePlanilla.setValorPendiente(
							Utilitario.redondear(detallePlanilla.getValorTotal() - detallePlanilla.getValorPagado()));
					detallePlanillaBO.update(usuario, detallePlanilla);
				}
			}
			cambioEstadoBO.cambiarEstadoMandatory(35, usuario, idDocumento);
		}
		Runtime.getRuntime().gc();
	}

	@Override
	public void recalcularPlanilla(Usuario usuario, CabeceraPlanilla cabeceraPlanilla, Lectura lectura)
			throws Exception {

		if ("ING".equalsIgnoreCase(cabeceraPlanilla.getEstado())) {
			lectura = lecturaBO.recalcularLectura(usuario, lectura);
			CabeceraPlanilla cp = cabeceraPlanilla;
			lecturaBO.update(usuario, lectura);
			HashMap<String, Object> pama = new HashMap<>();
			pama.put("idLectura", lectura);
			pama.put("idCabeceraPlanilla", cabeceraPlanilla);
			DetallePlanilla detallePlanilla = detallePlanillaBO
					.findByNamedQuery("DetallePlanilla.findByLecturaAndCabcera", pama);
			// Si es mayor a cero realizamos el calculo caso contrario
			// cobramos el basico
			detallePlanilla.setValorUnidad(Utilitario.redondear(lectura.getValorMetro3()));
			detallePlanilla.setValorTotal(
					Utilitario.redondear((lectura.getMetros3() != null && lectura.getValorMetro3() != null
							? (lectura.getMetros3() * lectura.getValorMetro3())
							: 0.0)
							+ (lectura.getMetros3Exceso() != null && lectura.getValorMetro3Exceso() != null
									&& lectura.getMetros3Exceso() > 0 && lectura.getValorMetro3Exceso() > 0
											? (lectura.getMetros3Exceso() * lectura.getValorMetro3Exceso())
											: 0.0)));
			detallePlanilla.setValorPendiente(detallePlanilla.getValorTotal());
			detallePlanilla.setValorPagado(0.0);
			detallePlanilla.setDescripcion(
					Utilitario.redondear(lectura.getMetros3()) + " m3 " + lectura.getIdPeriodoPago().getDescripcion());
			detallePlanillaBO.update(usuario, detallePlanilla);
			// Recalculamos el valos de la planilla
			map.clear();
			map.put("idCabeceraPlanilla", cp);
			List<DetallePlanilla> detallePlanillas = detallePlanillaBO
					.findAllByNamedQuery("DetallePlanilla.findByCabecara", map);
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

		} else {
			lectura = lecturaBO.recalcularLectura(usuario, lectura);
			lecturaBO.update(usuario, lectura);
			HashMap<String, Object> pama = new HashMap<>();
			pama.put("idLectura", lectura);
			pama.put("idCabeceraPlanilla", cabeceraPlanilla);
			DetallePlanilla detallePlanilla = detallePlanillaBO
					.findByNamedQuery("DetallePlanilla.findByLecturaAndCabcera", pama);
			// Si es mayor a cero realizamos el calculo caso contrario
			// cobramos el basico
			if (lectura.getMetros3() > 0) {
				detallePlanilla.setDescripcion(Utilitario.redondear(lectura.getMetros3()) + " m3 "
						+ lectura.getIdPeriodoPago().getDescripcion());
			}
			if (lectura.getMetros3() > 0)
				detallePlanillaBO.update(usuario, detallePlanilla);
		}
		Runtime.getRuntime().gc();
	}

	/**
	 * Inicia con valores por defectos una planila
	 * 
	 * @param idPeriodoPago
	 * @param idLlave
	 * @param responsable
	 * @param path
	 * @param numeroFactura
	 * @return
	 * @throws Exception
	 */
	protected CabeceraPlanilla iniciarCabecera(Integer idPeriodoPago, Llave idLlave, Usuario responsable, String path,
			Integer numeroFactura) throws Exception {
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
		if (responsable != null)
			planillaNueva.setIdUsuario(responsable);
		if (idLlave != null)
			planillaNueva.setIdLlave(idLlave);
		planillaNueva.setObservacion(
				path.substring(0, path.length() - numeroFactura.toString().length()) + numeroFactura.toString());
		planillaNueva.setIdPeriodoPago(periodoPagoBO.findByPk(idPeriodoPago));
		log.info(String.format("Número de factura Nueva: %s", planillaNueva.getObservacion()));
		return planillaNueva;
	}

	/**
	 * Verifica si el usuario pago o no el mes anteriro en caso positivo le genera
	 * una multa y adicional trasnfiere los valores a la planilla del mes actual
	 * 
	 * @param llave
	 * @param pn
	 * @param usuario
	 * @param multaAtrazoMes
	 * @return Si existe una multa no null , en caso de no existir nada retorna null
	 * @throws Exception
	 */
	protected CabeceraPlanilla verificarMultNoPago(Llave llave, CabeceraPlanilla pn, Usuario usuario,
			RegistroEconomico multaAtrazoMes) throws Exception {
		Integer contador = 0;
		log.info("Obtiene las planillas no pagadas del mes anterior");
		List<CabeceraPlanilla> pnps = findPlanillasByLLaveAndStatus(llave, "NOPAG");
		log.info(String.format("Cantidad de planillas no pagadas %s", pnps.size()));
		for (CabeceraPlanilla pnp : pnps) {
			List<DetallePlanilla> dpnps = detallePlanillaBO.findDetalles(pnp, "DetallePlanilla.findByCabecaraNoPag");
			for (DetallePlanilla dpnp : dpnps) {
				DetallePlanilla dpnpn = detallePlanillaBO.traspasarDetalle(pn, dpnp);
				if (Constantes.origen_mes_Actual.equals(dpnp.getOrigen())) {
					dpnpn.setOrigen(Constantes.origen_no_pagado);
				} else {
					dpnpn.setOrigen(dpnp.getOrigen());
				}
				detallePlanillaBO.save(usuario, dpnpn);
				pn.setSubtotal(pn.getSubtotal() + dpnpn.getValorTotal());
				pn.setTotal(pn.getTotal() + dpnpn.getValorTotal());
			}
			Double multa = detallePlanillaBO.crearMulta(pnp, pn, multaAtrazoMes, llave, usuario);
			if (llave.getIdUsuario().getTieneDescuento())
				multa = multa * 0.5;
			if (multa > 0.0) {
				multaAtrazoMes.setValor(multaAtrazoMes.getValor() + multa);
				pn.setSubtotal(pn.getSubtotal() + multa);
				pn.setTotal(pn.getTotal() + multa);
			}
			cambioEstadoBO.cambiarEstadoSinVerificar(22, usuario, pnp.getIdCabeceraPlanilla(), "");
			Query queryDetalle = em().createQuery(
					"UPDATE DetallePlanilla SET estado='TRAS' where estado='NOPAG' AND idCabeceraPlanilla=:idCabeceraPlanilla");
			queryDetalle.setParameter("idCabeceraPlanilla", pnp);
			queryDetalle.executeUpdate();
			contador++;
		}
		if (contador > 0)
			return pn;
		else
			return null;

	}

	/**
	 * Verifica si el usuario posee planillas que ha pagado con un valor incompleto,
	 * en caso de existir alguna las traspasa apra la planilla nueva
	 * 
	 * @param llave
	 * @param pn
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	protected CabeceraPlanilla verificarPlanillasIncompletas(Llave llave, CabeceraPlanilla pn, Usuario usuario)
			throws Exception {
		log.info("Obtiene las planillas incompletas del mes anterior");
		List<CabeceraPlanilla> pnps = findPlanillasByLLaveAndStatus(llave, "INC");
		log.info(String.format("Cantidad de planillas incompletas %s", pnps.size()));
		for (CabeceraPlanilla planillasIncompletas : pnps) {
			List<DetallePlanilla> detalleIncomepletos = detallePlanillaBO.findDetalles(planillasIncompletas,
					"DetallePlanilla.findByCabecaraNoPagInc");
			for (DetallePlanilla detalleIncompleto : detalleIncomepletos) {
				DetallePlanilla detallePlanillaNuevo = detallePlanillaBO.traspasarDetalleInconompleto(pn,
						detalleIncompleto);
				detallePlanillaNuevo.setOrigen(Constantes.origen_pagado_incompleto);
				detallePlanillaBO.save(usuario, detallePlanillaNuevo);
				pn.setSubtotal(pn.getSubtotal() + detallePlanillaNuevo.getValorTotal());
				pn.setTotal(pn.getTotal() + detallePlanillaNuevo.getValorTotal());
			}
			cambioEstadoBO.cambiarEstadoSinVerificar(36, usuario, planillasIncompletas.getIdCabeceraPlanilla(), "");
			// Actualizamos el estado de los detalles
			Query queryDetalle = em().createQuery(
					"UPDATE DetallePlanilla SET estado='TRAS' where  idCabeceraPlanilla=:idCabeceraPlanilla AND estado IN ('ING','INC')");
			queryDetalle.setParameter("idCabeceraPlanilla", planillasIncompletas);
			queryDetalle.executeUpdate();
			// Actualizamos el estado de las lecturas a traspazadas
			Query queryLecturas = em().createQuery(
					" UPDATE Lectura SET estado='TRAS' where idLectura IN (SELECT dp.idLectura.idLectura FROM DetallePlanilla dp WHERE dp.idCabeceraPlanilla=:idCabeceraPlanilla)");
			queryLecturas.setParameter("idCabeceraPlanilla", planillasIncompletas);
			queryLecturas.executeUpdate();
		}
		return pn;
	}

	/**
	 * Inicialzia las planillas de cada usuario
	 * 
	 * @param periodoPago
	 * @param llaves
	 * @param usuario
	 * @param numeroFactura
	 * @param path
	 * @return
	 * @throws Exception
	 */
	protected Integer iniciarPlanillas(PeriodoPago periodoPago, List<Llave> llaves, Usuario usuario,
			Integer numeroFactura, String path) throws Exception {
		if (llaves != null && llaves.size() > 0) {
			Boolean existenMultaAtrazos = false;
			Integer cantidadMultaAtrazosAplicados = 0;
			RegistroEconomico multaAtrazoMes = registroEconomicoBO.getByPeriodo(periodoPago, "CUO");
			log.info("Consulta el registro e. para las multas");
			if (multaAtrazoMes != null) {
				log.info("Si existe");
				cantidadMultaAtrazosAplicados = multaAtrazoMes.getCantidadAplicados();
				existenMultaAtrazos = true;
			} else {
				log.info("No existe");
				multaAtrazoMes = registroEconomicoBO.iniciarMulta(periodoPago, usuario);
			}

			for (Llave llave : llaves) {
				log.info(String.format("Número Lllave: %s", llave.getNumero()));
				numeroFactura++;
				CabeceraPlanilla pn = iniciarCabecera(periodoPago.getIdPeriodoPago(), llave, llave.getIdUsuario(), path,
						numeroFactura);
				save(usuario, pn);
				cambioEstadoBO.cambiarEstadoSinVerificar(18, usuario, pn.getIdCabeceraPlanilla(), "");
				CabeceraPlanilla cpnpg = verificarMultNoPago(llave, pn, usuario, multaAtrazoMes);
				if (cpnpg != null) {
					pn = cpnpg;
					cantidadMultaAtrazosAplicados++;
					existenMultaAtrazos = true;
				}
				pn = verificarPlanillasIncompletas(llave, pn, usuario);
				lecturaBO.iniciarLecturaCero(periodoPago, llave, usuario);
				pn.setValorPendiente(pn.getTotal());
				update(usuario, pn);
				Runtime.getRuntime().gc();
			}
			if (!existenMultaAtrazos)
				cambioEstadoBO.eliminarEntidad(8, multaAtrazoMes.getIdRegistroEconomico());
			else {
				multaAtrazoMes.setCantidadAplicados(cantidadMultaAtrazosAplicados);
				registroEconomicoBO.update(usuario, multaAtrazoMes);
			}
			Runtime.getRuntime().gc();
		}
		return numeroFactura;
	}

}
