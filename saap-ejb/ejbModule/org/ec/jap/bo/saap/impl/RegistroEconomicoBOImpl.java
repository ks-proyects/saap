package org.ec.jap.bo.saap.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateless;

import org.ec.jap.bo.saap.CabeceraPlanillaBO;
import org.ec.jap.bo.saap.DetallePlanillaBO;
import org.ec.jap.bo.saap.LlaveBO;
import org.ec.jap.bo.saap.RegistroEconomicoBO;
import org.ec.jap.bo.saap.TipoRegistroBO;
import org.ec.jap.bo.sistema.CambioEstadoBO;
import org.ec.jap.dao.saap.impl.RegistroEconomicoDAOImpl;
import org.ec.jap.entiti.dto.UsuarioPagoDTO;
import org.ec.jap.entiti.saap.CabeceraPlanilla;
import org.ec.jap.entiti.saap.DetallePlanilla;
import org.ec.jap.entiti.saap.PeriodoPago;
import org.ec.jap.entiti.saap.RegistroEconomico;
import org.ec.jap.entiti.saap.Usuario;
import org.ec.jap.utilitario.Utilitario;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class RegistroEconomicoBOImpl extends RegistroEconomicoDAOImpl implements RegistroEconomicoBO {

	/**
	 * Default constructor.
	 */
	public RegistroEconomicoBOImpl() {
	}

	@EJB
	CabeceraPlanillaBO cabeceraPlanillaBO;
	@EJB
	DetallePlanillaBO detallePlanillaBO;
	@EJB
	LlaveBO llaveBO;

	@EJB
	TipoRegistroBO tipoRegistroBO;

	@Override
	public void aplicarCuota(Usuario usuario, Integer idRegistroEconomico) throws Exception {
		RegistroEconomico registroEconomico = findByPk(idRegistroEconomico);
		Integer count = 0;
		if (!"ABIE".equals(registroEconomico.getIdPeriodoPago().getEstado()))
			throw new EJBTransactionRolledbackException("El periodo ya ha sido cerrado o aún no esta abierto");
		List<CabeceraPlanilla> cabeceraPlanillas = cabeceraPlanillaBO.findAllByNamedQuery("CabeceraPlanilla.findAllIngresado");
		for (CabeceraPlanilla cabeceraPlanilla : cabeceraPlanillas) {
			DetallePlanilla detallePlanilla = new DetallePlanilla();
			map = new HashMap<>();
			// Verificamos que aun no se lo haya aplicado
			map.put("idCabeceraPlanilla", cabeceraPlanilla);
			map.put("idRegistroEconomico", registroEconomico);
			detallePlanilla = detallePlanillaBO.findByNamedQuery("DetallePlanilla.findByRegistroAndCabecara", map);
			if (detallePlanilla == null) {
				detallePlanilla = new DetallePlanilla();
				detallePlanilla.setEstado("ING");
				detallePlanilla.setIdCabeceraPlanilla(cabeceraPlanilla);
				detallePlanilla.setIdRegistroEconomico(registroEconomico);
				detallePlanilla.setValorUnidad(registroEconomico.getValor());
				detallePlanilla.setValorTotal(registroEconomico.getValor());
				detallePlanilla.setFechaRegistro(Calendar.getInstance().getTime());
				detallePlanilla.setValorPendiente(detallePlanilla.getValorTotal());
				detallePlanilla.setValorPagado(0.0);
				String tipoRegistro = registroEconomico.getTipoRegistro().getTipoRegistro();
				if ("CONS".equals(tipoRegistro)) {
					detallePlanilla.setOrdenStr("B");
					detallePlanilla.setDescripcion(registroEconomico.getDescripcion());
				} else if ("MULAGU".equals(tipoRegistro) || "BASCON".equalsIgnoreCase(tipoRegistro)) {
					detallePlanilla.setOrdenStr("A");
					detallePlanilla.setDescripcion(registroEconomico.getDescripcion());
				} else if ("CUO".equals(tipoRegistro)) {
					detallePlanilla.setOrdenStr("F");
					detallePlanilla.setDescripcion(registroEconomico.getDescripcion());
				} else if ("CUOINI".equals(tipoRegistro)) {
					detallePlanilla.setOrdenStr("G");
					detallePlanilla.setDescripcion(registroEconomico.getDescripcion());
				} else if ("INASIS".equals(tipoRegistro)) {
					detallePlanilla.setOrdenStr("E");
					detallePlanilla.setDescripcion(registroEconomico.getDescripcion());
				} else {
					detallePlanilla.setOrdenStr("H");
				}
				detallePlanillaBO.save(usuario, detallePlanilla);

				cabeceraPlanilla.setSubtotal(cabeceraPlanilla.getSubtotal() + detallePlanilla.getValorTotal());
				cabeceraPlanilla.setTotal(cabeceraPlanilla.getTotal() + detallePlanilla.getValorTotal());
				cabeceraPlanillaBO.update(usuario, cabeceraPlanilla);
			} else if (Utilitario.redondear(detallePlanilla.getValorTotal()) != Utilitario.redondear(registroEconomico.getValor())) {
				cabeceraPlanilla.setSubtotal(cabeceraPlanilla.getSubtotal() - detallePlanilla.getValorTotal());
				cabeceraPlanilla.setTotal(cabeceraPlanilla.getTotal() - detallePlanilla.getValorTotal());

				detallePlanilla.setValorUnidad(registroEconomico.getValor());
				detallePlanilla.setValorTotal(registroEconomico.getValor());
				detallePlanillaBO.update(usuario, detallePlanilla);
				cabeceraPlanilla.setSubtotal(cabeceraPlanilla.getSubtotal() + detallePlanilla.getValorTotal());
				cabeceraPlanilla.setTotal(cabeceraPlanilla.getTotal() + detallePlanilla.getValorTotal());
				cabeceraPlanillaBO.update(usuario, cabeceraPlanilla);
			}
			count++;
		}
		registroEconomico.setCantidadAplicados(count);
		update(usuario, registroEconomico);

	}

	@Override
	public List<UsuarioPagoDTO> getUsuarioPagoDTOs(RegistroEconomico registroEconomico, String filtro, Usuario usuario) throws Exception {
		// TODO Auto-generated method stub
		List<UsuarioPagoDTO> dtos = new ArrayList<>();
		HashMap<String, Object> p = new HashMap<>();
		p.put("idRegistroEconomico", registroEconomico);
		p.put("filtro", filtro != null ? filtro.trim() : "");
		List<Object[]> list = findObjects("RegistroEconomico.finUser", p);

		for (Object[] objects : list) {
			UsuarioPagoDTO dto = new UsuarioPagoDTO();
			CabeceraPlanilla cabeceraPlanilla = (CabeceraPlanilla) objects[1];
			DetallePlanilla detallePlanilla = (DetallePlanilla) objects[4];

			if (detallePlanilla != null) {
				cabeceraPlanilla.setSubtotal(Utilitario.redondear((cabeceraPlanilla.getSubtotal() - detallePlanilla.getValorTotal())));
				cabeceraPlanilla.setTotal(Utilitario.redondear(cabeceraPlanilla.getTotal() - detallePlanilla.getValorTotal()));

				detallePlanilla.setValorTotal(Utilitario.redondear(registroEconomico.getValor()));
				detallePlanilla.setFechaRegistro(Calendar.getInstance().getTime());
				detallePlanilla.setValorPendiente(detallePlanilla.getValorTotal());
				detallePlanilla.setValorUnidad(Utilitario.redondear(registroEconomico.getValor()));

				cabeceraPlanilla.setSubtotal(Utilitario.redondear(cabeceraPlanilla.getSubtotal() + detallePlanilla.getValorTotal()));
				cabeceraPlanilla.setTotal(Utilitario.redondear(cabeceraPlanilla.getTotal() + detallePlanilla.getValorTotal()));

				detallePlanillaBO.update(usuario, detallePlanilla);
				cabeceraPlanillaBO.update(usuario, cabeceraPlanilla);
			}

			dto.setUsuario((Usuario) objects[0]);
			dto.setCabeceraPlanilla(cabeceraPlanilla);
			dto.setSeleccionado("S".equals(objects[2].toString()));
			String estado = (objects[3] != null ? (String) objects[3] : "");
			dto.setEstadoDescripcion("PAG".equals(estado) ? "Pagado" : "NOPAG".equals(estado) ? "No Pagado" : "ING".equals(estado) ? "Ingresado" : "");
			dto.setDetallePlanilla(detallePlanilla);
			dtos.add(dto);
		}
		return dtos;
	}

	@EJB
	CambioEstadoBO cambioEstadoBO;

	@Override
	public RegistroEconomico inicializar(PeriodoPago periodoPago, String tipoRegistro, String descripcion, Integer cantidadAplicados, Usuario usuario) throws Exception {
		RegistroEconomico registroEconomico = new RegistroEconomico();
		registroEconomico.setTipoRegistro(tipoRegistroBO.findByPk(tipoRegistro));
		registroEconomico.setIdPeriodoPago(periodoPago);
		registroEconomico.setDescripcion(descripcion);
		registroEconomico.setFechaRegistro(Calendar.getInstance().getTime());
		registroEconomico.setCantidadAplicados(cantidadAplicados);
		registroEconomico.setEstado("ING");
		registroEconomico.setValor(0.0);
		save(usuario, registroEconomico);
		cambioEstadoBO.cambiarEstadoSinVerificar(8, usuario, registroEconomico.getIdRegistroEconomico(), "");
		cambioEstadoBO.cambiarEstadoSinVerificar(9, usuario, registroEconomico.getIdRegistroEconomico(), "");
		return registroEconomico;
	}
}
