/**
 * 
 */
package org.ec.jap.backend.utilitario;

import java.io.File;
import java.sql.Connection;
import java.util.Map;

import javax.print.PrintService;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporterParameter;

/**
 * @author Freddy
 * 
 */
public class JassperConfigUtil {
	/**
	 * PRIVATE METHODS
	 */
	private static void setCompileTempDir(ServletContext context, String uri) {
		System.setProperty("jasper.reports.compile.temp", context.getRealPath(uri));
	}

	/**
	 * PUBLIC METHODS
	 */
	public static boolean compileReport(ServletContext context, String compileDir, String filename) throws JRException {
		String jasperFileName = context.getRealPath(compileDir + filename + ".jasper");
		File jasperFile = new File(jasperFileName);
		if (jasperFile.exists()) {
			return true; // jasper file already exists, do not compile again
		}
		try {
			// jasper file has not been constructed yet, so compile the xml file
			setCompileTempDir(context, compileDir);
			String xmlFileName = jasperFileName.substring(0, jasperFileName.indexOf(".jasper")) + ".jrxml";
			JasperCompileManager.compileReportToFile(xmlFileName);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static JasperPrint fillReport(File reportFile, Map<String, Object> parameters, Connection conn) throws JRException {
		parameters.put("BaseDir", reportFile.getParentFile());
		JasperPrint jasperPrint = JasperFillManager.fillReport(reportFile.getPath(), parameters, conn);
		return jasperPrint;
	}

	public static String getJasperFilePath(ServletContext context, String compileDir, String jasperFile) {
		return context.getRealPath(compileDir + jasperFile);
	}

	public static void exportReportAsExcel(JasperPrint jasperPrint, HttpServletResponse response, String nombreFile) throws Exception {
		response.setHeader("Content-disposition", "attachment; filename=" + nombreFile);
		response.setContentType("application/vnd.ms-excel");
		JRXlsExporter exporterXLS = new JRXlsExporter();
		exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
		exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, response.getOutputStream());
		exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
		exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
		exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
		exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		exporterXLS.exportReport();
	}

	public static void exportReportAsWord(JasperPrint jasperPrint, HttpServletResponse response, String nombreFile) throws Exception {
		response.setHeader("Content-disposition", "attachment; filename=" + nombreFile);
		response.setContentType("application/word");
		JRDocxExporter exportetDOC = new JRDocxExporter();
		exportetDOC.setParameter(JRDocxExporterParameter.JASPER_PRINT, jasperPrint);
		exportetDOC.setParameter(JRDocxExporterParameter.OUTPUT_STREAM, response.getOutputStream());
		exportetDOC.exportReport();
	}

	public static void exportReportAsPDF(JasperPrint jasperPrint, HttpServletResponse response, String nombreFile) throws Exception {
		response.setHeader("Content-disposition", "attachment; filename=" + nombreFile);
		response.setContentType("application/pdf");
		JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
	}

	public static void exportReportAsPrint(JasperPrint jasperPrint, PrintService printService) throws Exception {
		if (printService != null) {
			JRExporter jrExporter = new JRPrintServiceExporter();
			jrExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			jrExporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, printService);
			jrExporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printService.getAttributes());
			jrExporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
			jrExporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
			jrExporter.exportReport();
		} else {
			throw new Exception("No se puede establecer conección con la impresora configurada");
		}
	}
}
