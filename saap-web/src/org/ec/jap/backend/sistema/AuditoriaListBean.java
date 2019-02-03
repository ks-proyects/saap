/**
 * 
 */
package org.ec.jap.backend.sistema;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.sistema.AuditoriaBO;
import org.ec.jap.entiti.sistema.Auditoria;

/**
 * @author Freddy G Castillo C
 * 
 */
@ManagedBean
@ViewScoped
public class AuditoriaListBean extends Bean {

	@EJB
	AuditoriaBO auditoriaBO;

	private Date fechaMin;
	private Date fechaMax;
	private Integer idUsuario;
	private String tipoEntidad;
	private List<Auditoria> auditorias;

	public AuditoriaListBean() {
		super();
		Calendar calendar=Calendar.getInstance();
		fechaMax=calendar.getTime();
		
		calendar.add(Calendar.MONTH, -1);
		fechaMin=calendar.getTime();
		
		idUsuario=0;
		tipoEntidad="0";
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
			map.put("fechaMin", fechaMin);
			map.put("fechaMax", fechaMax);
			map.put("tipoEntidad", tipoEntidad);
			map.put("idUsuario", idUsuario);
			auditorias = auditoriaBO.findAllByNamedQuery("Auditoria.findByFilter", map);
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}
	
	public List<SelectItem> getTipoEntidadList() {
		try {
			return getSelectItems(getUsuarioCurrent(), null, true, "ListaValor.findTipoEntidad");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public List<SelectItem> getUsuarioList() {
		try {
			return getSelectItems(getUsuarioCurrent(), null, true, "ListaValor.findUserSystem");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public Date getFechaMin() {
		return fechaMin;
	}

	public void setFechaMin(Date fechaMin) {
		this.fechaMin = fechaMin;
	}

	public Date getFechaMax() {
		return fechaMax;
	}

	public void setFechaMax(Date fechaMax) {
		this.fechaMax = fechaMax;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getTipoEntidad() {
		return tipoEntidad;
	}

	public void setTipoEntidad(String tipoEntidad) {
		this.tipoEntidad = tipoEntidad;
	}

	public List<Auditoria> getAuditorias() {
		return auditorias;
	}

	public void setAuditorias(List<Auditoria> auditorias) {
		this.auditorias = auditorias;
	}

}
