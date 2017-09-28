package com.litejunit.v1;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 单个测试用例；；
 * @author wangyj
 *
 */
public abstract class TestCase extends Assert implements Test {

	private String name;
	
	public TestCase(String name) {
		this.name = name;
	}
	
	
	@Override
	public int countTestCases() {
		return 1;
	}

	@Override
	public void run(TestResult tr) {
		tr.run(this);
	}
	
	protected void setUp() {
		
	}
	
	protected void tearDown() {
		
	}
	
	protected void runTest() throws Throwable {
		Method runMethod = null;
		try {
			runMethod = getClass().getMethod(name, null);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		if (!Modifier.isPublic(runMethod.getModifiers())) {
			fail("Method \"" + name + "\" should be public ");
		}
		try {
			runMethod.invoke(this, new Class[0]);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw e;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw e;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw e.getTargetException();
		}
	}
	
	public void doRun() throws Throwable{
		setUp();
		try{
			runTest();
		} 
		finally{
			tearDown();
		}		
	}

}
