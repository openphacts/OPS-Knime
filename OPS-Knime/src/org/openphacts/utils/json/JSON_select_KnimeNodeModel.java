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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.collection.CollectionCellFactory;
import org.knime.core.data.collection.ListCell;
import org.knime.core.data.container.CloseableRowIterator;
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

/**
 * This is the model implementation of JSON_select_Knime. Reads a JSON string,
 * converts its hierarchical structure into a flat 2D matrix and exports it as a
 * Knime table.
 * 
 * @author Ronald Siebes - VU Amsterdam
 */
public class JSON_select_KnimeNodeModel extends NodeModel {

	public static final String TYPE_DEFAULT = "URL";
	private static int rowIndex = 0;
	private static Map<String, String> paramSet = new LinkedHashMap<String, String>();

	private static Map<Object, Map<String, Set<Object>>> jsonSet = null;
	static String jsonKey = null;

	/**
	 * Constructor for the node model.
	 */
	protected JSON_select_KnimeNodeModel() {

		super(2, 2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {
		jsonSet =new LinkedHashMap<Object, Map<String, Set<Object>>>();
		paramSet = new LinkedHashMap<String, String>();
		CloseableRowIterator cit = inData[0].iterator();
		DataRow current;
		while (cit.hasNext()) {
			current = cit.next();
			Iterator <DataCell> cellIt = current.iterator();
			//first get the column key
			if(cellIt.hasNext()){
				DataCell first = cellIt.next();
				String firstName = first.toString();
				String secondName = first.toString(); //keep this if next part is empty
				//second, get an eventual new name for the key
				if(cellIt.hasNext()){
					secondName = cellIt.next().toString();
				}
				paramSet.put(firstName, secondName);
			}
			
		}
		JSONObject json=null;
    	
	        String compoundUri= inData[1].iterator().next().getCell(0).toString(); // ugly...needs definitely some array bound checks here
	
	        URL requestURL = buildRequestURL(compoundUri);
	        
	        
	        
	        try{
	         json = JSON_KnimeNodeModel.grabSomeJson(requestURL);
	        } catch (IOException e){
	        	
	        	//the json is not valid (e.g. 404 page), therefore create two empty tables
	        	
	        	DataColumnSpec[] allColSpecs = new DataColumnSpec[paramSet.size()];
	        	

	        	String[] paramSetArray = paramSet.keySet().toArray(
	    				new String[paramSet.size()]);
	    		for (int i = 0; i < paramSetArray.length; i++) {

	    			String colName = paramSet.get(paramSetArray[i]);
	    			if(i==0){
	    				allColSpecs[i] = new DataColumnSpecCreator(colName, StringCell.TYPE).createSpec();
	    				
	    			}else {//the rest are ListCells with Strings
	    				allColSpecs[i] = new DataColumnSpecCreator(colName, ListCell.getCollectionType(StringCell.TYPE)).createSpec();
	    			}
	    			
	    		}
	        	

	        	DataTableSpec outputSpec = new DataTableSpec(allColSpecs);

	        	DataColumnSpec[] allColSpecs2 = new DataColumnSpec[0];
	        	DataTableSpec outputSpec2 = new DataTableSpec(allColSpecs2);
		   		 BufferedDataContainer container = exec.createDataContainer(outputSpec);
		   		 container.close();
		   		 BufferedDataContainer container2 = exec.createDataContainer(outputSpec2);
		   		 container2.close();
	        	return new BufferedDataTable[]{container.getTable(),container2.getTable()};  	
	        }
	        
    	
       
		
		
	
																	// checks
		CloseableRowIterator it = inData[0].iterator();
		if(it.hasNext()){
			jsonKey = inData[0].iterator().next().getCell(0).toString();
			// here
		}
		
		BufferedDataTable jsonTable = makeTable(exec, inData[0], json);
		DataColumnSpec[] resultColSpecs = new DataColumnSpec[paramSet.size()];
		
		String[] paramSetArray = paramSet.keySet().toArray(
				new String[paramSet.size()]);
		for (int i = 0; i < paramSetArray.length; i++) {

			String colName = paramSet.get(paramSetArray[i]);
			//first one is the iteration key, always a String
			if(i==0){
				resultColSpecs[i] = new DataColumnSpecCreator(colName, StringCell.TYPE).createSpec();
				
			}else {//the rest are ListCells with Strings
				resultColSpecs[i] = new DataColumnSpecCreator(colName, ListCell.getCollectionType(StringCell.TYPE)).createSpec();
			}
		}
		DataTableSpec resultSpec = new DataTableSpec(resultColSpecs);
		BufferedDataContainer resultContainer = exec
				.createDataContainer(resultSpec);


		Iterator<Object> jsonSetKeyIt = jsonSet.keySet().iterator();
		String resultString = "";
		int resultRowCount = 0;
		while (jsonSetKeyIt.hasNext()) {
			Object jsonSetKeyObj = jsonSetKeyIt.next();
			RowKey key = new RowKey("Row" + (resultRowCount + 1));
			DataCell[] cells = new DataCell[paramSet.size()];
			cells[0] = new StringCell(jsonSetKeyObj.toString());
			
			for (int i = 1; i < paramSet.size(); i++) {

					cells[i] = CollectionCellFactory.createListCell(new ArrayList<StringCell>());
				
			}
			Iterator<String> jsonSetAgrIt = jsonSet.get(jsonSetKeyObj).keySet()
					.iterator();
			String agrString = "";
			
			
			while (jsonSetAgrIt.hasNext()) {
				String currentAgrKey = jsonSetAgrIt.next();
				Iterator<Object> currentAgrValueSet = jsonSet
						.get(jsonSetKeyObj).get(currentAgrKey).iterator();

				while (currentAgrValueSet.hasNext()) {
					ArrayList<StringCell> al = new ArrayList<StringCell>();
					Object jsonObject= currentAgrValueSet.next();
					if(jsonObject.getClass().getName().equals("net.sf.json.JSONArray")){
						
						JSONArray array = (JSONArray)jsonObject;
						
						Iterator arrayIt = array.iterator();
						while(arrayIt.hasNext()){
							al.add(new StringCell(arrayIt.next().toString()));
						}
						
					} else
					{
						
						al.add(new StringCell(jsonObject.toString()));
					}
					
					cells[resultSpec.findColumnIndex(paramSet.get(currentAgrKey))] = CollectionCellFactory.createListCell(al);	
					
					
				}
			}
			if(jsonKey!=null && paramSet.get(jsonKey)!=null){
				resultRowCount += 1;
				DataRow row = new DefaultRow(key, cells);
				resultContainer.addRowToTable(row);
			}
		} 

		resultContainer.close();
		
		BufferedDataTable[] resultContainers = new BufferedDataTable[2];
		resultContainers[0] = resultContainer.getTable();
		resultContainers[1] = jsonTable;
		return resultContainers;
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
		return new DataTableSpec[] { null,null };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		// TODO: generated method stub
		// type_settings.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		// type_settings.loadSettingsFrom(settings);
		// TODO: generated method stub
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		// type_settings.validateSettings(settings);
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

	protected static BufferedDataTable makeTable(ExecutionContext exec,
			BufferedDataTable params, JSONObject wholeObject) {

		
		DataColumnSpec[] resultColSpecs = new DataColumnSpec[paramSet.size()];
		Iterator<String> paramSetIterator = paramSet.keySet().iterator();
		int resultCounter = 0;
		while (paramSetIterator.hasNext()) {
			String colName = paramSet.get(paramSetIterator.next());

			resultColSpecs[resultCounter] = new DataColumnSpecCreator(colName,
					StringCell.TYPE).createSpec();
			resultCounter++;

		}
		DataTableSpec resultSpec = new DataTableSpec(resultColSpecs);
		BufferedDataContainer resultContainer = exec
				.createDataContainer(resultSpec);
		rowIndex = 0;
		Map<String, Map<String, String>> resultTable = new LinkedHashMap<String, Map<String, String>>();
		dim(resultTable, "", wholeObject); // recursion start
		DataColumnSpec[] allColSpecs = new DataColumnSpec[resultTable.size()];

		// the execution context will provide us with storage capacity, in this
		// case a data container to which we will add rows sequentially
		// Note, this container can also handle arbitrary big data tables, it
		// will buffer to disc if necessary.

		Iterator<String> colNames = resultTable.keySet().iterator();
		int counter = 0;
		while (colNames.hasNext()) {
			String colName = colNames.next();

			allColSpecs[counter] = new DataColumnSpecCreator(colName,
					StringCell.TYPE).createSpec();
			counter++;

		}

		String[][] valueArray = new String[rowIndex + 1][resultTable.size()];
		for (int i = 0; i < rowIndex + 1; i++) {
			for (int j = 0; j < resultTable.size(); j++) {
				valueArray[i][j] = "";// every cell needs a string, and not null
										// even if the string is ""
			}
		}

		Iterator<Map<String, String>> colIt = resultTable.values().iterator();
		int colIndex = 0;
		while (colIt.hasNext()) {
			Map<String, String> col = colIt.next();
			Iterator<String> rowIt = col.keySet().iterator();
			while (rowIt.hasNext()) {

				String rowIndexString = rowIt.next();
				int rowIndex = Integer.parseInt(rowIndexString);
				String cellValue = col.get(rowIndexString);
				valueArray[rowIndex][colIndex] = cellValue;
			}
			colIndex++;
		}

		DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
		BufferedDataContainer container = exec.createDataContainer(outputSpec);

		for (int i = 0; i < rowIndex + 1; i++) {
			RowKey key = new RowKey("Row" + (i + 1));
			DataCell[] cells = new DataCell[resultTable.size()];
			DataCell[] resultCells = new DataCell[paramSet.size()];
			boolean found = false;
			for (int j = 0; j < resultTable.size(); j++) {
				// System.out.println("adding  row:"+j+",col :"+i+", value="+valueArray[i][j]);
				if (valueArray[i][j].length() == 0) {

				}
				cells[j] = new StringCell(valueArray[i][j]);
				String name = container.getTableSpec().getColumnSpec(j)
						.getName();
				found = true;
				if (paramSet.containsKey(name)) {
					int position = resultContainer.getTableSpec()
							.findColumnIndex(paramSet.get(name));
					resultCells[position] = new StringCell(valueArray[i][j]);
				}
			}
			if (found) {
				for (int k = 0; k < resultCells.length; k++) {
					if (resultCells[k] == null) {
						resultCells[k] = new StringCell("");
					}
				}
				boolean hasValues = false;
				for (int j = 0; j < resultCells.length; j++) {
					if (!resultCells[j].toString().equals("")) {
						hasValues = true;
						break;
					}
				}
				if (hasValues) {
					DataRow resultRow = new DefaultRow(key, resultCells);
					resultContainer.addRowToTable(resultRow);
				}
			}

			DataRow row = new DefaultRow(key, cells);
			container.addRowToTable(row);

		}

		container.close();

		resultContainer.close();
		return container.getTable();

	}

	static Object currentJsonKey = null;

	protected static void dim(Map<String, Map<String, String>> resultTable,
			String currentPath, Object currentJSON) {

		String type = currentJSON.getClass().getName();
		// System.out.println(type);
		if (type.equals("net.sf.json.JSONArray")) {
			JSONArray jArray = (JSONArray) currentJSON;

			for (int i = 0; i < jArray.size(); i++) {

				if (!(jArray.get(i).getClass().getName()
						.equals("net.sf.json.JSONArray"))
						&& (!(jArray.get(i).getClass().getName()
								.equals("net.sf.json.JSONObject")))) {
					String extPath = currentPath;
					if (resultTable.get(extPath) == null) {
						Map<String, String> newCol = new LinkedHashMap<String, String>();
						resultTable.put(extPath, newCol);
					}
					Map<String, String> col = resultTable.get(extPath);
					col.put("" + rowIndex, jArray.get(i).toString());
					rowIndex += 1;
				} else {

					dim(resultTable, currentPath, jArray.get(i));
					rowIndex += 1;
				}

			}
		} else if (type.equals("net.sf.json.JSONObject")) {
			JSONObject jObject = (JSONObject) currentJSON;
			Iterator<String> keys = jObject.keys();
			String key = null;
			while (keys.hasNext()) {
				key = keys.next();

				Object object = jObject.get(key);
				String objectType = object.getClass().getName();
				String extPath = currentPath + ".." + key;
				System.out.println(extPath);
				try {
				if(jsonKey!=null){
					if (jsonKey.equals(extPath)) {
						
						Map<String, Set<Object>> newMap = new LinkedHashMap<String, Set<Object>>();
						jsonSet.put(object, newMap);
						currentJsonKey = object;
					} else if (paramSet.containsKey(extPath)) {
						if (!jsonSet.get(currentJsonKey).containsKey(extPath)) {
							Set<Object> agrObjects = new LinkedHashSet<Object>();
							jsonSet.get(currentJsonKey).put(extPath, agrObjects);
						}
						jsonSet.get(currentJsonKey).get(extPath).add(object);
	
					}
				}
				}catch (Exception e){
					e.printStackTrace();
				}
				if (objectType.equals("net.sf.json.JSONArray")
						|| objectType.equals("net.sf.json.JSONObject")) {
					dim(resultTable, extPath, object);
				} else {
					if (resultTable.get(extPath) == null) {
						Map<String, String> newCol = new LinkedHashMap<String, String>();
						resultTable.put(extPath, newCol);
					}
					Map<String, String> col = resultTable.get(extPath);
					if (object.toString() != null) {
						col.put("" + rowIndex, object.toString());
					} else {
						// error
						System.out
								.println("ERROR: Wanted to add something to Row: "
										+ rowIndex
										+ ", Col: "
										+ extPath
										+ ", val:"
										+ object.getClass().getName());
					}
				}

			}

		}
	}

	protected static URL buildRequestURL(String c_uri)
			throws URISyntaxException, MalformedURLException,
			UnsupportedEncodingException {

		URI uri = new URI(c_uri);
		return uri.toURL();
	}

	protected static JSONObject grabSomeJson(URL url) throws IOException {
		String str = "";
		URL x = url;
		BufferedReader in = new BufferedReader(new InputStreamReader(
				x.openStream()));

		String inputLine;

		while ((inputLine = in.readLine()) != null)
			str += inputLine + "\n";
		in.close();

		// System.out.println(str);
		JSONObject jo = (JSONObject) JSONSerializer.toJSON(str);

		return jo;

	}

	protected static JSONObject parseSomeJson(String json) throws IOException {

		JSONObject jo = (JSONObject) JSONSerializer.toJSON(json);

		return jo;

	}
}
