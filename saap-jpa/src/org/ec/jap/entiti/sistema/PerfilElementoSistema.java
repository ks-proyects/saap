/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ec.jap.entiti.sistema;

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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "perfil_elemento_sistema", schema = "saap")
@NamedQueries({
		@NamedQuery(name = "PerfilElementoSistema.fyndByPerfil", query = " select esi from PerfilElementoSistema esi where esi.idPerfil=:idPerfil "),
		@NamedQuery(name = "PerfilElementoSistema.fyndAllByPerfil", query = " select (select pes from PerfilElementoSistema pes where pes.idElementoSistema=es and pes.idPerfil=:idPerfil ), es from ElementoSistema es   ") })
public class PerfilElementoSistema implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_perfil_elemento_sistema")
	private Integer idPerfilElementoSistema;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "pes_activo")
	private String acctivo;
	@JoinColumn(name = "id_perfil", referencedColumnName = "id_perfil")
	@ManyToOne(optional = false)
	private Perfil idPerfil;
	@JoinColumn(name = "id_elemento_sistema", referencedColumnName = "id_elemento_sistema")
	@ManyToOne(optional = false)
	private ElementoSistema idElementoSistema;

	@Transient
	private Boolean seleccionado;

	public PerfilElementoSistema() {
	}

	public PerfilElementoSistema(Integer idPerfilElementoSistema) {
		this.idPerfilElementoSistema = idPerfilElementoSistema;
	}

	public PerfilElementoSistema(Integer idPerfilElementoSistema, String acctivo) {
		this.idPerfilElementoSistema = idPerfilElementoSistema;
		this.acctivo = acctivo;
	}

	public Integer getIdPerfilElementoSistema() {
		return idPerfilElementoSistema;
	}

	public void setIdPerfilElementoSistema(Integer idPerfilElementoSistema) {
		this.idPerfilElementoSistema = idPerfilElementoSistema;
	}

	public String getAcctivo() {
		return acctivo;
	}

	public void setAcctivo(String acctivo) {
		this.acctivo = acctivo;
	}

	public Perfil getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(Perfil idPerfil) {
		this.idPerfil = idPerfil;
	}

	public ElementoSistema getIdElementoSistema() {
		return idElementoSistema;
	}

	public void setIdElementoSistema(ElementoSistema idElementoSistema) {
		this.idElementoSistema = idElementoSistema;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idPerfilElementoSistema != null ? idPerfilElementoSistema.hashCode() : 0);
		return hash;
	}

	public Boolean getSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(Boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof PerfilElementoSistema)) {
			return false;
		}
		PerfilElementoSistema other = (PerfilElementoSistema) object;
		if ((this.idPerfilElementoSistema == null && other.idPerfilElementoSistema != null)
				|| (this.idPerfilElementoSistema != null && !this.idPerfilElementoSistema.equals(other.idPerfilElementoSistema))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.PerfilElementoSistema[ idPerfilElementoSistema=" + idPerfilElementoSistema + " ]";
	}

}
