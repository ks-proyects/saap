/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ec.jap.entiti.sistema;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.ec.jap.entiti.saap.Parametro;
import org.ec.jap.entiti.saap.Usuario;

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "comunidad", schema = "saap")
@NamedQueries({})
public class Comunidad implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "id_comunidad")
	private Integer idComunidad;
	@Size(max = 2147483647)
	@Column(name = "nombre")
	private String nombre;

	private Integer frecuencia;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idComunidad")
	private List<Usuario> usuarioList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idComunidad")
	private List<Filtro> filtroList;
	@OneToMany(mappedBy = "idComunidad")
	private List<Parametro> parametroList;

	public Comunidad() {
	}

	public Comunidad(Integer idComunidad) {
		this.idComunidad = idComunidad;
	}

	public Integer getIdComunidad() {
		return idComunidad;
	}

	public void setIdComunidad(Integer idComunidad) {
		this.idComunidad = idComunidad;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Usuario> getUsuarioList() {
		return usuarioList;
	}

	public void setUsuarioList(List<Usuario> usuarioList) {
		this.usuarioList = usuarioList;
	}

	public List<Filtro> getFiltroList() {
		return filtroList;
	}

	public void setFiltroList(List<Filtro> filtroList) {
		this.filtroList = filtroList;
	}

	public List<Parametro> getParametroList() {
		return parametroList;
	}

	public void setParametroList(List<Parametro> parametroList) {
		this.parametroList = parametroList;
	}

	public Integer getFrecuencia() {
		return frecuencia;
	}

	public void setFrecuencia(Integer frecuencia) {
		this.frecuencia = frecuencia;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idComunidad != null ? idComunidad.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Comunidad)) {
			return false;
		}
		Comunidad other = (Comunidad) object;
		if ((this.idComunidad == null && other.idComunidad != null)
				|| (this.idComunidad != null && !this.idComunidad
						.equals(other.idComunidad))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.Comunidad[ idComunidad=" + idComunidad + " ]";
	}

}
