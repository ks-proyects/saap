package org.ec.jap.enumerations;

import org.ec.jap.utilitario.Identificador;

public enum EpocaEnum implements Identificador<EpocaEnum> {
	VERANO("Verano"), INVIERNO("Invierno");

	private String descipcion;

	private EpocaEnum(String descipcion) {
		this.descipcion = descipcion;
	}

	@Override
	public EpocaEnum getIdentificador() {
		return this;
	}

	@Override
	public String getDescripcion() {
		return descipcion;
	}

}
