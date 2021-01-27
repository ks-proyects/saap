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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.ec.jap.anotaciones.AuditoriaAnot;
import org.ec.jap.anotaciones.AuditoriaMethod;
import org.ec.jap.utilitario.Utilitario;

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "cabecera_planilla")
@NamedQueries({
		@NamedQuery(name = "CabeceraPlanilla.findConsulta", query = "SELECT c FROM CabeceraPlanilla c INNER JOIN c.idServicio ll inner join ll.idUsuario u INNER JOIN c.idPeriodoPago per WHERE per.estado='CERR' AND (  ll.numero = :filtro OR u.cedula = :filtro) ORDER BY cast(ll.numero,int),c.fechaRegistro,c.observacion DESC"),
		@NamedQuery(name = "CabeceraPlanilla.findConAbono", query = "SELECT c FROM CabeceraPlanilla c WHERE  c.estado in (:estado,:estado2) AND c.idPeriodoPago.idPeriodoPago=:idPeriodoPago AND c.valorPagadoAbono!=0.0"),
		@NamedQuery(name = "CabeceraPlanilla.findSinPagar", query = "SELECT c FROM CabeceraPlanilla c WHERE  c.estado in (:estado,:estado2) AND c.idPeriodoPago.idPeriodoPago=:idPeriodoPago "),
		@NamedQuery(name = "CabeceraPlanilla.findAbono", query = "SELECT c FROM CabeceraPlanilla  c INNER JOIN c.idServicio ll   WHERE ll=:llave AND c IN (SELECT MAX(cp) from CabeceraPlanilla cp INNER JOIN cp.idServicio ll WHERE ll=:llave AND cp!=:cp )"),
		@NamedQuery(name = "CabeceraPlanilla.findByPerAbiertActFilters", query = "SELECT c FROM CabeceraPlanilla c inner join c.idUsuario u inner join u.llaveList ll "
				+ "WHERE c IN "
				+ "	(SELECT dp.idCabeceraPlanilla FROM DetallePlanilla dp INNER JOIN dp.idCabeceraPlanilla cabp "
				+ "	 	WHERE cabp.idCabeceraPlanilla=c.idCabeceraPlanilla and cabp.idPeriodoPago.estado=:estado ) "
				+ "AND c.idPeriodoPago.estado=:estado " + "AND ( " + "     ll.numero like CONCAT('%',:filtro,'%') "
				+ "     OR UPPER(ll.idUsuario.nombres) like  UPPER(CONCAT('%',:filtro,'%')) "
				+ "     OR UPPER(ll.idUsuario.apellidos) like  UPPER(CONCAT('%',:filtro,'%')) "
				+ "     OR UPPER(u.nombres) like  UPPER(CONCAT('%',:filtro,'%')) "
				+ "     OR UPPER(u.apellidos) like  UPPER(CONCAT('%',:filtro,'%')) " + " ) "
				+ " ORDER BY u.apellidos,u.apellidos.nombres"),
		@NamedQuery(name = "CabeceraPlanilla.findNoPag", query = "SELECT c FROM CabeceraPlanilla c WHERE  c.estado in (:estado,:estado2) AND c.idPeriodoPago.idPeriodoPago=:idPeriodoPago"),
		@NamedQuery(name = "CabeceraPlanilla.findAllNoPag", query = "SELECT c FROM CabeceraPlanilla c WHERE  c.estado=:estado AND ( c.idServicio=:idServicio or c.idUsuario=:idUsuario)"),
		@NamedQuery(name = "CabeceraPlanilla.findAllNoPagAlcantiralado", query = "SELECT c FROM CabeceraPlanilla c WHERE  c.estado=:estado AND c.idUsuario=:idUser "),
		@NamedQuery(name = "CabeceraPlanilla.findNewUser", query = "SELECT COUNT(c.idCabeceraPlanilla) FROM CabeceraPlanilla c WHERE c.idServicio=:idServicio AND c IN ( SELECT dp.idCabeceraPlanilla FROM DetallePlanilla dp INNER JOIN dp.idLectura l WHERE l.idServicio=:idServicio )"),
		@NamedQuery(name = "CabeceraPlanilla.findByFilters", query = "SELECT c FROM CabeceraPlanilla c INNER JOIN c.idServicio ll inner join ll.idUsuario u WHERE  ll.numero like CONCAT('%',:filtro,'%') OR u.cedula like CONCAT('%',:filtro,'%') OR u.cedula like CONCAT('%',:filtro,'%')  OR UPPER(u.nombres) like  UPPER(CONCAT('%',:filtro,'%')) OR UPPER(u.apellidos) like  UPPER(CONCAT('%',:filtro,'%')) ORDER BY cast(ll.numero,int),c.fechaRegistro,c.observacion DESC"),
		@NamedQuery(name = "CabeceraPlanilla.findAllIngresado", query = "SELECT c FROM CabeceraPlanilla c WHERE c.estado='ING'"),
		@NamedQuery(name = "CabeceraPlanilla.findByUsuarioAndEstado", query = "SELECT c FROM CabeceraPlanilla c inner join c.idServicio  ll where ll.idServicio=:idServicio AND c.estado=:estado") })
