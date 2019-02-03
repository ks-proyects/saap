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
@Table(name = "elemento_sistema", schema = "saap")
@NamedQueries({
		@NamedQuery(name = "ElementoSistema.findByUser", query = "SELECT e FROM ElementoSistema e WHERE e.idElementoSistemaPadre=:idElementoSistemaPadre AND e.tipoElemento=:tipoElemento AND e.idElementoSistema IN(SELECT p.idElementoSistema.idElementoSistema FROM PerfilElementoSistema p WHERE p.idPerfil.idPerfil IN (SELECT per.idPerfil FROM Perfil per WHERE per IN (SELECT up.idPerfil FROM UsuarioPerfil up WHERE up.idUsuario.idUsuario=:idUsuario AND up.activo=:activoUP AND up.idUsuario.estado IN (:estadoU,'EDI')) AND per.activo=:activoPerf) AND p.acctivo=:acctivoPES) AND e.visible=:visibleES"),
		@NamedQuery(name = "ElementoSistema.findByUserAndAction", query = "SELECT e FROM ElementoSistema e WHERE ( e.outcome=:outcome OR e.outcome='INS' ) AND e.idElementoSistemaPadre=:idElementoSistemaPadre AND e.tipoElemento=:tipoElemento AND e.idElementoSistema IN(SELECT p.idElementoSistema.idElementoSistema FROM PerfilElementoSistema p WHERE p.idPerfil.idPerfil IN (SELECT per.idPerfil FROM Perfil per WHERE per IN (SELECT up.idPerfil FROM UsuarioPerfil up WHERE up.idUsuario.idUsuario=:idUsuario AND up.activo=:activoUP AND up.idUsuario.estado in (:estadoU,'EDI')) AND per.activo=:activoPerf) AND p.acctivo=:acctivoPES) AND e.visible=:visibleES"),
		@NamedQuery(name = "ElementoSistema.findByController", query = "SELECT e FROM ElementoSistema e inner join e.classControlador cc WHERE cc.classControlador=:classControlador") })
public class ElementoSistema implements Serializable, Comparable<ElementoSistema> {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "id_elemento_sistema")
	private Integer idElementoSistema;
	@Column(name = "id_elemento_sistema_padre")
	private Integer idElementoSistemaPadre;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "tipo_elemento")
	private String tipoElemento;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "nombre")
	private String nombre;
	@Basic(optional = false)
	@NotNull
	@Column(name = "orden")
	private Integer orden2;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "visible")
	private String visible;
	@Size(max = 2147483647)
	@Column(name = "imagen_logo")
	private String imagenLogo;
	@Size(max = 2147483647)
	@Column(name = "url")
	private String url;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "outcome")
	private String outcome;
	@JoinColumn(name = "class_controlador", referencedColumnName = "class_controlador")
	@ManyToOne
	private Controlador classControlador;
	@JoinColumn(name = "id_accion", referencedColumnName = "id_accion")
	@ManyToOne
	private Accion idAccion;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idElementoSistema")
	private List<Filtro> filtroList;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idElementoSistema")
	private List<PerfilElementoSistema> perfilElementoSistemaList;

	public ElementoSistema() {
	}

	public ElementoSistema(Integer idElementoSistema) {
		this.idElementoSistema = idElementoSistema;
	}

	public ElementoSistema(Integer idElementoSistema, String tipoElemento, String nombre, Integer orden2, String visible, String outcome) {
		this.idElementoSistema = idElementoSistema;
		this.tipoElemento = tipoElemento;
		this.nombre = nombre;
		this.orden2 = orden2;
		this.visible = visible;
		this.outcome = outcome;
	}

	public Integer getIdElementoSistema() {
		return idElementoSistema;
	}

	public void setIdElementoSistema(Integer idElementoSistema) {
		this.idElementoSistema = idElementoSistema;
	}

	public Integer getIdElementoSistemaPadre() {
		return idElementoSistemaPadre;
	}

	public void setIdElementoSistemaPadre(Integer idElementoSistemaPadre) {
		this.idElementoSistemaPadre = idElementoSistemaPadre;
	}

	public String getTipoElemento() {
		return tipoElemento;
	}

	public void setTipoElemento(String tipoElemento) {
		this.tipoElemento = tipoElemento;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getOrden2() {
		return orden2;
	}

	public void setOrden2(Integer orden2) {
		this.orden2 = orden2;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public String getImagenLogo() {
		return imagenLogo;
	}

	public void setImagenLogo(String imagenLogo) {
		this.imagenLogo = imagenLogo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public Controlador getClassControlador() {
		return classControlador;
	}

	public void setClassControlador(Controlador classControlador) {
		this.classControlador = classControlador;
	}

	public Accion getIdAccion() {
		return idAccion;
	}

	public void setIdAccion(Accion idAccion) {
		this.idAccion = idAccion;
	}

	public List<Filtro> getFiltroList() {
		return filtroList;
	}

	public void setFiltroList(List<Filtro> filtroList) {
		this.filtroList = filtroList;
	}

	public List<PerfilElementoSistema> getPerfilElementoSistemaList() {
		return perfilElementoSistemaList;
	}

	public void setPerfilElementoSistemaList(List<PerfilElementoSistema> perfilElementoSistemaList) {
		this.perfilElementoSistemaList = perfilElementoSistemaList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idElementoSistema != null ? idElementoSistema.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof ElementoSistema)) {
			return false;
		}
		ElementoSistema other = (ElementoSistema) object;
		if ((this.idElementoSistema == null && other.idElementoSistema != null) || (this.idElementoSistema != null && !this.idElementoSistema.equals(other.idElementoSistema))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.ElementoSistema[ idElementoSistema=" + idElementoSistema + " ]";
	}

	@Override
	public int compareTo(ElementoSistema o) {
		// TODO Auto-generated method stub
		return orden2.compareTo(o.orden2);
	}

}
