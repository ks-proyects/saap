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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "registro_economico")
@NamedQueries({
		@NamedQuery(name = "RegistroEconomico.findCuentasPorPagaAcumul", query = "SELECT SUM(r.valor) FROM RegistroEconomico r inner join r.tipoRegistro tr inner join r.idPeriodoPago pp WHERE tr=:tipoRegistro AND  (pp.anio<=:anio or :anio=0) AND (pp.mes<=:mes or :mes=-1)"),
		@NamedQuery(name = "RegistroEconomico.findCuentasPorPaga", query = "SELECT SUM(r.valor) FROM RegistroEconomico r inner join r.tipoRegistro tr inner join r.idPeriodoPago pp WHERE tr=:tipoRegistro AND  (pp.anio=:anio or :anio=0) AND (pp.mes=:mes or :mes=-1)"),
		@NamedQuery(name = "RegistroEconomico.findByPeriodoAndTipoLast", query = "SELECT r FROM RegistroEconomico r WHERE  r.idRegistroEconomico=(SELECT ree FROM RegistroEconomico ree WHERE ree.tipoRegistro=:tipoRegistro AND  ree.idPeriodoPago=:idPeriodoPago)"),
		@NamedQuery(name = "RegistroEconomico.findAllToYear", query = "SELECT pp.anio,pp.mes,tr.accion,SUM(re.valor*re.cantidadAplicados) from RegistroEconomico re inner join re.tipoRegistro tr inner join re.idPeriodoPago pp WHERE pp.anio<=:anio OR :anio=0 GROUP BY pp.fechaInicio, pp.anio,pp.mes,tr.accion ORDER BY pp.fechaInicio"),
		@NamedQuery(name = "RegistroEconomico.findAllByYear", query = "SELECT pp.anio,pp.mes,tr.accion,SUM(re.valor*re.cantidadAplicados) from RegistroEconomico re inner join re.tipoRegistro tr inner join re.idPeriodoPago pp WHERE (pp.anio=:anio OR :anio=0) GROUP BY pp.fechaInicio, pp.anio,pp.mes,tr.accion ORDER BY pp.fechaInicio"),
		@NamedQuery(name = "RegistroEconomico.findAllByAnioAndMes", query = "SELECT r.tipoRegistro.accion  ,SUM(r.valor*r.cantidadAplicados) FROM RegistroEconomico r WHERE   (r.idPeriodoPago.anio=:anio or :anio=0) AND (r.idPeriodoPago.mes=:mes or :mes=-1) GROUP BY r.tipoRegistro.accion "),
		@NamedQuery(name = "RegistroEconomico.findByAnioAndMes", query = "SELECT r.tipoRegistro.descripcion ,SUM(r.valor*r.cantidadAplicados) FROM RegistroEconomico r inner join r.idPeriodoPago pp WHERE r.tipoRegistro.accion=:accion AND  (pp.anio=:anio or :anio=0) AND (pp.mes=:mes or :mes=-1) GROUP BY r.tipoRegistro.descripcion "),
		@NamedQuery(name = "RegistroEconomico.findByType", query = "SELECT r FROM RegistroEconomico r WHERE r.tipoRegistro.tipoRegistro  IN (:tipoRegistro)AND r.idPeriodoPago=:idPeriodoPago ORDER BY r.fechaRegistro DESC"),
		@NamedQuery(name = "RegistroEconomico.finUser", query = "select DISTINCT u,cp,(select max(dp.idDetallePlanilla) from DetallePlanilla dp where dp.idCabeceraPlanilla=cp and dp.idRegistroEconomico=:idRegistroEconomico) from CabeceraPlanilla cp left outer join cp.idUsuario u  WHERE ( UPPER(u.nombres) LIKE UPPER(CONCAT('%',:filtro,'%')) OR UPPER(u.apellidos) LIKE UPPER(CONCAT('%',:filtro,'%')) OR UPPER(u.cedula) LIKE UPPER(CONCAT('%',:filtro,'%'))) AND  cp.idPeriodoPago IN (SELECT re.idPeriodoPago from RegistroEconomico re WHERE re=:idRegistroEconomico) AND u.estado in('ACT','EDI') AND u.tipoUsuario='JAAP' ORDER BY u.nombres,u.apellidos"),
		@NamedQuery(name = "RegistroEconomico.findAll", query = "SELECT r FROM RegistroEconomico r WHERE r.tipoRegistro.tipoRegistro  IN ('CUO') ORDER BY r.fechaRegistro DESC"),
		@NamedQuery(name = "RegistroEconomico.findByPeriodoAndTipo", query = "SELECT r FROM RegistroEconomico r WHERE r.tipoRegistro=:tipoRegistro AND  r.idPeriodoPago=:idPeriodoPago"),
		@NamedQuery(name = "RegistroEconomico.findByTiporegistro", query = "SELECT r FROM RegistroEconomico r WHERE r.tipoRegistro.tipoRegistro  IN (:tipoRegistro) AND UPPER(r.descripcion) LIKE UPPER(CONCAT('%',:filtro,'%')) ORDER BY r.fechaRegistro DESC") })
