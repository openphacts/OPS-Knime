package org.openphacts.utils;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
/**
 * <code>NodeDialog</code> for the "RegExFilter" Node.
 * This node iterates over every cell from the first input table and executes the regular expression from the second input table. Every non-empty result is added as a row in the output table. * n * nThe second row of the second input table provides the option to give a name to the result column 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Ronald Siebes
 */
public class RegExFilterNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring RegExFilter node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected RegExFilterNodeDialog() {
        super();
        
       
    }
}

