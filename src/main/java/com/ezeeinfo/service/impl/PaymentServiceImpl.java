package com.ezeeinfo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezeeinfo.dao.PaymentDAO;
import com.ezeeinfo.dto.PaymentDTO;
import com.ezeeinfo.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private PaymentDAO paymentDAO;

	@Override
	public List<PaymentDTO> getAllPayments(String namespaceCode) {
		return paymentDAO.getAllPayments(namespaceCode);
	}

	@Override
	public PaymentDTO getPaymentByCode(String code) {
		return paymentDAO.getPaymentByCode(code);
	}

}
