/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ec.jap.entiti.sistema;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "cambio_estado_condicion")
@NamedQueries({ @NamedQuery(name = "CambioEstadoCondicion.findByCambioEstado", query = "SELECT c FROM CambioEstadoCondicion c WHERE c.idCambioEstado=:idCambioEstado and c.aplica='S'"), })
public class CambioEstadoCondicion implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "id_cambio_estado_condicion")
	private Integer idCambioEstadoCondicion;
	@Size(max = 2147483647)
	@Column(name = "sentencia_valida")
	private String sentenciaValida;
	@JoinColumn(name = "id_cambio_estado", referencedColumnName = "id_cambio_estado")
	@ManyToOne
	private CambioEstado idCambioEstado;

	private String aplica;

	private String descripcion;

	public CambioEstadoCondicion() {
	}

	public CambioEstadoCondicion(Integer idCambioEstadoCondicion) {
		this.idCambioEstadoCondicion = idCambioEstadoCondicion;
	}

	public Integer getIdCambioEstadoCondicion() {
		return idCambioEstadoCondicion;
	}

	public void setIdCambioEstadoCondicion(Integer idCambioEstadoCondicion) {
		this.idCambioEstadoCondicion = idCambioEstadoCondicion;
	}

	public String getSentenciaValida() {
		return sentenciaValida;
	}

	public void setSentenciaValida(String sentenciaValida) {
		this.sentenciaValida = sentenciaValida;
	}

	public CambioEstado getIdCambioEstado() {
		return idCambioEstado;
	}

	public void setIdCambioEstado(CambioEstado idCambioEstado) {
		this.idCambioEstado = idCambioEstado;
	}

	public String getAplica() {
		return aplica;
	}

	public void setAplica(String aplica) {
		this.aplica = aplica;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idCambioEstadoCondicion != null ? idCambioEstadoCondicion.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof CambioEstadoCondicion)) {
			return false;
		}
		CambioEstadoCondicion other = (CambioEstadoCondicion) object;
		if ((this.idCambioEstadoCondicion == null && other.idCambioEstadoCondicion != null)
				|| (this.idCambioEstadoCondicion != null && !this.idCambioEstadoCondicion.equals(other.idCambioEstadoCondicion))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.CambioEstadoCondicion[ idCambioEstadoCondicion=" + idCambioEstadoCondicion + " ]";
	}

}
