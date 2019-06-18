package com.diconium.microservice.controller.support;

import com.diconium.microservice.exception.CustomException;
import com.diconium.microservice.exception.ResourceNotFoundException;

public interface IAuthenticatedExecution {

	public abstract Object execute() throws ResourceNotFoundException, CustomException;

}
