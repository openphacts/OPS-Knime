package org.openphacts;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;

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
        
        addDialogComponent(new DialogComponentNumber(
                new SettingsModelIntegerBounded(
                    OPS_search_freetextNodeModel.CFGKEY_COUNT,
                    OPS_search_freetextNodeModel.DEFAULT_COUNT,
                    Integer.MIN_VALUE, Integer.MAX_VALUE),
                    "Counter:", /*step*/ 1, /*componentwidth*/ 5));
                    
    }
}

