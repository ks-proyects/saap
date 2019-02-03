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
@Table(name = "documento_adjunto")
@NamedQueries({ @NamedQuery(name = "DocumentoAdjunto.deleteByIdentidad", query = " DELETE FROM DocumentoAdjunto da WHERE da.idDocumento=:idDocumento") })
public class DocumentoAdjunto implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "id_documento_adjunto")
	private Integer idDocumentoAdjunto;
	@Basic(optional = false)
	@NotNull
	@Column(name = "id_documento")
	private int idDocumento;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "nombre_archivo")
	private String nombreArchivo;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "url")
	private String url;
	@Column(name = "fecha_registro")
	@Temporal(TemporalType.DATE)
	private Date fechaRegistro;
	@JoinColumn(name = "tipo_entidad", referencedColumnName = "tipo_entidad")
	@ManyToOne(optional = false)
	private TipoEntidad tipoEntidad;

	public DocumentoAdjunto() {
	}

	public DocumentoAdjunto(Integer idDocumentoAdjunto) {
		this.idDocumentoAdjunto = idDocumentoAdjunto;
	}

	public DocumentoAdjunto(Integer idDocumentoAdjunto, int idDocumento, String nombreArchivo, String url) {
		this.idDocumentoAdjunto = idDocumentoAdjunto;
		this.idDocumento = idDocumento;
		this.nombreArchivo = nombreArchivo;
		this.url = url;
	}

	public Integer getIdDocumentoAdjunto() {
		return idDocumentoAdjunto;
	}

	public void setIdDocumentoAdjunto(Integer idDocumentoAdjunto) {
		this.idDocumentoAdjunto = idDocumentoAdjunto;
	}

	public int getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(int idDocumento) {
		this.idDocumento = idDocumento;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public TipoEntidad getTipoEntidad() {
		return tipoEntidad;
	}

	public void setTipoEntidad(TipoEntidad tipoEntidad) {
		this.tipoEntidad = tipoEntidad;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idDocumentoAdjunto != null ? idDocumentoAdjunto.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof DocumentoAdjunto)) {
			return false;
		}
		DocumentoAdjunto other = (DocumentoAdjunto) object;
		if ((this.idDocumentoAdjunto == null && other.idDocumentoAdjunto != null) || (this.idDocumentoAdjunto != null && !this.idDocumentoAdjunto.equals(other.idDocumentoAdjunto))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.DocumentoAdjunto[ idDocumentoAdjunto=" + idDocumentoAdjunto + " ]";
	}

}
