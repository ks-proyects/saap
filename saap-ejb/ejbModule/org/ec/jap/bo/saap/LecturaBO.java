package org.ec.jap.bo.saap;

import java.util.List;

import javax.ejb.Local;

import org.ec.jap.dao.saap.LecturaDAO;
import org.ec.jap.entiti.saap.Lectura;
import org.ec.jap.entiti.saap.Usuario;

@Local
public interface LecturaBO extends LecturaDAO {

	/**
	 * Método que guarda las lecturas cundo aun no se cierra el periodo
	 * 
	 * @param usuario
	 * @param lecturas
	 * @return
	 * @throws Exception
	 */
	public String guardarLecturas(Usuario usuario, List<Lectura> lecturas) throws Exception;

	/**
	 * Método que guarda las lecturas que estan sin tomar la lectura y las que
	 * aun no han sido pagadas
	 * 
	 * @param usuario
	 * @param lecturas
	 * @return
	 * @throws Exception
	 */
	String guardarLecturasCerradas(Usuario usuario, List<Lectura> lecturas) throws Exception;

	/**
	 * Método que realizar el recalculo de los valores de la lectura
	 * 
	 * @param usuario
	 * @param lectura
	 * @return
	 * @throws Exception
	 */
	Lectura recalcularLectura(Usuario usuario, Lectura lectura) throws Exception;
}
