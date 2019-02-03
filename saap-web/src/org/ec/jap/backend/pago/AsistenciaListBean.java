/**
 * 
 */
package org.ec.jap.backend.pago;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.saap.ActividadBO;
import org.ec.jap.bo.saap.AsistenciaBO;
import org.ec.jap.entiti.saap.Actividad;
import org.ec.jap.entiti.saap.Asistencia;
import org.ec.jap.entiti.sistema.Filtro;

/**
 * @author Freddy G Castillo C
 * 
 */
@ManagedBean
@ViewScoped
public class AsistenciaListBean extends Bean {

	@EJB
	AsistenciaBO asistenciaBO;

	@EJB
	ActividadBO actividadBO;

	private Actividad actividad;

	private Filtro filtro;

	private Filtro filtroEstado;

	private List<Asistencia> listAsistencias;

	public AsistenciaListBean() {
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

			filtro = filtroBO.getFiltro(getUsuarioCurrent(), "FILTRO", getPage(), filtro != null ? filtro.getValorCadena() : "",filtro,false);
			filtroEstado = filtroBO.getFiltro(getUsuarioCurrent(), "FILTRO_BOOLEAN", getPage(), filtroEstado != null ? filtroEstado.getValorCadena() : "-1",filtroEstado,false);
			filtroBO.getFiltro(getUsuarioCurrent(), "ID_ACTIVIDAD", getPage(), getParam1Integer());

			actividad = actividadBO.findByPk(getParam1Integer());
			setNombreArchivo("Asistencias-" + actividad.getDescripcion());

			setNivelBloqueo("APL".equals(actividad.getActEstado()) ? 1 : 0);
			redisplayAction(7, !getReadOnly1());
			map = new HashMap<>();
			map.put("filtro", filtro.getValorCadena() != null ? filtro.getValorCadena() : "");
			map.put("asistio", filtroEstado.getValorCadena());

			map.put("actividad", actividad);
			listAsistencias = asistenciaBO.findAllByNamedQuery("Asistencia.findByFilter", map);
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@Override
	public String guardar() {
		try {
			asistenciaBO.actualizarAsistencias(getUsuarioCurrent(), listAsistencias);
			displayMessage(Mensaje.saveMessaje, Mensaje.SEVERITY_INFO);
			return getPage().getOutcome();
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
			return "";
		}
	}

	public void changeName(AjaxBehaviorEvent event) {
		try {
			map = new HashMap<>();
			filtroBO.update(getUsuarioCurrent(), filtro);
			filtroBO.update(getUsuarioCurrent(), filtroEstado);
			map.put("asistio", filtroEstado.getValorCadena());
			map.put("filtro", filtro.getValorCadena() != null ? filtro.getValorCadena() : "");
			map.put("actividad", actividad);
			listAsistencias = asistenciaBO.findAllByNamedQuery("Asistencia.findByFilter", map);
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	public List<SelectItem> getEstado() {
		List<SelectItem> items = new ArrayList<>(0);
		items.add(new SelectItem("-1", "TODOS"));
		items.add(new SelectItem("0", "SI"));
		items.add(new SelectItem("1", "NO"));
		return items;
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public Actividad getActividad() {
		return actividad;
	}

	public void setActividad(Actividad actividad) {
		this.actividad = actividad;
	}

	public List<Asistencia> getListAsistencias() {
		return listAsistencias;
	}

	public void setListAsistencias(List<Asistencia> listAsistencias) {
		this.listAsistencias = listAsistencias;
	}

	public Filtro getFiltroEstado() {
		return filtroEstado;
	}

	public void setFiltroEstado(Filtro filtroEstado) {
		this.filtroEstado = filtroEstado;
	}

}
