package com.movies.model;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

import com.movies.model.error.ApiException;

class MyValidator {

	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	public static void validate(Object obj) throws ConstraintViolationException {

		final Set<ConstraintViolation<Object>> violations = validator.validate(obj);
		if (!violations.isEmpty()) {
			for (final ConstraintViolation<?> error : violations) {
				throw new ApiException(error.getMessage());

			}
		}
	}
}
