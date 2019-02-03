package org.ec.jap.bo.saap;

import javax.ejb.Local;

import org.ec.jap.dao.saap.DetallePlanillaDAO;
import org.ec.jap.entiti.saap.CabeceraPlanilla;
import org.ec.jap.entiti.saap.DetallePlanilla;
import org.ec.jap.entiti.saap.RegistroEconomico;
import org.ec.jap.entiti.saap.Usuario;

@Local
public interface DetallePlanillaBO extends DetallePlanillaDAO {

	public void asignarMulta(Usuario usuario, CabeceraPlanilla planilla, RegistroEconomico registroEconomico) throws Exception;

	void quitarMulta(Usuario usuario, CabeceraPlanilla planilla, RegistroEconomico registroEconomico, DetallePlanilla detallePlanilla) throws Exception;

	Boolean noExisteDetalle(Integer idCabecera) throws Exception;

	/**
	 * Dscarta los detalles del pago
	 * 
	 * @param planilla
	 * @param usuario
	 * @throws Exception
	 */
	void descartarPago(CabeceraPlanilla planilla, Usuario usuario) throws Exception;
}
