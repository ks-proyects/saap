/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ec.jap.entiti.saap;

import java.io.Serializable;

import javax.persistence.Basic;
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
import javax.validation.constraints.NotNull;

import org.ec.jap.enumerations.Formapago;

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "rango_consumo")
@NamedQueries({
		@NamedQuery(name = "RangoConsumo.findByMaxTarifaAndValor", query = "SELECT r FROM RangoConsumo r WHERE r.idTarifa=:idTarifa AND  :valor > r.m3Minimo AND r.m3Maximo=-1 AND r.formaPago=:formaPago"),
		@NamedQuery(name = "RangoConsumo.findByTarifaAndValor", query = "SELECT r FROM RangoConsumo r WHERE r.idTarifa=:idTarifa AND :valor between r.m3Minimo AND r.m3Maximo AND r.formaPago=:formaPago"),
		@NamedQuery(name = "RangoConsumo.findByTarifa", query = "select rac from RangoConsumo rac where rac.idTarifa=:idTarifa ORDER BY rac.orden") })
public class RangoConsumo implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_rango_consumo")
	private Integer idRangoConsumo;

	@Basic(optional = false)
	@NotNull(message = "El campo VALOR MÍNIMO es obligatorio.")
	@Column(name = "m3_minimo")
	private Double m3Minimo;

	@Basic(optional = false)
	@NotNull(message = "El campo VALOR MÁXIMO es obligatorio.")
	@Column(name = "m3_maximo")
	private Double m3Maximo;

	@Basic(optional = false)
	@NotNull(message = "El campo USD/M3 es obligatorio.")
	@Column(name = "valor_m3")
	private Double valorM3;

	@JoinColumn(name = "id_tarifa", referencedColumnName = "id_tarifa")
	@ManyToOne
	private Tarifa idTarifa;

	@NotNull(message = "El campo FORMA DE PAGO es obligatorio.")
	@Enumerated(EnumType.STRING)
	@Column(name = "forma_pago")
	private Formapago formaPago;

	@Column(name = "valor_exceso")
	private Double valorExceso;

	@NotNull(message = "El campo ORDEN es obligatorio.")
	private Integer orden;

	public RangoConsumo() {
	}

	public RangoConsumo(Integer idRangoConsumo) {
		this.idRangoConsumo = idRangoConsumo;
	}

	public RangoConsumo(Integer idRangoConsumo, Double m3Minimo, Double m3Maximo, Double valorM3) {
		this.idRangoConsumo = idRangoConsumo;
		this.m3Minimo = m3Minimo;
		this.m3Maximo = m3Maximo;
		this.valorM3 = valorM3;
	}

	public Integer getIdRangoConsumo() {
		return idRangoConsumo;
	}

	public void setIdRangoConsumo(Integer idRangoConsumo) {
		this.idRangoConsumo = idRangoConsumo;
	}

	public Double getM3Minimo() {
		return m3Minimo;
	}

	public void setM3Minimo(Double m3Minimo) {
		this.m3Minimo = m3Minimo;
	}

	public Double getM3Maximo() {
		return m3Maximo;
	}

	public void setM3Maximo(Double m3Maximo) {
		this.m3Maximo = m3Maximo;
	}

	public Double getValorM3() {
		return valorM3;
	}

	public void setValorM3(Double valorM3) {
		this.valorM3 = valorM3;
	}

	public Tarifa getIdTarifa() {
		return idTarifa;
	}

	public void setIdTarifa(Tarifa idTarifa) {
		this.idTarifa = idTarifa;
	}

	public Formapago getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(Formapago formaPago) {
		this.formaPago = formaPago;
	}

	public Double getValorExceso() {
		return valorExceso;
	}

	public void setValorExceso(Double valorExceso) {
		this.valorExceso = valorExceso;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idRangoConsumo != null ? idRangoConsumo.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof RangoConsumo)) {
			return false;
		}
		RangoConsumo other = (RangoConsumo) object;
		if ((this.idRangoConsumo == null && other.idRangoConsumo != null) || (this.idRangoConsumo != null && !this.idRangoConsumo.equals(other.idRangoConsumo))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.RangoConsumo[ idRangoConsumo=" + idRangoConsumo + " ]";
	}

}
