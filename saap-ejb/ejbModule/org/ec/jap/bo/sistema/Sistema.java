/**
 * 
 */
package org.ec.jap.bo.sistema;

import java.io.Serializable;

import org.ec.jap.dao.sistema.DAO;

/**
 * @author Freddy G Castillo C
 * 
 */
public interface Sistema<Entiti, Pk extends Serializable> extends
		DAO<Entiti, Pk> {

}
