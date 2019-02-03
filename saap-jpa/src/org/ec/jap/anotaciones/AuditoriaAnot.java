package org.ec.jap.anotaciones;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Clase que representa la auditoria de una clase
 * 
 * @author Freddy Castillo
 * @version {@code 1.0}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AuditoriaAnot {
	/**
	 * Atributo que representa el tipo de entidad
	 * 
	 * @return
	 */
	public String entityType();
}
