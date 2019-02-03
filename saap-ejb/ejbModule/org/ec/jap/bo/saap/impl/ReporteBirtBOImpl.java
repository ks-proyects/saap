package org.ec.jap.bo.saap.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.ec.jap.bo.saap.ReporteBirtBO;
import org.ec.jap.entiti.dto.ReporteCabeceraDTO;
import org.ec.jap.entiti.dto.ReporteDetalleDTO;
import org.ec.jap.entiti.dto.ReportePieDTO;

@Stateless
public class ReporteBirtBOImpl implements ReporteBirtBO {

	@PersistenceContext
	EntityManager entityManager;
	private static final String REPORT_SP = "SP";
	private static final String REPORT_NATIVE_QUERY = "NATIVE QUERY";
	private static final String REPORT_JPA = "JPA";

	public ReporteBirtBOImpl() {

	}

	@SuppressWarnings("unchecked")
	public ReporteCabeceraDTO getReporteCabecera(Integer idUser, Integer idReporte) throws Exception {
		String origenReporte = REPORT_SP;
		ReporteCabeceraDTO reporteCabeceraDTO = new ReporteCabeceraDTO();
		List<ReporteCabeceraDTO> reporteCabeceraDTOs = new ArrayList<ReporteCabeceraDTO>();
		try {
			if (origenReporte.equals(REPORT_SP)) {
				Query query = entityManager.createNativeQuery("SELECT * FROM  reporte_cabecera(?,?); ", ReporteCabeceraDTO.class);
				query.setParameter(1, idUser);
				query.setParameter(2, idReporte);
				reporteCabeceraDTOs = query.getResultList();
				if (!reporteCabeceraDTOs.isEmpty())
					reporteCabeceraDTO = reporteCabeceraDTOs.get(0);
			}

		} catch (Exception e) {
			throw e;
		}

		return reporteCabeceraDTO;
	}

	@SuppressWarnings("all")
	public List<ReporteDetalleDTO> getReporteDetalle(Integer idUser, Integer idReporte) throws Exception {
		String origenReporte = REPORT_SP;
		List<ReporteDetalleDTO> reporteDetalleDTOs = new ArrayList<ReporteDetalleDTO>();
		try {
			if (origenReporte.equals(REPORT_SP)) {
				Query query = entityManager.createNativeQuery("SELECT * FROM reporte_detalle(?,?); ", ReporteDetalleDTO.class);
				query.setParameter(1, idUser);
				query.setParameter(2, idReporte);
				reporteDetalleDTOs = query.getResultList();
			}

		} catch (Exception e) {
			throw e;
		}
		return reporteDetalleDTOs;
	}

	@SuppressWarnings("unchecked")
	public ReportePieDTO getReportePie(Integer idUser, Integer idReporte) throws Exception {
		String origenReporte = REPORT_SP;
		ReportePieDTO reportePieDTO = new ReportePieDTO();
		List<ReportePieDTO> reportePieDTOs = new ArrayList<ReportePieDTO>();

		try {

			switch (idReporte) {

			default:
				origenReporte = REPORT_SP;
			}

			if (origenReporte.equals(REPORT_SP)) {
				Query query = entityManager.createNativeQuery("execute data.ReportePie :idUser,::idReporte ", ReportePieDTO.class);

				query.setParameter("idUser", idUser);
				query.setParameter("idReporte", idReporte);

				reportePieDTOs = query.getResultList();

				if (!reportePieDTOs.isEmpty())
					reportePieDTO = reportePieDTOs.get(0);
			}

			if (origenReporte.equals(REPORT_JPA)) {
			}

			if (origenReporte.equals(REPORT_NATIVE_QUERY)) {
			}

		} catch (Exception e) {
			throw e;
		}

		return reportePieDTO;
	}

}
