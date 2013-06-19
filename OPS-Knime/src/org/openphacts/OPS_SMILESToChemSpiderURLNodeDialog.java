package org.openphacts;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "OPS_SMILESToChemSpiderURL" Node.
 * Returns a ChemSpider URL corresponding to an input SMILES string. Driven by ChemSpider.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Ronald Siebes - VUA
 */
public class OPS_SMILESToChemSpiderURLNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the OPS_SMILESToChemSpiderURL node.
     */
    protected OPS_SMILESToChemSpiderURLNodeDialog() {
    	 addDialogComponent(new DialogComponentString(new 
         		SettingsModelString(OpenPhactsDataNodeModel.API_URL, 
         				OPS_SMILESToChemSpiderURLNodeModel.DEFAULT_API_URL), "Server:", true, 30));
         addDialogComponent(new DialogComponentString(new SettingsModelString(
         		OPS_SMILESToChemSpiderURLNodeModel.APP_ID, OPS_SMILESToChemSpiderURLNodeModel.APP_ID_DEFAULT), "Your application ID:"));
         addDialogComponent(new DialogComponentString(new SettingsModelString(
         		OPS_SMILESToChemSpiderURLNodeModel.APP_KEY, OPS_SMILESToChemSpiderURLNodeModel.APP_KEY_DEFAULT), "Your application key:"));
         addDialogComponent(new DialogComponentString(new SettingsModelString(
         		OPS_SMILESToChemSpiderURLNodeModel.SMILES_STRING, OPS_SMILESToChemSpiderURLNodeModel.SMILES_STRING_DEFAULT), "A SMILES string. E.g. CC(=O)Oc1ccccc1C(=O)O"));

    }
}

