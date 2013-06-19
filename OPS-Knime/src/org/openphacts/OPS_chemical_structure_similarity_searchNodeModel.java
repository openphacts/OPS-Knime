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
import org.knime.core.node.defaultnodesettings.SettingsModelDouble;
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
 * This is the model implementation of OPS_chemical_structure_similarity_search.
 * Returns a set of ChemSpider compound URLs, similar to the input molecule according to the specified algorithm and threshold. Driven by ChemSpider.
 *
 * @author Ronald Siebes - VUA
 */
public class OPS_chemical_structure_similarity_searchNodeModel extends NodeModel {
    
    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(OPS_chemical_structure_similarity_searchNodeModel.class);
        
    /** the settings key which is used to retrieve and 
        store the settings (from the dialog or from a settings file)    
       (package visibility to be usable from the dialog). */
	static final String CFGKEY_COUNT = "Count";

    /** initial default count value. */
    static final int DEFAULT_COUNT = 100;

    public static final String API_URL = "uri";
	public static final String DEFAULT_API_URL = "https://beta.openphacts.org";
	public static final String APP_ID_DEFAULT = "15a18100";
	public static final String APP_ID = "app_id";
	public static final String APP_KEY_DEFAULT = "528a8272f1cd961d215f318a0315dd3d";
	public static final String APP_KEY = "app_key";
	public static final String MOLECULE = "searchOptions.Molecule";
	public static final String MOLECULE_DEFAULT = "CC(=O)Oc1ccccc1C(=O)O";
	public static final String SIMILARITY_TYPE = "searchOptions.SimilarityType";
	private static final Map<String,String> SimilarityTypeMap = new HashMap<String,String>();
	public static final String SIMILARITY_TYPE_DEFAULT = "Tanimoto";
	public static final String THRESHOLD = "searchOptions.Threshold";
	public static final String LIMIT = "resultOptions.Limit";
	public static final String START = "resultOptions.Start";
	public static final String LENGTH = "resultOptions.Length";
	public static final double THRESHOLD_DEFAULT = 0.2;
	public static final int LIMIT_DEFAULT = 20;
	public static final int START_DEFAULT = 0;
	public static final int LENGTH_DEFAULT = 10;

	private final SettingsModelString api_settings = new SettingsModelString(OPS_chemical_structure_similarity_searchNodeModel.API_URL, OPS_chemical_structure_similarity_searchNodeModel.DEFAULT_API_URL);
	private final SettingsModelString app_id_settings = new SettingsModelString(OPS_chemical_structure_similarity_searchNodeModel.APP_ID, OPS_chemical_structure_similarity_searchNodeModel.APP_ID_DEFAULT);
	private final SettingsModelString app_key_settings = new SettingsModelString(OPS_chemical_structure_similarity_searchNodeModel.APP_KEY, OPS_chemical_structure_similarity_searchNodeModel.APP_KEY_DEFAULT);
	private final SettingsModelString molecule_settings = new SettingsModelString(OPS_chemical_structure_similarity_searchNodeModel.MOLECULE, OPS_chemical_structure_similarity_searchNodeModel.MOLECULE_DEFAULT);
	private final SettingsModelString similarity_type_settings = new SettingsModelString(OPS_chemical_structure_similarity_searchNodeModel.SIMILARITY_TYPE, OPS_chemical_structure_similarity_searchNodeModel.SIMILARITY_TYPE_DEFAULT);
	private final SettingsModelDouble threshold_settings = new SettingsModelDouble(OPS_chemical_structure_similarity_searchNodeModel.THRESHOLD, OPS_chemical_structure_similarity_searchNodeModel.THRESHOLD_DEFAULT);
	private final SettingsModelInteger limit_settings = new SettingsModelInteger(OPS_chemical_structure_similarity_searchNodeModel.LIMIT, OPS_chemical_structure_similarity_searchNodeModel.LIMIT_DEFAULT);
	private final SettingsModelInteger start_settings = new SettingsModelInteger(OPS_chemical_structure_similarity_searchNodeModel.START, OPS_chemical_structure_similarity_searchNodeModel.START_DEFAULT);
	private final SettingsModelInteger length_settings = new SettingsModelInteger(OPS_chemical_structure_similarity_searchNodeModel.LENGTH, OPS_chemical_structure_similarity_searchNodeModel.LENGTH_DEFAULT);

	/**
     * Constructor for the node model.
     */
    protected OPS_chemical_structure_similarity_searchNodeModel() {
    
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
        String compoundUri= inData[0].iterator().next().getCell(0).toString(); // ugly...needs definitely some array bound checks here

        URL requestURL = buildRequestURL(compoundUri);
        System.out.println(requestURL.toString());
        JSONObject json = this.grabSomeJson(requestURL);
        
        System.out.println(json.toString());
        JSONObject result = (JSONObject)  json.get("result");
        DataCell[] cells = new DataCell[1];
        if(result !=null){
        	JSONObject primaryTopic = result.getJSONObject("primaryTopic");
        	if(primaryTopic !=null){
        		JSONArray chemSpiderURLs = primaryTopic.getJSONArray("result");
        		if(chemSpiderURLs !=null){
        			RowKey key =null;
        	    	for (int i = 0; i < chemSpiderURLs.size(); i++) {
        	    		key =   new RowKey("chemSpiderURL_"+i);
        	    		cells[0] = new StringCell(chemSpiderURLs.getString(i));
        	    		DataRow row = new DefaultRow(key, cells);
        	    		container.addRowToTable(row);
        	    	}
        		}
        	}
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
    	molecule_settings.saveSettingsTo(settings);
    	similarity_type_settings.saveSettingsTo(settings);
    	threshold_settings.saveSettingsTo(settings);
    	limit_settings.saveSettingsTo(settings);
    	start_settings.saveSettingsTo(settings);
    	length_settings.saveSettingsTo(settings);
       

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
    	molecule_settings.loadSettingsFrom(settings);
    	similarity_type_settings.loadSettingsFrom(settings);
    	threshold_settings.loadSettingsFrom(settings);
    	limit_settings.loadSettingsFrom(settings);
    	start_settings.loadSettingsFrom(settings);
    	length_settings.loadSettingsFrom(settings);


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
    	molecule_settings.validateSettings(settings);
    	similarity_type_settings.validateSettings(settings);
    	threshold_settings.validateSettings(settings);
    	limit_settings.validateSettings(settings);
    	start_settings.validateSettings(settings);
    	length_settings.validateSettings(settings);
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
    	    	
    	String url_str = "https://" + hosturi.getHost() + "/structure/similarity?" + app_id_string+getURIParams()+"&searchOptions.Molecule="+URLEncoder.encode(smiles);
    	
   
    	
    	System.out.println("URL " + url_str);
    	URI uri = new URI(url_str);
    	return uri.toURL();
    }
    
    
    private String getURIParams(){
    	String result ="";
    	
    	result += getURIParam(app_key_settings);
    	//result += getURIParam(molecule_settings);//moved dialog option to input parameter
    	result += getURIParam(similarity_type_settings);
    	result += getURIParamDouble(threshold_settings);
    	result += getURIParamInteger(limit_settings);
    	result += getURIParamInteger(start_settings);
    	result += getURIParamInteger(length_settings);

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
    	return "&"+setting.getKey()+"="+ setting.getIntValue();
    }
    private String getURIParamDouble(SettingsModelDouble setting){
    	return "&"+setting.getKey()+"="+ setting.getDoubleValue();
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

