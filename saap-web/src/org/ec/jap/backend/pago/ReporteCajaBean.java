/**
 * 
 */
package org.ec.jap.backend.pago;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.saap.ReporteCajaBO;
import org.ec.jap.entiti.sistema.Filtro;
import org.ec.jap.enumerations.TipoListaValor;
import org.ec.jap.utilitario.Utilitario;
import org.ec.jap.xmlaccessortype.Pie;

/**
 * @author Freddy G Castillo C
 * 
 */
@ManagedBean
@ViewScoped
public class ReporteCajaBean extends Bean {

	@EJB
	ReporteCajaBO reporteCajaBO;

	// private Integer anio;
	private Filtro anio;
	// private Integer mes;
	private Filtro mes;
	private List<SelectItem> items;

	public ReporteCajaBean() {
		super();
	}

	@PostConstruct
	public void init() {
		try {
			super.init();
			items = new ArrayList<>(0);
			items.add(new SelectItem(-1, "Todos", "Todos"));
			search(null);
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	public void search(ActionEvent event) {
		try {
			setNombreArchivo("ReporteCajaAnual-" + Calendar.getInstance().get(Calendar.YEAR));
			mes = filtroBO.getFiltro(getUsuarioCurrent(), "MES", getPage(), mes != null ? mes.getValorEntero() : -1, mes, false);
			anio = filtroBO.getFiltro(getUsuarioCurrent(), "ANIO", getPage(), anio != null ? anio.getValorEntero() : 0, anio, false);
			if (anio.getValorEntero() != 0) {
				changeAnio(null);
			}

			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			String webRoot = servletContext.getRealPath("/");
			String pathReal = webRoot + "/tools/swf/ingresos/ampie_data.xml";
			Pie pie = reporteCajaBO.getReporteIngresos(anio.getValorEntero(), mes.getValorEntero());
			Utilitario.generarXMLPie(pathReal, pie);

			pathReal = webRoot + "/tools/swf/salidas/ampie_data.xml";
			pie = reporteCajaBO.getReporteSalidas(anio.getValorEntero(), mes.getValorEntero());
			Utilitario.generarXMLPie(pathReal, pie);

			pathReal = webRoot + "/tools/swf/total/ampie_data.xml";
			pie = reporteCajaBO.getReporteSalidasIngresos(anio.getValorEntero(), mes.getValorEntero());
			Utilitario.generarXMLPie(pathReal, pie);

		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	/**
	 * @return the anio
	 */
	public Filtro getAnio() {
		return anio;
	}

	/**
	 * @param anio
	 *            the anio to set
	 */
	public void setAnio(Filtro anio) {
		this.anio = anio;
	}

	/**
	 * @return the mes
	 */
	public Filtro getMes() {
		return mes;
	}

	/**
	 * @param mes
	 *            the mes to set
	 */
	public void setMes(Filtro mes) {
		this.mes = mes;
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

	public void changeAnio(AjaxBehaviorEvent event) {
		items = new ArrayList<>();
		try {
			items.add(new SelectItem(-1, "Todos", "Todos"));
			if (anio.getValorEntero() != 0) {
				items.addAll(getSelectItems(getUsuarioCurrent(), null, "MESES", false, false, false, TipoListaValor.F, null));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<SelectItem> getMeses() {
		return items;
	}

}
