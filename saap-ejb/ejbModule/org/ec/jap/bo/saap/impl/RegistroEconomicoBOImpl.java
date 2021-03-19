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
import org.ec.jap.bo.saap.ServicioBO;
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
	ServicioBO llaveBO;

	@EJB
	TipoRegistroBO tipoRegistroBO;

	@Override
	public void aplicarCuota(Usuario usuario, Integer idRegistroEconomico) throws Exception {
		RegistroEconomico registroEconomico = findByPk(idRegistroEconomico);
		Integer count = 0;
		if (!"ABIE".equals(registroEconomico.getIdPeriodoPago().getEstado()))
			throw new EJBTransactionRolledbackException("El periodo ya ha sido cerrado o aún no esta abierto");
		List<CabeceraPlanilla> cabeceraPlanillas = cabeceraPlanillaBO
				.findAllByNamedQuery("CabeceraPlanilla.findAllIngresado");
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
			} else if (Utilitario.redondear(detallePlanilla.getValorTotal()) != Utilitario
					.redondear(registroEconomico.getValor())) {
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
	public List<UsuarioPagoDTO> getUsuarioPagoDTOs(RegistroEconomico registroEconomico, String filtro, Usuario usuario)
			throws Exception {
		// TODO Auto-generated method stub
		List<UsuarioPagoDTO> dtos = new ArrayList<>();
		HashMap<String, Object> p = new HashMap<>();
		p.put("idRegistroEconomico", registroEconomico);
		p.put("filtro", filtro != null ? filtro.trim() : "");
		List<Object[]> list = findObjects("RegistroEconomico.finUser", p);
		for (Object[] objects : list) {
			UsuarioPagoDTO dto = new UsuarioPagoDTO();
			CabeceraPlanilla cabeceraPlanilla = (CabeceraPlanilla) objects[1];
			if (cabeceraPlanilla != null) {
				Integer id = (Integer) objects[2];
				DetallePlanilla detallePlanilla = null;
				if (id != null) {
					detallePlanilla = detallePlanillaBO.findByIdCustom(id);
					if (detallePlanilla != null) {
						Double valorT = detallePlanilla.getValorTotal();
						cabeceraPlanilla.setSubtotal(Utilitario.redondear((cabeceraPlanilla.getSubtotal() - valorT)));
						cabeceraPlanilla.setTotal(Utilitario.redondear(cabeceraPlanilla.getTotal() - valorT));
						detallePlanilla.setValorTotal(Utilitario.redondear(registroEconomico.getValor()));
						detallePlanilla.setFechaRegistro(Calendar.getInstance().getTime());
						detallePlanilla.setValorPendiente(detallePlanilla.getValorTotal());
						detallePlanilla.setValorUnidad(Utilitario.redondear(registroEconomico.getValor()));
						cabeceraPlanilla.setSubtotal(Utilitario.redondear(cabeceraPlanilla.getSubtotal() + valorT));
						cabeceraPlanilla.setTotal(Utilitario.redondear(cabeceraPlanilla.getTotal() + valorT));
						detallePlanillaBO.update(usuario, detallePlanilla);
						cabeceraPlanillaBO.update(usuario, cabeceraPlanilla);
					}
				}
				dto.setUsuario((Usuario) objects[0]);
				dto.setCabeceraPlanilla(cabeceraPlanilla);
				dto.setSeleccionado(detallePlanilla != null);
				String estado = (detallePlanilla != null ? detallePlanilla.getEstado() : "");
				dto.setEstadoDescripcion("PAG".equals(estado) ? "Pagado"
						: "NOPAG".equals(estado) ? "No Pagado" : "ING".equals(estado) ? "Ingresado" : "");
				dto.setDetallePlanilla(detallePlanilla);
				dtos.add(dto);
			}
		}
		return dtos;
	}

	@EJB
	CambioEstadoBO cambioEstadoBO;

	@Override
	public RegistroEconomico inicializar(PeriodoPago periodoPago, String tipoRegistro, String descripcion,
			Integer cantidadAplicados, Usuario usuario) throws Exception {
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

	@Override
	public RegistroEconomico getByPeriodo(PeriodoPago periodoPago, String tipo) throws Exception {
		map.clear();
		map.put("idPeriodoPago", periodoPago);
		map.put("tipoRegistro", tipoRegistroBO.findByPk(tipo));
		List<RegistroEconomico> registroEconomicos = findAllByNamedQuery("RegistroEconomico.findByPeriodoAndTipo", map);
		return registroEconomicos.size() > 0 ? registroEconomicos.get(0) : null;
	}

	@Override
	public RegistroEconomico iniciarPeriodPago(PeriodoPago periodoPago, Usuario usuario) throws Exception {
		inicializar(periodoPago, "INASIS", "Multa Inasistencia " + periodoPago.getDescripcion(), 1, usuario);
		// Gastos, lo creamos uno para que pueda registrar los gastos en todo
		// este ciclo
		inicializar(periodoPago, "GAST", "Gasto " + periodoPago.getDescripcion(), 1, usuario);
		// Cuentas Por Pagar, para que las registre la cuentas por pagar de este
		// mes
		inicializar(periodoPago, "CUEPAG", "Valor Por Pagar " + periodoPago.getDescripcion(), 1, usuario);

		RegistroEconomico reg = inicializar(periodoPago, "MULAGU", "Multa Consumo " + periodoPago.getDescripcion(), 0,
				usuario);
		return reg;
	}

	@Override
	public RegistroEconomico iniciarMulta(PeriodoPago periodoPago, Usuario usuario) throws Exception {
		RegistroEconomico reg = inicializar(periodoPago, "MULAGU", "Multa Consumo " + periodoPago.getDescripcion(), 0,
				usuario);
		return reg;
	}
}
