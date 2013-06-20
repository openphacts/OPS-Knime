package org.openphacts;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "OPS_target" Node.
 * Returns information about a single compound that corresponds to {uri}.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author openphacts
 */
public class OPS_targetNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring OPS_target node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected OPS_targetNodeDialog() {
        super();
        
        createNewGroup("Group 1:");
        addDialogComponent(new DialogComponentString(new 
        		SettingsModelString(OPS_targetNodeModel.API_URL, 
        				OPS_targetNodeModel.DEFAULT_API_URL), "Server:", true, 30));
        addDialogComponent(new DialogComponentString(new SettingsModelString(
        		OPS_targetNodeModel.APP_ID, OPS_targetNodeModel.APP_ID_DEFAULT), "Your application ID:"));
        addDialogComponent(new DialogComponentString(new SettingsModelString(
        		OPS_targetNodeModel.APP_KEY, OPS_targetNodeModel.APP_KEY_DEFAULT), "Your application key:"));
                    
    }
}

