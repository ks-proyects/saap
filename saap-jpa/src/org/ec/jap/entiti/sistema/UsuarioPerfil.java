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

import org.ec.jap.entiti.saap.Usuario;
import org.ec.jap.enumerations.ValorSiNo;

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "usuario_perfil", schema = "saap")
@NamedQueries({ @NamedQuery(name = "UsuarioPerfil.fyndByPerfil", query = " select up from UsuarioPerfil up where up.idPerfil=:idPerfil "),
		@NamedQuery(name = "UsuarioPerfil.fyndByUser", query = " select up from UsuarioPerfil up left outer join  up.idPerfil p  "),
		@NamedQuery(name = "UsuarioPerfil.fyndByUserAndPerfil", query = " select up from UsuarioPerfil up where up.idPerfil=:idPerfil and  up.idUsuario=:idUsuario ") })
public class UsuarioPerfil implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario_perfil")
	private Integer idUsuarioPerfil;

	@Enumerated(EnumType.STRING)
	@Column(name = "solo_lectura")
	private ValorSiNo soloLectura;

	@Enumerated(EnumType.STRING)
	@Column(name = "lectura_escritura")
	private ValorSiNo lecturaEscritura;

	@Enumerated(EnumType.STRING)
	@Column(name = "up_activo")
	private ValorSiNo activo;

	@JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
	@ManyToOne(optional = false)
	private Usuario idUsuario;

	@JoinColumn(name = "id_perfil", referencedColumnName = "id_perfil")
	@ManyToOne(optional = false)
	private Perfil idPerfil;

	public UsuarioPerfil() {
	}

	public UsuarioPerfil(Integer idUsuarioPerfil) {
		this.idUsuarioPerfil = idUsuarioPerfil;
	}

	public Integer getIdUsuarioPerfil() {
		return idUsuarioPerfil;
	}

	public void setIdUsuarioPerfil(Integer idUsuarioPerfil) {
		this.idUsuarioPerfil = idUsuarioPerfil;
	}

	public ValorSiNo getSoloLectura() {
		return soloLectura;
	}

	public void setSoloLectura(ValorSiNo soloLectura) {
		this.soloLectura = soloLectura;
	}

	public ValorSiNo getLlecturaEscritura() {
		return lecturaEscritura;
	}

	public void setLlecturaEscritura(ValorSiNo llecturaEscritura) {
		this.lecturaEscritura = llecturaEscritura;
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

	public Perfil getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(Perfil idPerfil) {
		this.idPerfil = idPerfil;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idUsuarioPerfil != null ? idUsuarioPerfil.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof UsuarioPerfil)) {
			return false;
		}
		UsuarioPerfil other = (UsuarioPerfil) object;
		if ((this.idUsuarioPerfil == null && other.idUsuarioPerfil != null) || (this.idUsuarioPerfil != null && !this.idUsuarioPerfil.equals(other.idUsuarioPerfil))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.UsuarioPerfil[ idUsuarioPerfil=" + idUsuarioPerfil + " ]";
	}

}
