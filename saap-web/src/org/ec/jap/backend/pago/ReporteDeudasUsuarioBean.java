/**
 * 
 */
package org.ec.jap.backend.pago;

import java.util.ArrayList;
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

/**
 * @author Freddy G Castillo C
 * 
 */
@ManagedBean
@ViewScoped
public class ReporteDeudasUsuarioBean extends Bean {

	@EJB
	ReporteCajaBO reporteCajaBO;

	private List<SelectItem> tipoRegistroIngresos;
	private String tipoRegistro;
	private Map<String, Double[]> medalsData = new LinkedHashMap<String, Double[]>();
	private ArrayList<String> colors = new ArrayList<String>();
	private ArrayList<String> names = new ArrayList<String>();

	private Integer[] medalsChina = new Integer[] { 51, 21 };
	private Integer[] medalsUSA = new Integer[] { 36, 38 };
	private Integer[] medalsRussia = new Integer[] { 23, 21 };

	public ReporteDeudasUsuarioBean() {
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

			if (tipoRegistro == null) {

			}
			medalsData.putAll(reporteCajaBO.getTipoRegistro(tipoRegistro));

			names.add("Cobrado/Gastado");
			names.add("Por Cobrar");

			colors.add("#B40404");
			colors.add("#2E64FE");

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

	public Integer[] getMedalsChina() {
		return medalsChina;
	}

	public Integer[] getMedalsUSA() {
		return medalsUSA;
	}

	public Integer[] getMedalsRussia() {
		return medalsRussia;
	}

	/**
	 * @return the tipoRegistro
	 */
	public String getTipoRegistro() {
		return tipoRegistro;
	}

	/**
	 * @param tipoRegistro
	 *            the tipoRegistro to set
	 */
	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

}
