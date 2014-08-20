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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

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
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;


/**
 * This is the model implementation of OPS_JSON.
 * Reads a JSON file and transforms it to a KNIME table
 *
 * @author Ronald Siebes - VU Amsterdam
 */
public class OPS_JSONNodeModel extends NodeModel {
    
    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(OPS_JSONNodeModel.class);
       
 
    /** the settings key which is used to retrieve and 
        store the settings (from the dialog or from a settings file)    
       (package visibility to be usable from the dialog). */
	static final String JSON_URL = "json_url";
	static final String JSON_CONFIG_RESULT = "json_result";
	static final String SELECTION_PARAMETERS = "selection_parameters";
	static final String SELECTION_CUSTOMIZED_NAMES = "selection_customized_names";
	static final String ALL_PARAMETERS = "all_parameters";
	static final String ALL_CUSTOMIZED_NAMES = "all_customized_names";
	
	
    private static int rowIndex = 0;
	private static Map<Object, Map<String, Set<Object>>> jsonSet = null;
	private static String jsonKey = null;
	private static Object currentJsonKey = null;
	
	
    /** initial default values. */
    static final String DEFAULT_JSON_URL = "https://raw.githubusercontent.com/openphacts/OPS_LinkedDataApi/1.3.1/api-config-files/swagger.jsonhttps://raw.githubusercontent.com/openphacts/OPS_LinkedDataApi/1.3.1/api-config-files/swagger.json";
    //static final String DEFAULT_JSON_CONFIG_RESULT = "{}";
    // Here we put the parts of the JSON result that the user selected, and optionally gave new names to
 	static final String[] DEFAULT_SELECTION_PARAMETERS = new String[1000];//should be enough
 	static final String[] DEFAULT_SELECTION_CUSTOMIZED_NAMES = new String[1000];
 	static final String[] DEFAULT_ALL_PARAMETERS = new String[1000];//should be enough
 	static final String[] DEFAULT_ALL_CUSTOMIZED_NAMES = new String[1000];

    // example value: the models count variable filled from the dialog 
    // and used in the models execution method. The default components of the
    // dialog work with "SettingsModels".
    private final SettingsModelString json_url =
        new SettingsModelString(OPS_JSONNodeModel.JSON_URL,
                    OPS_JSONNodeModel.DEFAULT_JSON_URL);
    
   // private final SettingsModelString json_result_string = new SettingsModelString(OPS_JSONNodeModel.JSON_CONFIG_RESULT,
     //       OPS_JSONNodeModel.DEFAULT_JSON_CONFIG_RESULT);
    
    private final SettingsModelStringArray selection_parameters = new SettingsModelStringArray(OPS_JSONNodeModel.SELECTION_PARAMETERS, OPS_JSONNodeModel.DEFAULT_SELECTION_PARAMETERS);
    private final SettingsModelStringArray selection_customized_names = new SettingsModelStringArray(OPS_JSONNodeModel.SELECTION_CUSTOMIZED_NAMES, OPS_JSONNodeModel.DEFAULT_SELECTION_CUSTOMIZED_NAMES);
    private final SettingsModelStringArray all_parameters = new SettingsModelStringArray(OPS_JSONNodeModel.ALL_PARAMETERS, OPS_JSONNodeModel.DEFAULT_ALL_PARAMETERS);
    private final SettingsModelStringArray all_customized_names = new SettingsModelStringArray(OPS_JSONNodeModel.ALL_CUSTOMIZED_NAMES, OPS_JSONNodeModel.DEFAULT_ALL_CUSTOMIZED_NAMES);
   

