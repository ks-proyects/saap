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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.ec.jap.anotaciones.AuditoriaAnot;
import org.ec.jap.anotaciones.AuditoriaMethod;
import org.ec.jap.entiti.sistema.Auditoria;
import org.ec.jap.entiti.sistema.Comunidad;
import org.ec.jap.entiti.sistema.Filtro;
import org.ec.jap.entiti.sistema.UsuarioPerfil;
import org.ec.jap.entiti.sistema.UsuarioRol;

/**
 * 
 * @author Freddy G Castillo C
 */
@Entity
@Table(name = "usuario", schema = "saap")
@NamedQueries({ @NamedQuery(name = "Usuario.findByFilters", query = " SELECT u,(SELECT SUM(asi.numeroRayas) FROM Asistencia asi inner join asi.actividad act inner join act.tipoActividad tact inner join act.idPeriodoPago per WHERE asi.idUsuario=u AND ((act.actividad=:actividad OR :actividad=0) AND (tact.tipoActividad=:tipoActividad OR :tipoActividad=0)) AND (per.idPeriodoPago=:idPeriodoPago OR :idPeriodoPago=0) AND (per.anio=:anio OR :anio=0)) FROM Usuario u WHERE u.tipoUsuario=:tipoUsuario  ORDER BY upper(CONCAT(u.nombres,' ',u.apellidos)), u.fechaIngreso DESC"), @NamedQuery(name = "Usuario.findAllActivos", query = "SELECT u FROM Usuario u WHERE  u.estado='ACT'"),
		@NamedQuery(name = "Usuario.findByCedNom", query = " SELECT u FROM Usuario u WHERE u.tipoUsuario=:tipoUsuario AND (upper(u.cedula) LIKE upper(CONCAT('%',:filtro,'%'))  OR upper(CONCAT(u.nombres,' ',u.apellidos)) LIKE upper(CONCAT('%',:filtro,'%'))) ORDER BY upper(CONCAT(u.nombres,' ',u.apellidos)), u.fechaIngreso DESC"), @NamedQuery(name = "Usuario.findByCedulaAndId", query = "SELECT COUNT(u.idUsuario) FROM Usuario u WHERE u.cedula = :cedula and u.tipoUsuario=:tipoUsuario and u.idUsuario != :idUsuario"), @NamedQuery(name = "Usuario.findByCedula", query = "SELECT COUNT(u.idUsuario) FROM Usuario u WHERE u.cedula = :cedula and u.tipoUsuario=:tipoUsuario "),
		@NamedQuery(name = "Usuario.findByUsername", query = "SELECT u FROM Usuario u WHERE u.username = :username and u in (SELECT up.idUsuario FROM UsuarioPerfil up)"),
		@NamedQuery(name = "Usuario.findBySinLLave", query = "SELECT u FROM Usuario u WHERE u.poseeAlcant=true and u.cantAlcant > 0 and u not in (SELECT ll.idUsuario FROM Llave ll) ")})
