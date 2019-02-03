/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ec.jap.entiti.saap;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "libro_diario")
@NamedQueries({})
public class LibroDiario implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id_libro_diario")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idLibroDiario;
	@Column(name = "mes_ejercicio")
	private Integer mesEjercicio;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "descripcion")
	private String descripcion;
	@Basic(optional = false)
	@NotNull
	@Column(name = "debe")
	private Double debe;
	@Basic(optional = false)
	@NotNull
	@Column(name = "haber")
	private Double haber;
	@Column(name = "saldo")
	private BigInteger saldo;
	@Size(max = 2147483647)
	@Column(name = "ld_estado")
	private String estado;
	@JoinColumn(name = "id_cuenta", referencedColumnName = "id_cuenta")
	@ManyToOne(optional = false)
	private Cuenta idCuenta;
	@JoinColumn(name = "id_asiento", referencedColumnName = "id_asiento")
	@ManyToOne(optional = false)
	private Asiento idAsiento;

	public LibroDiario() {
	}

	public LibroDiario(Integer idLibroDiario) {
		this.idLibroDiario = idLibroDiario;
	}

	public Integer getIdLibroDiario() {
		return idLibroDiario;
	}

	public void setIdLibroDiario(Integer idLibroDiario) {
		this.idLibroDiario = idLibroDiario;
	}

	public Integer getMesEjercicio() {
		return mesEjercicio;
	}

	public void setMesEjercicio(Integer mesEjercicio) {
		this.mesEjercicio = mesEjercicio;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Double getDebe() {
		return debe;
	}

	public void setDebe(Double debe) {
		this.debe = debe;
	}

	public Double getHaber() {
		return haber;
	}

	public void setHaber(Double haber) {
		this.haber = haber;
	}

	public BigInteger getSaldo() {
		return saldo;
	}

	public void setSaldo(BigInteger saldo) {
		this.saldo = saldo;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Cuenta getIdCuenta() {
		return idCuenta;
	}

	public void setIdCuenta(Cuenta idCuenta) {
		this.idCuenta = idCuenta;
	}

	public Asiento getIdAsiento() {
		return idAsiento;
	}

	public void setIdAsiento(Asiento idAsiento) {
		this.idAsiento = idAsiento;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idLibroDiario != null ? idLibroDiario.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof LibroDiario)) {
			return false;
		}
		LibroDiario other = (LibroDiario) object;
		if ((this.idLibroDiario == null && other.idLibroDiario != null)
				|| (this.idLibroDiario != null && !this.idLibroDiario
						.equals(other.idLibroDiario))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.LibroDiario[ idLibroDiario=" + idLibroDiario
				+ " ]";
	}

}
