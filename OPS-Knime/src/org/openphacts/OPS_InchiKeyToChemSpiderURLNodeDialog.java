package org.openphacts;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "OPS_InchiKeyToChemSpiderURL" Node.
 * Returns a ChemSpider URL corresponding to an input InChIKey string. Driven by ChemSpider.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Ronald siebes
 */
public class OPS_InchiKeyToChemSpiderURLNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the OPS_InchiKeyToChemSpiderURL node.
     */
    protected OPS_InchiKeyToChemSpiderURLNodeDialog() {
    	  addDialogComponent(new DialogComponentString(new 
          		SettingsModelString(OpenPhactsDataNodeModel.API_URL, 
          				OPS_InchiKeyToChemSpiderURLNodeModel.DEFAULT_API_URL), "Server:", true, 30));
          addDialogComponent(new DialogComponentString(new SettingsModelString(
          		OPS_InchiKeyToChemSpiderURLNodeModel.APP_ID, OPS_InchiKeyToChemSpiderURLNodeModel.APP_ID_DEFAULT), "Your application ID:"));
          addDialogComponent(new DialogComponentString(new SettingsModelString(
          		OPS_InchiKeyToChemSpiderURLNodeModel.APP_KEY, OPS_InchiKeyToChemSpiderURLNodeModel.APP_KEY_DEFAULT), "Your application key:"));
          addDialogComponent(new DialogComponentString(new SettingsModelString(
          		OPS_InchiKeyToChemSpiderURLNodeModel.INCHI_KEY, OPS_InchiKeyToChemSpiderURLNodeModel.INCHI_KEY_DEFAULT), "An InChIKey string. E.g. BSYNRYMUTXBXSQ-UHFFFAOYSA-N "));

    }
}

