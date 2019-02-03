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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Freddy
 */
@Entity
@Table(name = "actividad")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Actividad.findAllByPeriodo", query = "SELECT a FROM Asistencia asi inner join asi.actividad a WHERE asi.asistio=false AND a.idPeriodoPago=:idPeriodoPago"),
		@NamedQuery(name = "Actividad.findAll", query = "SELECT a FROM Actividad a"),
		@NamedQuery(name = "Actividad.findByActividad", query = "SELECT a FROM Actividad a WHERE a.actividad = :actividad"),
		@NamedQuery(name = "Actividad.findByDescripcion", query = "SELECT a FROM Actividad a WHERE a.descripcion = :descripcion"),
		@NamedQuery(name = "Actividad.findByValorMulta", query = "SELECT a FROM Actividad a WHERE a.valorMulta = :valorMulta"),
		@NamedQuery(name = "Actividad.findByMaximoRayas", query = "SELECT a FROM Actividad a WHERE a.maximoRayas = :maximoRayas"),
		@NamedQuery(name = "Actividad.findByFechaEvento", query = "SELECT a FROM Actividad a WHERE a.fechaEvento = :fechaEvento"),
		@NamedQuery(name = "Actividad.findByActEstado", query = "SELECT a FROM Actividad a WHERE a.actEstado = :actEstado"),
		@NamedQuery(name = "Actividad.findAllByUser", query = "SELECT a FROM Actividad a inner join a.idPeriodoPago pp WHERE pp.idPeriodoPago=:idPeriodoPago OR :idPeriodoPago=0 ORDER BY a.fechaEvento  ") })
public class Actividad implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "actividad")
	private Integer actividad;

	@Basic(optional = false)
	@NotNull()
	@Size(min = 1, max = 2000, message = "La descripción es obligatoria.")
	@Column(name = "descripcion")
	private String descripcion;

	@Basic(optional = false)
	@NotNull(message = "El valor de la multa es obligatoria.")
	@Column(name = "valor_multa")
	private Double valorMulta;

	@Basic(optional = false)
	@NotNull(message = "El número de rayas por beneficiario es obligatorio.")
	@Column(name = "maximo_rayas")
	private Integer maximoRayas;

	@Basic(optional = false)
	@NotNull(message = "La fecha de la actividad es obligatoria.")
	@Column(name = "fecha_evento")
	private Date fechaEvento;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 32)
	@Column(name = "act_estado")
	private String actEstado;

	@JoinColumn(name = "tipo_actividad", referencedColumnName = "tipo_actividad")
	@ManyToOne
	private TipoActividad tipoActividad;

	@JoinColumn(name = "id_periodo_pago", referencedColumnName = "id_periodo_pago")
	@ManyToOne(optional = false)
	private PeriodoPago idPeriodoPago;

	@OneToMany(mappedBy = "actividad")
	private List<Asistencia> asistenciaList;

	public Actividad() {
	}

	public Actividad(Integer actividad) {
		this.actividad = actividad;
	}

	public Actividad(Integer actividad, String descripcion, Double valorMulta, int maximoRayas, Date fechaEvento, String actEstado) {
		this.actividad = actividad;
		this.descripcion = descripcion;
		this.valorMulta = valorMulta;
		this.maximoRayas = maximoRayas;
		this.fechaEvento = fechaEvento;
		this.actEstado = actEstado;
	}

	/**
	 * Atributo actividad
	 * 
	 * @return el valor del atributo actividad
	 */
	public Integer getActividad() {
		return actividad;
	}

	/**
	 * El @param actividad define actividad
	 */
	public void setActividad(Integer actividad) {
		this.actividad = actividad;
	}

	/**
	 * Atributo descripcion
	 * 
	 * @return el valor del atributo descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * El @param descripcion define descripcion
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * Atributo valorMulta
	 * 
	 * @return el valor del atributo valorMulta
	 */
	public Double getValorMulta() {
		return valorMulta;
	}

	/**
	 * El @param valorMulta define valorMulta
	 */
	public void setValorMulta(Double valorMulta) {
		this.valorMulta = valorMulta;
	}

	/**
	 * Atributo maximoRayas
	 * 
	 * @return el valor del atributo maximoRayas
	 */
	public Integer getMaximoRayas() {
		return maximoRayas;
	}

	/**
	 * El @param maximoRayas define maximoRayas
	 */
	public void setMaximoRayas(Integer maximoRayas) {
		this.maximoRayas = maximoRayas;
	}

	/**
	 * Atributo fechaEvento
	 * 
	 * @return el valor del atributo fechaEvento
	 */
	public Date getFechaEvento() {
		return fechaEvento;
	}

	/**
	 * El @param fechaEvento define fechaEvento
	 */
	public void setFechaEvento(Date fechaEvento) {
		this.fechaEvento = fechaEvento;
	}

	/**
	 * Atributo actEstado
	 * 
	 * @return el valor del atributo actEstado
	 */
	public String getActEstado() {
		return actEstado;
	}

	/**
	 * El @param actEstado define actEstado
	 */
	public void setActEstado(String actEstado) {
		this.actEstado = actEstado;
	}

	/**
	 * Atributo tipoActividad
	 * 
	 * @return el valor del atributo tipoActividad
	 */
	public TipoActividad getTipoActividad() {
		return tipoActividad;
	}

	/**
	 * El @param tipoActividad define tipoActividad
	 */
	public void setTipoActividad(TipoActividad tipoActividad) {
		this.tipoActividad = tipoActividad;
	}

	/**
	 * Atributo idPeriodoPago
	 * 
	 * @return el valor del atributo idPeriodoPago
	 */
	public PeriodoPago getIdPeriodoPago() {
		return idPeriodoPago;
	}

	/**
	 * El @param idPeriodoPago define idPeriodoPago
	 */
	public void setIdPeriodoPago(PeriodoPago idPeriodoPago) {
		this.idPeriodoPago = idPeriodoPago;
	}

	/**
	 * Atributo asistenciaList
	 * 
	 * @return el valor del atributo asistenciaList
	 */
	public List<Asistencia> getAsistenciaList() {
		return asistenciaList;
	}

	/**
	 * El @param asistenciaList define asistenciaList
	 */
	public void setAsistenciaList(List<Asistencia> asistenciaList) {
		this.asistenciaList = asistenciaList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (actividad != null ? actividad.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Actividad)) {
			return false;
		}
		Actividad other = (Actividad) object;
		if ((this.actividad == null && other.actividad != null) || (this.actividad != null && !this.actividad.equals(other.actividad))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "uce.Actividad[ actividad=" + actividad + " ]";
	}

}
