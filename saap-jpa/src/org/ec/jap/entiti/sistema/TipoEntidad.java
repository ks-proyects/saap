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
@Table(name = "tipo_entidad")
@NamedQueries({})
public class TipoEntidad implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "tipo_entidad")
	private String tipoEntidad;
	@Size(max = 2147483647)
	@Column(name = "sentencia_select")
	private String sentenciaSelect;
	@Size(max = 2147483647)
	@Column(name = "sentencia_update")
	private String sentenciaUpdate;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "grupo")
	private String grupo;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "es_activo")
	private String esActivo;

	private String descripcion;

	@OneToMany(mappedBy = "tipoEntidad")
	private List<EstadoEntidad> estadoEntidadList;
	@OneToMany(mappedBy = "tipoEntidad")
	private List<DocumentoAdjunto> documentoAdjuntoList;
	@OneToMany(mappedBy = "tipoEntidad")
	private List<Auditoria> auditoriaList;
	@OneToMany(mappedBy = "tipoEntidad")
	private List<EntidadCambioEstado> entidadCambioEstadoList;
	@OneToMany(mappedBy = "tipoEntidad")
	private List<CambioEstado> cambioEstadoList;

	@Size(max = 2147483647)
	@Column(name = "sentencia_delete")
	private String sentenciaDelete;

	public TipoEntidad() {
	}

	public TipoEntidad(String tipoEntidad) {
		this.tipoEntidad = tipoEntidad;
	}

	public TipoEntidad(String tipoEntidad, String grupo) {
		this.tipoEntidad = tipoEntidad;
		this.grupo = grupo;
	}

	public String getTipoEntidad() {
		return tipoEntidad;
	}

	public void setTipoEntidad(String tipoEntidad) {
		this.tipoEntidad = tipoEntidad;
	}

	public String getSentenciaSelect() {
		return sentenciaSelect;
	}

	public void setSentenciaSelect(String sentenciaSelect) {
		this.sentenciaSelect = sentenciaSelect;
	}

	public String getSentenciaUpdate() {
		return sentenciaUpdate;
	}

	public void setSentenciaUpdate(String sentenciaUpdate) {
		this.sentenciaUpdate = sentenciaUpdate;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public List<EstadoEntidad> getEstadoEntidadList() {
		return estadoEntidadList;
	}

	public void setEstadoEntidadList(List<EstadoEntidad> estadoEntidadList) {
		this.estadoEntidadList = estadoEntidadList;
	}

	public List<DocumentoAdjunto> getDocumentoAdjuntoList() {
		return documentoAdjuntoList;
	}

	public void setDocumentoAdjuntoList(List<DocumentoAdjunto> documentoAdjuntoList) {
		this.documentoAdjuntoList = documentoAdjuntoList;
	}

	public List<Auditoria> getAuditoriaList() {
		return auditoriaList;
	}

	public void setAuditoriaList(List<Auditoria> auditoriaList) {
		this.auditoriaList = auditoriaList;
	}

	public List<EntidadCambioEstado> getEntidadCambioEstadoList() {
		return entidadCambioEstadoList;
	}

	public void setEntidadCambioEstadoList(List<EntidadCambioEstado> entidadCambioEstadoList) {
		this.entidadCambioEstadoList = entidadCambioEstadoList;
	}

	public List<CambioEstado> getCambioEstadoList() {
		return cambioEstadoList;
	}

	public void setCambioEstadoList(List<CambioEstado> cambioEstadoList) {
		this.cambioEstadoList = cambioEstadoList;
	}

	public String getEsActivo() {
		return esActivo;
	}

	public void setEsActivo(String esActivo) {
		this.esActivo = esActivo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getSentenciaDelete() {
		return sentenciaDelete;
	}

	public void setSentenciaDelete(String sentenciaDelete) {
		this.sentenciaDelete = sentenciaDelete;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (tipoEntidad != null ? tipoEntidad.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof TipoEntidad)) {
			return false;
		}
		TipoEntidad other = (TipoEntidad) object;
		if ((this.tipoEntidad == null && other.tipoEntidad != null) || (this.tipoEntidad != null && !this.tipoEntidad.equals(other.tipoEntidad))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.TipoEntidad[ tipoEntidad=" + tipoEntidad + " ]";
	}

}
