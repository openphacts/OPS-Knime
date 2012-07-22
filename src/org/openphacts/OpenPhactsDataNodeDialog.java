package org.openphacts;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "OpenPhactsData" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author OpenPhacts
 */
public class OpenPhactsDataNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring OpenPhactsData node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected OpenPhactsDataNodeDialog() {
        super();
       
        addDialogComponent(new DialogComponentString(new 
        		SettingsModelString(OpenPhactsDataNodeModel.API_URL, 
        				OpenPhactsDataNodeModel.DEFAULT_API_URL), "Server:", true, 30));
   
        addDialogComponent(new DialogComponentString(new 
        		SettingsModelString(OpenPhactsDataNodeModel.FAMILY_URI, 
        			OpenPhactsDataNodeModel.DEFAULT_FAMILY_URI), 
        			"Enzyme Class URL:", true, 30));
        
        addDialogComponent(new DialogComponentString(new SettingsModelString(OpenPhactsDataNodeModel.ACTIVITY, OpenPhactsDataNodeModel.DEFAULT_ACTIVITY), 
        			"Activity Type", true, 30));
        
        addDialogComponent(new DialogComponentString(new SettingsModelString(OpenPhactsDataNodeModel.ORGANISM, OpenPhactsDataNodeModel.DEFAULT_ORGANISM), 
    			"Organism", true, 30));
        
        addDialogComponent(new DialogComponentString(new SettingsModelString(OpenPhactsDataNodeModel.MAX_ACT, OpenPhactsDataNodeModel.DEFAULT_MAX_ACT), 
    			"Max Activity", true, 30));
        
        addDialogComponent(new DialogComponentString(new SettingsModelString(OpenPhactsDataNodeModel.MIN_ACT, OpenPhactsDataNodeModel.DEFAULT_MIN_ACT), 
    			"Min Activity", true, 30));
       
        
        
                    
    }
}

