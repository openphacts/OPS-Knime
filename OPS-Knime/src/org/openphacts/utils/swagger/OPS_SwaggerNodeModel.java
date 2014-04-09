package org.openphacts.utils.swagger;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.regex.Pattern;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

/**
 * This is the model implementation of OPS_Swagger.
 * This node loads a swagger web service description file and lets the user select on of the services via the config panel together with setting default parameters. An input table can be added with more parameters and values. The result of executing the node is a URL with the service call. 
 *
 * @author Ronald Siebes - VU Amsterdam
 */
public class OPS_SwaggerNodeModel extends NodeModel {
    
	 // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(OPS_SwaggerNodeModel.class);
        


	//static final String SWAGGER_URL_DEFAULT = "https://raw.githubusercontent.com/openphacts/OPS_LinkedDataApi/1.4.0/api-config-files/swagger.json";
	static final String SWAGGER_URL_DEFAULT = "https://raw.githubusercontent.com/openphacts/OPS_LinkedDataApi/1.3.1/api-config-files/swagger.json";
	static final String SWAGGER_URL = "swagger";

	public static final String SWAGGER_RESULT = "swaggerResult";

	public static final String SWAGGER_RESULT_DEFAULT = "empty";

	public static final String TEMPLATE_SELECTION_DEFAULT = "empty";

	public static final String TEMPLATE_SELECTION = "templateSelection";



	public static final String RESULT_URL = "resultUrl";



	public static final String RESULT_URL_DEFAULT = "<empty>";


