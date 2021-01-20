package org.ec.jap.bo.saap;

import java.util.List;

import javax.ejb.Local;

import org.ec.jap.dao.saap.LecturaDAO;
import org.ec.jap.entiti.saap.Lectura;
import org.ec.jap.entiti.saap.Llave;
import org.ec.jap.entiti.saap.PeriodoPago;
import org.ec.jap.entiti.saap.Usuario;

@Local
public interface LecturaBO extends LecturaDAO {

	/**
	 * Método que guarda las lecturas que estan sin tomar la lectura y las que aun
	 * no han sido pagadas
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

	Integer findLecturaAnterior(Llave llave, Boolean usuarioNuevo) throws Exception;

	/**
	 * Genera una lectura con valores por defecto y la lectura anterior
	 * 
	 * @param periodoPago
	 * @param llave
	 * @param usuario
	 * @throws Exception
	 */
	void iniciarLecturaCero(PeriodoPago periodoPago, Llave llave, Usuario usuario) throws Exception;

	/**
	 * Método que guarda las lecturas cundo aun no se cierra el periodo
	 * 
	 * @param usuario
	 * @param lecturas
	 * @param pp
	 * @return
	 * @throws Exception
	 */
	String guardarLecturas(Usuario usuario, List<Lectura> lecturas, PeriodoPago pp) throws Exception;
}
