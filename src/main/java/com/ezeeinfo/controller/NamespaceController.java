package com.ezeeinfo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ezeeinfo.controller.io.NamespaceIO;
import com.ezeeinfo.dto.NamespaceDTO;
import com.ezeeinfo.service.NamespaceService;

@RestController
@RequestMapping("/namespace")
public class NamespaceController {

	@Autowired
	NamespaceService namespaceService;

	@RequestMapping(method = RequestMethod.GET)
	public List<NamespaceIO> getAllNamespaces() {
		return namespaceService.getAllNamespaces().stream().map(dto -> namespaceDTOToIO(dto)).toList();
	}

	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	public NamespaceIO getNamespaceByCode(@PathVariable("code") String code) {
		return namespaceDTOToIO(namespaceService.getNamespaceByCode(code));
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public NamespaceIO update(@RequestBody NamespaceIO namespaceIO, HttpServletRequest request) {
		NamespaceDTO namespaceDTO = namespaceService.update(namespaceIOToDTO(namespaceIO), request);
		return namespaceDTOToIO(namespaceDTO);
	}

	public NamespaceIO namespaceDTOToIO(NamespaceDTO namespaceDTO) {
		NamespaceIO namespaceIO = new NamespaceIO();
		namespaceIO.setCode(namespaceDTO.getCode());
		namespaceIO.setName(namespaceDTO.getName());
		namespaceIO.setActiveFlag(namespaceDTO.getActiveFlag());

		return namespaceIO;
	}

	public NamespaceDTO namespaceIOToDTO(NamespaceIO namespaceIO) {
		NamespaceDTO namespaceDTO = new NamespaceDTO();
		namespaceDTO.setCode(namespaceIO.getCode());
		namespaceDTO.setName(namespaceIO.getName());
		namespaceDTO.setActiveFlag(namespaceIO.getActiveFlag());

		return namespaceDTO;
	}
}
