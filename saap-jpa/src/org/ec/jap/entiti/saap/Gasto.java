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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Freddy
 */
@Entity
@Table(name = "gasto")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Gasto.findAllGastosAcum", query = "SELECT SUM(g.valor) FROM Gasto g inner join g.idRegistroEconomico ree inner join ree.tipoRegistro tr inner join g.idPeriodoPago pp WHERE tr=:tipoRegistro AND  (pp.anio<=:anio or :anio=0) AND (pp.mes<=:mes or :mes=-1)"),
		@NamedQuery(name = "Gasto.findAllGastos", query = "SELECT SUM(g.valor) FROM Gasto g inner join g.idRegistroEconomico ree inner join ree.tipoRegistro tr inner join g.idPeriodoPago pp WHERE tr=:tipoRegistro AND  (pp.anio=:anio or :anio=0) AND (pp.mes=:mes or :mes=-1)"),
		@NamedQuery(name = "Gasto.findAllByUser", query = "SELECT a FROM Gasto a inner join a.idPeriodoPago pp WHERE pp.idPeriodoPago=:idPeriodoPago OR :idPeriodoPago=0 ORDER BY a.fechaIngreso  "),
		@NamedQuery(name = "Gasto.findAll", query = "SELECT g FROM Gasto g"), @NamedQuery(name = "Gasto.findByIdGasto", query = "SELECT g FROM Gasto g WHERE g.idGasto = :idGasto"),
		@NamedQuery(name = "Gasto.findByDescripcion", query = "SELECT g FROM Gasto g WHERE g.descripcion = :descripcion"),
		@NamedQuery(name = "Gasto.findByValor", query = "SELECT g FROM Gasto g WHERE g.valor = :valor"),
		@NamedQuery(name = "Gasto.findByFechaIngreso", query = "SELECT g FROM Gasto g WHERE g.fechaIngreso = :fechaIngreso"),
		@NamedQuery(name = "Gasto.findByNumeroDocumento", query = "SELECT g FROM Gasto g WHERE g.numeroDocumento = :numeroDocumento") })
public class Gasto implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_gasto")
	private Integer idGasto;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2000)
	@Column(name = "descripcion")
	private String descripcion;

	@Basic(optional = false)
	@NotNull
	@Column(name = "valor")
	private Double valor;

	@Basic(optional = false)
	@NotNull
	@Column(name = "fecha_ingreso")
	private Date fechaIngreso;

	@Size(max = 128)
	@Column(name = "numero_documento")
	private String numeroDocumento;

	@JoinColumn(name = "id_periodo_pago", referencedColumnName = "id_periodo_pago")
	@ManyToOne(optional = false)
	private PeriodoPago idPeriodoPago;

	@JoinColumn(name = "id_destino", referencedColumnName = "id_destino")
	@ManyToOne(optional = false)
	private Destino idDestino;

	@JoinColumn(name = "id_registro_economico", referencedColumnName = "id_registro_economico")
	@ManyToOne
	private RegistroEconomico idRegistroEconomico;

	public Gasto() {
	}

	public Gasto(Integer idGasto) {
		this.idGasto = idGasto;
	}

	public Gasto(Integer idGasto, String descripcion, Double valor, Date fechaIngreso) {
		this.idGasto = idGasto;
		this.descripcion = descripcion;
		this.valor = valor;
		this.fechaIngreso = fechaIngreso;
	}

	public Integer getIdGasto() {
		return idGasto;
	}

	public void setIdGasto(Integer idGasto) {
		this.idGasto = idGasto;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public PeriodoPago getIdPeriodoPago() {
		return idPeriodoPago;
	}

	public void setIdPeriodoPago(PeriodoPago idPeriodoPago) {
		this.idPeriodoPago = idPeriodoPago;
	}

	public Destino getIdDestino() {
		return idDestino;
	}

	public void setIdDestino(Destino idDestino) {
		this.idDestino = idDestino;
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
		hash += (idGasto != null ? idGasto.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Gasto)) {
			return false;
		}
		Gasto other = (Gasto) object;
		if ((this.idGasto == null && other.idGasto != null) || (this.idGasto != null && !this.idGasto.equals(other.idGasto))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "uce.Gasto[ idGasto=" + idGasto + " ]";
	}

}
