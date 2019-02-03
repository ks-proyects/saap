/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ec.jap.entiti.saap;

import java.io.Serializable;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Freddy
 */
@Entity
@Table(name = "asistencia")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Asistencia.findByPeriodo", query = "SELECT a FROM Asistencia a WHERE a.idUsuario=:idUsuario AND a.actividad.idPeriodoPago=:idPeriodoPago AND a.asistio=false"),
		@NamedQuery(name = "Asistencia.findByFilter", query = "SELECT a FROM Asistencia a INNER JOIN a.idUsuario usu where ((case a.asistio when TRUE then '0' when FALSE then '1' else '-1' end)=:asistio OR :asistio='-1') and a.actividad=:actividad and ( UPPER(a.numeroLlave) LIKE UPPER(CONCAT('%',:filtro,'%')) OR UPPER(usu.apellidos) LIKE UPPER(CONCAT('%',:filtro,'%')) OR UPPER(usu.nombres) LIKE UPPER(CONCAT('%',:filtro,'%') )) ORDER BY UPPER(usu.nombres)"),
		@NamedQuery(name = "Asistencia.findAll", query = "SELECT a FROM Asistencia a"),
		@NamedQuery(name = "Asistencia.findByIdAsistencia", query = "SELECT a FROM Asistencia a WHERE a.idAsistencia = :idAsistencia"),
		@NamedQuery(name = "Asistencia.findByNumeroRayas", query = "SELECT a FROM Asistencia a WHERE a.numeroRayas = :numeroRayas"),
		@NamedQuery(name = "Asistencia.findByAsistio", query = "SELECT a FROM Asistencia a WHERE a.asistio = :asistio"),
		@NamedQuery(name = "Asistencia.findByObservacion", query = "SELECT a FROM Asistencia a WHERE a.observacion = :observacion"),
		@NamedQuery(name = "Asistencia.findByUsuario", query = "SELECT sum(a.numeroRayas) FROM Asistencia a WHERE a.idUsuario=:idUsuario and a.asistio=true") })
public class Asistencia implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_asistencia")
	private Integer idAsistencia;

	@Basic(optional = false)
	@NotNull
	@Column(name = "numero_rayas")
	private Double numeroRayas;
	@Basic(optional = false)
	@NotNull
	@Column(name = "asistio")
	private boolean asistio;

	@Size(max = 4000)
	@Column(name = "observacion")
	private String observacion;

	@JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
	@ManyToOne(optional = false)
	private Usuario idUsuario;

	@JoinColumn(name = "actividad", referencedColumnName = "actividad")
	@ManyToOne(optional = false)
	private Actividad actividad;

	@Column(name = "numero_llave")
	private String numeroLlave;

	@OneToMany(mappedBy = "idAsistencia")
	private List<DetallePlanilla> detallePlanillaList;

	@JoinColumn(name = "id_registro_economico", referencedColumnName = "id_registro_economico")
	@ManyToOne
	private RegistroEconomico idRegistroEconomico;

	public Asistencia() {
	}

	public Asistencia(Integer idAsistencia) {
		this.idAsistencia = idAsistencia;
	}

	public Asistencia(Integer idAsistencia, Double numeroRayas, boolean asistio) {
		this.idAsistencia = idAsistencia;
		this.numeroRayas = numeroRayas;
		this.asistio = asistio;
	}

	public Integer getIdAsistencia() {
		return idAsistencia;
	}

	public void setIdAsistencia(Integer idAsistencia) {
		this.idAsistencia = idAsistencia;
	}

	public Double getNumeroRayas() {
		return numeroRayas;
	}

	public void setNumeroRayas(Double numeroRayas) {
		this.numeroRayas = numeroRayas;
	}

	public boolean getAsistio() {
		return asistio;
	}

	public void setAsistio(boolean asistio) {
		this.asistio = asistio;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Usuario getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Usuario idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Actividad getActividad() {
		return actividad;
	}

	public void setActividad(Actividad actividad) {
		this.actividad = actividad;
	}

	public String getNumeroLlave() {
		return numeroLlave;
	}

	public void setNumeroLlave(String numeroLlave) {
		this.numeroLlave = numeroLlave;
	}

	public List<DetallePlanilla> getDetallePlanillaList() {
		return detallePlanillaList;
	}

	public void setDetallePlanillaList(List<DetallePlanilla> detallePlanillaList) {
		this.detallePlanillaList = detallePlanillaList;
	}

	public RegistroEconomico getIdRegistroEconomico() {
		return idRegistroEconomico;
	}

	public void setIdRegistroEconomico(RegistroEconomico idRegistroEconomico) {
		this.idRegistroEconomico = idRegistroEconomico;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idAsistencia != null ? idAsistencia.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Asistencia)) {
			return false;
		}
		Asistencia other = (Asistencia) object;
		if ((this.idAsistencia == null && other.idAsistencia != null) || (this.idAsistencia != null && !this.idAsistencia.equals(other.idAsistencia))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "uce.Asistencia[ idAsistencia=" + idAsistencia + " ]";
	}

}
