package com.ezeeinfo.service;

import java.util.List;

import com.ezeeinfo.dto.PaymentDTO;

public interface PaymentService {

	List<PaymentDTO> getAllPayments(String namespaceCode);

	PaymentDTO getPaymentByCode(String code);

}