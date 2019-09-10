package org.ec.jap.backend.pago;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.saap.CabeceraPlanillaBO;
import org.ec.jap.bo.saap.DetallePlanillaBO;
import org.ec.jap.bo.saap.PeriodoPagoBO;
import org.ec.jap.bo.saap.RegistroEconomicoBO;
import org.ec.jap.entiti.saap.CabeceraPlanilla;
import org.ec.jap.entiti.saap.DetallePlanilla;
import org.ec.jap.entiti.sistema.Filtro;

@ManagedBean
@ViewScoped
public class PlanillaEditBean extends Bean {

	/**
	 * param1->idPlanilla
	 */

	@EJB
	CabeceraPlanillaBO cabeceraPlanillaBO;

	@EJB
	DetallePlanillaBO detallePlanillaBO;

	@EJB
	PeriodoPagoBO periodoPagoBO;

	private CabeceraPlanilla cp;
	private List<DetallePlanilla> dp;
	protected Filtro filtro;

	public PlanillaEditBean() {
		super();
	}

	@PostConstruct
	@Override
	public void init() {
		try {
			setTipoEntidad(getParam3String());
			super.init();
			search(null);
			redisplayActions(cp.getEstado(), cp.getIdCabeceraPlanilla());
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}

	}

	@Override
	public void search(ActionEvent event) {
		try {

			String aplicaMetodoCobroAnt = parametroBO.getString("", getUsuarioCurrent().getIdComunidad().getIdComunidad(), "APLTITAA");
			setAtribute("aplicaMA", aplicaMetodoCobroAnt);
			if ("INS".equals(getAccion())) {
				cp = new CabeceraPlanilla();
			} else {
				filtro = filtroBO.getFiltro(getUsuarioCurrent(), "IDCABPLA", getPage(), getParam2Integer(), filtro, true);
				cp = cabeceraPlanillaBO.findByPk(getParam2Integer());
				map = new HashMap<>();
				map.put("idCabeceraPlanilla", cp);
				dp = detallePlanillaBO.findAllByNamedQuery("DetallePlanilla.findByCabecara", map);
			}
			// Se mostrarn solo para cuando esta en proceso de pago
			String pageLasT = getLastOutcome().getOutcome();

			redisplayAction(11, "pagoList".equals(pageLasT));
			redisplayAction(2, "pagoList".equals(pageLasT));
			redisplayAction(8, !"ING".equalsIgnoreCase(cp.getEstado()));
			String nombreFile = "JAAP_" + GregorianCalendar.getInstance().getTimeInMillis();
			setNombreArchivo(nombreFile);

		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	@EJB
	RegistroEconomicoBO registroEconomicoBO;

	@Override
	public String insert() {
		setAccion("INS");
		return "detalleEdit?faces-redirect=true";
	}

	public CabeceraPlanilla getCp() {
		return cp;
	}

	public void setCp(CabeceraPlanilla cp) {
		this.cp = cp;
	}

	public List<DetallePlanilla> getDp() {
		return dp;
	}

	public void setDp(List<DetallePlanilla> dp) {
		this.dp = dp;
	}

}
