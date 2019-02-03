package org.ec.jap.bo.sistema.impl;

import java.util.HashMap;
import java.util.List;

import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.ec.jap.bo.sistema.CambioEstadoCondicionBO;
import org.ec.jap.dao.sistema.impl.CambioEstadoCondicionDAOImpl;
import org.ec.jap.entiti.sistema.CambioEstado;
import org.ec.jap.entiti.sistema.CambioEstadoCondicion;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class CambioEstadoCondicionBOImpl extends CambioEstadoCondicionDAOImpl implements CambioEstadoCondicionBO {

	/**
	 * Default constructor.
	 */
	public CambioEstadoCondicionBOImpl() {
	}

	@Override
	public void cumpleCondicion(CambioEstado cambioEstado,Object idDocument) throws Exception {
		Integer valorReturn = 0;
		HashMap<String, Object> p = new HashMap<>();
		p.put("idCambioEstado", cambioEstado);
		List<CambioEstadoCondicion> cambioEstadoCondicions = findAllByNamedQuery("CambioEstadoCondicion.findByCambioEstado", p);
		for (CambioEstadoCondicion cambioEstadoCondicion : cambioEstadoCondicions) {
			Query query=em().createQuery(cambioEstadoCondicion.getSentenciaValida());
			query.setParameter("id", idDocument);
			valorReturn=Integer.valueOf(query.getSingleResult().toString());
			if(valorReturn.equals(0))
				throw new EJBTransactionRolledbackException(cambioEstadoCondicion.getDescripcion());
		}
	}
}
