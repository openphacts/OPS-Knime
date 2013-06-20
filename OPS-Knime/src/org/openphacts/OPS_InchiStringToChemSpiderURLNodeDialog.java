package org.openphacts;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "OPS_InchiStringToChemSpiderURL" Node.
 * Returns a ChemSpider URL corresponding to an input InChI string. Driven by ChemSpider.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Ronald Siebes -VUA
 */
public class OPS_InchiStringToChemSpiderURLNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the OPS_InchiStringToChemSpiderURL node.
     */
    protected OPS_InchiStringToChemSpiderURLNodeDialog() {
    	  addDialogComponent(new DialogComponentString(new 
            		SettingsModelString(OpenPhactsDataNodeModel.API_URL, 
            				OPS_InchiStringToChemSpiderURLNodeModel.DEFAULT_API_URL), "Server:", true, 30));
            addDialogComponent(new DialogComponentString(new SettingsModelString(
            		OPS_InchiStringToChemSpiderURLNodeModel.APP_ID, OPS_InchiStringToChemSpiderURLNodeModel.APP_ID_DEFAULT), "Your application ID:"));
            addDialogComponent(new DialogComponentString(new SettingsModelString(
            		OPS_InchiStringToChemSpiderURLNodeModel.APP_KEY, OPS_InchiStringToChemSpiderURLNodeModel.APP_KEY_DEFAULT), "Your application key:"));
 
    }
}

