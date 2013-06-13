package org.openphacts;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "OPS_search_freetext" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author openphacts
 */
public class OPS_search_freetextNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring OPS_search_freetext node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected OPS_search_freetextNodeDialog() {
        super();
        
        createNewGroup("Group 1:");
        addDialogComponent(new DialogComponentString(new 
        		SettingsModelString(OpenPhactsDataNodeModel.API_URL, 
        				OPS_search_freetextNodeModel.DEFAULT_API_URL), "Server:", true, 30));
        addDialogComponent(new DialogComponentString(new SettingsModelString(
        		OPS_search_freetextNodeModel.APP_ID, OPS_search_freetextNodeModel.APP_ID_DEFAULT), "Your application ID:"));
        addDialogComponent(new DialogComponentString(new SettingsModelString(
        		OPS_search_freetextNodeModel.APP_KEY, OPS_search_freetextNodeModel.APP_KEY_DEFAULT), "Your application key:"));
        addDialogComponent(new DialogComponentString(new SettingsModelString(
        		OPS_search_freetextNodeModel.QUERY, OPS_search_freetextNodeModel.QUERY_DEFAULT), "Your query (e.g. water)"));
        addDialogComponent(new DialogComponentNumber(new SettingsModelInteger(
        		OPS_search_freetextNodeModel.LIMIT, 3), "Limit: enter an integer (between 1 and common sense, default is 10):", 10));
        addDialogComponent(new DialogComponentStringSelection(
                new SettingsModelString(OPS_search_freetextNodeModel.BRANCH, OPS_search_freetextNodeModel.BRANCH_DEFAULT),
                "Branch:", "Community", "UMLS", "SwissProt","ChemSpider","ConceptWiki" ));

                    
    }
}

