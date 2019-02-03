package org.ec.jap.bo.saap;

import java.util.List;

import javax.ejb.Local;

import org.ec.jap.entiti.dto.ReporteCabeceraDTO;
import org.ec.jap.entiti.dto.ReporteDetalleDTO;
import org.ec.jap.entiti.dto.ReportePieDTO;

@Local
public interface ReporteBirtBO {

	public ReporteCabeceraDTO getReporteCabecera(Integer idUser, Integer idReporte) throws Exception;

	public List<ReporteDetalleDTO> getReporteDetalle(Integer idUser, Integer idReporte) throws Exception;

	public ReportePieDTO getReportePie(Integer idUser, Integer idReporte) throws Exception;

}
