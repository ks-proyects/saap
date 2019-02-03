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
@Table(name = "cambio_estado")
@NamedQueries({
		@NamedQuery(name = "CambioEstado.findByEstado", query = "SELECT c FROM CambioEstado c WHERE c.permitido='S' and c.idEstadoAnterior=:idEstadoAnterior and c.tipoEntidad.tipoEntidad=:tipoEntidad and c.idElementoSistemaFk=:idElementoSistemaFk and c.idAccionNegocioFk in (SELECT ra.idAccionNegocio.idAccionNegocio FROM UsuarioRol u inner join u.idRol rol inner join rol.rolAccionNegocioList ra where rol.activo=:activoRol  AND u.idUsuario=:idUsuario)"),
		@NamedQuery(name = "CambioEstado.findNivelBolqueo", query = "SELECT MAX(c.nivelBloqueo) FROM CambioEstado c WHERE c.permitido='S' and c.idEstadoAnterior=:idEstadoAnterior and c.tipoEntidad.tipoEntidad=:tipoEntidad and c.idElementoSistemaFk=:idElementoSistemaFk and c.idAccionNegocioFk in (SELECT ra.idAccionNegocio.idAccionNegocio FROM UsuarioRol u inner join u.idRol rol inner join rol.rolAccionNegocioList ra where rol.activo=:activoRol  AND u.idUsuario=:idUsuario)") })
public class CambioEstado implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@Column(name = "id_cambio_estado")
	private Integer idCambioEstado;

	private String descripcion;

	@Basic(optional = false)
	@Column(name = "id_elemento_sistema_fk")
	private int idElementoSistemaFk;

	@Column(name = "id_accion_negocio_fk")
	private Integer idAccionNegocioFk;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "permitido")
	private String permitido;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "aplica_condicion")
	private String aplicaCondicion;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "aplica_condicion_inicial")
	private String aplicaCondicionInicial;

	@Size(max = 2147483647)
	@Column(name = "aplica_motivo")
	private String aplicaMotivo;
	@Size(max = 2147483647)
	@Column(name = "imagen_accion")
	private String imagenAccion;
	@Basic(optional = false)
	@NotNull
	@Column(name = "nivel_bloqueo")
	private int nivelBloqueo;
	@OneToMany(mappedBy = "idCambioEstado")
	private List<CambioEstadoCondicion> cambioEstadoCondicionList;
	@OneToMany(mappedBy = "idCambioEstado")
	private List<EntidadCambioEstado> entidadCambioEstadoList;
	@JoinColumn(name = "tipo_entidad", referencedColumnName = "tipo_entidad")
	@ManyToOne(optional = false)
	private TipoEntidad tipoEntidad;

	@JoinColumn(name = "estado_nuevo", referencedColumnName = "id_estado_entidad")
	@ManyToOne(optional = false)
	private EstadoEntidad idEstadoNuevo;

	@JoinColumn(name = "estado_anterior", referencedColumnName = "id_estado_entidad")
	@ManyToOne(optional = false)
	private EstadoEntidad idEstadoAnterior;

	@ManyToOne
	@JoinColumn(name = "id_accion")
	private Accion accion;

	public CambioEstado() {
	}

	public CambioEstado(Integer idCambioEstado) {
		this.idCambioEstado = idCambioEstado;
	}

	public CambioEstado(Integer idCambioEstado, int idElementoSistemaFk, String permitido, String aplicaCondicion, int nivelBloqueo) {
		this.idCambioEstado = idCambioEstado;
		this.idElementoSistemaFk = idElementoSistemaFk;
		this.permitido = permitido;
		this.aplicaCondicion = aplicaCondicion;
		this.nivelBloqueo = nivelBloqueo;
	}

	public Integer getIdCambioEstado() {
		return idCambioEstado;
	}

	public void setIdCambioEstado(Integer idCambioEstado) {
		this.idCambioEstado = idCambioEstado;
	}

	public int getIdElementoSistemaFk() {
		return idElementoSistemaFk;
	}

	public void setIdElementoSistemaFk(int idElementoSistemaFk) {
		this.idElementoSistemaFk = idElementoSistemaFk;
	}

	public Accion getAccion() {
		return accion;
	}

	public void setAccion(Accion accion) {
		this.accion = accion;
	}

	public Integer getIdAccionNegocioFk() {
		return idAccionNegocioFk;
	}

	public void setIdAccionNegocioFk(Integer idAccionNegocioFk) {
		this.idAccionNegocioFk = idAccionNegocioFk;
	}

	public String getPermitido() {
		return permitido;
	}

	public void setPermitido(String permitido) {
		this.permitido = permitido;
	}

	public String getAplicaCondicion() {
		return aplicaCondicion;
	}

	public void setAplicaCondicion(String aplicaCondicion) {
		this.aplicaCondicion = aplicaCondicion;
	}

	public String getAplicaMotivo() {
		return aplicaMotivo;
	}

	public void setAplicaMotivo(String aplicaMotivo) {
		this.aplicaMotivo = aplicaMotivo;
	}

	public String getImagenAccion() {
		return imagenAccion;
	}

	public void setImagenAccion(String imagenAccion) {
		this.imagenAccion = imagenAccion;
	}

	public int getNivelBloqueo() {
		return nivelBloqueo;
	}

	public void setNivelBloqueo(int nivelBloqueo) {
		this.nivelBloqueo = nivelBloqueo;
	}

	public List<CambioEstadoCondicion> getCambioEstadoCondicionList() {
		return cambioEstadoCondicionList;
	}

	public void setCambioEstadoCondicionList(List<CambioEstadoCondicion> cambioEstadoCondicionList) {
		this.cambioEstadoCondicionList = cambioEstadoCondicionList;
	}

	public List<EntidadCambioEstado> getEntidadCambioEstadoList() {
		return entidadCambioEstadoList;
	}

	public void setEntidadCambioEstadoList(List<EntidadCambioEstado> entidadCambioEstadoList) {
		this.entidadCambioEstadoList = entidadCambioEstadoList;
	}

	public TipoEntidad getTipoEntidad() {
		return tipoEntidad;
	}

	public void setTipoEntidad(TipoEntidad tipoEntidad) {
		this.tipoEntidad = tipoEntidad;
	}

	public EstadoEntidad getIdEstadoNuevo() {
		return idEstadoNuevo;
	}

	public void setIdEstadoNuevo(EstadoEntidad idEstadoNuevo) {
		this.idEstadoNuevo = idEstadoNuevo;
	}

	public EstadoEntidad getIdEstadoAnterior() {
		return idEstadoAnterior;
	}

	public void setIdEstadoAnterior(EstadoEntidad idEstadoAnterior) {
		this.idEstadoAnterior = idEstadoAnterior;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getAplicaCondicionInicial() {
		return aplicaCondicionInicial;
	}

	public void setAplicaCondicionInicial(String aplicaCondicionInicial) {
		this.aplicaCondicionInicial = aplicaCondicionInicial;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idCambioEstado != null ? idCambioEstado.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof CambioEstado)) {
			return false;
		}
		CambioEstado other = (CambioEstado) object;
		if ((this.idCambioEstado == null && other.idCambioEstado != null) || (this.idCambioEstado != null && !this.idCambioEstado.equals(other.idCambioEstado))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.CambioEstado[ idCambioEstado=" + idCambioEstado + " ]";
	}

}
