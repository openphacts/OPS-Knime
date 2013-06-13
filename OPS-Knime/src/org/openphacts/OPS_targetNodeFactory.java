package org.openphacts;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "OPS_target" Node.
 * Returns information about a single target that corresponds to {uri}.
 *
 * @author Ronald Siebes VUA - OpenPHACTS
 */
public class OPS_targetNodeFactory 
        extends NodeFactory<OPS_targetNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public OPS_targetNodeModel createNodeModel() {
        return new OPS_targetNodeModel();
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
    public NodeView<OPS_targetNodeModel> createNodeView(final int viewIndex,
            final OPS_targetNodeModel nodeModel) {
        return new OPS_targetNodeView(nodeModel);
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
        return new OPS_targetNodeDialog();
    }

}

