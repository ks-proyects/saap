package org.ec.jap.bo.saap.impl;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

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
import org.ec.jap.bo.saap.ParametroBO;
import org.ec.jap.bo.saap.PeriodoPagoBO;
import org.ec.jap.bo.saap.RangoConsumoBO;
import org.ec.jap.bo.saap.RegistroEconomicoBO;
import org.ec.jap.bo.saap.ServicioBO;
import org.ec.jap.bo.saap.TarifaBO;
import org.ec.jap.bo.saap.TipoRegistroBO;
import org.ec.jap.bo.saap.UsuarioBO;
import org.ec.jap.bo.sistema.CambioEstadoBO;
import org.ec.jap.bo.sistema.EmailBO;
import org.ec.jap.dao.saap.impl.CabeceraPlanillaDAOImpl;
import org.ec.jap.entiti.saap.CabeceraPlanilla;
import org.ec.jap.entiti.saap.DetallePlanilla;
import org.ec.jap.entiti.saap.Gasto;
import org.ec.jap.entiti.saap.Lectura;
import org.ec.jap.entiti.saap.Parametro;
import org.ec.jap.entiti.saap.PeriodoPago;
import org.ec.jap.entiti.saap.RegistroEconomico;
import org.ec.jap.entiti.saap.Servicio;
import org.ec.jap.entiti.saap.Usuario;
import org.ec.jap.enumerations.EstadoCabeceraPlanilla;
import org.ec.jap.enumerations.TipoServicioEnum;
import org.ec.jap.utilitario.Constantes;
import org.ec.jap.utilitario.Utilitario;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class CabeceraPlanillaBOImpl extends CabeceraPlanillaDAOImpl implements CabeceraPlanillaBO {

	public Logger log = Logger.getLogger(CabeceraPlanillaDAOImpl.class.getName());

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
	protected RegistroEconomicoBO reBO;
	@EJB
	protected CambioEstadoBO cambioEstadoBO;
	@EJB
	protected RangoConsumoBO rangoConsumoBO;
	@EJB
	protected DetallePlanillaBO detallePlanillaBO;
	@EJB
	protected ServicioBO llaveBO;
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

	@EJB
	protected TarifaBO tarifaBO;
	@EJB
	EmailBO emailBo;

	public CabeceraPlanillaBOImpl() {
	}

	public List<CabeceraPlanilla> findPlanillasByServicioAndStatus(Servicio llave, Usuario user, String estatus)
			throws Exception {
		HashMap<String, Object> p = new HashMap<>();
		p.put("estado", estatus);
		p.put("idServicio", llave);
		p.put("idUsuario", user);
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
		log.info("Obtiene todos los usuarios que poseen algun servicio y se encuentren en ACTIVOS o en estado EDISION");
		List<Usuario> uActivos = usuarioBO.findAllByNamedQuery("Usuario.findAllActivosHadSer");
		log.info(String.format("Cantidad de llaves por usuariso activo %s", uActivos.size()));
		// Obtenemos el periodo de pago
		PeriodoPago periodoPago = periodoPagoBO.findByIdCustom(idPeriodoPago);
		Parametro parametro = parametroBO.findByPk("NUMFACT");
		Integer numeroFactura = parametro.getValorEntero();
		String path = "0000000000000";
		numeroFactura = iniciarPlanillas(periodoPago, uActivos, usuario, numeroFactura, path);
		parametro.setValorEntero(numeroFactura);
		parametroBO.update(usuario, parametro);
		Runtime.getRuntime().gc();
	}

	@Override
	public void regenerarPeriodoPagoAbierto(Usuario usuario, Integer idPeriodoPago) throws Exception {
		log.info("----------------regenerarPeriodoPago----------------");
		PeriodoPago periodoPago = periodoPagoBO.findByIdCustom(idPeriodoPago);
		Parametro parametro = parametroBO.findByPk("NUMFACT");
		Integer numeroFactura = parametro.getValorEntero();
		String path = "0000000000000";
		map = new HashMap<>();
		map.put("idPeriodoPago", periodoPago);
		List<Usuario> llaves = usuarioBO.findAllByNamedQuery("Usuario.findServiceNotHadDet", map);
		numeroFactura = iniciarPlanillas(periodoPago, llaves, usuario, numeroFactura, path);
		parametro.setValorEntero(numeroFactura);
		parametroBO.update(usuario, parametro);
		Runtime.getRuntime().gc();

	}

	public List<Usuario> regenerarPlanillasPeriodoCerrado(Usuario usuario, Integer idPeriodoPago) throws Exception {
		log.info("----------------regenerarPlanillasPeriodoCerrado----------------");
		PeriodoPago periodoPago = periodoPagoBO.findByIdCustom(idPeriodoPago);
		map = new HashMap<>();
		map.put("idPeriodoPago", periodoPago);
		List<Usuario> usuarios = usuarioBO.findAllByNamedQuery("Usuario.findServiceNotHadDet", map);
		if (usuarios != null && usuarios.size() > 0) {
			Parametro parametro = parametroBO.findByPk("NUMFACT");
			Integer numeroFactura = parametro.getValorEntero();
			String path = "0000000000000";
			numeroFactura = iniciarPlanillas(periodoPago, usuarios, usuario, numeroFactura, path);
			parametro.setValorEntero(numeroFactura);
			parametroBO.update(usuario, parametro);
			Runtime.getRuntime().gc();
			System.gc();
			return usuarios;
		}
		log.info("----------------regenerarPlanillasPeriodoCerrado Fin--------------");
		return usuarios;

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
	protected Integer iniciarPlanillas(PeriodoPago periodoPago, List<Usuario> usuarios, Usuario userSystem,
			Integer numeroFactura, String path) throws Exception {
		map = new HashMap<String, Object>();
		if (usuarios != null && usuarios.size() > 0) {
			Boolean existenMultaAtrazos = false;
			Integer cantidadMultaAtrazosAplicados = 0;
			RegistroEconomico multaAtrazoMes = reBO.getByPeriodo(periodoPago, "MULAGU");
			if (multaAtrazoMes != null) {
				cantidadMultaAtrazosAplicados = multaAtrazoMes.getCantidadAplicados();
				existenMultaAtrazos = true;
			} else {
				multaAtrazoMes = reBO.iniciarMulta(periodoPago, userSystem);
			}
			for (Usuario user : usuarios) {
				map.clear();
				map.put("idUsuario", user);
				map.put("idPeriodoPago", periodoPago);
				List<Servicio> servicios = llaveBO.findAllByNamedQuery("Servicio.findByUserActivo", map);
				// Verificamos si ya posee factura, es para el caso que se agregue un servico
				// adicional luego de haber creado la planilla
				List<CabeceraPlanilla> planillaActual = findAllByNamedQuery("CabeceraPlanilla.findByUserPeriodo", map);
				if (servicios.size() > 0) {
					CabeceraPlanilla pn = null;
					Servicio llave = null;
					String nombre = user.getApellidos() + " " + user.getNombres();
					String format = "........................";
					nombre = nombre.length() > format.length() ? nombre.substring(0, format.length())
							: nombre + format.substring(nombre.length(), format.length());
					if (servicios != null && servicios.size() > 0)
						for (Servicio servicio : servicios)
							if (TipoServicioEnum.AGUA_POTABLE.equals(servicio.getTipoServicio())) {
								llave = servicio;
								break;
							}
					if (planillaActual.isEmpty()) {
						numeroFactura++;
						pn = iniciarCabecera(periodoPago.getIdPeriodoPago(), user, path, numeroFactura);
						save(userSystem, pn);
						cambioEstadoBO.cambiarEstadoSinVerificar(18, userSystem, pn.getIdCabeceraPlanilla(), "");
						CabeceraPlanilla cpnpg = verificarMultNoPago(llave, user, pn, userSystem, multaAtrazoMes);
						if (cpnpg != null) {
							pn = cpnpg;
							cantidadMultaAtrazosAplicados++;
							existenMultaAtrazos = true;
						}
						pn = verificarPlanillasIncompletas(llave, user, pn, userSystem);
					} else {
						pn = planillaActual.get(0);
					}

					log.info(String.format("%1$s ===> Medidor: %2$s Factura: %3$s", nombre,
							llave != null ? llave.getNumero() : "NA", numeroFactura));
					if (llave != null)
						lecturaBO.iniciarLecturaCero(periodoPago, llave, userSystem);
					pn.setValorPendiente(pn.getTotal());
					update(userSystem, pn);
					Runtime.getRuntime().gc();
				}
			}
			if (!existenMultaAtrazos)
				cambioEstadoBO.eliminarEntidad(8, multaAtrazoMes.getIdRegistroEconomico());
			else {
				multaAtrazoMes.setCantidadAplicados(cantidadMultaAtrazosAplicados);
				reBO.update(userSystem, multaAtrazoMes);
			}
			Runtime.getRuntime().gc();
		}
		return numeroFactura;
	}

	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public void cerrarPeriodoPago(Usuario systemUser, Integer idPeriodoPago) throws Exception {
		log.info("----------------cerrarPeriodoPago----------------");
		HashMap<String, Object> map = new HashMap<>();
		// Verificamos que todas las lecturas esten correctamente ingresadas
		// map.put("idPeriodoPago", idPeriodoPago);
		// map.put("usuarioNuevo", true);
		// map.put("sinLectura", true);
		// List<Lectura> lecturas =
		// lecturaBO.findAllByNamedQuery("Lectura.findErroneoByPeriodo", map);
		// if (!lecturas.isEmpty())
		// throw new Exception("Existen lecturas mal ingresadas, revice las lecturas.");
		// Obtenemos el periodo de pago
		PeriodoPago periodoPago = periodoPagoBO.findByIdCustom(idPeriodoPago);
		// Obtenemso las lecturas del mes, incluido la llave y las facturas
		map.clear();
		map.put("estado", "ING");
		map.put("idPeriodoPago", idPeriodoPago);
		List<Object[]> objects = lecturaBO.findObjects("Lectura.findByPerido", map);

		Integer cantidadAlcantarilladosAplicados = 0;
		RegistroEconomico registroEconomicoAlcantarillado = reBO.inicializar(periodoPago, "ALCANCON",
				"Alcantarillado " + periodoPago.getDescripcion(), 0, systemUser);
		for (Object[] object : objects) {
			CabeceraPlanilla cp = (CabeceraPlanilla) object[0];
			Servicio servicio = (Servicio) object[1];
			Double vAlcan = servicio.getIdTarifa() != null ? servicio.getIdTarifa().getAlcantarillado() : 0.0;

			// Obtenemos la lectura de la llave
			if (servicio != null && TipoServicioEnum.AGUA_POTABLE.equals(servicio.getTipoServicio())) {
				map = new HashMap<>();
				map.put("idServicio", servicio.getIdServicio());
				map.put("idPeriodoPago", periodoPago);
				Lectura lec = lecturaBO.findByNamedQuery("Lectura.findByPeridoAndLlaveInicio", map);
				Boolean debeRegistrarDetalle = true;
				if (lec != null) {
					DetallePlanilla dpls = detallePlanillaBO.buildInitialDetailLectura(cp, lec);
					if (lec.getUsuarioNuevo()) {
						debeRegistrarDetalle = false;
					} else if (lec.getSinLectura()) {
						if (lec.getMetros3() > 0) {
							dpls = detallePlanillaBO.builDetailLectura(periodoPago, lec, dpls);
						}
					} else {
						dpls = detallePlanillaBO.builDetailLectura(periodoPago, lec, dpls);
					}
					if (debeRegistrarDetalle) {
						if (lec.getMetros3() > 0 || lec.getValorMetro3() > 0) {
							dpls.setValorTotalOrigen(dpls.getValorTotal());
							dpls.setOrigen(Constantes.origen_mes_Actual);
							detallePlanillaBO.save(systemUser, dpls);
							cp.setSubtotal(Utilitario.redondear(cp.getSubtotal() + dpls.getValorTotal()));
							cp.setTotal(Utilitario.redondear(cp.getTotal() + dpls.getValorTotal()));
						}
					}
				}
			} else if (servicio != null && TipoServicioEnum.ALCANTARILLADO.equals(servicio.getTipoServicio())) {
				DetallePlanilla alcantarillado = detallePlanillaBO.crearDetalleAlcantarillado(systemUser, cp,
						registroEconomicoAlcantarillado, 1, vAlcan, periodoPago.getDescripcion(), servicio);
				cp.setSubtotal(Utilitario.redondear(cp.getSubtotal() + alcantarillado.getValorTotal()));
				cp.setTotal(Utilitario.redondear(cp.getTotal() + alcantarillado.getValorTotal()));
				registroEconomicoAlcantarillado
						.setValor(registroEconomicoAlcantarillado.getValor() + alcantarillado.getValorTotal());
				detallePlanillaBO.save(systemUser, alcantarillado);
				cantidadAlcantarilladosAplicados++;
			}
			update(systemUser, cp);
			enviarEmail(cp, periodoPago);
			Runtime.getRuntime().gc();
		}
		if (cantidadAlcantarilladosAplicados == 0)
			cambioEstadoBO.eliminarEntidad(8, registroEconomicoAlcantarillado.getIdRegistroEconomico());
		else {
			registroEconomicoAlcantarillado.setCantidadAplicados(cantidadAlcantarilladosAplicados);
			reBO.update(systemUser, registroEconomicoAlcantarillado);
		}
		// Cambiamos el estado de las cuotas
		map.clear();
		map.put("idPeriodoPago", periodoPago);
		map.put("tipoRegistro", tipoRegistroBO.findByPk("CUO"));
		List<RegistroEconomico> registroEconomicos = reBO.findAllByNamedQuery("RegistroEconomico.findByPeriodoAndTipo",
				map);
		for (RegistroEconomico registroEconomico : registroEconomicos) {
			cambioEstadoBO.cambiarEstadoSinVerificar(40, systemUser, registroEconomico.getIdRegistroEconomico(), "");
		}
		Runtime.getRuntime().gc();
		log.info("----------------cerrarPeriodoPago Fin----------------");
	}

	void enviarEmail(CabeceraPlanilla cp, PeriodoPago periodoPago) {
		try {
			map.clear();
			map.put("idCabeceraPlanilla", cp);
			List<DetallePlanilla> del = detallePlanillaBO.findAllByNamedQuery("DetallePlanilla.findByCabecaraSinPagar",
					map);
			emailBo.enviarEmailUsuario(cp, del, periodoPago);
		} catch (Exception e) {
			log.info(e.getMessage());
		}
	}

	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public void regenerarPeriodoCerrado(Usuario usuario, Integer idPeriodoPago) throws Exception {
		PeriodoPago periodoPago = periodoPagoBO.findByIdCustom(idPeriodoPago);
		regenerarPlanillasPeriodoCerrado(usuario, idPeriodoPago);

		HashMap<String, Object> map = new HashMap<>();
		map.clear();
		map.put("estado", "ING");
		map.put("idPeriodoPago", idPeriodoPago);
		log.info("-----regenerarPeriodoCerrado");
		List<Object[]> objects = lecturaBO.findObjects("Lectura.findByPeridoCerrModificable", map);
		Integer cantidadAlcantarilladosAplicados = 0;
		RegistroEconomico registroEconomicoAlcantarillado = reBO.inicializar(periodoPago, "ALCANCON",
				"Alcantarillado " + periodoPago.getDescripcion(), 0, usuario);
		log.info(String.format("-----Cantidad Usuarios: %s", objects.size()));
		if (!objects.isEmpty()) {
			for (Object[] object : objects) {
				CabeceraPlanilla cp = (CabeceraPlanilla) object[0];
				Servicio servicio = (Servicio) object[1];
				Double vAlcan = servicio.getIdTarifa() != null ? servicio.getIdTarifa().getAlcantarillado() : 0.0;
				// Obtenemos la lectura de la llave
				if (servicio != null && TipoServicioEnum.AGUA_POTABLE.equals(servicio.getTipoServicio())) {
					// Obtenemos la lectura
					map = new HashMap<>();
					map.put("idServicio", servicio.getIdServicio());
					map.put("idPeriodoPago", periodoPago);
					Lectura lec = lecturaBO.findByNamedQuery("Lectura.findByPeridoAndLlave", map);
					if (lec != null) {
						HashMap<String, Object> pama = new HashMap<>();
						pama.put("idLectura", lec);
						pama.put("idCabeceraPlanilla", cp);

						DetallePlanilla dpls = detallePlanillaBO.findByNamedQuery("DetallePlanilla.findByLectura",
								pama);
						if (dpls == null)
							dpls = detallePlanillaBO.buildInitialDetailLectura(cp, lec);
						dpls = detallePlanillaBO.builDetailLectura(periodoPago, lec, dpls);
						if (lec.getMetros3() > 0 || lec.getValorMetro3() > 0)
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
				} else if (servicio != null && TipoServicioEnum.ALCANTARILLADO.equals(servicio.getTipoServicio())) {
					HashMap<String, Object> pama = new HashMap<>();
					pama.put("idServicio", servicio);
					pama.put("idCabeceraPlanilla", cp);
					DetallePlanilla dpls = detallePlanillaBO.findByNamedQuery("DetallePlanilla.findByServicio", pama);
					if (dpls == null) {
						DetallePlanilla alcantarillado = detallePlanillaBO.crearDetalleAlcantarillado(usuario, cp,
								registroEconomicoAlcantarillado, 1, vAlcan, periodoPago.getDescripcion(), servicio);
						cp.setSubtotal(Utilitario.redondear(cp.getSubtotal() + alcantarillado.getValorTotal()));
						cp.setTotal(Utilitario.redondear(cp.getTotal() + alcantarillado.getValorTotal()));
						registroEconomicoAlcantarillado
								.setValor(registroEconomicoAlcantarillado.getValor() + alcantarillado.getValorTotal());
						detallePlanillaBO.save(usuario, alcantarillado);
						cantidadAlcantarilladosAplicados++;
					}
				}
				update(usuario, cp);
				Runtime.getRuntime().gc();
				enviarEmail(cp, periodoPago);
			}
			Runtime.getRuntime().gc();
		}
		log.info("-----regenerarPeriodoCerrado Fin");

	}

	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	@Override
	public void finalizarPlanilla(Usuario usuario, Integer idPeriodoPago) throws Exception {
		PeriodoPago periodoPago = periodoPagoBO.findByIdCustom(idPeriodoPago);
		HashMap<String, Object> p = new HashMap<>();

		// Obtenemos las planillas en cero y las actualziamos en cero
		// Obtenemos las planillas no pagadas
		p.put("estado", "ING");
		p.put("estado2", "ING");
		p.put("idPeriodoPago", idPeriodoPago);
		List<CabeceraPlanilla> planillasEncero = findAllByNamedQuery("CabeceraPlanilla.findCero", p);
		log.info(String.format("Planillas con valor Cero :%s", planillasEncero.size()));
		for (CabeceraPlanilla cp : planillasEncero) {
			cambioEstadoBO.cambiarEstadoSinVerificar(21, usuario, cp.getIdCabeceraPlanilla(), "");
			Query queryDetalle = em().createQuery(
					" UPDATE DetallePlanilla SET estado='PAG',valorPendiente=0.0,valorPagado=valorTotal where idCabeceraPlanilla=:idCabeceraPlanilla");
			queryDetalle.setParameter("idCabeceraPlanilla", cp);
			Query queryLecturas = em().createQuery(
					" UPDATE Lectura SET estado='PAG' where idLectura IN (SELECT dp.idLectura.idLectura FROM DetallePlanilla dp WHERE dp.idCabeceraPlanilla=:idCabeceraPlanilla)");
			queryLecturas.setParameter("idCabeceraPlanilla", cp);
		}

		// Obtenemos las planillas no pagadas
		p.put("estado", "ING");
		p.put("estado2", "ING");
		p.put("idPeriodoPago", idPeriodoPago);
		List<CabeceraPlanilla> planillasNoPagadas = findAllByNamedQuery("CabeceraPlanilla.findSinPagar", p);
		log.info(String.format("Cantidad de Planillas no PAGADAS:%s", planillasNoPagadas.size()));
		int contador = 0;
		for (CabeceraPlanilla planillaActual : planillasNoPagadas) {
			CabeceraPlanilla planillaAnterior = getAbonoMesAnterior(planillaActual.getIdServicio(), planillaActual);
			if (planillaAnterior != null && planillaAnterior.getAbonoUsd() != 0.0) {
				Double abonoUsd = planillaAnterior.getAbonoUsd();
				planillaAnterior.setAbonoUsd(0.0);
				planillaActual.setAbonoUsd(abonoUsd);
				update(usuario, planillaActual);
				update(usuario, planillaAnterior);
				contador++;
			}
		}
		log.info(String.format("Planillas No Pagadas Con Abono Anterior :%s", contador));
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

		// AQUI VA EL CALCULO DEL VALOR TOMADO DE LOS ABONOS PARA REDUCIR EL
		// VALOR DE LAS CUENTAS POR PAGAR
		p.put("estado", "PAG");
		p.put("estado2", "INC");
		p.put("idPeriodoPago", idPeriodoPago);
		List<CabeceraPlanilla> planillasPagadasConAbono = findAllByNamedQuery("CabeceraPlanilla.findConAbono", p);
		log.info(String.format("Planillas pagadas con abono :%s", planillasPagadasConAbono.size()));

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
		RegistroEconomico rePorDevolverAnterior = reBO.findByNamedQuery("RegistroEconomico.findByType", map);
		Double valorDevuelto = 0.0;
		Double valorPorDevolverPendiente = 0.0;
		for (CabeceraPlanilla cp : planillasPagadasConAbono) {
			valorDevuelto += Utilitario.redondear(cp.getValorPagadoAbono());
		}
		log.info(String.format("Valor Recaudado por abono: %s", valorDevuelto));
		if (rePorDevolverAnterior != null) {
			valorPorDevolverPendiente = Utilitario.redondear(rePorDevolverAnterior.getValor() - valorDevuelto);
			rePorDevolverAnterior.setValor(0.0);
			reBO.update(usuario, rePorDevolverAnterior);
		}
		// Obtenemos los abonos del presente mes
		p.put("estado", "PAG");
		p.put("estado2", "INC");
		p.put("idPeriodoPago", idPeriodoPago);
		List<CabeceraPlanilla> planillasConAbono = findAllByNamedQuery("CabeceraPlanilla.findNoPag", p);

		Double totalRecaudado = 0.0;
		Double valorPorDevolver = 0.0;
		for (CabeceraPlanilla cabeceraPlanilla : planillasConAbono) {
			totalRecaudado += (cabeceraPlanilla.getValorPagado() != null ? cabeceraPlanilla.getValorPagado() : 0.0);
			valorPorDevolver += (cabeceraPlanilla.getAbonoUsd() != null ? cabeceraPlanilla.getAbonoUsd() : 0.0);
		}
		log.info(String.format("Recaudado : %1$s Abonos Mes: %2$s", totalRecaudado, valorPorDevolver));

		// AL Valor Total de este mes actual tambien sumamos el valor pendiente
		// por devolver del mes pasado
		if (valorPorDevolverPendiente > 0.0)
			valorPorDevolver += valorPorDevolverPendiente;
		log.info(String.format("Valor Total Pendiente Por Devolver: %s", valorPorDevolver));

		map.clear();
		map.put("idPeriodoPago", periodoPagoBO.findByIdCustom(idPeriodoPago));
		map.put("tipoRegistro", "CUEPAG");
		RegistroEconomico rePorDevolver = reBO.findByNamedQuery("RegistroEconomico.findByType", map);

		// Registramos en caja por parte de servicios el valor pagado del periodo
		if (totalRecaudado > 0.0)
			cuentaBO.registrarAsiento(Constantes.numeroCaja, Constantes.cuentaCaja, Constantes.numeroServicio,
					Constantes.cuentaServicio, Utilitario.redondear(totalRecaudado), usuario);
		// Registramos en las cuentas por pagar de pero que estan en caja
		if (valorPorDevolver > 0.0)
			cuentaBO.registrarAsiento(Constantes.numeroCuentasPorPagar, Constantes.cuentaCuentasPorPagar,
					Constantes.numeroCaja, Constantes.cuentaCaja, Utilitario.redondear(valorPorDevolver), usuario);
		// Registramos en caja el valor devuelto con pago mediante abono
		if (valorDevuelto > 0.0)
			cuentaBO.registrarAsiento(Constantes.numeroCaja, Constantes.cuentaCaja, Constantes.numeroCuentasPorPagar,
					Constantes.cuentaCuentasPorPagar, Utilitario.redondear(valorDevuelto), usuario);
		// Actualizamos el registro economico del valor por devolver
		if (rePorDevolver != null) {
			if (valorPorDevolver.equals(0.0)) {
				cambioEstadoBO.eliminarEntidad(8, rePorDevolver.getIdRegistroEconomico());
			} else {
				rePorDevolver.setValor(valorPorDevolver);
				reBO.update(usuario, rePorDevolver);
				cambioEstadoBO.cambiarEstadoSinVerificar(40, usuario, rePorDevolver.getIdRegistroEconomico(), "");
			}
		}

		// Registramos en caja el valor total de los gastos
		log.info("Inicia Obtención de Gastos");
		map.clear();
		map.put("idPeriodoPago", idPeriodoPago);
		List<Gasto> gastos = gastoBO.findAllByNamedQuery("Gasto.findAllByUser", map);
		Double totalGastos = 0.0;
		for (Gasto gasto : gastos) {
			totalGastos = totalGastos + (gasto.getValor() != null ? gasto.getValor() : 0.0);
		}

		log.info(String.format("Valor Total Gastos: %s", totalGastos));
		// Tambien actualizamos en el registro economico de gastos
		map.clear();
		map.put("idPeriodoPago", periodoPagoBO.findByIdCustom(idPeriodoPago));
		map.put("tipoRegistro", "GAST");
		RegistroEconomico registroEconomico = reBO.findByNamedQuery("RegistroEconomico.findByType", map);
		if (registroEconomico != null) {
			// Si no existe gastos eliminamos el registro economico
			if (totalGastos.equals(0.0)) {
				cambioEstadoBO.eliminarEntidad(8, registroEconomico.getIdRegistroEconomico());
			} else {
				registroEconomico.setValor(totalGastos);
				registroEconomico.setCantidadAplicados(1);
				reBO.update(usuario, registroEconomico);
				cambioEstadoBO.cambiarEstadoSinVerificar(40, usuario, registroEconomico.getIdRegistroEconomico(), "");
			}
		}
		// Registramos los gastos en el libro diario
		if (totalGastos > 0.0)
			cuentaBO.registrarAsiento(Constantes.numeroGasto, Constantes.cuentaGasto, Constantes.numeroCaja,
					Constantes.cuentaCaja, Utilitario.redondear(totalGastos), usuario);

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
		System.gc();
	}

	@Override
	public CabeceraPlanilla getAbonoMesAnterior(Servicio llave, CabeceraPlanilla actual) throws Exception {
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

		CabeceraPlanilla anterior = getAbonoMesAnterior(cp.getIdServicio(), cp);

		map.clear();
		map.put("idPeriodoPago", anterior.getIdPeriodoPago());
		map.put("tipoRegistro", "CUEPAG");
		RegistroEconomico registroEconomicoPorDevolver = reBO.findByNamedQuery("RegistroEconomico.findByType", map);
		Double valorAbonoAnterior = Utilitario.redondear(anterior.getAbonoUsd());

		if (registroEconomicoPorDevolver != null) {
			registroEconomicoPorDevolver
					.setValor(Utilitario.redondear(registroEconomicoPorDevolver.getValor() + valorAbonoAnterior));
			reBO.update(usuario, registroEconomicoPorDevolver);
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
		map.put("idServicio", cp.getIdServicio());

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
		RegistroEconomico registroEconomicoPorDevolver = reBO.findByNamedQuery("RegistroEconomico.findByType", map);
		if (registroEconomicoPorDevolver != null) {
			if (registroEconomicoPorDevolver.getValor() > 0)
				registroEconomicoPorDevolver.setValor(
						Utilitario.redondear(registroEconomicoPorDevolver.getValor() - anterior.getAbonoUsd()));
			else
				// Si la cuenta ya no tiene ningun valor le seteamos con valor
				// cero
				registroEconomicoPorDevolver.setValor(0.0);
			reBO.update(usuario, registroEconomicoPorDevolver);
		}

		update(usuario, cp);
		String nombre = cp.getIdUsuario().getApellidos() + " " + cp.getIdUsuario().getNombres();
		log.info(String.format("%3$s ===> Factura: %4$s Total: %1$s, Paga: %2$s ,Abono: %5$s", cp.getTotal(),
				cp.getValorPagado(), nombre, cp.getObservacion(), cp.getAbonoUsd()));
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
		if (lectura.getIdPeriodoPago().getAnio() > 2020 && lectura.getIdPeriodoPago().getMes() > 0) {
			if ("PAG".equalsIgnoreCase(cabeceraPlanilla.getEstado())
					|| "TRAS".equalsIgnoreCase(cabeceraPlanilla.getEstado())) {
				throw new Exception("La factura ya se encuentra pagada o finalizada");
			}
			if ("ING".equalsIgnoreCase(cabeceraPlanilla.getEstado())) {
				lectura = lecturaBO.recalcularLectura(usuario, lectura);
				CabeceraPlanilla cp = cabeceraPlanilla;
				lecturaBO.update(usuario, lectura);
				HashMap<String, Object> pama = new HashMap<>();
				pama.put("idLectura", lectura);
				pama.put("idCabeceraPlanilla", cabeceraPlanilla);
				DetallePlanilla detallePlanilla = detallePlanillaBO
						.findByNamedQuery("DetallePlanilla.findByLecturaAndCabcera", pama);
				detallePlanilla = detallePlanillaBO.builDetailLectura(lectura.getIdPeriodoPago(), lectura,
						detallePlanilla);
				detallePlanillaBO.update(usuario, detallePlanilla);
				// Recalculamos el valor de la planilla
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
					cp.setValorPendiente(
							Utilitario.redondear(cp.getTotal()) - Utilitario.redondear(cp.getValorPagado()));
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

				if (lectura.getMetros3() > 0) {
					detallePlanilla
							.setDescripcion(Utilitario.redondear(lectura.getMetros3() + lectura.getMetros3Exceso())
									+ " m3 " + lectura.getIdPeriodoPago().getDescripcion());
				}
				if (lectura.getMetros3() > 0)
					detallePlanillaBO.update(usuario, detallePlanilla);
			}
			Runtime.getRuntime().gc();
		} else {
			throw new Exception("No puede recalcular una lectura que fue registrada antes del cambio del reglamento");
		}
	}

	/**
	 * Inicia con valores por defectos una planila
	 * 
	 * @param idPeriodoPago
	 * @param idServicio
	 * @param responsable
	 * @param path
	 * @param numeroFactura
	 * @return
	 * @throws Exception
	 */
	protected CabeceraPlanilla iniciarCabecera(Integer idPeriodoPago, Usuario responsable, String path,
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
		planillaNueva.setIdUsuario(responsable);
		planillaNueva.setObservacion(
				path.substring(0, path.length() - numeroFactura.toString().length()) + numeroFactura.toString());
		planillaNueva.setIdPeriodoPago(periodoPagoBO.findByIdCustom(idPeriodoPago));
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
	protected CabeceraPlanilla verificarMultNoPago(Servicio llave, Usuario user, CabeceraPlanilla pn, Usuario usuario,
			RegistroEconomico multaAtrazoMes) throws Exception {
		Integer contador = 0;
		List<CabeceraPlanilla> pnps = findPlanillasByServicioAndStatus(llave, user, "NOPAG");

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
			Double valorMulta = tarifaBO.getValorMulta(user);
			Double multa = detallePlanillaBO.crearMulta(pnp, pn, multaAtrazoMes, usuario, valorMulta);
			if (user.getTieneDescuento())
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
	protected CabeceraPlanilla verificarPlanillasIncompletas(Servicio llave, Usuario user, CabeceraPlanilla pn,
			Usuario usuario) throws Exception {
		List<CabeceraPlanilla> pnps = findPlanillasByServicioAndStatus(llave, user, "INC");
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

}
