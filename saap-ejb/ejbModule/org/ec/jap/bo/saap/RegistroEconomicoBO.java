package org.ec.jap.bo.saap;

import java.util.List;

import javax.ejb.Local;

import org.ec.jap.dao.saap.RegistroEconomicoDAO;
import org.ec.jap.entiti.dto.UsuarioPagoDTO;
import org.ec.jap.entiti.saap.PeriodoPago;
import org.ec.jap.entiti.saap.RegistroEconomico;
import org.ec.jap.entiti.saap.Usuario;

@Local
public interface RegistroEconomicoBO extends RegistroEconomicoDAO {

	/**
	 * Método que aplica una cuota a un usuarios especifico
	 * 
	 * @param usuario
	 * @param idRegistroEconomico
	 * @throws Exception
	 */
	public void aplicarCuota(Usuario usuario, Integer idRegistroEconomico) throws Exception;

	/**
	 * Obtiene todos los usuarios de la JAAP q esten activos y que tengan una
	 * llave
	 * 
	 * @param registroEconomico
	 * @return
	 * @throws Exception
	 */
	public List<UsuarioPagoDTO> getUsuarioPagoDTOs(RegistroEconomico registroEconomico, String filtro, Usuario usuario) throws Exception;

	/**
	 * Método que inicializa un registro economico
	 * 
	 * @param periodoPago
	 * @param tipoRegistro
	 * @param descripcion
	 * @param cantidadAplicados
	 * @return
	 * @throws Exception
	 */
	public RegistroEconomico inicializar(PeriodoPago periodoPago, String tipoRegistro, String descripcion, Integer cantidadAplicados, Usuario usuario) throws Exception;

}
