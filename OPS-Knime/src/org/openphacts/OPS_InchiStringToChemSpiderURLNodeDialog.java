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
            addDialogComponent(new DialogComponentString(new SettingsModelString(
            		OPS_InchiStringToChemSpiderURLNodeModel.INCHI_STRING, OPS_InchiStringToChemSpiderURLNodeModel.INCHI_STRING_DEFAULT), "An InChI string. E.g. InChI=1S/C9H8O4/c1-6(10)13-8-5-3-2-4-7(8)9(11)12/h2-5H,1H3,(H,11,12)"));

    }
}

