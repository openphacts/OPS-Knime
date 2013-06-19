package org.openphacts;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "OPS_InchiKeyToChemSpiderURL" Node.
 * Returns a ChemSpider URL corresponding to an input InChIKey string. Driven by ChemSpider.
 *
 * @author Ronald siebes
 */
public class OPS_InchiKeyToChemSpiderURLNodeFactory 
        extends NodeFactory<OPS_InchiKeyToChemSpiderURLNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public OPS_InchiKeyToChemSpiderURLNodeModel createNodeModel() {
        return new OPS_InchiKeyToChemSpiderURLNodeModel();
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
    public NodeView<OPS_InchiKeyToChemSpiderURLNodeModel> createNodeView(final int viewIndex,
            final OPS_InchiKeyToChemSpiderURLNodeModel nodeModel) {
        return new OPS_InchiKeyToChemSpiderURLNodeView(nodeModel);
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
        return new OPS_InchiKeyToChemSpiderURLNodeDialog();
    }

}

