package com.ezeeinfo.util;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import com.ezeeinfo.dto.NamespaceDTO;
import com.ezeeinfo.dto.UserDTO;
import com.ezeeinfo.dto.enumeration.UserRoleEM;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtil {

	private static final String SECRET = "ezeeinfo-secret-key-ezeeinfo-secret-key";
	private static final Key KEY = new SecretKeySpec(SECRET.getBytes(), SignatureAlgorithm.HS256.getJcaName());

	public static Claims getClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token).getBody();
	}

	public static String generateToken(UserDTO userDTO) {
		return Jwts.builder().claim("userId", userDTO.getId()).claim("userCode", userDTO.getCode()).claim("userName", userDTO.getUsername()).claim("role", userDTO.getRole().getId()).claim("email", userDTO.getEmail()).claim("mobile", userDTO.getMobile()).claim("activeFlag", userDTO.getActiveFlag()).claim("namespaceId", userDTO.getNamespace().getId()).claim("namespaceCode", userDTO.getNamespace().getCode()).claim("namespaceName", userDTO.getNamespace().getName()).claim("namespaceActiveFlag", userDTO.getNamespace().getActiveFlag()).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + 86400000)).signWith(KEY).compact();

	}

	public static UserDTO getUserDTO(String token) {
		Claims claims = getClaims(token);

		NamespaceDTO namespaceDTO = new NamespaceDTO();

		UserDTO userDTO = new UserDTO();

		namespaceDTO.setId(Integer.valueOf(claims.get("namespaceId").toString()));
		namespaceDTO.setCode(claims.get("namespaceCode").toString());
		namespaceDTO.setName(claims.get("namespaceName").toString());
		namespaceDTO.setActiveFlag(Integer.valueOf(claims.get("namespaceActiveFlag").toString()));

		userDTO.setId(Integer.valueOf(claims.get("userId").toString()));
		userDTO.setCode(claims.get("userCode").toString());
		userDTO.setUsername(claims.get("userName").toString());
		userDTO.setRole(UserRoleEM.getUserRoleEM(Integer.valueOf(claims.get("role").toString())));
		userDTO.setEmail(claims.get("email").toString());
		userDTO.setMobile(claims.get("mobile").toString());
		userDTO.setActiveFlag(Integer.valueOf(claims.get("activeFlag").toString()));
		userDTO.setNamespace(namespaceDTO);
		return userDTO;
	}
}
