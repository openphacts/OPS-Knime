package org.openphacts.utils.json;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "OPS_JSON" Node.
 * Reads a JSON file and transforms it to a KNIME table
 *
 * @author Ronald Siebes - VU Amsterdam
 */
public class OPS_JSONNodeFactory 
        extends NodeFactory<OPS_JSONNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public OPS_JSONNodeModel createNodeModel() {
        return new OPS_JSONNodeModel();
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
    public NodeView<OPS_JSONNodeModel> createNodeView(final int viewIndex,
            final OPS_JSONNodeModel nodeModel) {
        return new OPS_JSONNodeView(nodeModel);
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
        return new OPS_JSONNodeDialog();
    }

}

