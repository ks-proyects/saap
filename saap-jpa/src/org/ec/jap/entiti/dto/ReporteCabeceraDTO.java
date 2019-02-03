package org.ec.jap.entiti.dto;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Objeto de Transferendia de datos para el reporte de cabecera
 * 
 * @author Freddy G. Castillo C.
 * @version {@code 1.0}
 * 
 */
@Entity
public class ReporteCabeceraDTO {

	@Id
	private String pais;
	private String empresa;
	private String sistema;
	private String titulo;
	private String subtitulo;
	private Date fecha;
	private String texto01;
	private String texto02;
	private String texto03;
	private String texto04;
	private String texto05;
	private String texto06;
	private String texto07;
	private String texto08;
	private String texto09;
	private String texto10;
	private String texto11;
	private String texto12;
	private Double valorGeneral01;
	private Double valorGeneral02;

	public ReporteCabeceraDTO() {
		super();
	}

	public ReporteCabeceraDTO(String pais, String empresa, String sistema, String titulo, String subtitulo, Date fecha, String texto01, String texto02, Double valorGeneral01, Double valorGeneral02) {
		super();
		this.pais = pais;
		this.empresa = empresa;
		this.sistema = sistema;
		this.titulo = titulo;
		this.subtitulo = subtitulo;
		this.fecha = fecha;
		this.texto01 = texto01;
		this.texto02 = texto02;
		this.valorGeneral01 = valorGeneral01;
		this.valorGeneral02 = valorGeneral02;
	}

	/**
	 * Atributo pais
	 * 
	 * @return el pais
	 */
	public String getPais() {
		return pais;
	}

	/**
	 * El @param pais define pais
	 */
	public void setPais(String pais) {
		this.pais = pais;
	}

	/**
	 * Atributo empresa
	 * 
	 * @return el empresa
	 */
	public String getEmpresa() {
		return empresa;
	}

	/**
	 * El @param empresa define empresa
	 */
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	/**
	 * Atributo sistema
	 * 
	 * @return el sistema
	 */
	public String getSistema() {
		return sistema;
	}

	/**
	 * El @param sistema define sistema
	 */
	public void setSistema(String sistema) {
		this.sistema = sistema;
	}

	/**
	 * Atributo titulo
	 * 
	 * @return el titulo
	 */
	public String getTitulo() {
		return titulo;
	}

	/**
	 * El @param titulo define titulo
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	/**
	 * Atributo subtitulo
	 * 
	 * @return el subtitulo
	 */
	public String getSubtitulo() {
		return subtitulo;
	}

	/**
	 * El @param subtitulo define subtitulo
	 */
	public void setSubtitulo(String subtitulo) {
		this.subtitulo = subtitulo;
	}

	/**
	 * Atributo fecha
	 * 
	 * @return el fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * El @param fecha define fecha
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * Atributo texto01
	 * 
	 * @return el texto01
	 */
	public String getTexto01() {
		return texto01;
	}

	/**
	 * El @param texto01 define texto01
	 */
	public void setTexto01(String texto01) {
		this.texto01 = texto01;
	}

	/**
	 * Atributo texto02
	 * 
	 * @return el texto02
	 */
	public String getTexto02() {
		return texto02;
	}

	/**
	 * El @param texto02 define texto02
	 */
	public void setTexto02(String texto02) {
		this.texto02 = texto02;
	}

	/**
	 * Atributo texto03
	 * 
	 * @return el texto03
	 */
	public String getTexto03() {
		return texto03;
	}

	/**
	 * El @param texto03 define texto03
	 */
	public void setTexto03(String texto03) {
		this.texto03 = texto03;
	}

	/**
	 * Atributo texto04
	 * 
	 * @return el texto04
	 */
	public String getTexto04() {
		return texto04;
	}

	/**
	 * El @param texto04 define texto04
	 */
	public void setTexto04(String texto04) {
		this.texto04 = texto04;
	}

	/**
	 * Atributo texto05
	 * 
	 * @return el texto05
	 */
	public String getTexto05() {
		return texto05;
	}

	/**
	 * El @param texto05 define texto05
	 */
	public void setTexto05(String texto05) {
		this.texto05 = texto05;
	}

	/**
	 * Atributo texto06
	 * 
	 * @return el texto06
	 */
	public String getTexto06() {
		return texto06;
	}

	/**
	 * El @param texto06 define texto06
	 */
	public void setTexto06(String texto06) {
		this.texto06 = texto06;
	}

	/**
	 * Atributo texto07
	 * 
	 * @return el texto07
	 */
	public String getTexto07() {
		return texto07;
	}

	/**
	 * El @param texto07 define texto07
	 */
	public void setTexto07(String texto07) {
		this.texto07 = texto07;
	}

	/**
	 * Atributo texto08
	 * 
	 * @return el texto08
	 */
	public String getTexto08() {
		return texto08;
	}

	/**
	 * El @param texto08 define texto08
	 */
	public void setTexto08(String texto08) {
		this.texto08 = texto08;
	}

	/**
	 * Atributo texto09
	 * 
	 * @return el texto09
	 */
	public String getTexto09() {
		return texto09;
	}

	/**
	 * El @param texto09 define texto09
	 */
	public void setTexto09(String texto09) {
		this.texto09 = texto09;
	}

	/**
	 * Atributo texto10
	 * 
	 * @return el texto10
	 */
	public String getTexto10() {
		return texto10;
	}

	/**
	 * El @param texto10 define texto10
	 */
	public void setTexto10(String texto10) {
		this.texto10 = texto10;
	}

	/**
	 * Atributo texto11
	 * 
	 * @return el texto11
	 */
	public String getTexto11() {
		return texto11;
	}

	/**
	 * El @param texto11 define texto11
	 */
	public void setTexto11(String texto11) {
		this.texto11 = texto11;
	}

	/**
	 * Atributo texto12
	 * 
	 * @return el texto12
	 */
	public String getTexto12() {
		return texto12;
	}

	/**
	 * El @param texto12 define texto12
	 */
	public void setTexto12(String texto12) {
		this.texto12 = texto12;
	}

	/**
	 * Atributo valorGeneral01
	 * 
	 * @return el valorGeneral01
	 */
	public Double getValorGeneral01() {
		return valorGeneral01;
	}

	/**
	 * El @param valorGeneral01 define valorGeneral01
	 */
	public void setValorGeneral01(Double valorGeneral01) {
		this.valorGeneral01 = valorGeneral01;
	}

	/**
	 * Atributo valorGeneral02
	 * 
	 * @return el valorGeneral02
	 */
	public Double getValorGeneral02() {
		return valorGeneral02;
	}

	/**
	 * El @param valorGeneral02 define valorGeneral02
	 */
	public void setValorGeneral02(Double valorGeneral02) {
		this.valorGeneral02 = valorGeneral02;
	}

}