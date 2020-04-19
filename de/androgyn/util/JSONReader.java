package de.androgyn.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONReader {

	private JSONObject JSONData;
	private String path;
	private File file;
	private static HashMap<String, JSONReader> cachedData = new HashMap<String, JSONReader>();
	
	
	public JSONReader(String path) {
		try {
			file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String data = br.readLine();
			if (data == null) {
				JSONData = generateDefaultJSONObject();
				save();
			} else {
				JSONData = (JSONObject) new JSONParser().parse(data);
			}
			br.close();
			fr.close();
		} catch (IOException | ParseException ex) {
			ex.printStackTrace();
		}
		
	}
	
	public static JSONReader getJSONReader(String path) {
		if (cachedData.containsKey(path)) {
			return cachedData.get(path);
		}
		return new JSONReader(path);
	}
	
	public JSONObject getJSONData() {
		return JSONData;
	}
	
	public List<Object> getList(String path) {
		List<Object> list = new ArrayList<>();
		for (Object obj : (JSONArray)get(path,new JSONArray())) list.add(obj.toString());
		return (list);
	}
	
	public boolean getBoolean(String path, Boolean defaultvalue) {
		checkPath(path, defaultvalue);
		return Boolean.valueOf(JSONData.get(path).toString());
	}
	
	public int getInt(String path, int defaultvalue) {
		checkPath(path, defaultvalue);
		return Integer.valueOf(JSONData.get(path).toString());
	}
	
	public String getString(String path, String defaultvalue) {
		checkPath(path, defaultvalue);
		return String.valueOf(JSONData.get(path).toString());
	}

	public Double getDouble(String path, double defaultvalue) {
		checkPath(path, defaultvalue);
		return Double.valueOf(JSONData.get(path).toString());
	}
	
	public JSONObject getJSONObject(String path, JSONObject defaultvalue) {
		checkPath(path, defaultvalue);
		return (JSONObject) JSONData.get(path);
	}
	
	private void checkPath(String path, Object defaultvalue) {
		if (!JSONData.containsKey(path)) {
			if (defaultvalue != null) {
				JSONData.put(path, defaultvalue);
				save();
			}
		}
	}
	
	public void setList(String path, List<Object> list) {
		JSONArray array = new JSONArray();
		for (Object obj : list) {
			array.add(obj);
		}
		JSONData.put(path, array);
	}
	
	public void remove(String path) {
		JSONData.remove(path);
		save();
	}
	
	public void set(String path, Object value) {
		JSONData.put(path, value);
		save();
	}

	public Object get(String path, Object defaultvalue) {
		if (!JSONData.containsKey(path)) {
			JSONData.put(path, defaultvalue);
			save();
		}
		return JSONData.get(path);
	}
	
	public JSONObject generateDefaultJSONObject() {
		JSONObject data = new JSONObject();
		data.put("enabled", false);
		return data;
	}
	
	public void save() {
//		manager.getDataManager().set(path, encodeB64(JSONData.toJSONString()));
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(JSONData.toJSONString());
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
