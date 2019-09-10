/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ec.jap.entiti.saap;

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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "detalle_planilla")
@NamedQueries({
		
		@NamedQuery(name = "DetallePlanilla.findByLecturaAndCabcera", query = "SELECT d FROM DetallePlanilla d where d.idLectura=:idLectura and d.idCabeceraPlanilla=:idCabeceraPlanilla "),
		@NamedQuery(name = "DetallePlanilla.findValorInasistencias", query = "SELECT CASE :tipo WHEN 'I' THEN SUM(dp.valorPagado) ELSE SUM(dp.valorPendiente) END FROM DetallePlanilla dp inner join dp.idAsistencia ina inner join ina.idRegistroEconomico re inner join re.tipoRegistro tr  WHERE tr=:tipoRegistro  AND dp.estado IN (:estado1,:estado2) "),
		@NamedQuery(name = "DetallePlanilla.findValor", query = "SELECT CASE :tipo WHEN 'I' THEN SUM(dp.valorPagado) ELSE SUM(dp.valorPendiente) END FROM DetallePlanilla dp inner join dp.idRegistroEconomico re inner join re.tipoRegistro tr  WHERE tr=:tipoRegistro AND dp.estado IN (:estado1,:estado2) "),
		@NamedQuery(name = "DetallePlanilla.findByInc", query = "SELECT d FROM DetallePlanilla d where d.idCabeceraPlanilla!=:idCabeceraPlanilla AND d.idCabeceraPlanilla.idLlave=:idLlave AND d.estado IN ('NOPAG','INC') ORDER BY d.ordenStr,d.fechaRegistro ASC"),
		@NamedQuery(name = "DetallePlanilla.findByCabecaraNoPagInc", query = "SELECT d FROM DetallePlanilla d where d.idCabeceraPlanilla=:idCabeceraPlanilla AND d.estado IN ('ING','INC') AND d.valorPendiente!=0 ORDER BY d.ordenStr,d.fechaRegistro ASC"),
		@NamedQuery(name = "DetallePlanilla.findConsumosAntValor", query = "SELECT CASE :tipo WHEN 'I' THEN SUM(dp.valorPagado) ELSE SUM(dp.valorPendiente) END FROM DetallePlanilla dp inner join dp.idRegistroEconomico tr WHERE tr.tipoRegistro=:tipoRegistro AND dp.estado IN (:estado1,:estado2) "),
		@NamedQuery(name = "DetallePlanilla.findConsumosValor", query = "SELECT CASE :tipo WHEN 'I' THEN SUM(dp.valorPagado) ELSE SUM(dp.valorPendiente) END  FROM DetallePlanilla dp inner join dp.idLectura lec inner join lec.tipoRegistro tr  WHERE tr=:tipoRegistro AND  dp.estado IN (:estado1,:estado2) "),
		@NamedQuery(name = "DetallePlanilla.findConsumosAnt", query = "SELECT SUM(dp.valorPagado) FROM DetallePlanilla dp inner join dp.idRegistroEconomico tr inner join dp.idCabeceraPlanilla cp inner join cp.idPeriodoPago pp WHERE tr.tipoRegistro=:tipoRegistro AND  (pp.anio=:anio or :anio=0) AND (pp.mes=:mes or :mes=-1) AND dp.estado IN ('PAG','INC') "),
		@NamedQuery(name = "DetallePlanilla.findByCabecaraForCancelar", query = "SELECT d FROM DetallePlanilla d where d.idCabeceraPlanilla=:idCabeceraPlanilla AND d.estado IN ('PAG','INC')"),
		@NamedQuery(name = "DetallePlanilla.findCuotaInasistenciaAcumulat", query = "SELECT SUM(dp.valorPagado) FROM DetallePlanilla dp inner join dp.idAsistencia ina inner join ina.idRegistroEconomico re inner join re.tipoRegistro tr inner join dp.idCabeceraPlanilla cp inner join cp.idPeriodoPago pp WHERE tr=:tipoRegistro AND  (pp.anio=:anio or :anio=0) AND (pp.mes=:mes or :mes=-1) AND dp.estado IN ('PAG','INC') "),
		@NamedQuery(name = "DetallePlanilla.findCuotasAcumulatovo", query = "SELECT SUM(dp.valorPagado) FROM DetallePlanilla dp inner join dp.idRegistroEconomico re inner join re.tipoRegistro tr inner join dp.idCabeceraPlanilla cp inner join cp.idPeriodoPago pp WHERE tr=:tipoRegistro AND  (pp.anio=:anio or :anio=0)  AND (pp.mes=:mes or :mes=-1)  AND dp.estado IN ('PAG','INC') "),
		@NamedQuery(name = "DetallePlanilla.findAcumulativos", query = "SELECT SUM(dp.valorPagado) FROM DetallePlanilla dp inner join dp.idRegistroEconomico re inner join re.tipoRegistro tr inner join dp.idCabeceraPlanilla cp inner join cp.idPeriodoPago pp WHERE tr=:tipoRegistro AND  (pp.anio=:anio or :anio=0) AND (pp.mes=:mes or :mes=-1)  AND dp.estado IN ('PAG','INC') "),
		@NamedQuery(name = "DetallePlanilla.findConsumosAntAcum", query = "SELECT SUM(dp.valorPagado) FROM DetallePlanilla dp inner join dp.idRegistroEconomico tr inner join dp.idCabeceraPlanilla cp inner join cp.idPeriodoPago pp WHERE tr.tipoRegistro=:tipoRegistro AND  (pp.anio=:anio or :anio=0) AND (pp.mes=:mes or :mes=-1) AND dp.estado IN ('PAG','INC') "),
		@NamedQuery(name = "DetallePlanilla.findConsumosAcum", query = "SELECT SUM(dp.valorPagado) FROM DetallePlanilla dp inner join dp.idLectura lec inner join lec.tipoRegistro tr inner join dp.idCabeceraPlanilla cp inner join cp.idPeriodoPago pp WHERE tr=:tipoRegistro AND  (pp.anio=:anio or :anio=0) AND (pp.mes=:mes or :mes=-1) AND dp.estado IN ('PAG','INC') "),
		@NamedQuery(name = "DetallePlanilla.findCuotaInasistencia", query = "SELECT SUM(dp.valorPagado) FROM DetallePlanilla dp inner join dp.idAsistencia ina inner join ina.idRegistroEconomico re inner join re.tipoRegistro tr inner join dp.idCabeceraPlanilla cp inner join cp.idPeriodoPago pp WHERE tr=:tipoRegistro AND  (pp.anio=:anio or :anio=0) AND (pp.mes=:mes or :mes=-1) AND dp.estado IN ('PAG','INC') "),
		@NamedQuery(name = "DetallePlanilla.findCuotas", query = "SELECT SUM(dp.valorPagado) FROM DetallePlanilla dp inner join dp.idRegistroEconomico re inner join re.tipoRegistro tr inner join dp.idCabeceraPlanilla cp inner join cp.idPeriodoPago pp WHERE tr=:tipoRegistro AND  (pp.anio=:anio or :anio=0) AND (pp.mes=:mes or :mes=-1) AND dp.estado IN ('PAG','INC') "),
		@NamedQuery(name = "DetallePlanilla.findConsumos", query = "SELECT SUM(dp.valorPagado) FROM DetallePlanilla dp inner join dp.idLectura lec inner join lec.tipoRegistro tr inner join dp.idCabeceraPlanilla cp inner join cp.idPeriodoPago  pp WHERE tr=:tipoRegistro AND  (pp.anio=:anio or :anio=0) AND (pp.mes=:mes or :mes=-1) AND dp.estado IN ('PAG','INC') "),
		@NamedQuery(name = "DetallePlanilla.findByCabecaraSinPagar", query = "SELECT d FROM DetallePlanilla d where d.idCabeceraPlanilla=:idCabeceraPlanilla AND d.estado IN ('ING','INC') and coalesce(d.valorPagado,0.0)<d.valorTotal ORDER BY d.ordenStr,d.fechaRegistro ASC"),
		@NamedQuery(name = "DetallePlanilla.findByCabecaraNoPag", query = "SELECT d FROM DetallePlanilla d where d.idCabeceraPlanilla=:idCabeceraPlanilla AND d.estado IN ('NOPAG') AND d.valorPendiente != 0 ORDER BY d.ordenStr,d.fechaRegistro ASC"),
		@NamedQuery(name = "DetallePlanilla.findByLectura", query = "SELECT d FROM DetallePlanilla d where d.idLectura=:idLectura"),
		@NamedQuery(name = "DetallePlanilla.findByBasico", query = "SELECT d FROM DetallePlanilla d where d.idRegistroEconomico=:regeco and d.idCabeceraPlanilla=:cp and upper(d.descripcion) like concat('%',upper(:desc),'%')"),
		@NamedQuery(name = "DetallePlanilla.findByUserAndTipoReg", query = "select dp FROM DetallePlanilla dp inner join dp.idCabeceraPlanilla cp inner join cp.idLlave ll inner join ll.idUsuario u inner join dp.idRegistroEconomico re inner join re.idPeriodoPago pp inner join re.tipoRegistro tp WHERE tp.tipoRegistro=:tipoRegistro AND u.idUsuario=:idUsuario AND (pp.idPeriodoPago=:idPeriodoPago OR :idPeriodoPago=0) AND dp.estado not in ('TRAS') ORDER BY re.fechaRegistro,re.descripcion"),
		@NamedQuery(name = "DetallePlanilla.findByCabecara", query = "SELECT d FROM DetallePlanilla d where d.idCabeceraPlanilla=:idCabeceraPlanilla   ORDER BY d.ordenStr,d.fechaRegistro ASC"),// ,d.idDetallePlanilla
																																																	// //
																																																	// ASC
		@NamedQuery(name = "DetallePlanilla.findByRegistroAndCabecara", query = "SELECT MAX(d) FROM DetallePlanilla d where d.idCabeceraPlanilla=:idCabeceraPlanilla AND d.idRegistroEconomico=:idRegistroEconomico") })
