/**
 * 
 */
package org.ec.jap.backend.pago;

import java.util.Calendar;
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
import org.ec.jap.bo.saap.CabeceraPlanillaBO;
import org.ec.jap.bo.sistema.FiltroBO;
import org.ec.jap.entiti.saap.CabeceraPlanilla;
import org.ec.jap.entiti.sistema.Filtro;
import org.ec.jap.utilitario.Utilitario;

/**
 * @author Freddy G Castillo C
 * 
 */
@ManagedBean
@ViewScoped
public class PagoListBean extends Bean {

	@EJB
	CabeceraPlanillaBO cabeceraPlanillaBO;

	private List<CabeceraPlanilla> cabeceraPlanillas;
	Filtro filtroCan;
	Filtro filtroFact;
	@EJB
	FiltroBO filtroBO;

	public PagoListBean() {
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

			filtroCan = filtroBO.getFiltro(getUsuarioCurrent(), "FILTRO_CAN", getPage(), filtroCan != null ? filtroCan.getValorEntero() : "", filtroCan, false);
			filtroFact = filtroBO.getFiltro(getUsuarioCurrent(), "FILTRO_FACT", getPage(), filtroFact != null ? filtroFact.getValorCadena() : 0, filtroFact, false);
			setNombreArchivo("Pagos-" + Calendar.getInstance().get(Calendar.YEAR) + "-" + Utilitario.mes(Calendar.getInstance().get(Calendar.MONTH)));
			map = new HashMap<>();
			map.put("filtro", filtroFact.getValorCadena() != null ? filtroFact.getValorCadena() : "%");
			map.put("estado", "CERR");
			cabeceraPlanillas = cabeceraPlanillaBO.findAllByNamedQuery(filtroCan.getValorEntero(), "CabeceraPlanilla.findByPerAbiertActFilters", map);
			Runtime.getRuntime().gc();
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	public void changeName(AjaxBehaviorEvent event) {
		try {
			map.put("filtro", filtroFact.getValorCadena() != null ? filtroFact.getValorCadena() : "%");
			map.put("estado", "CERR");
			cabeceraPlanillas = cabeceraPlanillaBO.findAllByNamedQuery(filtroCan.getValorEntero(), "CabeceraPlanilla.findByPerAbiertActFilters", map);
			Runtime.getRuntime().gc();
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	public List<CabeceraPlanilla> getCabeceraPlanillas() {
		return cabeceraPlanillas;
	}

	public void setCabeceraPlanillas(List<CabeceraPlanilla> cabeceraPlanillas) {
		this.cabeceraPlanillas = cabeceraPlanillas;
	}

	public Filtro getFiltroFact() {
		return filtroFact;
	}

	public void setFiltroFact(Filtro filtroFact) {
		this.filtroFact = filtroFact;
	}

	public Filtro getFiltroCan() {
		return filtroCan;
	}

	public void setFiltroCan(Filtro filtroCan) {
		this.filtroCan = filtroCan;
	}
}
