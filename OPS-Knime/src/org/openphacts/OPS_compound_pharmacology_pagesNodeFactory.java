package org.openphacts;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "OPS_compound_pharmacology_pages" Node.
 * A page of items corresponding to acitivity values in the LDC for a given compound
 *
 * @author openphacts
 */
public class OPS_compound_pharmacology_pagesNodeFactory 
        extends NodeFactory<OPS_compound_pharmacology_pagesNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public OPS_compound_pharmacology_pagesNodeModel createNodeModel() {
        return new OPS_compound_pharmacology_pagesNodeModel();
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
    public NodeView<OPS_compound_pharmacology_pagesNodeModel> createNodeView(final int viewIndex,
            final OPS_compound_pharmacology_pagesNodeModel nodeModel) {
        return new OPS_compound_pharmacology_pagesNodeView(nodeModel);
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
        return new OPS_compound_pharmacology_pagesNodeDialog();
    }

}

