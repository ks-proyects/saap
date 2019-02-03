package org.ec.jap.bo.saap.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.ec.jap.bo.saap.DetallePlanillaBO;
import org.ec.jap.bo.saap.GastoBO;
import org.ec.jap.bo.saap.RegistroEconomicoBO;
import org.ec.jap.bo.saap.ReporteCajaBO;
import org.ec.jap.bo.saap.TipoRegistroBO;
import org.ec.jap.entiti.dto.ConsumoDTO;
import org.ec.jap.entiti.saap.TipoRegistro;
import org.ec.jap.enumerations.AccionType;
import org.ec.jap.utilitario.Utilitario;
import org.ec.jap.xmlaccessortype.Pie;
import org.ec.jap.xmlaccessortype.Slice;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class ReporteCajaBOImpl implements ReporteCajaBO {

	@EJB
	RegistroEconomicoBO registroEconomicoBO;

	@EJB
	TipoRegistroBO tipoRegistroBO;

	@EJB
	DetallePlanillaBO detallePlanillaBO;

	@EJB
	GastoBO gastoBO;

	/**
	 * Default constructor.
	 */
	public ReporteCajaBOImpl() {
	}

	@Override
	public Pie getReporteIngresos(Integer anio, Integer mes) throws Exception {
		HashMap<String, Object> map = new HashMap<>();
		map.put("accion", AccionType.I);
		List<TipoRegistro> ingresos = tipoRegistroBO.findAllByNamedQuery("TipoRegistro.findByTipo", map);
		map.clear();
		map.put("anio", anio);
		map.put("mes", mes);

		List<Object[]> list = new ArrayList<>(0);// =
													// registroEconomicoBO.findObjects("RegistroEconomico.findByAnioAndMes",
													// map);
		Pie pie = new Pie();
		List<Slice> slices = new ArrayList<>(0);
		Boolean isFirst = true;

		Double valor = 0.0;
		for (TipoRegistro tipoRegistro : ingresos) {
			Object[] objects = new Object[2];
			map.put("tipoRegistro", tipoRegistro);
			objects[0] = tipoRegistro.getDescripcion();
			switch (tipoRegistro.getTipoRegistro()) {
			case "CONS":
				valor = (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findConsumos", map);
				valor += (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findConsumosAnt", map);
				break;
			case "CUO":
				valor = (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findCuotas", map);
				break;
			case "CUOINI":
				valor = (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findCuotas", map);
				break;
			case "INASIS":
				valor = (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findCuotaInasistencia", map);
				break;
			case "IOTR":
				valor = (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findCuotaInasistencia", map);
				break;
			case "MUL":
				valor = (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findCuotas", map);
				break;
			case "MULAGU":
				valor = (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findCuotas", map);
				break;
			default:
				break;
			}
			valor = Utilitario.redondear(valor);
			objects[1] = valor != null ? valor : 0.0;
			list.add(objects);
		}

		for (Object[] object : list) {
			Slice slice = new Slice();
			if (isFirst) {
				slice.setPullOut(isFirst);
				isFirst = false;
			}
			String title = (String) object[0];
			Double value = (Double) object[1];
			slice.setTitle(title + "(" + String.valueOf(value) + ")");
			slice.setValue(BigDecimal.valueOf(value));
			slices.add(slice);
		}
		pie.setSlice(slices);
		return pie;
	}

	@Override
	public Pie getReporteSalidas(Integer anio, Integer mes) throws Exception {
		HashMap<String, Object> map = new HashMap<>();
		map.put("accion", AccionType.S);
		List<TipoRegistro> ingresos = tipoRegistroBO.findAllByNamedQuery("TipoRegistro.findByTipo", map);
		map.clear();
		map.put("anio", anio);
		map.put("mes", mes);

		List<Object[]> list = new ArrayList<>(0);// =
													// registroEconomicoBO.findObjects("RegistroEconomico.findByAnioAndMes",
													// map);
		Pie pie = new Pie();
		List<Slice> slices = new ArrayList<>(0);
		Boolean isFirst = true;

		Double valor = 0.0;
		for (TipoRegistro tipoRegistro : ingresos) {
			Object[] objects = new Object[2];
			map.put("tipoRegistro", tipoRegistro);
			objects[0] = tipoRegistro.getDescripcion();
			switch (tipoRegistro.getTipoRegistro()) {
			case "GAST":
				valor = (Double) gastoBO.findDoubleByNamedQuery("Gasto.findAllGastos", map);
				break;
			case "CUEPAG":
				valor = (Double) registroEconomicoBO.findDoubleByNamedQuery("RegistroEconomico.findCuentasPorPaga", map);
				break;

			default:
				break;
			}
			objects[1] = valor != null ? valor : 0.0;
			list.add(objects);
		}

		for (Object[] object : list) {
			Slice slice = new Slice();
			if (isFirst) {
				slice.setPullOut(isFirst);
				isFirst = false;
			}
			String title = (String) object[0];
			Double value = (Double) object[1];
			slice.setTitle(title + "(" + String.valueOf(value) + ")");
			slice.setValue(BigDecimal.valueOf(value));
			slices.add(slice);
		}
		pie.setSlice(slices);
		return pie;
	}

	private Double getSum(Pie pie) throws Exception {
		Double valor = 0.0;
		for (Slice reg : pie.getSlice()) {
			valor += reg.getValue().doubleValue();
		}
		valor = Utilitario.redondear(valor);
		return valor;
	}

	@Override
	public Pie getReporteSalidasIngresos(Integer anio, Integer mes) throws Exception {

		Pie pieIngresos = getReporteIngresos(anio, mes);
		Pie pieSalidad = getReporteSalidas(anio, mes);

		Pie pie = new Pie();
		List<Slice> slices = new ArrayList<>(0);

		Slice slice = new Slice();
		slice.setPullOut(true);
		Double value = getSum(pieIngresos);
		slice.setTitle(AccionType.I.getDescripcion() + "(" + String.valueOf(value) + ")");
		slice.setValue(BigDecimal.valueOf(value));
		slices.add(slice);
		Slice sliceSalidas = new Slice();
		value = getSum(pieSalidad);
		sliceSalidas.setTitle(AccionType.S.getDescripcion() + "(" + String.valueOf(value) + ")");
		sliceSalidas.setValue(BigDecimal.valueOf(value));
		slices.add(sliceSalidas);

		pie.setSlice(slices);
		return pie;
	}

	@Override
	public List<Object[]> getReporteIngresoSalida(Integer anio) throws Exception {
		HashMap<String, Object> map = new HashMap<>();
		map.put("anio", anio);
		List<Object[]> list = registroEconomicoBO.findObjects("RegistroEconomico.findAllByYear", map);
		for (Object[] objects : list) {
			Integer anio_ = objects[0] != null ? Integer.valueOf(objects[0].toString()) : 0;
			Integer mes_ = objects[1] != null ? Integer.valueOf(objects[1].toString()) : -1;
			AccionType accionType = objects[2] != null ? AccionType.valueOf(objects[2].toString()) : AccionType.I;
			if (AccionType.I.equals(accionType)) {
				objects[3] = getSum(getReporteIngresos(anio_, mes_));
			} else {
				objects[3] = getSum(getReporteSalidas(anio_, mes_));
			}
		}
		return list;
	}

	public Double getIngresosAcumulativo(Integer anio, Integer mes) throws Exception {
		Double valor = 0.0;
		HashMap<String, Object> map = new HashMap<>();
		map.put("accion", AccionType.I);
		List<TipoRegistro> ingresos = tipoRegistroBO.findAllByNamedQuery("TipoRegistro.findByTipo", map);
		map.clear();
		map.put("anio", anio);
		map.put("mes", mes);
		for (TipoRegistro tipoRegistro : ingresos) {
			map.put("tipoRegistro", tipoRegistro);
			switch (tipoRegistro.getTipoRegistro()) {
			case "CONS":
				valor += (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findConsumosAcum", map);
				valor += (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findConsumosAntAcum", map);

				break;
			case "CUO":
				valor += (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findAcumulativos", map);
				break;
			case "CUOINI":
				valor += (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findCuotasAcumulatovo", map);
				break;
			case "INASIS":
				valor += (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findCuotaInasistenciaAcumulat", map);
				break;
			case "IOTR":
				valor += (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findCuotaInasistenciaAcumulat", map);
				break;
			case "MUL":
				valor += (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findCuotasAcumulatovo", map);
				break;
			case "MULAGU":
				valor += (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findCuotasAcumulatovo", map);
				break;
			default:
				break;
			}
		}
		valor = Utilitario.redondear(valor);
		return valor;
	}

	public Double getSalidasAcomulativo(Integer anio, Integer mes) throws Exception {
		HashMap<String, Object> map = new HashMap<>();
		map.put("accion", AccionType.S);
		List<TipoRegistro> ingresos = tipoRegistroBO.findAllByNamedQuery("TipoRegistro.findByTipo", map);
		map.clear();
		map.put("anio", anio);
		map.put("mes", mes);
		Double valor = 0.0;
		for (TipoRegistro tipoRegistro : ingresos) {
			map.put("tipoRegistro", tipoRegistro);
			switch (tipoRegistro.getTipoRegistro()) {
			case "GAST":
				valor += (Double) gastoBO.findDoubleByNamedQuery("Gasto.findAllGastosAcum", map);
				break;
			case "CUEPAG":
				valor += (Double) registroEconomicoBO.findDoubleByNamedQuery("RegistroEconomico.findCuentasPorPagaAcumul", map);
				break;

			default:
				break;
			}
		}
		valor = Utilitario.redondear(valor);
		return valor;
	}

	@Override
	public List<Object[]> getReporteIngresoSlidaAcumulativo(Integer anio) throws Exception {
		HashMap<String, Object> map = new HashMap<>();
		map.put("anio", anio);
		List<Object[]> list = registroEconomicoBO.findObjects("RegistroEconomico.findAllToYear", map);
		for (Object[] objects : list) {
			Integer anio_ = objects[0] != null ? Integer.valueOf(objects[0].toString()) : 0;
			Integer mes_ = objects[1] != null ? Integer.valueOf(objects[1].toString()) : -1;
			AccionType accionType = objects[2] != null ? AccionType.valueOf(objects[2].toString()) : AccionType.I;
			if (AccionType.I.equals(accionType)) {
				objects[3] = getIngresosAcumulativo(anio_, mes_);
			} else {
				objects[3] = getSalidasAcomulativo(anio_, mes_);
			}
		}

		return list;
	}

	@Override
	public Map<String, Double[]> getTipoRegistro(String tipoRegistro) throws Exception {
		Map<String, Double[]> medalsData = new LinkedHashMap<String, Double[]>();
		if (tipoRegistro == null || "0".equals(tipoRegistro)) {
			List<TipoRegistro> ingresos = tipoRegistroBO.findAll();
			for (TipoRegistro tp : ingresos) {
				medalsData.putAll(getIngresosAcumulativo(tp));
			}

		} else {
			TipoRegistro registro = tipoRegistroBO.findByPk(tipoRegistro);
			medalsData.putAll(getIngresosAcumulativo(registro));
		}
		return medalsData;
	}

	public Map<String, Double[]> getIngresosAcumulativo(TipoRegistro tipoRegistro) throws Exception {
		Double valor = 0.0;
		Map<String, Double[]> medalsData = new LinkedHashMap<String, Double[]>();
		HashMap<String, Object> param = new HashMap<>();
		param.put("tipoRegistro", tipoRegistro);
		param.put("estado2", "INC");
		Double[] doubles = new Double[2];
		switch (tipoRegistro.getTipoRegistro()) {
		case "CONS":
			param.put("tipo", "I");
			param.put("estado1", "PAG");
			valor = (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findConsumosValor", param);
			valor += (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findConsumosAntValor", param);
			doubles[0] = Utilitario.redondear(valor);
			param.put("tipo", "S");
			param.put("estado1", "ING");

			valor = (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findConsumosValor", param);
			valor += (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findConsumosAntValor", param);
			doubles[1] = Utilitario.redondear(valor);
			break;
		case "CUO":
			param.put("tipo", "I");
			param.put("estado1", "PAG");
			valor = (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findValor", param);
			doubles[0] = Utilitario.redondear(valor);
			param.put("tipo", "S");
			param.put("estado1", "ING");
			valor = (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findValor", param);
			doubles[1] = Utilitario.redondear(valor);
			break;
		case "CUOINI":
			param.put("tipo", "I");
			param.put("estado1", "PAG");
			valor = (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findValor", param);
			doubles[0] = Utilitario.redondear(valor);
			param.put("tipo", "S");
			param.put("estado1", "ING");
			valor = (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findValor", param);
			doubles[1] = Utilitario.redondear(valor);
			break;
		case "INASIS":
			param.put("tipo", "I");
			param.put("estado1", "PAG");
			valor = (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findValorInasistencias", param);
			doubles[0] = Utilitario.redondear(valor);
			param.put("tipo", "S");
			param.put("estado1", "ING");
			valor = (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findValorInasistencias", param);
			doubles[1] = Utilitario.redondear(valor);
			break;
		case "IOTR":
			param.put("tipo", "I");
			param.put("estado1", "PAG");
			valor = (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findValor", param);
			doubles[0] = Utilitario.redondear(valor);
			param.put("tipo", "S");
			param.put("estado1", "ING");
			valor = (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findValor", param);
			doubles[1] = Utilitario.redondear(valor);
			break;
		case "MUL":
			param.put("tipo", "I");
			param.put("estado1", "PAG");
			valor = (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findValor", param);
			doubles[0] = Utilitario.redondear(valor);
			param.put("tipo", "S");
			param.put("estado1", "ING");
			valor = (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findValor", param);
			doubles[1] = Utilitario.redondear(valor);
			break;
		case "MULAGU":
			param.put("tipo", "I");
			param.put("estado1", "PAG");
			valor = (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findValor", param);
			doubles[0] = Utilitario.redondear(valor);
			param.put("tipo", "S");
			param.put("estado1", "ING");
			valor = (Double) detallePlanillaBO.findDoubleByNamedQuery("DetallePlanilla.findValor", param);
			doubles[1] = Utilitario.redondear(valor);
			break;
		default:
			break;
		}
		medalsData.put(tipoRegistro.getDescripcion(), doubles);

		return medalsData;
	}

	@Override
	public List<ConsumoDTO> getReporteConsumo(Integer anio,Integer idTrifa) throws Exception {
		
		List<ConsumoDTO> detallePlanillas = detallePlanillaBO.findAllEntentiByNamedQuery("ConsumoDTO.findConsumo", anio, anio, "A","B",idTrifa,idTrifa);
		
		return detallePlanillas;
	}

}
