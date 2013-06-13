package org.openphacts;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "OPS_search_freetext" Node.
 * 
 *
 * @author openphacts
 */
public class OPS_search_freetextNodeFactory 
        extends NodeFactory<OPS_search_freetextNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public OPS_search_freetextNodeModel createNodeModel() {
        return new OPS_search_freetextNodeModel();
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
    public NodeView<OPS_search_freetextNodeModel> createNodeView(final int viewIndex,
            final OPS_search_freetextNodeModel nodeModel) {
        return new OPS_search_freetextNodeView(nodeModel);
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
        return new OPS_search_freetextNodeDialog();
    }

}

