/**
 * 
 */
package org.ec.jap.dao.sistema.impl;

import org.ec.jap.dao.sistema.ElementoSistemaDAO;
import org.ec.jap.entiti.sistema.ElementoSistema;

/**
 * Clase de Acceso a Datos de {@link ElementoSistema}
 * 
 * @author Freddy G. Castillo C.
 * @version {@code 1.0}
 */
public class ElementoSistemaDAOImpl extends SistemaImple<ElementoSistema, Integer> implements ElementoSistemaDAO {

	public ElementoSistemaDAOImpl() {
		super(ElementoSistema.class);
	}

}
