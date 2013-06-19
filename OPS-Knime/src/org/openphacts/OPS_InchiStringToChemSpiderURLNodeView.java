package org.openphacts;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "OPS_InchiStringToChemSpiderURL" Node.
 * Returns a ChemSpider URL corresponding to an input InChI string. Driven by ChemSpider.
 *
 * @author Ronald Siebes -VUA
 */
public class OPS_InchiStringToChemSpiderURLNodeView extends NodeView<OPS_InchiStringToChemSpiderURLNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link OPS_InchiStringToChemSpiderURLNodeModel})
     */
    protected OPS_InchiStringToChemSpiderURLNodeView(final OPS_InchiStringToChemSpiderURLNodeModel nodeModel) {
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

