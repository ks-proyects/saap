package org.ec.jap.utilitario;

import java.util.ArrayList;
import java.util.List;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

public class Impresora {

	public static PrintService getImpresora(String nombreImpresora) throws Exception {
		List<PrintService> services = getImpresoras();
		if (services.size() > 0) {
			for (PrintService printService : services) {
				if (printService.getName().equalsIgnoreCase(nombreImpresora)) {
					return printService;
				}
			}
		}
		System.out.println("No se encuntra la impresora "+nombreImpresora);
		return null;
	}

	public static List<PrintService> getImpresoras() throws Exception {
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		List<PrintService> printServices = new ArrayList<>();
		for (int i = 0; i < services.length; i++) {
			printServices.add(services[i]);
		}
		return printServices;
	}
}
