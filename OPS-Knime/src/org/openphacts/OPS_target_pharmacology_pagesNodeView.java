package org.openphacts;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "OPS_target_pharmacology_pages" Node.
 * A page of items corresponding to acitivity values in the LDC for a given target
 *
 * @author Ronald Siebes - VUA
 */
public class OPS_target_pharmacology_pagesNodeView extends NodeView<OPS_target_pharmacology_pagesNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link OPS_target_pharmacology_pagesNodeModel})
     */
    protected OPS_target_pharmacology_pagesNodeView(final OPS_target_pharmacology_pagesNodeModel nodeModel) {
        super(nodeModel);

        // TODO instantiate the components of the view here.

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {

        // TODO retrieve the new model from your nodemodel and 
        // update the view.
        OPS_target_pharmacology_pagesNodeModel nodeModel = 
            (OPS_target_pharmacology_pagesNodeModel)getNodeModel();
        assert nodeModel != null;
        
        // be aware of a possibly not executed nodeModel! The data you retrieve
        // from your nodemodel could be null, emtpy, or invalid in any kind.
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onClose() {
    
        // TODO things to do when closing the view
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onOpen() {

        // TODO things to do when opening the view
    }

}

