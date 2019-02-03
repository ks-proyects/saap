/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ec.jap.entiti.saap;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.ec.jap.enumerations.AccionType;

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "tipo_registro")
@NamedQueries({ @NamedQuery(name = "TipoRegistro.findByTipo", query = "select tr from TipoRegistro tr where tr.accion=:accion") })
public class TipoRegistro implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "tipo_registro")
	private String tipoRegistro;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "descripcion")
	private String descripcion;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoRegistro")
	private List<Lectura> lecturaList;
	@OneToMany(mappedBy = "tipoRegistro")
	private List<RegistroEconomico> registroEconomicoList;

	@Enumerated(EnumType.STRING)
	@Column(name = "accion")
	private AccionType accion;

	public TipoRegistro() {
	}

	public TipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public TipoRegistro(String tipoRegistro, String descripcion) {
		this.tipoRegistro = tipoRegistro;
		this.descripcion = descripcion;
	}

	public String getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<Lectura> getLecturaList() {
		return lecturaList;
	}

	public void setLecturaList(List<Lectura> lecturaList) {
		this.lecturaList = lecturaList;
	}

	public List<RegistroEconomico> getRegistroEconomicoList() {
		return registroEconomicoList;
	}

	public void setRegistroEconomicoList(List<RegistroEconomico> registroEconomicoList) {
		this.registroEconomicoList = registroEconomicoList;
	}

	public AccionType getAccion() {
		return accion;
	}

	public void setAccion(AccionType accion) {
		this.accion = accion;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (tipoRegistro != null ? tipoRegistro.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof TipoRegistro)) {
			return false;
		}
		TipoRegistro other = (TipoRegistro) object;
		if ((this.tipoRegistro == null && other.tipoRegistro != null) || (this.tipoRegistro != null && !this.tipoRegistro.equals(other.tipoRegistro))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.TipoRegistro[ tipoRegistro=" + tipoRegistro + " ]";
	}

}
