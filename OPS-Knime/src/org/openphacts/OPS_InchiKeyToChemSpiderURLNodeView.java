package org.openphacts;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "OPS_InchiKeyToChemSpiderURL" Node.
 * Returns a ChemSpider URL corresponding to an input InChIKey string. Driven by ChemSpider.
 *
 * @author Ronald siebes
 */
public class OPS_InchiKeyToChemSpiderURLNodeView extends NodeView<OPS_InchiKeyToChemSpiderURLNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link OPS_InchiKeyToChemSpiderURLNodeModel})
     */
    protected OPS_InchiKeyToChemSpiderURLNodeView(final OPS_InchiKeyToChemSpiderURLNodeModel nodeModel) {
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

