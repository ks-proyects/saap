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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "tarifa")
@NamedQueries({
		@NamedQuery(name = "Tarifa.findRangoConsumos", query = "select count(rc.idRangoConsumo) from RangoConsumo rc where rc.idTarifa=:idTarifa ") })
public class Tarifa implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_tarifa")
	private Integer idTarifa;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "descripcion")
	private String descripcion;
	@OneToMany(mappedBy = "idTarifa")
	private List<RangoConsumo> rangoConsumoList;
	
	@JoinColumn(name = "tipo_llave", referencedColumnName = "tipo_llave")
	@ManyToOne
	private TipoLlave tipoLlave;
	
	@OneToMany(mappedBy = "idTarifa")
	private List<Llave> llaveList;

	@OneToMany(mappedBy = "tarifa")
	private List<Usuario> usuarios;

	@Column(name = "basico_pago")
	private Double basicoPago;

	@Column(name = "multa_no_pago")
	private Double multaNoPago;
	
	@Column(name = "valor_alcantarillado")
	private Double valorAlcantarillado;

	public Tarifa() {
	}

	public Tarifa(Integer idTarifa) {
		this.idTarifa = idTarifa;
	}

	public Tarifa(Integer idTarifa, String descripcion) {
		this.idTarifa = idTarifa;
		this.descripcion = descripcion;
	}

	public Integer getIdTarifa() {
		return idTarifa;
	}

	public void setIdTarifa(Integer idTarifa) {
		this.idTarifa = idTarifa;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<RangoConsumo> getRangoConsumoList() {
		return rangoConsumoList;
	}

	public void setRangoConsumoList(List<RangoConsumo> rangoConsumoList) {
		this.rangoConsumoList = rangoConsumoList;
	}

	public TipoLlave getTipoLlave() {
		return tipoLlave;
	}

	public void setTipoLlave(TipoLlave tipoLlave) {
		this.tipoLlave = tipoLlave;
	}

	public List<Llave> getLlaveList() {
		return llaveList;
	}

	public void setLlaveList(List<Llave> llaveList) {
		this.llaveList = llaveList;
	}

	public Double getBasicoPago() {
		return basicoPago;
	}

	public void setBasicoPago(Double basicoPago) {
		this.basicoPago = basicoPago;
	}

	public Double getMultaNoPago() {
		return multaNoPago;
	}

	public void setMultaNoPago(Double multaNoPago) {
		this.multaNoPago = multaNoPago;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	

	public Double getValorAlcantarillado() {
		return valorAlcantarillado;
	}

	public void setValorAlcantarillado(Double valorAlcantarillado) {
		this.valorAlcantarillado = valorAlcantarillado;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idTarifa != null ? idTarifa.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Tarifa)) {
			return false;
		}
		Tarifa other = (Tarifa) object;
		if ((this.idTarifa == null && other.idTarifa != null)
				|| (this.idTarifa != null && !this.idTarifa.equals(other.idTarifa))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.Tarifa[ idTarifa=" + idTarifa + " ]";
	}

}