public class DetallePlanilla implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_detalle_planilla")
	private Integer idDetallePlanilla;

	@Basic(optional = false)
	@NotNull
	@Column(name = "valor_unidad")
	private Double valorUnidad;

	@Column(name = "valor_total")
	private Double valorTotal;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 16)
	@Column(name = "dp_estado")
	private String estado;
	@JoinColumn(name = "id_registro_economico", referencedColumnName = "id_registro_economico")
	@ManyToOne
	private RegistroEconomico idRegistroEconomico;
	@JoinColumn(name = "id_lectura", referencedColumnName = "id_lectura")
	@ManyToOne
	private Lectura idLectura;
	@JoinColumn(name = "id_cabecera_planilla", referencedColumnName = "id_cabecera_planilla")
	@ManyToOne
	private CabeceraPlanilla idCabeceraPlanilla;

	@JoinColumn(name = "id_asistencia", referencedColumnName = "id_asistencia")
	@ManyToOne
	private Asistencia idAsistencia;

	@Column(name = "es_manual")
	private Boolean esManual;

	private String descripcion;

	@Column(name = "valor_pagado")
	private Double valorPagado;

	@Column(name = "valor_pendiente")
	private Double valorPendiente;

	@Column(name = "orden_str")
	private String ordenStr;

	@Column(name = "valor_total_origen")
	private Double valorTotalOrigen;

	private String origen;
	@Transient
	private String outcome;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_registro")
	private Date fechaRegistro;

	public DetallePlanilla() {
	}

	public DetallePlanilla(Integer idDetallePlanilla) {
		this.idDetallePlanilla = idDetallePlanilla;
	}

	public DetallePlanilla(Integer idDetallePlanilla, Double valorUnidad, String estado) {
		this.idDetallePlanilla = idDetallePlanilla;
		this.valorUnidad = valorUnidad;
		this.estado = estado;
	}

	public Integer getIdDetallePlanilla() {
		return idDetallePlanilla;
	}

	public void setIdDetallePlanilla(Integer idDetallePlanilla) {
		this.idDetallePlanilla = idDetallePlanilla;
	}

	public Double getValorUnidad() {
		return valorUnidad;
	}

	public void setValorUnidad(Double valorUnidad) {
		this.valorUnidad = valorUnidad;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public RegistroEconomico getIdRegistroEconomico() {
		return idRegistroEconomico;
	}

	public void setIdRegistroEconomico(RegistroEconomico idRegistroEconomico) {
		this.idRegistroEconomico = idRegistroEconomico;
	}

	public Lectura getIdLectura() {
		return idLectura;
	}

	public void setIdLectura(Lectura idLectura) {
		this.idLectura = idLectura;
	}

	public CabeceraPlanilla getIdCabeceraPlanilla() {
		return idCabeceraPlanilla;
	}

	public void setIdCabeceraPlanilla(CabeceraPlanilla idCabeceraPlanilla) {
		this.idCabeceraPlanilla = idCabeceraPlanilla;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Asistencia getIdAsistencia() {
		return idAsistencia;
	}

	public void setIdAsistencia(Asistencia idAsistencia) {
		this.idAsistencia = idAsistencia;
	}

	public Boolean getEsManual() {
		return esManual;
	}

	public void setEsManual(Boolean esManual) {
		this.esManual = esManual;
	}

	/**
	 * Atributo valorPagado
	 * 
	 * @return el valor del atributo valorPagado
	 */
	public Double getValorPagado() {
		return valorPagado;
	}

	/**
	 * El @param valorPagado define valorPagado
	 */
	public void setValorPagado(Double valorPagado) {
		this.valorPagado = valorPagado;
	}

	/**
	 * Atributo valorPendiente
	 * 
	 * @return el valor del atributo valorPendiente
	 */
	public Double getValorPendiente() {
		return valorPendiente != null ? valorPendiente : 0.0;
	}

	/**
	 * El @param valorPendiente define valorPendiente
	 */
	public void setValorPendiente(Double valorPendiente) {
		this.valorPendiente = valorPendiente;
	}

	/**
	 * Atributo ordenStr
	 * 
	 * @return el valor del atributo ordenStr
	 */
	public String getOrdenStr() {
		return ordenStr;
	}

	/**
	 * El @param ordenStr define ordenStr
	 */
	public void setOrdenStr(String ordenStr) {
		this.ordenStr = ordenStr;
	}

	/**
	 * Atributo valorTotalOrigen
	 * 
	 * @return el valor del atributo valorTotalOrigen
	 */
	public Double getValorTotalOrigen() {
		return valorTotalOrigen;
	}

	/**
	 * El @param valorTotalOrigen define valorTotalOrigen
	 */
	public void setValorTotalOrigen(Double valorTotalOrigen) {
		this.valorTotalOrigen = valorTotalOrigen;
	}

	/**
	 * Atributo origen
	 * 
	 * @return el valor del atributo origen
	 */
	public String getOrigen() {
		return origen;
	}

	/**
	 * El @param origen define origen
	 */
	public void setOrigen(String origen) {
		this.origen = origen;
	}

	/**
	 * Atributo outcome
	 * 
	 * @return el valor del atributo outcome
	 */
	public String getOutcome() {
		if (idLectura != null) {
			outcome = "detalleLectura";
		} else {
			outcome = "";
		}
		return outcome;
	}

	/**
	 * El @param outcome define outcome
	 */
	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idDetallePlanilla != null ? idDetallePlanilla.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof DetallePlanilla)) {
			return false;
		}
		DetallePlanilla other = (DetallePlanilla) object;
		if ((this.idDetallePlanilla == null && other.idDetallePlanilla != null) || (this.idDetallePlanilla != null && !this.idDetallePlanilla.equals(other.idDetallePlanilla))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.DetallePlanilla[ idDetallePlanilla=" + idDetallePlanilla + " ]";
	}
	public void initValue(){
		this.estado="ING";
		this.valorPagado=0.0;
	}

}
