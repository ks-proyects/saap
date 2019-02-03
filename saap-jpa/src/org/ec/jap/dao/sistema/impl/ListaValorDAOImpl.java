/**
 * 
 */
package org.ec.jap.dao.sistema.impl;

import org.ec.jap.dao.sistema.ListaValorDAO;
import org.ec.jap.entiti.dto.ListaValor;

/**
 * Clase de Acceso a Datos de {@link ListaValor}
 * 
 * @author Freddy G. Castillo C.
 * @version {@code 1.0}
 */
public class ListaValorDAOImpl extends SistemaImple<ListaValor, Integer> implements ListaValorDAO {

	public ListaValorDAOImpl() {
		super(ListaValor.class);
	}

}
