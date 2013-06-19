package org.openphacts;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "OPS_SMILESToChemSpiderURL" Node.
 * Returns a ChemSpider URL corresponding to an input SMILES string. Driven by ChemSpider.
 *
 * @author Ronald Siebes - VUA
 */
public class OPS_SMILESToChemSpiderURLNodeView extends NodeView<OPS_SMILESToChemSpiderURLNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link OPS_SMILESToChemSpiderURLNodeModel})
     */
    protected OPS_SMILESToChemSpiderURLNodeView(final OPS_SMILESToChemSpiderURLNodeModel nodeModel) {
        super(nodeModel);
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onClose() {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onOpen() {
        // TODO: generated method stub
    }

}

