/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ec.jap.entiti.saap;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.ec.jap.anotaciones.AuditoriaAnot;
import org.ec.jap.anotaciones.AuditoriaMethod;
import org.ec.jap.enumerations.EpocaEnum;

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "periodo_pago")
@NamedQueries({
		@NamedQuery(name = "PeriodoPago.findById", query = "SELECT new PeriodoPago(p.idPeriodoPago,p.descripcion,p.fechaInicio,p.fechaFin, p.estado,p.epoca) FROM PeriodoPago p WHERE p.idPeriodoPago=:idPeriodoPago"),
		@NamedQuery(name = "PeriodoPago.findByFechas", query = "SELECT p FROM PeriodoPago p WHERE p.fechaInicio=:fechaInicio and p.fechaFin=:fechaFin ORDER BY p.idPeriodoPago DESC "),
		@NamedQuery(name = "PeriodoPago.findByAnio", query = "SELECT new PeriodoPago(p.idPeriodoPago,p.descripcion,p.fechaInicio,p.fechaFin, p.estado) FROM PeriodoPago p WHERE  UPPER(p.descripcion) like UPPER(CONCAT('%',:filtro,'%')) ORDER BY p.fechaFin DESC"),
		@NamedQuery(name = "PeriodoPago.findMaxMes", query = "SELECT MAX(p.fechaFin) FROM PeriodoPago p"),
		@NamedQuery(name = "PeriodoPago.findAll", query = "SELECT p FROM PeriodoPago p ORDER BY p.fechaFin DESC"),
		@NamedQuery(name = "PeriodoPago.findAbiertoObj", query = "SELECT new PeriodoPago(p.idPeriodoPago,p.descripcion,p.estado,p.epoca) FROM PeriodoPago p WHERE p.estado in ('ABIE','CERR')"),
		@NamedQuery(name = "PeriodoPago.findAbierto", query = "SELECT p FROM PeriodoPago p WHERE p.estado in ('ABIE','CERR')"),
		@NamedQuery(name = "PeriodoPago.findAbiertoCerrado", query = "SELECT p FROM PeriodoPago p WHERE p.estado  in ('ABIE','CERR')") })
@AuditoriaAnot(entityType = "PERPAG")
public class PeriodoPago implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_periodo_pago")
	private Integer idPeriodoPago;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 1024)
	@Column(name = "descripcion")
	private String descripcion;
	@Basic(optional = false)
	@NotNull
	@Column(name = "fecha_inicio")
	@Temporal(TemporalType.DATE)
	private Date fechaInicio;
	@Basic(optional = false)
	@NotNull
	@Column(name = "fecha_fin")
	@Temporal(TemporalType.DATE)
	private Date fechaFin;
	@Basic(optional = false)
	@NotNull
	@Column(name = "anio")
	private Integer anio;
	@Basic(optional = false)
	@NotNull
	@Column(name = "mes")
	private Integer mes;
	@Size(max = 16)
	@Column(name = "pp_estado")
	private String estado;

	@Enumerated(EnumType.STRING)
	private EpocaEnum epoca;

	@OneToMany(mappedBy = "idPeriodoPago", fetch = FetchType.EAGER)
	private List<CabeceraPlanilla> cabeceraPlanillaList;

	@OneToMany(mappedBy = "idPeriodoPago")
	private List<RegistroEconomico> registroEconomicoList;

	@OneToMany(mappedBy = "idPeriodoPago")
	private List<Lectura> lecturasList;

	@OneToMany(mappedBy = "idPeriodoPago")
	private List<Actividad> actividadList;

	@OneToMany(mappedBy = "idPeriodoPago")
	private List<Gasto> gastoList;

	public PeriodoPago() {
	}

	public PeriodoPago(Integer idPeriodoPago) {
		this.idPeriodoPago = idPeriodoPago;
	}

	public PeriodoPago(Integer idPeriodoPago, String descripcion, Date fechaInicio, Date fechaFin, String estado) {
		super();
		this.idPeriodoPago = idPeriodoPago;
		this.descripcion = descripcion;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.estado = estado;
	}

	public PeriodoPago(Integer idPeriodoPago, String descripcion, Date fechaInicio, Date fechaFin, String estado,
			EpocaEnum epoca) {
		super();
		this.idPeriodoPago = idPeriodoPago;
		this.descripcion = descripcion;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.estado = estado;
		this.epoca = epoca;
	}

	public PeriodoPago(Integer idPeriodoPago, String descripcion, Date fechaInicio, Date fechaFin, Integer anio,
			Integer mes) {
		this.idPeriodoPago = idPeriodoPago;
		this.descripcion = descripcion;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.anio = anio;
		this.mes = mes;
	}
	

	public PeriodoPago(Integer idPeriodoPago, String descripcion, String estado, EpocaEnum epoca) {
		super();
		this.idPeriodoPago = idPeriodoPago;
		this.descripcion = descripcion;
		this.estado = estado;
		this.epoca = epoca;
	}

	@AuditoriaMethod(isIdEntity = true, disabled = true)
	public Integer getIdPeriodoPago() {
		return idPeriodoPago;
	}

	public void setIdPeriodoPago(Integer idPeriodoPago) {
		this.idPeriodoPago = idPeriodoPago;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	@AuditoriaMethod(name = "Estado")
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<CabeceraPlanilla> getCabeceraPlanillaList() {
		return cabeceraPlanillaList;
	}

	public void setCabeceraPlanillaList(List<CabeceraPlanilla> cabeceraPlanillaList) {
		this.cabeceraPlanillaList = cabeceraPlanillaList;
	}

	public List<RegistroEconomico> getRegistroEconomicoList() {
		return registroEconomicoList;
	}

	public void setRegistroEconomicoList(List<RegistroEconomico> registroEconomicoList) {
		this.registroEconomicoList = registroEconomicoList;
	}

	public List<Lectura> getLecturasList() {
		return lecturasList;
	}

	public void setLecturasList(List<Lectura> lecturasList) {
		this.lecturasList = lecturasList;
	}

	public List<Actividad> getActividadList() {
		return actividadList;
	}

	public void setActividadList(List<Actividad> actividadList) {
		this.actividadList = actividadList;
	}

	public List<Gasto> getGastoList() {
		return gastoList;
	}

	public void setGastoList(List<Gasto> gastoList) {
		this.gastoList = gastoList;
	}

	public EpocaEnum getEpoca() {
		return epoca;
	}

	public void setEpoca(EpocaEnum epoca) {
		this.epoca = epoca;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idPeriodoPago != null ? idPeriodoPago.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof PeriodoPago)) {
			return false;
		}
		PeriodoPago other = (PeriodoPago) object;
		if ((this.idPeriodoPago == null && other.idPeriodoPago != null)
				|| (this.idPeriodoPago != null && !this.idPeriodoPago.equals(other.idPeriodoPago))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.PeriodoPago[ idPeriodoPago=" + idPeriodoPago + " ]";
	}

}
