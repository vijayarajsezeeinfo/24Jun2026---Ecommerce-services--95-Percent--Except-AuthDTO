package com.ezeeinfo.util;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component

public class JwtFilter extends OncePerRequestFilter {

	private static final Logger LOG = LoggerFactory.getLogger(JwtFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		try {
			String auth = request.getHeader("Authorization");
			LOG.info("Authorization Header : {}", auth);
			if (auth != null && auth.startsWith("Bearer ")) {
				String token = auth.substring(7);
				token = token.replace("\"", "");
				Integer userId = JwtUtil.getUserId(token);
				LOG.info("User Id From JWT : {}", userId);
				SecurityUtil.setUserId(userId);
			}
			filterChain.doFilter(request, response);
		}
		finally {
			SecurityUtil.clear();
		}
	}

}
