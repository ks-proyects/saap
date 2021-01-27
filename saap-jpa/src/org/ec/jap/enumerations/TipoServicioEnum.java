package org.ec.jap.enumerations;

import org.ec.jap.utilitario.Identificador;

public enum TipoServicioEnum implements Identificador<TipoServicioEnum> {
	AGUA_POTABLE("Agua potable"), ALCANTARILLADO("Alcantarillado"), AGUA_RIEGO("Agua de Riego");

	private String descipcion;

	private TipoServicioEnum(String descipcion) {
		this.descipcion = descipcion;
	}

	@Override
	public TipoServicioEnum getIdentificador() {
		return this;
	}

	@Override
	public String getDescripcion() {
		return descipcion;
	}

}
