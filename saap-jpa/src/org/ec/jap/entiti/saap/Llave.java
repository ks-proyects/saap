/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ec.jap.entiti.saap;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "llave")
@NamedQueries({
		@NamedQuery(name = "Llave.findByActivoInactivo", query = "SELECT l FROM Llave l inner join l.idUsuario u WHERE l.activo  IN ('SI') AND u NOT IN (SELECT asi.idUsuario FROM Asistencia asi WHERE asi.actividad = :actividad) AND u.estado IN ('ACT','INAC','EDI') ORDER BY u.nombres"),

		@NamedQuery(name = "Llave.findAllByUser", query = "SELECT l FROM Llave l inner join l.idUsuario u WHERE UPPER(l.numero) like UPPER(CONCAT('%',:filtro,'%')) OR upper(u.cedula) LIKE upper(CONCAT('%',:filtro,'%'))  OR upper(CONCAT(u.nombres,u.apellidos)) LIKE upper(CONCAT('%',:filtro,'%')) ORDER BY cast(l.numero,int),u.nombres,u.apellidos"),
		@NamedQuery(name = "Llave.findByUser", query = "SELECT l FROM Llave l where l.idUsuario.idUsuario=:idUsuario order by l.fechaRegistro, l.numero"),
		@NamedQuery(name = "Llave.findOfUsuariosActivos", query = "SELECT l FROM Llave l inner join l.idUsuario u WHERE u.estado IN ('ACT','EDI') and l.activo IN ('SI') ORDER BY l.numero"),
		@NamedQuery(name = "Llave.findOfUsuariosActivosAndNotPeriodo", query = "SELECT l FROM Llave l inner join l.idUsuario u WHERE u.estado IN ('ACT','EDI') AND l.activo IN ('SI') AND l NOT IN (SELECT le.idLlave FROM Lectura le WHERE le.idPeriodoPago=:idPeriodoPago and le.idLlave.idLlave=l.idLlave)"),
		@NamedQuery(name = "Llave.findOfUsuariosActivosAndNotFactura", query = "SELECT l FROM Llave l inner join l.idUsuario u WHERE u.estado IN ('ACT','EDI') AND l.activo IN ('SI') AND l NOT IN (SELECT cp.idLlave FROM CabeceraPlanilla cp WHERE cp.idPeriodoPago=:idPeriodoPago and cp.idLlave.idLlave=l.idLlave)") })
public class Llave implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_llave")
	private Integer idLlave;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
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
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idLlave")
	private List<Lectura> lecturaList;
	
	@JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
	@ManyToOne(optional = false)
	private Usuario idUsuario;
	
	@JoinColumn(name = "id_tarifa", referencedColumnName = "id_tarifa")
	@ManyToOne
	private Tarifa idTarifa;
	
	@OneToMany(mappedBy = "idLlave")
	private List<CabeceraPlanilla> cabeceraPlanillaList;

	public Llave() {
	}

	public Llave(Integer idLlave) {
		this.idLlave = idLlave;
	}

	public Llave(Integer idLlave, String numero, String estado, Date fechaRegistro) {
		this.idLlave = idLlave;
		this.numero = numero;
		this.estado = estado;
		this.fechaRegistro = fechaRegistro;
	}

	public Integer getIdLlave() {
		return idLlave;
	}

	public void setIdLlave(Integer idLlave) {
		this.idLlave = idLlave;
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

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idLlave != null ? idLlave.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Llave)) {
			return false;
		}
		Llave other = (Llave) object;
		if ((this.idLlave == null && other.idLlave != null)
				|| (this.idLlave != null && !this.idLlave.equals(other.idLlave))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.Llave[ idLlave=" + idLlave + " ]";
	}
}
