/**
 * 
 */
package org.ec.jap.backend.pagina;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

import org.ec.jap.bo.saap.UsuarioBO;
import org.ec.jap.bo.sistema.AuditoriaBO;
import org.ec.jap.bo.sistema.ElementoSistemaBO;
import org.ec.jap.bo.sistema.TipoEntidadBO;
import org.ec.jap.entiti.saap.Usuario;
import org.ec.jap.entiti.sistema.Auditoria;
import org.ec.jap.entiti.sistema.ElementoSistema;
import org.ec.jap.entiti.sistema.TipoEntidad;
import org.ec.jap.utilitario.Utilitario;
import org.richfaces.PanelMenuMode;
import org.richfaces.component.SwitchType;
import org.richfaces.component.UIPanelMenu;
import org.richfaces.component.UIPanelMenuGroup;
import org.richfaces.component.UIPanelMenuItem;
import org.richfaces.component.UITab;
import org.richfaces.component.UITabPanel;

/**
 * @author Freddy G Castillo C
 * 
 */
@ManagedBean
@SessionScoped
public class PageBean extends Bean {

	/**
	 * 
	 */
	@EJB
	ElementoSistemaBO elementoSistemaBO;
	@EJB
	UsuarioBO usuarioBO;

	private List<ElementoSistema> listModulos = new ArrayList<>();
	private ElementoSistema elementoSistema = new ElementoSistema();
	protected Integer idElementoSistemaPadre = 0;

	private UITabPanel tabPanel = new UITabPanel();

	private Boolean disableCambioContrasenia;

	private Boolean mostrarCancelar;

	public PageBean() {
	}

	@PostConstruct
	@Override
	public void init() {
		try {
			// FacesContext fcontext = FacesContext.getCurrentInstance();
			// ServletContext scontext = (ServletContext)
			// fcontext.getExternalContext().getContext();
			// Recupero el Usuario del aplicativo que se ha conectado
			
			
			
			if (FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal() != null) {
				String login = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();
				if (login != null) {
					Usuario usuario = usuarioBO.findUserByUsername(login);
					if (usuario != null) {
						if (usuario.getPassword().equals(Utilitario.getMD5_Base64(usuario.getUsername()))) {
							disableCambioContrasenia = false;
							mostrarCancelar = true;
						} else {
							disableCambioContrasenia = true;
							mostrarCancelar = false;
						}
					}
					setUsuarioCurent(usuario);
					setAuditoriaLogin();
					listModulos = elementoSistemaBO.findAllByUser(usuario.getIdComunidad().getIdComunidad(), usuario.getIdUsuario(), "MOD", -1);
					if (!listModulos.isEmpty())
						idElementoSistemaPadre = listModulos.isEmpty() ? -1 : listModulos.get(0).getIdElementoSistema();
					clickOption(null);
				}
				// Número de registros en la tabla
				Integer numeroRegistros = parametroBO.getInteger("", getUsuarioCurrent().getIdComunidad().getIdComunidad(), "NUMREGTAB");
				setNumeroFilas(numeroRegistros);

			}
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("skinBean");
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("skinBean");
		} catch (Exception e) {
			//e.printStackTrace();
			e.printStackTrace();
		}
	}

