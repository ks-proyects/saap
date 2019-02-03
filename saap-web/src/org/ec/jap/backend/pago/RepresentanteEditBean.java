package org.ec.jap.backend.pago;

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
import org.ec.jap.bo.saap.LlaveBO;
import org.ec.jap.bo.saap.ParentescoBO;
import org.ec.jap.bo.saap.RepresentanteBO;
import org.ec.jap.bo.saap.UsuarioBO;
import org.ec.jap.bo.sistema.ComunidadBO;
import org.ec.jap.entiti.saap.Representante;
import org.ec.jap.entiti.saap.Usuario;
import org.ec.jap.utilitario.UtilitarioFecha;

@ManagedBean
@ViewScoped
public class RepresentanteEditBean extends Bean {

	/**
	 * param1->idUsuario param2->idParentesco
	 */

	@EJB
	UsuarioBO usuarioBO;

	@EJB
	ComunidadBO comunidadBO;

	@EJB
	EstadoCivilBO estadoCivilBO;

	@EJB
	LlaveBO llaveBO;

	@EJB
	RepresentanteBO representanteBO;
	
	@EJB
	ParentescoBO parentescoBO;

	private Usuario usuario;
	private Integer idEstadoCivil;
	private Integer idParentesco;
	private Representante representante;

	public RepresentanteEditBean() {
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
				representante= new Representante();
				usuario = new Usuario();
			} else {
				representante=representanteBO.findByPk(getParam2Integer());
				idParentesco=representante.getIdParentesco().getIdParentesco();
				usuario = representante.getIdUsuario();
				idEstadoCivil=usuario.getIdEstadoCivil().getIdEstadoCivil();
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
			representante.setIdParentesco(parentescoBO.findByPk(idParentesco));
			if ("INS".equals(getAccion())) {
				map= new HashMap<>();
				map.put("cedula", usuario.getCedula());
				map.put("tipoUsuario", usuario.getTipoUsuario());
				Integer numReg=((Long)usuarioBO.findObjectByNamedQuery("Usuario.findByCedula", map)).intValue();
				if(numReg>0)
				{
					displayMessage("Esta persona no puede ser un representante y un usuario de la JAAP a la misma vez.", Mensaje.SEVERITY_WARN);
					return "";
				}
				
				map= new HashMap<>();
				map.put("cedula", usuario.getCedula());
				numReg=((Long)representanteBO.findObjectByNamedQuery("Representante.findByCed", map)).intValue();
				if(numReg>0)
				{
					displayMessage("No puede ser una misma persona representante de dos Usuarios de la JAAP.", Mensaje.SEVERITY_WARN);
					return "";
				}
				
				usuario.setEstado("ING");
				usuario.setIdComunidad(comunidadBO.findByPk(1));
				usuarioBO.save(getUsuarioCurrent(), usuario);
				representante.setIdRepresentado(usuarioBO.findByPk(getParam1Integer()));
				representante.setIdUsuario(usuario);
				representanteBO.save(getUsuarioCurrent(), representante);
				setParam2(representante.getIdRepresentante());
				setAccion("UPD");
			} else {
				usuarioBO.update(getUsuarioCurrent(), usuario);
				representanteBO.update(getUsuarioCurrent(),representante);
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
			representanteBO.delete(getUsuarioCurrent(), representante);
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
	public List<SelectItem> getItemParentesco() {
		HashMap<String, Object> map = new HashMap<>(0);
		try {
			return getSelectItems(getUsuarioCurrent(), map, "ListaValor.findParentesco");
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

	public Integer getIdParentesco() {
		return idParentesco;
	}

	public void setIdParentesco(Integer idParentesco) {
		this.idParentesco = idParentesco;
	}

	public Representante getRepresentante() {
		return representante;
	}

	public void setRepresentante(Representante representante) {
		this.representante = representante;
	}
	

}
