package org.openphacts.utils.json;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

/**
 * <code>NodeDialog</code> for the "JSON_select_Knime" Node.
 * Generates a table representing the result from a json url according to a specified list of keys-headers
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Ronald Siebes
 */
public class JSON_select_KnimeNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the JSON_select_Knime node.
     */
    protected JSON_select_KnimeNodeDialog() {

    }
}

