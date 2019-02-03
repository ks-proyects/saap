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
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.saap.LecturaBO;
import org.ec.jap.bo.saap.LlaveBO;
import org.ec.jap.bo.saap.TarifaBO;
import org.ec.jap.bo.saap.TipoLlaveBO;
import org.ec.jap.bo.saap.UsuarioBO;
import org.ec.jap.entiti.saap.Lectura;
import org.ec.jap.entiti.saap.Llave;
import org.ec.jap.entiti.saap.Usuario;

@ManagedBean
@ViewScoped
public class LlaveEditBean extends Bean {

	/**
	 * 
	 */

	@EJB
	UsuarioBO usuarioBO;
	@EJB
	TipoLlaveBO tipoLlaveBO;
	@EJB
	TarifaBO tarifaBO;
	@EJB
	LlaveBO llaveBO;
	@EJB
	LecturaBO lecturaBO;

	private Integer idPeriodoPago;
	private Llave llave;
	private Integer idTarifa;
	private String tipoLlave;
	private Usuario usuario;
	private Integer anio;
	private String mes;
	private List<Lectura> lecturasList = new ArrayList<>();

	public LlaveEditBean() {
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
				llave = new Llave();
				usuario = usuarioBO.findByPk(getParam1Integer());
			} else {
				llave = llaveBO.findByPk(getParam2Integer());
				idTarifa = llave.getIdTarifa().getIdTarifa();
				tipoLlave = llave.getIdTarifa().getTipoLlave().getTipoLlave();
				usuario = llave.getIdUsuario();
				map = new HashMap<>();
				map.put("idLlave", llave);
				map.put("idPeriodoPago", idPeriodoPago!=null?idPeriodoPago:0);
				lecturasList = lecturaBO.findAllByNamedQuery("Lectura.findByUser", map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@Override
	public String guardar() {
		try {

			llave.setIdTarifa(tarifaBO.findByPk(idTarifa));
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
	public String insert() {
		setAccion("INS");
		return "lecturaEdit?faces-redirect=true";
	}

	@Override
	public String nuevo() {
		// TODO Auto-generated method stub
		try {
			map = new HashMap<>();
			map.put("idUsuario", usuarioBO.findByPk(getParam1Integer()).getIdUsuario());
			List<Llave> listLlaves = llaveBO.findAllByNamedQuery("Llave.findByUser", map);
			Integer numMaxLlaves = parametroBO.getInteger("", getUsuarioCurrent().getIdComunidad().getIdComunidad(), "NUMLLAV");
			if (listLlaves.size() >= numMaxLlaves) {
				displayMessage("El número de llaves permitidas por usuario es: " + numMaxLlaves.toString() + ". No puede asignar más llaves.", Mensaje.SEVERITY_WARN);
				return getPage().getNombre();
			} else {
				return super.nuevo();
			}

		} catch (Exception e) {
			displayMessage(Mensaje.errorMessaje, Mensaje.SEVERITY_FATAL);
			return getPage().getOutcome();
		}
	}
	
	public void valueChange(ValueChangeEvent event) {
		idPeriodoPago=Integer.valueOf(event.getNewValue().toString());
		map = new HashMap<>();
		map.put("idLlave", llave);
		map.put("idPeriodoPago", idPeriodoPago!=null?idPeriodoPago:0);
		try {
			lecturasList = lecturaBO.findAllByNamedQuery("Lectura.findByUser", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<SelectItem> getItemTipoLlave() {
		HashMap<String, Object> map = new HashMap<>(0);
		try {
			return getSelectItems(getUsuarioCurrent(), map, "ListaValor.findTipoLlave");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<SelectItem>(0);
	}

	public List<SelectItem> getItemTarifa() {
		HashMap<String, Object> map = new HashMap<>(0);
		try {
			return getSelectItems(getUsuarioCurrent(), map, "ListaValor.findTarifa");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<SelectItem>(0);
	}

	public Llave getLlave() {
		return llave;
	}

	public void setLlave(Llave llave) {
		this.llave = llave;
	}

	public Integer getIdTarifa() {
		return idTarifa;
	}

	public void setIdTarifa(Integer idTarifa) {
		this.idTarifa = idTarifa;
	}

	public String getTipoLlave() {
		return tipoLlave;
	}

	public void setTipoLlave(String tipoLlave) {
		this.tipoLlave = tipoLlave;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Lectura> getLecturasList() {
		return lecturasList;
	}

	public void setLecturasList(List<Lectura> lecturasList) {
		this.lecturasList = lecturasList;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public Integer getIdPeriodoPago() {
		return idPeriodoPago;
	}

	public void setIdPeriodoPago(Integer idPeriodoPago) {
		this.idPeriodoPago = idPeriodoPago;
	}

	public List<SelectItem> getPeriodos() {
		try {
			return getSelectItems(getUsuarioCurrent(), null, true, "ListaValor.findNoEsta");
		} catch (Exception e) {

			e.printStackTrace();
			return new ArrayList<>();
		}
	}

}
