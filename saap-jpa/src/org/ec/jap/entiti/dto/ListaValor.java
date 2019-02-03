/**
 * 
 */
package org.ec.jap.entiti.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Objeto de Transferendia de datos para las listas
 * 
 * @author Freddy G. Castillo C.
 * @version {@code 1.0}
 * 
 */
@Entity
@NamedQueries({
		@NamedQuery(name = "ListaValor.findTipoRegistro", query = " SELECT new ListaValor(-1,p.tipoRegistro,UPPER(p.descripcion),'') FROM TipoRegistro p ORDER BY p.descripcion"),
		@NamedQuery(name = "ListaValor.findTipoRegistroByTipo", query = " SELECT new ListaValor(-1,tr.tipoRegistro,tr.descripcion,tr.descripcion) from TipoRegistro tr where tr.accion=:accion ORDER BY tr.descripcion ASC"),
		@NamedQuery(name = "ListaValor.findActividadByTipoAndPeriodo", query = " SELECT new ListaValor(act.actividad,'',act.descripcion,act.descripcion) FROM Actividad act WHERE act.idPeriodoPago.idPeriodoPago=:idPeriodoPago AND ( act.tipoActividad.tipoActividad=:tipoActividad OR :tipoActividad=0) order by act.descripcion"),
		@NamedQuery(name = "ListaValor.findTipoActividad", query = " SELECT new ListaValor(t.tipoActividad,'',t.descripcion,t.descripcion) FROM TipoActividad t ORDER BY t.descripcion "),
		@NamedQuery(name = "ListaValor.findPeriodoByAnio", query = " SELECT new ListaValor(p.idPeriodoPago,'',CONCAT(p.descripcion,'(',p.estado,')'),p.descripcion) FROM PeriodoPago p WHERE p.estado not in ('ABIE','ING') and (p.anio=:anio) order by p.descripcion"),
		@NamedQuery(name = "ListaValor.findAnioPeriodo", query = " SELECT new ListaValor(p.anio,'',str(p.anio),'') FROM PeriodoPago p WHERE p.estado in ('ABIE','CERR','FIN','FINI') GROUP BY p.anio"),
		@NamedQuery(name = "ListaValor.findTarifaConsu", query = " SELECT new ListaValor(p.idTarifa,'',p.descripcion,'') FROM Tarifa p "),
		
		@NamedQuery(name = "ListaValor.findPeriododAvalibleGasto", query = " SELECT new ListaValor(p.idPeriodoPago,'',CONCAT(p.descripcion,'(',p.estado,')'),p.descripcion) FROM PeriodoPago p WHERE p.estado in ('ABIE','CERR')"),
		@NamedQuery(name = "ListaValor.findByCurrentAndEstado", query = " SELECT new ListaValor(t.tipoActividad,'',t.descripcion,t.descripcion) FROM TipoActividad t WHERE t.activo = :activo OR t.tipoActividad=:tipoActividad"),
		@NamedQuery(name = "ListaValor.findDestinoByCurrentAndEstado", query = " SELECT new ListaValor(t.idDestino,'',t.descripcion,t.descripcion) FROM Destino t WHERE t.activo = :activo OR t.idDestino=:idDestino "),
		@NamedQuery(name = "ListaValor.findDestinoByEstado", query = " SELECT new ListaValor(t.idDestino,'',t.descripcion,t.descripcion) FROM Destino t WHERE t.activo = :activo "),
		@NamedQuery(name = "ListaValor.findPeriododAvalibleAndPeriodo", query = " SELECT new ListaValor(p.idPeriodoPago,'',CONCAT(p.descripcion,'(',p.estado,')'),p.descripcion) FROM PeriodoPago p WHERE p.estado in ('ABIE') OR p.idPeriodoPago=:idPeriodoPago"),
		@NamedQuery(name = "ListaValor.findPeriododAvalible", query = " SELECT new ListaValor(p.idPeriodoPago,'',CONCAT(p.descripcion,'(',p.estado,')'),p.descripcion) FROM PeriodoPago p WHERE p.estado in ('ABIE')"),
		@NamedQuery(name = "ListaValor.findByEstado", query = " SELECT new ListaValor(t.tipoActividad,'',t.descripcion,t.descripcion) FROM TipoActividad t WHERE t.activo = :activo "),
		@NamedQuery(name = "ListaValor.finPeridosByEstado", query = "SELECT new ListaValor(p.idPeriodoPago,'',CONCAT(p.descripcion,'(',p.estado,')'),p.descripcion) FROM PeriodoPago p WHERE  p.estado  IN (:estado) ORDER BY p.fechaFin DESC"),
		@NamedQuery(name = "ListaValor.findNoEsta", query = "SELECT new ListaValor(p.idPeriodoPago,'',CONCAT(p.descripcion,'(',p.estado,')'),p.descripcion) FROM PeriodoPago p WHERE  p.estado  NOT IN ('ING') ORDER BY p.fechaFin DESC"),
		@NamedQuery(name = "ListaValor.finAllPeridos", query = "SELECT new ListaValor(p.idPeriodoPago,'',CONCAT(p.descripcion,'(',p.estado,')'),p.descripcion) FROM PeriodoPago p ORDER BY p.fechaFin DESC"),
		@NamedQuery(name = "ListaValor.finPeridos", query = "SELECT new ListaValor(p.idPeriodoPago,'',CONCAT(p.descripcion,'(',p.estado,')'),p.descripcion) FROM PeriodoPago p WHERE (p.idPeriodoPago=:idPeriodoPago) OR p.estado  IN ( :estado1,:estado2) ORDER BY p.fechaFin DESC"),
		@NamedQuery(name = "ListaValor.findAnios", query = "SELECT DISTINCT new ListaValor(p.anio,'',str(p.anio),str(p.anio))  FROM PeriodoPago p  ORDER BY p.anio DESC"),
		@NamedQuery(name = "ListaValor.findMaxAnios", query = "SELECT DISTINCT max(r.anio) FROM PeriodoPago r"),
		@NamedQuery(name = "ListaValor.findParentesco", query = "SELECT new ListaValor(p.idParentesco,'',p.descripcion,p.descripcion)  FROM Parentesco p ORDER BY p.descripcion"),
		@NamedQuery(name = "ListaValor.findEstadoCivil", query = "select new ListaValor(ec.idEstadoCivil,'',ec.descripcion,ec.descripcion) FROM EstadoCivil ec ORDER BY ec.descripcion"),
		@NamedQuery(name = "ListaValor.findTipoLlave", query = "SELECT new ListaValor(-1,t.tipoLlave,t.descripcion,t.descripcion)  FROM TipoLlave t ORDER BY t.descripcion"),
		@NamedQuery(name = "ListaValor.findTarifa", query = "SELECT new ListaValor(t.idTarifa,'',t.descripcion,t.descripcion)  FROM Tarifa t WHERE t.tipoLlave.tipoLlave=:tipoLlave ORDER BY t.descripcion"),
		@NamedQuery(name = "ListaValor.findUserSystem", query = "SELECT new ListaValor(us.idUsuario,'',CONCAT(us.nombres,' ',us.apellidos),CONCAT(us.nombres,' ',us.apellidos))  FROM Usuario  us WHERE us.tipoUsuario='SIS' ORDER BY us.nombres"),
		@NamedQuery(name = "ListaValor.findTipoEntidad", query = "SELECT new ListaValor(-1,te.tipoEntidad,te.descripcion,te.descripcion)  FROM TipoEntidad  te WHERE te.grupo='AUD' ORDER BY te.descripcion") })
