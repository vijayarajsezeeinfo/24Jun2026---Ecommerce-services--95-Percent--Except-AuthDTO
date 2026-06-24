package com.ezeeinfo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezeeinfo.dao.CartItemDAO;
import com.ezeeinfo.dao.UserDAO;
import com.ezeeinfo.dto.CartItemDTO;
import com.ezeeinfo.dto.UserDTO;
import com.ezeeinfo.exception.ServiceException;
import com.ezeeinfo.service.CartItemService;
import com.ezeeinfo.util.SecurityUtil;

@Service
public class CartItemServiceImpl implements CartItemService {

	@Autowired
	CartItemDAO cartItemDAO;
	@Autowired
	UserDAO userDAO;

	@Override
	public List<CartItemDTO> getAllCartItems(String namespaceCode) {
		return cartItemDAO.getAllCartItems(namespaceCode);
	}

	@Override
	public CartItemDTO getCartItemByCode(String code) {
		return cartItemDAO.getCartItemByCode(code);
	}

	@Override
	public CartItemDTO update(CartItemDTO cartItemDTO) {

		UserDTO loggedInUser = userDAO.getUser(SecurityUtil.getUserId());
		cartItemDTO.setUpdatedBy(loggedInUser);

		UserDTO cartOwningUser = userDAO.getUserByCode(cartItemDTO.getCart().getUser().getCode());

		if (loggedInUser.getId() != cartOwningUser.getId()) {
			throw new ServiceException("EXCEPTION 403: ONLY CART OWNER CAN ADD CART ITEM");
		}

		return cartItemDAO.update(cartItemDTO);
	}
}