public class RegistroEconomico implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_registro_economico")
	private Integer idRegistroEconomico;
	@Column(name = "fecha_registro")
	@Temporal(TemporalType.DATE)
	private Date fechaRegistro;
	@Basic(optional = false)
	@NotNull(message = "El campo VALOR es obligatorio.")
	@Column(name = "valor")
	private Double valor;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "descripcion")
	private String descripcion;

	@Size(max = 16)
	@Column(name = "re_estado")
	private String estado;

	@Column(name = "cantidad_aplicados")
	private Integer cantidadAplicados;

	@OneToMany(mappedBy = "idRegistroEconomico")
	private List<DetallePlanilla> detallePlanillaList;
	@JoinColumn(name = "tipo_registro", referencedColumnName = "tipo_registro")
	@ManyToOne
	private TipoRegistro tipoRegistro;
	@JoinColumn(name = "id_periodo_pago", referencedColumnName = "id_periodo_pago")
	@ManyToOne
	private PeriodoPago idPeriodoPago;

	@OneToMany(mappedBy = "idRegistroEconomico")
	private List<Gasto> gastoList;

	@OneToMany(mappedBy = "idRegistroEconomico")
	private List<Asistencia> asistenciaList;

	public RegistroEconomico() {
	}

	public RegistroEconomico(Integer idRegistroEconomico) {
		this.idRegistroEconomico = idRegistroEconomico;
	}

	public RegistroEconomico(Integer idRegistroEconomico, Double valor, String descripcion) {
		this.idRegistroEconomico = idRegistroEconomico;
		this.valor = valor;
		this.descripcion = descripcion;
	}

	public Integer getIdRegistroEconomico() {
		return idRegistroEconomico;
	}

	public void setIdRegistroEconomico(Integer idRegistroEconomico) {
		this.idRegistroEconomico = idRegistroEconomico;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

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

	public TipoRegistro getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(TipoRegistro tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public PeriodoPago getIdPeriodoPago() {
		return idPeriodoPago;
	}

	public void setIdPeriodoPago(PeriodoPago idPeriodoPago) {
		this.idPeriodoPago = idPeriodoPago;
	}

	public List<Gasto> getGastoList() {
		return gastoList;
	}

	public void setGastoList(List<Gasto> gastoList) {
		this.gastoList = gastoList;
	}

	public List<Asistencia> getAsistenciaList() {
		return asistenciaList;
	}

	public void setAsistenciaList(List<Asistencia> asistenciaList) {
		this.asistenciaList = asistenciaList;
	}

	public Integer getCantidadAplicados() {
		return cantidadAplicados;
	}

	public void setCantidadAplicados(Integer cantidadAplicados) {
		this.cantidadAplicados = cantidadAplicados;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idRegistroEconomico != null ? idRegistroEconomico.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof RegistroEconomico)) {
			return false;
		}
		RegistroEconomico other = (RegistroEconomico) object;
		if ((this.idRegistroEconomico == null && other.idRegistroEconomico != null) || (this.idRegistroEconomico != null && !this.idRegistroEconomico.equals(other.idRegistroEconomico))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.RegistroEconomico[ idRegistroEconomico=" + idRegistroEconomico + " ]";
	}

}
