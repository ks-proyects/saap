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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "estado_entidad")
@NamedQueries({ @NamedQuery(name = "EstadoEntidad.findByEstadoAndTipo", query = "SELECT e FROM EstadoEntidad e where  e.estado=:estado and e.tipoEntidad.tipoEntidad=:tipoEntidad") })
public class EstadoEntidad implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@Column(name = "id_estado_entidad")
	private Integer idEstadoEntidad;
	@Basic(optional = false)
	@Size(min = 1, max = 2147483647)
	@Column(name = "estado")
	private String estado;
	@Basic(optional = false)
	@Size(min = 1, max = 2147483647)
	@Column(name = "descripcion")
	private String descripcion;
	@JoinColumn(name = "tipo_entidad", referencedColumnName = "tipo_entidad")
	@ManyToOne(optional = false)
	private TipoEntidad tipoEntidad;
	@OneToMany(mappedBy = "idEstadoNuevo")
	private List<CambioEstado> cambioEstadoList;
	@OneToMany(mappedBy = "idEstadoAnterior")
	private List<CambioEstado> cambioEstadoList1;

	public EstadoEntidad() {
	}

	public EstadoEntidad(Integer idEstadoEntidad) {
		this.idEstadoEntidad = idEstadoEntidad;
	}

	public EstadoEntidad(Integer idEstadoEntidad, String estado, String descripcion) {
		this.idEstadoEntidad = idEstadoEntidad;
		this.estado = estado;
		this.descripcion = descripcion;
	}

	public Integer getIdEstadoEntidad() {
		return idEstadoEntidad;
	}

	public void setIdEstadoEntidad(Integer idEstadoEntidad) {
		this.idEstadoEntidad = idEstadoEntidad;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public TipoEntidad getTipoEntidad() {
		return tipoEntidad;
	}

	public void setTipoEntidad(TipoEntidad tipoEntidad) {
		this.tipoEntidad = tipoEntidad;
	}

	public List<CambioEstado> getCambioEstadoList() {
		return cambioEstadoList;
	}

	public void setCambioEstadoList(List<CambioEstado> cambioEstadoList) {
		this.cambioEstadoList = cambioEstadoList;
	}

	public List<CambioEstado> getCambioEstadoList1() {
		return cambioEstadoList1;
	}

	public void setCambioEstadoList1(List<CambioEstado> cambioEstadoList1) {
		this.cambioEstadoList1 = cambioEstadoList1;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idEstadoEntidad != null ? idEstadoEntidad.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof EstadoEntidad)) {
			return false;
		}
		EstadoEntidad other = (EstadoEntidad) object;
		if ((this.idEstadoEntidad == null && other.idEstadoEntidad != null) || (this.idEstadoEntidad != null && !this.idEstadoEntidad.equals(other.idEstadoEntidad))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.EstadoEntidad[ idEstadoEntidad=" + idEstadoEntidad + " ]";
	}

}
