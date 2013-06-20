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
 * This is the model implementation of OPS_target.
 * 
 *
 * @author openphacts
 */
public class OPS_targetNodeModel extends NodeModel {
    
    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(OPS_targetNodeModel.class);
        
    
	public static final String API_URL = "OpenPhactsURL";
    public static final String DEFAULT_API_URL = "https://beta.openphacts.org";
    
	public static final String APP_ID = "app_id";
	public static final String APP_ID_DEFAULT = "69ac6ae3";

	public static final String APP_KEY = "app_key";
	public static final String APP_KEY_DEFAULT = "08731c119b4abbf8ea95128c3e4264a8";

	public static final String URI = "uri";
	public static final String URI_DEFAULT = "http://www.conceptwiki.org/concept/00059958-a045-4581-9dc5-e5a08bb0c291";
	
	
    private final SettingsModelString api_settings = new SettingsModelString(OPS_targetNodeModel.API_URL, OPS_targetNodeModel.DEFAULT_API_URL);
    private final SettingsModelString app_id_settings = new SettingsModelString(OPS_targetNodeModel.APP_ID, OPS_targetNodeModel.APP_ID_DEFAULT);
    private final SettingsModelString app_key_settings = new SettingsModelString(OPS_targetNodeModel.APP_KEY, OPS_targetNodeModel.APP_KEY_DEFAULT);


