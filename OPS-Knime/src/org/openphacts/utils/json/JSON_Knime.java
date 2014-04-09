package org.openphacts.utils.json;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Vector;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class JSON_Knime {

	
	
	private static LinkedHashMap<String, String> params = null;
	private static Vector<LinkedHashMap<String,String>> results = new Vector<LinkedHashMap<String,String>>();
	
	
	public static void doIt(URL url, LinkedHashMap<String,String> params){
		
		try {
			JSONObject jo = grabSomeJson(url);
			recursiveMatch(jo,new LinkedHashMap<String,String>(),"");
			
			for(int i=0;i<results.size();i++){
				Iterator<String> it = results.get(i).keySet().iterator();
				while (it.hasNext()){
					String item = it.next();
					System.out.println( item+", "+ results.get(i).get(item));
				}
				//System.out.println("row "+i+" : ");
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static void recursiveMatch(Object json, LinkedHashMap<String,String> currentRow, String path){
		String objType = json.getClass().getName();
		
		if(objType.equals("net.sf.json.JSONObject")) {
			JSONObject jo = (JSONObject)json;
			Iterator<String> it = jo.keys(); //assuming that keys are ALWAYS strings (seems so looking at json specs)
			while (it.hasNext()){
				String currentKey = it.next();
				//path +=".."+currentKey;
				String newPath = path + ".." + currentKey;
				recursiveMatch(jo.get(currentKey), currentRow,newPath);
			}		
		} 
		else if (objType.equals("net.sf.json.JSONArray")) {
			JSONArray ja = (JSONArray)json;
			for (int i=0;i<ja.size();i++){
				String valType = ja.get(i).getClass().getName();
				if(valType.equals("net.sf.json.JSONObject")){
					recursiveMatch(ja.get(i),currentRow,path);
				}else {
					
					
					LinkedHashMap<String,String> newRow  = new LinkedHashMap<String,String>();
					//newRow.putAll(currentRow);
					newRow.put(path, ja.getString(i));
					results.add(newRow);
				}
				
			}
		}
		else {
			currentRow.put(path, json.toString());
			LinkedHashMap<String,String> newRow  = new LinkedHashMap<String,String>();
			newRow.putAll(currentRow);
			//newRow.put(path, json.toString());
			
			results.add(newRow);
		}
	}
    private static JSONObject grabSomeJson(URL url) throws IOException
    {
    	String str="";
    	BufferedReader in = new BufferedReader(
    	new InputStreamReader(url.openStream()));

    	String inputLine;

    	while ((inputLine = in.readLine()) != null)
    	 str+=inputLine+"\n";
    	in.close(); 
    	
    	//System.out.println(str);
    	JSONObject jo = (JSONObject) JSONSerializer.toJSON( str);
    	
    	
    	return jo;

    }
    
    
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//JSON_Knime jk = new JSON_Knime();
		LinkedHashMap<String, String> params = new LinkedHashMap<String,String>();
		params.put("primaryTopic.._about", "exactMatch");
		params.put("primaryTopic..exactMatch.._about", "exactMatch");
		params.put("primaryTopic..exactMatch.._about", "exactMatch");
		try{
			JSON_Knime.doIt(
				new URL(
					"https://beta.openphacts.org/1.3/target?uri=http%3A%2F%2Fidentifiers.org%2Fncbigene%2F6256&app_id=15a18100&app_key=528a8272f1cd961d215f318a0315dd3d"
						),params);
		}catch(Exception e){
			
		}
		// TODO Auto-generated method stub

	}

}
