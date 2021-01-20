package org.ec.jap.bo.saap.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.ec.jap.bo.saap.LecturaBO;
import org.ec.jap.bo.saap.ParametroBO;
import org.ec.jap.bo.saap.RangoConsumoBO;
import org.ec.jap.bo.saap.TipoRegistroBO;
import org.ec.jap.dao.saap.impl.LecturaDAOImpl;
import org.ec.jap.entiti.saap.Lectura;
import org.ec.jap.entiti.saap.Llave;
import org.ec.jap.entiti.saap.PeriodoPago;
import org.ec.jap.entiti.saap.RangoConsumo;
import org.ec.jap.entiti.saap.TipoRegistro;
import org.ec.jap.entiti.saap.Usuario;
import org.ec.jap.enumerations.Formapago;
import org.ec.jap.utilitario.Utilitario;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class LecturaBOImpl extends LecturaDAOImpl implements LecturaBO {

	private static final Logger log = Logger.getLogger(LecturaBOImpl.class.getName());
	@EJB
	private ParametroBO parametroBO;
	@EJB
	private RangoConsumoBO rangoConsumoBO;

	@EJB
	private TipoRegistroBO tipoRegistroBO;

	/**
	 * Default constructor.
	 */
	public LecturaBOImpl() {
	}

	protected Lectura buildLectura(Lectura lectura, PeriodoPago pp) throws Exception {
		lectura.setValorBasico(0.0);
		if (lectura.getSinLectura()) {
			lectura.setLecturaIngresada(0.0);
			lectura.setMetros3(0.0);
			lectura.setMetros3Exceso(0.0);
			lectura.setValorBasico(0.0);
			lectura.setValorMetro3(0.0);
			lectura.setValorMetro3Exceso(0.0);
			lectura.setUsuarioNuevo(false);
		} else if (lectura.getUsuarioNuevo()) {
			// OBTENEMOS LA LECTURA ANTERIOR QUE NO ESTE SIN LECTURA O DE UN
			// USUARIO NUEVO SI NO EXISTE O EL VALOR DE
			// CONSUMO ES CERO ACTUALIZAMOS LA LECTURA INGRESADA COMO
			// LECTURA DE UN USUARIO NUEVO
			lectura.setSinLectura(false);
			if (lectura.getLecturaAnterior() == null || lectura.getLecturaAnterior() == 0.0) {
				lectura.setLecturaIngresada(0.0);
				lectura.setMetros3(0.0);
				lectura.setMetros3Exceso(0.0);
				lectura.setValorBasico(0.0);
				lectura.setValorMetro3(0.0);
				lectura.setValorMetro3Exceso(0.0);
				// Si es un usuario nuevo no puede tener lecturas anteriores
			} else {
				lectura.setUsuarioNuevo(false);
			}
		} else {
			if ((lectura.getLecturaIngresada() != null && !lectura.getLecturaIngresada().equals(0.0))
					|| lectura.getLecturaAnterior().equals(lectura.getLecturaIngresada())) {
				lectura = buildTarifa(lectura, pp);
			}
		}
		return lectura;
	}

	@Override
	public String guardarLecturas(Usuario usuario, List<Lectura> lecturas, PeriodoPago pp) throws Exception {
		Double valorDiferencia = parametroBO.getNumerico("", usuario.getIdComunidad().getIdComunidad(), "DIFCONS");
		String mensaje = "";
		String mensajeAlerta = "";
		for (Lectura lectura : lecturas) {
			lectura = buildLectura(lectura, pp);
			if ((lectura.getLecturaIngresada() != null && !lectura.getLecturaIngresada().equals(0.0))
					|| lectura.getLecturaAnterior().equals(lectura.getLecturaIngresada())) {
				Double totalM3 = Utilitario.redondear(lectura.getMetros3() + lectura.getMetros3Exceso());
				Double diferencia = Math.abs(Utilitario.redondear(totalM3 - lectura.getMetros3Anterior()));
				if (diferencia >= valorDiferencia) {
					mensajeAlerta += lectura.getIdLlave().getNumero() + ", ";
				}
			} else
				mensaje += lectura.getIdLlave().getNumero() + ", ";
		}
		for (Lectura lectura : lecturas) {
			update(usuario, lectura);
		}
		if (!mensaje.isEmpty())
			mensaje = "Debe registrar las lecturas de las siguientes llaves: \n "
					+ mensaje.substring(0, mensaje.length() - 2);
		if (!mensajeAlerta.isEmpty())
			mensaje = mensaje + "\n \n "
					+ "Las lecturas de las siguientes llaves posiblemente este ingresadas incorrectamente : \n"
					+ mensajeAlerta.substring(0, mensajeAlerta.length() - 2);

		return mensaje;

	}

	@Override
	public Lectura recalcularLectura(Usuario usuario, Lectura lectura) throws Exception {
		lectura.setValorBasico(0.0);
		lectura.setModifiqued(true);
		lectura = buildLectura(lectura, lectura.getIdPeriodoPago());
		update(usuario, lectura);
		return lectura;
	}

	protected Lectura buildTarifa(Lectura lectura, PeriodoPago pp) throws Exception {
		Double consumo = Utilitario.redondear(lectura.getLecturaIngresada() - lectura.getLecturaAnterior());
		lectura.setMetros3(consumo);
		lectura.setValorBasico(0.0);
		lectura.setValorMetro3Exceso(0.0);
		lectura.setMetros3Exceso(0.0);
		if (lectura.getMetros3() > 0) {
			if (!lectura.getMetros3().equals(0.0)) {
				map = new HashMap<>(0);
				map.put("idTarifa", lectura.getIdLlave().getIdTarifa());
				map.put("valor", lectura.getMetros3());
				map.put("epoca", pp.getEpoca());
				RangoConsumo rangoConsumo = rangoConsumoBO.findByNamedQuery("RangoConsumo.findByTarifaAndValor", map);
				// Si
				if (rangoConsumo == null) {
					// En caso que no exista en la tarifa del usuario buscamos en todas las tarifas
					// configuradas
					map.clear();
					map.put("valor", lectura.getMetros3());
					map.put("epoca", pp.getEpoca());
					RangoConsumo rangoAll = rangoConsumoBO.findByNamedQuery("RangoConsumo.findByValorAndEpoca", map);
					// Si no existe en ninguna buscamos
					if (rangoAll == null) {

						rangoConsumo = rangoConsumoBO.findByNamedQuery("RangoConsumo.findByMaxTarifaAndValor", map);
						lectura.setMetros3Exceso(0.0);
						lectura.setValorMetro3Exceso(0.0);
						lectura.setMetros3(lectura.getMetros3());// El
						lectura.setBasicoM3(0.0);
					}

				} else
					lectura.setBasicoM3(0.0);
				lectura.setValorMetro3Exceso(0.0);
				lectura.setValorMetro3(Utilitario.redondear(rangoConsumo.getValorM3()));
			}
		} else {
			lectura.setLecturaIngresada(lectura.getLecturaAnterior());
			lectura.setMetros3(0.0);
			lectura.setMetros3Exceso(0.0);
			lectura.setValorMetro3(0.0);
			lectura.setValorMetro3Exceso(0.0);
		}
		return lectura;
	}

	@Override
	public String guardarLecturasCerradas(Usuario usuario, List<Lectura> lecturas) throws Exception {
		Double valorDiferencia = parametroBO.getNumerico("", usuario.getIdComunidad().getIdComunidad(), "DIFCONS");
		String mensaje = "";
		String mensajeAlerta = "";
		for (Lectura lectura : lecturas) {
			lectura.setValorBasico(Utilitario.redondear(lectura.getIdLlave().getIdTarifa().getBasicoPago()));
			if (lectura.getSinLectura() || !lectura.getEsModificable()) {
				if ((lectura.getLecturaIngresada() != null && !lectura.getLecturaIngresada().equals(0.0))
						|| lectura.getLecturaAnterior().equals(lectura.getLecturaIngresada())) {
					lectura = buildTarifa(lectura, lectura.getIdPeriodoPago());
					Double totalM3 = Utilitario.redondear(lectura.getMetros3() + lectura.getMetros3Exceso());
					Double diferencia = Math.abs(Utilitario.redondear(totalM3 - lectura.getMetros3Anterior()));
					if (diferencia >= valorDiferencia) {
						mensajeAlerta += lectura.getIdLlave().getNumero() + ", ";
					}

				} else
					mensaje += lectura.getIdLlave().getNumero() + ", ";
			}
		}
		for (Lectura lectura : lecturas) {
			update(usuario, lectura);
		}
		if (!mensaje.isEmpty())
			mensaje = "Debe registrar las lecturas de las siguientes llaves: \n "
					+ mensaje.substring(0, mensaje.length() - 2);
		if (!mensajeAlerta.isEmpty())
			mensaje = mensaje + "\n \n "
					+ "Las lecturas de las siguientes llaves posiblemente este ingresadas incorrectamente : \n"
					+ mensajeAlerta.substring(0, mensajeAlerta.length() - 2);
		return mensaje;

	}

	@Override
	public Integer findLecturaAnterior(Llave llave, Boolean usuarioNuevo) throws Exception {
		HashMap<String, Object> mapAux = new HashMap<>();
		mapAux.put("idLlave", llave);
		mapAux.put("usuarioNuevo", usuarioNuevo);
		Integer idLecturaAnteriorIngresada = findIntegerByNamedQuery("Lectura.findLastByPeridoAndLlave", mapAux);
		return idLecturaAnteriorIngresada;
	}

	@Override
	public void iniciarLecturaCero(PeriodoPago periodoPago, Llave llave, Usuario usuario) throws Exception {
		TipoRegistro registro = tipoRegistroBO.findByPk("CONS");
		Integer idLecturaAnteriorIngresada = findLecturaAnterior(llave, true);
		Lectura lecturaAnterior = null;
		if (idLecturaAnteriorIngresada != null)
			lecturaAnterior = findByPk(idLecturaAnteriorIngresada);
		Lectura lectura = new Lectura();
		lectura.setTipoRegistro(registro);
		lectura.setIdLlave(llave);
		lectura.setSinLectura(false);
		lectura.setUsuarioNuevo(false);
		lectura.setEsModificable(false);
		lectura.setIdPeriodoPago(periodoPago);
		lectura.setEstado("ING");
		lectura.setDescripcion(
				"Lectura de " + Utilitario.mes(periodoPago.getMes()) + " - " + periodoPago.getAnio().toString());
		lectura.setMetros3(0.0);
		lectura.setFechaRegistro(Calendar.getInstance().getTime());
		lectura.setLecturaIngresada(0.0);
		lectura.setValorMetro3Exceso(0.0);
		lectura.setMetros3Exceso(0.0);
		lectura.setValorMetro3(0.0);
		lectura.setValorBasico(Utilitario.redondear(lectura.getIdLlave().getIdTarifa().getBasicoPago()));
		if (lecturaAnterior != null) {
			log.info(String.format("Id Lectura anterior: ", lecturaAnterior.getIdLectura()));
			Double me3Anterior = Utilitario
					.redondear(lecturaAnterior.getMetros3() + lecturaAnterior.getMetros3Exceso());
			log.info(String.format("M3 Anterior: ", me3Anterior));
			lectura.setMetros3Anterior(me3Anterior);
			if (!lecturaAnterior.getSinLectura())
				lectura.setLecturaAnterior(lecturaAnterior != null ? lecturaAnterior.getLecturaIngresada() : 0.0);
			else
				lectura.setLecturaAnterior(lecturaAnterior != null ? lecturaAnterior.getLecturaAnterior() : 0.0);
		} else
			lectura.setLecturaAnterior(0.0);
		lectura.setNumeroMeses(
				lecturaAnterior != null
						? (Utilitario.numeroMeses(lecturaAnterior.getIdPeriodoPago().getFechaInicio(),
								periodoPago.getFechaInicio()))
						: 1);
		save(usuario, lectura);

	}
}
