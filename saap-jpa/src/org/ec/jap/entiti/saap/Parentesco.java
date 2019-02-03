/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ec.jap.entiti.saap;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "parentesco")
@NamedQueries({})
public class Parentesco implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "id_parentesco")
	private Integer idParentesco;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "descripcion")
	private String descripcion;
	@OneToMany(mappedBy = "idParentesco")
	private List<Representante> representanteList;

	public Parentesco() {
	}

	public Parentesco(Integer idParentesco) {
		this.idParentesco = idParentesco;
	}

	public Parentesco(Integer idParentesco, String descripcion) {
		this.idParentesco = idParentesco;
		this.descripcion = descripcion;
	}

	public Integer getIdParentesco() {
		return idParentesco;
	}

	public void setIdParentesco(Integer idParentesco) {
		this.idParentesco = idParentesco;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<Representante> getRepresentanteList() {
		return representanteList;
	}

	public void setRepresentanteList(List<Representante> representanteList) {
		this.representanteList = representanteList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idParentesco != null ? idParentesco.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Parentesco)) {
			return false;
		}
		Parentesco other = (Parentesco) object;
		if ((this.idParentesco == null && other.idParentesco != null) || (this.idParentesco != null && !this.idParentesco.equals(other.idParentesco))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.Parentesco[ idParentesco=" + idParentesco + " ]";
	}

}
