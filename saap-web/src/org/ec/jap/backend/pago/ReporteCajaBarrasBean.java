/**
 * 
 */
package org.ec.jap.backend.pago;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.saap.ReporteCajaBO;
import org.ec.jap.entiti.sistema.Filtro;
import org.ec.jap.enumerations.AccionType;
import org.ec.jap.utilitario.Utilitario;

/**
 * @author Freddy G Castillo C
 * 
 */
@ManagedBean
@ViewScoped
public class ReporteCajaBarrasBean extends Bean {

	@EJB
	ReporteCajaBO reporteCajaBO;

	private Filtro anio;

	private Map<String, Double[]> medalsData = new LinkedHashMap<String, Double[]>();
	private Map<String, Double[]> medalsDataHist = new LinkedHashMap<String, Double[]>();

	private List<Object[]> objects;

	public ReporteCajaBarrasBean() {
		super();
	}

	@PostConstruct
	public void init() {
		try {
			super.init();
			search(null);
		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	public void search(ActionEvent event) {
		try {
			setNombreArchivo("Ingresos_Mensual");
			anio = filtroBO.getFiltro(getUsuarioCurrent(), "ANIO", getPage(), anio != null ? anio.getValorEntero() : 0, anio, false);

			medalsData = new LinkedHashMap<String, Double[]>();
			medalsDataHist = new LinkedHashMap<String, Double[]>();
			objects = reporteCajaBO.getReporteIngresoSalida(anio == null ? 0 : anio.getValorEntero());
			Integer anio = 0;
			Integer mes = 0;
			String mesDesc = "";
			Double valor = 0.0;
			String descricpionGenerica = null;
			int size = getNames().size();
			Double[] data = new Double[size];
			Double[] dataLast = new Double[size];
			int i = 0;
			for (Object[] object : objects) {
				anio = (Integer) object[0];
				mes = (Integer) object[1];
				mesDesc = Utilitario.mes(mes);
				String descripcion = String.valueOf(anio) + "-" + mesDesc;
				valor = Utilitario.redondear((Double) object[3]);
				if (descripcion.equals(descricpionGenerica)) {
					data[i] = Utilitario.redondear(valor);
					dataLast[i] = Utilitario.redondear((dataLast[i] == null ? 0.0 : dataLast[i]) + valor);
					i++;
				} else {
					if (i > 1) {
						data[i] = Utilitario.redondear(data[i - 2] - data[i - 1]);
						dataLast[i] = Utilitario.redondear(dataLast[i - 2] - dataLast[i - 1]);
					}
					if (descricpionGenerica != null && !descripcion.equals(descricpionGenerica)) {
						if (data[1] == null) {
							data[1] = 0.0;
						}
						if (data[2] == null) {
							data[2] = data[0];
						}
						if (dataLast[1] == null) {
							dataLast[1] = 0.0;
						}
						if (dataLast[2] == null) {
							dataLast[2] = dataLast[0];
						}

						medalsData.put(descricpionGenerica, data);
						Double[] dataLastAux = new Double[size];
						dataLastAux[0] = dataLast[0];
						dataLastAux[1] = dataLast[1];
						dataLastAux[2] = dataLast[2];
						medalsDataHist.put(descricpionGenerica, dataLastAux);
						i = 0;
						data = new Double[size];
					}

					descricpionGenerica = descripcion;
					data[i] = Utilitario.redondear(valor);
					dataLast[i] = Utilitario.redondear((dataLast[i] == null ? 0.0 : dataLast[i]) + valor);

					i++;
				}
			}
			if (data.length >= 2 && data[i - 1] != null) {
				data[i] = Utilitario.redondear(data[i - 2] - data[i - 1]);
				dataLast[i] = Utilitario.redondear(dataLast[i - 2] - dataLast[i - 1]);
			}
			if (descricpionGenerica != null) {
				medalsData.put(descricpionGenerica, data);
				Double[] dataLastAux = new Double[size];
				dataLastAux[0] = dataLast[0];
				dataLastAux[1] = dataLast[1];
				dataLastAux[2] = dataLast[2];
				medalsDataHist.put(descricpionGenerica, dataLastAux);
			}

		} catch (Exception e) {
			e.printStackTrace();
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
		}
	}

	public ArrayList<String> getColors() {
		ArrayList<String> colors = new ArrayList<String>();
		colors.clear();
		colors.add("#DAA520");
		colors.add("#B43104");
		colors.add("#2E64FE");
		return colors;
	}

	public ArrayList<String> getNames() {
		ArrayList<String> names = new ArrayList<String>();
		Set<String> strings = new HashSet<>();
		for (Object[] object : objects) {
			AccionType accionType = (AccionType) object[2];
			strings.add(accionType.getDescripcion());
		}
		names.addAll(strings);
		names.add("Saldo");

		return names;
	}

	public ArrayList<String> getColorsCapital() {
		ArrayList<String> colors = new ArrayList<String>();
		colors.clear();
		colors.add("#DAA520");
		colors.add("#B43104");
		colors.add("#2E64FE");
		return colors;
	}

	public ArrayList<String> getNamesCapital() {
		ArrayList<String> names = new ArrayList<String>();
		Set<String> strings = new HashSet<>();
		for (Object[] object : objects) {
			AccionType accionType = (AccionType) object[2];
			strings.add(accionType.getDescripcion());
		}
		names.addAll(strings);
		names.add("Capital");

		return names;
	}

	public Map<String, Double[]> getMedalsData() {
		return medalsData;
	}

	public Map<String, Double[]> getMedalsDataHist() {
		return medalsDataHist;
	}

	public Filtro getAnio() {
		return anio;
	}

	public void setAnio(Filtro anio) {
		this.anio = anio;
	}

	public List<SelectItem> getAnios() {
		List<SelectItem> items = new ArrayList<>();
		try {
			items = getSelectItems(getUsuarioCurrent(), null, true, "ListaValor.findAnioPeriodo");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

}
