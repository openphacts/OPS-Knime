package org.openphacts;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "OPS_compound" Node.
 * 
 *
 * @author Ronald Siebes
 */
public class OPS_compoundNodeFactory 
        extends NodeFactory<OPS_compoundNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public OPS_compoundNodeModel createNodeModel() {
        return new OPS_compoundNodeModel();
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
    public NodeView<OPS_compoundNodeModel> createNodeView(final int viewIndex,
            final OPS_compoundNodeModel nodeModel) {
        return new OPS_compoundNodeView(nodeModel);
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
        return new OPS_compoundNodeDialog();
    }

}

