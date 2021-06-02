package org.ec.jap.bo.saap;

import java.util.List;

import javax.ejb.Local;

import org.ec.jap.dao.saap.DetallePlanillaDAO;
import org.ec.jap.entiti.saap.CabeceraPlanilla;
import org.ec.jap.entiti.saap.DetallePlanilla;
import org.ec.jap.entiti.saap.Lectura;
import org.ec.jap.entiti.saap.PeriodoPago;
import org.ec.jap.entiti.saap.RegistroEconomico;
import org.ec.jap.entiti.saap.Servicio;
import org.ec.jap.entiti.saap.Usuario;

@Local
public interface DetallePlanillaBO extends DetallePlanillaDAO {

	public void asignarMulta(Usuario usuario, CabeceraPlanilla planilla, RegistroEconomico registroEconomico)
			throws Exception;

	void quitarMulta(Usuario usuario, CabeceraPlanilla planilla, RegistroEconomico registroEconomico,
			DetallePlanilla detallePlanilla) throws Exception;

	Boolean noExisteDetalle(Integer idCabecera) throws Exception;

	/**
	 * Dscarta los detalles del pago
	 * 
	 * @param planilla
	 * @param usuario
	 * @throws Exception
	 */
	void descartarPago(CabeceraPlanilla planilla, Usuario usuario) throws Exception;

	DetallePlanilla crearDetalleAlcantarillado(Usuario currentUser, CabeceraPlanilla cp,
			RegistroEconomico registroEconomicoAlcantarillado, Integer cantidad, Double valor, String ppDescripcion,Servicio servicio)
			throws Exception;

	DetallePlanilla traspasarDetalle(CabeceraPlanilla planillaNueva, DetallePlanilla detallePlanilla) throws Exception;

	DetallePlanilla traspasarDetalleInconompleto(CabeceraPlanilla planillaNueva, DetallePlanilla detalleIncompleto) throws Exception;

	/**
	 * Obtiene los detalles de la plnilla no pagadas
	 * 
	 * @param planillaNoPagada
	 * @return
	 * @throws Exception
	 */
	List<DetallePlanilla> findDetalles(CabeceraPlanilla planillaNoPagada, String namedQuery) throws Exception;

	/**
	 * 
	 * @param planillaNoPagada
	 * @param planillaNueva
	 * @param multaAtrazoMes
	 * @param llave
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	Double crearMulta(CabeceraPlanilla planillaNoPagada, CabeceraPlanilla planillaNueva,
			RegistroEconomico multaAtrazoMes, Usuario usuario, Double valorMulta) throws Exception;

	/**
	 * Genera un detalle de una lectura de acuerdo al consumo
	 * 
	 * @param periodoPago
	 * @param lec
	 * @param dpls
	 * @return
	 * @throws Exception
	 */
	DetallePlanilla builDetailLectura(PeriodoPago periodoPago, Lectura lec, DetallePlanilla dpls) throws Exception;

	/**
	 * Crea un detalle inicial de una lectura
	 * 
	 * @param cp
	 * @param lec
	 * @return
	 * @throws Exception
	 */
	DetallePlanilla buildInitialDetailLectura(CabeceraPlanilla cp, Lectura lec) throws Exception;
	
	DetallePlanilla findByIdCustom(Integer idDetalle) throws Exception;
	/**
	 * 
	 * @param periodoPago
	 * @param lec
	 * @param dpls
	 * @return
	 * @throws Exception
	 */
	DetallePlanilla builDetailLecturaCero(PeriodoPago periodoPago, Lectura lec, DetallePlanilla dpls) throws Exception;
}
