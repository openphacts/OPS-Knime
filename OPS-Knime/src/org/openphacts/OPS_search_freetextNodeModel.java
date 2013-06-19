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
 * This is the model implementation of OPS_search_freetext.
 * 
 *
 * @author openphacts
 */
public class OPS_search_freetextNodeModel extends NodeModel {
    
    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(OPS_search_freetextNodeModel.class);
        
    
	public static final String API_URL = "OpenPhactsURL";
    public static final String DEFAULT_API_URL = "https://beta.openphacts.org";
    
	public static final String APP_ID = "app_id";
	public static final String APP_ID_DEFAULT = "69ac6ae3";

	public static final String APP_KEY = "app_key";
	public static final String APP_KEY_DEFAULT = "08731c119b4abbf8ea95128c3e4264a8";

	public static final String QUERY = "q";
	public static final String QUERY_DEFAULT = "water";
	
	public static final String LIMIT = "limit";
	public static final int LIMIT_DEFAULT = 10;

	public static final String BRANCH = "branch";
	public static final String BRANCH_DEFAULT = "ConceptWiki";

	public static final String METADATA = "_metadata";
	public static final String METADATA_DEFAULT = "all";
	private static final Map<String,String> branchMap = new HashMap<String,String>();
	
	
    private final SettingsModelString api_settings = new SettingsModelString(OPS_search_freetextNodeModel.API_URL, OPS_search_freetextNodeModel.DEFAULT_API_URL);
    private final SettingsModelString app_id_settings = new SettingsModelString(OPS_search_freetextNodeModel.APP_ID, OPS_search_freetextNodeModel.APP_ID_DEFAULT);
    private final SettingsModelString app_key_settings = new SettingsModelString(OPS_search_freetextNodeModel.APP_KEY, OPS_search_freetextNodeModel.APP_KEY_DEFAULT);
 //   private final SettingsModelString query_settings = new SettingsModelString(OPS_search_freetextNodeModel.QUERY, OPS_search_freetextNodeModel.QUERY_DEFAULT);

    private final SettingsModelInteger limit_settings = new SettingsModelInteger(OPS_search_freetextNodeModel.LIMIT, OPS_search_freetextNodeModel.LIMIT_DEFAULT);
    private final SettingsModelString branch_settings = new SettingsModelString(OPS_search_freetextNodeModel.BRANCH, OPS_search_freetextNodeModel.BRANCH_DEFAULT);


