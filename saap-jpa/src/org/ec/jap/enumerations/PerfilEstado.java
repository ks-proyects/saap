package org.ec.jap.enumerations;

import org.ec.jap.utilitario.Identificador;

public enum PerfilEstado implements Identificador<PerfilEstado> {
	S("SI"), N("NO");

	private String descipcion;

	private PerfilEstado(String descipcion) {
		this.descipcion = descipcion;
	}

	@Override
	public PerfilEstado getIdentificador() {
		return this;
	}

	@Override
	public String getDescripcion() {
		return descipcion;
	}

}
