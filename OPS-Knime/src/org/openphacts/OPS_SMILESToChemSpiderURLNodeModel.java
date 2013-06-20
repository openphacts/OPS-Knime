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

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
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
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * This is the model implementation of OPS_SMILESToChemSpiderURL.
 * Returns a ChemSpider URL corresponding to an input SMILES string. Driven by ChemSpider.
 *
 * @author Ronald Siebes - VUA
 */
public class OPS_SMILESToChemSpiderURLNodeModel extends NodeModel {
    
	 public static final String API_URL = "uri";
		public static final String DEFAULT_API_URL = "https://beta.openphacts.org";
		public static final String APP_ID_DEFAULT = "15a18100";
		public static final String APP_ID = "app_id";
		public static final String APP_KEY_DEFAULT = "528a8272f1cd961d215f318a0315dd3d";
		public static final String APP_KEY = "app_key";		

		private final SettingsModelString api_settings = new SettingsModelString(OPS_SMILESToChemSpiderURLNodeModel.API_URL, OPS_SMILESToChemSpiderURLNodeModel.DEFAULT_API_URL);
		private final SettingsModelString app_id_settings = new SettingsModelString(OPS_SMILESToChemSpiderURLNodeModel.APP_ID, OPS_SMILESToChemSpiderURLNodeModel.APP_ID_DEFAULT);
		private final SettingsModelString app_key_settings = new SettingsModelString(OPS_SMILESToChemSpiderURLNodeModel.APP_KEY, OPS_SMILESToChemSpiderURLNodeModel.APP_KEY_DEFAULT);
		

		/**
	     * Constructor for the node model.
	     */
	    protected OPS_SMILESToChemSpiderURLNodeModel() {
	    
	        // TODO: Specify the amount of input and output ports needed.
	        super(1, 1);
	    }

	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
	            final ExecutionContext exec) throws Exception {
	        DataColumnSpec[] allColSpecs = new DataColumnSpec[1];
	        allColSpecs[0] = 
	                new DataColumnSpecCreator("ChemSpiderURL", StringCell.TYPE).createSpec();
	        
	        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
	        // the execution context will provide us with storage capacity, in this
	        // case a data container to which we will add rows sequentially
	        // Note, this container can also handle arbitrary big data tables, it
	        // will buffer to disc if necessary.
	        BufferedDataContainer container = exec.createDataContainer(outputSpec);
	        String smiles= inData[0].iterator().next().getCell(0).toString(); // ugly...needs definitely some array bound checks here

	        URL requestURL = buildRequestURL(smiles);
	        System.out.println(requestURL.toString());
	        JSONObject json = this.grabSomeJson(requestURL);
	        DataCell[] cells = new DataCell[1];
	        System.out.println(json.toString());
	        JSONObject result = (JSONObject)  json.get("result");
	        if(result!=null){
	        	JSONObject primaryTopic= result.getJSONObject("primaryTopic");
			if(primaryTopic!=null){
				cells[0] = new StringCell(getCellFromJSON("_about",primaryTopic));
			}
	        }
	        DataRow row = new DefaultRow("aboutCell", cells);
	        container.addRowToTable(row);
	    	
	    	
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
	    protected URL buildRequestURL(String smiles) throws URISyntaxException, MalformedURLException, UnsupportedEncodingException  
	    {
	       	URI hosturi = new URI(api_settings.getStringValue());
	       	String app_id_string =  "app_id="+app_id_settings.getStringValue();
	    	    	
	    	String url_str = "https://" + hosturi.getHost() + "/structure?" + app_id_string+getURIParams()+"&smiles="+smiles;
	    	
	   
	    	
	    	System.out.println("URL " + url_str);
	    	URI uri = new URI(url_str);
	    	return uri.toURL();
	    }
	    
	    
	    private String getURIParams(){
	    	String result ="";
	    	
	    	result += getURIParam(app_key_settings);

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

