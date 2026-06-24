package com.ezeeinfo.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezeeinfo.dao.AddressDAO;
import com.ezeeinfo.dao.UserDAO;
import com.ezeeinfo.dto.AddressDTO;
import com.ezeeinfo.dto.UserDTO;
import com.ezeeinfo.exception.ServiceException;
import com.ezeeinfo.service.AddressService;
import com.ezeeinfo.util.SecurityUtil;

@Service

public class AddressServiceImpl implements AddressService {
	@Autowired
	AddressDAO addressDAO;
	@Autowired
	UserDAO userDAO;

	private static final Logger LOG = LoggerFactory.getLogger(AddressServiceImpl.class);

	@Override
	public List<AddressDTO> getAllAddresses(String namespaceCode) {
		// TODO Auto-generated method stub
		LOG.info("{}", addressDAO.getAllAddresses(namespaceCode));
		return addressDAO.getAllAddresses(namespaceCode);
	}

	@Override
	public AddressDTO getAddressByCode(String code) {
		// TODO Auto-generated method stub
		LOG.info("{}", addressDAO.getAddressByCode(code));
		return addressDAO.getAddressByCode(code);
	}

	@Override
	public AddressDTO update(AddressDTO addressDTO) {
		LOG.info("Input Address : {}", addressDTO);
		UserDTO loggedInUser = userDAO.getUser(SecurityUtil.getUserId());
		addressDTO.setUpdatedBy(loggedInUser);
		UserDTO addressOwningUser = userDAO.getUserByCode(addressDTO.getUser().getCode());

		if (!loggedInUser.getNamespace().getCode().equalsIgnoreCase(addressDTO.getNamespace().getCode())) {
			throw new ServiceException("EXCEPTION 403: ONLY SAME NAMESPACE USER CAN SAVE/MODIFY THE USER ADDRESS");
		}

		if (!loggedInUser.getCode().equalsIgnoreCase(addressOwningUser.getCode()) || loggedInUser.getRole().getId() != 1) {
			throw new ServiceException("EXCEPTION 403: ONLY USER OR ADMIN CAN SAVE/MODIFY THE USER ADDRESS");
		}

		return addressDAO.update(addressDTO);
	}

}
