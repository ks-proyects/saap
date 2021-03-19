package org.ec.jap.backend.pagina;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.persistence.NamedQuery;

import org.ec.jap.entiti.dto.ListaValor;
import org.ec.jap.entiti.saap.Usuario;
import org.ec.jap.enumerations.TipoListaValor;
import org.ec.jap.utilitario.Identificador;

public abstract class Bean extends Actions {

	/**
	 * Metodo para iniciar un backend
	 * 
	 * @return
	 */
	public Bean() {
		super();
	}

	@Override
	public void init() throws Exception {
		checkPage();
		generateActions();
	}

	/**
	 * Método que genera un {@link List} de {@link SelectItem} en base a un
	 * conjunto de parámetros de un {@link NamedQuery}
	 * 
	 * @param idUser
	 * @param map
	 * @param namedQuerry
	 * @return
	 * @throws Exception
	 */
	public <ID> List<SelectItem> getSelectItems(Usuario idUser, HashMap<String, Object> map, String namedQuerry, Boolean mostrarNulo, Boolean mostrarTodos, Boolean mostrarSeleccione, TipoListaValor tipo, Identificador<ID>[] identificadores) throws Exception {
		// TODO Auto-generated method stub

		List<SelectItem> selectItems = new ArrayList<SelectItem>(0);
		if (mostrarNulo) {
			selectItems.add(new SelectItem(-1, "", ""));
		}
		if (mostrarTodos) {
			selectItems.add(new SelectItem(0, "Todos", "Todos"));
		}
		if (mostrarSeleccione) {
			selectItems.add(new SelectItem(-1, "SELECCIONE", "SELECCIONE"));
		}

		if (TipoListaValor.NQ.equals(tipo)) {
			List<ListaValor> list = listaValoreBO.findAllByNamedQuery(namedQuerry, map);

			for (ListaValor listaValor : list) {
				if (listaValor.getValorInteger().equals(-1)) {
					selectItems.add(new SelectItem(listaValor.getValorString(), listaValor.getLabel(), listaValor.getTooltip()));
				} else {
					selectItems.add(new SelectItem(listaValor.getValorInteger(), listaValor.getLabel(), listaValor.getTooltip()));
				}
			}
		} else if (TipoListaValor.F.equals(tipo)) {
			if ("PERFILESTADO".equals(namedQuerry)) {
				selectItems.add(new SelectItem(0, "S", "SI"));
				selectItems.add(new SelectItem(0, "N", "NO"));
			}
			if ("MESES".equals(namedQuerry)) {
				selectItems.add(new SelectItem(0, "Enero", "Febrero"));
				selectItems.add(new SelectItem(1, "Febrero", "Febrero"));
				selectItems.add(new SelectItem(2, "Marzo", "Marzo"));
				selectItems.add(new SelectItem(3, "Abril", "Abril"));
				selectItems.add(new SelectItem(4, "Mayo", "Mayo"));
				selectItems.add(new SelectItem(5, "Junio", "Junio"));
				selectItems.add(new SelectItem(6, "Julio", "Julio"));
				selectItems.add(new SelectItem(7, "Agosto", "Agosto"));
				selectItems.add(new SelectItem(8, "Septiembre", "Septiembre"));
				selectItems.add(new SelectItem(9, "Octubre", "Octubre"));
				selectItems.add(new SelectItem(10, "Noviembre", "Noviembre"));
				selectItems.add(new SelectItem(11, "Diciembre", "Diciembre"));

			}
		} else if (TipoListaValor.ENUM.equals(tipo)) {
			if (identificadores == null) {
				return selectItems;
			}
			for (Identificador<ID> i : identificadores) {
				selectItems.add(new SelectItem(i.getIdentificador(), i.getDescripcion()));
			}
		}
		return selectItems;
	}

	/**
	 * Método que genera un {@link List} de {@link SelectItem} en base a un
	 * conjunto de parámetros de un {@link NamedQuery}
	 * 
	 * @param idUser
	 * @param map
	 * @param namedQuerry
	 * @return
	 * @throws Exception
	 */
	public List<SelectItem> getSelectItems(Usuario idUser, HashMap<String, Object> map, Boolean mostrarSeleccione, String name, String tipo) throws Exception {
		// TODO Auto-generated method stub
		return getSelectItems(idUser, map, name, false, false, false, TipoListaValor.NQ, null);
	}

	/**
	 * Método que genera un {@link List} de {@link SelectItem} en base a un
	 * conjunto de parámetros de un {@link NamedQuery}
	 * 
	 * @param idTipo
	 * @return
	 * @throws Exception
	 */
	public <ID> List<SelectItem> getSelectItems(Identificador<ID>[] identificadores) throws Exception {
		return getSelectItems(null, null, null, false, false, false, TipoListaValor.ENUM, identificadores);
	}
	public <ID> List<SelectItem> getSelectItems(Identificador<ID>[] identificadores,Boolean seleccione) throws Exception {
		return getSelectItems(null, null, null, false, false, seleccione, TipoListaValor.ENUM, identificadores);
	}

	/**
	 * Método que genera un {@link List} de {@link SelectItem} en base a un
	 * conjunto de parámetros de un {@link NamedQuery}
	 * 
	 * @param idUser
	 * @param map
	 * @param namedQuerry
	 * @return
	 * @throws Exception
	 */
	public List<SelectItem> getSelectItems(Usuario idUser, HashMap<String, Object> map, String namedQuerry) throws Exception {
		// TODO Auto-generated method stub
		return getSelectItems(idUser, map, namedQuerry, false, false, false, TipoListaValor.NQ, null);
	}
	/**
	 * Método que genera un {@link List} de {@link SelectItem} en base a un
	 * conjunto de parámetros de un {@link NamedQuery}
	 * 
	 * @param idUser
	 * @param map
	 * @param namedQuerry
	 * @param mostrarTodos 
	 * @return
	 * @throws Exception
	 */
	public List<SelectItem> getSelectItemsTodos(Usuario idUser, HashMap<String, Object> map, String namedQuerry, Boolean mostrarTodos) throws Exception {
		// TODO Auto-generated method stub
		return getSelectItems(idUser, map, namedQuerry, false, mostrarTodos, false, TipoListaValor.NQ, null);
	}

	/**
	 * Método que genera un {@link List} de {@link SelectItem} en base a un
	 * conjunto de parámetros de un {@link NamedQuery}
	 * 
	 * @param idUser
	 * @param map
	 * @param namedQuerry
	 * @return
	 * @throws Exception
	 */
	public List<SelectItem> getSelectItems(Usuario idUser, HashMap<String, Object> map, Boolean mostrarTodos, String namedQuerry) throws Exception {
		// TODO Auto-generated method stub
		return getSelectItems(idUser, map, namedQuerry, false, mostrarTodos, false, TipoListaValor.NQ, null);
	}

	/**
	 * Método que genera un {@link List} de {@link SelectItem} en base a un
	 * conjunto de parámetros de un {@link NamedQuery}
	 * 
	 * @param idUser
	 * @param map
	 * @param namedQuerry
	 * @return
	 * @throws Exception
	 */
	public List<SelectItem> getSelectItems(Usuario idUser, HashMap<String, Object> map, String namedQuerry, Boolean mostrarNulo) throws Exception {
		// TODO Auto-generated method stub
		return getSelectItems(idUser, map, namedQuerry, mostrarNulo, false, false, TipoListaValor.NQ, null);
	}

}
