package org.openphacts;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "OPS_InchiStringToChemSpiderURL" Node.
 * Returns a ChemSpider URL corresponding to an input InChI string. Driven by ChemSpider.
 *
 * @author Ronald Siebes -VUA
 */
public class OPS_InchiStringToChemSpiderURLNodeFactory 
        extends NodeFactory<OPS_InchiStringToChemSpiderURLNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public OPS_InchiStringToChemSpiderURLNodeModel createNodeModel() {
        return new OPS_InchiStringToChemSpiderURLNodeModel();
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
    public NodeView<OPS_InchiStringToChemSpiderURLNodeModel> createNodeView(final int viewIndex,
            final OPS_InchiStringToChemSpiderURLNodeModel nodeModel) {
        return new OPS_InchiStringToChemSpiderURLNodeView(nodeModel);
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
        return new OPS_InchiStringToChemSpiderURLNodeDialog();
    }

}

