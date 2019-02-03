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
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.saap.GastoBO;
import org.ec.jap.entiti.saap.Gasto;

/**
 * @author Freddy G Castillo C
 * 
 */
@ManagedBean
@ViewScoped
public class GastoListBean extends Bean {

	@EJB
	GastoBO gastoBO;

	private Integer idPeriodopago;

	private List<SelectItem> periodos;

	private List<Gasto> listGastos;

	public GastoListBean() {
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
			periodos = getSelectItems(getUsuarioCurrent(), null, "ListaValor.findNoEsta");
			map.clear();
			map.put("idPeriodoPago", idPeriodopago != null ? idPeriodopago : 0);
			listGastos = gastoBO.findAllByNamedQuery("Gasto.findAllByUser", map);
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@Override
	public String insert() {
		setAccion("INS");
		return "gastoEdit?faces-redirect=true";
	}

	public void valueChange(ValueChangeEvent event) {
		try {
			idPeriodopago = event == null ? 0 : Integer.valueOf(event.getNewValue() != null ? event.getNewValue().toString() : "0");
			map = new HashMap<>();
			map.put("idPeriodoPago", idPeriodopago != null ? idPeriodopago : 0);
			listGastos = gastoBO.findAllByNamedQuery("Gasto.findAllByUser", map);
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	public Integer getIdPeriodopago() {
		return idPeriodopago;
	}

	public void setIdPeriodopago(Integer idPeriodopago) {
		this.idPeriodopago = idPeriodopago;
	}

	public List<SelectItem> getPeriodos() {
		return periodos;
	}

	public void setPeriodos(List<SelectItem> periodos) {
		this.periodos = periodos;
	}

	public List<Gasto> getListGastos() {
		return listGastos;
	}

	public void setListGastos(List<Gasto> listGastos) {
		this.listGastos = listGastos;
	}

}
