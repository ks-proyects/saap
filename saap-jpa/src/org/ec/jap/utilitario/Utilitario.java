package org.ec.jap.utilitario;

import java.io.File;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.ec.jap.xmlaccessortype.Pie;

import sun.misc.BASE64Encoder;

public class Utilitario {

	private static List<SelectItem> items = new ArrayList<>();

	public static List<SelectItem> getMeses() {
		items = new ArrayList<>();
		items.add(new SelectItem(0, "Enero"));
		items.add(new SelectItem(1, "Febrero"));
		items.add(new SelectItem(2, "Marzo"));
		items.add(new SelectItem(3, "Abril"));
		items.add(new SelectItem(4, "Mayo"));
		items.add(new SelectItem(5, "Junio"));
		items.add(new SelectItem(6, "Julio"));
		items.add(new SelectItem(7, "Agosto"));
		items.add(new SelectItem(8, "Septiembre"));
		items.add(new SelectItem(9, "Octubre"));
		items.add(new SelectItem(10, "Noviembre"));
		items.add(new SelectItem(11, "Diciembre"));
		return items;
	}

	public static Integer numeroMeses(Date fechaDeInicio, Date fechaDeFin) {
		Calendar fechaInicio = Calendar.getInstance();
		Calendar fechaActual = Calendar.getInstance();
		fechaInicio.setTime(fechaDeInicio);
		fechaActual.setTime(fechaDeFin);

		Integer anioInicio = fechaInicio.get(Calendar.YEAR);
		Integer anioActual = fechaActual.get(Calendar.YEAR);
		Integer mesInicio = fechaInicio.get(Calendar.MONTH);
		Integer mesActual = fechaActual.get(Calendar.MONTH);
		Integer numeroMeses = 0;
		if (fechaInicio.before(fechaActual)) {
			if (anioActual > anioInicio) {
				numeroMeses = (anioActual - anioInicio) * 12 + (mesActual + 1);
			} else if (mesActual > mesInicio) {
				numeroMeses = (mesActual - mesInicio);
			}
		}
		return numeroMeses;
	}

