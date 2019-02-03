/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ec.jap.entiti.saap;

import java.io.Serializable;

import javax.persistence.Basic;
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
import javax.validation.constraints.NotNull;

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "representante")
@NamedQueries({ @NamedQuery(name = "Representante.findRepresentado", query = "SELECT r FROM Representante r WHERE r.idRepresentado.idUsuario=:idUsuario order by r.orden"),
		@NamedQuery(name = "Representante.findByCed", query = "SELECT COUNT(r.idRepresentante) FROM Representante r inner join r.idUsuario u WHERE u.cedula=:cedula") })
public class Representante implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_representante")
	private Integer idRepresentante;
	@Basic(optional = false)
	@NotNull
	@Column(name = "orden")
	private int orden;
	@JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
	@ManyToOne(optional = false)
	private Usuario idUsuario;
	@JoinColumn(name = "id_representado", referencedColumnName = "id_usuario")
	@ManyToOne(optional = false)
	private Usuario idRepresentado;
	@JoinColumn(name = "id_parentesco", referencedColumnName = "id_parentesco")
	@ManyToOne
	private Parentesco idParentesco;

	public Representante() {
	}

	public Representante(Integer idRepresentante) {
		this.idRepresentante = idRepresentante;
	}

	public Representante(Integer idRepresentante, int orden) {
		this.idRepresentante = idRepresentante;
		this.orden = orden;
	}

	public Integer getIdRepresentante() {
		return idRepresentante;
	}

	public void setIdRepresentante(Integer idRepresentante) {
		this.idRepresentante = idRepresentante;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public Usuario getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Usuario idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Usuario getIdRepresentado() {
		return idRepresentado;
	}

	public void setIdRepresentado(Usuario idRepresentado) {
		this.idRepresentado = idRepresentado;
	}

	public Parentesco getIdParentesco() {
		return idParentesco;
	}

	public void setIdParentesco(Parentesco idParentesco) {
		this.idParentesco = idParentesco;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idRepresentante != null ? idRepresentante.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Representante)) {
			return false;
		}
		Representante other = (Representante) object;
		if ((this.idRepresentante == null && other.idRepresentante != null) || (this.idRepresentante != null && !this.idRepresentante.equals(other.idRepresentante))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.Representante[ idRepresentante=" + idRepresentante + " ]";
	}
}
