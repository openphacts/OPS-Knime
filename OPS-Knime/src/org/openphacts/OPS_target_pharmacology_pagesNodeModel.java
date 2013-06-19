package org.openphacts;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;

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
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;


/**
 * This is the model implementation of OPS_target_pharmacology_pages.
 * A page of items corresponding to acitivity values in the LDC for a given target
 *
 * @author Ronald Siebes - VUA
 */
public class OPS_target_pharmacology_pagesNodeModel extends NodeModel {
	 public static final String API_URL = "uri";
		public static final String DEFAULT_API_URL = "https://beta.openphacts.org";
		public static final String APP_ID_DEFAULT = "15a18100";
		public static final String APP_ID = "app_id";
		public static final String APP_KEY_DEFAULT = "528a8272f1cd961d215f318a0315dd3d";
		public static final String APP_KEY = "app_key";
		public static final String URI_DEFAULT = "http://www.conceptwiki.org/concept/38932552-111f-4a4e-a46a-4ed1d7bdf9d5";
		public static final String URI = "uri";
		public static final String ASSAY_ORGANISM = "assay_organism";
		public static final String ASSAY_ORGANISM_DEFAULT = "";
		public static final String TARGET_ORGANISM = "target_organism";
		public static final String TARGET_ORGANISM_DEFAULT = "";
		public static final String ACTIVITY_TYPE_DEFAULT = "";
		public static final String ACTIVITY_TYPE = "activity_type";
		public static final String ACTIVITY_VALUE_DEFAULT = "";
		public static final String ACTIVITY_VALUE = "activity_value";
		public static final String MIN_ACTIVITY_VALUE = "min_activity_value";
		public static final String MIN_ACTIVITY_VALUE_DEFAULT = "";
		public static final String MIN_EX_ACTIVITY_VALUE = "min_ex_activity_value";
		public static final String MIN_EX_ACTIVITY_VALUE_DEFAULT = "";
		public static final String MAX_ACTIVITY_VALUE_DEFAULT = "";
		public static final String MAX_ACTIVITY_VALUE = "max_activity_value";
		public static final String MAX_EX_ACTIVITY_VALUE_DEFAULT = "";
		public static final String MAX_EX_ACTIVITY_VALUE = "max_ex_activity_value";
		public static final String ACTIVITY_UNIT = "activity_unit";
		public static final String ACTIVITY_UNIT_DEFAULT = "";
		public static final String PAGE = "_page";
		public static final int PAGE_DEFAULT = 1;
		public static final String PAGE_SIZE = "_pageSize";
		public static final String PAGE_SIZE_DEFAULT = "10";
		public static final String ORDER_BY_DEFAULT = "";
		public static final String ORDER_BY = "_orderBy";

