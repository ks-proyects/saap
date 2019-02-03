/**
 * 
 */
package org.ec.jap.backend.pagina;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.el.MethodExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.MethodExpressionActionListener;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.saap.ParametroBO;
import org.ec.jap.bo.sistema.CambioEstadoBO;
import org.ec.jap.bo.sistema.ElementoSistemaBO;
import org.ec.jap.bo.sistema.EstadoEntidadBO;
import org.ec.jap.bo.sistema.FiltroBO;
import org.ec.jap.bo.sistema.ListaValoreBO;
import org.ec.jap.entiti.saap.Usuario;
import org.ec.jap.entiti.sistema.CambioEstado;
import org.ec.jap.entiti.sistema.ElementoSistema;
import org.ec.jap.entiti.sistema.EstadoEntidad;
import org.ec.jap.enumerations.OutputType;
import org.ec.jap.enumerations.ValorSiNo;
import org.richfaces.component.UIToolbar;
import org.richfaces.component.UIToolbarGroup;

/**
 * @author Freddy G Castillo C
 * 
 */

@SuppressWarnings("unchecked")
public abstract class Actions {

	@EJB
	ElementoSistemaBO elementoSistemaBO;

	@EJB
	protected ListaValoreBO listaValoreBO;

	@EJB
	protected ParametroBO parametroBO;

	@EJB
	protected FiltroBO filtroBO;

	protected HashMap<String, Object> map = new HashMap<>(0);

	protected UIToolbarGroup toolbarGroup;
	private Boolean soloLectura = false;
	protected Context context;

