/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ec.jap.entiti.sistema;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "rol_accion_negocio", schema = "saap")
@NamedQueries({ @NamedQuery(name = "RolAccionNegocio.fyndAllByRol", query = " select (select rolan from RolAccionNegocio rolan where rolan.idAccionNegocio=ane and rolan.idRol=:idRol ), ane from AccionNegocio ane  ") })
public class RolAccionNegocio implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_rol_accion_negocio")
	private Integer idRolAccionNegocio;

	@JoinColumn(name = "id_rol", referencedColumnName = "id_rol")
	@ManyToOne(optional = false)
	private Rol idRol;

	@JoinColumn(name = "id_accion_negocio", referencedColumnName = "id_accion_negocio")
	@ManyToOne(optional = false)
	private AccionNegocio idAccionNegocio;

	@Transient
	private Boolean seleccionado;

	public RolAccionNegocio() {
	}

	public RolAccionNegocio(Integer idRolAccionNegocio) {
		this.idRolAccionNegocio = idRolAccionNegocio;
	}

	public Integer getIdRolAccionNegocio() {
		return idRolAccionNegocio;
	}

	public void setIdRolAccionNegocio(Integer idRolAccionNegocio) {
		this.idRolAccionNegocio = idRolAccionNegocio;
	}

	public Rol getIdRol() {
		return idRol;
	}

	public void setIdRol(Rol idRol) {
		this.idRol = idRol;
	}

	public AccionNegocio getIdAccionNegocio() {
		return idAccionNegocio;
	}

	public void setIdAccionNegocio(AccionNegocio idAccionNegocio) {
		this.idAccionNegocio = idAccionNegocio;
	}

	public Boolean getSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(Boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idRolAccionNegocio != null ? idRolAccionNegocio.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof RolAccionNegocio)) {
			return false;
		}
		RolAccionNegocio other = (RolAccionNegocio) object;
		if ((this.idRolAccionNegocio == null && other.idRolAccionNegocio != null) || (this.idRolAccionNegocio != null && !this.idRolAccionNegocio.equals(other.idRolAccionNegocio))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.RolAccionNegocio[ idRolAccionNegocio=" + idRolAccionNegocio + " ]";
	}

}
