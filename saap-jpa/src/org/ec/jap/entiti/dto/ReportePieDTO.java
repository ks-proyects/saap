package org.ec.jap.entiti.dto;

/**
 * Objeto de Transferendia de datos pie de reporte
 * 
 * @author Freddy G. Castillo C.
 * @version {@code 1.0}
 * 
 */
public class ReportePieDTO {

	private String texto01;
	private String texto02;
	private String texto03;
	private Double valorGeneral01;
	private Double valorGeneral02;
	private Double valorGeneral03;

	public ReportePieDTO() {
		super();
	}

	public ReportePieDTO(String texto01, String texto02, String texto03, Double valorGeneral01, Double valorGeneral02, Double valorGeneral03) {
		super();
		this.texto01 = texto01;
		this.texto02 = texto02;
		this.texto03 = texto03;
		this.valorGeneral01 = valorGeneral01;
		this.valorGeneral02 = valorGeneral02;
		this.valorGeneral03 = valorGeneral03;
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

	/**
	 * Atributo valorGeneral03
	 * 
	 * @return el valorGeneral03
	 */
	public Double getValorGeneral03() {
		return valorGeneral03;
	}

	/**
	 * El @param valorGeneral03 define valorGeneral03
	 */
	public void setValorGeneral03(Double valorGeneral03) {
		this.valorGeneral03 = valorGeneral03;
	}

}
