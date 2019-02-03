/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ec.jap.entiti.sistema;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "accion_negocio", schema = "saap")
@NamedQueries({})
public class AccionNegocio implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "id_accion_negocio")
	private Integer idAccionNegocio;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "descripcion")
	private String descripcion;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idAccionNegocio")
	private List<RolAccionNegocio> rolAccionNegocioList;

	public AccionNegocio() {
	}

	public AccionNegocio(Integer idAccionNegocio) {
		this.idAccionNegocio = idAccionNegocio;
	}

	public AccionNegocio(Integer idAccionNegocio, String descripcion) {
		this.idAccionNegocio = idAccionNegocio;
		this.descripcion = descripcion;
	}

	public Integer getIdAccionNegocio() {
		return idAccionNegocio;
	}

	public void setIdAccionNegocio(Integer idAccionNegocio) {
		this.idAccionNegocio = idAccionNegocio;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<RolAccionNegocio> getRolAccionNegocioList() {
		return rolAccionNegocioList;
	}

	public void setRolAccionNegocioList(
			List<RolAccionNegocio> rolAccionNegocioList) {
		this.rolAccionNegocioList = rolAccionNegocioList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idAccionNegocio != null ? idAccionNegocio.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof AccionNegocio)) {
			return false;
		}
		AccionNegocio other = (AccionNegocio) object;
		if ((this.idAccionNegocio == null && other.idAccionNegocio != null)
				|| (this.idAccionNegocio != null && !this.idAccionNegocio
						.equals(other.idAccionNegocio))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.AccionNegocio[ idAccionNegocio="
				+ idAccionNegocio + " ]";
	}

}
