package org.openphacts.utils.json;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;

public class JSON_Option_Dialog extends DefaultNodeSettingsPane {
	private final SettingsModelString json_url =
	        new SettingsModelString(OPS_JSONNodeModel.JSON_URL,
	                    OPS_JSONNodeModel.DEFAULT_JSON_URL);
	DialogComponentString json_config_urlDialog = null;
	public JSON_Option_Dialog(){
		json_config_urlDialog = new DialogComponentString(json_url,
				"Example JSON URL: ");
		addDialogComponent(json_config_urlDialog);
	}
	  @Override
	    public void loadAdditionalSettingsFrom(NodeSettingsRO settings, DataTableSpec[] specs) throws NotConfigurableException {
	        try {
	        	json_url.loadSettingsFrom(settings);
	        } catch (InvalidSettingsException ex) {
	                ex.printStackTrace();
	        }
	    }
	   
	    @Override
	    public void saveAdditionalSettingsTo(NodeSettingsWO settings) {
	    	json_url.saveSettingsTo(settings);
	    }


}
