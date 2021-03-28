package com.animalx.AnimalX.exeptions;

public class RegraNegocioException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public RegraNegocioException(String msg) {
		super(msg);
	}

}
