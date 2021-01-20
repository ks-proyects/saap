package org.ec.jap.bo.saap;

import java.util.List;

import javax.ejb.Local;

import org.ec.jap.dao.saap.DetallePlanillaDAO;
import org.ec.jap.entiti.saap.CabeceraPlanilla;
import org.ec.jap.entiti.saap.DetallePlanilla;
import org.ec.jap.entiti.saap.Llave;
import org.ec.jap.entiti.saap.RegistroEconomico;
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
			RegistroEconomico registroEconomicoAlcantarillado, Integer cantidad, Double valor, String ppDescripcion)
			throws Exception;

	DetallePlanilla traspasarDetalle(CabeceraPlanilla planillaNueva, DetallePlanilla detallePlanilla);

	DetallePlanilla traspasarDetalleInconompleto(CabeceraPlanilla planillaNueva, DetallePlanilla detalleIncompleto);

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
			RegistroEconomico multaAtrazoMes, Llave llave, Usuario usuario) throws Exception;
}
