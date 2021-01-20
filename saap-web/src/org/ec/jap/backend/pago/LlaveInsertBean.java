package org.ec.jap.backend.pago;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.saap.LlaveBO;
import org.ec.jap.bo.saap.UsuarioBO;
import org.ec.jap.entiti.saap.Llave;
import org.ec.jap.entiti.saap.Usuario;

@ManagedBean
@ViewScoped
public class LlaveInsertBean extends Bean {

	/**
	 * 
	 */

	@EJB
	UsuarioBO usuarioBO;

	@EJB
	LlaveBO llaveBO;

	private Llave llave;

	private Usuario usuario;
	private List<SelectItem> itemTarifa;
	private List<SelectItem> itemTipoLlave;

	public LlaveInsertBean() {
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
			itemTipoLlave = getSelectItems(getUsuarioCurrent(), null, "ListaValor.findTipoLlave");
			if ("INS".equals(getAccion())) {
				llave = new Llave();
				usuario = usuarioBO.findByPk(getParam1Integer());

			} else {
				llave = llaveBO.findByPk(getParam2Integer());
				usuario = llave.getIdUsuario();
			}
			setNivelBloqueo("ING".equals(usuario.getEstado()) || "EDI".equals(usuario.getEstado()) ? 1 : 0);

		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@Override
	public String guardar() {
		try {

			if ("INS".equals(getAccion())) {
				llave.setEstado("ING");
				llave.setFechaRegistro(Calendar.getInstance().getTime());
				llave.setIdUsuario(usuario);
				llaveBO.save(getUsuarioCurrent(), llave);
				setParam2(llave.getIdLlave());
				setAccion("UPD");
			} else {
				llaveBO.update(getUsuarioCurrent(), llave);
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
		// TODO Auto-generated method stub
		try {
			llaveBO.delete(getUsuarioCurrent(), llave);
			displayMessage(Mensaje.deleteMessaje, Mensaje.SEVERITY_INFO);
			return getLastPage().getOutcome() + "?faces-redirect=true";
		} catch (Exception e) {
			displayMessage(Mensaje.errorMessaje, Mensaje.SEVERITY_FATAL);
			return getPage().getOutcome();
		}

	}

	@Override
	public String nuevo() {
		// TODO Auto-generated method stub
		try {
			map = new HashMap<>();
			map.put("idUsuario", usuarioBO.findByPk(getParam1Integer()).getIdUsuario());
			List<Llave> listLlaves = llaveBO.findAllByNamedQuery("Llave.findByUser", map);
			Integer numMaxLlaves = parametroBO.getInteger("", getUsuarioCurrent().getIdComunidad().getIdComunidad(),
					"NUMLLAV");
			if (listLlaves.size() >= numMaxLlaves) {
				displayMessage("El número de llaves permitidas por usuario es: " + numMaxLlaves.toString()
						+ ". No puede asignar más llaves.", Mensaje.SEVERITY_WARN);
				return getPage().getNombre();
			} else {
				return super.nuevo();
			}

		} catch (Exception e) {
			displayMessage(Mensaje.errorMessaje, Mensaje.SEVERITY_FATAL);
			return getPage().getOutcome();
		}
	}

	public List<SelectItem> getItemTipoLlave() {
		return itemTipoLlave;
	}

	public void setItemTipoLlave(List<SelectItem> itemTipoLlave) {
		this.itemTipoLlave = itemTipoLlave;
	}

	public List<SelectItem> getItemTarifa() {
		return itemTarifa;
	}

	public void setItemTarifa(List<SelectItem> itemTarifa) {
		this.itemTarifa = itemTarifa;
	}

	public Llave getLlave() {
		return llave;
	}

	public void setLlave(Llave llave) {
		this.llave = llave;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
