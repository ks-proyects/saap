/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ec.jap.entiti.saap;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "estado_civil")
@NamedQueries({ @NamedQuery(name = "EstadoCivil.findUser", query = "select count(u.idUsuario) from Usuario u where u.idEstadoCivil=:idEstadoCivil") })
public class EstadoCivil implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_estado_civil")
	private Integer idEstadoCivil;

	@Column(name = "descripcion")
	private String descripcion;

	@OneToMany(mappedBy = "idEstadoCivil")
	private List<Usuario> usuarioList;

	public EstadoCivil() {
	}

	public EstadoCivil(Integer idEstadoCivil) {
		this.idEstadoCivil = idEstadoCivil;
	}

	public Integer getIdEstadoCivil() {
		return idEstadoCivil;
	}

	public void setIdEstadoCivil(Integer idEstadoCivil) {
		this.idEstadoCivil = idEstadoCivil;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<Usuario> getUsuarioList() {
		return usuarioList;
	}

	public void setUsuarioList(List<Usuario> usuarioList) {
		this.usuarioList = usuarioList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idEstadoCivil != null ? idEstadoCivil.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof EstadoCivil)) {
			return false;
		}
		EstadoCivil other = (EstadoCivil) object;
		if ((this.idEstadoCivil == null && other.idEstadoCivil != null) || (this.idEstadoCivil != null && !this.idEstadoCivil.equals(other.idEstadoCivil))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.EstadoCivil[ idEstadoCivil=" + idEstadoCivil + " ]";
	}

}
