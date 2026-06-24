package com.ezeeinfo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezeeinfo.dao.BrandDAO;
import com.ezeeinfo.dao.UserDAO;
import com.ezeeinfo.dto.BrandDTO;
import com.ezeeinfo.dto.UserDTO;
import com.ezeeinfo.exception.ServiceException;
import com.ezeeinfo.service.BrandService;
import com.ezeeinfo.util.SecurityUtil;

@Service
public class BrandServiceImpl implements BrandService {
	@Autowired
	BrandDAO brandDAO;
	@Autowired
	UserDAO userDAO;

	@Override
	public List<BrandDTO> getAllBrands(String namespaceCode) {
		// TODO Auto-generated method stub
		return brandDAO.getAllBrands(namespaceCode);
	}

	@Override
	public BrandDTO getBrandByCode(String code) {
		// TODO Auto-generated method stub
		return brandDAO.getBrandByCode(code);
	}

	@Override
	public BrandDTO update(BrandDTO brandDTO) {
		UserDTO loggedInUser = userDAO.getUser(SecurityUtil.getUserId());
		brandDTO.setUpdatedBy(loggedInUser);

		if (!loggedInUser.getNamespace().getCode().equalsIgnoreCase(brandDTO.getNamespace().getCode())) {
			throw new ServiceException("EXCEPTION 403: ONLY SAME NAMESPACE USER CAN SAVE/MODIFY BRAND");
		}

		if (loggedInUser.getRole().getId() != 1) {
			throw new ServiceException("EXCEPTION 403: ONLY ADMIN CAN SAVE/MODIFY BRAND");
		}
		return brandDAO.update(brandDTO);
	}

}
