/**
 * 
 */
package org.ec.jap.dao.saap.impl;

import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;

import org.ec.jap.dao.saap.LecturaDAO;
import org.ec.jap.dao.sistema.impl.SistemaImple;
import org.ec.jap.entiti.saap.Lectura;
import org.ec.jap.entiti.saap.Llave;

/**
 * Clase de Acceso a Datos de {@link Lectura}
 * 
 * @author Freddy
 * @version {@code 1.0}
 */
public class LecturaDAOImpl extends SistemaImple<Lectura, Integer> implements LecturaDAO {

	public LecturaDAOImpl() {
		super(Lectura.class);
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Lectura> getLast3(Llave idLlave, Lectura actual) throws Exception {
		HashMap<String, Object> p = new HashMap<String, Object>();
		p.put("idLlave", idLlave);
		p.put("idLectura", actual);
		Query query = getQuery("Lectura.findLasThree", p);
		query.setMaxResults(3);
		List<Lectura> list = query.getResultList();
		return list;
	}

}
