/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ec.jap.entiti.sistema;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.ec.jap.enumerations.PerfilEstado;

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "perfil", schema = "saap")
@NamedQueries({
		@NamedQuery(name = "Perfil.findByFilter", query = "select pe from Perfil pe where UPPER(pe.descripcion) LIKE UPPER(CONCAT('%',:descripcion,'%'))  OR  UPPER(pe.activo.descripcion) LIKE UPPER(CONCAT('%',:descripcion,'%'))"),
		@NamedQuery(name = "Perfil.findActivos", query = "select pe from Perfil pe where pe.activo=:activo order by pe.descripcion") })
public class Perfil implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_perfil")
	private Integer idPerfil;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647, message = "El campo DESCRIPCIÓN es obligatorio.")
	@Column(name = "descripcion")
	private String descripcion;

	@Enumerated(EnumType.STRING)
	@Column(name = "p_activo")
	private PerfilEstado activo;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idPerfil")
	private List<UsuarioPerfil> usuarioPerfilList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idPerfil")
	private List<PerfilElementoSistema> perfilElementoSistemaList;

	public Perfil() {
	}

	public Perfil(Integer idPerfil) {
		this.idPerfil = idPerfil;
	}

	public Integer getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(Integer idPerfil) {
		this.idPerfil = idPerfil;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public PerfilEstado getActivo() {
		return activo;
	}

	public void setActivo(PerfilEstado activo) {
		this.activo = activo;
	}

	public List<UsuarioPerfil> getUsuarioPerfilList() {
		return usuarioPerfilList;
	}

	public void setUsuarioPerfilList(List<UsuarioPerfil> usuarioPerfilList) {
		this.usuarioPerfilList = usuarioPerfilList;
	}

	public List<PerfilElementoSistema> getPerfilElementoSistemaList() {
		return perfilElementoSistemaList;
	}

	public void setPerfilElementoSistemaList(List<PerfilElementoSistema> perfilElementoSistemaList) {
		this.perfilElementoSistemaList = perfilElementoSistemaList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idPerfil != null ? idPerfil.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Perfil)) {
			return false;
		}
		Perfil other = (Perfil) object;
		if ((this.idPerfil == null && other.idPerfil != null) || (this.idPerfil != null && !this.idPerfil.equals(other.idPerfil))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.Perfil[ idPerfil=" + idPerfil + " ]";
	}

}