@AuditoriaAnot(entityType = "USU")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Integer idUsuario;

	@Column(name = "tipo_usuario")
	private String tipoUsuario;

	@Basic(optional = false)
	@Size(min = 10, max = 10, message = "El campo CEDULA es obligatorio.")
	@Column(name = "cedula")
	private String cedula;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647, message = "El campo NOMBRES es obligatorio.")
	@Column(name = "nombres")
	private String nombres;

	@Size(min = 1, max = 2147483647, message = "El campo APELLIDOS es obligatorio.")
	@Column(name = "apellidos")
	private String apellidos;

	@Size(max = 2147483647)
	@Column(name = "username")
	private String username;

	@Size(max = 2147483647)
	@Column(name = "password")
	private String password;

	@Size(max = 2147483647)
	@Column(name = "direccion")
	private String direccion;

	@Basic(optional = false)
	@NotNull
	@Column(name = "edad")
	private Integer edad;

	@Basic(optional = false)
	@Size(min = 1, max = 2147483647)
	@Column(name = "sexo")
	private String sexo;

	@Size(max = 2147483647)
	@Column(name = "telefono1")
	private String telefono1;

	@Size(max = 2147483647)
	@Column(name = "telefono2")
	private String telefono2;

	@Size(max = 2147483647)
	@Column(name = "telefono3")
	private String telefono3;

	// @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
	// message="Invalid email")//if the field contains email address consider
	// using this annotation to enforce field validation
	// @Pattern(regexp = "[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}", message =
	// "El correo ingresado no es correcto")
	@Size(max = 2147483647)
	@Column(name = "email")
	private String email;

	@Basic(optional = false)
	@Size(min = 1, max = 2147483647)
	@Column(name = "u_estado")
	private String estado;

	@Basic(optional = false)
	@Size(min = 1, max = 2)
	@Column(name = "es_comunero")
	private String esComunero;

	@Column(name = "posee_alcant")
	private Boolean poseeAlcant;

	@Column(name = "cant_alcant")
	private Integer cantAlcant;

	@NotNull(message = "El campo FECHA NACIMIENTO es obligatorio.")
	@Column(name = "fecha_nacimiento")
	private Date fechaNacimiento;;

	@OneToMany(mappedBy = "idUsuario")
	private List<UsuarioPerfil> usuarioPerfilList;

	@JoinColumn(name = "id_estado_civil", referencedColumnName = "id_estado_civil")
	@ManyToOne
	private EstadoCivil idEstadoCivil;

	@JoinColumn(name = "id_comunidad", referencedColumnName = "id_comunidad")
	@ManyToOne(optional = false)
	private Comunidad idComunidad;

	@OneToMany(mappedBy = "idUsuario")
	private List<UsuarioRol> usuarioRolList;

	@OneToMany(mappedBy = "idUsuario")
	private List<Filtro> filtroList;

	@OneToMany(mappedBy = "idUsuario")
	private List<Representante> representanteList;

	@OneToMany(mappedBy = "idRepresentado")
	private List<Representante> representanteList1;

	@OneToMany(mappedBy = "idUsuario")
	private List<Llave> llaveList;

	@OneToMany(mappedBy = "idUsuario")
	private List<CabeceraPlanilla> facturaList;

	@OneToMany(mappedBy = "usuario")
	private List<Auditoria> auditoriaList;

	@OneToMany(mappedBy = "idUsuario")
	private List<Asistencia> asistenciaList;

	@Column(name = "fecha_ingreso")
	private Date fechaIngreso;

	@Transient
	private Double cantidadRayas;

	public Usuario() {
	}

	public List<CabeceraPlanilla> getFacturaList() {
		return facturaList;
	}

	public void setFacturaList(List<CabeceraPlanilla> facturaList) {
		this.facturaList = facturaList;
	}

	public Usuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	@AuditoriaMethod(isIdEntity = true, disabled = true)
	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	@AuditoriaMethod(name = "Cedula")
	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Integer getEdad() {
		return edad;
	}

	public void setEdad(Integer edad) {
		this.edad = edad;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getTelefono1() {
		return telefono1;
	}

	public void setTelefono1(String telefono1) {
		this.telefono1 = telefono1;
	}

	public String getTelefono2() {
		return telefono2;
	}

	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}

	public String getTelefono3() {
		return telefono3;
	}

	public void setTelefono3(String telefono3) {
		this.telefono3 = telefono3;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<UsuarioPerfil> getUsuarioPerfilList() {
		return usuarioPerfilList;
	}

	public void setUsuarioPerfilList(List<UsuarioPerfil> usuarioPerfilList) {
		this.usuarioPerfilList = usuarioPerfilList;
	}

	public EstadoCivil getIdEstadoCivil() {
		return idEstadoCivil;
	}

	public void setIdEstadoCivil(EstadoCivil idEstadoCivil) {
		this.idEstadoCivil = idEstadoCivil;
	}

	public Comunidad getIdComunidad() {
		return idComunidad;
	}

	public void setIdComunidad(Comunidad idComunidad) {
		this.idComunidad = idComunidad;
	}

	public List<UsuarioRol> getUsuarioRolList() {
		return usuarioRolList;
	}

	public void setUsuarioRolList(List<UsuarioRol> usuarioRolList) {
		this.usuarioRolList = usuarioRolList;
	}

	public List<Filtro> getFiltroList() {
		return filtroList;
	}

	public void setFiltroList(List<Filtro> filtroList) {
		this.filtroList = filtroList;
	}

	public List<Representante> getRepresentanteList() {
		return representanteList;
	}

	public void setRepresentanteList(List<Representante> representanteList) {
		this.representanteList = representanteList;
	}

	public List<Representante> getRepresentanteList1() {
		return representanteList1;
	}

	public void setRepresentanteList1(List<Representante> representanteList1) {
		this.representanteList1 = representanteList1;
	}

	public List<Llave> getLlaveList() {
		return llaveList;
	}

	public void setLlaveList(List<Llave> llaveList) {
		this.llaveList = llaveList;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getEsComunero() {
		return esComunero;
	}

	public void setEsComunero(String esComunero) {
		this.esComunero = esComunero;
	}

	public List<Auditoria> getAuditoriaList() {
		return auditoriaList;
	}

	public void setAuditoriaList(List<Auditoria> auditoriaList) {
		this.auditoriaList = auditoriaList;
	}

	public String getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public List<Asistencia> getAsistenciaList() {
		return asistenciaList;
	}

	public void setAsistenciaList(List<Asistencia> asistenciaList) {
		this.asistenciaList = asistenciaList;
	}

	public Double getCantidadRayas() {
		return cantidadRayas;
	}

	public void setCantidadRayas(Double cantidadRayas) {
		this.cantidadRayas = cantidadRayas;
	}

	public Boolean getPoseeAlcant() {
		return poseeAlcant;
	}

	public void setPoseeAlcant(Boolean poseeAlcant) {
		this.poseeAlcant = poseeAlcant;
	}

	public Integer getCantAlcant() {
		return cantAlcant;
	}

	public void setCantAlcant(Integer cantAlcant) {
		this.cantAlcant = cantAlcant;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idUsuario != null ? idUsuario.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Usuario)) {
			return false;
		}
		Usuario other = (Usuario) object;
		if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "org.ec.jap.entiti.Usuario[ idUsuario=" + idUsuario + " ]";
	}

}
