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
import org.ec.jap.bo.saap.ServicioBO;
import org.ec.jap.bo.saap.TarifaBO;
import org.ec.jap.bo.saap.UsuarioBO;
import org.ec.jap.entiti.saap.Servicio;
import org.ec.jap.entiti.saap.Usuario;
import org.ec.jap.enumerations.TipoServicioEnum;

@ManagedBean
@ViewScoped
public class LlaveInsertBean extends Bean {

	/**
	 * 
	 */
	@EJB
	TarifaBO tarifaBO;

	@EJB
	UsuarioBO usuarioBO;

	@EJB
	ServicioBO llaveBO;

	private Servicio llave;
	private Integer idTarifa;
	private Integer idServicio;
	private Boolean nuevo;

	private Usuario usuario;
	private List<SelectItem> itemTarifa;
	private List<SelectItem> itemTipoLlave;
	private List<Servicio> listLlaves;

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
			nuevo = true;
			if ("INS".equals(getAccion())) {
				llave = new Servicio();
				usuario = usuarioBO.findByPk(getParam1Integer());
			} else {
				llave = llaveBO.findByPk(getParam2Integer());
				usuario = llave.getIdUsuario();
				idTarifa = llave.getIdTarifa() != null ? llave.getIdTarifa().getIdTarifa() : -1;
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

			if (nuevo) {
				llave.setIdTarifa(tarifaBO.findByPk(idTarifa));
				if ("INS".equals(getAccion())) {
					map = new HashMap<>();
					map.put("idUsuario", usuario.getIdUsuario());
					map.put("tipoServicio", llave.getTipoServicio());
					listLlaves = llaveBO.findAllByNamedQuery("Servicio.findByUserAndType", map);
					Integer numMaxServicio = parametroBO.getInteger("",
							getUsuarioCurrent().getIdComunidad().getIdComunidad(), "NUMLLAV");
					if (listLlaves.size() >= numMaxServicio) {
						displayMessage("El número de " + llave.getTipoServicio().getDescripcion()
								+ " permitidas por usuario es: " + numMaxServicio.toString()
								+ ". No puede asignar más llaves.", Mensaje.SEVERITY_WARN);
						return getPage().getNombre();
					}
					llave.setEstado("ING");
					llave.setFechaRegistro(Calendar.getInstance().getTime());
					llave.setIdUsuario(usuario);
					llaveBO.save(getUsuarioCurrent(), llave);
					setParam2(llave.getIdServicio());
					setAccion("UPD");
				} else {
					llaveBO.update(getUsuarioCurrent(), llave);
				}
			} else {
				llave = llaveBO.findByPk(idServicio);
				llave.setIdUsuario(usuario);
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
			return super.nuevo();
		} catch (Exception e) {
			displayMessage(Mensaje.errorMessaje, Mensaje.SEVERITY_FATAL);
			return getPage().getOutcome();
		}
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

	public List<SelectItem> getServiciosInactivos() {
		HashMap<String, Object> map = new HashMap<>(0);
		try {
			return getSelectItems(getUsuarioCurrent(), map, "ListaValor.findServicioInactivo");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<SelectItem>(0);
	}

	public List<SelectItem> getTipoServicios() {
		try {
			return getSelectItems(TipoServicioEnum.values(), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<SelectItem>(0);
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

	public Servicio getLlave() {
		return llave;
	}

	public void setLlave(Servicio llave) {
		this.llave = llave;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Integer getIdTarifa() {
		return idTarifa;
	}

	public void setIdTarifa(Integer idTarifa) {
		this.idTarifa = idTarifa;
	}

	public Integer getIdServicio() {
		return idServicio;
	}

	public void setIdServicio(Integer idServicio) {
		this.idServicio = idServicio;
	}

	public Boolean getNuevo() {
		return nuevo;
	}

	public void setNuevo(Boolean nuevo) {
		this.nuevo = nuevo;
	}

}
