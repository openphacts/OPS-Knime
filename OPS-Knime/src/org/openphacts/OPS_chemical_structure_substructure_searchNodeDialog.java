package org.openphacts;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelDouble;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "OPS_chemical_structure_substructure_search" Node.
 * Returns a set of ChemSpider compound URLs that contain the specified structure. Driven by ChemSpider.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Ronald Siebes - VUA
 */
public class OPS_chemical_structure_substructure_searchNodeDialog extends DefaultNodeSettingsPane {

	 /**
     * New pane for configuring OPS_chemical_structure_substructure_search node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected OPS_chemical_structure_substructure_searchNodeDialog() {
        super();
        
  	  createNewGroup("Group 1:");
      addDialogComponent(new DialogComponentString(new 
      		SettingsModelString(OPS_chemical_structure_substructure_searchNodeModel.API_URL, 
      				OPS_chemical_structure_substructure_searchNodeModel.DEFAULT_API_URL), "Server:", true, 30));
      addDialogComponent(new DialogComponentString(new SettingsModelString(
      		OPS_chemical_structure_substructure_searchNodeModel.APP_ID, OPS_chemical_structure_substructure_searchNodeModel.APP_ID_DEFAULT), "Your application ID:"));
      addDialogComponent(new DialogComponentString(new SettingsModelString(
      		OPS_chemical_structure_substructure_searchNodeModel.APP_KEY, OPS_chemical_structure_substructure_searchNodeModel.APP_KEY_DEFAULT), "Your application key:"));
      addDialogComponent(new DialogComponentNumber(new SettingsModelInteger(
      		OPS_chemical_structure_substructure_searchNodeModel.LIMIT, OPS_chemical_structure_substructure_searchNodeModel.LIMIT_DEFAULT), "Integer. Search limit. Specify how many results return back during the search. Default value: 20", 1));       
      addDialogComponent(new DialogComponentNumber(new SettingsModelInteger(
      		OPS_chemical_structure_substructure_searchNodeModel.START, OPS_chemical_structure_substructure_searchNodeModel.START_DEFAULT), "Integer. Return results starting the index. Default value: 0 ", 1));       
      addDialogComponent(new DialogComponentNumber(new SettingsModelInteger(
      		OPS_chemical_structure_substructure_searchNodeModel.LENGTH, OPS_chemical_structure_substructure_searchNodeModel.LENGTH_DEFAULT), "Integer. How many results should be returned starting from Start index. Default value: 10", 1));       


                    
    }
}


