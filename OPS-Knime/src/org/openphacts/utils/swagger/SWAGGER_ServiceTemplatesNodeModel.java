package org.openphacts.utils.swagger;

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
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;


/**
 * This is the model implementation of SWAGGER_ServiceTemplates.
 * 
 *
 * @author Ronald Siebes - VU Amsterdam
 */
public class SWAGGER_ServiceTemplatesNodeModel extends NodeModel {
    
    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(SWAGGER_ServiceTemplatesNodeModel.class);
        

    /**
     * Constructor for the node model.
     */
    protected SWAGGER_ServiceTemplatesNodeModel() {
    
        // TODO one incoming port and one outgoing port is assumed
        super(1, 1);
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
        DataColumnSpec[] allColSpecs = new DataColumnSpec[2];
        allColSpecs[0] = 
            new DataColumnSpecCreator("SWAGGER url", StringCell.TYPE).createSpec();
        allColSpecs[1] = 
                new DataColumnSpecCreator("SWAGGER Service template", StringCell.TYPE).createSpec();
       
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        // the execution context will provide us with storage capacity, in this
        // case a data container to which we will add rows sequentially
        // Note, this container can also handle arbitrary big data tables, it
        // will buffer to disc if necessary.
        BufferedDataContainer container = exec.createDataContainer(outputSpec);
     	Iterator<DataRow> varTableIt = inData[0].iterator();
     	int count = 0;
 	   	while(varTableIt.hasNext()){
 	   		
 	   		
 	   		
 	   		
 	   		String swaggerURL = varTableIt.next().getCell(0).toString();
 	   		
 	   		Vector<String> templates = loadSwagger(swaggerURL);
 	   		Iterator<String> templateIt = templates.iterator();
 	   		int count2 = 0;
 	   		while(templateIt.hasNext()){
 	   			RowKey key = new RowKey("Row " + count+" - "+ count2);
 	   		    DataCell[] cells = new DataCell[2];
 	   			cells[0] = new StringCell(swaggerURL);
 	   			cells[1] = new StringCell(templateIt.next().toString());
	 	   		 DataRow row = new DefaultRow(key, cells);
		         container.addRowToTable(row);
		         count2++;
 	   			
 	   		}
 	   	count++;
	 	   	exec.checkCanceled();
	        exec.setProgress("loading swagger");
	 	   	
 	   		
 	   	
      
        }
        // once we are done, we close the container and return its table
        container.close();
        BufferedDataTable out = container.getTable();
        return new BufferedDataTable[]{out};
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
        
       // m_count.saveSettingsTo(settings);

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
        
       // m_count.loadSettingsFrom(settings);

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

//        m_count.validateSettings(settings);

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
	private Vector<String> loadSwagger(String stringValue) throws URISyntaxException, IOException {
		
		Vector<String> urlTemplates = new Vector<String>();
		JSONObject swaggerObject;
		URL swaggerURL = null;
	
			swaggerURL = buildRequestURL(stringValue);
	
		if(swaggerURL !=null){
			
				swaggerObject = grabSomeJson(swaggerURL);
				
				String basePath = swaggerObject.getString("basePath");
				
				JSONArray servicesJSON = swaggerObject.getJSONArray("apis");
				JSONObject currentService=null;
				
				for(int i=0;i<servicesJSON.size();i++){
					currentService = servicesJSON.getJSONObject(i);
					String path = currentService.getString("path");
					JSONArray operations = currentService.getJSONArray("operations");
					for(int j=0;j<operations.size();j++){
						String urlTemplate = basePath+path+"?";
						JSONObject operation = operations.getJSONObject(j);
						JSONArray parameters = operation.getJSONArray("parameters");
						for(int k=0;k<parameters.size();k++){
							JSONObject parameter = parameters.getJSONObject(k);
							urlTemplate += "["+(parameter.getString("name")+"="+parameter.getString("name"));
							if(k+2 <= parameters.size() ){
								urlTemplate+="&]";
							}else{
								urlTemplate+="]";
							}
						}
						urlTemplates.add(urlTemplate);
					}
				}
				
				//SWAGGER_KnimeNodeDialog.serviceSelectionComponent.setVisibleRowCount(services.length);
				//SWAGGER_KnimeNodeDialog.infoMsg.setText(text)
					
				
			
		}
		return urlTemplates;
		
	}
	
	
	//This is what Gali, our Sphinx cat wrote when he sat on my computer when i got myself a coffee:
	//6ttttttttttttttttt5;lppppppppgffffffffffffffffffffff m,O)P_______________________________________________________________________________________________Ohjbv
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
}

