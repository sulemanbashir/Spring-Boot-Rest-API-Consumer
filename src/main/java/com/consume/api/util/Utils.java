package com.consume.api.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;

public class Utils {
	
	
	public static <E> E jsonToJavaObject(String json, Class<E> className){
		E javaClass = null;
		ObjectMapper  objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(Visibility.ANY));
		try {
			//System.out.print(json);
			javaClass = objectMapper.readValue(json, className);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return javaClass;
	}

}
