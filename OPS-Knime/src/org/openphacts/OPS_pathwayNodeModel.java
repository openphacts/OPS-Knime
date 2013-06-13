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
import java.util.HashMap;
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
 * This is the model implementation of OPS_pathway.
 * 
 *
 * @author openphacts
 */
public class OPS_pathwayNodeModel extends NodeModel {
    
    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(OPS_pathwayNodeModel.class);
        
    
	public static final String API_URL = "OpenPhactsURL";
    public static final String DEFAULT_API_URL = "https://beta.openphacts.org";
    
	public static final String APP_ID = "app_id";
	public static final String APP_ID_DEFAULT = "69ac6ae3";

	public static final String APP_KEY = "app_key";
	public static final String APP_KEY_DEFAULT = "08731c119b4abbf8ea95128c3e4264a8";

	public static final String URI = "uri";
	public static final String URI_DEFAULT = "http://rdf.wikipathways.org/WP1019_r48131";
	
	
    private final SettingsModelString api_settings = new SettingsModelString(OPS_pathwayNodeModel.API_URL, OPS_pathwayNodeModel.DEFAULT_API_URL);
    private final SettingsModelString app_id_settings = new SettingsModelString(OPS_pathwayNodeModel.APP_ID, OPS_pathwayNodeModel.APP_ID_DEFAULT);
    private final SettingsModelString app_key_settings = new SettingsModelString(OPS_pathwayNodeModel.APP_KEY, OPS_pathwayNodeModel.APP_KEY_DEFAULT);
    private final SettingsModelString uri_settings = new SettingsModelString(OPS_pathwayNodeModel.URI, OPS_pathwayNodeModel.URI_DEFAULT);


    /**
     * Constructor for the node model.
     */
    protected OPS_pathwayNodeModel() {

    	
        // TODO one incoming port and one outgoing port is assumed
        super(0, 2);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
        DataColumnSpec[] allColSpecs = new DataColumnSpec[7];
        allColSpecs[0] = 
            new DataColumnSpecCreator("WikiPathwaysURI", StringCell.TYPE).createSpec();
        allColSpecs[1] = 
            new DataColumnSpecCreator("identifier", StringCell.TYPE).createSpec();
        allColSpecs[2] = 
            new DataColumnSpecCreator("title_en", StringCell.TYPE).createSpec();  
        allColSpecs[3] = 
            new DataColumnSpecCreator("title", StringCell.TYPE).createSpec();
        allColSpecs[4] = 
            new DataColumnSpecCreator("description", StringCell.TYPE).createSpec();
        allColSpecs[5] = 
            new DataColumnSpecCreator("organism_about", StringCell.TYPE).createSpec();
        allColSpecs[6] = 
            new DataColumnSpecCreator("organism_label", StringCell.TYPE).createSpec();
       
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        // the execution context will provide us with storage capacity, in this
        // case a data container to which we will add rows sequentially
        // Note, this container can also handle arbitrary big data tables, it
        // will buffer to disc if necessary.
        BufferedDataContainer container = exec.createDataContainer(outputSpec);
        
        
        DataColumnSpec[] allColSpecs2 = new DataColumnSpec[2];
        allColSpecs2[0] = 
            new DataColumnSpecCreator("_about", StringCell.TYPE).createSpec();
        allColSpecs2[1] = 
            new DataColumnSpecCreator("type", StringCell.TYPE).createSpec();

       
        DataTableSpec outputSpec2 = new DataTableSpec(allColSpecs2);
        // the execution context will provide us with storage capacity, in this
        // case a data container to which we will add rows sequentially
        // Note, this container can also handle arbitrary big data tables, it
        // will buffer to disc if necessary.
        BufferedDataContainer container2 = exec.createDataContainer(outputSpec2);
        
        URL requestURL = buildRequestURL();
        JSONObject json = this.grabSomeJson(requestURL);
        
        System.out.println(json.toString());
        JSONObject result = (JSONObject)  json.get("result");
        
        JSONObject primaryTopic = (JSONObject)  result.get("primaryTopic");
        
        RowKey key = new RowKey("result");
		DataCell[] cells = new DataCell[7];
		cells[0] = new StringCell(getCellFromJSON("_about",primaryTopic));
		
		cells[1] = new StringCell(getCellFromJSON("identifier",primaryTopic));
		cells[2] = new StringCell(getCellFromJSON("title_en",primaryTopic));
		cells[3] = new StringCell(getCellFromJSON("title",primaryTopic));
		cells[4] = new StringCell(getCellFromJSON("description",primaryTopic));
    	JSONObject organism = (JSONObject)  primaryTopic.get("organism");
		cells[5] = new StringCell(getCellFromJSON("_about",organism));
		cells[6] = new StringCell(getCellFromJSON("type",organism));
	    JSONArray items2 = (JSONArray)  primaryTopic.get("hasPart");

    	for (int i = 0; i < items2.size(); i++) {
    		RowKey key2 = new RowKey("hasPart_"+i);
    		DataCell[] cells2 = new DataCell[2];
    		cells2[0] = new StringCell(((JSONObject)items2.getJSONObject(i)).getString("_about"));
    		cells2[1] = new StringCell(((JSONObject)items2.getJSONObject(i)).getString("type"));
    		 DataRow row2 = new DefaultRow(key2, cells2);
    		 container2.addRowToTable(row2);
    		
    		

    	}
    	 DataRow row = new DefaultRow(key, cells);
		 container.addRowToTable(row);
         container.close();
        container2.close();
        BufferedDataTable out = container.getTable();
        BufferedDataTable out2 = container2.getTable();
       
        return new BufferedDataTable[]{out,out2};
    }

    private String getCellFromJSON(String key,JSONObject item){
    	String result = "";
    	System.out.println("item: "+ item.toString());
    	if(item.get(key)!=null && !(item.get(key).getClass().getName().equals("net.sf.json.JSONArray")||item.get(key).getClass().getName().equals("net.sf.json.JSONObject"))){
    		return item.getString(key);
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

        return new DataTableSpec[]{null,null};
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
    	//"http://ops.few.vu.nl/target/enzyme/pharmacology/pages?uri=http%3A%2F%2Fpurl.uniprot.org%2Fenzyme%2F1.1.-.-&activity_type=Potency&maxEx-activity_value=10&minEx-activity_value=4&assay_organism=Homo%20sapiens&_page=1";
        
    	URI hosturi = new URI(api_settings.getStringValue());
    	
    	String app_id = URLEncoder.encode(app_id_settings.getStringValue(),"UTF-8"); 
    	//String family = family_settings.getStringValue();
    	

    	String queryStr = "app_id=" + app_id + "&";
    	queryStr = queryStr + "app_key=" + app_key_settings.getStringValue() +"&";
    	queryStr = queryStr + "uri=" + URLEncoder.encode(uri_settings.getStringValue(),"UTF-8");
    	//queryStr = queryStr + "_page=1";
    	
    	String url_str = "https://" + hosturi.getHost() + "/pathway?" + queryStr;
    	
   
    	
    	System.out.println("URL " + url_str);
    	URI uri = new URI(url_str);
    	return uri.toURL();
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

