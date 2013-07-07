package org.openphacts.utils.swagger;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;

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

/**
 * This is the model implementation of SWAGGER_GenerateGET_URL.
 * 
 *
 * @author Ronald Siebes - VUA
 */
public class SWAGGER_GenerateGET_URLNodeModel extends NodeModel {
    
    /**
     * Constructor for the node model.
     */
    protected SWAGGER_GenerateGET_URLNodeModel() {
    
        // TODO: Specify the amount of input and output ports needed.
        super(2, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {

    	DataColumnSpec[] allColSpecs = new DataColumnSpec[1];
        allColSpecs[0] = 
                new DataColumnSpecCreator("URL", StringCell.TYPE).createSpec();
        
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        // the execution context will provide us with storage capacity, in this
        // case a data container to which we will add rows sequentially
        // Note, this container can also handle arbitrary big data tables, it
        // will buffer to disc if necessary.
        BufferedDataContainer container = exec.createDataContainer(outputSpec);
        DataCell[] cells = new DataCell[1];
        
	String urlTemplate = inData[1].iterator().next().getCell(1).toString();
	Iterator<DataRow> varIt = inData[0].iterator();
	DataTableSpec dts = inData[0].getDataTableSpec();
	String[] colNames = dts.getColumnNames();
	
	while(varIt.hasNext()){
		
		DataRow current = varIt.next();
		Iterator<DataCell> cellIt = current.iterator();
		int colCount = 0;
		
		while(cellIt.hasNext()){
			DataCell currentCell= cellIt.next();
			String curVar =  colNames[colCount];
			String curVal = currentCell.toString();
			if(urlTemplate.contains(curVar)){
				urlTemplate = urlTemplate.replace("["+curVar+"="+curVar+"&]", curVar+"="+URLEncoder.encode(curVal,"UTF-8")+"&");
				urlTemplate = urlTemplate.replace("["+curVar+"="+curVar+"]", curVar+"="+URLEncoder.encode(curVal,"UTF-8"));
			}
			colCount++;
			
		}
		
		
		
		
	}
	//System.out.println("yy:"+urlTemplate);
	urlTemplate =urlTemplate.replaceAll("\\[.*?\\]", "");
	urlTemplate = urlTemplate.substring(0, urlTemplate.length()-1);
	//System.out.println("yy:"+urlTemplate);
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
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {

        // TODO: generated method stub
        return new DataTableSpec[]{null};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
         // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        // TODO: generated method stub
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        // TODO: generated method stub
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        // TODO: generated method stub
    }

}

