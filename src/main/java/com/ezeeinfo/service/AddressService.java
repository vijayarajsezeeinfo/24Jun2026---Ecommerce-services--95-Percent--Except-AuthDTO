package com.ezeeinfo.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ezeeinfo.dto.AddressDTO;

public interface AddressService {

	List<AddressDTO> getAllAddresses(String namespaceCode);

	AddressDTO getAddressByCode(String code);

	AddressDTO update(AddressDTO addressDTO, HttpServletRequest request);
}