public class ListaValor {

	@Id
	private Integer valorInteger;
	private String valorString;
	private String label;
	private String tooltip;

	public ListaValor() {
		// TODO Auto-generated constructor stub
	}

	public ListaValor(Integer valorInteger, String valorString, String label, String tooltip) {
		super();
		this.valorInteger = valorInteger;
		this.valorString = valorString;
		this.label = label;
		this.tooltip = tooltip;
	}

	/**
	 * Atributo valorInteger
	 * 
	 * @return el valorInteger
	 */
	public Integer getValorInteger() {
		return valorInteger;
	}

	/**
	 * El @param valorInteger define valorInteger
	 */
	public void setValorInteger(Integer valorInteger) {
		this.valorInteger = valorInteger;
	}

	/**
	 * Atributo valorString
	 * 
	 * @return el valorString
	 */
	public String getValorString() {
		return valorString;
	}

	/**
	 * El @param valorString define valorString
	 */
	public void setValorString(String valorString) {
		this.valorString = valorString;
	}

	/**
	 * Atributo label
	 * 
	 * @return el label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * El @param label define label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Atributo tooltip
	 * 
	 * @return el tooltip
	 */
	public String getTooltip() {
		return tooltip;
	}

	/**
	 * El @param tooltip define tooltip
	 */
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

}
