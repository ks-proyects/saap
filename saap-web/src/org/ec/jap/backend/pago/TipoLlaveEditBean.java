package org.ec.jap.backend.pago;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.saap.TipoLlaveBO;
import org.ec.jap.entiti.saap.TipoLlave;

@ManagedBean
@ViewScoped
public class TipoLlaveEditBean extends Bean {

	/**
	 * param1->idUsuario
	 */

	@EJB
	TipoLlaveBO tipoLlaveBO;

	private TipoLlave tipoLlave;

	public TipoLlaveEditBean() {
		super();
	}

	@PostConstruct
	@Override
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
			if ("INS".equals(getAccion())) {
				tipoLlave = new TipoLlave();
			} else {
				setNivelBloqueo(1);
				tipoLlave = tipoLlaveBO.findByPk(getParam1String());
			}
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@Override
	public String guardar() {
		try {
			if ("INS".equals(getAccion())) {
				TipoLlave tipoLlaveAux = tipoLlaveBO.findByPk(this.tipoLlave.getTipoLlave());
				if (tipoLlaveAux != null)
					throw new Exception("Ya existe un tipo de llave con el mismo CÓDIGO.");
				tipoLlaveBO.save(getUsuarioCurrent(), this.tipoLlave);
				setAccion("UPD");
				setParam1(this.tipoLlave.getTipoLlave());
			} else {
				tipoLlaveBO.update(getUsuarioCurrent(), tipoLlave);
			}
			displayMessage(Mensaje.saveMessaje, Mensaje.SEVERITY_INFO);
			return getPage().getOutcome();
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
			return getPage().getOutcome();
		}
	}

	@Override
	public String delete() {
		try {
			map.clear();
			map.put("tipoLlave", tipoLlave);
			Integer num = tipoLlaveBO.findIntegerByNamedQuery("TipoLlave.findParametros", map);
			if (num > 0)
				throw new Exception("Existen parametros configurados para este tipo de llave, no puede eliminar");
			num = tipoLlaveBO.findIntegerByNamedQuery("TipoLlave.findTarifas", map);
			if (num > 0)
				throw new Exception("Existen tarifas configurados para este tipo de llave, no puede eliminar");

			tipoLlaveBO.delete(getUsuarioCurrent(), tipoLlave);
			displayMessage(Mensaje.deleteMessaje, Mensaje.SEVERITY_INFO);
			return getLastPage().getOutcome() + "?faces-redirect=true";
		} catch (Exception e) {
			displayMessage(e.getMessage(), Mensaje.SEVERITY_FATAL);
			return getPage().getOutcome();
		}
	}

	public TipoLlave getTipoLlave() {
		return tipoLlave;
	}

	public void setTipoLlave(TipoLlave tipoLlave) {
		this.tipoLlave = tipoLlave;
	}

}
