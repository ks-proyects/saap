/**
 * 
 */
package org.ec.jap.dao.sistema.impl;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJBTransactionRolledbackException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.ec.jap.anotaciones.AuditoriaAnot;
import org.ec.jap.anotaciones.AuditoriaMethod;
import org.ec.jap.dao.sistema.DAO;
import org.ec.jap.entiti.saap.Usuario;
import org.ec.jap.entiti.sistema.Auditoria;
import org.ec.jap.entiti.sistema.TipoEntidad;

/**
 * Clase de Acceso a Datos de {@link Eniti}
 * 
 * @author Freddy G. Castillo C.
 * @version {@code 1.0}
 */
@SuppressWarnings("unchecked")
public abstract class DAOImpl<Entiti, Pk extends Serializable> implements DAO<Entiti, Pk> {

	protected HashMap<String, Object> map = new HashMap<String, Object>();
	private Class<Entiti> entiti = null;
	private String nameEntiti = "";

	public Logger logger = Logger.getLogger(nameEntiti);

	protected abstract EntityManager em();

	public DAOImpl(Class<Entiti> entiti) {
		this.entiti = entiti;
		this.nameEntiti = this.entiti.getSimpleName();
	}

	@Override
	public final void save(Usuario user, Entiti entiti) throws Exception {
		em().persist(entiti);
		generateTrace(user, entiti, "INS");
	};

	public final void delete(Usuario user, Entiti entiti) throws Exception {
		logger.info("Se ha eliminado el entity " + nameEntiti + " por el usuario " + user.getNombres() + " " + user.getApellidos());
		entiti = em().merge(entiti);
		generateTrace(user, entiti, "DEL");
		em().remove(entiti);
		em().flush();
	};

	public void update(Usuario user, Entiti entiti) throws Exception {
		em().merge(entiti);
		generateTrace(user, entiti, "UPD");
	};

	@Override
	public final List<Entiti> findAll() throws Exception {
		return getQuery(null, null).getResultList();
	}

	@Override
	public final List<Entiti> findAllByNamedQuery(String namedQuery) throws Exception {
		return getQuery(namedQuery, null).getResultList();
	}

	@Override
	public final List<Entiti> findAllByNamedQuery(String namedQuery, HashMap<String, Object> p) throws Exception {
		return getQuery(namedQuery, p).getResultList();
	}

	@Override
	public final List<Entiti> findAllByNamedQuery(Integer maxResult, String namedQuery, HashMap<String, Object> p) throws Exception {
		return getQuery(namedQuery, p).setMaxResults(maxResult).getResultList();
	}

	@Override
	public final List<Entiti> findAllByNamedQueryObject(String namedQuery, Object... p) throws Exception {
		return getQueryParam(namedQuery, p).getResultList();
	}

	@Override
	public final Entiti findByPk(Pk id) throws Exception {
		return em().find(entiti, id);
	};

	@Override
	public final Entiti findByNamedQuery(String namedQuery) throws Exception {
		List<?> list = getQuery(namedQuery, null).getResultList();
		if (list.size() > 0)
			return (Entiti) list.get(0);
		return null;
	};

	@Override
	public final Entiti findByNamedQuery(String namedQuery, HashMap<String, Object> p) throws Exception {
		List<?> list = getQuery(namedQuery, p).getResultList();
		if (list.size() > 0)
			return (Entiti) list.get(0);
		return null;
	};

	@Override
	public final Object findObjectByNamedQuery(String namedQuery) throws Exception {
		List<?> list = getQuery(namedQuery, null).getResultList();
		if (list.size() > 0)
			return list.get(0);
		return null;
	};

	@Override
	public final List<Object[]> findAllObjectByNamedQuery(String namedQuery, Object... p) throws Exception {
		List<Object[]> list = new ArrayList<Object[]>();
		Iterator<?> iterator = getQueryParam(namedQuery, p).getResultList().iterator();
		while (iterator.hasNext()) {
			list.add((Object[]) iterator.next());
		}
		return list;
	}

	@Override
	public final <E> List<E> findAllEntentiByNamedQuery(String namedQuery, Object... p) throws Exception {

		return getQueryParam(namedQuery, p).getResultList();
	}

	@Override
	public final Object findObjectByNamedQuery(String namedQuery, HashMap<String, Object> p) throws Exception {
		List<?> list = getQuery(namedQuery, p).getResultList();
		if (list.size() > 0)
			return list.get(0);
		return null;
	};

