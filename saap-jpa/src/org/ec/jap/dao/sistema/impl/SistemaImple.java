package org.ec.jap.dao.sistema.impl;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.ec.jap.dao.sistema.Sistema;

/**
 * Clase de Acceso a Datos de {@link Entiti}
 * 
 * @author Freddy G. Castillo C.
 * @version {@code 1.0}
 */
public class SistemaImple<Entiti, Pk extends Serializable> extends DAOImpl<Entiti, Pk> implements Sistema<Entiti, Pk> {

	@PersistenceContext
	EntityManager manager;

	public SistemaImple(Class<Entiti> entiti) {
		super(entiti);
	}

	@Override
	protected EntityManager em() {
		return manager;
	}

	

}
