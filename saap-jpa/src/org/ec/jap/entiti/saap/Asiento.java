/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ec.jap.entiti.saap;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "asiento")
@NamedQueries({ @NamedQuery(name = "Asiento.findAnioByPeridodFiscal", query = "SELECT MAX(a.numero) FROM Asiento a WHERE a in (SELECT ld.idAsiento FROM LibroDiario ld WHERE ld.idCuenta.anioEjercicio=:anioEjercicio)")

})
public class Asiento implements Serializable {
	private static final long serialVersionUID = 1L;
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id_asiento")
	private Integer idAsiento;

	@NotNull
	@Column(name = "fecha")
	private Date fecha;

	@NotNull
	@Column(name = "numero")
	private Integer numero;

	@OneToMany(mappedBy = "idAsiento")
	private List<LibroDiario> libroDiarioList;

	public Asiento() {
	}

	public Integer getIdAsiento() {
		return idAsiento;
	}

	public void setIdAsiento(Integer idAsiento) {
		this.idAsiento = idAsiento;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public List<LibroDiario> getLibroDiarioList() {
		return libroDiarioList;
	}

	public void setLibroDiarioList(List<LibroDiario> libroDiarioList) {
		this.libroDiarioList = libroDiarioList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idAsiento != null ? idAsiento.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Asiento)) {
			return false;
		}
		Asiento other = (Asiento) object;
		if ((this.idAsiento == null && other.idAsiento != null) || (this.idAsiento != null && !this.idAsiento.equals(other.idAsiento))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.Cuenta[ idCuenta=" + idAsiento + " ]";
	}

}
