package org.ec.jap.backend.sistema;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.sistema.RolBO;
import org.ec.jap.entiti.sistema.Rol;
import org.ec.jap.enumerations.ValorSiNo;

@ManagedBean
@ViewScoped
public class RolEditBean extends Bean {

	/**
	 * 
	 */

	Rol rol;

	@EJB
	RolBO rolBO;

	public RolEditBean() {
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
				rol = new Rol();
			} else {
				rol = rolBO.findByPk(getParam1Integer());
			}

		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@Override
	public String delete() {
		try {
			map.clear();
			map.put("idRol", rol);
			Integer usuarios = rolBO.findIntegerByNamedQuery("Rol.findUser", map);
			if (usuarios > 0)
				throw new Exception("Existen usuarios configurados con este rol, no se puede eliminar.");
			Integer acciones = rolBO.findIntegerByNamedQuery("Rol.findAccion", map);
			if (acciones > 0)
				throw new Exception("Existen acciones configurados con este rol, no se puede eliminar.");
			rolBO.delete(getUsuarioCurrent(), rol);
			return getLastPage().getOutcome();
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
			return "";
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public String guardar() {
		try {
			if ("INS".equals(getAccion())) {
				rolBO.save(getUsuarioCurrent(), rol);
				setAccion("UP");
				setParam1(rol.getIdRol());
			} else {
				rolBO.update(getUsuarioCurrent(), rol);
			}
			displayMessage(Mensaje.saveMessaje, Mensaje.SEVERITY_INFO);
			return getPage().getOutcome();
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
			return "";
		}
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public List<SelectItem> getEstados() {
		try {
			return getSelectItems(ValorSiNo.values());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
