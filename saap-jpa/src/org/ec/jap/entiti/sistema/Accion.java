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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.ec.jap.enumerations.OutputType;

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "accion", schema = "saap")
@NamedQueries({})
public class Accion implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "id_accion")
	private Integer idAccion;
	@Size(max = 2147483647)
	@Column(name = "descripcion")
	private String descripcion;
	@Size(max = 2147483647)
	@Column(name = "imagen")
	private String imagen;
	@Size(max = 2147483647)
	@Column(name = "id_componente_interfaz")
	private String idComponenteInterfaz;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "accion_page")
	private String accionPage;

	@Column(name = "teclado_accion")
	private String tecladoAccion;

	@OneToMany(mappedBy = "idAccion")
	private List<ElementoSistema> elementoSistemaList;

	@OneToMany(mappedBy = "accion")
	private List<CambioEstado> cambioEstados;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo")
	private OutputType tipo;

	public Accion() {
	}

	public Accion(Integer idAccion) {
		this.idAccion = idAccion;
	}

	public Accion(Integer idAccion, String accionPage) {
		this.idAccion = idAccion;
		this.accionPage = accionPage;
	}

	public Integer getIdAccion() {
		return idAccion;
	}

	public void setIdAccion(Integer idAccion) {
		this.idAccion = idAccion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getIdComponenteInterfaz() {
		return idComponenteInterfaz;
	}

	public void setIdComponenteInterfaz(String idComponenteInterfaz) {
		this.idComponenteInterfaz = idComponenteInterfaz;
	}

	public String getAccionPage() {
		return accionPage;
	}

	public void setAccionPage(String accionPage) {
		this.accionPage = accionPage;
	}

	public List<ElementoSistema> getElementoSistemaList() {
		return elementoSistemaList;
	}

	public void setElementoSistemaList(List<ElementoSistema> elementoSistemaList) {
		this.elementoSistemaList = elementoSistemaList;
	}

	public String getTecladoAccion() {
		return tecladoAccion;
	}

	public void setTecladoAccion(String tecladoAccion) {
		this.tecladoAccion = tecladoAccion;
	}

	public List<CambioEstado> getCambioEstados() {
		return cambioEstados;
	}

	public void setCambioEstados(List<CambioEstado> cambioEstados) {
		this.cambioEstados = cambioEstados;
	}

	public OutputType getTipo() {
		return tipo;
	}

	public void setTipo(OutputType tipo) {
		this.tipo = tipo;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idAccion != null ? idAccion.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Accion)) {
			return false;
		}
		Accion other = (Accion) object;
		if ((this.idAccion == null && other.idAccion != null) || (this.idAccion != null && !this.idAccion.equals(other.idAccion))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.Accion[ idAccion=" + idAccion + " ]";
	}

}
