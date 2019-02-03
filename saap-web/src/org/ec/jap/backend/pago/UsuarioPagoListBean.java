/**
 * 
 */
package org.ec.jap.backend.pago;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.saap.DetallePlanillaBO;
import org.ec.jap.bo.saap.RegistroEconomicoBO;
import org.ec.jap.entiti.dto.UsuarioPagoDTO;
import org.ec.jap.entiti.saap.RegistroEconomico;

/**
 * @author Freddy G Castillo C
 * 
 */
@ManagedBean
@ViewScoped
public class UsuarioPagoListBean extends Bean {

	@EJB
	RegistroEconomicoBO registroEconomicoBO;

	@EJB
	DetallePlanillaBO detallePlanillaBO;

	private RegistroEconomico registroEconomico;

	private List<UsuarioPagoDTO> pagoDTOs;

	private String filtro;

	public UsuarioPagoListBean() {
		super();
	}

	@PostConstruct
	public void init() {
		try {
			super.init();
			search(null);
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	public void search(ActionEvent event) {
		try {
			map = new HashMap<>();
			registroEconomico = registroEconomicoBO.findByPk(getParam1Integer());
			setNivelBloqueo("ABIE".equals(registroEconomico.getIdPeriodoPago().getEstado()) ? 0 : 1);
			redisplayAction(7, getNivelBloqueo() == 0);
			pagoDTOs = registroEconomicoBO.getUsuarioPagoDTOs(registroEconomico, filtro, getUsuarioCurrent());
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	public void changeName(AjaxBehaviorEvent event) {
		try {
			pagoDTOs = registroEconomicoBO.getUsuarioPagoDTOs(registroEconomico, filtro, getUsuarioCurrent());
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@Override
	public String guardar() {
		try {
			for (UsuarioPagoDTO dto : pagoDTOs) {
				if (dto.getSeleccionado() && dto.getDetallePlanilla() == null) {
					detallePlanillaBO.asignarMulta(getUsuarioCurrent(), dto.getCabeceraPlanilla(), registroEconomico);
				} else if (!dto.getSeleccionado() && dto.getDetallePlanilla() != null) {
					detallePlanillaBO.quitarMulta(getUsuarioCurrent(), dto.getCabeceraPlanilla(), registroEconomico, dto.getDetallePlanilla());
				}
			}
			pagoDTOs = registroEconomicoBO.getUsuarioPagoDTOs(registroEconomico, filtro, getUsuarioCurrent());
			displayMessage(Mensaje.defaultMessaje, Mensaje.SEVERITY_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
		return super.guardar();
	}

	public String getFiltro() {
		return filtro;
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}

	public RegistroEconomico getRegistroEconomico() {
		return registroEconomico;
	}

	public void setRegistroEconomico(RegistroEconomico registroEconomico) {
		this.registroEconomico = registroEconomico;
	}

	public List<UsuarioPagoDTO> getPagoDTOs() {
		return pagoDTOs;
	}

	public void setPagoDTOs(List<UsuarioPagoDTO> pagoDTOs) {
		this.pagoDTOs = pagoDTOs;
	}

}
