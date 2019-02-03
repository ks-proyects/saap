package org.ec.jap.bo.saap.impl;

import java.util.Calendar;
import java.util.HashMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.ec.jap.bo.saap.AsientoBO;
import org.ec.jap.bo.saap.CuentaBO;
import org.ec.jap.bo.saap.LibroDiarioBO;
import org.ec.jap.dao.saap.impl.CuentaDAOImpl;
import org.ec.jap.entiti.saap.Asiento;
import org.ec.jap.entiti.saap.Cuenta;
import org.ec.jap.entiti.saap.LibroDiario;
import org.ec.jap.entiti.saap.Usuario;

/**
 * Session Bean implementation class DatAuditoriaBO
 */
@Stateless
public class CuentaBOImpl extends CuentaDAOImpl implements CuentaBO {

	@EJB
	CuentaBO cuentaBO;
	@EJB
	AsientoBO asientoBO;
	@EJB
	LibroDiarioBO libroDiarioBO;

	@Override
	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public void registrarAsiento(String numCuentaDebe, String descDeb, String numCuentaHaber, String descHaber, Double monto, Usuario usuario) throws Exception {
		// REGISTRAMOS EN EL LIBRO DIARIO DEL PRESENTE ANIO UN INGRESO POR
		// CONCEPTO DE PLANILLA DE PAGO
		Integer anio = Calendar.getInstance().get(Calendar.YEAR);
		HashMap<String, Object> p = new HashMap<>();
		p.put("numCuenta", numCuentaDebe);
		p.put("anioEjercicio", anio);
		Cuenta cuentaDebe = cuentaBO.findByNamedQuery("Cuenta.findByAnioAndNumCuenta", p);

		p = new HashMap<>();
		p.put("numCuenta", numCuentaHaber);
		p.put("anioEjercicio", anio);
		Cuenta cuentaHaber = cuentaBO.findByNamedQuery("Cuenta.findByAnioAndNumCuenta", p);
		if (cuentaDebe == null) {
			cuentaDebe = new Cuenta();
			cuentaDebe.setAnioEjercicio(anio);
			cuentaDebe.setDebe(0.0);
			cuentaDebe.setDescripcion(descDeb);
			cuentaDebe.setHaber(0.0);
			cuentaDebe.setSaldo(0.0);
			cuentaDebe.setNumCuenta(numCuentaDebe);
			cuentaBO.save(usuario, cuentaDebe);
		}
		if (cuentaHaber == null) {
			cuentaHaber = new Cuenta();
			cuentaHaber.setAnioEjercicio(anio);
			cuentaHaber.setDebe(0.0);
			cuentaHaber.setDescripcion(descHaber);
			cuentaHaber.setHaber(0.0);
			cuentaHaber.setSaldo(0.0);
			cuentaHaber.setNumCuenta(numCuentaHaber);
			cuentaBO.save(usuario, cuentaHaber);
		}

		p = new HashMap<>();
		p.put("anioEjercicio", anio);
		Integer bigDecimal = (Integer) asientoBO.findObjectByNamedQuery("Asiento.findAnioByPeridodFiscal", p);
		Integer numero = (bigDecimal != null ? bigDecimal.intValue() : 0);
		Asiento asiento = new Asiento();
		asiento.setFecha(Calendar.getInstance().getTime());
		asiento.setNumero(numero + 1);
		asientoBO.save(usuario, asiento);

		LibroDiario libroDiarioCaja = new LibroDiario();
		libroDiarioCaja.setDebe(cuentaDebe.getDebe() + monto);
		libroDiarioCaja.setMesEjercicio(Calendar.getInstance().get(Calendar.MONTH));
		libroDiarioCaja.setHaber(0.0);
		libroDiarioCaja.setDescripcion(cuentaDebe.getDescripcion());
		libroDiarioCaja.setIdAsiento(asiento);
		libroDiarioCaja.setIdCuenta(cuentaDebe);

		LibroDiario libroDiarioServicio = new LibroDiario();
		libroDiarioServicio.setDebe(0.0);
		libroDiarioServicio.setMesEjercicio(Calendar.getInstance().get(Calendar.MONTH));
		libroDiarioServicio.setHaber(cuentaDebe.getHaber() + monto);
		libroDiarioServicio.setDescripcion(cuentaHaber.getDescripcion());
		libroDiarioServicio.setIdAsiento(asiento);
		libroDiarioServicio.setIdCuenta(cuentaHaber);

		libroDiarioBO.save(usuario, libroDiarioCaja);
		libroDiarioBO.save(usuario, libroDiarioServicio);

		cuentaDebe.setDebe(cuentaDebe.getDebe() + monto);

		cuentaHaber.setHaber(cuentaHaber.getHaber() + monto);

		cuentaBO.update(usuario, cuentaHaber);
		cuentaBO.update(usuario, cuentaHaber);

	}
}
