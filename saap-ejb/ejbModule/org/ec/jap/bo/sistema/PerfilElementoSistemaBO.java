package org.ec.jap.bo.sistema;

import java.util.List;

import javax.ejb.Local;

import org.ec.jap.dao.sistema.PerfilElementoSistemaDAO;
import org.ec.jap.entiti.saap.Usuario;
import org.ec.jap.entiti.sistema.Perfil;
import org.ec.jap.entiti.sistema.PerfilElementoSistema;

@Local
public interface PerfilElementoSistemaBO extends PerfilElementoSistemaDAO {

	List<PerfilElementoSistema> generateTree(Perfil perfil) throws Exception;

	void saveOptions(Usuario usuario, List<PerfilElementoSistema> perfilElementoSistemas, Perfil perfil) throws Exception;

}
