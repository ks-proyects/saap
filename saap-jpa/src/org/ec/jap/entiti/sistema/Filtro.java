/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ec.jap.entiti.sistema;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.ec.jap.entiti.saap.Usuario;

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "filtro", schema = "saap")
@NamedQueries({ @NamedQuery(name = "Filtro.findByTipoAndUser", query = "SELECT f FROM Filtro f INNER JOIN f.codigo tp INNER JOIN f.idUsuario u inner join f.idElementoSistema e WHERE tp=:tp and u=:u and e=:e") })
public class Filtro implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_filtro")
	private Integer idFiltro;
	@Column(name = "valor_numerico")
	private Double valorNumerico;
	@Column(name = "valor_entero")
	private Integer valorEntero;
	@Size(max = 2147483647)
	@Column(name = "valor_cadena")
	private String valorCadena;
	@Column(name = "valor_fecha")
	@Temporal(TemporalType.DATE)
	private Date valorFecha;

	@Column(name = "valor_boolean")
	private Boolean valorBoolean;

	@JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
	@ManyToOne(optional = false)
	private Usuario idUsuario;
	@JoinColumn(name = "codigo", referencedColumnName = "codigo")
	@ManyToOne(optional = false)
	private TipoFiltro codigo;
	@JoinColumn(name = "id_elemento_sistema", referencedColumnName = "id_elemento_sistema")
	@ManyToOne(optional = false)
	private ElementoSistema idElementoSistema;
	@JoinColumn(name = "id_comunidad", referencedColumnName = "id_comunidad")
	@ManyToOne(optional = false)
	private Comunidad idComunidad;

	public Filtro() {
	}

	public Filtro(Integer idFiltro) {
		this.idFiltro = idFiltro;
	}

	public Integer getIdFiltro() {
		return idFiltro;
	}

	public void setIdFiltro(Integer idFiltro) {
		this.idFiltro = idFiltro;
	}

	public Double getValorNumerico() {
		return valorNumerico;
	}

	public void setValorNumerico(Double valorNumerico) {
		this.valorNumerico = valorNumerico;
	}

	public Integer getValorEntero() {
		return valorEntero;
	}

	public void setValorEntero(Integer valorEntero) {
		this.valorEntero = valorEntero;
	}

	public String getValorCadena() {
		return valorCadena;
	}

	public void setValorCadena(String valorCadena) {
		this.valorCadena = valorCadena;
	}

	public Date getValorFecha() {
		return valorFecha;
	}

	public void setValorFecha(Date valorFecha) {
		this.valorFecha = valorFecha;
	}

	public Usuario getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Usuario idUsuario) {
		this.idUsuario = idUsuario;
	}

	public TipoFiltro getCodigo() {
		return codigo;
	}

	public void setCodigo(TipoFiltro codigo) {
		this.codigo = codigo;
	}

	public ElementoSistema getIdElementoSistema() {
		return idElementoSistema;
	}

	public void setIdElementoSistema(ElementoSistema idElementoSistema) {
		this.idElementoSistema = idElementoSistema;
	}

	public Comunidad getIdComunidad() {
		return idComunidad;
	}

	public void setIdComunidad(Comunidad idComunidad) {
		this.idComunidad = idComunidad;
	}

	public Boolean getValorBoolean() {
		return valorBoolean;
	}

	public void setValorBoolean(Boolean valorBoolean) {
		this.valorBoolean = valorBoolean;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idFiltro != null ? idFiltro.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Filtro)) {
			return false;
		}
		Filtro other = (Filtro) object;
		if ((this.idFiltro == null && other.idFiltro != null) || (this.idFiltro != null && !this.idFiltro.equals(other.idFiltro))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.Filtro[ idFiltro=" + idFiltro + " ]";
	}

}
