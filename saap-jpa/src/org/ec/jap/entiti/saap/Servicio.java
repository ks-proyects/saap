/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ec.jap.entiti.saap;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import org.ec.jap.enumerations.TipoServicioEnum;

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "servicio")
@NamedQueries({
		@NamedQuery(name = "Servicio.findByActivoInactivo", query = "SELECT l FROM Servicio l inner join l.idUsuario u WHERE l.activo  IN ('SI') AND u NOT IN (SELECT asi.idUsuario FROM Asistencia asi WHERE asi.actividad = :actividad) AND u.estado IN ('ACT','INAC','EDI') ORDER BY u.nombres"),

		@NamedQuery(name = "Servicio.findAllByUser", query = "SELECT l FROM Servicio l inner join l.idUsuario u WHERE l.tipoServicio='AGUA_POTABLE' and UPPER(l.numero) like UPPER(CONCAT('%',:filtro,'%')) OR upper(u.cedula) LIKE upper(CONCAT('%',:filtro,'%'))  OR upper(CONCAT(u.nombres,u.apellidos)) LIKE upper(CONCAT('%',:filtro,'%')) ORDER BY cast(l.numero,int),u.nombres,u.apellidos"),
		@NamedQuery(name = "Servicio.findByUser", query = "SELECT l FROM Servicio l where l.idUsuario.idUsuario=:idUsuario order by l.fechaRegistro, l.numero"),
		@NamedQuery(name = "Servicio.findByUserAndType", query = "SELECT l FROM Servicio l where l.idUsuario.idUsuario=:idUsuario and l.tipoServicio=:tipoServicio order by l.fechaRegistro, l.numero"),
		@NamedQuery(name = "Servicio.findByUserActivo", query = "SELECT l FROM Servicio l where l.idUsuario=:idUsuario and l.activo='SI' and l not in ( select dt.idServicio from DetallePlanilla dt where dt.idCabeceraPlanilla.idUsuario=:idUsuario and  dt.idCabeceraPlanilla.idPeriodoPago=:idPeriodoPago)"),
		@NamedQuery(name = "Servicio.findOfUsuariosActivos", query = "SELECT l FROM Servicio l inner join l.idUsuario u WHERE u.estado IN ('ACT','EDI') and l.activo IN ('SI') ORDER BY l.numero"),
		@NamedQuery(name = "Servicio.findOfUsuariosActivosAndNotPeriodo", query = "SELECT l FROM Servicio l inner join l.idUsuario u WHERE u.estado IN ('ACT','EDI') AND l.activo IN ('SI') AND l NOT IN (SELECT le.idServicio FROM Lectura le WHERE le.idPeriodoPago=:idPeriodoPago and le.idServicio.idServicio=l.idServicio)"),
		@NamedQuery(name = "Servicio.findOfUsuariosActivosAndNotFactura", query = "SELECT l FROM Servicio l inner join l.idUsuario u WHERE u.estado IN ('ACT','EDI') AND l.activo IN ('SI') AND l NOT IN (SELECT cp.idServicio FROM CabeceraPlanilla cp WHERE cp.idPeriodoPago=:idPeriodoPago and cp.idServicio.idServicio=l.idServicio)") })
public class Servicio implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_servicio")
	private Integer idServicio;

	@Basic(optional = false)
	@NotNull(message = "El campo Número o Lote es obligatorio.")
	@Size(min = 1, max = 16,message = "El campo Número o Lote es obligatorio.")
	@Column(name = "numero")
	private String numero;

	@Size(max = 2147483647)
	@Column(name = "ubicacion")
	private String ubicacion;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "ll_estado")
	private String estado;

	@Column(name = "activo")
	private String activo;

	@Basic(optional = false)
	@NotNull
	@Column(name = "fecha_registro")
	@Temporal(TemporalType.DATE)
	private Date fechaRegistro;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idServicio")
	private List<Lectura> lecturaList;

	@JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
	@ManyToOne(optional = false)
	private Usuario idUsuario;

	@JoinColumn(name = "id_tarifa", referencedColumnName = "id_tarifa")
	@ManyToOne
	private Tarifa idTarifa;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_servicio")
	private TipoServicioEnum tipoServicio;

	@OneToMany(mappedBy = "idServicio")
	private List<CabeceraPlanilla> cabeceraPlanillaList;

	public Servicio() {
	}

	public Servicio(Integer idServicio) {
		this.idServicio = idServicio;
	}

	public Servicio(Integer idServicio, String numero, String estado, Date fechaRegistro) {
		this.idServicio = idServicio;
		this.numero = numero;
		this.estado = estado;
		this.fechaRegistro = fechaRegistro;
	}

	public Integer getIdServicio() {
		return idServicio;
	}

	public void setIdServicio(Integer idServicio) {
		this.idServicio = idServicio;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public List<Lectura> getLecturaList() {
		return lecturaList;
	}

	public void setLecturaList(List<Lectura> lecturaList) {
		this.lecturaList = lecturaList;
	}

	public Usuario getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Usuario idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Tarifa getIdTarifa() {
		return idTarifa;
	}

	public void setIdTarifa(Tarifa idTarifa) {
		this.idTarifa = idTarifa;
	}

	public List<CabeceraPlanilla> getCabeceraPlanillaList() {
		return cabeceraPlanillaList;
	}

	public void setCabeceraPlanillaList(List<CabeceraPlanilla> cabeceraPlanillaList) {
		this.cabeceraPlanillaList = cabeceraPlanillaList;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public TipoServicioEnum getTipoServicio() {
		return tipoServicio;
	}

	public void setTipoServicio(TipoServicioEnum tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idServicio != null ? idServicio.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Servicio)) {
			return false;
		}
		Servicio other = (Servicio) object;
		if ((this.idServicio == null && other.idServicio != null)
				|| (this.idServicio != null && !this.idServicio.equals(other.idServicio))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.Llave[ idServicio=" + idServicio + " ]";
	}
}