    /**
     * Constructor for the node model.
     */
    protected OPS_search_freetextNodeModel() {

    	
        // TODO one incoming port and one outgoing port is assumed
        super(1, 1);
    	branchMap.put("Community", "1");
    	branchMap.put("UMLS", "2");
    	branchMap.put("SwissProt", "3");
    	branchMap.put("ChemSpider", "4");
    	branchMap.put("ConceptWiki", "5");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {


        // the data table spec of the single output table, 
        // the table will have three columns:
        DataColumnSpec[] allColSpecs = new DataColumnSpec[5];
        allColSpecs[0] = 
            new DataColumnSpecCreator("Concept URI", StringCell.TYPE).createSpec();
        allColSpecs[1] = 
                new DataColumnSpecCreator("Match", StringCell.TYPE).createSpec();
        allColSpecs[2] = 
                new DataColumnSpecCreator("Alt. label EN", StringCell.TYPE).createSpec();
        allColSpecs[3] = 
            new DataColumnSpecCreator("Sem. tags", StringCell.TYPE).createSpec();
        allColSpecs[4] = 
                new DataColumnSpecCreator("Exact-match (preferred is first row) ", StringCell.TYPE).createSpec();
 
        
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        // the execution context will provide us with storage capacity, in this
        // case a data container to which we will add rows sequentially
        // Note, this container can also handle arbitrary big data tables, it
        // will buffer to disc if necessary.
        BufferedDataContainer container = exec.createDataContainer(outputSpec);
        String q= inData[0].iterator().next().getCell(0).toString(); // ugly...needs definitely some array bound checks here
        URL requestURL = buildRequestURL(q);
        JSONObject json = this.grabSomeJson(requestURL);
        
        System.out.println(json.toString());
        JSONObject result = (JSONObject)  json.get("result");
        
        JSONObject primaryTopic = (JSONObject)  result.get("primaryTopic");
    	JSONArray items = (JSONArray)  primaryTopic.get("result");
    	
    	for (int i = 0; i < items.size(); i++) {
    		System.out.println("item");
    		JSONObject item = items.getJSONObject(i);
    		
    	System.out.println(item);
    		
    		RowKey key = new RowKey("result " + i);
    		DataCell[] cells = new DataCell[5];
    		cells[0] = new StringCell(getCellFromJSON("_about",item));
            cells[1] = new StringCell(getCellFromJSON("match",item));
            cells[2] = new StringCell(getCellFromJSON("altLabel_en",item));
            cells[3] = new StringCell(getCellFromJSON("semanticTag",item));
            cells[4] = new StringCell(getCellFromJSON("exactMatch",item));
            DataRow row = new DefaultRow(key, cells);
            container.addRowToTable(row);
    	}

        container.close();
        BufferedDataTable out = container.getTable();
       
        return new BufferedDataTable[]{out};
    }

    private String getCellFromJSON(String key,JSONObject item){
    	String result = "";
	    if(key.equals("_about")){
	    	if(item.get("_about")!=null && item.get("_about").getClass().getName().equals("java.lang.String")){
	    		return item.getString("_about");
	    	}
	    }else if(key.equals("match")) {
	    	if(item.get("match")!=null && item.get("match").getClass().getName().equals("java.lang.String")){
	    		return item.getString("match");
	    	}
	    }else if(key.equals("semanticTag")) {
	    	if(item.get("semanticTag")!=null ){
	    		return item.get("semanticTag").toString();
	    	}
	    }else if(key.equals("exactMatch" )){
	    	if(item.get("exactMatch")!=null && item.get("exactMatch").getClass().getName().equals("net.sf.json.JSONArray")){
	    		JSONArray exactMatchArray = item.getJSONArray("exactMatch");
	    		String tempResult = "";
	    		String current;
	    		for (int i = 0; i < exactMatchArray.size(); i++) {
	    			current = exactMatchArray.get(i).toString();
	    			//preferred on top of the list
	    			if(current.indexOf("PREFERRED")!=-1){
	    				tempResult = current+";\n" +tempResult;
	    			}else{
	    				tempResult = tempResult + ";\n" + current;
	    			}
	    		}
	    		return tempResult;
	    	}
	    }else if(key.equals("altLabel_en")) {
	    	if(item.get("altLabel_en")!=null ){
	    		return item.get("altLabel_en").toString();
	    	}
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
    	limit_settings.saveSettingsTo(settings);
    	branch_settings.saveSettingsTo(settings);
//    	query_settings.saveSettingsTo(settings);
       

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
    	limit_settings.loadSettingsFrom(settings);
    	branch_settings.loadSettingsFrom(settings);
 //   	query_settings.loadSettingsFrom(settings);


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
    	limit_settings.validateSettings(settings);
    	branch_settings.validateSettings(settings);

    	
    	
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
    protected URL buildRequestURL(String q) throws URISyntaxException, MalformedURLException, UnsupportedEncodingException  
    {
    	//"http://ops.few.vu.nl/target/enzyme/pharmacology/pages?uri=http%3A%2F%2Fpurl.uniprot.org%2Fenzyme%2F1.1.-.-&activity_type=Potency&maxEx-activity_value=10&minEx-activity_value=4&assay_organism=Homo%20sapiens&_page=1";
        
    	URI hosturi = new URI(api_settings.getStringValue());
    	
    	String app_id = URLEncoder.encode(app_id_settings.getStringValue(),"UTF-8"); 
    	//String family = family_settings.getStringValue();
    	

    	String queryStr = "app_id=" + app_id + "&";
    	queryStr = queryStr + "app_key=" + app_key_settings.getStringValue() +"&";
    	queryStr = queryStr + "q=" + q +"&";
    	queryStr = queryStr + "limit=" + limit_settings.getIntValue()+ "&";
    	queryStr = queryStr + "branch="+ branchMap.get(branch_settings.getStringValue());
    	//queryStr = queryStr + "_page=1";
    	
    	String url_str = "https://" + hosturi.getHost() + "/search/freetext?" + queryStr;
    	
   
    	
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

