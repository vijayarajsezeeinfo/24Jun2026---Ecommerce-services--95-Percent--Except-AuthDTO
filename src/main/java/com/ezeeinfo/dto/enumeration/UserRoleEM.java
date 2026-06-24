package com.ezeeinfo.dto.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum UserRoleEM {

	SUPER_ADMIN("SUPA", 0, "Super Admin"), ADMIN("ADMN", 1, "Admin"), SELLER("SELL", 2, "Seller"), BUYER("BUYR", 3, "Buyer");

	private final int id;
	private final String code;
	private final String name;

	UserRoleEM(String code, int id, String name) {
		this.code = code;
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	@JsonCreator
	public static UserRoleEM getUserRoleEM(int id) {
		UserRoleEM[] values = values();
		UserRoleEM roleEM = null;
		for (UserRoleEM role : values) {
			if (role.id == id) {
				roleEM = role;
			}
		}
		return roleEM;
	}

	public static UserRoleEM getUserRoleEM(String code) {
		UserRoleEM roleEM = null;
		for (UserRoleEM role : values()) {
			if (role.getCode().equalsIgnoreCase(code)) {
				roleEM = role;
			}
		}
		return roleEM;
	}
}