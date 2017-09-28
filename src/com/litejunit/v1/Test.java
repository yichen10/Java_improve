package com.litejunit.v1;

/**
 * 测试类接口
 * @author wangyj
 *
 */
public interface Test {

	public abstract int countTestCases();
	
	public void run(TestResult tr);
}
