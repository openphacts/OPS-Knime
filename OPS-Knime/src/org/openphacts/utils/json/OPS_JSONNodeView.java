package org.openphacts.utils.json;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "OPS_JSON" Node.
 * Reads a JSON file and transforms it to a KNIME table
 *
 * @author Ronald Siebes - VU Amsterdam
 */
public class OPS_JSONNodeView extends NodeView<OPS_JSONNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link OPS_JSONNodeModel})
     */
    protected OPS_JSONNodeView(final OPS_JSONNodeModel nodeModel) {
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
        OPS_JSONNodeModel nodeModel = 
            (OPS_JSONNodeModel)getNodeModel();
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

