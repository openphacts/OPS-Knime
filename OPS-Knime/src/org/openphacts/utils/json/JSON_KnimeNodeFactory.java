package org.openphacts.utils.json;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "JSON_Knime" Node.
 * Reads a JSON string, converts its hierarchical structure into a flat 2D matrix and exports it as a Knime table. 
 *
 * @author Ronald Siebes - VU Amsterdam
 */
public class JSON_KnimeNodeFactory 
        extends NodeFactory<JSON_KnimeNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public JSON_KnimeNodeModel createNodeModel() {
        return new JSON_KnimeNodeModel();
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
    public NodeView<JSON_KnimeNodeModel> createNodeView(final int viewIndex,
            final JSON_KnimeNodeModel nodeModel) {
        return new JSON_KnimeNodeView(nodeModel);
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
        return new JSON_KnimeNodeDialog();
    }

}

