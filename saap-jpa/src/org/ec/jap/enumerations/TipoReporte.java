package org.ec.jap.enumerations;

import org.ec.jap.utilitario.Identificador;

public enum TipoReporte implements Identificador<TipoReporte> {
	DIN("Dinamico"), STATIC("Estatico");

	private String descipcion;

	private TipoReporte(String descipcion) {
		this.descipcion = descipcion;
	}

	@Override
	public TipoReporte getIdentificador() {
		return this;
	}

	@Override
	public String getDescripcion() {
		return descipcion;
	}

}
