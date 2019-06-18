package com.diconium.microservice.controller.support;

import com.diconium.microservice.exception.CustomException;
import com.diconium.microservice.exception.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class BaseController {

	public static Object runAuthenticated(IAuthenticatedExecution iAuthenticatedExecution)
		throws ResourceNotFoundException, CustomException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.isAuthenticated()) {
			return iAuthenticatedExecution.execute();
		}

		return null;
	}

}
