package org.openphacts;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "OpenPhactsData" Node.
 * 
 *
 * @author OpenPhacts
 */
public class OpenPhactsDataNodeFactory 
        extends NodeFactory<OpenPhactsDataNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public OpenPhactsDataNodeModel createNodeModel() {
        return new OpenPhactsDataNodeModel();
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
    public NodeView<OpenPhactsDataNodeModel> createNodeView(final int viewIndex,
            final OpenPhactsDataNodeModel nodeModel) {
        return new OpenPhactsDataNodeView(nodeModel);
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
        return new OpenPhactsDataNodeDialog();
    }

}

