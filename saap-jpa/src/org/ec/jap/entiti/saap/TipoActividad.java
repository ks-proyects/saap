/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ec.jap.entiti.saap;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.ec.jap.enumerations.ValorSiNo;

/**
 * 
 * @author Freddy
 */
@Entity
@Table(name = "tipo_actividad")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "TipoActividad.findActivo", query = "SELECT t FROM TipoActividad t WHERE t.activo = :activo"),
		@NamedQuery(name = "TipoActividad.findAll", query = "SELECT t FROM TipoActividad t"),
		@NamedQuery(name = "TipoActividad.findByTipoActividad", query = "SELECT t FROM TipoActividad t WHERE t.tipoActividad = :tipoActividad"),
		@NamedQuery(name = "TipoActividad.findByDescripcion", query = "SELECT t FROM TipoActividad t WHERE t.descripcion = :descripcion"),
		@NamedQuery(name = "TipoActividad.findByActivo", query = "SELECT t FROM TipoActividad t WHERE t.activo = :activo"),
		@NamedQuery(name = "TipoActividad.findByValorMulta", query = "SELECT t FROM TipoActividad t WHERE t.valorMulta = :valorMulta") })
public class TipoActividad implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tipo_actividad")
	private Integer tipoActividad;

	@Basic(optional = false)
	@Size(min = 1, max = 2000, message = "La descrición es obligatorio.")
	@Column(name = "descripcion")
	private String descripcion;
	@Basic(optional = false)
	@Enumerated(EnumType.STRING)
	@Column(name = "activo")
	private ValorSiNo activo;

	@Basic(optional = false)
	@NotNull(message = "El valor de la multa es obligatorio.")
	@Column(name = "valor_multa")
	private Double valorMulta;

	@OneToMany(mappedBy = "tipoActividad")
	private List<Actividad> actividadList;

	public TipoActividad() {
	}

	public TipoActividad(Integer tipoActividad) {
		this.tipoActividad = tipoActividad;
	}

	public Integer getTipoActividad() {
		return tipoActividad;
	}

	public void setTipoActividad(Integer tipoActividad) {
		this.tipoActividad = tipoActividad;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public ValorSiNo getActivo() {
		return activo;
	}

	public void setActivo(ValorSiNo activo) {
		this.activo = activo;
	}

	public Double getValorMulta() {
		return valorMulta;
	}

	public void setValorMulta(Double valorMulta) {
		this.valorMulta = valorMulta;
	}

	@XmlTransient
	public List<Actividad> getActividadList() {
		return actividadList;
	}

	public void setActividadList(List<Actividad> actividadList) {
		this.actividadList = actividadList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (tipoActividad != null ? tipoActividad.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof TipoActividad)) {
			return false;
		}
		TipoActividad other = (TipoActividad) object;
		if ((this.tipoActividad == null && other.tipoActividad != null) || (this.tipoActividad != null && !this.tipoActividad.equals(other.tipoActividad))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "uce.TipoActividad[ tipoActividad=" + tipoActividad + " ]";
	}

}
