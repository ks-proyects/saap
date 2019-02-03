package org.ec.jap.bo.saap;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import org.ec.jap.entiti.dto.ConsumoDTO;
import org.ec.jap.xmlaccessortype.Pie;

@Local
public interface ReporteCajaBO {

	public Pie getReporteIngresos(Integer anio, Integer mes) throws Exception;

	Pie getReporteSalidasIngresos(Integer anio, Integer mes) throws Exception;

	Pie getReporteSalidas(Integer anio, Integer mes) throws Exception;

	List<Object[]> getReporteIngresoSalida(Integer anio) throws Exception;

	List<Object[]> getReporteIngresoSlidaAcumulativo(Integer anio)
			throws Exception;

	Map<String, Double[]> getTipoRegistro(String tipoRegistro) throws Exception;

	List<ConsumoDTO> getReporteConsumo(Integer anio,Integer idTrifa) throws Exception;

}
