package org.ec.jap.enumerations;

import org.ec.jap.utilitario.Identificador;

public enum Formapago implements Identificador<Formapago> {
	A("Anterior"), N("Nuevo");

	private String descipcion;

	private Formapago(String descipcion) {
		this.descipcion = descipcion;
	}

	@Override
	public Formapago getIdentificador() {
		return this;
	}

	@Override
	public String getDescripcion() {
		return descipcion;
	}

}