@AuditoriaAnot(entityType = "CABPLA")
public class CabeceraPlanilla implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_cabecera_planilla")
	private Integer idCabeceraPlanilla;
	@Size(max = 2147483647)
	@Column(name = "observacion")
	private String observacion;

	@Column(name = "observacion1")
	private String observacion1;

	@Basic(optional = false)
	@NotNull
	@Column(name = "subtotal")
	private Double subtotal;
	@Basic(optional = false)
	@NotNull
	@Column(name = "descuento")
	private Double descuento;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_registro")
	private Date fechaRegistro;

	@Basic(optional = false)
	@NotNull
	@Column(name = "base")
	private Double base;
	@Basic(optional = false)
	@NotNull
	@Column(name = "total")
	private Double total;

	@Column(name = "valor_pagado")
	private Double valorPagado;

	@Column(name = "cambio_usd")
	private Double cambioUsd;

	@Column(name = "abono_usd")
	private Double abonoUsd;

	@Column(name = "valor_cancelado")
	private Double valorCancelado;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "cp_estado")
	private String estado;
	@OneToMany(mappedBy = "idCabeceraPlanilla")
	private List<DetallePlanilla> detallePlanillaList;
	
	@JoinColumn(name = "id_servicio", referencedColumnName = "id_servicio")
	@ManyToOne
	private Servicio idServicio;

	@JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
	@ManyToOne
	private Usuario idUsuario;

	@JoinColumn(name = "id_periodo_pago", referencedColumnName = "id_periodo_pago")
	@ManyToOne
	private PeriodoPago idPeriodoPago;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_pago")
	private Date fechaPago;

	@Column(name = "valor_pagado_abono")
	private Double valorPagadoAbono;

	@Transient
	private Double valorPendiente = 0.0;

	public CabeceraPlanilla() {
	}

	public CabeceraPlanilla(Integer idCabeceraPlanilla) {
		this.idCabeceraPlanilla = idCabeceraPlanilla;
	}

	public CabeceraPlanilla(Integer idCabeceraPlanilla, Double subtotal, Double descuento, Double base, Double total,
			String estado) {
		this.idCabeceraPlanilla = idCabeceraPlanilla;
		this.subtotal = subtotal;
		this.descuento = descuento;
		this.base = base;
		this.total = total;
		this.estado = estado;
	}

	@AuditoriaMethod(methodToAudit = true, disabled = true)
	public Boolean getValidToAudit() {
		return valorPagado != null && valorPagado > 0.0;
	}

	@AuditoriaMethod(isIdEntity = true, disabled = true)
	public Integer getIdCabeceraPlanilla() {
		return idCabeceraPlanilla;
	}

	@AuditoriaMethod(name = "LLave")
	public String getIdLlaveNumero() {
		return idServicio != null ? idServicio.getNumero() : "";
	}

	@AuditoriaMethod(name = "Usuario")
	public String getUsuarioNombre() {
		return idUsuario != null ? idUsuario.getNombres() : "";
	}

	public void setIdCabeceraPlanilla(Integer idCabeceraPlanilla) {
		this.idCabeceraPlanilla = idCabeceraPlanilla;
	}

	@AuditoriaMethod(name = "N�mero de Factura")
	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	@AuditoriaMethod(disabled = true)
	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	@AuditoriaMethod(disabled = true)
	public Double getDescuento() {
		return descuento;
	}

	public void setDescuento(Double descuento) {
		this.descuento = descuento;
	}

	@AuditoriaMethod(disabled = true)
	public Double getBase() {
		return base;
	}

	public void setBase(Double base) {
		this.base = base;
	}

	@AuditoriaMethod(name = "Valor Factura")
	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	@AuditoriaMethod(name = "Estado")
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<DetallePlanilla> getDetallePlanillaList() {
		return detallePlanillaList;
	}

	public void setDetallePlanillaList(List<DetallePlanilla> detallePlanillaList) {
		this.detallePlanillaList = detallePlanillaList;
	}

	public Servicio getIdServicio() {
		return idServicio;
	}

	public void setIdServicio(Servicio idServicio) {
		this.idServicio = idServicio;
	}

	public PeriodoPago getIdPeriodoPago() {
		return idPeriodoPago;
	}

	public void setIdPeriodoPago(PeriodoPago idPeriodoPago) {
		this.idPeriodoPago = idPeriodoPago;
	}

	@AuditoriaMethod(disabled = true)
	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	@AuditoriaMethod(name = "Valor Pagado")
	public Double getValorPagado() {
		return valorPagado == null ? 0.0 : valorPagado;
	}

	public void setValorPagado(Double valorPagado) {
		this.valorPagado = valorPagado;
	}

	@AuditoriaMethod(disabled = true)
	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	@AuditoriaMethod(disabled = true)
	public Double getCambioUsd() {
		return cambioUsd != null ? cambioUsd : 0.0;
	}

	public void setCambioUsd(Double cambioUsd) {
		this.cambioUsd = cambioUsd;
	}

	@AuditoriaMethod(disabled = true)
	public Double getAbonoUsd() {
		return abonoUsd != null ? abonoUsd : 0.0;
	}

	public void setAbonoUsd(Double abonoUsd) {
		this.abonoUsd = abonoUsd;
	}

	@AuditoriaMethod(name = "Valor Cancelado")
	public Double getValorCancelado() {
		return valorCancelado != null ? valorCancelado : 0.0;
	}

	public void setValorCancelado(Double valorCancelado) {
		this.valorCancelado = valorCancelado;
	}

	/**
	 * Atributo valorPagadoAbono
	 * 
	 * @return el valor del atributo valorPagadoAbono
	 */
	@AuditoriaMethod(name = "Valor Abono")
	public Double getValorPagadoAbono() {
		return valorPagadoAbono != null ? valorPagadoAbono : 0.0;
	}

	/**
	 * El @param valorPagadoAbono define valorPagadoAbono
	 */
	public void setValorPagadoAbono(Double valorPagadoAbono) {
		this.valorPagadoAbono = valorPagadoAbono;
	}

	/**
	 * Atributo valorPendiente
	 * 
	 * @return el valor del atributo valorPendiente
	 */
	@AuditoriaMethod(name = "Valor Pendiente")
	public Double getValorPendiente() {
		try {
			return Utilitario.redondear(total - valorPagado);
		} catch (Exception e) {
			return valorPendiente;
		}
	}

	/**
	 * El @param valorPendiente define valorPendiente
	 */
	public void setValorPendiente(Double valorPendiente) {
		this.valorPendiente = valorPendiente;
	}

	@AuditoriaMethod(disabled = true)
	public String getObservacion1() {
		return observacion1;
	}

	public void setObservacion1(String observacion1) {
		this.observacion1 = observacion1;
	}

	public Usuario getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Usuario idUsuario) {
		this.idUsuario = idUsuario;
	}

	@AuditoriaMethod(disabled = true)
	public String getUsuarioDesc() {
		if (idUsuario != null) {
			return idUsuario.getNombres().concat(" ").concat(idUsuario.getApellidos());
		} else if (idServicio != null) {
			return idServicio.getIdUsuario().getNombres().concat(" ").concat(idServicio.getIdUsuario().getApellidos());
		}
		return "";
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idCabeceraPlanilla != null ? idCabeceraPlanilla.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof CabeceraPlanilla)) {
			return false;
		}
		CabeceraPlanilla other = (CabeceraPlanilla) object;
		if ((this.idCabeceraPlanilla == null && other.idCabeceraPlanilla != null)
				|| (this.idCabeceraPlanilla != null && !this.idCabeceraPlanilla.equals(other.idCabeceraPlanilla))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.CabeceraPlanilla[ idCabeceraPlanilla=" + idCabeceraPlanilla + " ]";
	}

}
