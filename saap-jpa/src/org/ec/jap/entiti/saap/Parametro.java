/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ec.jap.entiti.saap;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.ec.jap.entiti.sistema.Comunidad;

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "parametro")
@NamedQueries({
		@NamedQuery(name = "Parametro.findAllByFiltro", query = "select p from Parametro p WHERE UPPER(p.descripcion) LIKE upper(CONCAT('%',:filtro,'%'))"),
		@NamedQuery(name = "Parametro.findValorNumerico", query = "select DISTINCT p.valorNumerico from Parametro p WHERE p.parametro=:parametro AND p.idComunidad.idComunidad=:idComunidad AND (p.tipoLlave.tipoLlave=:tipoLlave OR :tipoLlave='' )"),
		@NamedQuery(name = "Parametro.findValorEntero", query = "select DISTINCT p.valorEntero from Parametro p WHERE p.parametro=:parametro AND p.idComunidad.idComunidad=:idComunidad AND (p.tipoLlave.tipoLlave=:tipoLlave OR :tipoLlave='' )"),
		@NamedQuery(name = "Parametro.findValorFecha", query = "select DISTINCT p.valorFecha from Parametro p WHERE p.parametro=:parametro AND p.idComunidad.idComunidad=:idComunidad AND (p.tipoLlave.tipoLlave=:tipoLlave OR :tipoLlave='' )"),
		@NamedQuery(name = "Parametro.findValorString", query = "select DISTINCT p.valorString from Parametro p WHERE p.parametro=:parametro AND p.idComunidad.idComunidad=:idComunidad AND (p.tipoLlave.tipoLlave=:tipoLlave OR :tipoLlave='' )") })
public class Parametro implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "parametro")
	private String parametro;
	@Column(name = "valor_entero")
	private Integer valorEntero;
	@Column(name = "valor_numerico")
	private Double valorNumerico;
	@Column(name = "valor_fecha")
	@Temporal(TemporalType.DATE)
	private Date valorFecha;
	@Size(max = 2147483647)
	@Column(name = "valor_string")
	private String valorString;
	@JoinColumn(name = "tipo_llave", referencedColumnName = "tipo_llave")
	@ManyToOne
	private TipoLlave tipoLlave;
	@JoinColumn(name = "id_comunidad", referencedColumnName = "id_comunidad")
	@ManyToOne
	private Comunidad idComunidad;

	private String descripcion;

	public Parametro() {
	}

	public Parametro(String parametro) {
		this.parametro = parametro;
	}

	public String getParametro() {
		return parametro;
	}

	public void setParametro(String parametro) {
		this.parametro = parametro;
	}

	public Integer getValorEntero() {
		return valorEntero;
	}

	public void setValorEntero(Integer valorEntero) {
		this.valorEntero = valorEntero;
	}

	public Double getValorNumerico() {
		return valorNumerico;
	}

	public void setValorNumerico(Double valorNumerico) {
		this.valorNumerico = valorNumerico;
	}

	public Date getValorFecha() {
		return valorFecha;
	}

	public void setValorFecha(Date valorFecha) {
		this.valorFecha = valorFecha;
	}

	public String getValorString() {
		return valorString;
	}

	public void setValorString(String valorString) {
		this.valorString = valorString;
	}

	public TipoLlave getTipoLlave() {
		return tipoLlave;
	}

	public void setTipoLlave(TipoLlave tipoLlave) {
		this.tipoLlave = tipoLlave;
	}

	public Comunidad getIdComunidad() {
		return idComunidad;
	}

	public void setIdComunidad(Comunidad idComunidad) {
		this.idComunidad = idComunidad;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (parametro != null ? parametro.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Parametro)) {
			return false;
		}
		Parametro other = (Parametro) object;
		if ((this.parametro == null && other.parametro != null) || (this.parametro != null && !this.parametro.equals(other.parametro))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.Parametro[ parametro=" + parametro + " ]";
	}

}
