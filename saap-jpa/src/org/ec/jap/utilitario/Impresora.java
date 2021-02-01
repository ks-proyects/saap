package org.ec.jap.utilitario;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;

public class Impresora {

	public static Logger log = Logger.getLogger(Impresora.class.getName());
	public static PrintService getImpresora(String nombreImpresora, int numCopies) throws Exception {
		List<PrintService> services = getImpresoras(numCopies);
		if (services.size() > 0) {
			for (PrintService printService : services) {
				log.info("Impresora: "+printService.getName());
				if (printService.getName().equalsIgnoreCase(nombreImpresora)) {
					return printService;
				}
			}
		}
		log.info("No se encuntra la impresora " + nombreImpresora);
		return null;
	}

	public static List<PrintService> getImpresoras(int numCopies) throws Exception {
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		aset.add(new Copies(numCopies));
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, aset);
		List<PrintService> printServices = new ArrayList<>();
		for (int i = 0; i < services.length; i++) {
			printServices.add(services[i]);
		}
		return printServices;
	}
}
