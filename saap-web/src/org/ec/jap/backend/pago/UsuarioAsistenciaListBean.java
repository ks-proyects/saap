/**
 * 
 */
package org.ec.jap.backend.pago;

import java.util.ArrayList;
import java.util.Calendar;
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
import org.ec.jap.bo.saap.UsuarioBO;
import org.ec.jap.bo.sistema.FiltroBO;
import org.ec.jap.entiti.saap.Usuario;

/**
 * @author Freddy G Castillo C
 * 
 */
@ManagedBean
@ViewScoped
public class UsuarioAsistenciaListBean extends Bean {

	/**
	 * 
	 */
	@EJB
	UsuarioBO usuarioBO;
	@EJB
	FiltroBO filtroBO;

	private int anio;
	private int idPeriodo;
	private int idActividad;
	private int idTipoActividad;

	private List<Usuario> listUsuarios = new ArrayList<>();

	public UsuarioAsistenciaListBean() {
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

	@Override
	public void search(ActionEvent event) {
		try {
			setNombreArchivo("Rayas-" + Calendar.getInstance().get(Calendar.YEAR));
			filtroBO.getFiltro(getUsuarioCurrent(), "TIPO_USUARIO", getPage(), getParam20String());
			anio = filtroBO.getFiltro(getUsuarioCurrent(), "ANIO", getPage(), anio).getValorEntero();
			idPeriodo = filtroBO.getFiltro(getUsuarioCurrent(), "ID_PERIODO", getPage(), idPeriodo).getValorEntero();
			idActividad = filtroBO.getFiltro(getUsuarioCurrent(), "ID_ACTIVIDAD", getPage(), idActividad).getValorEntero();
			idTipoActividad = filtroBO.getFiltro(getUsuarioCurrent(), "ID_TIPO_ACTIVIDAD", getPage(), idTipoActividad).getValorEntero();

			map = new HashMap<>();
			map.put("tipoUsuario", getParam20String());
			map.put("anio", anio);
			map.put("idPeriodoPago", idPeriodo);
			map.put("actividad", idActividad);
			map.put("tipoActividad", idTipoActividad);
			listUsuarios = usuarioBO.findBenefeciariosAsistencias("Usuario.findByFilters", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void changeName(AjaxBehaviorEvent event) {
		try {

			filtroBO.getFiltro(getUsuarioCurrent(), "TIPO_USUARIO", getPage(), getParam20String());
			filtroBO.update(getUsuarioCurrent(), filtroBO.getFiltro(getUsuarioCurrent(), "ANIO", getPage(), anio));
			filtroBO.update(getUsuarioCurrent(), filtroBO.getFiltro(getUsuarioCurrent(), "ID_PERIODO", getPage(), idPeriodo));
			filtroBO.update(getUsuarioCurrent(), filtroBO.getFiltro(getUsuarioCurrent(), "ID_ACTIVIDAD", getPage(), idActividad));
			filtroBO.update(getUsuarioCurrent(), filtroBO.getFiltro(getUsuarioCurrent(), "ID_TIPO_ACTIVIDAD", getPage(), idTipoActividad));

			map = new HashMap<>();
			map.put("tipoUsuario", getParam20String());
			map.put("anio", anio);
			map.put("idPeriodoPago", idPeriodo);
			map.put("actividad", idActividad);
			map.put("tipoActividad", idTipoActividad);

			listUsuarios = usuarioBO.findBenefeciariosAsistencias("Usuario.findByFilters", map);
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	public List<Usuario> getListUsuarios() {
		return listUsuarios;
	}

	public void setListUsuarios(List<Usuario> listUsuarios) {
		this.listUsuarios = listUsuarios;
	}

	public List<SelectItem> getAnios() {
		List<SelectItem> items = new ArrayList<>();
		try {
			items = getSelectItems(getUsuarioCurrent(), null, true, "ListaValor.findAnioPeriodo");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	public List<SelectItem> getTipoActividad() {
		List<SelectItem> items = new ArrayList<>();
		try {
			items = getSelectItems(getUsuarioCurrent(), null, true, "ListaValor.findTipoActividad");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	public List<SelectItem> getPeriodos() {
		List<SelectItem> items = new ArrayList<>();
		try {
			map.clear();
			map.put("anio", anio);
			items = getSelectItems(getUsuarioCurrent(), map, true, "ListaValor.findPeriodoByAnio");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	public List<SelectItem> getActividad() {
		List<SelectItem> items = new ArrayList<>();
		try {
			map.clear();
			map.put("idPeriodoPago", idPeriodo);
			map.put("tipoActividad", idTipoActividad);
			items = getSelectItems(getUsuarioCurrent(), map, true, "ListaValor.findActividadByTipoAndPeriodo");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	public int getAnio() {
		return anio;
	}

	public void setAnio(int anio) {
		this.anio = anio;
	}

	public int getIdPeriodo() {
		return idPeriodo;
	}

	public void setIdPeriodo(int idPeriodo) {
		this.idPeriodo = idPeriodo;
	}

	public int getIdActividad() {
		return idActividad;
	}

	public void setIdActividad(int idActividad) {
		this.idActividad = idActividad;
	}

	public int getIdTipoActividad() {
		return idTipoActividad;
	}

	public void setIdTipoActividad(int idTipoActividad) {
		this.idTipoActividad = idTipoActividad;
	}

}
