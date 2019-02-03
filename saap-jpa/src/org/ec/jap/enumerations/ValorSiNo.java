package org.ec.jap.enumerations;

import org.ec.jap.utilitario.Identificador;

public enum ValorSiNo implements Identificador<ValorSiNo> {
	N("NO"), S("SI");

	private String descipcion;

	private ValorSiNo(String descipcion) {
		this.descipcion = descipcion;
	}

	@Override
	public ValorSiNo getIdentificador() {
		return this;
	}

	@Override
	public String getDescripcion() {
		return descipcion;
	}

}
