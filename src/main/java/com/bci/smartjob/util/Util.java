package com.bci.smartjob.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Slf4j
public class Util {

	private Util() {
	}

	/**
	 * Method that allows obtaining headers from a request.
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public static Map<String, String> getHeaders(HttpServletRequest request) {
		Map<String, String> map = new HashMap<>();
		Enumeration headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}
		return map;
	}
	
	/**
	 * Method that allows obtaining headers from a response.
	 * 
	 * @param response
	 * @return
	 */
	public static Map<String, String> getHeaders(HttpServletResponse response) {
		Map<String, String> map = new HashMap<>();
		Collection<String> headerNames = response.getHeaderNames();
		if(!CollectionUtils.isEmpty(headerNames)) {
			for (Iterator<String> iterator = headerNames.iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				String value = response.getHeader(key);
				map.put(key, value);
			}
		}		
		return map;
	}
	
	/**
	 * Method that allows converting object to json.
	 * 
	 * @param o
	 * @return
	 */
    public static String objectToJson(Object o) {
        String jsonInString = null;
        try {
        	ObjectMapper mapperObj = new ObjectMapper();
            mapperObj.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            jsonInString = mapperObj.writeValueAsString(o);
        } catch (Exception e) {
        	log.error("Error en objectToJson: " + e.getMessage());
        }
        return jsonInString;
    }

    /**
	 * Method that allows converting json to object.
	 * 
	 * @param type
	 * @param jsonString
	 * @return
	 */
    public static <T> T jsonToObject(Class<T> type, String jsonString) {
        try {
        	ObjectMapper mapperObj = new ObjectMapper();
            mapperObj.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            return mapperObj.readValue(jsonString, type);
        } catch (Exception e) {
        	log.error("Error en jsonToObject: " + e.getMessage());
            return null;
        }
    }

	/**
	 * Method that allows converting an object to another object.
	 *
	 * @param type
	 * @param o
	 * @return
	 */
	public static <T> T objectToObject(Class<T> type, Object o) {
        try {
        	ObjectMapper mapperObj = new ObjectMapper();
            mapperObj.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapperObj.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
            mapperObj.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
            String jsonString = objectToJson(o);
            return mapperObj.readValue(jsonString, type);
        } catch (Exception e) {
        	log.error("Error en objectToObject: " + e.getMessage());
            return null;
        }
    }

	/**
	 * Method that allows converting map to object.
	 *
	 * @param type
	 * @param map
	 * @return
	 */
	public static <T> T mapToObject(Class<T> type, Map<?, ?> map) {
        try {
        	ObjectMapper mapperObj = new ObjectMapper();
            mapperObj.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            mapperObj.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            String jsonString = mapTojson(map);
            return mapperObj.readValue(jsonString, type);
        } catch (Exception e) {
        	log.error("Error en mapToObject: " + e.getMessage());
            return null;
        }
    }

	/**
	 * Method that allows converting map to json.
	 *
	 * @param map
	 * @return
	 */
	public static String mapTojson(Map<?, ?> map) {
        try {
        	ObjectMapper mapperObj = new ObjectMapper();
            mapperObj.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            return mapperObj.writeValueAsString(map);
        } catch (JsonProcessingException e) {
        	log.error("Error en mapTojson: " + e.getMessage());
            return null;
        }
    }

	/**
	 * Method that allows converting a map list to a list of objects.
	 *
	 * @param type
	 * @param list
	 * @return
	 */
	public static <T> List<T> listMapToListObject(Class<T> type, List<?> list) {
        List<T> listReponse = new ArrayList<>();
        try {
            if (list == null) {
                return listReponse;
            }
            ObjectMapper mapperObj = new ObjectMapper();
            mapperObj.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                    DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
            for (int i = 0; i < list.size(); i++) {
                Map<?, ?> m = (Map<?, ?>) list.get(i);
                T t = mapToObject(type, m);
                listReponse.add(t);
            }
        } catch (Exception e) {
        	log.error("Error en listMapToListObject: " + e.getMessage());
        }
        return listReponse;
    }

	/**
	 * Method that allows converting a list of objects to another list of objects.
	 *
	 * @param clazz
	 * @param list
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static <T> List<T> listObjectToListObject(Class clazz, List<?> list) {
		List<T> listReponse = new ArrayList<>();
		try {
			if (list == null) {
				return listReponse;
			}
			ObjectMapper mapperObj = new ObjectMapper();
			mapperObj.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
			mapperObj.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			mapperObj.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

			String listJson = mapperObj.writeValueAsString(list);
			TypeFactory t = TypeFactory.defaultInstance();
			listReponse = mapperObj.readValue(listJson, t.constructCollectionType(ArrayList.class, clazz));
		} catch (Exception e) {
			log.error("Error en listObjectToListObject: " + e.getMessage());
		}
		return listReponse;
	}

	/**
	 * Method that allows converting json string to list of objects.
	 *
	 * @param clazz
	 * @param listJson
	 * @return
	 */
    @SuppressWarnings("rawtypes")
	public static <T> List<T> jsonStringToListObject(Class clazz, String listJson) {
        List<T> listReponse = new ArrayList<>();
        try {
            if (listJson == null) {
                return listReponse;
            }
            ObjectMapper mapperObj = new ObjectMapper();
            mapperObj.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
            TypeFactory t = TypeFactory.defaultInstance();

            listReponse = mapperObj.readValue(listJson, t.constructCollectionType(ArrayList.class, clazz));
        } catch (Exception e) {
        	log.error("Error en jsonStringToListObject: " + e.getMessage());
        }
        return listReponse;
    }

	/**
	 * Method that allows formatting a decimal with 02 digit.
	 *
	 * @param amount
	 * @return
	 */
	public static BigDecimal formatTwoDecimals(BigDecimal amount) {
		return amount.setScale(2, RoundingMode.DOWN);
	}


	/**
	 * Generates a Universally Unique Identifier (UUID) as a String.
	 *
	 * @return A randomly generated UUID as a String.
	 */
	public static String generateUuid() {
		return UUID.randomUUID().toString();
	}

}
