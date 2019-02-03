package org.ec.jap.bo.saap;

import java.util.Date;

import javax.ejb.Local;

import org.ec.jap.dao.saap.ParametroDAO;

@Local
public interface ParametroBO extends ParametroDAO {

	public Integer getInteger(String tipoLlave, Integer idComunidad, String parametro) throws Exception;

	public Double getNumerico(String tipoLlave, Integer idComunidad, String parametro) throws Exception;

	public Date getFecha(String tipoLlave, Integer idComunidad, String parametro) throws Exception;

	public String getString(String tipoLlave, Integer idComunidad, String parametro) throws Exception;
}
