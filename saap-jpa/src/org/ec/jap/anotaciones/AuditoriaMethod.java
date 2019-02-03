package org.ec.jap.anotaciones;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Clase que representa una anotaci�n para un atributo y que se necesita
 * realizar la auditoria
 * 
 * @author Freddy Castillo
 * @version {@code 1.0}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuditoriaMethod {
	/**
	 * Se realiza la auditoria o no
	 * 
	 * @return {@code True} se realizara la auditoria, {@code False} no se
	 *         realizara
	 */
	public boolean disabled() default false;

	/**
	 * Nombre del atributo
	 * 
	 * @return Nombre del atributo, por omisi�n sera {@code default}
	 */
	public String name() default "default";

	/**
	 * Es clave primaria
	 * 
	 * @return {@code True} se realizara si es clave primaria, {@code False} no
	 *         lo es
	 */
	public boolean isIdEntity() default false;

	/**
	 * Es clave foranea
	 * 
	 * @return {@code True} se realizara si es clave foranea, {@code False} no
	 *         lo es
	 * @return
	 */
	public boolean isIdEntity1() default false;

	/**
	 * Es clave foranea
	 * 
	 * @return {@code True} se realizara si es clave foranea, {@code False} no
	 *         lo es
	 * @return
	 */
	public boolean isIdEntity2() default false;

	/**
	 * Es descripci�n primaria
	 * 
	 * @return {@code True} se realizara si es descripci�n primaria,
	 *         {@code False} no lo es
	 * @return
	 */
	public boolean isEntityDescription() default false;

	/**
	 * Es descripci�n foranea
	 * 
	 * @return {@code True} se realizara si es descripci�n foranea,
	 *         {@code False} no lo es
	 * @return
	 */
	public boolean isEntityDescription1() default false;

	/**
	 * Es descripci�n foranea
	 * 
	 * @return {@code True} se realizara si es descripci�n foranea,
	 *         {@code False} no lo es
	 * @return
	 */
	public boolean isEntityDescription2() default false;
}
