/**
 * 
 */
package org.ec.jap.utilitario;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Freddy G Castillo C
 * 
 */
public class UtilitarioFecha {

	/**
	 * Cálcula la edad de una persona en base a la fecha de nacimiento
	 * @param fechaNaciemiento
	 * @return
	 * @throws Exception
	 */
	public static Integer getEdad(Date fechaNaciemiento) throws Exception {
		Calendar fechaAct = Calendar.getInstance();
		fechaAct.setTime(Calendar.getInstance().getTime());

		Calendar fechaNac = Calendar.getInstance();
		fechaNac.setTime(fechaNaciemiento);

		int dif_anios = fechaAct.get(Calendar.YEAR) - fechaNac.get(Calendar.YEAR);
		int dif_meses = fechaAct.get(Calendar.MONTH) - fechaNac.get(Calendar.MONTH);
		int dif_dias = fechaAct.get(Calendar.DAY_OF_MONTH) - fechaNac.get(Calendar.DAY_OF_MONTH);

		// Si está en ese año pero todavía no los ha cumplido
		if (dif_meses < 0 || (dif_meses == 0 && dif_dias < 0)) {
			dif_anios--;
		}
		return dif_anios;
	}
}