    /**
     * Constructor for the node model.
     */
    protected OPS_JSONNodeModel() {
    
        // TODO one incoming port and two outgoing ports are assumed
        super(1, 2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec){

        // TODO do something here
        logger.info("Starting OPS_JSON execution ...");
        System.out.println("starting execution");
        
        String jsonToFetch = inData[0].iterator().next().getCell(0).toString();
        
        URL jsonURL;
        Map<String, Map<String, String>> resultTable = null;
		try {
			jsonURL = buildRequestURL(jsonToFetch);
			JSONObject jsonObject = grabSomeJson(jsonURL);
	         resultTable = getResultTable(jsonObject);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			DataColumnSpec[] selectedColSpecs = new DataColumnSpec[selection_customized_names.getStringArrayValue().length];
	        for (int i=0;i<selection_customized_names.getStringArrayValue().length;i++){
	        	selectedColSpecs[i] = 
	          			new DataColumnSpecCreator(selection_customized_names.getStringArrayValue()[i] ,
								ListCell.getCollectionType(StringCell.TYPE))
						.createSpec();
	        }
	   
	        DataTableSpec selectedOutputSpec = new DataTableSpec(selectedColSpecs);
	        System.out.println("Length:"+selectedOutputSpec.getColumnNames().length);
	        BufferedDataContainer selectionContainer = exec.createDataContainer(selectedOutputSpec);
	        BufferedDataContainer selectionContainer2 = exec.createDataContainer(selectedOutputSpec);
	        selectionContainer.close();
	        selectionContainer2.close();
	        
	        
	        
	        
	        BufferedDataTable selectionComplete = selectionContainer.getTable();
	        BufferedDataTable outComplete = selectionContainer2.getTable();
	        System.out.println("ready");
	        return new BufferedDataTable[]{selectionComplete,outComplete};
	        
		}
        
        
        
        Vector<String> mySelectionParams = new Vector<String>();
        
		mySelectionParams.addAll(Arrays.asList(selection_parameters.getStringArrayValue()));
        /** FIRST WE CREATE THE TABLE WITH THE COMPLETE JSON RESULT **/

        /** Create the column specs, and a lookup map **/
		DataColumnSpec[] allColSpecs = new DataColumnSpec[resultTable.keySet().size()];
		Map<Integer,String> allColIndex = new LinkedHashMap<Integer,String>();
		Map<Integer,String> colIndexBySelCol = new LinkedHashMap<Integer,String>();
		Set<Integer> selCols = new LinkedHashSet<Integer>();
		Iterator<String> allColNameIt = resultTable.keySet().iterator();
		int allCollCounter=0;
		int selCollCounter=0;
		while( allColNameIt.hasNext()){
			String colName = allColNameIt.next();
		  	allColSpecs[allCollCounter] = 
		  			new DataColumnSpecCreator(colName ,
							ListCell.getCollectionType(StringCell.TYPE))
					.createSpec();
		  	System.out.println(colName+",contains it?:"+mySelectionParams.contains(colName));
		  	//
		  		allColIndex.put(new Integer(allCollCounter),colName);
		  		
		  		if(mySelectionParams.contains(colName)){
		  			selCols.add(new Integer(allCollCounter));
		  			colIndexBySelCol.put(new Integer(selCollCounter),colName);
		  			selCollCounter++;
		  		}
		  		System.out.println("allColIndex:"+allCollCounter+", colName:"+colName);
		  		allCollCounter++;
		  	//}
		  		
		  	System.out.println("colName:"+colName+", colIndex:"+allCollCounter+", allColIndex.size:"+allColIndex.size());
		  	
		}
		DataTableSpec completeOutputSpec = new DataTableSpec(allColSpecs);
		BufferedDataContainer completeContainer = exec.createDataContainer(completeOutputSpec);
		

		/** Create a map with colIds to the colIndex **/
		

		//= new DataCell[resultTable.keySet().size()];
		
		/** create String[][] with the resulttable **/
		
		String[][] resultArray = new String[rowIndex][resultTable.keySet().size()];
		System.out.println("maxrows:"+rowIndex+",maxcols:"+resultTable.keySet().size());
		Iterator<String> allResultKeyIt = resultTable.keySet().iterator();
		int colCounter = 0;
		while(allResultKeyIt.hasNext()){
			String key = allResultKeyIt.next();
			Map<String,String> column = resultTable.get(key);
			Iterator<String> columnCells = column.keySet().iterator();
			while(columnCells.hasNext()){
				String rowPositionString= columnCells.next();
		        int rowPosition = Integer.parseInt(rowPositionString);
		        String cellValue = column.get(rowPositionString);
		        resultArray[rowPosition][colCounter]=cellValue;
		        System.out.println("adding row:"+rowPosition+", col:"+ colCounter+",val:"+cellValue);
			}
			colCounter++;
		}
		
		String[][] compressArray = compressArray(resultArray);
		Vector<Vector<String>> selectionVector = new Vector<Vector<String>>();
		
		
		System.out.println("compressArray"+compressArray.length);
		
		


		
		
		for(int i=0;i<compressArray.length;i++){
			
			Vector<String> rowVals =new Vector<String>();
			boolean atLeastOneCellValueInRow=false;
			for(int j=0;j<compressArray[i].length;j++){
				//System.out.println("selCols.length:"+selCols.size());
				if(selCols.contains(new Integer(j))){
					
					if(compressArray[i][j] !=""){
						System.out.println("we want col"+j+", with value "+compressArray[i][j]+", in row"+selectionVector.size());
						rowVals.add(compressArray[i][j]);
						atLeastOneCellValueInRow = true;
					}else{
						

						rowVals.add("");
					}
					//rowVals.add("");
					
				}
			}
			if(atLeastOneCellValueInRow){
				selectionVector.add(rowVals);
				System.out.println("rowvalsize:"+rowVals.size());
				
			}
			
			
		}
		/*This part is going to merge redundant rows
		 * Algorithm:
		 * - Take row x, and the next row y
		 * - iterate over each column and flag if the col value of x and col value of y have different values, with exception when one of the cells is empty
		 * - if the flag remains false then the two rows can be merged
		 * - merging means moving the non-empty cell values of y to row x and after that removing row y completely
		 */
		/*
		//first move the cell values of the 'next' row to the 'current' row if applicable
    	boolean continueMergingProcess = true;
    	while(continueMergingProcess){
    		boolean anyRowMerged = false;
			for(int i=0;i<compressArray.length;i++){
				System.out.println(compressArray.length);
				//take row i and row i+1 (if that exists)
				if(compressArray.length> i+1){
					boolean colHasDifferentValues = false;
					for (int j =0; j<compressArray[i].length;j++){
						
						if((!compressArray[i][j].equals("")&&compressArray[i][j]!=null) && (!compressArray[i+1][j].equals("")&&compressArray[i+1][j]!=null) && !compressArray[i][j].equals(compressArray[i+1][j])){
							colHasDifferentValues = true;
							System.out.println(compressArray[i][j]+", "+compressArray[i+1][j]);
							System.out.println("true");
							//i++;
							
							break;//we can skip the loop because these two rows can't be merged
						}
						
					}
					System.out.println("Before "+printArrayRow(compressArray[i]));
					System.out.println("Before "+printArrayRow(compressArray[i+1]));
					if(!colHasDifferentValues){
						anyRowMerged = true;
						
						for (int j =0; j<compressArray[i].length;j++){
							
							if(!compressArray[i+1][j].equals("")){
								//put the non-empty cell of row i+1 in row i
								compressArray[i][j] = compressArray[i+1][j];
								// clear the cell of row i+1;
								compressArray[i+1][j] = "";
								
								
							}
							
						}
						System.out.println("after "+printArrayRow(compressArray[i]));
						System.out.println("after "+printArrayRow(compressArray[i+1]));
						String[][] newArray = new String[compressArray.length-1][compressArray[0].length];
						for( int j =0; j<compressArray.length-1;j++){
							if(j<i+1){
								newArray[j] = compressArray[j];
							}else{
								newArray[j]= compressArray[j+1];
							}
							
						}
						compressArray = newArray;
						
						break;
					}
					
					
				}
			
			}
			System.out.println("anyRowMerged="+anyRowMerged);
			if(!anyRowMerged){
				
				continueMergingProcess = false;
			}
    	}
		*/
		
		/*This part is going to merge redundant rows
		 * Algorithm:
		 * - Take row x, and the next row y
		 * - iterate over each column and flag if the col value of x and col value of y have different values, with exception when one of the cells is empty
		 * - if the flag remains false then the two rows can be merged
		 * - merging means moving the non-empty cell values of y to row x and after that removing row y completely
		 */
		
		//first move the cell values of the 'next' row to the 'current' row if applicable
    	boolean continueMergingProcess = true;
    	
    	while(continueMergingProcess){
    		boolean atLeastOneMerge = false;	
    		for(int i=0;i<selectionVector.size()-1;i++){
    			if(selectionVector.size()>1){//we need at least two rows to make this procedure applicable
        			Vector<String> row1 = selectionVector.get(i);
            		Vector<String> row2 = selectionVector.get(i+1);
            		boolean colHasDifferentValues = false;
            		int firstValPosRow1 = 100000;
            		int firstValPosRow2 = 100000;
        			for(int j =0;j<row1.size();j++){
        				if (!row1.get(j).equals("")&&firstValPosRow1 == 100000){
        					firstValPosRow1 = j;
        				}
        				if (!row2.get(j).equals("")&&firstValPosRow2 == 100000){
        					firstValPosRow2 = j;
        				}
        				if((!row1.get(j).equals("") && !row2.get(j).equals("") && !row1.get(j).equals(row2.get(j))) || (firstValPosRow1>=firstValPosRow2 &&firstValPosRow1!=100000 )){
        					System.out.println(firstValPosRow1+";;;"+ firstValPosRow2);
        					colHasDifferentValues = true;
							break;//we can skip the loop because these two rows can't be merged
						}
        			}
        			if(!colHasDifferentValues){
        				atLeastOneMerge = true;
        				//Vector<String> mergedRow = new Vector<String>(row1);
        				for (int j =0; j<row2.size();j++){
							
							if(!row2.get(j).equals("")){
								//put the non-empty cell of row i+1 in row i
								row1.set(j, row2.get(j));
								
								
							}
							
						}
        				//selectionVector.set(i, mergedRow); //replace first row with merged row
        				selectionVector.remove(i+1);//delete the second row
        			}
    			}else{
    				break;
    			}
    			
    		}
    		if(!atLeastOneMerge){//we can stop now because no rows can be merged anymore
    			continueMergingProcess = false;
    		}
    		
    	}
    	
		Iterator<Vector<String>> test = selectionVector.iterator();
		while(test.hasNext()){
			Vector<String> next = test.next();
			Iterator<String> testRows = next.iterator();
			System.out.print("SELECTION ROW:");
			while(testRows.hasNext()){
				System.out.print(testRows.next()+",");
			}
			System.out.println();
		}
		
		/** CREATE THE FULL OUTPUT TABLE **/
		
		for(int i=0;i<resultArray.length;i++){
			RowKey key = new RowKey("Row" + (i));
			DataCell[] resultCells = new DataCell[resultArray[i].length];
			
			for(int j=0;j<resultArray[i].length;j++){
				ArrayList<StringCell> cellValue = new ArrayList<StringCell>();
				if(resultArray[i][j]==null){
					cellValue.add(new StringCell(""));
				}else{
					String cellVal = resultArray[i][j];
	        		if(cellVal.indexOf(";;")>=0){
	        			String [] splitCell = cellVal.split(";;");
	        			for(int k =0;k<splitCell.length;k++){
	        				cellValue.add(new StringCell(splitCell[k]));
	        			}
	        		}else{
	        			cellValue.add(new StringCell(cellVal));
	        		}
					//cellValue.add(new StringCell(resultArray[i][j]));
				}
				resultCells[j]=CollectionCellFactory
						.createListCell(cellValue);
			}
			//System.out.println("")
			DataRow row = new DefaultRow(key, resultCells);
			//allRows.put(key.getString(), row);
			System.out.println("adding row:"+key.getString()+", with row:"+row);
			//allCells.put(row, resultCells);
			completeContainer.addRowToTable(row);
			
		}
		/** NOW WE CREATE A SMALLER TABLE CONTAINING THE SELECTION MADE BY THE USER **/ 
		
		System.out.println("are we here?");
	
        DataColumnSpec[] selectedColSpecs = new DataColumnSpec[selection_customized_names.getStringArrayValue().length];
        for (int i=0;i<selection_customized_names.getStringArrayValue().length;i++){
        	selectedColSpecs[i] = 
          			new DataColumnSpecCreator(selection_customized_names.getStringArrayValue()[i] ,
							ListCell.getCollectionType(StringCell.TYPE))
					.createSpec();
        }
   
        DataTableSpec selectedOutputSpec = new DataTableSpec(selectedColSpecs);
        System.out.println("Length:"+selectedOutputSpec.getColumnNames().length);
        BufferedDataContainer selectionContainer = exec.createDataContainer(selectedOutputSpec);
   
        Iterator<Vector<String>> selectionRowIt = selectionVector.iterator();
        int selectionRowCounter = 0;
        while(selectionRowIt.hasNext()){
        	Vector<String> currentRow = selectionRowIt.next();
        	RowKey key = new RowKey("Row" + (selectionRowCounter));
        	System.out.println("sais:"+ currentRow.size());
        	
        	
        	DataCell[] resultCells = new DataCell[selection_customized_names.getStringArrayValue().length];
        	for (int i =0;i<selection_customized_names.getStringArrayValue().length;i++){
        		ArrayList<StringCell> cellValues = new ArrayList<StringCell>();
        		resultCells[i]=CollectionCellFactory
						.createListCell(cellValues);
        	}
        	
        	Iterator<String> cellIt = currentRow.iterator();
        	int cellCounter = 0;
        	while(cellIt.hasNext()){
        	
        		
        		ArrayList<StringCell> cellValues = new ArrayList<StringCell>();
        		String cellValue = cellIt.next();
        		if(cellValue.indexOf(";;")>=0){
        			String [] splitCell = cellValue.split(";;");
        			for(int i =0;i<splitCell.length;i++){
        				cellValues.add(new StringCell(splitCell[i]));
        			}
        		}else{
        			cellValues.add(new StringCell(cellValue));
        		}
        		System.out.println("cellcoutner"+ colIndexBySelCol.get(cellCounter));
        		if( mySelectionParams.indexOf(colIndexBySelCol.get(cellCounter))>-1){
        		resultCells[ mySelectionParams.indexOf(colIndexBySelCol.get(cellCounter))]=CollectionCellFactory
						.createListCell(cellValues);
        		System.out.println("have it");
        		}
        		cellCounter++;
        		
        	}
        	selectionRowCounter++;
        	//System.out.println("")
			DataRow row = new DefaultRow(key, resultCells);
			//allRows.put(key.getString(), row);
			System.out.println("adding selectionrow:"+key.getString()+", with row:"+row);
			//allCells.put(row, resultCells);
			selectionContainer.addRowToTable(row);
        	
        	
        }
        // once we are done, we close the containers and return its tables
        selectionContainer.close();
        System.out.println("hier");
        completeContainer.close();

        System.out.println("daar");
       BufferedDataTable selectionComplete = selectionContainer.getTable();
        BufferedDataTable outComplete = completeContainer.getTable();
        System.out.println("ready");
        return new BufferedDataTable[]{selectionComplete,outComplete};
        //return new BufferedDataTable[]{ outComplete};

    }

    
    private String printArrayRow(String[] row){
    	
    	String result = "";
    	for(int i=0;i<row.length;i++){
    		result+=(row[i]+", ");
    	}
    	result = result.substring(0, result.length()-2);
    	return result;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
    	
    	rowIndex=0;
    	
        // TODO Code executed on reset.
        // Models build during execute are cleared here.
        // Also data handled in load/saveInternals will be erased here.
    }

    
    private String[][] compressArray(String[][] fullArray){
    	//System.out.println("starting bla");
    	String[][] compressArray = new String[fullArray.length][fullArray[0].length];//dangerous
    	
    	for(int i=0;i<fullArray.length;i++){
    		for(int j=0;j<fullArray[i].length;j++){
    			if(fullArray[i][j]==null){
    				fullArray[i][j]="";
    				compressArray[i][j]="";
    				//System.out.println("--------------ARRRRAYYY NULLLL");
    			}else{
    				System.out.println("compressArrauy["+i+"]["+j+"] = "+fullArray[i][j]);
    				compressArray[i][j]=fullArray[i][j];
    			}
    			
    			
    			
    		}
    	}
    	
    	int latestRowUpdate = 0;
    	for(int i=1;i<fullArray.length;i++){
    		//boolean replacedNastyCell = false;
    		for(int j=1;j<fullArray[i].length;j++){
    			if(!fullArray[i][j].equals("")){
    				
    				int rightmostField = 0;
    				int nrOfNonBlankCells = 0;
    				for(int k=1;k<compressArray[i].length;k++){
    					
    					if(!compressArray[latestRowUpdate][k].equals("")){
    						nrOfNonBlankCells++;
    						System.out.println("rightmostField row:"+latestRowUpdate+", col:"+k+", val:"+compressArray[latestRowUpdate][k]);
    						rightmostField = k;
    					}
    				}
    				
    				int previousChild=-1;
    				for(int k=0;k<compressArray[i].length;k++){
    					
    					if(!compressArray[k][j].equals("")&&k<i){
    						System.out.println("previousChild row:"+k+", col:"+j+", val:"+compressArray[k][j]);
    						previousChild = k;
    					}
    				}
    				if(previousChild==-1){
    					System.out.println("previousChild non exist for:"+i+", col:"+j+", val:"+compressArray[i][j]);
    					compressArray[0][j]=compressArray[i][j];
    					compressArray[i][j]="";
    				}
    				else if(nrOfNonBlankCells==1){
    					System.out.println("only one cell in row:"+i+", col:"+j+", val:"+compressArray[i][j]);
    					if(compressArray[latestRowUpdate][j].equals("")){
    						compressArray[latestRowUpdate][j] = compressArray[i][j];
    					}else{
    						System.out.println("compressArray[latestRowUpdate][j]"+compressArray[latestRowUpdate][j]);
    						compressArray[latestRowUpdate][j]=compressArray[latestRowUpdate][j]+";;"+compressArray[i][j];
    					}
    					
    					compressArray[i][j]="";
    					/*
    					if(!replacedNastyCell){
    						compressArray[latestRowUpdate][j]=compressArray[latestRowUpdate-1][j]+compressArray[latestRowUpdate][j];
    						compressArray[latestRowUpdate-1][j]="";
    						replacedNastyCell = true;
    					}*/
    					
    				}
    				else if (rightmostField >j){
    					latestRowUpdate++;
    					compressArray[latestRowUpdate][j] = compressArray[i][j];
    					compressArray[i][j]="";
    				} else{
    					if(compressArray[latestRowUpdate][j].equals("")){
    						compressArray[latestRowUpdate][j] = compressArray[i][j];
    					}else{
    						compressArray[latestRowUpdate][j] = compressArray[latestRowUpdate][j] + ";;" + compressArray[i][j];
    					}
    					
    					compressArray[i][j]="";
    				}
    				
    				/*
    				else if(parent == previousChild && previousChild<i ){
    					System.out.println("parent same as previous child:"+i+", col:"+j+", val:"+compressArray[i][j]);
    					compressArray[previousChild+1][j] = compressArray[i][j];
    					compressArray[i][j]="";
    				}else if(parent>previousChild){
    					System.out.println("parent later than previous child:"+i+", col:"+j+", val:"+compressArray[i][j]);
    					compressArray[parent][j] = compressArray[i][j];
    					compressArray[i][j]="";
    				}else if(parent<previousChild){
    					System.out.println("previousChild has to be merged with current:"+i+", col:"+j+", val:"+compressArray[i][j]);
    					compressArray[previousChild][j]=compressArray[previousChild][j]+";"+compressArray[i][j];
    					compressArray[i][j]="";
    				}*/
    				
    				
    			}
    			
    		}
    	}
    	
    	    	//latestRowUpdate = compressArray.length;
	
    	String[][] resultCompressArray = null;
    	if(latestRowUpdate ==0){
    		resultCompressArray  = new String[1][compressArray[0].length];
    	}else{
    		resultCompressArray  = new String[latestRowUpdate][compressArray[0].length];
    	}
    	
    	System.out.println("latestrowupdate:"+latestRowUpdate);
    	
    	if(latestRowUpdate==0){
    		for(int j=0;j<compressArray[0].length;j++){
    			resultCompressArray[0][j] = compressArray[0][j];
    			System.out.print(compressArray[0][j]+"| ");
    			
    		}
    	}else{
	    	for(int i=0;i<latestRowUpdate;i++){
	    		System.out.print("row:"+i+", values:");
	    		for(int j=0;j<compressArray[i].length;j++){
	    			resultCompressArray[i][j] = compressArray[i][j];
	    			System.out.print(compressArray[i][j]+"| ");
	    			
	    		}
	    		System.out.println();
	    	}
    	}
    	
    	
    	return resultCompressArray;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {
        
    	if(inSpecs[0].getNumColumns()!=1){
    		throw new InvalidSettingsException("The input table should have exactly one column, with the name 'url' that contains the url to the json");
    	}else if(inSpecs[0].getColumnSpec("url") ==null){
    		throw new InvalidSettingsException("The name of the only column in your input table should be changed to 'url'");

    	}
    	
        return new DataTableSpec[]{null,null};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
    	
        json_url.saveSettingsTo(settings);
       // json_result_string.saveSettingsTo(settings);
        selection_parameters.saveSettingsTo(settings);
        selection_customized_names.saveSettingsTo(settings);
        all_parameters.saveSettingsTo(settings);
        all_customized_names.saveSettingsTo(settings);
        
       // settings.

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
        json_url.loadSettingsFrom(settings);
      //  json_result_string.loadSettingsFrom(settings);
        selection_parameters.loadSettingsFrom(settings);
        selection_customized_names.loadSettingsFrom(settings);
        all_parameters.loadSettingsFrom(settings);
        all_customized_names.loadSettingsFrom(settings);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
    	 json_url.validateSettings(settings);
    	 
    
    	 
    	//System.out.println("setting:"+settings.getString(selection_parameters.getStringArrayValue()[0]));
       //  json_result_string.validateSettings(settings);
         selection_parameters.validateSettings(settings);
         System.out.println("validatesettings:"+selection_parameters.getStringArrayValue().length);
         selection_customized_names.validateSettings(settings);
         all_parameters.validateSettings(settings);
         all_customized_names.validateSettings(settings);
         selection_parameters.setStringArrayValue(settings.getStringArray(SELECTION_PARAMETERS));
         System.out.println("validatesettingsXX:"+selection_parameters.getStringArrayValue().length);
         selection_customized_names.setStringArrayValue(settings.getStringArray(SELECTION_CUSTOMIZED_NAMES));
         all_parameters.setStringArrayValue(settings.getStringArray(ALL_PARAMETERS));
         all_customized_names.setStringArrayValue(settings.getStringArray(ALL_CUSTOMIZED_NAMES));
         System.out.println("start");
         for (int i=0;i<selection_parameters.getStringArrayValue().length;i++){
        	 System.out.println("we have:"+selection_parameters.getStringArrayValue()[i] );
         }
         for (int i=0;i<selection_customized_names.getStringArrayValue().length;i++){
        	 System.out.println("we have name:"+selection_customized_names.getStringArrayValue()[i] );
         }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        
        // TODO load internal data. 
        // Everything handed to output ports is loaded automatically (data
        // returned by the execute method, models loaded in loadModelContent,
        // and user settings set through loadSettingsFrom - is all taken care 
        // of). Load here only the other internals that need to be restored
        // (e.g. data used by the views).

    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
       
        // TODO save internal models. 
        // Everything written to output ports is saved automatically (data
        // returned by the execute method, models saved in the saveModelContent,
        // and user settings saved through saveSettingsTo - is all taken care 
        // of). Save here only the other internals that need to be preserved
        // (e.g. data used by the views).

    }
    
    
	protected URL buildRequestURL(String c_uri) throws URISyntaxException,
	MalformedURLException, UnsupportedEncodingException {

	URI uri = new URI(c_uri);
	return uri.toURL();
}

protected JSONObject grabSomeJson(URL url) throws IOException {
	String str = "";
	URL x = url;
	BufferedReader in = new BufferedReader(new InputStreamReader(
			x.openStream()));

	String inputLine;

	while ((inputLine = in.readLine()) != null)
		str += inputLine + "\n";
	in.close();

	JSONObject jo = (JSONObject) JSONSerializer.toJSON(str);

	return jo;

}
    
private Map<String, Map<String, String>> getResultTable(JSONObject o){
		
		
		Map<String, Map<String, String>> resultTable = new LinkedHashMap<String, Map<String, String>>();
		dim(resultTable, "", o); // recursion start
		
		///print the bastard 
   	 Iterator<String> resultKeyIt = resultTable.keySet().iterator();
       
        while(resultKeyIt.hasNext()){
        	// System.out.println("hier6?");
        	String key = resultKeyIt.next();
        	
	        	Map<String,String> result = resultTable.get(key);
	        	System.out.print("key:"+key);
	        	Iterator<String> resultValueIt = result.keySet().iterator();
	        	
	        	while(resultValueIt.hasNext()){
	        		String value = resultValueIt.next();
	        		
	        			System.out.print(", valueKey:"+ value+", valueValue:"+ result.get(value));
	        			
	        		
	        		
	        	}
	        	System.out.println();
	        	System.out.println("--------------------------------------------------------------------------------------------------------------------------");
        	
        	
        	
        	
        }
	
		return resultTable;
	}
    protected static void dim(Map<String, Map<String, String>> resultTable,
			String currentPath, Object currentJSON) {
    	
    	


		String type = currentJSON.getClass().getName();
		System.out.println(type);
		if (type.equals("net.sf.json.JSONArray")) {
			JSONArray jArray = (JSONArray) currentJSON;

			for (int i = 0; i < jArray.size(); i++) {
				
				String keytype = jArray.get(i).getClass().getName();
				
				
				if (keytype.equals("net.sf.json.JSONArray")
						|| keytype.equals("net.sf.json.JSONObject")) {
					dim(resultTable, currentPath, jArray.get(i));
				
				}else {
					String extPath = currentPath ;
					if (resultTable.get(extPath) == null) {
						Map<String, String> newCol = new LinkedHashMap<String, String>();
						resultTable.put(extPath, newCol);
					}
					Map<String, String> col = resultTable.get(extPath);
					
					if(col.get(""+rowIndex)!=null){
						
						rowIndex += 1;
						col.put("" + rowIndex, jArray.get(i).toString());
						System.out.println("row:"+rowIndex+", extpath:"+extPath+",elements:"+col.size()+",last resul1t:"+resultTable.get(extPath).get(""+rowIndex).toString());
						
						
					}else{
						col.put("" + rowIndex,jArray.get(i).toString());
						System.out.println("row:"+rowIndex+", extpath:"+extPath+",elements:"+col.size()+",last resul1t:"+resultTable.get(extPath).get(""+rowIndex).toString());
						
						rowIndex += 1;
					}
					
					
					
					//rowIndex+=1;
					
					//rowIndex += 1;
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
				//boolean firstColOccur = false;
				
				if (objectType.equals("net.sf.json.JSONArray")
						|| objectType.equals("net.sf.json.JSONObject")) {
					dim(resultTable, extPath, object);
				
				}else{
					if (resultTable.get(extPath) == null) {
					
						Map<String, String> newCol = new LinkedHashMap<String, String>();

						resultTable.put(extPath, newCol);
		
					}
				
					Map<String, String> col = resultTable.get(extPath);
					col.put("" + rowIndex, jObject.get(key).toString());
				
					System.out.println("row:"+rowIndex+", extpath:"+extPath+", elements:"+col.size()+",last result:"+resultTable.get(extPath).get(""+rowIndex).toString());
					rowIndex += 1;
					
					
				}
					
				
				
			}

		}
	}

}

