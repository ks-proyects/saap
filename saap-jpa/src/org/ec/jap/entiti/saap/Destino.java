/*
O * To change this template, choose Tools | Templates
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

import org.ec.jap.enumerations.ValorSiNo;

/**
 * 
 * @author Freddy
 */
@Entity
@Table(name = "destino")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Destino.findAll", query = "SELECT d FROM Destino d"),
		@NamedQuery(name = "Destino.findByIdDestino", query = "SELECT d FROM Destino d WHERE d.idDestino = :idDestino"),
		@NamedQuery(name = "Destino.findByDescripcion", query = "SELECT d FROM Destino d WHERE d.descripcion = :descripcion"),
		@NamedQuery(name = "Destino.findByActivo", query = "SELECT d FROM Destino d WHERE d.activo = :activo") })
public class Destino implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_destino")
	private Integer idDestino;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2000)
	@Column(name = "descripcion")
	private String descripcion;

	@Basic(optional = false)
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "activo")
	private ValorSiNo activo;

	@OneToMany(mappedBy = "idDestino")
	private List<Gasto> gastoList;

	public Destino() {
	}

	public Destino(Integer idDestino) {
		this.idDestino = idDestino;
	}

	public Integer getIdDestino() {
		return idDestino;
	}

	public void setIdDestino(Integer idDestino) {
		this.idDestino = idDestino;
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

	public List<Gasto> getGastoList() {
		return gastoList;
	}

	public void setGastoList(List<Gasto> gastoList) {
		this.gastoList = gastoList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idDestino != null ? idDestino.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Destino)) {
			return false;
		}
		Destino other = (Destino) object;
		if ((this.idDestino == null && other.idDestino != null) || (this.idDestino != null && !this.idDestino.equals(other.idDestino))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "uce.Destino[ idDestino=" + idDestino + " ]";
	}

}
