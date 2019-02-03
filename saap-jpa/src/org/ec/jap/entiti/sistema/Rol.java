/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ec.jap.entiti.sistema;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.ec.jap.enumerations.ValorSiNo;

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "rol", schema = "saap")
@NamedQueries({ @NamedQuery(name = "Rol.findUser", query = "select count(ur.idUsuarioRol) from UsuarioRol ur where ur.idRol=:idRol"),
		@NamedQuery(name = "Rol.findAccion", query = "select count(ran.idRolAccionNegocio) from RolAccionNegocio ran where ran.idRol=:idRol"), })
public class Rol implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_rol")
	private Integer idRol;

	@Basic(optional = false)
	@Size(min = 1, max = 2147483647, message = "El campo DESCRIPCIÓN es obligatorio.")
	@Column(name = "descripcion")
	private String descripcion;

	@OneToMany(mappedBy = "idRol")
	private List<UsuarioRol> usuarioRolList;
	@OneToMany(mappedBy = "idRol")
	private List<RolAccionNegocio> rolAccionNegocioList;

	@Enumerated(EnumType.STRING)
	private ValorSiNo activo;

	public Rol() {
	}

	public Rol(Integer idRol) {
		this.idRol = idRol;
	}

	public Rol(Integer idRol, String descripcion) {
		this.idRol = idRol;
		this.descripcion = descripcion;
	}

	public Integer getIdRol() {
		return idRol;
	}

	public void setIdRol(Integer idRol) {
		this.idRol = idRol;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<UsuarioRol> getUsuarioRolList() {
		return usuarioRolList;
	}

	public void setUsuarioRolList(List<UsuarioRol> usuarioRolList) {
		this.usuarioRolList = usuarioRolList;
	}

	public List<RolAccionNegocio> getRolAccionNegocioList() {
		return rolAccionNegocioList;
	}

	public void setRolAccionNegocioList(List<RolAccionNegocio> rolAccionNegocioList) {
		this.rolAccionNegocioList = rolAccionNegocioList;
	}

	public ValorSiNo getActivo() {
		return activo;
	}

	public void setActivo(ValorSiNo activo) {
		this.activo = activo;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idRol != null ? idRol.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Rol)) {
			return false;
		}
		Rol other = (Rol) object;
		if ((this.idRol == null && other.idRol != null) || (this.idRol != null && !this.idRol.equals(other.idRol))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.Rol[ idRol=" + idRol + " ]";
	}

}
