package org.openphacts;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "OPS_pathway" Node.
 * Returns information about a single compound that corresponds to {uri}.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author openphacts
 */
public class OPS_pathwayNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring OPS_pathway node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected OPS_pathwayNodeDialog() {
        super();
        
        createNewGroup("Group 1:");
        addDialogComponent(new DialogComponentString(new 
        		SettingsModelString(OPS_pathwayNodeModel.API_URL, 
        				OPS_pathwayNodeModel.DEFAULT_API_URL), "Server:", true, 30));
        addDialogComponent(new DialogComponentString(new SettingsModelString(
        		OPS_pathwayNodeModel.APP_ID, OPS_pathwayNodeModel.APP_ID_DEFAULT), "Your application ID:"));
        addDialogComponent(new DialogComponentString(new SettingsModelString(
        		OPS_pathwayNodeModel.APP_KEY, OPS_pathwayNodeModel.APP_KEY_DEFAULT), "Your application key:"));
        addDialogComponent(new DialogComponentString(new SettingsModelString(
        		OPS_pathwayNodeModel.URI, OPS_pathwayNodeModel.URI_DEFAULT), "A Pathway URI. e.g.: http://rdf.wikipathways.org/WP1019_r48131 "));
                    
    }
}

