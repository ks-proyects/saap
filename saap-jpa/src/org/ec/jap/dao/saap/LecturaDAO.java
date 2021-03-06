package org.ec.jap.dao.saap;

import java.util.List;

import org.ec.jap.dao.sistema.Sistema;
import org.ec.jap.entiti.saap.Lectura;
import org.ec.jap.entiti.saap.Servicio;

/**
 * Interfaz de Acceso a Datos de {@link Lectura}
 * 
 * @author Freddy
 * @version {@code 1.0}
 */
public interface LecturaDAO extends Sistema<Lectura, Integer> {

	/**
	 * 
	 * @param idServicio
	 * @return
	 * @throws Exception
	 */
	List<Lectura> getLast3(Servicio idServicio,Lectura actual) throws Exception;
}