    private final SettingsModelString swaggerUrl = new SettingsModelString(OPS_SwaggerNodeModel.SWAGGER_URL,OPS_SwaggerNodeModel.SWAGGER_URL_DEFAULT);
   // protected final SettingsModelString swaggerResult = new SettingsModelString(OPS_SwaggerNodeModel.SWAGGER_RESULT,OPS_SwaggerNodeModel.SWAGGER_RESULT_DEFAULT);
    protected final SettingsModelString templateSelection = new SettingsModelString(OPS_SwaggerNodeModel.TEMPLATE_SELECTION,OPS_SwaggerNodeModel.TEMPLATE_SELECTION_DEFAULT);
    private final SettingsModelString resultUrl = new SettingsModelString(OPS_SwaggerNodeModel.RESULT_URL,OPS_SwaggerNodeModel.RESULT_URL_DEFAULT);

    
    /**
     * Constructor for the node model.
     */
    protected OPS_SwaggerNodeModel() {
    	
   
        // TODO one incoming port and one outgoing port is assumed
        super(1, 1);
     	resultUrl.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				//System.out.println("resultUrl"+resultUrl.getStringValue());
				
				
			}
        	
        	
        	
        	
        });
    
        swaggerUrl.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				//System.out.println("swaggerurl"+swaggerUrl.getStringValue());
				
			}
        	
        	
        	
        	
        });
        
        templateSelection.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				//System.out.println("templateSelection"+templateSelection.getStringValue());
				
				
			}
        	
        	
        	
        	
        });
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {

        // TODO do something here
        logger.error("Node Model Stub... this is not yet implemented !"+resultUrl.getStringValue());

    	DataColumnSpec[] allColSpecs = new DataColumnSpec[1];
        allColSpecs[0] = 
                new DataColumnSpecCreator("url", StringCell.TYPE).createSpec();
        
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
     //   System.out.println("calling execute"+resultUrl.getStringValue());
        // the execution context will provide us with storage capacity, in this
        // case a data container to which we will add rows sequentially
        // Note, this container can also handle arbitrary big data tables, it
        // will buffer to disc if necessary.
        BufferedDataContainer container = exec.createDataContainer(outputSpec);
        DataCell[] cells = new DataCell[1];
        
	String urlTemplate = resultUrl.getStringValue();
	Iterator<DataRow> varIt = inData[0].iterator();
	DataTableSpec dts = inData[0].getDataTableSpec();
	String[] colNames = dts.getColumnNames();
	
	if(varIt.hasNext()){
	//while(varIt.hasNext()){
		
		DataRow current = varIt.next();
		Iterator<DataCell> cellIt = current.iterator();
		int colCount = 0;
		
		while(cellIt.hasNext()){
			DataCell currentCell= cellIt.next();
			String curVar =  colNames[colCount];
			String curVal = currentCell.toString();
			//System.out.println("curvar:"+curVar+", curval:"+curVal+", urlTemplate:"+urlTemplate);
			
				if(urlTemplate.indexOf(curVar)==-1){
					System.out.println("1curvar:"+curVar+", curval:"+curVal+", urlTemplate:"+urlTemplate);
					urlTemplate = urlTemplate+"&"+curVar+"="+URLEncoder.encode(currentCell.toString(),"UTF-8");
					System.out.println("2curvar:"+curVar+", curval:"+curVal+", urlTemplate:"+urlTemplate);
				}else{
					System.out.println("3curvar:"+curVar+", curval:"+curVal+", urlTemplate:"+urlTemplate);
					String configValue = urlTemplate.substring(urlTemplate.indexOf(curVar)+curVar.length()+1,urlTemplate.indexOf("]",urlTemplate.indexOf(curVar)));
					
					System.out.println("4curvar:"+curVar+", curval:"+curVal+", urlTemplate:"+urlTemplate);
					if(configValue.endsWith("&")){
						configValue = configValue.substring(0,configValue.length()-1); //get rid of the &
					}
					if(colCount==0){
						urlTemplate +="&";
					}
					System.out.println("5curvar:"+curVar+", curval:"+curVal+", urlTemplate:"+urlTemplate);
					if(!configValue.equals("")){
						System.out.println("6curvar:"+curVar+", curval:"+curVal+", urlTemplate:"+urlTemplate);
						System.out.println("config var: "+curVar+" with configValue:"+ configValue+ ", is overwritten with value: "+currentCell.toString()  );
						urlTemplate = urlTemplate.replaceAll(Pattern.quote("["+curVar+"="+configValue+"&]"), curVar+"="+URLEncoder.encode(currentCell.toString(),"UTF-8")+"&");
						urlTemplate = urlTemplate.replaceAll(Pattern.quote("["+curVar+"="+configValue+"]"), curVar+"="+URLEncoder.encode(currentCell.toString(),"UTF-8"));
						
					}else{
						//System.out.println("now we are here");
						System.out.println("7curvar:"+curVar+", curval:"+curVal+", urlTemplate:"+urlTemplate);
						String doubleStr= urlTemplate.toString();
						urlTemplate = urlTemplate.replaceAll(Pattern.quote("["+curVar+"="+configValue+"&]"), curVar+"="+URLEncoder.encode(currentCell.toString(),"UTF-8")+"&");
						urlTemplate = urlTemplate.replaceAll(Pattern.quote("["+curVar+"="+configValue+"]"), curVar+"="+URLEncoder.encode(currentCell.toString(),"UTF-8"));
						//System.out.println("urlTemplate before:"+doubleStr+",  and after:"+urlTemplate);
					}
				}
				
							
				
			
			colCount++;
			
		}
		
		
		
		
	}
	System.out.println("yy:"+urlTemplate);
	
	//urlTemplate =urlTemplate.replaceAll("\\[.*?\\]", "");
	urlTemplate =urlTemplate.replaceAll("\\[", "");
	urlTemplate =urlTemplate.replaceAll("\\]", "");
	//urlTemplate = urlTemplate.substring(0, urlTemplate.length()-1);
	//System.out.println("urlTemplate became "+urlTemplate);
	cells[0] = new StringCell(urlTemplate);
	  DataRow row = new DefaultRow("aboutCell", cells);
	  
        container.addRowToTable(row);
    	
    	
        container.close();
        BufferedDataTable out = container.getTable();
    // TODO: Return a BufferedDataTable for each output port 
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

        swaggerUrl.saveSettingsTo(settings);
        templateSelection.saveSettingsTo(settings);
        resultUrl.saveSettingsTo(settings);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {

        swaggerUrl.loadSettingsFrom(settings);
        templateSelection.loadSettingsFrom(settings);
       resultUrl.loadSettingsFrom(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {

        swaggerUrl.validateSettings(settings);
        templateSelection.validateSettings(settings);
        
        resultUrl.validateSettings(settings);
      //  System.out.println("url"+resultUrl.getStringValue());
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