	@Override
	public final Object findDoubleByNamedQuery(String namedQuery, HashMap<String, Object> p) throws Exception {
		Object object = findObjectByNamedQuery(namedQuery, p);
		if (object == null)
			return 0.0;
		else
			return Double.valueOf(object.toString());
	};

	@Override
	public final List<Object[]> findObjects(String nq) throws Exception {
		List<Object[]> list = new ArrayList<Object[]>();
		Iterator<?> iterator = getQuery(nq, null).getResultList().iterator();
		while (iterator.hasNext()) {
			list.add((Object[]) iterator.next());
		}
		return list;
	};

	@Override
	public final List<Object[]> findObjects(String nq, HashMap<String, Object> p) throws Exception {

		List<Object[]> list = new ArrayList<Object[]>();
		Iterator<?> iterator = getQuery(nq, p).getResultList().iterator();
		while (iterator.hasNext()) {
			list.add((Object[]) iterator.next());
		}
		return list;
	};

	@Override
	public Integer findIntegerByNamedQuery(String namedQuery, HashMap<String, Object> map) throws Exception {
		Object object = getQuery(namedQuery, map).getSingleResult();
		if (object instanceof Integer)
			return (Integer) object;
		if (object instanceof Long)
			return ((Long) object).intValue();
		return null;
	}

	@Override
	public void executeQuerry(String namedQuery, HashMap<String, Object> p) throws Exception {
		getQuery(namedQuery, p).executeUpdate();
	}

	@Override
	public String findStringByNamedQuery(String namedQuery, HashMap<String, Object> map) throws Exception {
		Object object = getQuery(namedQuery, map).getSingleResult();
		return object != null ? (String) object : "";
	}

	protected final Query getQuery(String namedQuery, HashMap<String, Object> p) throws Exception {
		Query query = null;
		if (namedQuery == null)
			query = em().createQuery("SELECT o FROM " + nameEntiti + " o");
		else {
			query = em().createNamedQuery(namedQuery);
			if (p != null)
				for (String key : p.keySet()) {
					query.setParameter(key, p.get(key));
				}
		}
		return query;
	}

	protected final Query getQueryParam(String namedQuery, Object... p) throws Exception {
		Query query = null;
		if (namedQuery == null)
			query = em().createQuery("SELECT o FROM " + nameEntiti + " o");
		else {
			query = em().createNamedQuery(namedQuery);
			if (p != null)
				for (int i = 0; i < p.length; i++) {
					query.setParameter(i + 1, p[i]);
				}
		}
		return query;
	}

