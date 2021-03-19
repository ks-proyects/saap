package org.ec.jap.bo.saap;

import javax.ejb.Local;

import org.ec.jap.dao.saap.CabeceraPlanillaDAO;
import org.ec.jap.entiti.saap.CabeceraPlanilla;
import org.ec.jap.entiti.saap.Lectura;
import org.ec.jap.entiti.saap.Servicio;
import org.ec.jap.entiti.saap.Usuario;

@Local
public interface CabeceraPlanillaBO extends CabeceraPlanillaDAO {

	/**
	 * Genera la cabecera de la planilla de los usuarios activos y que tienen
	 * una llave, esto se realiza al abrir un periodo de pago
	 * 
	 * @param usuario
	 * @param idPeriodoPago
	 * @throws Exception
	 */
	public void abrirPeriodoPago(Usuario usuario, Integer idPeriodoPago) throws Exception;

	/**
	 * Una vez ingresada todas las lecturas se debe cerrar el periodo en el cual
	 * se crea el detalle de las planillas para el pago respectivo
	 * 
	 * @param usuario
	 * @param idPeriodoPago
	 * @throws Exception
	 */
	public void cerrarPeriodoPago(Usuario usuario, Integer idPeriodoPago) throws Exception;

	/**
	 * Método que se invoca al finalizar un periodo de pago
	 * 
	 * @param usuario
	 * @param idPeriodoPago
	 * @throws Exception
	 */
	public void finalizarPlanilla(Usuario usuario, Integer idPeriodoPago) throws Exception;

	/**
	 * Método invocado al momento de presionar el boton pagar planilla
	 * 
	 * @param usuario
	 * @param idPeriodoPago
	 * @throws Exception
	 */
	public void pagarPlanilla(Usuario usuario, Integer idCabeceraPlanilla) throws Exception;

	/**
	 * Método encargado de regenerar las olanillas de los usuarios que no se
	 * generaron al momento de abrir el periodo
	 * 
	 * @param usuario
	 * @param idPeriodoPago
	 * @throws Exception
	 */
	public void regenerarPeriodoPagoAbierto(Usuario usuario, Integer idPeriodoPago) throws Exception;

	/**
	 * Método encargado de regenerar los calculos del consumo de agua potable de
	 * los ya existentes
	 * 
	 * @param usuario
	 * @param idPeriodoPago
	 * @throws Exception
	 */
	void regenerarPeriodoCerrado(Usuario usuario, Integer idPeriodoPago) throws Exception;

	/**
	 * Método que obtiene la planilla anterior de una llave
	 * 
	 * @param llave
	 * @return
	 * @throws Exception
	 */
	CabeceraPlanilla getAbonoMesAnterior(Servicio llave, CabeceraPlanilla actual) throws Exception;

	/**
	 * Método que cancela un pago realizado
	 * 
	 * @param usuario
	 * @param idPeriodoPago
	 * @throws Exception
	 */
	void descartarPago(Usuario usuario, Integer idPeriodoPago) throws Exception;

	/**
	 * Meotod que gestiona el proceso para guardar una planilla
	 * 
	 * @param usuario
	 * @throws Exception
	 */
	public void guardarPlanilla(Usuario usuario, Double valorAPagar, CabeceraPlanilla cp, CabeceraPlanilla anterior, Double valorAbono, Integer idCambioEstado, Object idDocumento) throws Exception;

	/**
	 * Método que recalcula los valores de una planilla
	 * 
	 * @param usuario
	 * @param cabeceraPlanilla
	 * @throws Exception
	 */
	void recalcularPlanilla(Usuario usuario, CabeceraPlanilla cabeceraPlanilla,Lectura lectura) throws Exception;

}
