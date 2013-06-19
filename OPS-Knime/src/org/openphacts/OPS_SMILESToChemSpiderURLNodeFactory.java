package org.openphacts;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "OPS_SMILESToChemSpiderURL" Node.
 * Returns a ChemSpider URL corresponding to an input SMILES string. Driven by ChemSpider.
 *
 * @author Ronald Siebes - VUA
 */
public class OPS_SMILESToChemSpiderURLNodeFactory 
        extends NodeFactory<OPS_SMILESToChemSpiderURLNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public OPS_SMILESToChemSpiderURLNodeModel createNodeModel() {
        return new OPS_SMILESToChemSpiderURLNodeModel();
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
    public NodeView<OPS_SMILESToChemSpiderURLNodeModel> createNodeView(final int viewIndex,
            final OPS_SMILESToChemSpiderURLNodeModel nodeModel) {
        return new OPS_SMILESToChemSpiderURLNodeView(nodeModel);
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
        return new OPS_SMILESToChemSpiderURLNodeDialog();
    }

}

