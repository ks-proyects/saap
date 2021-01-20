package org.ec.jap.entiti.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.ec.jap.utilitario.Utilitario;

@Entity
@NamedQueries({
		@NamedQuery(name = "ConsumoDTO.findConsumo", query = "SELECT new ConsumoDTO(pp.descripcion,count(d),sum(lec.metros3)+sum(lec.metros3Exceso),sum(d.valorTotal),sum(d.valorPagado),sum(lec.valorMetro3)/count(d)) FROM DetallePlanilla d inner join d.idCabeceraPlanilla cp inner join cp.idPeriodoPago pp inner join d.idLectura lec inner join cp.idLlave ll inner join ll.idUsuario.tarifa tar where lec.idPeriodoPago=pp and (pp.anio=? or ?=0) and (d.ordenStr=? OR d.ordenStr=?) and (tar.idTarifa=? or ?=0) group by pp.anio, pp.mes,pp.descripcion order by pp.anio,pp.mes"), })
public class ConsumoDTO {

	@Id
	private String descripcion;
	private Long numeroPagos;
	private Double consumoM3;
	private Double valorMetro3;
	private Double valorTotal;
	private Double valorRecaudado;
	@SuppressWarnings("unused")
	private Double valorPorCobra;

	private Double valorMetro3Facturado;

	public ConsumoDTO() {
		// TODO Auto-generated constructor stub
	}

	public ConsumoDTO(String descripcion, Long numeroPagos, Double consumoM3, Double valorTotal, Double valorRecaudado,
			Double valorMetro3) {
		super();
		this.descripcion = descripcion;
		this.numeroPagos = numeroPagos;
		this.consumoM3 = consumoM3;
		this.valorTotal = valorTotal;
		this.valorRecaudado = valorRecaudado;
		this.valorMetro3 = valorMetro3;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Long getNumeroPagos() {
		return numeroPagos;
	}

	public void setNumeroPagos(Long numeroPagos) {
		this.numeroPagos = numeroPagos;
	}

	public Double getConsumoM3() {
		return consumoM3;
	}

	public void setConsumoM3(Double consumoM3) {
		this.consumoM3 = consumoM3;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Double getValorRecaudado() {
		return valorRecaudado;
	}

	public void setValorRecaudado(Double valorRecaudado) {
		this.valorRecaudado = valorRecaudado;
	}

	public Double getValorPorCobra() {
		try {
			return Utilitario.redondear(valorTotal - valorRecaudado);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0.0;
	}

	public void setValorPorCobra(Double valorPorCobra) {
		this.valorPorCobra = valorPorCobra;
	}

	public Double getValorMetro3() {
		return valorMetro3;
	}

	public void setValorMetro3(Double valorMetro3) {
		this.valorMetro3 = valorMetro3;
	}

	public Double getValorMetro3Facturado() {
		try {
			System.out.println(descripcion);
			System.out.println(valorRecaudado);
			if (valorMetro3 != null && valorMetro3 != 0.0) {

				return Utilitario.redondear(valorRecaudado / valorMetro3);

			}
			valorMetro3Facturado = valorRecaudado;
			return valorMetro3Facturado;
		} catch (Exception e) {
		}
		return 0.0;
	}

	public void setValorMetro3Facturado(Double valorMetro3Facturado) {
		this.valorMetro3Facturado = valorMetro3Facturado;
	}

}
