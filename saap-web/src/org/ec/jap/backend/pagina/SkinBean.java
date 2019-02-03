/**
 * 
 */
package org.ec.jap.backend.pagina;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.saap.UsuarioBO;
import org.ec.jap.bo.sistema.ElementoSistemaBO;
import org.ec.jap.bo.sistema.FiltroBO;
import org.ec.jap.entiti.saap.Usuario;
import org.ec.jap.entiti.sistema.Filtro;
import org.ec.jap.utilitario.Utilitario;

/**
 * @author Freddy G Castillo C
 * 
 */
@ManagedBean
@SessionScoped
public class SkinBean extends Bean {

	@EJB
	FiltroBO filtroBO;

	private Filtro skin;
	@EJB
	UsuarioBO usuarioBO;
	@EJB
	ElementoSistemaBO elementoSistemaBO;

	private String cedula;

	private String contraseniaAnterior;

	private String contraseniaNueva;

	private String contraseniaNuevaConf;

	private Boolean disableButon;

	public SkinBean() {
	}

	@PostConstruct
	public void init() {
		try {
			skin = filtroBO.getFiltro(getUsuarioCurrent() == null ? usuarioBO.findByPk(1) : getUsuarioCurrent(), "THEME", elementoSistemaBO.findByPk(1), "saap", skin, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clickStyle(ActionEvent event) {
		UIParameter parameterRichFaces = (UIParameter) event.getComponent().getChildren().get(0);
		// UIParameter parameterPrimeFaces = (UIParameter)
		// event.getComponent().getChildren().get(1);
		try {
			if (skin != null)
				skin.setValorCadena(parameterRichFaces.getValue().toString());
			skin = filtroBO.getFiltro(getUsuarioCurrent(), "THEME", elementoSistemaBO.findByPk(1), parameterRichFaces.getValue().toString(), skin, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// skinPrime=parameterPrimeFaces.getValue().toString();
		// fc.setStringValue(skin);
		// fcPrime.setStringValue(skinPrime);
		// filterConditionBO.update(this.sessionController.getIdUser(),
		// this.sessionController.getIdEnterprise(), fc);
		// filterConditionBO.update(this.sessionController.getIdUser(),
		// this.sessionController.getIdEnterprise(), fcPrime);
	}

	public String exit() {
		/*
		 * FacesContext context = FacesContext.getCurrentInstance();
		 * HttpServletRequest request = (HttpServletRequest)
		 * context.getExternalContext().getRequest(); try { request.logout(); }
		 * catch (ServletException e) { e.printStackTrace(); }
		 */
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		if (disableButon != null && disableButon == false)
			displayMessage("Ingrese Nuevamente con sus neuvas credenciales", Mensaje.SEVERITY_INFO);
		return "/home/index?faces-redirect=true";
	}

	public String savePass() {
		try {
			if (!getUsuarioCurrent().getCedula().equals(cedula)) {
				throw new Exception("La cedula ingresada no es la correcta");
			}
			if (!getUsuarioCurrent().getPassword().equals(Utilitario.getMD5_Base64(contraseniaAnterior))) {
				throw new Exception("La contraseña anterior ingresada no es la correcta");
			}
			if (!contraseniaNueva.equals(contraseniaNuevaConf)) {
				throw new Exception("Las contraseñas no coinciden");
			}
			if (contraseniaNueva.equals(contraseniaAnterior)) {
				throw new Exception("Las contraseña nueva no puede ser la misma que la anterior");
			}
			if (getUsuarioCurrent().getUsername().contains(contraseniaNueva.toLowerCase())) {
				throw new Exception("Las contraseña nueva no puede contener el nombre del usuario");
			}

			Usuario usuario = getUsuarioCurrent();
			usuario.setPassword(Utilitario.getMD5_Base64(contraseniaNueva));
			usuarioBO.update(getUsuarioCurrent(), usuario);
			displayMessage(Mensaje.saveMessaje, Mensaje.SEVERITY_INFO);
			disableButon = false;

			FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
			return "/home/index?faces-redirect=true";
			// return "";
		} catch (Exception e) {
			disableButon = true;
			displayMessage(e.getMessage(), Mensaje.SEVERITY_INFO);
			// FacesContext.getCurrentInstance().addMessage("", new
			// FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(),
			// e.getMessage()));
			return "";
		}
	}

	public String exitPass() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/home/index?faces-redirect=true";
	}

	public Filtro getSkin() {
		return skin;
	}

	public void setSkin(Filtro skin) {
		this.skin = skin;
	}

	public String getContraseniaAnterior() {
		return contraseniaAnterior;
	}

	public void setContraseniaAnterior(String contraseniaAnterior) {
		this.contraseniaAnterior = contraseniaAnterior;
	}

	public String getContraseniaNueva() {
		return contraseniaNueva;
	}

	public void setContraseniaNueva(String contraseniaNueva) {
		this.contraseniaNueva = contraseniaNueva;
	}

	public String getContraseniaNuevaConf() {
		return contraseniaNuevaConf;
	}

	public void setContraseniaNuevaConf(String contraseniaNuevaConf) {
		this.contraseniaNuevaConf = contraseniaNuevaConf;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public Boolean getDisableButon() {
		return disableButon;
	}

	public void setDisableButon(Boolean disableButon) {
		this.disableButon = disableButon;
	}

}
