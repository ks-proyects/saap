package org.ec.jap.bo.sistema;

import java.util.List;

import javax.ejb.Local;

import org.ec.jap.dao.sistema.RolAccionNegocioDAO;
import org.ec.jap.entiti.saap.Usuario;
import org.ec.jap.entiti.sistema.Rol;
import org.ec.jap.entiti.sistema.RolAccionNegocio;

@Local
public interface RolAccionNegocioBO extends RolAccionNegocioDAO {

	List<RolAccionNegocio> generateTree(Rol rol) throws Exception;

	void saveAccions(Usuario usuarioCurrent, List<RolAccionNegocio> rolAccionNegociosSeleccionado, Rol rol)throws Exception;

}
