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
@Table(name = "cuenta")
@NamedQueries({ @NamedQuery(name = "Cuenta.findByAnioAndNumCuenta", query = "SELECT c FROM Cuenta c WHERE c.anioEjercicio=:anioEjercicio AND c.numCuenta=:numCuenta") })
public class Cuenta implements Serializable {
	private static final long serialVersionUID = 1L;
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id_cuenta")
	private Integer idCuenta;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "descripcion")
	private String descripcion;

	@NotNull
	@Column(name = "num_cuenta")
	private String numCuenta;
	@Basic(optional = false)
	@NotNull
	@Column(name = "anio_ejercicio")
	private Integer anioEjercicio;
	@Basic(optional = false)
	@NotNull
	@Column(name = "debe")
	private Double debe;
	@Basic(optional = false)
	@NotNull
	@Column(name = "haber")
	private Double haber;
	@Column(name = "saldo")
	private Double saldo;
	@OneToMany(mappedBy = "idCuenta")
	private List<LibroDiario> libroDiarioList;

	public Cuenta() {
	}

	public Cuenta(Integer idCuenta) {
		this.idCuenta = idCuenta;
	}

	public Integer getIdCuenta() {
		return idCuenta;
	}

	public void setIdCuenta(Integer idCuenta) {
		this.idCuenta = idCuenta;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getAnioEjercicio() {
		return anioEjercicio;
	}

	public void setAnioEjercicio(int anioEjercicio) {
		this.anioEjercicio = anioEjercicio;
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

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	public void setAnioEjercicio(Integer anioEjercicio) {
		this.anioEjercicio = anioEjercicio;
	}

	public List<LibroDiario> getLibroDiarioList() {
		return libroDiarioList;
	}

	public void setLibroDiarioList(List<LibroDiario> libroDiarioList) {
		this.libroDiarioList = libroDiarioList;
	}

	public String getNumCuenta() {
		return numCuenta;
	}

	public void setNumCuenta(String numCuenta) {
		this.numCuenta = numCuenta;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idCuenta != null ? idCuenta.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Cuenta)) {
			return false;
		}
		Cuenta other = (Cuenta) object;
		if ((this.idCuenta == null && other.idCuenta != null) || (this.idCuenta != null && !this.idCuenta.equals(other.idCuenta))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.Cuenta[ idCuenta=" + idCuenta + " ]";
	}

}
