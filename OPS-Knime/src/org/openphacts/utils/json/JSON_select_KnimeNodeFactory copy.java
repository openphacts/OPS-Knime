package org.openphacts.utils.json;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "JSON_select_Knime" Node.
 * Generates a table representing the result from a json url according to a specified list of keys-headers
 *
 * @author Ronald Siebes
 */
public class JSON_select_KnimeNodeFactory 
        extends NodeFactory<JSON_select_KnimeNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public JSON_select_KnimeNodeModel createNodeModel() {
        return new JSON_select_KnimeNodeModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNrNodeViews() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeView<JSON_select_KnimeNodeModel> createNodeView(final int viewIndex,
            final JSON_select_KnimeNodeModel nodeModel) {
        return new JSON_select_KnimeNodeView(nodeModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDialog() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeDialogPane createNodeDialogPane() {
        return new JSON_select_KnimeNodeDialog();
    }

}

