package org.ec.jap.enumerations;

import org.ec.jap.utilitario.Identificador;

public enum AccionType implements Identificador<AccionType> {
	I("Ingreso"), S("Salida");

	private String descipcion;

	private AccionType(String descipcion) {
		this.descipcion = descipcion;
	}

	@Override
	public AccionType getIdentificador() {
		return this;
	}

	@Override
	public String getDescripcion() {
		return descipcion;
	}

}
