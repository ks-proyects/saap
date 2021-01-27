package org.ec.jap.backend.pago;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.saap.CabeceraPlanillaBO;
import org.ec.jap.entiti.saap.CabeceraPlanilla;
import org.ec.jap.entiti.saap.Servicio;

@ManagedBean
@ViewScoped
public class ConsultaConsumoBean {

	@EJB
	CabeceraPlanillaBO cabeceraPlanillaBO;

	/**
	 * 
	 */
	private String filtro;
	private CabeceraPlanilla cp;

	public ConsultaConsumoBean() {
		super();
	}

	@PostConstruct
	public void init() {
		cp = new CabeceraPlanilla();
		cp.setIdServicio(new Servicio());
	}

	public void search(ActionEvent event) {

		try {
			HashMap<String, Object> hashMap = new HashMap<>();
			hashMap.put("filtro", filtro != null ? filtro : "");
			List<CabeceraPlanilla> cabeceraPlanillas = cabeceraPlanillaBO.findAllByNamedQuery("CabeceraPlanilla.findConsulta", hashMap);
			if (!cabeceraPlanillas.isEmpty()) {
				cp = cabeceraPlanillas.get(0);
			} else {
				cp = new CabeceraPlanilla();
				cp.setIdServicio(new Servicio());
				String msg = "El pago para el beneficioario con la cedula/# llave " + filtro + ", no existe";
				FacesMessage facesMessage = new FacesMessage(Mensaje.SEVERITY_INFO, msg, msg);
				FacesContext.getCurrentInstance().addMessage(null, facesMessage);
			}
		} catch (Exception e2) {// TODO: handle exception
		}
	}

	/**
	 * @return the filtro
	 */
	public String getFiltro() {
		return filtro;
	}

	/**
	 * @param filtro
	 *            the filtro to set
	 */
	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}

	/**
	 * @return the cp
	 */
	public CabeceraPlanilla getCp() {
		return cp;
	}

	/**
	 * @param cp
	 *            the cp to set
	 */
	public void setCp(CabeceraPlanilla cp) {
		this.cp = cp;
	}

}
