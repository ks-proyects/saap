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

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "lectura")
@NamedQueries({
	@NamedQuery(name = "Lectura.findByPeridoAndService", query = "SELECT r FROM Lectura r where  r.idServicio=:idServicio AND r.idPeriodoPago=:idPeriodoPago"),	
	@NamedQuery(name = "Lectura.findLasThree", query = "SELECT r FROM Lectura r where  r.idServicio=:idServicio and r!=:idLectura order by r.idLectura DESC"),
		@NamedQuery(name = "Lectura.findByPeriod", query = "SELECT r FROM Lectura r where  r.idServicio=:idServicio AND  r.idPeriodoPago=:idPeriodoPago "),
		@NamedQuery(name = "Lectura.findByPeridoCerrModificable", query = "SELECT  cp,ll FROM  CabeceraPlanilla cp inner join cp.idUsuario u inner join u.llaveList ll where cp.estado=:estado AND cp.idPeriodoPago.idPeriodoPago=:idPeriodoPago AND ll.tipoServicio=:tipoServicio AND ll IN (select lect.idServicio FROM Lectura lect WHERE lect.esModificable=:esModificable AND lect.idPeriodoPago=:idPeriodoPago) order by u.apellidos,u.apellidos.nombres"),
		@NamedQuery(name = "Lectura.findErroneoByPeriodoSN", query = "SELECT r FROM Lectura r where   r.idPeriodoPago.idPeriodoPago=:idPeriodoPago  AND r.sinLectura=:sinLectura AND r.lecturaIngresada<r.lecturaAnterior"),
		@NamedQuery(name = "Lectura.findErroneoByPeriodo", query = "SELECT r FROM Lectura r where   r.idPeriodoPago.idPeriodoPago=:idPeriodoPago AND r.usuarioNuevo!=:usuarioNuevo  AND r.sinLectura!=:sinLectura AND r.lecturaIngresada<r.lecturaAnterior"),
		@NamedQuery(name = "Lectura.findLastByPeridoAndLlave", query = "SELECT MAX(r.idLectura) FROM Lectura r where  r.idServicio=:idServicio AND  r.usuarioNuevo!=:usuarioNuevo "),
		@NamedQuery(name = "Lectura.findByPer", query = "SELECT  r FROM Lectura r inner join r.idPeriodoPago pp inner join r.idServicio ll inner join ll.idUsuario u WHERE pp=:p AND (UPPER(ll.numero) LIKE UPPER(CONCAT('%',:filtro,'%')) OR UPPER(u.nombres) LIKE UPPER(CONCAT('%',:filtro,'%')) OR UPPER(u.apellidos) LIKE UPPER(CONCAT('%',:filtro,'%'))  OR UPPER(u.cedula) LIKE UPPER(CONCAT('%',:filtro,'%')) ) ORDER BY cast(ll.numero,int)"),
		@NamedQuery(name = "Lectura.findByAnioAndMes", query = "SELECT DISTINCT r FROM Lectura r inner join r.idPeriodoPago pp WHERE r.idServicio=:llave AND pp.anio=:anio AND pp.mes=:mes"),
		@NamedQuery(name = "Lectura.findByPerido", query = "SELECT  cp,ll FROM  CabeceraPlanilla cp inner join cp.idUsuario u inner join  u.llaveList ll   where  cp.estado=:estado AND cp.idPeriodoPago.idPeriodoPago=:idPeriodoPago order by u.apellidos,u.apellidos.nombres"),
		@NamedQuery(name = "Lectura.findByPeridoAndLlave", query = "SELECT r FROM Lectura r where  r.idServicio.idServicio=:idServicio and r.estado='ING' AND r.idPeriodoPago=:idPeriodoPago"),
		@NamedQuery(name = "Lectura.findByUser", query = "SELECT r FROM Lectura r inner join r.idServicio ll where ll=:idServicio AND ( r.idPeriodoPago.idPeriodoPago=:idPeriodoPago or :idPeriodoPago=0) ORDER BY r.fechaRegistro DESC"), })
