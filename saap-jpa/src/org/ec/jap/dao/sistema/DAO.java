package org.ec.jap.dao.sistema;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.persistence.NamedQuery;

import org.ec.jap.entiti.saap.Usuario;

/**
 * Interfaz de Acceso a Datos de {@link Entiti}
 * 
 * @author Freddy
 * @version {@code 1.0}
 */
public interface DAO<Entiti, Pk extends Serializable> {

	// DML
	/**
	 * Guarda un {@link Entiti}
	 * 
	 * @param entiti
	 *            {@link Entiti}
	 * @throws Exception
	 */
	void save(Usuario user, Entiti entiti) throws Exception;

	/**
	 * Actualiza un {@link Entiti}
	 * 
	 * @param entiti
	 * @throws Exception
	 */
	void update(Usuario user, Entiti entiti) throws Exception;

	/**
	 * Elimina un {@link Entiti}
	 * 
	 * @param entiti
	 * @throws Exception
	 */
	void delete(Usuario user, Entiti entiti) throws Exception;

	// CONSULTAS
	/**
	 * Encuentra un {@link Entiti} mediante el primary key
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	Entiti findByPk(Pk id) throws Exception;

	/**
	 * Encuentra un {@link Entiti} mediante un {@link NamedQuery}
	 * 
	 * @param namedQuery
	 * @return
	 * @throws Exception
	 */
	Entiti findByNamedQuery(String namedQuery) throws Exception;

	/**
	 * Encuentra un {@link Entiti} mediante un {@link NamedQuery} y un
	 * {@link HashMap} de parametros
	 * 
	 * @param namedQuery
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Entiti findByNamedQuery(String namedQuery, HashMap<String, Object> map) throws Exception;

	/**
	 * Encuentra todos los {@link Entiti}s
	 * 
	 * @return
	 * @throws Exception
	 */
	List<Entiti> findAll() throws Exception;

	/**
	 * Encuentra todos los {@link Entiti}s mediante un {@link NamedQuery} y un
	 * {@link HashMap} de parametros
	 * 
	 * @param namedQuery
	 * @return
	 * @throws Exception
	 */
	List<Entiti> findAllByNamedQuery(String namedQuery) throws Exception;

	/**
	 * Encuentra todos los {@link Entiti}s mediante un {@link NamedQuery} y un
	 * {@link HashMap} de parametros
	 * 
	 * @param namedQuery
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Entiti> findAllByNamedQuery(String namedQuery, HashMap<String, Object> map) throws Exception;

	/**
	 * Encuentra todos los {@link Object} mediante un {@link NamedQuery}
	 * 
	 * @param namedQuery
	 * @return
	 * @throws Exception
	 */
	Object findObjectByNamedQuery(String namedQuery) throws Exception;

	/**
	 * Encuentra todos los {@link Object} mediante un {@link NamedQuery} y un
	 * {@link HashMap} de parametros
	 * 
	 * @param namedQuery
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Object findObjectByNamedQuery(String namedQuery, HashMap<String, Object> map) throws Exception;

	/**
	 * Encuentra una lista de {@link Object} mediante un {@link NamedQuery}
	 * 
	 * @param nq
	 * @return
	 * @throws Exception
	 */
	List<Object[]> findObjects(String nq) throws Exception;

	/**
	 * Encuentra una lista de {@link Object} mediante un {@link NamedQuery} y un
	 * {@link HashMap} de parametros
	 * 
	 * @param nq
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Object[]> findObjects(String nq, HashMap<String, Object> map) throws Exception;

	/**
	 * Encuentra todos los {@link Integer} mediante un {@link NamedQuery} y un
	 * {@link HashMap} de parametros
	 * 
	 * @param namedQuery
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Integer findIntegerByNamedQuery(String namedQuery, HashMap<String, Object> map) throws Exception;

	/**
	 * Encuentra todos los {@link String} mediante un {@link NamedQuery} y un
	 * {@link HashMap} de parametros
	 * 
	 * @param namedQuery
	 * @param map
	 * @return
	 * @throws Exception
	 */
	String findStringByNamedQuery(String namedQuery, HashMap<String, Object> map) throws Exception;

	/**
	 * 
	 * @param querry
	 * @param map
	 * @throws Exception
	 */
	void executeQuerry(String namedQuery, HashMap<String, Object> map) throws Exception;

	Object findDoubleByNamedQuery(String namedQuery, HashMap<String, Object> p) throws Exception;

	/**
	 * 
	 * @param namedQuery
	 * @param p
	 * @return
	 * @throws Exception
	 */
	List<Entiti> findAllByNamedQueryObject(String namedQuery, Object... p) throws Exception;

	/**
	 * 
	 * @param namedQuery
	 * @param p
	 * @return
	 * @throws Exception
	 */
	List<Object[]> findAllObjectByNamedQuery(String namedQuery, Object... p) throws Exception;

	<E> List<E> findAllEntentiByNamedQuery(String namedQuery, Object... p) throws Exception;

}
