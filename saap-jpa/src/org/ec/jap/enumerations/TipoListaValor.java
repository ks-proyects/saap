package org.ec.jap.enumerations;

import org.ec.jap.utilitario.Identificador;

public enum TipoListaValor implements Identificador<TipoListaValor> {
	F("Fijo"), NQ("NamedQuerry"), SQL("Sqlnativo"),ENUM("Enumeración");

	private String descipcion;

	private TipoListaValor(String descipcion) {
		this.descipcion = descipcion;
	}

	@Override
	public TipoListaValor getIdentificador() {
		return this;
	}

	@Override
	public String getDescripcion() {
		// TODO Auto-generated method stub
		return descipcion;
	}

}
