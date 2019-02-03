/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ec.jap.entiti.sistema;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.ec.jap.entiti.saap.Usuario;
import org.ec.jap.enumerations.ValorSiNo;

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "usuario_rol", schema = "saap")
@NamedQueries({ @NamedQuery(name = "UsuarioRol.findByRol", query = "select (select ur from UsuarioRol ur where ur.idUsuario=:idUsuario and ur.idRol=rol), rol from Rol rol where rol.activo=:activo") })
public class UsuarioRol implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario_rol")
	private Integer idUsuarioRol;

	@Transient
	@Enumerated(EnumType.STRING)
	private ValorSiNo activo;

	@JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
	@ManyToOne(optional = false)
	private Usuario idUsuario;

	@JoinColumn(name = "id_rol", referencedColumnName = "id_rol")
	@ManyToOne(optional = false)
	private Rol idRol;

	public UsuarioRol() {
	}

	public UsuarioRol(Integer idUsuarioRol) {
		this.idUsuarioRol = idUsuarioRol;
	}

	public Integer getIdUsuarioRol() {
		return idUsuarioRol;
	}

	public void setIdUsuarioRol(Integer idUsuarioRol) {
		this.idUsuarioRol = idUsuarioRol;
	}

	public ValorSiNo getActivo() {
		return activo;
	}

	public void setActivo(ValorSiNo activo) {
		this.activo = activo;
	}

	public Usuario getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Usuario idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Rol getIdRol() {
		return idRol;
	}

	public void setIdRol(Rol idRol) {
		this.idRol = idRol;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idUsuarioRol != null ? idUsuarioRol.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof UsuarioRol)) {
			return false;
		}
		UsuarioRol other = (UsuarioRol) object;
		if ((this.idUsuarioRol == null && other.idUsuarioRol != null) || (this.idUsuarioRol != null && !this.idUsuarioRol.equals(other.idUsuarioRol))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.UsuarioRol[ idUsuarioRol=" + idUsuarioRol + " ]";
	}

}
