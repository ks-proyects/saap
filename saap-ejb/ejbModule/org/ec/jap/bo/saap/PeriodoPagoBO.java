package org.ec.jap.bo.saap;

import javax.ejb.Local;

import org.ec.jap.dao.saap.PeriodoPagoDAO;
import org.ec.jap.entiti.saap.PeriodoPago;

@Local
public interface PeriodoPagoBO extends PeriodoPagoDAO {
	
	PeriodoPago findByIdCustom(Integer idPeriodo)throws Exception;
}
