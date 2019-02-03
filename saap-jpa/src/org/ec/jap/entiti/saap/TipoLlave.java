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
import javax.persistence.Id;
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
@Table(name = "tipo_llave")
@NamedQueries({ @NamedQuery(name = "TipoLlave.findParametros", query = "select count(par) from Parametro par where par.tipoLlave=:tipoLlave"),
		@NamedQuery(name = "TipoLlave.findTarifas", query = "select count(tar) from Tarifa tar where tar.tipoLlave=:tipoLlave") })
public class TipoLlave implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Size(min = 1, max = 2147483647, message = "El campo CÓDIGO es obligatorio.")
	@Column(name = "tipo_llave")
	private String tipoLlave;

	@Basic(optional = false)
	@Size(min = 1, max = 2147483647, message = "El campo DESCRIPCIÓN es obligatorio.")
	@Column(name = "descripcion")
	private String descripcion;
	@OneToMany(mappedBy = "tipoLlave")
	private List<Parametro> parametroList;

	@OneToMany(mappedBy = "tipoLlave")
	private List<Tarifa> tarifaList;

	public TipoLlave() {
	}

	public TipoLlave(String tipoLlave) {
		this.tipoLlave = tipoLlave;
	}

	public TipoLlave(String tipoLlave, String descripcion) {
		this.tipoLlave = tipoLlave;
		this.descripcion = descripcion;
	}

	public String getTipoLlave() {
		return tipoLlave;
	}

	public void setTipoLlave(String tipoLlave) {
		this.tipoLlave = tipoLlave;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<Parametro> getParametroList() {
		return parametroList;
	}

	public void setParametroList(List<Parametro> parametroList) {
		this.parametroList = parametroList;
	}

	public List<Tarifa> getTarifaList() {
		return tarifaList;
	}

	public void setTarifaList(List<Tarifa> tarifaList) {
		this.tarifaList = tarifaList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (tipoLlave != null ? tipoLlave.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof TipoLlave)) {
			return false;
		}
		TipoLlave other = (TipoLlave) object;
		if ((this.tipoLlave == null && other.tipoLlave != null) || (this.tipoLlave != null && !this.tipoLlave.equals(other.tipoLlave))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.TipoLlave[ tipoLlave=" + tipoLlave + " ]";
	}

}
