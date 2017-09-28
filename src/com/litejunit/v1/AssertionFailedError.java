package com.litejunit.v1;

/**
 * assertion failed断言失败；
 * 
 * @author wangyj
 *
 */
public class AssertionFailedError extends Error {

	public AssertionFailedError() {

	}

	public AssertionFailedError(String message) {
		super(message);
	}
}
