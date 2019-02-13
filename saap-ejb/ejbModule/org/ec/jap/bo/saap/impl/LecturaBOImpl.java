package org.ec.jap.bo.saap.impl;

import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.ec.jap.bo.saap.LecturaBO;
import org.ec.jap.bo.saap.ParametroBO;
import org.ec.jap.bo.saap.RangoConsumoBO;
import org.ec.jap.dao.saap.impl.LecturaDAOImpl;
import org.ec.jap.entiti.saap.Lectura;
import org.ec.jap.entiti.saap.RangoConsumo;
import org.ec.jap.entiti.saap.Usuario;
import org.ec.jap.enumerations.Formapago;
import org.ec.jap.utilitario.Utilitario;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class LecturaBOImpl extends LecturaDAOImpl implements LecturaBO {

	@EJB
	private ParametroBO parametroBO;
	@EJB
	private RangoConsumoBO rangoConsumoBO;

	/**
	 * Default constructor.
	 */
	public LecturaBOImpl() {
	}

	@Override
	public String guardarLecturas(Usuario usuario, List<Lectura> lecturas) throws Exception {
		// Parametro que indica el método de calculo de los metros consumidos
		String aplicaMetodoCobroAnt = parametroBO.getString("", usuario.getIdComunidad().getIdComunidad(), "APLTITAA");
		Boolean esMetodoAnterior = "SI".equalsIgnoreCase(aplicaMetodoCobroAnt);
		Double valorDiferencia = parametroBO.getNumerico("", usuario.getIdComunidad().getIdComunidad(), "DIFCONS");
		String mensaje = "";
		String mensajeAlerta = "";
		for (Lectura lectura : lecturas) {
			lectura.setValorBasico(0.0);
			if (lectura.getSinLectura()) {
				lectura.setLecturaIngresada(0.0);
				lectura.setMetros3(0.0);
				lectura.setMetros3Exceso(0.0);
				lectura.setValorBasico(0.0);
				lectura.setValorMetro3(0.0);
				lectura.setValorMetro3Exceso(0.0);
				lectura.setUsuarioNuevo(false);
				// lectura.setLecturaIngresada(lectura.getLecturaAnterior());
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
					lectura.setValorMetro3Exceso(0.0);// Si es un usuario nuevo
														// no puede tener
														// lecturas anteriores
				} else {
					lectura.setUsuarioNuevo(false);
				}
			} else {

				if ((lectura.getLecturaIngresada() != null && !lectura.getLecturaIngresada().equals(0.0)) || lectura.getLecturaAnterior().equals(lectura.getLecturaIngresada())) {
					lectura.setMetros3(lectura.getLecturaIngresada() - lectura.getLecturaAnterior());
					lectura.setValorBasico(Utilitario.redondear(lectura.getIdLlave().getIdTarifa().getBasicoPago()));
					lectura.setValorMetro3Exceso(0.0);
					lectura.setMetros3Exceso(0.0);
					if (lectura.getMetros3() > 0) {
						if (!lectura.getMetros3().equals(0.0)) {
							map = new HashMap<>(0);
							map.put("idTarifa", lectura.getIdLlave().getIdTarifa());
							map.put("valor", lectura.getMetros3());
							map.put("formaPago", "SI".equalsIgnoreCase(aplicaMetodoCobroAnt) ? Formapago.valueOf("A") : Formapago.valueOf("N"));
							RangoConsumo rangoConsumo = rangoConsumoBO.findByNamedQuery("RangoConsumo.findByTarifaAndValor", map);
							// Si no esta en algun rango buscamos el exseso
							if (rangoConsumo == null) {
								// Para el caso de la modalidad anterior en el
								// registro
								// viene el valor por metro cubico y el valor de
								// la
								// mulata por exceso
								rangoConsumo = rangoConsumoBO.findByNamedQuery("RangoConsumo.findByMaxTarifaAndValor", map);
								Double metros3Exceso = esMetodoAnterior ? Utilitario.redondear((lectura.getMetros3() - rangoConsumo.getM3Minimo())) : 0;
								lectura.setMetros3Exceso(metros3Exceso);

								lectura.setValorMetro3Exceso(esMetodoAnterior ? Utilitario.redondear(rangoConsumo.getValorExceso()) : 0);
								lectura.setMetros3(esMetodoAnterior ? Utilitario.redondear(rangoConsumo.getM3Minimo()) : lectura.getMetros3());// El
								// valor
								// basico
								// consumido
								// sera
								// la
								// diferencia
								lectura.setBasicoM3(esMetodoAnterior ? rangoConsumo.getM3Minimo() : 0);
							} else
								lectura.setBasicoM3(esMetodoAnterior ? rangoConsumo.getM3Maximo() : 0);
							lectura.setValorMetro3Exceso(esMetodoAnterior ? rangoConsumo.getValorExceso() : 0);
							lectura.setValorMetro3(Utilitario.redondear(rangoConsumo.getValorM3()));
						}
					} else {
						lectura.setLecturaIngresada(lectura.getLecturaAnterior());
						lectura.setMetros3(0.0);
						lectura.setMetros3Exceso(0.0);
						lectura.setValorMetro3(0.0);
						lectura.setValorMetro3Exceso(0.0);
					}
					Double totalM3 = Utilitario.redondear(lectura.getMetros3() + lectura.getMetros3Exceso());
					Double diferencia = Math.abs(Utilitario.redondear(totalM3 - lectura.getMetros3Anterior()));
					if (diferencia >= valorDiferencia) {
						mensajeAlerta += lectura.getIdLlave().getNumero() + ", ";
					}
				} else {
					mensaje += lectura.getIdLlave().getNumero() + ", ";
				}
			}
		}

		for (Lectura lectura : lecturas) {
			update(usuario, lectura);
		}
		if (!mensaje.isEmpty())
			mensaje = "Debe registrar las lecturas de las siguientes llaves: \n " + mensaje.substring(0, mensaje.length() - 2);
		if (!mensajeAlerta.isEmpty())
			mensaje = mensaje + "\n \n " + "Las lecturas de las siguientes llaves posiblemente este ingresadas incorrectamente : \n" + mensajeAlerta.substring(0, mensajeAlerta.length() - 2);

		return mensaje;

	}

	@Override
	public Lectura recalcularLectura(Usuario usuario, Lectura lectura) throws Exception {
		// Parametro que indica el método de calculo de los metros consumidos
		String aplicaMetodoCobroAnt = parametroBO.getString("", usuario.getIdComunidad().getIdComunidad(), "APLTITAA");
		Boolean esMetodoAnterior = "SI".equalsIgnoreCase(aplicaMetodoCobroAnt);
		lectura.setValorBasico(0.0);
		if (lectura.getSinLectura()) {
			lectura.setLecturaIngresada(0.0);
			lectura.setMetros3(0.0);
			lectura.setMetros3Exceso(0.0);
			lectura.setValorBasico(0.0);
			lectura.setValorMetro3(0.0);
			lectura.setValorMetro3Exceso(0.0);
			lectura.setUsuarioNuevo(false);
			// lectura.setLecturaIngresada(lectura.getLecturaAnterior());
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
			} else {
				lectura.setUsuarioNuevo(false);
			}
		} else {

			if ((lectura.getLecturaIngresada() != null && !lectura.getLecturaIngresada().equals(0.0)) || lectura.getLecturaAnterior().equals(lectura.getLecturaIngresada())) {
				lectura.setMetros3(lectura.getLecturaIngresada() - lectura.getLecturaAnterior());
				lectura.setValorBasico(Utilitario.redondear(lectura.getIdLlave().getIdTarifa().getBasicoPago()));
				lectura.setValorMetro3Exceso(0.0);
				lectura.setMetros3Exceso(0.0);
				if (lectura.getMetros3() > 0) {
					if (!lectura.getMetros3().equals(0.0)) {
						map = new HashMap<>(0);
						map.put("idTarifa", lectura.getIdLlave().getIdTarifa());
						map.put("valor", lectura.getMetros3());
						map.put("formaPago", esMetodoAnterior ? Formapago.valueOf("A") : Formapago.valueOf("N"));
						RangoConsumo rangoConsumo = rangoConsumoBO.findByNamedQuery("RangoConsumo.findByTarifaAndValor", map);
						if (rangoConsumo == null) {
							rangoConsumo = rangoConsumoBO.findByNamedQuery("RangoConsumo.findByMaxTarifaAndValor", map);
							Double metros3Excso = esMetodoAnterior ? Utilitario.redondear((lectura.getMetros3() - rangoConsumo.getM3Minimo())) : 0.0;
							lectura.setMetros3Exceso(metros3Excso);
							Double valorMetro3Exceso = esMetodoAnterior ? Utilitario.redondear(rangoConsumo.getValorExceso()) : 0.0;
							lectura.setValorMetro3Exceso(valorMetro3Exceso);
							lectura.setMetros3(esMetodoAnterior ? Utilitario.redondear(rangoConsumo.getM3Minimo()) : lectura.getMetros3());
							lectura.setBasicoM3(esMetodoAnterior ? rangoConsumo.getM3Minimo() : 0);
						} else
							lectura.setBasicoM3(esMetodoAnterior ? rangoConsumo.getM3Maximo() : 0);
						lectura.setValorMetro3Exceso(esMetodoAnterior ? Utilitario.redondear(rangoConsumo.getValorExceso()) : 0);
						lectura.setValorMetro3(Utilitario.redondear(rangoConsumo.getValorM3()));
					}
				} else {
					lectura.setLecturaIngresada(lectura.getLecturaAnterior());
					lectura.setMetros3(0.0);
					lectura.setMetros3Exceso(0.0);
					lectura.setValorMetro3(0.0);
					lectura.setValorMetro3Exceso(0.0);
				}
			}
		}

		update(usuario, lectura);

		return lectura;
	}

	@Override
	public String guardarLecturasCerradas(Usuario usuario, List<Lectura> lecturas) throws Exception {
		// Parametro que indica el método de calculo de los metros consumidos
		String aplicaMetodoCobroAnt = parametroBO.getString("", usuario.getIdComunidad().getIdComunidad(), "APLTITAA");
		Boolean esMetodoAnterior = "SI".equalsIgnoreCase(aplicaMetodoCobroAnt);
		Double valorDiferencia = parametroBO.getNumerico("", usuario.getIdComunidad().getIdComunidad(), "DIFCONS");
		String mensaje = "";
		String mensajeAlerta = "";

		for (Lectura lectura : lecturas) {
			lectura.setValorBasico(Utilitario.redondear(lectura.getIdLlave().getIdTarifa().getBasicoPago()));
			if (lectura.getSinLectura() || !lectura.getEsModificable()) {
				if ((lectura.getLecturaIngresada() != null && !lectura.getLecturaIngresada().equals(0.0)) || lectura.getLecturaAnterior().equals(lectura.getLecturaIngresada())) {
					lectura.setMetros3(lectura.getLecturaIngresada() - lectura.getLecturaAnterior());
					
					lectura.setMetros3Exceso(0.0);
					lectura.setValorMetro3Exceso(0.0);
					if (lectura.getMetros3() > 0) {
						if (!lectura.getMetros3().equals(0.0)) {
							map = new HashMap<>(0);
							map.put("idTarifa", lectura.getIdLlave().getIdTarifa());
							map.put("valor", lectura.getMetros3());
							map.put("formaPago", esMetodoAnterior ? Formapago.valueOf("A") : Formapago.valueOf("N"));
							RangoConsumo rangoConsumo = rangoConsumoBO.findByNamedQuery("RangoConsumo.findByTarifaAndValor", map);
							// Si no esta en algun rango buscamos el exseso
							if (rangoConsumo == null) {
								// Para el caso de la modalidad anterior en el
								// registro
								// viene el valor por metro cubico y el valor de
								// la
								// mulata por exceso
								rangoConsumo = rangoConsumoBO.findByNamedQuery("RangoConsumo.findByMaxTarifaAndValor", map);
								Double exceso = esMetodoAnterior ? Utilitario.redondear((lectura.getMetros3() - rangoConsumo.getM3Minimo())) : 0;
								lectura.setMetros3Exceso(exceso);
								Double valorExceso = esMetodoAnterior ? Utilitario.redondear(rangoConsumo.getValorExceso()) : 0;
								lectura.setValorMetro3Exceso(valorExceso);
								lectura.setMetros3(esMetodoAnterior ? Utilitario.redondear(rangoConsumo.getM3Minimo()) : lectura.getMetros3());// El
								// valor
								// basico
								// consumido
								// sera
								// la
								// diferencia
								lectura.setBasicoM3(esMetodoAnterior ? rangoConsumo.getM3Minimo() : 0);
							} else {
								lectura.setBasicoM3(esMetodoAnterior ? rangoConsumo.getM3Maximo() : 0);

							}
							lectura.setValorMetro3Exceso(esMetodoAnterior ? rangoConsumo.getValorExceso() : 0);
							lectura.setValorMetro3(Utilitario.redondear(rangoConsumo.getValorM3()));
						}
					} else {
						lectura.setLecturaIngresada(lectura.getLecturaAnterior());
						lectura.setMetros3(0.0);
						lectura.setMetros3Exceso(0.0);
						lectura.setValorMetro3(0.0);
						lectura.setValorMetro3Exceso(0.0);
					}
					Double totalM3 = Utilitario.redondear(lectura.getMetros3() + lectura.getMetros3Exceso());
					Double diferencia = Math.abs(Utilitario.redondear(totalM3 - lectura.getMetros3Anterior()));
					if (diferencia >= valorDiferencia) {
						mensajeAlerta += lectura.getIdLlave().getNumero() + ", ";
					}

				} else {
					mensaje += lectura.getIdLlave().getNumero() + ", ";
				}
			}
		}

		for (Lectura lectura : lecturas) {
			update(usuario, lectura);
		}
		if (!mensaje.isEmpty())
			mensaje = "Debe registrar las lecturas de las siguientes llaves: \n " + mensaje.substring(0, mensaje.length() - 2);
		if (!mensajeAlerta.isEmpty())
			mensaje = mensaje + "\n \n " + "Las lecturas de las siguientes llaves posiblemente este ingresadas incorrectamente : \n" + mensajeAlerta.substring(0, mensajeAlerta.length() - 2);

		return mensaje;

	}
}
