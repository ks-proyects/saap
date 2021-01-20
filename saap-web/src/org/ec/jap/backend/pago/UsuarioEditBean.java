package org.ec.jap.backend.pago;

import java.util.ArrayList;
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
import org.ec.jap.bo.saap.EstadoCivilBO;
import org.ec.jap.bo.saap.LlaveBO;
import org.ec.jap.bo.saap.RepresentanteBO;
import org.ec.jap.bo.saap.TarifaBO;
import org.ec.jap.bo.saap.UsuarioBO;
import org.ec.jap.bo.sistema.ComunidadBO;
import org.ec.jap.entiti.saap.Llave;
import org.ec.jap.entiti.saap.Representante;
import org.ec.jap.entiti.saap.Usuario;
import org.ec.jap.utilitario.UtilitarioFecha;

@ManagedBean
@ViewScoped
public class UsuarioEditBean extends Bean {

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
	LlaveBO llaveBO;

	@EJB
	TarifaBO tarifaBO;

	@EJB
	RepresentanteBO representanteBO;

	private Usuario usuario;
	private Integer idEstadoCivil;
	private Integer idTarifa;
	private List<Llave> listLlaves;
	private List<Representante> listRepresentantes;

	public UsuarioEditBean() {
		super();
	}

	@PostConstruct
	@Override
	public void init() {
		try {
			setTipoEntidad("USU");
			super.init();
			search(null);
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
				idTarifa = usuario.getTarifa()!=null?usuario.getTarifa().getIdTarifa():-1;
				map = new HashMap<>();
				map.put("idUsuario", usuario.getIdUsuario());
				listLlaves = llaveBO.findAllByNamedQuery("Llave.findByUser", map);
				listRepresentantes = representanteBO.findAllByNamedQuery("Representante.findRepresentado", map);
				redisplayAction(2, "ING".equals(usuario.getEstado()));// Representante
				redisplayAction(11, "ACT".equals(usuario.getEstado()));// Llave
			}
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@Override
	public String generalAccion() {
		// TODO Auto-generated method stub
		try {
			Integer numMaxLlaves = parametroBO.getInteger("", getUsuarioCurrent().getIdComunidad().getIdComunidad(),
					"NUMLLAV");
			if (listLlaves.size() >= numMaxLlaves) {
				displayMessage("El número de llaves permitidas por usuario es: " + numMaxLlaves.toString()
						+ ". No puede asignar más llaves.", Mensaje.SEVERITY_WARN);
				return getPage().getNombre();
			} else {
				setAccion("INS");
				return "llaveInsert?faces-redirect=true";
			}
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
			return getPage().getNombre();
		}

	}

	@Override
	public String insert() {
		this.setAccion("INS");
		return "representanteEdit?faces-redirect=true";
	}

	@Override
	public String guardar() {
		try {
			usuario.setTarifa(tarifaBO.findByPk(idTarifa));
			usuario.setEdad(UtilitarioFecha.getEdad(usuario.getFechaNacimiento()));
			usuario.setIdEstadoCivil(estadoCivilBO.findByPk(idEstadoCivil));
			if ("INS".equals(getAccion())) {
				map = new HashMap<>();
				usuario.setTipoUsuario(getParam20String());
				map.put("cedula", usuario.getCedula());

				Integer numReg = ((Long) representanteBO.findObjectByNamedQuery("Representante.findByCed", map))
						.intValue();
				if (numReg > 0) {
					displayMessage("Esta persona no puede ser un representante y un usuario de la JAAP a la misma vez.",
							Mensaje.SEVERITY_WARN);
					return "";
				}

				map = new HashMap<>();
				map.put("tipoUsuario", usuario.getTipoUsuario());
				map.put("cedula", usuario.getCedula());
				numReg = ((Long) usuarioBO.findObjectByNamedQuery("Usuario.findByCedula", map)).intValue();
				if (numReg > 0) {
					displayMessage("Ya existe una persona con este número de cédula como un usuario de la JAAP.",
							Mensaje.SEVERITY_WARN);
					return "";
				}

				usuario.setEstado("ING");
				usuario.setUsername(usuario.getNombres().toLowerCase().replace(" ", ""));
				usuario.setIdComunidad(comunidadBO.findByPk(1));
				usuario.setFechaIngreso(Calendar.getInstance().getTime());
				usuarioBO.save(getUsuarioCurrent(), usuario);
				setParam1(usuario.getIdUsuario());
				setAccion("UPD");
				cambioEstadoBO.cambiarEstadoSinVerificar(12, getUsuarioCurrent(), usuario.getIdUsuario(), "");

			} else {
				map = new HashMap<>();
				map.put("tipoUsuario", usuario.getTipoUsuario());
				map.put("cedula", usuario.getCedula());
				map.put("idUsuario", usuario.getIdUsuario());
				Integer numReg = ((Long) usuarioBO.findObjectByNamedQuery("Usuario.findByCedulaAndId", map)).intValue();
				if (numReg > 0) {
					displayMessage("Ya existe una persona con este número de cédula como un usuario de la JAAP.",
							Mensaje.SEVERITY_WARN);
					return "";
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

	public List<SelectItem> getTarifas() {
		HashMap<String, Object> map = new HashMap<>(0);
		try {
			return getSelectItems(getUsuarioCurrent(), map, "ListaValor.findTarifaConsu");
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

	public List<Llave> getListLlaves() {
		return listLlaves;
	}

	public void setListLlaves(List<Llave> listLlaves) {
		this.listLlaves = listLlaves;
	}

	public List<Representante> getListRepresentantes() {
		return listRepresentantes;
	}

	public void setListRepresentantes(List<Representante> listRepresentantes) {
		this.listRepresentantes = listRepresentantes;
	}

	public Integer getIdTarifa() {
		return idTarifa;
	}

	public void setIdTarifa(Integer idTarifa) {
		this.idTarifa = idTarifa;
	}
	

}
