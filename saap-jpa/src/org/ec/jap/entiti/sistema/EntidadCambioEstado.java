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

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "entidad_cambio_estado")
@NamedQueries({
		@NamedQuery(name = "EntidadCambioEstado.findByTipoIdDocumento", query = "SELECT e FROM EntidadCambioEstado e WHERE e.idDocumento = :idDocumento and e.tipoEntidad.tipoEntidad=:tipoEntidad ORDER BY e.fecha ASC"),
		@NamedQuery(name = "EntidadCambioEstado.deleteByIdentidad", query = "DELETE FROM EntidadCambioEstado e WHERE e.idDocumento = :idDocumento") })
public class EntidadCambioEstado implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_entidad_cambio_estado")
	private Integer idEntidadCambioEstado;
	@Basic(optional = false)
	@NotNull
	@Column(name = "id_documento")
	private int idDocumento;
	@Basic(optional = false)
	@NotNull
	@Column(name = "fecha")
	@Temporal(TemporalType.DATE)
	private Date fecha;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "accion")
	private String accion;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "estado_resultante")
	private String estadoResultante;
	@Size(max = 2147483647)
	@Column(name = "observacion")
	private String observacion;
	@JoinColumn(name = "tipo_entidad", referencedColumnName = "tipo_entidad")
	@ManyToOne(optional = false)
	private TipoEntidad tipoEntidad;
	@JoinColumn(name = "id_cambio_estado", referencedColumnName = "id_cambio_estado")
	@ManyToOne(optional = false)
	private CambioEstado idCambioEstado;

	private String motivo;

	public EntidadCambioEstado() {
	}

	public EntidadCambioEstado(Integer idEntidadCambioEstado) {
		this.idEntidadCambioEstado = idEntidadCambioEstado;
	}

	public EntidadCambioEstado(Integer idEntidadCambioEstado, int idDocumento, Date fecha, String accion, String estadoResultante) {
		this.idEntidadCambioEstado = idEntidadCambioEstado;
		this.idDocumento = idDocumento;
		this.fecha = fecha;
		this.accion = accion;
		this.estadoResultante = estadoResultante;
	}

	public Integer getIdEntidadCambioEstado() {
		return idEntidadCambioEstado;
	}

	public void setIdEntidadCambioEstado(Integer idEntidadCambioEstado) {
		this.idEntidadCambioEstado = idEntidadCambioEstado;
	}

	public int getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(int idDocumento) {
		this.idDocumento = idDocumento;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getEstadoResultante() {
		return estadoResultante;
	}

	public void setEstadoResultante(String estadoResultante) {
		this.estadoResultante = estadoResultante;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public TipoEntidad getTipoEntidad() {
		return tipoEntidad;
	}

	public void setTipoEntidad(TipoEntidad tipoEntidad) {
		this.tipoEntidad = tipoEntidad;
	}

	public CambioEstado getIdCambioEstado() {
		return idCambioEstado;
	}

	public void setIdCambioEstado(CambioEstado idCambioEstado) {
		this.idCambioEstado = idCambioEstado;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idEntidadCambioEstado != null ? idEntidadCambioEstado.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof EntidadCambioEstado)) {
			return false;
		}
		EntidadCambioEstado other = (EntidadCambioEstado) object;
		if ((this.idEntidadCambioEstado == null && other.idEntidadCambioEstado != null) || (this.idEntidadCambioEstado != null && !this.idEntidadCambioEstado.equals(other.idEntidadCambioEstado))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.EntidadCambioEstado[ idEntidadCambioEstado=" + idEntidadCambioEstado + " ]";
	}

}
