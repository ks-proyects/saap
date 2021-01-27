package org.ec.jap.backend.sistema;

import java.util.ArrayList;
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
import org.ec.jap.bo.saap.EstadoCivilBO;
import org.ec.jap.bo.saap.ServicioBO;
import org.ec.jap.bo.saap.RepresentanteBO;
import org.ec.jap.bo.saap.UsuarioBO;
import org.ec.jap.bo.sistema.ComunidadBO;
import org.ec.jap.entiti.saap.Servicio;
import org.ec.jap.entiti.saap.Representante;
import org.ec.jap.entiti.saap.Usuario;
import org.ec.jap.utilitario.Utilitario;
import org.ec.jap.utilitario.UtilitarioFecha;

@ManagedBean
@ViewScoped
public class UsuarioSEditBean extends Bean {

	/**
	 * param1->idUsuario
	 */

	@EJB
	UsuarioBO usuarioBO;

	@EJB
	ComunidadBO comunidadBO;

	@EJB
	EstadoCivilBO estadoCivilBO;

	@EJB
	ServicioBO llaveBO;

	@EJB
	RepresentanteBO representanteBO;

	private Usuario usuario;
	private Integer idEstadoCivil;
	private List<Servicio> listLlaves;
	private List<Representante> listRepresentantes;

	private Boolean resetearClave;

	public UsuarioSEditBean() {
		super();
	}

	@PostConstruct
	@Override
	public void init() {
		try {
			setTipoEntidad("USU");
			super.init();
			search(null);
			setIdPage(21);
			redisplayActions(usuario.getEstado(), usuario.getIdUsuario());
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}

	}

	@Override
	public void search(ActionEvent event) {
		try {

			if ("INS".equals(getAccion())) {
				usuario = new Usuario();
			} else {
				usuario = usuarioBO.findByPk(getParam1Integer());
			}
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@Override
	public String guardar() {
		try {
			usuario.setEdad(UtilitarioFecha.getEdad(usuario.getFechaNacimiento()));
			usuario.setIdEstadoCivil(estadoCivilBO.findByPk(idEstadoCivil));
			if ("INS".equals(getAccion())) {
				usuario.setTipoUsuario("SIS");
				map = new HashMap<>();
				map.put("cedula", usuario.getCedula());
				map.put("tipoUsuario", usuario.getTipoUsuario());
				int numReg = ((Long) usuarioBO.findObjectByNamedQuery("Usuario.findByCedula", map)).intValue();
				if (numReg > 0) {
					displayMessage("Ya existe una persona con este número de cédula como un usuario del Sistema.", Mensaje.SEVERITY_WARN);
					return "";
				}

				usuario.setEstado("ING");

				usuario.setUsername(usuario.getNombres().toLowerCase().replace(" ", ""));
				usuario.setIdComunidad(comunidadBO.findByPk(1));
				usuario.setPassword(Utilitario.getMD5_Base64(usuario.getUsername()));
				usuarioBO.save(getUsuarioCurrent(), usuario);
				setParam1(usuario.getIdUsuario());
				setAccion("UPD");
				cambioEstadoBO.cambiarEstadoSinVerificar(12, getUsuarioCurrent(), usuario.getIdUsuario(), "");

			} else {
				if (resetearClave) {
					usuario.setUsername(usuario.getNombres().toLowerCase().replace(" ", ""));
					usuario.setPassword(Utilitario.getMD5_Base64(usuario.getUsername()));
				}
				usuarioBO.update(getUsuarioCurrent(), usuario);
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
			usuarioBO.delete(getUsuarioCurrent(), usuario);
			displayMessage(Mensaje.deleteMessaje, Mensaje.SEVERITY_INFO);
			return getLastPage().getOutcome() + "?faces-redirect=true";
		} catch (Exception e) {
			displayMessage(Mensaje.errorMessaje, Mensaje.SEVERITY_FATAL);
			return getPage().getOutcome();
		}

	}

	public List<SelectItem> getItemEstadoCicils() {
		HashMap<String, Object> map = new HashMap<>(0);
		try {
			return getSelectItems(getUsuarioCurrent(), map, "ListaValor.findEstadoCivil");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<SelectItem>(0);
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Integer getIdEstadoCivil() {
		return idEstadoCivil;
	}

	public void setIdEstadoCivil(Integer idEstadoCivil) {
		this.idEstadoCivil = idEstadoCivil;
	}

	public List<Servicio> getListLlaves() {
		return listLlaves;
	}

	public void setListLlaves(List<Servicio> listLlaves) {
		this.listLlaves = listLlaves;
	}

	public List<Representante> getListRepresentantes() {
		return listRepresentantes;
	}

	public void setListRepresentantes(List<Representante> listRepresentantes) {
		this.listRepresentantes = listRepresentantes;
	}

	public Boolean getResetearClave() {
		return resetearClave;
	}

	public void setResetearClave(Boolean resetearClave) {
		this.resetearClave = resetearClave;
	}

}
