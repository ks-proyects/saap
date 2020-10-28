package org.ec.jap.backend.sistema;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.print.PrintService;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.entiti.saap.Parametro;
import org.ec.jap.utilitario.Impresora;

@ManagedBean
@ViewScoped
public class ImpresoraEditBean extends Bean {

	/**
	 * 
	 */

	Parametro parametro;
	Parametro isMatricialParam;

	Boolean isMatricial;

	public ImpresoraEditBean() {
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
			parametro = parametroBO.findByPk("NOMIMPR");
			isMatricialParam = parametroBO.findByPk("ESIMPRMAT");
			isMatricial = "SI".equalsIgnoreCase(isMatricialParam != null ? isMatricialParam.getValorString() : "");
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public String guardar() {
		try {
			isMatricialParam.setValorString(isMatricial ? "SI" : "NO");
			parametroBO.update(getUsuarioCurrent(), isMatricialParam);
			parametroBO.update(getUsuarioCurrent(), parametro);
			displayMessage(Mensaje.saveMessaje, Mensaje.SEVERITY_INFO);
			return getPage().getOutcome();
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
			return "";
		}
	}

	public Parametro getParametro() {
		return parametro;
	}

	public void setParametro(Parametro parametro) {
		this.parametro = parametro;
	}

	public Boolean getIsMatricial() {
		return isMatricial;
	}

	public void setIsMatricial(Boolean isMatricial) {
		this.isMatricial = isMatricial;
	}

	public List<SelectItem> getImpresoras() {
		List<SelectItem> selectItems = new ArrayList<SelectItem>(0);
		try {
			Integer numCopies=parametroBO.getInteger("", getUsuarioCurrent().getIdComunidad().getIdComunidad(), "NUMCOP");
			List<PrintService> list = Impresora.getImpresoras(1);
			selectItems.add(new SelectItem("-1", "SELECCIONE", "SELECCIONE"));
			for (PrintService print : list) {
				selectItems.add(new SelectItem(print.getName(), print.getName(), print.getName()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return selectItems;
	}

}