	public void clickOption(ActionEvent event) {
		try {
			idElementoSistemaPadre = event == null ? idElementoSistemaPadre : Integer.valueOf(((UIParameter) event.getComponent().getChildren().get(0)).getValue().toString());
			elementoSistema = elementoSistemaBO.findByPk(idElementoSistemaPadre);
			List<ElementoSistema> tabs = elementoSistemaBO.findAllByUser(getUsuarioCurrent().getIdComunidad().getIdComunidad(), getUsuarioCurrent().getIdUsuario(), "TAB", idElementoSistemaPadre);
			tabPanel = new UITabPanel();
			tabPanel.setId("tabPanel_0");
			tabPanel.setStyle("width:190px;height:100%;");
			tabPanel.setSwitchType(SwitchType.client);
			if (!tabs.isEmpty()) {
				for (ElementoSistema tab : tabs) {
					UITab uiTab = new UITab();
					uiTab.setId("tab_" + String.valueOf(tab.getIdElementoSistema()));
					uiTab.setHeader(tab.getNombre());
					uiTab.getChildren().add(getMenu(tab.getIdElementoSistema()));
					tabPanel.getChildren().add(uiTab);
				}
				redirect("/home/index.jsf");
			} else {
				// FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
				redirect("/home/index.jsf");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Meétodo que construye un menu recibiendo como parametro el id del tab,
	 * tipo de grupo y el tipo de item
	 * 
	 * @param idBrow
	 * @param typeGroup
	 * @param typeItem
	 * @return
	 * @throws Exception
	 */
	public UIPanelMenu getMenu(Integer idElementoSistemaPadre) throws Exception {

		List<ElementoSistema> systemGroup = elementoSistemaBO.findAllByUser(getUsuarioCurrent().getIdComunidad().getIdComunidad(), getUsuarioCurrent().getIdUsuario(), "GRUP", idElementoSistemaPadre);
		UIPanelMenu menu = new UIPanelMenu();
		menu.setId("panelMenu_" + String.valueOf(idElementoSistemaPadre * -1));
		menu.setGroupMode(PanelMenuMode.ajax);
		menu.setItemMode(PanelMenuMode.ajax);
		menu.setGroupExpandedLeftIcon("triangleUp");
		menu.setTopGroupCollapsedLeftIcon("triangleDown");
		menu.setTopGroupExpandedRightIcon("chevronUp");
		menu.setGroupCollapsedRightIcon("chevronDown");
		menu.setItemLeftIcon("disc");
		
		for (ElementoSistema grupo : systemGroup) {
			UIPanelMenuGroup group = new UIPanelMenuGroup();
			group.setId("panelMenuGroup_" + String.valueOf(grupo.getIdElementoSistema()));
			group.setLabel(grupo.getNombre());
			group.setExpanded(true);
			List<ElementoSistema> systemItems = elementoSistemaBO.findAllByUser(getUsuarioCurrent().getIdComunidad().getIdComunidad(), getUsuarioCurrent().getIdUsuario(), "OPT", grupo.getIdElementoSistema());
			for (ElementoSistema item : systemItems) {
				group.getChildren().add(getItems(item));
			}
			menu.getChildren().add(group);
		}

		List<ElementoSistema> systemItems = elementoSistemaBO.findAllByUser(getUsuarioCurrent().getIdComunidad().getIdComunidad(), getUsuarioCurrent().getIdUsuario(), "OPT", idElementoSistemaPadre);
		for (ElementoSistema item : systemItems) {
			menu.getChildren().add(getItems(item));
		}

		return menu;
	}

	/**
	 * Método que construye un HtmlPanelMenuItem en base a un SystemStructure
	 * 
	 * @param item
	 * @return
	 */
	public UIPanelMenuItem getItems(ElementoSistema item) {
		UIPanelMenuItem itemMenu = new UIPanelMenuItem();
		itemMenu.setId("panelMenuItem_" + String.valueOf(item.getIdElementoSistema()));
		itemMenu.setLabel(item.getNombre());
		// itemMenu.setOnclick("WorkFrame.document.location.href='" +
		// item.getUrl() + "'");
		itemMenu.setOnclick("setPage(\"" + item.getUrl() + "\")");
		return itemMenu;
	}

	@EJB
	AuditoriaBO auditoriaBO;
	@EJB
	TipoEntidadBO tipoEntidadBO;

	private void setAuditoriaLogin() throws Exception {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		session.getId();

		TipoEntidad trace = tipoEntidadBO.findByPk("LOGIN");

		if (trace != null) {
			Usuario user = getUsuarioCurrent();
			Auditoria tr = new Auditoria();
			tr.setIdEntidad(user.getIdUsuario());
			tr.setIdEntidad1(user.getIdComunidad().getIdComunidad());
			tr.setDescripcion("Login: " + user.getUsername());
			tr.setDescripcion1("Comunidad: " + user.getIdComunidad().getNombre());
			tr.setTipoEntidad(trace);
			tr.setUsuario(user);
			tr.setFecha(GregorianCalendar.getInstance().getTime());
			tr.setCambiosAtributos("Usuario:" + user.getNombres() + " " + user.getApellidos() + " Ingreso al Sistema");
			auditoriaBO.save(getUsuarioCurrent(), tr);

		}
	}

	public List<ElementoSistema> getListModulos() {
		return listModulos;
	}

	public void setListModulos(List<ElementoSistema> listModulos) {
		this.listModulos = listModulos;
	}

	public ElementoSistema getElementoSistema() {
		return elementoSistema;
	}

	public void setElementoSistema(ElementoSistema elementoSistema) {
		this.elementoSistema = elementoSistema;
	}

	public UITabPanel getTabPanel() {
		return tabPanel;
	}

	public void setTabPanel(UITabPanel tabPanel) {
		this.tabPanel = tabPanel;
	}

	public Boolean getDisableCambioContrasenia() {
		return disableCambioContrasenia;
	}

	public void setDisableCambioContrasenia(Boolean disableCambioContrasenia) {
		this.disableCambioContrasenia = disableCambioContrasenia;
	}

	public Boolean getMostrarCancelar() {
		return mostrarCancelar;
	}

	public void setMostrarCancelar(Boolean mostrarCancelar) {
		this.mostrarCancelar = mostrarCancelar;
	}

}
