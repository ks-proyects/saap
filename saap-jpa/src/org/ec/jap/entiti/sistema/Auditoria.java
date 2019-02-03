/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ec.jap.entiti.sistema;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.ec.jap.entiti.saap.Usuario;

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "auditoria", schema = "saap")
@NamedQueries({@NamedQuery(name="Auditoria.findByFilter",query=" select au from Auditoria au where au.fecha between :fechaMin and :fechaMax AND (au.tipoEntidad.tipoEntidad=:tipoEntidad OR :tipoEntidad='0') AND (au.usuario.idUsuario=:idUsuario OR :idUsuario=0) ORDER BY  au.fecha ")})
public class Auditoria implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_auditoria")
	private Integer idAuditoria;
	@Basic(optional = false)
	@NotNull
	@Column(name = "id_entidad")
	private int idEntidad;
	@Column(name = "id_entidad1")
	private Integer idEntidad1;
	@Column(name = "id_entidad2")
	private Integer idEntidad2;

	@Size(max = 2147483647)
	@Column(name = "descripcion")
	private String descripcion;

	@Size(max = 2147483647)
	@Column(name = "descripcion1")
	private String descripcion1;
	@Size(max = 2147483647)
	@Column(name = "descripcion2")
	private String descripcion2;
	@Size(max = 8000)
	@Column(name = "cambios_atributos")
	private String cambiosAtributos;
	@Basic(optional = false)
	@NotNull
	@Column(name = "fecha")
	@Temporal(TemporalType.DATE)
	private Date fecha;

	@JoinColumn(name = "tipo_entidad", referencedColumnName = "tipo_entidad")
	@ManyToOne(optional = false)
	private TipoEntidad tipoEntidad;

	@JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
	@ManyToOne(optional = false)
	private Usuario usuario;

	public Auditoria() {
	}

	public Auditoria(Integer idAuditoria) {
		this.idAuditoria = idAuditoria;
	}

	public Auditoria(Integer idAuditoria, int idEntidad, Date fecha) {
		this.idAuditoria = idAuditoria;
		this.idEntidad = idEntidad;
		this.fecha = fecha;
	}

	public Integer getIdAuditoria() {
		return idAuditoria;
	}

	public void setIdAuditoria(Integer idAuditoria) {
		this.idAuditoria = idAuditoria;
	}

	public int getIdEntidad() {
		return idEntidad;
	}

	public void setIdEntidad(int idEntidad) {
		this.idEntidad = idEntidad;
	}

	public Integer getIdEntidad1() {
		return idEntidad1;
	}

	public void setIdEntidad1(Integer idEntidad1) {
		this.idEntidad1 = idEntidad1;
	}

	public Integer getIdEntidad2() {
		return idEntidad2;
	}

	public void setIdEntidad2(Integer idEntidad2) {
		this.idEntidad2 = idEntidad2;
	}

	public String getDescripcion1() {
		return descripcion1;
	}

	public void setDescripcion1(String descripcion1) {
		this.descripcion1 = descripcion1;
	}

	public String getDescripcion2() {
		return descripcion2;
	}

	public void setDescripcion2(String descripcion2) {
		this.descripcion2 = descripcion2;
	}

	public String getCambiosAtributos() {
		return cambiosAtributos;
	}

	public void setCambiosAtributos(String cambiosAtributos) {
		this.cambiosAtributos = cambiosAtributos;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public TipoEntidad getTipoEntidad() {
		return tipoEntidad;
	}

	public void setTipoEntidad(TipoEntidad tipoEntidad) {
		this.tipoEntidad = tipoEntidad;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idAuditoria != null ? idAuditoria.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Auditoria)) {
			return false;
		}
		Auditoria other = (Auditoria) object;
		if ((this.idAuditoria == null && other.idAuditoria != null)
				|| (this.idAuditoria != null && !this.idAuditoria
						.equals(other.idAuditoria))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.Auditoria[ idAuditoria=" + idAuditoria + " ]";
	}

}