    /**
     * Constructor for the node model.
     */
    protected OPS_targetNodeModel() {

    	
        // TODO one incoming port and one outgoing port is assumed
        super(1, 1);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
        DataColumnSpec[] allColSpecs = new DataColumnSpec[18];
        allColSpecs[0] = 
            new DataColumnSpecCreator("_about", StringCell.TYPE).createSpec();
        allColSpecs[1] = 
            new DataColumnSpecCreator("prefLabel_en", StringCell.TYPE).createSpec();
        allColSpecs[2] = 
            new DataColumnSpecCreator("UniProtURI", StringCell.TYPE).createSpec();  
        allColSpecs[3] = 
                new DataColumnSpecCreator("Function_Annotation", StringCell.TYPE).createSpec();
        allColSpecs[4] = 
            new DataColumnSpecCreator("alternativeName(s)", StringCell.TYPE).createSpec();  
        allColSpecs[5] = 
            new DataColumnSpecCreator("classifiedWith", StringCell.TYPE).createSpec();
        allColSpecs[6] = 
            new DataColumnSpecCreator("existence", StringCell.TYPE).createSpec();
        allColSpecs[7] = 
            new DataColumnSpecCreator("organism", StringCell.TYPE).createSpec();
        allColSpecs[8] = 
            new DataColumnSpecCreator("sequence", StringCell.TYPE).createSpec();
        allColSpecs[9] = 
            new DataColumnSpecCreator("DrugBankURI", StringCell.TYPE).createSpec();
        allColSpecs[10] = 
            new DataColumnSpecCreator("cellularLocation", StringCell.TYPE).createSpec();
        allColSpecs[11] = 
            new DataColumnSpecCreator("molecularWeight", StringCell.TYPE).createSpec();
        allColSpecs[12] = 
            new DataColumnSpecCreator("numberOfResidues", StringCell.TYPE).createSpec();
        allColSpecs[13] = 
            new DataColumnSpecCreator("theoreticalPi", StringCell.TYPE).createSpec();
        allColSpecs[14] = 
            new DataColumnSpecCreator("ChemblURI", StringCell.TYPE).createSpec();
        allColSpecs[15] = 
            new DataColumnSpecCreator("description", StringCell.TYPE).createSpec();
        allColSpecs[16] = 
                new DataColumnSpecCreator("keyword", StringCell.TYPE).createSpec();
        allColSpecs[17] = 
                new DataColumnSpecCreator("subClassOf", StringCell.TYPE).createSpec();

       
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        // the execution context will provide us with storage capacity, in this
        // case a data container to which we will add rows sequentially
        // Note, this container can also handle arbitrary big data tables, it
        // will buffer to disc if necessary.
        BufferedDataContainer container = exec.createDataContainer(outputSpec);
        String c_uri= inData[0].iterator().next().getCell(0).toString(); // ugly...needs definitely some array bound checks here

        URL requestURL = buildRequestURL(c_uri);
        JSONObject json = this.grabSomeJson(requestURL);
        
        System.out.println(json.toString());
        JSONObject result = (JSONObject)  json.get("result");
        
        JSONObject primaryTopic = (JSONObject)  result.get("primaryTopic");
        
        RowKey key = new RowKey("result");
		DataCell[] cells = new DataCell[18];
		cells[0] = new StringCell(getCellFromJSON("_about",primaryTopic));

		cells[1] = new StringCell(getCellFromJSON("prefLabel_en",primaryTopic));
    	JSONArray items = (JSONArray)  primaryTopic.get("exactMatch");
    	
    	
    	
    	for (int i = 0; i < items.size(); i++) {
    		
    	
    		if(items.get(i).getClass().getName().equals("net.sf.json.JSONObject")){
    			JSONObject item = items.getJSONObject(i);

        		if(item.containsValue("http://purl.uniprot.org" )){
        			cells[2] = new StringCell(getCellFromJSON("_about",item));
        			cells[3] = new StringCell(getCellFromJSON("Function_Annotation",item));
        			cells[4] = new StringCell(getCellFromJSON("alternativeName",item));
        			cells[5] = new StringCell(getCellFromJSON("classifiedWith",item));
        			cells[6] = new StringCell(getCellFromJSON("existence",item));
        			cells[7] = new StringCell(getCellFromJSON("organism",item));
        			cells[8] = new StringCell(getCellFromJSON("sequence",item));
        		} else if(item.containsValue("http://linkedlifedata.com/resource/drugbank" )){
        			cells[9] = new StringCell(getCellFromJSON("_about",item));
        			cells[10] = new StringCell(getCellFromJSON("cellularLocation",item));
        			cells[11] = new StringCell(getCellFromJSON("molecularWeight",item));
        			cells[12] = new StringCell(getCellFromJSON("numberOfResidues",item));
        			cells[13] = new StringCell(getCellFromJSON("theoreticalPi",item));		
        		} else if(item.containsValue("http://data.kasabi.com/dataset/chembl-rdf" )){
        			cells[14] = new StringCell(getCellFromJSON("_about",item));
        			cells[15] = new StringCell(getCellFromJSON("description",item));
        			cells[16] = new StringCell(getCellFromJSON("keyword",item));
        			cells[17] = new StringCell(getCellFromJSON("subClassOf",item));	
        		}
    		}
    		
    		

    	}
        DataRow row = new DefaultRow(key, cells);
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
    protected URL buildRequestURL(String c_uri) throws URISyntaxException, MalformedURLException, UnsupportedEncodingException  
    {
    	//"http://ops.few.vu.nl/target/enzyme/pharmacology/pages?uri=http%3A%2F%2Fpurl.uniprot.org%2Fenzyme%2F1.1.-.-&activity_type=Potency&maxEx-activity_value=10&minEx-activity_value=4&assay_organism=Homo%20sapiens&_page=1";
        
    	URI hosturi = new URI(api_settings.getStringValue());
    	
    	String app_id = URLEncoder.encode(app_id_settings.getStringValue(),"UTF-8"); 
    	//String family = family_settings.getStringValue();
    	

    	String queryStr = "app_id=" + app_id + "&";
    	queryStr = queryStr + "app_key=" + app_key_settings.getStringValue() +"&";
    	queryStr = queryStr + "uri=" + URLEncoder.encode(c_uri,"UTF-8");
    	//queryStr = queryStr + "_page=1";
    	
    	String url_str = "https://" + hosturi.getHost() + "/target?" + queryStr;
    	
   
    	
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

