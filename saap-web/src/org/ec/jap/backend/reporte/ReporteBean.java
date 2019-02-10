package org.ec.jap.backend.reporte;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;

import org.ec.jap.backend.pagina.Bean;
import org.ec.jap.backend.utilitario.JassperConfigUtil;
import org.ec.jap.backend.utilitario.Mensaje;
import org.ec.jap.bo.saap.ParametroBO;
import org.ec.jap.enumerations.OutputType;
import org.ec.jap.utilitario.Impresora;

@ManagedBean
@ViewScoped
public class ReporteBean extends Bean {

	@Resource(mappedName = "java:/Saap")
	DataSource ds;

	@EJB
	private ParametroBO parametroBO;

	public ReporteBean() {

	}

	public String report() {
		OutputType formato = getFormatDocument();
		try {
			exportarReporte(getPage().getIdElementoSistema(), formato);
		} catch (Exception e) {
			displayMessage(e.getMessage(), Mensaje.SEVERITY_ERROR);
			return getPage().getOutcome();
		}
		return getPage().getOutcome();

	}

	protected String getCompileDir() {
		return "/tools/reportes/";
	}

	protected String getImageDir() {
		return "/tools/imagenes/";
	}

	protected void exportarReporte(Integer idReporte, OutputType formato) throws Exception {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ServletContext context = (ServletContext) externalContext.getContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
		ServletOutputStream servletOutputStream = response.getOutputStream();
		JassperConfigUtil.compileReport(context, getCompileDir(), getCompileFileName(idReporte));
		File reportFile = new File(JassperConfigUtil.getJasperFilePath(context, getCompileDir(), getCompileFileName(idReporte) + ".jasper"));
		String rutaImagen = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/" + request.getContextPath() + "/" + getImageDir() + "/" + "logo.png";
		Connection conn = ds.getConnection();
		facesContext.responseComplete();
		JasperPrint jasperPrint = JassperConfigUtil.fillReport(reportFile, getReportParameters(idReporte, rutaImagen), conn);
		if (formato.equals(OutputType.XLS)) {
			JassperConfigUtil.exportReportAsExcel(jasperPrint, response, getNombreArchivo() + formato.getDescripcion());
		} else if (formato.equals(OutputType.PDF)) {
			JassperConfigUtil.exportReportAsPDF(jasperPrint, response, getNombreArchivo() + formato.getDescripcion());
		} else if (formato.equals(OutputType.DOC)) {
			JassperConfigUtil.exportReportAsWord(jasperPrint, response, getNombreArchivo() + formato.getDescripcion());
		} else if (formato.equals(OutputType.PRINT)) {
			String servidor = parametroBO.getString("", getUsuarioCurrent().getIdComunidad().getIdComunidad(), "SERIMPR");
			String nombreImpresora = parametroBO.getString("", getUsuarioCurrent().getIdComunidad().getIdComunidad(), "NOMIMPR");
			JassperConfigUtil.exportReportAsPrint(jasperPrint, Impresora.getImpresora((servidor != null ? servidor : "") + nombreImpresora),parametroBO.getInteger("", getUsuarioCurrent().getIdComunidad().getIdComunidad(), "NUMCOP"));
		} else {
			request.getSession().setAttribute(BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
			response.sendRedirect(request.getContextPath() + "/servlets/report/" + formato);
		}
		servletOutputStream.flush();
		servletOutputStream.close();
	}

	protected String getCompileFileName(Integer idReport) {
		switch (idReport) {
		case 90:
			// Parametro que indica el método de calculo de los metros
			// consumidos
			return "SI".equalsIgnoreCase(getAtribute("aplicaMA").toString()) ? "ReciboPagoA" : "ReciboPago";
		case 18:
			return "Usuario";
		case 148:
			return "Usuario";
		case 243:
			return "Asistencia";
		case 290:
			return "AsistenciaUsuarios";
		case 295:
			return "ReporteCaja";
		case 268:
			return "ReporteCajaAnual";
		case 29:
			return "UsuarioLlaveLectura";
		case 93:
			return "UsuarioPago";
		case 273:
			return "IngresoMensual";
		case 311:
			return "ReporteConsumo";
		default:
			return "ReciboPagoA";
		}
	}

	protected Map<String, Object> getReportParameters(Integer idReport, String rutaImagen) {
		Map<String, Object> reportParameters = new HashMap<String, Object>();
		reportParameters.put("ID_REPORT", idReport);
		reportParameters.put("ID_USER", getUsuarioCurrent().getIdUsuario());
		reportParameters.put("PATH_IMAGE", rutaImagen);
		return reportParameters;
	}

}