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
 * Returns a set of concept URLs associated to the input free text. Driven by ConceptWiki.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Ronald Siebes
 */
public class OPS_search_freetextNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring OPS_search_freetext node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected OPS_search_freetextNodeDialog() {
        super();
        // following components are bordered
        createNewGroup("Group 1:");
        addDialogComponent(new DialogComponentString(new SettingsModelString(
        		OPS_search_freetextNodeModel.APP_ID, null), "Your application ID:"));
        addDialogComponent(new DialogComponentString(new SettingsModelString(
        		OPS_search_freetextNodeModel.APP_KEY, null), "Your application key:"));
        addDialogComponent(new DialogComponentNumber(new SettingsModelInteger(
        		OPS_search_freetextNodeModel.LIMIT, 3), "Limit: enter an integer (between 1 and common sense, default is 10):", 10));
        addDialogComponent(new DialogComponentStringSelection(
                new SettingsModelString(OPS_search_freetextNodeModel.BRANCH, null),
                "Branch:", "Community", "UMLS", "SwissProt","ChemSpider","ConceptWiki" ));
        addDialogComponent(new DialogComponentStringSelection(
                new SettingsModelString(OPS_search_freetextNodeModel.METADATA, null),
                "_metadata", "Execution", "Formats", "Views","All"));
                    
    }
}

