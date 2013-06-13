package org.openphacts;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "OPS_pathway" Node.
 * Returns information about a single pathway that corresponds to {uri}.
 *
 * @author openphacts
 */
public class OPS_pathwayNodeFactory 
        extends NodeFactory<OPS_pathwayNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public OPS_pathwayNodeModel createNodeModel() {
        return new OPS_pathwayNodeModel();
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
    public NodeView<OPS_pathwayNodeModel> createNodeView(final int viewIndex,
            final OPS_pathwayNodeModel nodeModel) {
        return new OPS_pathwayNodeView(nodeModel);
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
        return new OPS_pathwayNodeDialog();
    }

}

