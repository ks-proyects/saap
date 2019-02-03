package org.ec.jap.enumerations;

import org.ec.jap.utilitario.Identificador;

public enum OutputType implements Identificador<OutputType> {
	PDF(".pdf"), XLS(".xls"), HTML(".html"), RTF(".rtf"), PRINT("print"), DOC(".doc");

	private String descipcion;

	private OutputType(String descipcion) {
		this.descipcion = descipcion;
	}

	@Override
	public OutputType getIdentificador() {
		return this;
	}

	@Override
	public String getDescripcion() {
		return descipcion;
	}

}