	public Actions() {
		try {
			setNivelBloqueo(0);
			setDescripcionEstado("");
			initialParam();
			context = new InitialContext();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public abstract void init() throws Exception;;

	public void checkPage() throws Exception {
		map = new HashMap<>(0);
		map.put("classControlador", getPageCode());
		ElementoSistema sistema = elementoSistemaBO.findByNamedQuery("ElementoSistema.findByController", map);
		if (sistema == null) {
			throw new Exception("Usted no tiene permisos sobre esta pagina");
		}
		setTipoPagina(sistema.getTipoElemento());
		setTipoEntidad("LIST".equals(sistema.getImagenLogo()) ? "" : getTipoEntidad());
		setIdPage(sistema.getIdElementoSistema());
		setPage(sistema);
		addPage(sistema);

	}

	public void generateActions() throws Exception {

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		UIToolbar toolbar = (UIToolbar) FacesContext.getCurrentInstance().getViewRoot().findComponent("WorkPage:toolBar");

		if (toolbar != null) {
			toolbar.clearInitialState();
			toolbar.getChildren().clear();
			toolbarGroup = new UIToolbarGroup();
			toolbarGroup.setId("tbg_0");
			List<ElementoSistema> listAcctions = elementoSistemaBO.findControlsByUserAndAccion(getUsuarioCurrent().getIdComunidad().getIdComunidad(), getUsuarioCurrent().getIdUsuario(), "ACC", getIdPage(), getAccion());
			if (!listAcctions.isEmpty()) {
				for (ElementoSistema elementoSistema : listAcctions) {
					String idComponente = "btn_" + String.valueOf(elementoSistema.getIdAccion().getIdAccion().toString());

					HtmlCommandButton button = (HtmlCommandButton) FacesContext.getCurrentInstance().getViewRoot().findComponent("WorkPage:" + idComponente);

					button = createButton();
					button.setValue(elementoSistema.getNombre() != null && !"".equals(elementoSistema.getNombre().trim()) ? elementoSistema.getNombre() : elementoSistema.getIdAccion().getDescripcion());
					button.setId(idComponente);
					button.setOnclick("javascript: return onClic('" + elementoSistema.getIdAccion().getIdAccion().toString() + "')");
					button.setStyle(createStyle(elementoSistema, request));
					button.setStyleClass("buton");
					button.setTitle(elementoSistema.getIdAccion().getTecladoAccion());

					if ("EDI".equals(getPage().getImagenLogo().toUpperCase()))
						if (elementoSistema.getIdAccion().getIdAccion().equals(7))
							button.setImmediate(false);
						else
							button.setImmediate(true);

					if (elementoSistema.getIdAccion().getTipo() == null) {
						if (elementoSistema.getIdAccion().getIdAccion() == 1)
							button.addActionListener(getActionListener(getNameClass() + "." + elementoSistema.getIdAccion().getAccionPage()));
						else
							button.setActionExpression(getActionExpression(getNameClass() + "." + elementoSistema.getIdAccion().getAccionPage()));

					} else {
						UIParameter idDE = new UIParameter();
						idDE.setName("formatDocument");
						idDE.setValue(elementoSistema.getIdAccion().getTipo());

						UIParameter contentReport = new UIParameter();
						contentReport.setName("contentReport");
						contentReport.setValue(elementoSistema.getUrl());

						UIParameter idReport = new UIParameter();
						contentReport.setName("idReport");
						contentReport.setValue(elementoSistema.getIdElementoSistema());

						button.getChildren().add(idDE);
						button.getChildren().add(contentReport);
						button.getChildren().add(idReport);
						if (elementoSistema.getIdAccion().getIdAccion() == 8) {
							AjaxBehavior ajax = new AjaxBehavior();
							ajax.setRender(Arrays.asList(":messages"));
							ajax.setExecute(Arrays.asList(":WorkPage"));
							button.addClientBehavior(button.getDefaultEventName(), ajax);
						}
						button.setActionExpression(getActionExpression("reporteBean." + elementoSistema.getIdAccion().getAccionPage()));

					}
					toolbarGroup.getChildren().add(button);

				}
				toolbar.getChildren().add(toolbarGroup);

			} else
				throw new Exception("Usted no tiene permisos sobre esta pagina");
		}
	}

	@EJB
	protected CambioEstadoBO cambioEstadoBO;
	@EJB
	EstadoEntidadBO estadoEntidadBO;

	protected void redisplayActions(String estadoActual, Object idDocumento) throws Exception {

		setIdDocumento(idDocumento);

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

		map = new HashMap<>();
		map.put("estado", estadoActual);
		map.put("tipoEntidad", getTipoEntidad());

		EstadoEntidad estadoAnterior = estadoEntidadBO.findByNamedQuery("EstadoEntidad.findByEstadoAndTipo", map);

		if (estadoAnterior == null)
			return;
		setDescripcionEstado(estadoAnterior.getDescripcion());

		map = new HashMap<>();
		map.put("idEstadoAnterior", estadoAnterior);
		map.put("tipoEntidad", getTipoEntidad());
		map.put("idUsuario", getUsuarioCurrent());
		map.put("idElementoSistemaFk", getIdPage());
		map.put("activoRol", ValorSiNo.valueOf("S"));

		List<CambioEstado> cambioEstados = cambioEstadoBO.findAllByNamedQuery("CambioEstado.findByEstado", map);
		Integer nivelBloqueo = (Integer) cambioEstadoBO.findObjectByNamedQuery("CambioEstado.findNivelBolqueo", map);
		setNivelBloqueo(nivelBloqueo != null ? nivelBloqueo : 1);
		if (getNivelBloqueo() > 0) {
			redisplayAction(5, false);
			redisplayAction(7, false);
		}

		if (!cambioEstados.isEmpty()) {
			for (CambioEstado ce : cambioEstados) {
				if (cambioEstadoBO.verificarEstado(ce, (Integer) idDocumento)) {
					HtmlCommandButton button = createButton();
					button.setValue(ce.getDescripcion());
					button.setId("btn_" + String.valueOf(ce.getAccion().getIdAccion().toString()));
					button.setOnclick("javascript: return onClicTE('" + ce.getAccion().getIdAccion().toString() + "','" + ce.getAplicaMotivo() + "'," + ce.getIdCambioEstado() + "," + idDocumento + ")");
					button.setStyle(createStyle(ce.getImagenAccion(), request));
					button.setTitle(ce.getAccion().getTecladoAccion());
					button.setStyleClass("buton");
					UIParameter idCE = new UIParameter();
					idCE.setName("idCambioEstado");
					idCE.setValue(ce.getIdCambioEstado());

					UIParameter idDE = new UIParameter();
					idDE.setName("idDocumentoEntidad");
					idDE.setValue(idDocumento);

					button.getChildren().add(idCE);
					button.getChildren().add(idDE);
					// button.setOnkeyup("S".equals(ce.getAplicaMotivo())?
					// "javascript:opener.doPostBack();closePopup;":"");
					if ("EDI".equals(getPage().getImagenLogo().toUpperCase()))
						button.setImmediate(true);
					button.addActionListener(getActionListener(getNameClass() + "" + "." + "cambiarEstado"));
					toolbarGroup.getChildren().add(button);
				}

			}
			UIToolbar toolbar = (UIToolbar) FacesContext.getCurrentInstance().getViewRoot().findComponent("WorkPage:toolBar");
			toolbar.getChildren().clear();
			toolbar.getChildren().add(toolbarGroup);
			displayActionEnd(6);
		}

	}

	protected void displayActionEnd(Integer idAccion) throws Exception {
		if (toolbarGroup != null) {
			UIComponent component = toolbarGroup.findComponent("WorkPage:btn_" + idAccion.toString());
			if (component != null) {
				UIToolbar toolbar = (UIToolbar) FacesContext.getCurrentInstance().getViewRoot().findComponent("WorkPage:toolBar");
				toolbar.clearInitialState();
				toolbar.getChildren().clear();
				toolbarGroup.getChildren().remove(component);
				toolbarGroup.getChildren().add(component);
				toolbar.getChildren().add(toolbarGroup);
			}
		}

	}

	protected void redisplayAction(Integer idAccion, Boolean rendered) throws Exception {
		if (toolbarGroup != null) {
			UIComponent component = toolbarGroup.findComponent("WorkPage:btn_" + idAccion.toString());
			Integer index = toolbarGroup.getChildren().lastIndexOf(component);

			if (component != null) {
				component.setRendered(rendered);
				UIToolbar toolbar = (UIToolbar) FacesContext.getCurrentInstance().getViewRoot().findComponent("WorkPage:toolBar");
				toolbar.clearInitialState();
				toolbar.getChildren().clear();
				toolbarGroup.getChildren().add(index, component);
				toolbar.getChildren().add(toolbarGroup);
			}
		}
	}

	protected void redisplayAction(Integer idAccion, Boolean rendered, String label) throws Exception {
		if (toolbarGroup != null) {
			UIComponent component = toolbarGroup.findComponent("WorkPage:btn_" + idAccion.toString());
			Integer index = toolbarGroup.getChildren().lastIndexOf(component);

			if (component != null) {
				HtmlCommandButton button = (HtmlCommandButton) component;
				button.setRendered(rendered);
				button.setLabel(label);
				UIToolbar toolbar = (UIToolbar) FacesContext.getCurrentInstance().getViewRoot().findComponent("WorkPage:toolBar");
				toolbar.clearInitialState();
				toolbar.getChildren().clear();
				toolbarGroup.getChildren().add(index, button);
				toolbar.getChildren().add(toolbarGroup);
			}
		}
	}

	private HtmlCommandButton createButton() {

		return (HtmlCommandButton) FacesContext.getCurrentInstance().getApplication().createComponent(HtmlCommandButton.COMPONENT_TYPE);

	}

	private String createStyle(ElementoSistema ses, HttpServletRequest request) {

		return "background-image: url('" + request.getContextPath() + "/tools/imagenes/" + (ses.getImagenLogo() != null && !ses.getImagenLogo().equals("") ? ses.getImagenLogo() : ses.getIdAccion().getImagen()) + "');";
	}

	private String createStyle(String imagen, HttpServletRequest request) {

		return "background-image: url('" + request.getContextPath() + "/tools/imagenes/" + imagen + "');";
	}

	public void cambiarEstado(ActionEvent event) {
		try {
			UIParameter idCE = (UIParameter) event.getComponent().getChildren().get(0);
			UIParameter idDE = (UIParameter) event.getComponent().getChildren().get(1);
			cambioEstadoBO.cambiarEstado((Integer) idCE.getValue(), getUsuarioCurrent(), idDE.getValue());
			init();
			displayMessage(Mensaje.defaultMessaje, Mensaje.SEVERITY_INFO);
		} catch (Exception e) {
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
			e.printStackTrace();
		}
	}

	/**
	 * Método invocado al precionar el boton refresh
	 * 
	 * @return
	 */
	public String refresh() {
		map = new HashMap<>(0);
		map.put("classControlador", getPageCode());
		ElementoSistema sistema = new ElementoSistema();
		try {
			sistema = elementoSistemaBO.findByNamedQuery("ElementoSistema.findByController", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sistema.getOutcome();
	}

	/**
	 * Método invocado al precionar el botón regresar
	 * 
	 * @return
	 */
	public String regresar() {
		this.setAccion("UPD");
		return getLastPage().getOutcome();
	}

	public String guardar() {
		return null;

	}

	/**
	 * Método invocado al precionar el boton nuevo
	 * 
	 * @return
	 */
	public String nuevo() {
		setAccion("INS");
		return getPage().getOutcome();
	}

	/**
	 * Metodo invocado al precionar el botón insertar
	 * 
	 * @return
	 */
	public String insert() {
		return null;
	}

	public String generalAccion() {
		return null;

	}

	/**
	 * Metodo invocado al precionar el botón eliminar
	 * 
	 * @return
	 */
	public String delete() {
		return null;
	}

	/**
	 * Método invocado al precionar el botón imprimir
	 * 
	 * @return
	 */
	public String imprimir() {

		return "";

	}

	/**
	 * Metodo que gestiona el evento buscar
	 * 
	 * @param event
	 */
	public void search(ActionEvent event) {

	}

	protected ActionListener getActionListener(String action) {
		return new MethodExpressionActionListener(getActionExpression(action, null, new Class[] { ActionEvent.class }));
	}

	protected MethodExpression getActionExpression(String action) {
		return getActionExpression(action, String.class, new Class[] {});
	}

	@SuppressWarnings("rawtypes")
	protected MethodExpression getActionExpression(String action, Class<?> returned, Class[] parametros) {
		FacesContext context = FacesContext.getCurrentInstance();

		MethodExpression actionExpresion = context.getApplication().getExpressionFactory().createMethodExpression(context.getELContext(), "#{}".replace("}", "") + action + "}", returned, parametros);
		return actionExpresion;
	}

	public void redirect(String url) throws IOException {
		FacesContext faces = FacesContext.getCurrentInstance();
		ExternalContext context = faces.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) context.getRequest();
		HttpServletResponse response = (HttpServletResponse) context.getResponse();
		response.sendRedirect(request.getContextPath() + url);
	}

	protected final String getPageCode() {
		String pageCode;
		pageCode = this.toString();
		pageCode = pageCode.substring(0, pageCode.indexOf("@"));
		if (pageCode.indexOf("_$$_") != -1) {
			pageCode = pageCode.substring(0, pageCode.indexOf("_$$_"));
		}
		return pageCode;
	}

	protected final String getNameClass() {
		String namePage = this.getClass().getSimpleName().substring(0, 1).toLowerCase() + this.getClass().getSimpleName().substring(1);
		return namePage;
	}

	protected final String getDirectorioPDF() {
		String namePage = "";
		try {
			namePage = parametroBO.getString("", getUsuarioCurrent().getIdComunidad().getIdComunidad(), "DIRDOCPDF");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return namePage;
	}

	public Object getAtribute(String key) {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> map = externalContext.getSessionMap();
		return map.get(key);
	}

	public void setAtribute(String key, Object value) {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		externalContext.getSessionMap().put(key, value);
	}

	public void initialParam() throws Exception {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, String> map = facesContext.getExternalContext().getRequestParameterMap();
		if (map.get("idPage") != null) {
			setAtribute("idPage", map.get("idPage"));
			setAtribute("navigator", new ArrayList<ElementoSistema>(0));
		}

		if (map.get("contentReport") != null)
			setAtribute("contentReport", map.get("contentReport"));
		if (map.get("formatDocument") != null)
			setAtribute("formatDocument", map.get("formatDocument"));
		if (map.get("idReport") != null)
			setAtribute("idReport", map.get("idReport"));
		if (map.get("idCambioEstado") != null)
			setAtribute("idCambioEstado", map.get("idCambioEstado"));
		if (map.get("idDocumentoEntidad") != null)
			setAtribute("idDocumentoEntidad", map.get("idDocumentoEntidad"));
		if (map.get("idDocumento") != null)
			setAtribute("idDocumento", map.get("idDocumento"));
		if (map.get("action") != null)
			setAtribute("action", map.get("action"));
		if (map.get("param1") != null)
			setAtribute("param1", map.get("param1"));
		if (map.get("param2") != null)
			setAtribute("param2", map.get("param2"));
		if (map.get("param3") != null)
			setAtribute("param3", map.get("param3"));
		if (map.get("param4") != null)
			setAtribute("param4", map.get("param4"));
		if (map.get("param5") != null)
			setAtribute("param5", map.get("param5"));
		if (map.get("param6") != null)
			setAtribute("param6", map.get("param6"));
		if (map.get("param7") != null)
			setAtribute("param7", map.get("param7"));
		if (map.get("param8") != null)
			setAtribute("param8", map.get("param8"));
		if (map.get("param9") != null)
			setAtribute("param9", map.get("param9"));
		if (map.get("param10") != null)
			setAtribute("param10", map.get("param10"));
		if (map.get("param11") != null)
			setAtribute("param11", map.get("param11"));
		if (map.get("param12") != null)
			setAtribute("param12", map.get("param12"));
		if (map.get("param13") != null)
			setAtribute("param13", map.get("param13"));
		if (map.get("param14") != null)
			setAtribute("param14", map.get("param14"));
		if (map.get("param15") != null)
			setAtribute("param15", map.get("param15"));
		if (map.get("param16") != null)
			setAtribute("param16", map.get("param16"));
		if (map.get("param17") != null)
			setAtribute("param17", map.get("param17"));
		if (map.get("param18") != null)
			setAtribute("param18", map.get("param18"));
		if (map.get("param19") != null)
			setAtribute("param19", map.get("param19"));
		if (map.get("param20") != null)
			setAtribute("param20", map.get("param20"));
	}

	public ElementoSistema getLastPage() {
		List<ElementoSistema> historial = new ArrayList<>(0);
		if (getAtribute("navigator") != null) {
			if (getAtribute("navigator") instanceof ArrayList<?>) {
				historial = (List<ElementoSistema>) getAtribute("navigator");
				historial.remove(historial.size() - 1);
				ElementoSistema elementoSistema = historial.get(historial.size() - 1);
				setAtribute("navigator", historial);
				return elementoSistema;
			} else {
				return new ElementoSistema();
			}

		} else {
			setAtribute("navigator", historial);
			return new ElementoSistema();
		}
	}

	public ElementoSistema getLastOutcome() {
		List<ElementoSistema> historial = new ArrayList<>(0);
		if (getAtribute("navigator") != null) {
			if (getAtribute("navigator") instanceof ArrayList<?>) {
				historial = (List<ElementoSistema>) getAtribute("navigator");
				ElementoSistema elementoSistema = historial.get(historial.size() - 2);
				return elementoSistema;
			} else {
				return new ElementoSistema();
			}

		} else {
			setAtribute("navigator", historial);
			return new ElementoSistema();
		}
	}

	public void addPage(ElementoSistema elementoSistema) {
		if (!"POP".equals(getTipoPagina())) {
			List<ElementoSistema> historial = new ArrayList<>(0);
			if (getAtribute("navigator") != null) {
				if (getAtribute("navigator") instanceof ArrayList<?>) {
					historial = (List<ElementoSistema>) getAtribute("navigator");
					if (!historial.contains(elementoSistema)) {
						historial.add(elementoSistema);
						setAtribute("navigator", historial);
					}
				} else {
					historial.add(elementoSistema);
					setAtribute("navigator", historial);
				}

			} else {
				historial.add(elementoSistema);
				setAtribute("navigator", historial);
			}
		}
	}

	public OutputType getFormatDocument() {
		OutputType outputType = OutputType.valueOf(getString("formatDocument"));
		return outputType;
	}

	public Integer getIdReport() {
		Integer integer = getInteger("idReport");
		return integer;
	}

	public String getContentReport() {
		return getString("contentReport");
	}

	public Object getObject() {
		return getAtribute("object");
	}

	public void setObject(Object object) {
		setAtribute("object", object);
	}

	public Usuario getUsuarioCurrent() {
		return (Usuario) getAtribute("user");
	}

	public void setUsuarioCurent(Usuario usuario) {
		setAtribute("user", usuario);
	}

	public Integer getIdPage() {
		if ("POP".equals(getTipoPagina()))
			return getInteger("idPagePupup");
		else
			return getInteger("idPage");
	}

	public void setIdPage(Integer idPage) {
		if ("POP".equals(getTipoPagina()))
			setAtribute("idPagePupup", idPage);
		else
			setAtribute("idPage", idPage);
	}

	public ElementoSistema getPage() {
		if ("POP".equals(getTipoPagina()))
			return (ElementoSistema) getAtribute("pagePopup");
		else
			return (ElementoSistema) getAtribute("page");
	}

	public void setPage(ElementoSistema idPage) {
		if ("POP".equals(getTipoPagina()))
			setAtribute("pagePopup", idPage);
		else
			setAtribute("page", idPage);
	}

	public Integer getIdDocumentoInteger() {
		return getInteger("idDocumento");
	}

	public String getIdDocumentoString() {
		return getString("idDocumento");
	}

	public String getTipoEntidad() {
		return getString("tipoEntidad");
	}

	public String getTipoPagina() {
		return getString("tipoPagina");
	}

	public Integer getNivelBloqueo() {
		return getInteger("nivelBloqueo") != null ? getInteger("nivelBloqueo") : 0;
	}

	public String getAccion() {
		return getString("action");
	}

	public String getNombreArchivo() {
		return getString("nameReport");
	}

	public String getPathFile() {
		return getString("name_file");
	}

	public String getDescripcionEstado() {
		return getString("descEstado");
	}

	public Integer getIdCambioEstado() {
		return getInteger("idCambioEstado");
	}

	public Integer getIdDocumentoEntidad() {
		return getInteger("idDocumentoEntidad");
	}

	public Boolean getReadOnly1() {
		return getNivelBloqueo().equals(1);
	}

	public Boolean getReadOnly2() {
		return getNivelBloqueo().equals(2);
	}

	public Boolean getReadOnly3() {
		return getNivelBloqueo().equals(3);
	}

	public Boolean getReadOnly4() {
		return getNivelBloqueo().equals(4);
	}

	/**
	 * Obtiene el entero del param1
	 * 
	 * @return
	 */
	public Integer getReportDocumentInteger() {
		return getInteger("reportDocument");
	}

	/**
	 * Obtiene el string del param1
	 * 
	 * @return
	 */
	public String getReportDocumentString() {
		return getString("reportDocument");
	}

	/**
	 * Obtiene el entero del param1
	 * 
	 * @return
	 */
	public Integer getParam1Integer() {
		return getInteger("param1");
	}

	/**
	 * Obtiene el string del param1
	 * 
	 * @return
	 */
	public String getParam1String() {
		return getString("param1");
	}

	/**
	 * Obtiene el entero del param1
	 * 
	 * @return
	 */
	public Integer getParam2Integer() {
		return getInteger("param2");
	}

	/**
	 * Obtiene el string del param1
	 * 
	 * @return
	 */
	public String getParam2String() {
		return getString("param2");
	}

	/**
	 * Obtiene el entero del param1
	 * 
	 * @return
	 */
	public Integer getParam3Integer() {
		return getInteger("param3");
	}

	/**
	 * Obtiene el string del param1
	 * 
	 * @return
	 */
	public String getParam3String() {
		return getString("param3");
	}

	/**
	 * Obtiene el entero del param1
	 * 
	 * @return
	 */
	public Integer getParam4Integer() {
		return getInteger("param4");
	}

	/**
	 * Obtiene el string del param1
	 * 
	 * @return
	 */
	public String getParam4String() {
		return getString("param4");
	}

	/**
	 * Obtiene el entero del param1
	 * 
	 * @return
	 */
	public Integer getParam5Integer() {
		return getInteger("param5");
	}

	/**
	 * Obtiene el string del param1
	 * 
	 * @return
	 */
	public String getParam5String() {
		return getString("param5");
	}

	/**
	 * Obtiene el entero del param1
	 * 
	 * @return
	 */
	public Integer getParam6Integer() {
		return getInteger("param6");
	}

	/**
	 * Obtiene el string del param1
	 * 
	 * @return
	 */
	public String getParam6String() {
		return getString("param6");
	}

	/**
	 * Obtiene el entero del param1
	 * 
	 * @return
	 */
	public Integer getParam7Integer() {
		return getInteger("param7");
	}

	/**
	 * Obtiene el string del param1
	 * 
	 * @return
	 */
	public String getParam7String() {
		return getString("param7");
	}

	/**
	 * Obtiene el entero del param1
	 * 
	 * @return
	 */
	public Integer getParam8Integer() {
		return getInteger("param8");
	}

	/**
	 * Obtiene el string del param1
	 * 
	 * @return
	 */
	public String getParam8String() {
		return getString("param8");
	}

	/**
	 * Obtiene el entero del param1
	 * 
	 * @return
	 */
	public Integer getParam9Integer() {
		return getInteger("param9");
	}

	/**
	 * Obtiene el string del param1
	 * 
	 * @return
	 */
	public String getParam9String() {
		return getString("param9");
	}

	/**
	 * Obtiene el entero del param1
	 * 
	 * @return
	 */
	public Integer getParam10Integer() {
		return getInteger("param10");
	}

	/**
	 * Obtiene el string del param1
	 * 
	 * @return
	 */
	public String getParam10String() {
		return getString("param10");
	}

	/**
	 * Obtiene el entero del param1
	 * 
	 * @return
	 */
	public Integer getParam11Integer() {
		return getInteger("param11");
	}

	/**
	 * Obtiene el string del param1
	 * 
	 * @return
	 */
	public String getParam11String() {
		return getString("param11");
	}

	/**
	 * Obtiene el entero del param1
	 * 
	 * @return
	 */
	public Integer getParam12Integer() {
		return getInteger("param12");
	}

	/**
	 * Obtiene el string del param1
	 * 
	 * @return
	 */
	public String getParam12String() {
		return getString("param12");
	}

	/**
	 * Obtiene el entero del param1
	 * 
	 * @return
	 */
	public Integer getParam13Integer() {
		return getInteger("param13");
	}

	/**
	 * Obtiene el string del param1
	 * 
	 * @return
	 */
	public String getParam13String() {
		return getString("param13");
	}

	/**
	 * Obtiene el entero del param1
	 * 
	 * @return
	 */
	public Integer getParam14Integer() {
		return getInteger("param14");
	}

	/**
	 * Obtiene el string del param1
	 * 
	 * @return
	 */
	public String getParam14String() {
		return getString("param14");
	}

	/**
	 * Obtiene el entero del param1
	 * 
	 * @return
	 */
	public Integer getParam15Integer() {
		return getInteger("param15");
	}

	/**
	 * Obtiene el string del param1
	 * 
	 * @return
	 */
	public String getParam15String() {
		return getString("param15");
	}

	/**
	 * Obtiene el entero del param1
	 * 
	 * @return
	 */
	public Integer getParam16Integer() {
		return getInteger("param16");
	}

	/**
	 * Obtiene el string del param1
	 * 
	 * @return
	 */
	public String getParam16String() {
		return getString("param16");
	}

	/**
	 * Obtiene el entero del param1
	 * 
	 * @return
	 */
	public Integer getParam17Integer() {
		return getInteger("param17");
	}

	/**
	 * Obtiene el string del param1
	 * 
	 * @return
	 */
	public String getParam17String() {
		return getString("param17");
	}

	/**
	 * Obtiene el entero del param1
	 * 
	 * @return
	 */
	public Integer getParam18Integer() {
		return getInteger("param18");
	}

	/**
	 * Obtiene el string del param1
	 * 
	 * @return
	 */
	public String getParam18String() {
		return getString("param18");
	}

	/**
	 * Obtiene el entero del param1
	 * 
	 * @return
	 */
	public Integer getParam19Integer() {
		return getInteger("param19");
	}

	/**
	 * Obtiene el string del param1
	 * 
	 * @return
	 */
	public String getParam19String() {
		return getString("param19");
	}

	/**
	 * Obtiene el entero del param1
	 * 
	 * @return
	 */
	public Integer getParam20Integer() {
		return getInteger("param20");
	}

	/**
	 * Obtiene el string del param1
	 * 
	 * @return
	 */
	public String getParam20String() {
		return getString("param20");
	}

	public Integer getNumeroFilas() {
		return getInteger("numeroFilas");
	}

	public void setReportDocument(Object object) {
		setAtribute("reportDocument", object);
	}

	public void setParam1(Object object) {
		setAtribute("param1", object);
	}

	public void setParam2(Object object) {
		setAtribute("param2", object);
	}

	public void setParam3(Object object) {
		setAtribute("param3", object);
	}

	public void setParam4(Object object) {
		setAtribute("param4", object);
	}

	public void setParam5(Object object) {
		setAtribute("param5", object);
	}

	public void setParam6(Object object) {
		setAtribute("param6", object);
	}

	public void setParam7(Object object) {
		setAtribute("param7", object);
	}

	public void setParam8(Object object) {
		setAtribute("param8", object);
	}

	public void setParam9(Object object) {
		setAtribute("param9", object);
	}

	public void setParam10(Object object) {
		setAtribute("param10", object);
	}

	public void setParam11(Object object) {
		setAtribute("param11", object);
	}

	public void setParam12(Object object) {
		setAtribute("param12", object);
	}

	public void setParam13(Object object) {
		setAtribute("param13", object);
	}

	public void setParam14(Object object) {
		setAtribute("param14", object);
	}

	public void setParam15(Object object) {
		setAtribute("param15", object);
	}

	public void setParam16(Object object) {
		setAtribute("param16", object);
	}

	public void setParam17(Object object) {
		setAtribute("param17", object);
	}

	public void setParam18(Object object) {
		setAtribute("param18", object);
	}

	public void setParam19(Object object) {
		setAtribute("param19", object);
	}

	public void setParam20(Object object) {
		setAtribute("param20", object);
	}

	public void setIdDocumento(Object object) {
		setAtribute("idDocumento", object);
	}

	public void setTipoEntidad(Object object) {
		if ("POP".equals(getTipoPagina()))
			setAtribute("tipoEntidadPopup", object);
		else
			setAtribute("tipoEntidad", object);
	}

	public void setTipoPagina(Object object) {
		setAtribute("tipoPagina", object);
	}

	public void setNivelBloqueo(Object object) {
		setAtribute("nivelBloqueo", object);
	}

	public void setAccion(String action) {
		setAtribute("action", action);
	}

	public void setNombreArchivo(String nombreArchivo) {
		setAtribute("nameReport", nombreArchivo);
	}

	public void setPathFile(String nameFile) {
		setAtribute("name_file", nameFile);
	}

	public void setDescripcionEstado(String descripcionEstado) {
		setAtribute("descEstado", descripcionEstado);
	}

	public void setIdCambioEstado(Integer idCambioEstado) {
		setAtribute("idCambioEstado", idCambioEstado);
	}

	public void setIdDocumentoEntidad(Integer idCambioEstado) {
		setAtribute("idDocumentoEntidad", idCambioEstado);
	}

	public void setNumeroFilas(Integer numeroFilas) {
		setAtribute("numeroFilas", numeroFilas);
	}

	public final Integer getInteger(String nameParam) {
		Object object = getAtribute(nameParam);
		if (object != null) {
			return Integer.valueOf(object.toString());
		} else
			return null;
	}

	public final String getString(String nameParam) {

		Object object = getAtribute(nameParam);
		if (object != null) {
			return object.toString();
		} else
			return null;
	}

	/**
	 * Muestra los mensaje de error, operación con exito
	 * 
	 * @param message
	 *            mensaje definido en la clase {@link Mensaje}
	 * @param severity
	 *            tipo de mensaje defido en la calse {@link Mensaje} que hereda
	 *            de la clase {@link FacesMessage}
	 */
	public final void displayMessage(String message, FacesMessage.Severity severity) {
		FacesMessage facesMessage = new FacesMessage(severity, message, message);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}

	public Boolean getSoloLectura() {
		return soloLectura;
	}

	public void setSoloLectura(Boolean soloLectura) {
		this.soloLectura = soloLectura;
	}

}