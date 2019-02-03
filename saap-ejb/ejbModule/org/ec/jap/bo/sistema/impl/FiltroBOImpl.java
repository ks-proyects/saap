package org.ec.jap.bo.sistema.impl;

import java.util.Date;
import java.util.HashMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.ec.jap.bo.saap.TipoFiltroBO;
import org.ec.jap.bo.sistema.FiltroBO;
import org.ec.jap.dao.sistema.impl.FiltroDAOImpl;
import org.ec.jap.entiti.saap.Usuario;
import org.ec.jap.entiti.sistema.ElementoSistema;
import org.ec.jap.entiti.sistema.Filtro;
import org.ec.jap.entiti.sistema.TipoFiltro;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class FiltroBOImpl extends FiltroDAOImpl implements FiltroBO {

	
	@EJB
	TipoFiltroBO tipoFiltroBO;

	/**
	 * Default constructor.
	 */
	public FiltroBOImpl() {
	}

	@Override
	public Filtro getFiltro(Usuario idUser, String tipoFiltro, ElementoSistema elementoSistema, Object value) throws Exception {
		TipoFiltro tipoFiltro2 = tipoFiltroBO.findByPk(tipoFiltro);
		if (tipoFiltro2 == null) {
			tipoFiltro2 = new TipoFiltro();
			tipoFiltro2.setCodigo(tipoFiltro);
			tipoFiltro2.setDescripcion("Default");
			tipoFiltroBO.save(idUser, tipoFiltro2);
		}
		HashMap<String, Object> p = new HashMap<>();
		p.put("tp", tipoFiltro2);
		p.put("u", idUser);
		p.put("e", elementoSistema);
		Filtro filtro = findByNamedQuery("Filtro.findByTipoAndUser", p);
		if (filtro == null) {
			filtro = new Filtro();
			filtro.setCodigo(tipoFiltro2);
			filtro.setIdComunidad(idUser.getIdComunidad());
			filtro.setIdElementoSistema(elementoSistema);
			filtro.setIdUsuario(idUser);
			if (value instanceof Integer)
				filtro.setValorEntero((Integer) value);
			if (value instanceof String)
				filtro.setValorCadena(value.toString());
			if (value instanceof Double)
				filtro.setValorNumerico((Double) value);
			if (value instanceof Date)
				filtro.setValorFecha((Date) value);
			save(idUser, filtro);
		} else {

			if (value instanceof Integer) {
				filtro.setValorEntero((Integer) value);
			}
			if (value instanceof String) {
				filtro.setValorCadena(value.toString());
			}
			if (value instanceof Double) {
				filtro.setValorNumerico((Double) value);
			}
			if (value instanceof Date) {
				filtro.setValorFecha((Date) value);
			}
			if (value instanceof Boolean) {
				filtro.setValorBoolean((Boolean) value);
			}
			update(idUser, filtro);
		}

		return filtro;
	}

	@Override
	public Filtro getFiltro(Usuario idUser, String tipoFiltro, ElementoSistema elementoSistema, Object valueDefect, Filtro filtro, Boolean isParame) throws Exception {
		if (filtro != null) {
			update(idUser, filtro);
			return filtro;
		} else {

			TipoFiltro tipoFiltro2 = tipoFiltroBO.findByPk(tipoFiltro);
			if (tipoFiltro2 == null) {
				tipoFiltro2 = new TipoFiltro();
				tipoFiltro2.setCodigo(tipoFiltro);
				tipoFiltro2.setDescripcion("Default");
				tipoFiltroBO.save(idUser, tipoFiltro2);
			}
			HashMap<String, Object> p = new HashMap<>();
			p.put("tp", tipoFiltro2);
			p.put("u", idUser);
			p.put("e", elementoSistema);
			Filtro filtroFirst = findByNamedQuery("Filtro.findByTipoAndUser", p);
			if (filtroFirst == null) {
				filtroFirst = new Filtro();
				filtroFirst.setCodigo(tipoFiltro2);
				filtroFirst.setIdComunidad(idUser.getIdComunidad());
				filtroFirst.setIdElementoSistema(elementoSistema);
				filtroFirst.setIdUsuario(idUser);
				if (valueDefect instanceof Integer)
					filtroFirst.setValorEntero((Integer) valueDefect);
				if (valueDefect instanceof String)
					filtroFirst.setValorCadena(valueDefect.toString());
				if (valueDefect instanceof Double)
					filtroFirst.setValorNumerico((Double) valueDefect);
				if (valueDefect instanceof Date)
					filtroFirst.setValorFecha((Date) valueDefect);
				save(idUser, filtroFirst);
				return filtroFirst;
			} else {
				if (isParame) {
					if (valueDefect instanceof Integer)
						filtroFirst.setValorEntero((Integer) valueDefect);
					if (valueDefect instanceof String)
						filtroFirst.setValorCadena(valueDefect.toString());
					if (valueDefect instanceof Double)
						filtroFirst.setValorNumerico((Double) valueDefect);
					if (valueDefect instanceof Date)
						filtroFirst.setValorFecha((Date) valueDefect);
					update(idUser, filtroFirst);
				}
				return filtroFirst;
			}
		}
	}
}
