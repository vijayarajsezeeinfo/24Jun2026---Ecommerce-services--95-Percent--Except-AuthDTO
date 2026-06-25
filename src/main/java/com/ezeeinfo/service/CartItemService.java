package com.ezeeinfo.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ezeeinfo.dto.CartItemDTO;

public interface CartItemService {

	List<CartItemDTO> getAllCartItems(String namespaceCode);

	CartItemDTO getCartItemByCode(String code) ;

	CartItemDTO update(CartItemDTO cartItemDTO, HttpServletRequest request);

}