@AuditoriaAnot(entityType = "LEC")
public class Lectura implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_lectura")
	private Integer idLectura;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "descripcion")
	private String descripcion;

	@Basic(optional = false)
	@NotNull
	@Column(name = "metros3")
	private Double metros3;

	@NotNull(message = "El campo LECTURA es obligatorio.")
	@Column(name = "lectura_ingresada")
	private Double lecturaIngresada;

	@Column(name = "lectura_anterior")
	private Double lecturaAnterior;

	@Basic(optional = false)
	@NotNull
	@Column(name = "fecha_registro")
	@Temporal(TemporalType.DATE)
	private Date fechaRegistro;

	@Size(max = 16)
	@Column(name = "l_estado")
	private String estado;

	@OneToMany(mappedBy = "idLectura")
	private List<DetallePlanilla> detallePlanillaList;

	@JoinColumn(name = "tipo_registro", referencedColumnName = "tipo_registro")
	@ManyToOne(optional = false)
	private TipoRegistro tipoRegistro;

	@JoinColumn(name = "id_servicio", referencedColumnName = "id_servicio")
	@ManyToOne(optional = false)
	private Servicio idServicio;

	@JoinColumn(name = "id_periodo_pago", referencedColumnName = "id_periodo_pago")
	@ManyToOne
	private PeriodoPago idPeriodoPago;

	@Column(name = "metros3_exceso")
	private Double metros3Exceso;

	@Column(name = "valor_metro3")
	private Double valorMetro3;

	@Column(name = "valor_metro3_exceso")
	private Double valorMetro3Exceso;

	@Column(name = "sin_lectura")
	private Boolean sinLectura;

	@Column(name = "usuario_nuevo")
	private Boolean usuarioNuevo;

	@Column(name = "valor_basico")
	private Double valorBasico;

	@Column(name = "numero_meses")
	private Integer numeroMeses;

	@Column(name = "basico_m3")
	private Double basicoM3;

	@Column(name = "es_modificable")
	private Boolean esModificable;

	@Column(name = "metros3_anterior")
	private Double metros3Anterior;

	@Transient
	private Boolean disabled;

	@Transient
	private Boolean modifiqued;

	public Lectura() {
	}

	public Lectura(Integer idLectura) {
		this.idLectura = idLectura;
	}

	public Lectura(Integer idLectura, String descripcion, Double metros3, Date fechaRegistro) {
		this.idLectura = idLectura;
		this.descripcion = descripcion;
		this.metros3 = metros3;
		this.fechaRegistro = fechaRegistro;
	}

	@AuditoriaMethod(isIdEntity = true, disabled = true)
	public Integer getIdLectura() {
		return idLectura;
	}

	public void setIdLectura(Integer idLectura) {
		this.idLectura = idLectura;
	}

	@AuditoriaMethod(disabled = true)
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@AuditoriaMethod(disabled = true)
	public Double getMetros3() {
		return metros3;
	}

	public void setMetros3(Double metros3) {
		this.metros3 = metros3;
	}

	@AuditoriaMethod(disabled = true)
	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	@AuditoriaMethod(disabled = true)
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

	@AuditoriaMethod(disabled = true)
	public TipoRegistro getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(TipoRegistro tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	@AuditoriaMethod(name = "N�mero")
	public String getIdLlaveNumero() {
		return idServicio.getNumero();
	}

	@AuditoriaMethod(disabled = true)
	public Servicio getIdServicio() {
		return idServicio;
	}

	public void setIdServicio(Servicio idServicio) {
		this.idServicio = idServicio;
	}

	@AuditoriaMethod(disabled = true)
	public PeriodoPago getIdPeriodoPago() {
		return idPeriodoPago;
	}

	public void setIdPeriodoPago(PeriodoPago idPeriodoPago) {
		this.idPeriodoPago = idPeriodoPago;
	}

	@AuditoriaMethod(disabled = true)
	public Boolean getReadOnly() {
		if (idPeriodoPago != null && "CERR".equals(idPeriodoPago.getEstado())) {
			if (sinLectura)
				return false;
			else
				return esModificable;
		}
		return false;
	}

	@AuditoriaMethod(methodToAudit = true, disabled = true)
	public Boolean getValidToAudit() {
		return modifiqued != null && modifiqued;
	}

	@AuditoriaMethod(name = "Lectura Nueva")
	public Double getLecturaIngresada() {
		return lecturaIngresada;
	}

	public void setLecturaIngresada(Double lecturaIngresada) {
		this.lecturaIngresada = lecturaIngresada;
	}

	@AuditoriaMethod(name = "Lectura Anterior")
	public Double getLecturaAnterior() {
		return lecturaAnterior;
	}

	public void setLecturaAnterior(Double lecturaAnterior) {
		this.lecturaAnterior = lecturaAnterior;
	}

	@AuditoriaMethod(disabled = true)
	public Double getMetros3Exceso() {
		return metros3Exceso != null ? metros3Exceso : 0.0;
	}

	public void setMetros3Exceso(Double metros3Exceso) {
		this.metros3Exceso = metros3Exceso;
	}

	@AuditoriaMethod(name = "Valor Metro3")
	public Double getValorMetro3() {
		return valorMetro3;
	}

	public void setValorMetro3(Double valorMetro3) {
		this.valorMetro3 = valorMetro3;
	}

	@AuditoriaMethod(disabled = true)
	public Double getValorMetro3Exceso() {
		return valorMetro3Exceso == null ? 0.0 : valorMetro3Exceso;
	}

	public void setValorMetro3Exceso(Double valorMetro3Exceso) {
		this.valorMetro3Exceso = valorMetro3Exceso;
	}

	@AuditoriaMethod(disabled = true)
	public Boolean getSinLectura() {
		return sinLectura;
	}

	public void setSinLectura(Boolean sinLectura) {
		this.sinLectura = sinLectura;
	}

	@AuditoriaMethod(disabled = true)
	public Boolean getUsuarioNuevo() {
		return usuarioNuevo;
	}

	public void setUsuarioNuevo(Boolean usuarioNuevo) {
		this.usuarioNuevo = usuarioNuevo;

	}

	@AuditoriaMethod(name = "Valor B�sico")
	public Double getValorBasico() {
		return valorBasico;
	}

	public void setValorBasico(Double valorBasico) {
		this.valorBasico = valorBasico;
	}

	@AuditoriaMethod(disabled = true)
	public Integer getNumeroMeses() {
		return numeroMeses;
	}

	public void setNumeroMeses(Integer numeroMeses) {
		this.numeroMeses = numeroMeses;
	}

	@AuditoriaMethod(disabled = true)
	public Double getBasicoM3() {
		return basicoM3 != null ? basicoM3 : 0.0;
	}

	public void setBasicoM3(Double basicoM3) {
		this.basicoM3 = basicoM3;
	}

	/**
	 * Atributo esModificable
	 * 
	 * @return el valor del atributo esModificable
	 */
	@AuditoriaMethod(disabled = true)
	public Boolean getEsModificable() {
		return esModificable;
	}

	/**
	 * El @param esModificable define esModificable
	 */
	public void setEsModificable(Boolean esModificable) {
		this.esModificable = esModificable;
	}

	/**
	 * Atributo metros3Anterior
	 * 
	 * @return el valor del atributo metros3Anterior
	 */
	@AuditoriaMethod(disabled = true)
	public Double getMetros3Anterior() {
		return metros3Anterior != null ? metros3Anterior : 0.0;
	}

	/**
	 * El @param metros3Anterior define metros3Anterior
	 */
	public void setMetros3Anterior(Double metros3Anterior) {
		this.metros3Anterior = metros3Anterior;
	}

	@AuditoriaMethod(disabled = true)
	public Boolean getDisabled() {
		return disabled == null ? true : disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	@AuditoriaMethod(disabled = true)
	public Boolean getModifiqued() {
		return modifiqued;
	}

	public void setModifiqued(Boolean modifiqued) {
		this.modifiqued = modifiqued;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idLectura != null ? idLectura.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Lectura)) {
			return false;
		}
		Lectura other = (Lectura) object;
		if ((this.idLectura == null && other.idLectura != null)
				|| (this.idLectura != null && !this.idLectura.equals(other.idLectura))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.Lectura[ idLectura=" + idLectura + " ]";
	}
}
