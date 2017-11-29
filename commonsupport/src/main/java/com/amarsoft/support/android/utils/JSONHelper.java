package com.amarsoft.support.android.utils;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * JSON助手类
 *
 */
public class JSONHelper {

	/**
	 * 获取key对应的Object
	 *
	 * @param jsonObject
	 * @param key
	 * @return
	 */
	public static Object getValue(JSONObject jsonObject, String key) {
		if (jsonObject == null || jsonObject.toString().trim().length() == 0
				|| key == null || key.trim().length() == 0)
			return null;
		if (!jsonObject.isNull(key)) {
			try {
				return jsonObject.get(key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 获取key对应的String值
	 *
	 * @param jsonObject
	 * @param key
	 * @return
	 */
	public static String getStringValue(JSONObject jsonObject, String key) {
		if (jsonObject == null || jsonObject.toString().trim().length() == 0
				|| key == null || key.trim().length() == 0)
			return "";
		String value;
		if (!jsonObject.isNull(key)) {
			try {
				value = jsonObject.get(key).toString().trim();
				if (value == null || value.equals("null"))
					value = "";
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				value = "";
			}
		} else
			value = "";

		return value;
	}
}
