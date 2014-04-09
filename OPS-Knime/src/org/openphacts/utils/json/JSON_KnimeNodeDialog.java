package org.openphacts.utils.json;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "JSON_Knime" Node.
 * Reads a JSON string, converts its hierarchical structure into a flat 2D matrix and exports it as a Knime table. 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Ronald Siebes - VU Amsterdam
 */
public class JSON_KnimeNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the JSON_Knime node.
     */
    protected JSON_KnimeNodeDialog() {
    	super();
    	

    }
}

