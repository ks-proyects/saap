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

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "controlador", schema = "saap")
@NamedQueries({
		 })
public class Controlador implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "class_controlador")
	private String classControlador;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "descripcion_controlador")
	private String descripcionControlador;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "aplicacion")
	private String aplicacion;
	@OneToMany(mappedBy = "classControlador")
	private List<ElementoSistema> elementoSistemaList;

	public Controlador() {
	}

	public Controlador(String classControlador) {
		this.classControlador = classControlador;
	}

	public Controlador(String classControlador, String descripcionControlador,
			String aplicacion) {
		this.classControlador = classControlador;
		this.descripcionControlador = descripcionControlador;
		this.aplicacion = aplicacion;
	}

	public String getClassControlador() {
		return classControlador;
	}

	public void setClassControlador(String classControlador) {
		this.classControlador = classControlador;
	}

	public String getDescripcionControlador() {
		return descripcionControlador;
	}

	public void setDescripcionControlador(String descripcionControlador) {
		this.descripcionControlador = descripcionControlador;
	}

	public String getAplicacion() {
		return aplicacion;
	}

	public void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
	}

	public List<ElementoSistema> getElementoSistemaList() {
		return elementoSistemaList;
	}

	public void setElementoSistemaList(List<ElementoSistema> elementoSistemaList) {
		this.elementoSistemaList = elementoSistemaList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (classControlador != null ? classControlador.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Controlador)) {
			return false;
		}
		Controlador other = (Controlador) object;
		if ((this.classControlador == null && other.classControlador != null)
				|| (this.classControlador != null && !this.classControlador
						.equals(other.classControlador))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.Controlador[ classControlador="
				+ classControlador + " ]";
	}

}
