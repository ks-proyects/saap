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
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.saap.ReporteCajaBO;
import org.ec.jap.entiti.dto.ConsumoDTO;
import org.ec.jap.entiti.sistema.Filtro;

/**
 * @author Freddy G Castillo C
 * 
 */
@ManagedBean
@ViewScoped
public class ReporteConsumoBean extends Bean {

	@EJB
	ReporteCajaBO reporteCajaBO;

	// private Integer anio;
	private Filtro anio;
	private Filtro tb;
	private List<SelectItem> items;
	private List<ConsumoDTO> consumoDTOs;

	public ReporteConsumoBean() {
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
			setNombreArchivo("ReporteConsumo-" + Calendar.getInstance().get(Calendar.YEAR));
			anio = filtroBO.getFiltro(getUsuarioCurrent(), "ANIO", getPage(), anio != null ? anio.getValorEntero() : 0, anio, false);
			tb = filtroBO.getFiltro(getUsuarioCurrent(), "TARIFA", getPage(), tb != null ? anio.getValorEntero() : 0, tb, false);
			consumoDTOs = reporteCajaBO.getReporteConsumo(anio.getValorEntero(),tb.getValorEntero());
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

	
	public Filtro getTb() {
		return tb;
	}

	public void setTb(Filtro tb) {
		this.tb = tb;
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
	public List<SelectItem> getTipoBeneficiario() {
		List<SelectItem> items = new ArrayList<>();
		try {
			items = getSelectItems(getUsuarioCurrent(), null, true, "ListaValor.findTarifaConsu");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	public List<SelectItem> getMeses() {
		return items;
	}

	public List<ConsumoDTO> getConsumoDTOs() {
		return consumoDTOs;
	}

	public void setConsumoDTOs(List<ConsumoDTO> consumoDTOs) {
		this.consumoDTOs = consumoDTOs;
	}
	

}