	private final void generateTrace(Usuario usuarioCurrent, Entiti entiti, String traceType) throws PersistenceException {
		TipoEntidad tipoEntidad;
		Object object = null;
		String value = "";
		String field = "";
		boolean isIdEntity = false;
		boolean isIdEntity1 = false;
		boolean isIdEntity2 = false;
		boolean isEntityDescription = false;
		boolean isEntityDescription1 = false;
		boolean isEntityDescription2 = false;
		boolean metodoAuditable = false;
		boolean isDisabled = false;

		Annotation anotation = entiti.getClass().getAnnotation(AuditoriaAnot.class);

		if (anotation instanceof AuditoriaAnot) {
			AuditoriaAnot anotacionAuditoria = (AuditoriaAnot) anotation;
			String entityType = anotacionAuditoria.entityType();
			StringBuilder camposModificados = new StringBuilder();

			try {
				tipoEntidad = em().find(TipoEntidad.class, entityType);

				if ("S".equals(tipoEntidad.getEsActivo())) {
					Auditoria auditoria = new Auditoria();
					auditoria.setTipoEntidad(tipoEntidad);
					Boolean isValidToAudit = true;
					for (Method method : entiti.getClass().getMethods()) {
						if (method.getName().startsWith("get")) {
							anotation = method.getAnnotation(AuditoriaMethod.class);
							if (method.getReturnType().toString().contains("Boolean") && anotation instanceof AuditoriaMethod) {
								AuditoriaMethod sclTraceMethod = (AuditoriaMethod) anotation;
								if (sclTraceMethod.methodToAudit()) {
									object = method.invoke(entiti);
									isValidToAudit = (Boolean)object;
									break;
								}
							}
						}
					}
					if (isValidToAudit) {
						for (Method method : entiti.getClass().getMethods()) {
							value = "";
							field = "";
							isIdEntity = false;
							isIdEntity1 = false;
							isIdEntity2 = false;
							isEntityDescription = false;
							isEntityDescription1 = false;
							isEntityDescription2 = false;
							metodoAuditable = false;
							isDisabled = false;

							if (method.getName().startsWith("get")) {
								if (method.getReturnType().toString().endsWith("java.lang.String"))
									metodoAuditable = true;
								if (method.getReturnType().toString().endsWith("java.sql.Timestamp"))
									metodoAuditable = true;
								if (method.getReturnType().toString().endsWith("java.lang.Integer"))
									metodoAuditable = true;
								if (method.getReturnType().toString().endsWith("java.lang.Double"))
									metodoAuditable = true;
								if (method.getReturnType().toString().endsWith("java.lang.Boolean"))
									metodoAuditable = true;
								if (method.getReturnType().toString().endsWith("java.util.Date"))
									metodoAuditable = true;
								if (method.getReturnType().toString().equals("int"))
									metodoAuditable = true;
								if (method.getReturnType().toString().equals("boolean"))
									metodoAuditable = true;
								if (method.getReturnType().toString().endsWith("java.lang.BigDecimal"))
									metodoAuditable = true;

								// Verificamos las propiedades del método a
								// auditar
								if (metodoAuditable) {

									anotation = method.getAnnotation(AuditoriaMethod.class);
									if (anotation instanceof AuditoriaMethod) {
										AuditoriaMethod sclTraceMethod = (AuditoriaMethod) anotation;
										field = sclTraceMethod.name();
										isDisabled = sclTraceMethod.disabled();
										isIdEntity = sclTraceMethod.isIdEntity();
										isIdEntity1 = sclTraceMethod.isIdEntity1();
										isIdEntity2 = sclTraceMethod.isIdEntity2();
										isEntityDescription = sclTraceMethod.isEntityDescription();
										isEntityDescription1 = sclTraceMethod.isEntityDescription1();
										isEntityDescription2 = sclTraceMethod.isEntityDescription2();
									} else
										field = "default";
								}

							}

							if (metodoAuditable) {
								object = method.invoke(entiti);
								if (object != null) {
									if (method.getReturnType().toString().endsWith("java.util.Timestamp")) {
										Timestamp dateTime = (Timestamp) object;
										SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
										value = dateFormat.format(dateTime.getTime());
									} else if (method.getReturnType().toString().endsWith("java.util.Date")) {
										Date date = (Date) object;
										SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
										value = dateFormat.format(date);
									} else if (method.getReturnType().toString().endsWith("boolean") || method.getReturnType().toString().endsWith("java.lang.Boolean")) {
										value = (object.toString().toLowerCase().equals("true") ? "Verdadero" : "Falso");
									} else
										value = object.toString();

									if (isIdEntity)
										auditoria.setIdEntidad(Integer.valueOf(value));
									if (isIdEntity1)
										auditoria.setIdEntidad1(Integer.valueOf(value));
									if (isIdEntity2)
										auditoria.setIdEntidad2(Integer.valueOf(value));
									if (isEntityDescription)
										auditoria.setDescripcion(value);
									if (isEntityDescription1)
										auditoria.setDescripcion1(value);
									if (isEntityDescription2)
										auditoria.setDescripcion2(value);

								} else
									value = "Nulo";

								if (!isDisabled) {
									camposModificados.append(field.equals("default") ? method.getName().replace("get", "") : field);
									camposModificados.append(" = ");
									camposModificados.append(value);
									camposModificados.append("; ");
								}
							}
						}

						auditoria.setDescripcion("INS".equals(traceType) ? "Registro" : "UPD".equals(traceType) ? "Actualizacion" : "DEL".equals(traceType) ? "Eliminacion" : "");
						auditoria.setCambiosAtributos(camposModificados.toString());
						auditoria.setUsuario(usuarioCurrent);
						auditoria.setFecha(GregorianCalendar.getInstance().getTime());
						em().persist(auditoria);

					}
				}
			} catch (Exception e) {
				throw new EJBTransactionRolledbackException("Error en el registro de auditoría " + e.getMessage());
			}
		}
	}

	/**
	 * @return the map
	 */
	public HashMap<String, Object> getMap() {
		return map;
	}

	/**
	 * @param map
	 *            the map to set
	 */
	public void setMap(HashMap<String, Object> map) {
		this.map = map;
	}

}