		private final SettingsModelString api_settings = new SettingsModelString(OPS_target_pharmacology_pagesNodeModel.API_URL, OPS_target_pharmacology_pagesNodeModel.DEFAULT_API_URL);
		private final SettingsModelString app_id_settings = new SettingsModelString(OPS_target_pharmacology_pagesNodeModel.APP_ID, OPS_target_pharmacology_pagesNodeModel.APP_ID_DEFAULT);
		private final SettingsModelString app_key_settings = new SettingsModelString(OPS_target_pharmacology_pagesNodeModel.APP_KEY, OPS_target_pharmacology_pagesNodeModel.APP_KEY_DEFAULT);
		private final SettingsModelString uri_settings = new SettingsModelString(OPS_target_pharmacology_pagesNodeModel.URI, OPS_target_pharmacology_pagesNodeModel.URI_DEFAULT);
		private final SettingsModelString assay_organism_settings = new SettingsModelString(OPS_target_pharmacology_pagesNodeModel.ASSAY_ORGANISM, OPS_target_pharmacology_pagesNodeModel.ASSAY_ORGANISM_DEFAULT);
		private final SettingsModelString target_organism_settings = new SettingsModelString(OPS_target_pharmacology_pagesNodeModel.TARGET_ORGANISM, OPS_target_pharmacology_pagesNodeModel.TARGET_ORGANISM_DEFAULT);
		private final SettingsModelString activity_type_settings = new SettingsModelString(OPS_target_pharmacology_pagesNodeModel.ACTIVITY_TYPE, OPS_target_pharmacology_pagesNodeModel.ACTIVITY_TYPE_DEFAULT);
		private final SettingsModelString activity_value_settings = new SettingsModelString(OPS_target_pharmacology_pagesNodeModel.ACTIVITY_VALUE, OPS_target_pharmacology_pagesNodeModel.ACTIVITY_TYPE_DEFAULT);
		private final SettingsModelString min_activity_value_settings = new SettingsModelString(OPS_target_pharmacology_pagesNodeModel.MIN_ACTIVITY_VALUE, OPS_target_pharmacology_pagesNodeModel.MIN_ACTIVITY_VALUE_DEFAULT);
		private final SettingsModelString min_ex_activity_value_settings = new SettingsModelString(OPS_target_pharmacology_pagesNodeModel.MIN_EX_ACTIVITY_VALUE, OPS_target_pharmacology_pagesNodeModel.MIN_EX_ACTIVITY_VALUE_DEFAULT);
		private final SettingsModelString max_activity_value_settings = new SettingsModelString(OPS_target_pharmacology_pagesNodeModel.MAX_ACTIVITY_VALUE, OPS_target_pharmacology_pagesNodeModel.MAX_ACTIVITY_VALUE_DEFAULT);
		private final SettingsModelString max_ex_activity_value_settings = new SettingsModelString(OPS_target_pharmacology_pagesNodeModel.MAX_EX_ACTIVITY_VALUE, OPS_target_pharmacology_pagesNodeModel.MAX_EX_ACTIVITY_VALUE_DEFAULT);
		private final SettingsModelString activity_unit_settings = new SettingsModelString(OPS_target_pharmacology_pagesNodeModel.ACTIVITY_UNIT, OPS_target_pharmacology_pagesNodeModel.ACTIVITY_UNIT_DEFAULT);
		private final SettingsModelInteger page_settings = new SettingsModelInteger(OPS_target_pharmacology_pagesNodeModel.PAGE, OPS_target_pharmacology_pagesNodeModel.PAGE_DEFAULT);
		private final SettingsModelString page_size_settings = new SettingsModelString(OPS_target_pharmacology_pagesNodeModel.PAGE_SIZE, OPS_target_pharmacology_pagesNodeModel.PAGE_SIZE_DEFAULT);
		private final SettingsModelString order_by_settings = new SettingsModelString(OPS_target_pharmacology_pagesNodeModel.ORDER_BY, OPS_target_pharmacology_pagesNodeModel.ORDER_BY_DEFAULT);