	public static void generarXMLPie(String pathXml, Pie pie) throws Exception {
		File file = new File(pathXml);
		JAXBContext jaxbContext = JAXBContext.newInstance(Pie.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(pie, new FileOutputStream(file));
		// marshaller.marshal(pie, System.out);
	}

	public static String mes(Integer mes) throws Exception {
		switch (mes) {
		case 0:
			return "Enero";
		case 1:
			return "Febrero";
		case 2:
			return "Marzo";
		case 3:
			return "Abril";
		case 4:
			return "Mayo";
		case 5:
			return "Junio";
		case 6:
			return "Julio";
		case 7:
			return "Agosto";
		case 8:
			return "Septiembre";
		case 9:
			return "Octubre";
		case 10:
			return "Noviembre";
		case 11:
			return "Diciembre";
		default:
			return "";
		}
	}

	public static String descricionMes(Date fecha) throws Exception {
		String descricpion = "";
		Calendar fechaCal = Calendar.getInstance();
		fechaCal.setTime(fecha);
		//fechaCal.add(Calendar.MONTH, -1);
		Integer mesInt = fechaCal.get(Calendar.MONTH);
		switch (mesInt) {
		case 0:
			descricpion = "Enero - " + fechaCal.get(Calendar.YEAR);
			break;
		case 1:
			descricpion = "Febrero - " + fechaCal.get(Calendar.YEAR);
			break;
		case 2:
			descricpion = "Marzo - " + fechaCal.get(Calendar.YEAR);
			break;
		case 3:
			descricpion = "Abril - " + fechaCal.get(Calendar.YEAR);
			break;
		case 4:
			descricpion = "Mayo - " + fechaCal.get(Calendar.YEAR);
			break;
		case 5:
			descricpion = "Junio - " + fechaCal.get(Calendar.YEAR);
			break;
		case 6:
			descricpion = "Julio - " + fechaCal.get(Calendar.YEAR);
			break;
		case 7:
			descricpion = "Agosto - " + fechaCal.get(Calendar.YEAR);
			break;
		case 8:
			descricpion = "Septiembre - " + fechaCal.get(Calendar.YEAR);
			break;
		case 9:
			descricpion = "Octubre - " + fechaCal.get(Calendar.YEAR);
			break;
		case 10:
			descricpion = "Noviembre - " + fechaCal.get(Calendar.YEAR);
			break;
		case 11:
			descricpion = "Diciembre - " + fechaCal.get(Calendar.YEAR);
			break;
		default:
			descricpion = "";
		}
		return descricpion;
	}

	public static String mesFormat(Integer mes) throws Exception {
		switch (mes) {
		case 0:
			return "01";
		case 1:
			return "02";
		case 2:
			return "03";
		case 3:
			return "04";
		case 4:
			return "05";
		case 5:
			return "06";
		case 6:
			return "07";
		case 7:
			return "08";
		case 8:
			return "09";
		case 9:
			return "10";
		case 10:
			return "11";
		case 11:
			return "12";
		default:
			return "";
		}
	}

	public static Double redondear(Double d) throws Exception {
		if (d != null) {
			Double double1 = Math.rint(d * 100) / 100;
			return double1;
		} else
			return 0.0;

	}

	public static synchronized String getMD5_Base64(String input) {
		MessageDigest digest = null;

		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (digest == null) {
			return input;
		}

		// now everything is ok, go ahead
		try {
			digest.update(input.getBytes("UTF-8"));
		} catch (java.io.UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		byte[] rawData = digest.digest();
		BASE64Encoder bencoder = new BASE64Encoder();
		return bencoder.encode(rawData);
	}

	public static Boolean iniciaPeriodoActivo(Date fechaDeInicio, Integer diaDePago) {
		Boolean iniciaPeriodoActivo = false;
		Calendar fechaInicio = Calendar.getInstance();
		Calendar fechaActual = Calendar.getInstance();
		fechaInicio.setTime(fechaDeInicio);

		Integer anioInicio = fechaInicio.get(Calendar.YEAR);
		Integer anioActual = fechaActual.get(Calendar.YEAR);
		Integer mesInicio = fechaInicio.get(Calendar.MONTH);
		Integer mesActual = fechaActual.get(Calendar.MONTH);
		Integer diaActual = fechaActual.get(Calendar.DAY_OF_MONTH);
		if (fechaInicio.before(fechaActual)) {
			if (anioActual > anioInicio)
				iniciaPeriodoActivo = true;
			else if (mesActual > mesInicio)
				iniciaPeriodoActivo = true;
			else if (diaDePago > diaActual)
				iniciaPeriodoActivo = true;
		}

		return iniciaPeriodoActivo;
	}

	public static Boolean puedeAgregarNuevoPeriodo(Date fechaInicio) {
		Boolean puedeAgregarUnPeriodo = true;
		if (fechaInicio != null) {
			Calendar fechaAnterior = Calendar.getInstance();
			fechaAnterior.setTime(fechaInicio);
			Calendar fechaActual = Calendar.getInstance();
			fechaActual.add(Calendar.MONTH, 0);
			if (fechaAnterior.before(fechaActual)) {
				if (fechaAnterior.get(Calendar.YEAR) != fechaActual.get(Calendar.YEAR))
					puedeAgregarUnPeriodo = true;
				else if (fechaAnterior.get(Calendar.MONTH) != fechaActual.get(Calendar.MONTH))
					puedeAgregarUnPeriodo = true;
				else
					puedeAgregarUnPeriodo = false;
			} else {
				puedeAgregarUnPeriodo = false;
			}
			return puedeAgregarUnPeriodo;
		} else
			return puedeAgregarUnPeriodo;

	}

	public static void main(String[] arg) {
		System.out.println(Utilitario.getMD5_Base64("freddy"));

	}

}
