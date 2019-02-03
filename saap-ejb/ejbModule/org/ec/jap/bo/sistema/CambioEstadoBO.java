package org.ec.jap.bo.sistema;

import javax.ejb.Local;

import org.ec.jap.dao.sistema.CambioEstadoDAO;
import org.ec.jap.entiti.saap.Usuario;
import org.ec.jap.entiti.sistema.CambioEstado;

@Local
public interface CambioEstadoBO extends CambioEstadoDAO {

	/**
	 * 
	 * @param idCambioEstado
	 * @param usuario
	 * @param idDocumento
	 * @throws Exception
	 */
	public void cambiarEstado(Integer idCambioEstado, Usuario usuario, Object idDocumento) throws Exception;

	/**
	 * 
	 * @param idCambioEstado
	 * @param usuario
	 * @param idDocumento
	 * @throws Exception
	 */
	public void cambiarEstado(Integer idCambioEstado, Usuario usuario, Object idDocumento, String motivo) throws Exception;

	/**
	 * 
	 * @param idCambioEstado
	 * @param usuario
	 * @param idDocumento
	 * @param motivo
	 * @throws Exception
	 */
	void cambiarEstadoSinVerificar(Integer idCambioEstado, Usuario usuario, Object idDocumento, String motivo) throws Exception;

	/**
	 * Método que permite verificar si el siguiente cambio de estado es
	 * permitido
	 * 
	 * @param cambioEstado
	 * @return
	 * @throws Exception
	 */
	Boolean verificarEstado(CambioEstado cambioEstado, Integer idDocument) throws Exception;

	/**
	 * 
	 * @param idCambioEstado
	 * @param idDocumento
	 * @throws Exception
	 */
	void eliminarEntidad(Integer idCambioEstado, Object idDocumento) throws Exception;

	/**
	 * 
	 * @param idCambioEstado
	 * @param usuario
	 * @param idDocumento
	 * @throws Exception
	 */
	void cambiarEstadoMandatory(Integer idCambioEstado, Usuario usuario, Object idDocumento) throws Exception;
}
