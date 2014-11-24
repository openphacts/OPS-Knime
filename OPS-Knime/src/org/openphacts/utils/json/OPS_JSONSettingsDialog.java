package org.openphacts.utils.json;

import java.util.Collection;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentStringListSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;

public class OPS_JSONSettingsDialog extends DefaultNodeSettingsPane {
	private final SettingsModelString json_config_url = new SettingsModelString(
			OPS_JSONNodeModel.JSON_URL, OPS_JSONNodeModel.DEFAULT_JSON_URL);
	private final SettingsModelStringArray selection_parameters = new SettingsModelStringArray(OPS_JSONNodeModel.SELECTION_PARAMETERS, OPS_JSONNodeModel.DEFAULT_SELECTION_PARAMETERS);
	private final SettingsModelStringArray selection_customized_names = new SettingsModelStringArray(OPS_JSONNodeModel.SELECTION_CUSTOMIZED_NAMES, OPS_JSONNodeModel.DEFAULT_SELECTION_CUSTOMIZED_NAMES);
	private final SettingsModelStringArray all_parameters = new SettingsModelStringArray(OPS_JSONNodeModel.ALL_PARAMETERS, OPS_JSONNodeModel.DEFAULT_ALL_PARAMETERS);
	private final SettingsModelStringArray all_customized_names = new SettingsModelStringArray(OPS_JSONNodeModel.ALL_CUSTOMIZED_NAMES, OPS_JSONNodeModel.DEFAULT_ALL_CUSTOMIZED_NAMES);

	DialogComponentStringListSelection var_sel = null;
    DialogComponentStringListSelection name_sel = null;
    DialogComponentStringListSelection var_all_sel = null;
    DialogComponentStringListSelection name_all_sel = null;
	Collection<String> optionsArrayDummy;//{"niks"};
	Collection<String> optionNamesArrayDummy;//{"niks"};
	Collection<String> allOptionsArrayDummy;//{"niks"};
	Collection<String> allOptionNamesArrayDummy;//{"niks"};
	protected OPS_JSONSettingsDialog() {
		super();
		var_sel = new DialogComponentStringListSelection(selection_parameters,"var_sel",optionsArrayDummy,1,false,0);
    	name_sel = new DialogComponentStringListSelection(selection_customized_names,"var_sel_names",optionNamesArrayDummy,1,false,0);
    	var_all_sel = new DialogComponentStringListSelection(all_parameters,"var_all",allOptionsArrayDummy,1,false,0);
    	name_all_sel = new DialogComponentStringListSelection(all_customized_names,"var_all_names",allOptionNamesArrayDummy,1,false,0);
    	addDialogComponent(var_sel);
    	addDialogComponent(name_sel);
    	addDialogComponent(var_all_sel);
    	addDialogComponent(name_all_sel);
 
	}

}

