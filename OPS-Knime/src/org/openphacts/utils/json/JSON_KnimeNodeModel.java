package org.openphacts.utils.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;


/**
 * This is the model implementation of JSON_Knime.
 * Reads a JSON string, converts its hierarchical structure into a flat 2D matrix and exports it as a Knime table. 
 *
 * @author Ronald Siebes - VU Amsterdam
 */
public class JSON_KnimeNodeModel extends NodeModel {
	
	 public static final String TYPE_DEFAULT = "URL";
	public static final String TYPE = "type";
	private static int rowIndex =0;
	private final SettingsModelString type_settings = new SettingsModelString(TYPE, TYPE_DEFAULT);

    /**
     * Constructor for the node model.
     */
    protected JSON_KnimeNodeModel() {
    
        // TODO: Specify the amount of input and output ports needed.
        super(1, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec)  {
    	JSONObject json=null;
    	if(type_settings.getStringValue().equals("URL")){
	        String compoundUri= inData[0].iterator().next().getCell(0).toString(); // ugly...needs definitely some array bound checks here
	
	        URL requestURL;
			try {
				requestURL = buildRequestURL(compoundUri);
				json = JSON_KnimeNodeModel.grabSomeJson(requestURL);
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				DataColumnSpec[] allColSpecs = new DataColumnSpec[0];
	        	DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
	   		 BufferedDataContainer container = exec.createDataContainer(outputSpec);
	   		 container.close();
	        	return new BufferedDataTable[]{container.getTable()};
			}
	       
	        
    	}
        BufferedDataTable out = makeTable(exec,json);
        return new BufferedDataTable[]{out};
        // TODO: Return a BufferedDataTable for each output port 
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {

        // TODO: generated method stub
        return new DataTableSpec[]{null};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
         // TODO: generated method stub
    	type_settings.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	type_settings.loadSettingsFrom(settings);
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	type_settings.validateSettings(settings);
        // TODO: generated method stub
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        // TODO: generated method stub
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        // TODO: generated method stub
    }
    
    protected static BufferedDataTable makeTable(ExecutionContext exec, JSONObject wholeObject){
		rowIndex =0;
		Map<String,Map<String,String>> resultTable = new LinkedHashMap<String,Map<String,String>>();
	//	System.out.println("Start");
		dim(resultTable,"_root",wholeObject); //recursion start
		DataColumnSpec[] allColSpecs = new DataColumnSpec[resultTable.size()];

			
	        // the execution context will provide us with storage capacity, in this
	        // case a data container to which we will add rows sequentially
	        // Note, this container can also handle arbitrary big data tables, it
	        // will buffer to disc if necessary.
	       
		Iterator<String> colNames = resultTable.keySet().iterator();
		int counter = 0;
		while(colNames.hasNext()){
			String colName= colNames.next();
		
			allColSpecs[counter] = 
		            new DataColumnSpecCreator(colName, StringCell.TYPE).createSpec();
			counter++;
			
		}
		
		String[][] valueArray = new String[rowIndex+1][resultTable.size()]; 
		for(int i=0;i<rowIndex+1;i++){
			for(int j =0;j<resultTable.size();j++){
				valueArray[i][j]="";//every cell needs a string, and not null even if the string is ""
			}
		}
		
		Iterator<Map<String,String>> colIt = resultTable.values().iterator();
		int colIndex = 0;
		while (colIt.hasNext()){
			Map<String,String> col = colIt.next();
			Iterator<String> rowIt = col.keySet().iterator();
			while(rowIt.hasNext()){
				
				String rowIndexString = rowIt.next();
				int rowIndex = Integer.parseInt(rowIndexString);
				String cellValue = col.get(rowIndexString);
				//System.out.println("adding  row:"+rowIndex+",col :"+colIndex+", value="+cellValue);
				valueArray[rowIndex][colIndex] = cellValue;
			}
			colIndex++;
		}
		  DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
		 BufferedDataContainer container = exec.createDataContainer(outputSpec);
		for(int i=0;i<rowIndex+1;i++){
			RowKey key = new RowKey("Row"+(i+1));
			DataCell[] cells = new DataCell[resultTable.size()];
			for(int j=0;j<resultTable.size();j++){
				//System.out.println("adding  row:"+j+",col :"+i+", value="+valueArray[i][j]);
				cells[j] = new StringCell(valueArray[i][j]);
			}
			DataRow row = new DefaultRow(key, cells);
	        container.addRowToTable(row);
		}
		
        
        container.close();
        //container.getTableSpec().
        return container.getTable();
		
	}
    protected static void dim(Map<String,Map<String,String>> resultTable,String currentPath, Object currentJSON){

		 String type = currentJSON.getClass().getName();
		// System.out.println(type);
		 if(type.equals("net.sf.json.JSONArray")){
			 JSONArray jArray = (JSONArray)currentJSON;
			 int countStrings = 0;
			 for(int i = 0;i<jArray.size();i++){
				 
				 if(!(jArray.get(i).getClass().getName().equals("net.sf.json.JSONArray"))&&(!(jArray.get(i).getClass().getName().equals("net.sf.json.JSONObject")))){
					 String extPath = currentPath+"_"+countStrings+"_";
					 if(resultTable.get(extPath)==null){
						 Map<String,String> newCol = new LinkedHashMap<String,String>();
						 resultTable.put(extPath, newCol);
					 }
					 Map<String,String> col = resultTable.get(extPath);
					 col.put(""+rowIndex, jArray.get(i).toString());
					// System.out.println("Row: "+rowIndex+", Col: "+extPath+", val:"+jArray.get(i).toString());
					 countStrings++;
				 }else{
					 dim(resultTable,currentPath,jArray.get(i));
					 rowIndex+=1;
				 }
				 
			 }
		 } else  if(type.equals("net.sf.json.JSONObject")){
			 JSONObject jObject = (JSONObject)currentJSON;
			 Iterator<String> keys = jObject.keys();
			 String key=null;
			 while(keys.hasNext()){
				 key = keys.next();
				
				 Object object= jObject.get(key);
				 String objectType = object.getClass().getName();
				 String extPath = currentPath+"_"+key+"_";
				 if(objectType.equals("net.sf.json.JSONArray") || objectType.equals("net.sf.json.JSONObject")){
					 dim(resultTable,extPath,object);
				 }else{
					 if(resultTable.get(extPath)==null){
						 Map<String,String> newCol = new LinkedHashMap<String,String>();
						 resultTable.put(extPath, newCol);
					 }
					 Map<String,String> col = resultTable.get(extPath);
					 if(object.toString()!=null){
						 col.put(""+rowIndex, object.toString());
					 	//System.out.println("Row: "+rowIndex+", Col: "+extPath+", val:"+object.toString());
					 }else{
						 //error
						 System.out.println("ERROR: Wanted to add something to Row: "+rowIndex+", Col: "+extPath+", val:"+object.getClass().getName());
					 }
				 }
				 
			 }
			
		 }
	 }
	 protected static URL buildRequestURL(String c_uri) throws URISyntaxException, MalformedURLException, UnsupportedEncodingException  
	    {
	    	
	    	
	    	URI uri = new URI(c_uri);
	    	return uri.toURL();
	    }
	    
	    
	    
	    protected static JSONObject grabSomeJson(URL url) throws IOException
	    {
	    	String str="";
	    	URL x = url;
	    	BufferedReader in = new BufferedReader(
	    	new InputStreamReader(x.openStream()));

	    	String inputLine;

	    	while ((inputLine = in.readLine()) != null)
	    	 str+=inputLine+"\n";
	    	in.close(); 
	    	
	    	//System.out.println(str);
	    	JSONObject jo = (JSONObject) JSONSerializer.toJSON( str);
	    	
	    	return jo;

	    }

	    protected static JSONObject parseSomeJson(String json) throws IOException
	    {
	    	
	    	JSONObject jo = (JSONObject) JSONSerializer.toJSON( json);
	    	
	    	return jo;

	    }
}

