package org.ec.jap.enumerations;

import org.ec.jap.utilitario.Identificador;

public enum EstadoCabeceraPlanilla implements Identificador<EstadoCabeceraPlanilla> {
	ING("Ingresado"), S("Salida");

	private String descipcion;

	private EstadoCabeceraPlanilla(String descipcion) {
		this.descipcion = descipcion;
	}

	@Override
	public EstadoCabeceraPlanilla getIdentificador() {
		return this;
	}

	@Override
	public String getDescripcion() {
		return descipcion;
	}

}
