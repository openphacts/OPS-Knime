package org.openphacts.utils;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "RegExFilter" Node.
 * This node iterates over every cell from the first input table and executes the regular expression from the second input table. Every non-empty result is added as a row in the output table. * n * nThe second row of the second input table provides the option to give a name to the result column 
 *
 * @author Ronald Siebes
 */
public class RegExFilterNodeView extends NodeView<RegExFilterNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link RegExFilterNodeModel})
     */
    protected RegExFilterNodeView(final RegExFilterNodeModel nodeModel) {
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
        RegExFilterNodeModel nodeModel = 
            (RegExFilterNodeModel)getNodeModel();
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

