package org.ec.jap.bo.sistema.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;

import org.ec.jap.bo.sistema.ElementoSistemaBO;
import org.ec.jap.dao.sistema.impl.ElementoSistemaDAOImpl;
import org.ec.jap.entiti.sistema.ElementoSistema;
import org.ec.jap.enumerations.PerfilEstado;
import org.ec.jap.enumerations.ValorSiNo;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class ElementoSistemaBOImpl extends ElementoSistemaDAOImpl implements ElementoSistemaBO {

	/**
	 * Default constructor.
	 */
	public ElementoSistemaBOImpl() {
	}

	@Override
	public List<ElementoSistema> findAllByUser(Integer idEmpresa, Integer idUser, String tipoElemento, Integer idElementoPadre) throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, Object> p = new HashMap<>();

		
		p.put("activoUP", ValorSiNo.S);// El perfil asignado debe estar activo
		p.put("estadoU", "ACT");// El estado del usuario debe ser activo
		p.put("activoPerf", PerfilEstado.S);// El perfil de estar activo
		p.put("acctivoPES", "S");// El elemento asignado al perfil debes estar
									// activo
		p.put("visibleES", "S");// El elemento debe estar visible

		p.put("idUsuario", idUser);
		p.put("idElementoSistemaPadre", idElementoPadre);
		p.put("tipoElemento", tipoElemento);
		List<ElementoSistema> list = findAllByNamedQuery("ElementoSistema.findByUser", p);
		Collections.sort(list);
		return list;
	}

	@Override
	public List<ElementoSistema> findControlsByUserAndAccion(Integer idEmpresa, Integer idUser, String tipoElemento, Integer idElementoPadre, String accion) throws Exception {
		HashMap<String, Object> p = new HashMap<>();

		p.put("activoUP", ValorSiNo.S);// El perfil asignado debe estar activo
		p.put("estadoU", "ACT");// El estado del usuario debe ser activo
		p.put("activoPerf", PerfilEstado.S);// El perfil de estar activo
		p.put("acctivoPES", "S");// El elemento asignado al perfil debes estar
									// activo
		p.put("visibleES", "S");// El elemento debe estar visible

		p.put("idUsuario", idUser);
		p.put("idElementoSistemaPadre", idElementoPadre);
		p.put("tipoElemento", tipoElemento);
		p.put("outcome", accion);
		List<ElementoSistema> list = findAllByNamedQuery("ElementoSistema.findByUserAndAction", p);
		Collections.sort(list);
		return list;
	}

}
