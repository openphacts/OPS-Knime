package org.openphacts;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Iterator;

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
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeLogger.LEVEL;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;



/**
 * This is the model implementation of OpenPhactsData.
 * 
 *
 * @author OpenPhacts
 */
public class OpenPhactsDataNodeModel extends NodeModel {
    
    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(OpenPhactsDataNodeModel.class);
        
    /** the settings key which is used to retrieve and 
        store the settings (from the dialog or from a settings file)    
       (package visibility to be usable from the dialog). */
	public static final String API_URL = "OpenPhactsURL";

    /** initial default count value. */
    public static final String DEFAULT_API_URL = "http://ops.few.vu.nl/";

    public static final String FAMILY_URI = "input_uri";
    public static final String DEFAULT_FAMILY_URI = "http://purl.uniprot.org/enzyme/1.1.-.-";

    public static final String ACTIVITY = "activity";
    public static final String DEFAULT_ACTIVITY = "Potency";
    

    public static final String ORGANISM = "organism";
    public static final String DEFAULT_ORGANISM = "Homo sapiens";


    public static final String MAX_ACT = "maxact";
    public static final String DEFAULT_MAX_ACT = "10";
    
    public static final String MIN_ACT = "minact";
    public static final String DEFAULT_MIN_ACT = "4";
    
    
    private final SettingsModelString api_settings = new SettingsModelString(OpenPhactsDataNodeModel.API_URL, OpenPhactsDataNodeModel.DEFAULT_API_URL);
    private final SettingsModelString family_settings = new SettingsModelString(OpenPhactsDataNodeModel.FAMILY_URI, OpenPhactsDataNodeModel.DEFAULT_FAMILY_URI);
    private final SettingsModelString activity_settings = new SettingsModelString(OpenPhactsDataNodeModel.ACTIVITY, OpenPhactsDataNodeModel.DEFAULT_ACTIVITY);
    private final SettingsModelString organism_settings = new SettingsModelString(OpenPhactsDataNodeModel.ORGANISM, OpenPhactsDataNodeModel.DEFAULT_ORGANISM);
    private final SettingsModelString max_settings = new SettingsModelString(OpenPhactsDataNodeModel.MAX_ACT, OpenPhactsDataNodeModel.DEFAULT_MAX_ACT);
    private final SettingsModelString min_settings = new SettingsModelString(OpenPhactsDataNodeModel.MIN_ACT, OpenPhactsDataNodeModel.DEFAULT_MIN_ACT);
    
    
    /**
     * Constructor for the node model.
     */
    protected OpenPhactsDataNodeModel() {
    	
        super(0, 1);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {

        // TODO do something here
        logger.info("Node Model Stub... this is not yet implemented !");
        
    
        
        // the data table spec of the single output table, 
        // the table will have three columns:
        DataColumnSpec[] allColSpecs = new DataColumnSpec[8];
        allColSpecs[0] = 
            new DataColumnSpecCreator("Standard Value", DoubleCell.TYPE).createSpec();
        allColSpecs[1] = 
            new DataColumnSpecCreator("Activity Type", StringCell.TYPE).createSpec();
        allColSpecs[2] = 
            new DataColumnSpecCreator("Units", StringCell.TYPE).createSpec();
        allColSpecs[3] = 
            new DataColumnSpecCreator("Relation", StringCell.TYPE).createSpec();
        allColSpecs[4] = 
            new DataColumnSpecCreator("Assay Organism", StringCell.TYPE).createSpec();
        allColSpecs[5] = 
            new DataColumnSpecCreator("Assay Title", StringCell.TYPE).createSpec();
        allColSpecs[6] = 
            new DataColumnSpecCreator("Smiles", StringCell.TYPE).createSpec();
        allColSpecs[7] = 
            new DataColumnSpecCreator("Inchi", StringCell.TYPE).createSpec();
        
 
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        // the execution context will provide us with storage capacity, in this
        // case a data container to which we will add rows sequentially
        // Note, this container can also handle arbitrary big data tables, it
        // will buffer to disc if necessary.
        BufferedDataContainer container = exec.createDataContainer(outputSpec);
        
        URL requestURL = buildRequestURL();
        JSONObject json = this.grabSomeJson(requestURL);
        
        JSONObject result = (JSONObject)  json.get("result");
        
    	JSONArray items = (JSONArray)  result.get("items");
    	for (int i = 0; i < items.size(); i++) {
    		JSONObject item = items.getJSONObject(i);
    		double standard_value = item.getDouble("standardValue");
    		String activity_type = item.get("activity_type").toString();
    		String standard_unit = item.getString("standardUnits").toString();
    		String relation = item.getString("relation").toString();
    		
    		JSONObject assay = item.getJSONObject("onAssay").getJSONObject("target");
    		String assay_org = assay.getString("assay_organism");
    		String assay_title = assay.getString("title");
    		
    		JSONObject molecule = item.getJSONObject("forMolecule").getJSONObject("exactMatch");
    		String smiles = molecule.getString("smiles");
    		String inchi = molecule.getString("inchi");
    		
    		RowKey key = new RowKey("result " + i);
    		DataCell[] cells = new DataCell[8];
    		cells[0] = new DoubleCell(standard_value);
            cells[1] = new StringCell(activity_type);
            cells[2] = new StringCell(standard_unit);
            cells[3] = new StringCell(relation);
            cells[4] = new StringCell(assay_org);
            cells[5] = new StringCell(assay_title);
            cells[6] = new StringCell(smiles);
            cells[7] = new StringCell(inchi);
            
            DataRow row = new DefaultRow(key, cells);
            container.addRowToTable(row);
    	}

        container.close();
        BufferedDataTable out = container.getTable();
       
        return new BufferedDataTable[]{out};
    }

    protected URL buildRequestURL() throws URISyntaxException, MalformedURLException, UnsupportedEncodingException  
    {
    	//"http://ops.few.vu.nl/target/enzyme/pharmacology/pages?uri=http%3A%2F%2Fpurl.uniprot.org%2Fenzyme%2F1.1.-.-&activity_type=Potency&maxEx-activity_value=10&minEx-activity_value=4&assay_organism=Homo%20sapiens&_page=1";
        
    	URI hosturi = new URI(api_settings.getStringValue());
    	
    	String family = URLEncoder.encode(family_settings.getStringValue(),"UTF-8"); 
    	//String family = family_settings.getStringValue();
    	
    	System.out.println(family);
    	String queryStr = "uri=" + family + "&";
    	queryStr = queryStr + "activity_type=" + activity_settings.getStringValue() +"&";
    	queryStr = queryStr + "maxEx-activity_value=" + max_settings.getStringValue() + "&";
    	queryStr = queryStr + "minEx-activity_value="+ min_settings.getStringValue() + "&";
    	queryStr = queryStr +  "assay_organism=" + URLEncoder.encode(organism_settings.getStringValue(),"UTF-8");
    	//queryStr = queryStr + "_page=1";
    	
    	String url_str = "http://" + hosturi.getHost() + "/target/enzyme/pharmacology/pages?" + queryStr;
    	
    	/*URI uri = new URI("http",
    			hosturi.getHost(),
    			"/target/enzyme/pharmacology/pages",
    			queryStr,
    			null);*/
    	
    	System.out.println("URL " + url_str);
    	URI uri = new URI(url_str);
    	return uri.toURL();
    }
    
    protected JSONObject grabSomeJson(URL url) throws IOException
    {
    	String str=""; String xml="";
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
    
    private String[] parseCompoundInfo(String str)
    {
    	JSONObject jo = (JSONObject) JSONSerializer.toJSON( str);

    	// Get the array of activities
    	JSONObject result = (JSONObject)  jo.get("result");
    	JSONObject primaryTopic = (JSONObject)  result.get("primaryTopic");
    	JSONArray exactMatch = (JSONArray) primaryTopic.get("exactMatch");

    	// Assuming these are 1 and 2 is probably a really, really bad idea
    	JSONObject nameHash = (JSONObject) exactMatch.get(1);
    	JSONObject chemspiderHash = (JSONObject) exactMatch.get(2);
    	String [] ret = new String [] {nameHash.get("genericName").toString(), chemspiderHash.get("inchikey").toString()};

    	return ret;
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

        // TODO save user settings to the config object.
        
      //  m_count.saveSettingsTo(settings);
    	//System.out.println("save settigns");
    
    	family_settings.saveSettingsTo(settings);
    	api_settings.saveSettingsTo(settings);
    	activity_settings.saveSettingsTo(settings);
    	organism_settings.saveSettingsTo(settings);
    	max_settings.saveSettingsTo(settings);
    	min_settings.saveSettingsTo(settings);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
        // TODO load (valid) settings from the config object.
        // It can be safely assumed that the settings are valided by the 
        // method below.
        
      //  m_count.loadSettingsFrom(settings);
    	//api_settings.loadSettingsFrom(settings);
    	
    	//System.out.println("Load validated");
    	
    	api_settings.loadSettingsFrom(settings);
    	family_settings.loadSettingsFrom(settings);
      	activity_settings.loadSettingsFrom(settings);
    	organism_settings.loadSettingsFrom(settings);
    	max_settings.loadSettingsFrom(settings);
    	min_settings.loadSettingsFrom(settings);

    
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
    	family_settings.validateSettings(settings);
    	activity_settings.validateSettings(settings);
    	organism_settings.validateSettings(settings);
    	max_settings.validateSettings(settings);
    	min_settings.validateSettings(settings);

    	
    	
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

}

