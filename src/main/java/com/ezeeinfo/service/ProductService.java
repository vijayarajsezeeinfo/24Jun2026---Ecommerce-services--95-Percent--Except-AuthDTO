package com.ezeeinfo.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ezeeinfo.dto.ProductDTO;

public interface ProductService {

	List<ProductDTO> getAllProducts(String namespaceCode);

	ProductDTO getProductByCode(String code);

	ProductDTO update(ProductDTO productDTO,  HttpServletRequest request);

	List<ProductDTO> getProductsByNamePriceAndNamespace(String name, Double price, String namespaceCode);
}
