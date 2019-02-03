/**
 * 
 */
package org.ec.jap.backend.pago;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.saap.ReporteCajaBO;
import org.ec.jap.entiti.sistema.Filtro;

/**
 * @author Freddy G Castillo C
 * 
 */
@ManagedBean
@ViewScoped
public class ReporteCajaTipoRegistroBean extends Bean {
	@EJB
	ReporteCajaBO reporteCajaBO;

	private List<SelectItem> tipoRegistroIngresos;
	protected Filtro tipoRegistro;
	// private String tipoRegistro;
	private Map<String, Double[]> medalsData = new LinkedHashMap<String, Double[]>();
	private ArrayList<String> colors = new ArrayList<String>();
	private ArrayList<String> names = new ArrayList<String>();

	public ReporteCajaTipoRegistroBean() {
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
			setNombreArchivo("ReporteCaja-" + Calendar.getInstance().get(Calendar.YEAR));
			
			tipoRegistro = filtroBO.getFiltro(getUsuarioCurrent(), "FILTRO", getPage(), tipoRegistro != null ? tipoRegistro.getValorCadena() : "0",tipoRegistro,false);
			if (tipoRegistro == null) {

			}
			medalsData.clear();
			medalsData.putAll(reporteCajaBO.getTipoRegistro(tipoRegistro.getValorCadena()));

			names.add("Cobrado/Gastado");
			names.add("Por Cobrar");

			colors.add("#B40404");
			colors.add("#FFBF00");

		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	/**
	 * @return the tipoRegistroIngresos
	 */
	public List<SelectItem> getTipoRegistroIngresos() {
		tipoRegistroIngresos = new ArrayList<>();
		try {
			tipoRegistroIngresos = getSelectItems(getUsuarioCurrent(), null, true, "ListaValor.findTipoRegistro");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tipoRegistroIngresos;
	}

	/**
	 * @param tipoRegistroIngresos
	 *            the tipoRegistroIngresos to set
	 */
	public void setTipoRegistroIngresos(List<SelectItem> tipoRegistroIngresos) {
		this.tipoRegistroIngresos = tipoRegistroIngresos;
	}

	public Map<String, Double[]> getMedalsData() {
		return medalsData;
	}

	public ArrayList<String> getColors() {
		return colors;
	}

	public ArrayList<String> getNames() {
		return names;
	}

	/**
	 * @return the tipoRegistro
	 */
	public Filtro getTipoRegistro() {
		return tipoRegistro;
	}

	/**
	 * @param tipoRegistro
	 *            the tipoRegistro to set
	 */
	public void setTipoRegistro(Filtro tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}
}
