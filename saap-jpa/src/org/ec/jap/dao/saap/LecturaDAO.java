package org.ec.jap.dao.saap;

import java.util.List;

import org.ec.jap.dao.sistema.Sistema;
import org.ec.jap.entiti.saap.Lectura;
import org.ec.jap.entiti.saap.Llave;

/**
 * Interfaz de Acceso a Datos de {@link Lectura}
 * 
 * @author Freddy
 * @version {@code 1.0}
 */
public interface LecturaDAO extends Sistema<Lectura, Integer> {

	/**
	 * 
	 * @param idLlave
	 * @return
	 * @throws Exception
	 */
	List<Lectura> getLast3(Llave idLlave,Lectura actual) throws Exception;
}
