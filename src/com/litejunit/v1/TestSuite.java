package com.litejunit.v1;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;


public class TestSuite extends Assert implements Test{

	private List<Test> tests = new ArrayList<>();
	
	private String name;
	
	public TestSuite() {
	}
	
	public TestSuite(final Class<?> theClass){
		this.name = theClass.getName();
		Constructor<?> constructor = null;
		try {
			constructor = getConstructure(theClass);
		} catch (NoSuchMethodException e) {
			addTest(warning("Class "+theClass.getName()+" has no public constructor TestCase(String name)"));
			return;
		}
		
		if (!Modifier.isPublic(theClass.getModifiers())) {
			addTest(warning("class " + theClass.getName() + " is not public"));
			return ;
		}
		
		Vector<String> names = new Vector<>();
		Method[] methods = theClass.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			addTestMethod(methods[i], names, constructor);
		}
	}
	
	private void addTestMethod(Method method, Vector<String> names, Constructor<?> constructor) {
		String name = method.getName();
		if (names.contains(name)) {
			return;
		}
		if (isPublicTestMethod(method)) {
			names.addElement(name);
			Object[] args = new Object[]{name};
			try {
				addTest((Test)constructor.newInstance(args));
			} catch (IllegalArgumentException | InstantiationException | IllegalAccessException
					| InvocationTargetException e) {
				addTest(warning("IllegalArgumentException | InstantiationException | IllegalAccessException"
						+ "| InvocationTargetException"));
				e.printStackTrace();
			}
		} else {
			if (isTestMethod(method)) {
				addTest(warning("Test method isn't public: "+method.getName()));
			}
		}
		
	}

	private boolean isPublicTestMethod(Method method) {
		return isTestMethod(method) && Modifier.isPublic(method.getModifiers());
	}

	/**
	 * 判断是否是test方法；
	 * test开头,返回值为空，没有参数；
	 * @param method
	 * @return
	 */
	private boolean isTestMethod(Method method) {
		String name = method.getName();
		Class<?>[] paraeters = method.getParameterTypes();
		Class<?> returnType = method.getReturnType();
		return paraeters.length == 0 && name.startsWith("test") && returnType.equals(Void.TYPE);
	}

	private Constructor<?> getConstructure(Class<?> theClass) throws  NoSuchMethodException {
		Class<?>[] args = {String.class};
		return theClass.getConstructor(args);
	}

	@Override
	public int countTestCases() {
		int count = 0;
		for (Iterator<Test> e = tests();e.hasNext(); ) {
			Test test = e.next();
			count = count + test.countTestCases();
		}
		return count;
	}

	public Iterator<Test> tests() {
		return tests.iterator();
	}

	@Override
	public void run(TestResult tr) {
		for (Iterator<Test> e= tests(); e.hasNext(); ) {
	  		if (tr.shouldStop() ){
	  			break;
	  		}
			Test test= (Test)e.next();
			test.run(tr);
		}
	}
	
	public void addTest(Test test) {
		tests.add(test);
	}

	private Test warning(final String message) {
		return new TestCase("warning") {
			public void doRun() {
				fail(message);
			}
		};
	}
}