		/**
	     * Constructor for the node model.
	     */
	    protected OPS_target_pharmacology_pagesNodeModel() {
	    
	        // TODO: Specify the amount of input and output ports needed.
	        super(0, 1);
	    }

	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
	            final ExecutionContext exec) throws Exception {
	        DataColumnSpec[] allColSpecs = new DataColumnSpec[27];
	        allColSpecs[0] = 
	                new DataColumnSpecCreator("_about", StringCell.TYPE).createSpec();
	        allColSpecs[1] = 
	                new DataColumnSpecCreator("modified", StringCell.TYPE).createSpec();
	        allColSpecs[2] = 
	            new DataColumnSpecCreator("ChemblActivityURI", StringCell.TYPE).createSpec();
	        allColSpecs[3] = 
	            new DataColumnSpecCreator("pmid", StringCell.TYPE).createSpec();
	        allColSpecs[4] = 
	            new DataColumnSpecCreator("Chembl_forMolecule_URI", StringCell.TYPE).createSpec();  
	        allColSpecs[5] = 
	                new DataColumnSpecCreator("full_mwt", StringCell.TYPE).createSpec();
	        allColSpecs[6] = 
	            new DataColumnSpecCreator("DrugBankURI", StringCell.TYPE).createSpec();  
	        allColSpecs[7] = 
	            new DataColumnSpecCreator("drugType", StringCell.TYPE).createSpec();
	        allColSpecs[8] = 
	            new DataColumnSpecCreator("genericName", StringCell.TYPE).createSpec();
	        allColSpecs[9] = 
	            new DataColumnSpecCreator("conceptWikiURI", StringCell.TYPE).createSpec();
	        allColSpecs[10] = 
	            new DataColumnSpecCreator("prefLabel_en", StringCell.TYPE).createSpec();
	        allColSpecs[11] = 
	            new DataColumnSpecCreator("ChemspiderURI", StringCell.TYPE).createSpec();
	        allColSpecs[12] = 
	            new DataColumnSpecCreator("inchi", StringCell.TYPE).createSpec();
	        allColSpecs[13] = 
	            new DataColumnSpecCreator("inchikey", StringCell.TYPE).createSpec();
	        allColSpecs[14] = 
	            new DataColumnSpecCreator("smiles", StringCell.TYPE).createSpec();
	        allColSpecs[15] = 
	            new DataColumnSpecCreator("ro5_violations", StringCell.TYPE).createSpec();
	        allColSpecs[16] = 
	            new DataColumnSpecCreator("Chembl_onAssay_URI", StringCell.TYPE).createSpec();
	        allColSpecs[17] = 
	            new DataColumnSpecCreator("description", StringCell.TYPE).createSpec();
	        allColSpecs[18] = 
	                new DataColumnSpecCreator("targets", StringCell.TYPE).createSpec();
	        allColSpecs[19] = 
	                new DataColumnSpecCreator("organism", StringCell.TYPE).createSpec();
	        allColSpecs[20] = 
	                new DataColumnSpecCreator("relation", StringCell.TYPE).createSpec();
	        allColSpecs[21] = 
	                new DataColumnSpecCreator("standardUnits", StringCell.TYPE).createSpec();
	        allColSpecs[22] = 
	                new DataColumnSpecCreator("standardValue", StringCell.TYPE).createSpec();
	        allColSpecs[23] = 
	                new DataColumnSpecCreator("activity_type", StringCell.TYPE).createSpec();
	        allColSpecs[24] = 
	                new DataColumnSpecCreator("activity_value", StringCell.TYPE).createSpec();

	        allColSpecs[25] = 
	                new DataColumnSpecCreator("first", StringCell.TYPE).createSpec();
	        allColSpecs[26] = 
	                new DataColumnSpecCreator("next", StringCell.TYPE).createSpec();
	 
	       
	        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
	        // the execution context will provide us with storage capacity, in this
	        // case a data container to which we will add rows sequentially
	        // Note, this container can also handle arbitrary big data tables, it
	        // will buffer to disc if necessary.
	        BufferedDataContainer container = exec.createDataContainer(outputSpec);
	        URL requestURL = buildRequestURL();
	        System.out.println(requestURL.toString());
	        JSONObject json = this.grabSomeJson(requestURL);
	        
	        System.out.println(json.toString());
	        JSONObject result = (JSONObject)  json.get("result");
	        
	     
	        
	        
			DataCell[] cells = new DataCell[27];
			cells[0] = new StringCell(getCellFromJSON("_about",result));

			cells[1] = new StringCell(getCellFromJSON("modified",result));
	    	JSONArray items = (JSONArray)  result.get("items");
	    	
	    	
	    	RowKey key =null;
	    	for (int i = 0; i < items.size(); i++) {
	    		key =   new RowKey("result_"+i);
	    	
	    		if(items.get(i).getClass().getName().equals("net.sf.json.JSONObject")){
	    			JSONObject item = items.getJSONObject(i);
	    			cells[2] = new StringCell(getCellFromJSON("_about",item));
	    			cells[3] = new StringCell(getCellFromJSON("pmid",item));
	    			JSONObject forMolecule = item.getJSONObject("forMolecule");
	    			if(forMolecule !=null){
	    				cells[4] = new StringCell(getCellFromJSON("_about",forMolecule));
	        			cells[5] = new StringCell(getCellFromJSON("full_mwt",forMolecule));
	        			JSONArray exactMatch = forMolecule.getJSONArray("exactMatch");
	        			if(exactMatch !=null){
	        				for (int j = 0; j < exactMatch.size(); j++) {
	        					JSONObject exactMatchObject = exactMatch.getJSONObject(j);
	        					if(exactMatchObject.containsValue("http://linkedlifedata.com/resource/drugbank" )){
	        	        			
	        	        			cells[6] = new StringCell(getCellFromJSON("_about",exactMatchObject));
	        	        			cells[7] = new StringCell(getCellFromJSON("drugType",exactMatchObject));
	        	        			cells[8] = new StringCell(getCellFromJSON("genericName",exactMatchObject));
	        	        		} else if(exactMatchObject.containsValue("http://www.conceptwiki.org/" )){
	        	        			cells[9] = new StringCell(getCellFromJSON("_about",exactMatchObject));
	        	        			cells[10] = new StringCell(getCellFromJSON("prefLabel_en",exactMatchObject));	
	        	        		} else if(exactMatchObject.containsValue("http://rdf.chemspider.com/" )){
	        	        			cells[11] = new StringCell(getCellFromJSON("_about",exactMatchObject));
	        	        			cells[12] = new StringCell(getCellFromJSON("inchi",exactMatchObject));
	        	        			cells[13] = new StringCell(getCellFromJSON("inchikey",exactMatchObject));
	        	        			cells[14] = new StringCell(getCellFromJSON("smiles",exactMatchObject));	
	        	        			cells[15] = new StringCell(getCellFromJSON("ro5_violations",exactMatchObject));	
	        	        		}
	        				}
	        	
	        			}

	    			}
	    			JSONObject onAssay = item.getJSONObject("onAssay");
	    			if(onAssay !=null){
	    				cells[16] = new StringCell(getCellFromJSON("_about",onAssay));
	        			cells[17] = new StringCell(getCellFromJSON("description",onAssay));
	        			
	        			String target = onAssay.getString("target").toString();
	        			if(target!=null){
	        				cells[18]= new StringCell(target);
	        			}
	        			cells[19] = new StringCell(getCellFromJSON("organism",onAssay));
	    			}
	    			cells[20] = new StringCell(getCellFromJSON("relation",item));
	    			cells[21] = new StringCell(getCellFromJSON("standardUnits",item));
	    			cells[22] = new StringCell(getCellFromJSON("standardValue",item));
	    			cells[23] = new StringCell(getCellFromJSON("activity_type",item));
	    			cells[24] = new StringCell(getCellFromJSON("activity_value",item));
	    		}
	    		cells[25] =  new StringCell(getCellFromJSON("first",result));
	        	cells[26] =  new StringCell(getCellFromJSON("next",result));
	            DataRow row = new DefaultRow(key, cells);
	            container.addRowToTable(row);
	    	}
	    	
	        container.close();
	        BufferedDataTable out = container.getTable();
	       
	        return new BufferedDataTable[]{out};
	    }

	    private String getCellFromJSON(String key,JSONObject item){
	    	String result = "";
	    	System.out.println("item: "+ item.toString());
	    	if(item.get(key)!=null && !(item.get(key).getClass().getName().equals("net.sf.json.JSONArray")||item.get(key).getClass().getName().equals("net.sf.json.JSONObject"))){
	    		return item.getString(key);
	    	}else if(item.get(key)!=null){
	    		
	    		return item.get(key).toString();
	    	}
		    return result;
	    }
	    
	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    protected void reset() {
	        // TODO Code executed on reset.
	        // Models build during execute are cleared here.
	        // Also data handled in load/saveInternals will be erased here.
	    }

	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
	            throws InvalidSettingsException {
	        
	        // TODO: check if user settings are available, fit to the incoming
	        // table structure, and the incoming types are feasible for the node
	        // to execute. If the node can execute in its current state return
	        // the spec of its output data table(s) (if you can, otherwise an array
	        // with null elements), or throw an exception with a useful user message

	        return new DataTableSpec[]{null};
	    }

	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    protected void saveSettingsTo(final NodeSettingsWO settings) {

	    	api_settings.saveSettingsTo(settings);
	    	app_id_settings.saveSettingsTo(settings);
	    	app_key_settings.saveSettingsTo(settings);
	    	uri_settings.saveSettingsTo(settings);
	    	assay_organism_settings.saveSettingsTo(settings);
	    	target_organism_settings.saveSettingsTo(settings);
	    	activity_type_settings.saveSettingsTo(settings);
	    	activity_value_settings.saveSettingsTo(settings);
	    	min_activity_value_settings.saveSettingsTo(settings);
	    	min_ex_activity_value_settings.saveSettingsTo(settings);
	    	max_activity_value_settings.saveSettingsTo(settings);
	    	max_ex_activity_value_settings.saveSettingsTo(settings);
	    	activity_unit_settings.saveSettingsTo(settings);
	    	page_settings.saveSettingsTo(settings);
	    	page_size_settings.saveSettingsTo(settings);
	    	order_by_settings.saveSettingsTo(settings);
	       

	    }

	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
	            throws InvalidSettingsException {
	            
	    	api_settings.loadSettingsFrom(settings);
	    	app_id_settings.loadSettingsFrom(settings);
	    	app_key_settings.loadSettingsFrom(settings);
	    	uri_settings.loadSettingsFrom(settings);
	    	assay_organism_settings.loadSettingsFrom(settings);
	    	target_organism_settings.loadSettingsFrom(settings);
	    	activity_type_settings.loadSettingsFrom(settings);
	    	activity_value_settings.loadSettingsFrom(settings);
	    	min_activity_value_settings.loadSettingsFrom(settings);
	    	min_ex_activity_value_settings.loadSettingsFrom(settings);
	    	max_activity_value_settings.loadSettingsFrom(settings);
	    	max_ex_activity_value_settings.loadSettingsFrom(settings);
	    	activity_unit_settings.loadSettingsFrom(settings);
	    	page_settings.loadSettingsFrom(settings);
	    	page_size_settings.loadSettingsFrom(settings);
	    	order_by_settings.loadSettingsFrom(settings);


	    }

	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    protected void validateSettings(final NodeSettingsRO settings)
	            throws InvalidSettingsException {
	            
	        // TODO check if the settings could be applied to our model
	        // e.g. if the count is in a certain range (which is ensured by the
	        // SettingsModel).
	        // Do not actually set any values of any member variables.

	       // m_count.validateSettings(settings);
	    	
	    	//System.out.println("validating settings");
	    	api_settings.validateSettings(settings);
	    	app_id_settings.validateSettings(settings);
	    	app_key_settings.validateSettings(settings);
	    	uri_settings.validateSettings(settings);
	    	assay_organism_settings.validateSettings(settings);
	    	target_organism_settings.validateSettings(settings);
	    	activity_type_settings.validateSettings(settings);
	    	activity_value_settings.validateSettings(settings);
	    	min_activity_value_settings.validateSettings(settings);
	    	min_ex_activity_value_settings.validateSettings(settings);
	    	max_activity_value_settings.validateSettings(settings);
	    	max_ex_activity_value_settings.validateSettings(settings);
	    	activity_unit_settings.validateSettings(settings);
	    	page_settings.validateSettings(settings);
	    	page_size_settings.validateSettings(settings);
	    	order_by_settings.validateSettings(settings);
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
	    protected URL buildRequestURL() throws URISyntaxException, MalformedURLException, UnsupportedEncodingException  
	    {
	       	URI hosturi = new URI(api_settings.getStringValue());
	       	String app_id_string =  "app_id="+app_id_settings.getStringValue();
	    	    	
	    	String url_str = "https://" + hosturi.getHost() + "/compound/pharmacology/pages?" + app_id_string+getURIParams();
	    	
	   
	    	
	    	System.out.println("URL " + url_str);
	    	URI uri = new URI(url_str);
	    	return uri.toURL();
	    }
	    
	    
	    private String getURIParams(){
	    	String result ="";
	    	
	    	result += getURIParam(app_key_settings);
	    	result += getURIParam(uri_settings);
	    	result += getURIParam(assay_organism_settings);
	    	result += getURIParam(target_organism_settings);
	    	result += getURIParam(activity_type_settings);
	    	result += getURIParam(activity_value_settings);
	    	result += getURIParam(min_activity_value_settings);
	    	result += getURIParam(min_ex_activity_value_settings);
	    	result += getURIParam(max_activity_value_settings);
	    	result += getURIParam(max_ex_activity_value_settings);
	    	result += getURIParam(activity_unit_settings);
	    	result += getURIParamInteger(page_settings);
	    	result += getURIParam(page_size_settings);
	    	result += getURIParam(order_by_settings);

	    	return result;
	    }
	    private String getURIParam(SettingsModelString setting){
	    	
	    	String result = "";
	    	if(!(setting.getStringValue().equals(""))){
	    		return "&"+setting.getKey()+"="+URLEncoder.encode(setting.getStringValue());
	    	}
	    	return "";
	    }
	    private String getURIParamInteger(SettingsModelInteger setting){
	    	
	    	String result = "";
	    	if(!(setting.getIntValue() >=1)){
	    		return "&"+setting.getKey()+"="+URLEncoder.encode(""+ setting.getIntValue());
	    	}
	    	return "";
	    }
	    protected JSONObject grabSomeJson(URL url) throws IOException
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

	}

