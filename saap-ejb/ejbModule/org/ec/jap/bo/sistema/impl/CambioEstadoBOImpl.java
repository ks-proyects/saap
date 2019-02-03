package org.ec.jap.bo.sistema.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

import org.ec.jap.bo.saap.ActividadBO;
import org.ec.jap.bo.saap.CabeceraPlanillaBO;
import org.ec.jap.bo.saap.PeriodoPagoBO;
import org.ec.jap.bo.saap.RegistroEconomicoBO;
import org.ec.jap.bo.sistema.CambioEstadoBO;
import org.ec.jap.bo.sistema.CambioEstadoCondicionBO;
import org.ec.jap.bo.sistema.DocumentoAdjuntoBO;
import org.ec.jap.bo.sistema.EntidadCambioEstadoBO;
import org.ec.jap.dao.sistema.impl.CambioEstadoDAOImpl;
import org.ec.jap.entiti.saap.PeriodoPago;
import org.ec.jap.entiti.saap.Usuario;
import org.ec.jap.entiti.sistema.CambioEstado;
import org.ec.jap.entiti.sistema.CambioEstadoCondicion;
import org.ec.jap.entiti.sistema.EntidadCambioEstado;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class CambioEstadoBOImpl extends CambioEstadoDAOImpl implements CambioEstadoBO {

	/**
	 * Default constructor.
	 */
	@EJB
	EntidadCambioEstadoBO entidadCambioEstadoBO;
	@EJB
	CambioEstadoCondicionBO cambioEstadoCondicionBO;
	@EJB
	DocumentoAdjuntoBO documentoAdjuntoBO;

	public CambioEstadoBOImpl() {
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void cambiarEstado(Integer idCambioEstado, Usuario usuario, Object idDocumento) throws Exception {
		CambioEstado cambioEstado = findByPk(idCambioEstado);
		if (cambioEstado == null)
			return;

		if ("S".equals(cambioEstado.getAplicaCondicion())) {
			cambioEstadoCondicionBO.cumpleCondicion(cambioEstado, idDocumento);
		}

		verificarCambio(cambioEstado.getIdEstadoAnterior().getEstado(), cambioEstado.getIdEstadoNuevo().getEstado(), usuario, idDocumento, cambioEstado.getTipoEntidad().getTipoEntidad());

		String updateStado = cambioEstado.getTipoEntidad().getSentenciaUpdate();
		Query query = em().createQuery(updateStado);
		query.setParameter("id", idDocumento);
		query.setParameter("estado", cambioEstado.getIdEstadoNuevo().getEstado());

		query.executeUpdate();

		EntidadCambioEstado entidadCambioEstado = new EntidadCambioEstado();
		entidadCambioEstado.setAccion(cambioEstado.getDescripcion());
		entidadCambioEstado.setEstadoResultante(cambioEstado.getIdEstadoNuevo().getDescripcion());
		entidadCambioEstado.setFecha(Calendar.getInstance().getTime());
		entidadCambioEstado.setIdDocumento((Integer) idDocumento);
		entidadCambioEstado.setIdCambioEstado(cambioEstado);
		entidadCambioEstado.setTipoEntidad(cambioEstado.getTipoEntidad());
		entidadCambioEstado.setObservacion("Realizado por " + usuario.getNombres() + " " + usuario.getApellidos());
		entidadCambioEstadoBO.save(usuario, entidadCambioEstado);

	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void cambiarEstado(Integer idCambioEstado, Usuario usuario, Object idDocumento, String motivo) throws Exception {
		// TODO Auto-generated method stub
		CambioEstado cambioEstado = findByPk(idCambioEstado);
		if (cambioEstado == null)
			return;

		verificarCambio(cambioEstado.getIdEstadoAnterior().getEstado(), cambioEstado.getIdEstadoNuevo().getEstado(), usuario, idDocumento, cambioEstado.getTipoEntidad().getTipoEntidad());

		String updateStado = cambioEstado.getTipoEntidad().getSentenciaUpdate();
		Query query = em().createQuery(updateStado);
		query.setParameter("id", idDocumento);
		query.setParameter("estado", cambioEstado.getIdEstadoNuevo().getEstado());

		query.executeUpdate();

		EntidadCambioEstado entidadCambioEstado = new EntidadCambioEstado();
		entidadCambioEstado.setAccion(cambioEstado.getDescripcion());
		entidadCambioEstado.setEstadoResultante(cambioEstado.getIdEstadoNuevo().getDescripcion());
		entidadCambioEstado.setFecha(Calendar.getInstance().getTime());
		entidadCambioEstado.setIdDocumento((Integer) idDocumento);
		entidadCambioEstado.setIdCambioEstado(cambioEstado);
		entidadCambioEstado.setTipoEntidad(cambioEstado.getTipoEntidad());
		entidadCambioEstado.setObservacion("Realizado por " + usuario.getNombres() + " " + usuario.getApellidos());
		entidadCambioEstado.setMotivo(motivo);
		entidadCambioEstadoBO.save(usuario, entidadCambioEstado);

	}

	@Override
	public void cambiarEstadoSinVerificar(Integer idCambioEstado, Usuario usuario, Object idDocumento, String motivo) throws Exception {
		// TODO Auto-generated method stub
		CambioEstado cambioEstado = findByPk(idCambioEstado);
		if (cambioEstado == null)
			return;

		String updatEstado = cambioEstado.getTipoEntidad().getSentenciaUpdate();
		Query query = em().createQuery(updatEstado);
		query.setParameter("id", idDocumento);
		query.setParameter("estado", cambioEstado.getIdEstadoNuevo().getEstado());

		query.executeUpdate();

		EntidadCambioEstado entidadCambioEstado = new EntidadCambioEstado();
		entidadCambioEstado.setAccion(cambioEstado.getDescripcion());
		entidadCambioEstado.setEstadoResultante(cambioEstado.getIdEstadoNuevo().getDescripcion());
		entidadCambioEstado.setFecha(Calendar.getInstance().getTime());
		entidadCambioEstado.setIdDocumento((Integer) idDocumento);
		entidadCambioEstado.setIdCambioEstado(cambioEstado);
		entidadCambioEstado.setTipoEntidad(cambioEstado.getTipoEntidad());
		entidadCambioEstado.setObservacion("Realizado por " + usuario.getNombres() + " " + usuario.getApellidos());
		entidadCambioEstado.setMotivo(motivo);
		entidadCambioEstadoBO.save(usuario, entidadCambioEstado);

	}

	@Override
	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public void eliminarEntidad(Integer idCambioEstado, Object idDocumento) throws Exception {
		CambioEstado cambioEstado = findByPk(idCambioEstado);
		if (cambioEstado == null)
			return;

		String updatEstado = cambioEstado.getTipoEntidad().getSentenciaDelete();
		Query query = em().createQuery(updatEstado);
		query.setParameter("id", idDocumento);
		query.executeUpdate();
		if (idDocumento instanceof Integer) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("idDocumento", idDocumento);
			entidadCambioEstadoBO.executeQuerry("EntidadCambioEstado.deleteByIdentidad", map);
			documentoAdjuntoBO.executeQuerry("DocumentoAdjunto.deleteByIdentidad", map);
		}

	}

	@EJB
	PeriodoPagoBO periodoPagoBO;
	@EJB
	RegistroEconomicoBO registroEconomicoBO;
	@EJB
	CabeceraPlanillaBO cabeceraPlanillaBO;

	@EJB
	ActividadBO actividadBO;

	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public void verificarCambio(String estadoActual, String estadoNuevo, Usuario usuario, Object idDocumento, String tipoEntidad) throws Exception {

		switch (tipoEntidad) {
		case "PERPAG":
			switch (estadoNuevo) {
			case "ABIE":
				switch (estadoActual) {
				case "ING":
					List<PeriodoPago> periodoPago = periodoPagoBO.findAllByNamedQuery("PeriodoPago.findAbiertoCerrado");
					if (periodoPago.size() > 0)
						throw new EJBTransactionRolledbackException("No puede abrir un periodo de pago si existen periodos de pago sin finalizar o en estado abierto.");
					cabeceraPlanillaBO.iniciarCabeceraPlanilla(usuario, (Integer) idDocumento);
					break;
				case "ABIE":
					cabeceraPlanillaBO.regenerarPlanillasDePeriodo(usuario, (Integer) idDocumento);
					break;
				case "CERR":
					cabeceraPlanillaBO.regenerarPeriodoCerrado(usuario, (Integer) idDocumento);
					break;
				default:
					break;
				}
				break;
			case "CERR":
				switch (estadoActual) {
				case "CERR":
					cabeceraPlanillaBO.regenerarPeriodoCerrado(usuario, (Integer) idDocumento);
					break;
				case "ABIE":
					cabeceraPlanillaBO.cerrarLecturasAPlanilla(usuario, (Integer) idDocumento);
					break;
				default:
					break;
				}

				break;
			case "CERRI":
				switch (estadoActual) {
				case "CERR":
					cabeceraPlanillaBO.regenerarPeriodoCerrado(usuario, (Integer) idDocumento);
				case "ABIE":
					cabeceraPlanillaBO.cerrarLecturasAPlanilla(usuario, (Integer) idDocumento);
				default:
					break;
				}

				break;
			case "FIN":
				cabeceraPlanillaBO.finalizarPlanilla(usuario, (Integer) idDocumento);
				break;
			case "FINI":
				cabeceraPlanillaBO.finalizarPlanilla(usuario, (Integer) idDocumento);
				break;

			default:
				break;
			}
			break;
		case "REGECO":
			switch (estadoNuevo) {
			case "APL":
				registroEconomicoBO.aplicarCuota(usuario, (Integer) idDocumento);
				break;

			case "":
				break;

			default:
				break;
			}
			break;
		case "CABPLA":
			switch (estadoNuevo) {
			case "ANU":
				break;
			case "PAG":
				cabeceraPlanillaBO.pagarPlanilla(usuario, (Integer) idDocumento);
				break;
			case "ING":
				switch (estadoActual) {
				case "INC":
					cabeceraPlanillaBO.descartarPago(usuario, (Integer) idDocumento);
					break;
				case "PAG":
					cabeceraPlanillaBO.descartarPago(usuario, (Integer) idDocumento);
					break;
				default:
					break;
				}
				break;
			case "":

				break;

			default:
				break;
			}
			break;
		case "ACT":
			switch (estadoNuevo) {
			case "ING":
				switch (estadoActual) {
				case "ING":
					actividadBO.generarAsistencia(usuario, (Integer) idDocumento);
					break;
				default:
					break;
				}
				break;

			default:
				break;
			}
			break;
		case "":

		default:
			break;
		}

	}

	@EJB
	CambioEstadoCondicionBO estadoCondicionBO;

	@Override
	public Boolean verificarEstado(CambioEstado cambioEstado, Integer idDocument) throws Exception {
		CambioEstadoCondicion estadoCondicion = null;
		try {
			if ("N".equals(cambioEstado.getAplicaCondicionInicial()))
				return true;
			else {
				map = new HashMap<>();
				map.put("idCambioEstado", cambioEstado);
				estadoCondicion = estadoCondicionBO.findByNamedQuery("CambioEstadoCondicion.findByCambioEstado", map);
				if (estadoCondicion == null)
					return true;
				else {
					if ("S".equals(estadoCondicion.getAplica())) {
						Query query = em().createQuery(estadoCondicion.getSentenciaValida());

						query.setParameter("id", idDocument);
						Integer valorReturn = Integer.valueOf(query.getSingleResult().toString()).intValue();
						if (valorReturn.equals(0))
							return false;
						else
							return true;
					} else {
						return true;
					}
				}
			}
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public void cambiarEstadoMandatory(Integer idCambioEstado, Usuario usuario, Object idDocumento) throws Exception {
		CambioEstado cambioEstado = findByPk(idCambioEstado);
		if (cambioEstado == null)
			return;

		if ("S".equals(cambioEstado.getAplicaCondicion())) {
			cambioEstadoCondicionBO.cumpleCondicion(cambioEstado, idDocumento);
		}

		verificarCambio(cambioEstado.getIdEstadoAnterior().getEstado(), cambioEstado.getIdEstadoNuevo().getEstado(), usuario, idDocumento, cambioEstado.getTipoEntidad().getTipoEntidad());

		String updateStado = cambioEstado.getTipoEntidad().getSentenciaUpdate();
		Query query = em().createQuery(updateStado);
		query.setParameter("id", idDocumento);
		query.setParameter("estado", cambioEstado.getIdEstadoNuevo().getEstado());

		query.executeUpdate();

		EntidadCambioEstado entidadCambioEstado = new EntidadCambioEstado();
		entidadCambioEstado.setAccion(cambioEstado.getDescripcion());
		entidadCambioEstado.setEstadoResultante(cambioEstado.getIdEstadoNuevo().getDescripcion());
		entidadCambioEstado.setFecha(Calendar.getInstance().getTime());
		entidadCambioEstado.setIdDocumento((Integer) idDocumento);
		entidadCambioEstado.setIdCambioEstado(cambioEstado);
		entidadCambioEstado.setTipoEntidad(cambioEstado.getTipoEntidad());
		entidadCambioEstado.setObservacion("Realizado por " + usuario.getNombres() + " " + usuario.getApellidos());
		entidadCambioEstadoBO.save(usuario, entidadCambioEstado);

	}
}
