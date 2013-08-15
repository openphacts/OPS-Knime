package org.openphacts.utils;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "RegExFilter" Node.
 * This node iterates over every cell from the first input table and executes the regular expression from the second input table. Every non-empty result is added as a row in the output table. * n * nThe second row of the second input table provides the option to give a name to the result column 
 *
 * @author Ronald Siebes
 */
public class RegExFilterNodeFactory 
        extends NodeFactory<RegExFilterNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public RegExFilterNodeModel createNodeModel() {
        return new RegExFilterNodeModel();
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
    public NodeView<RegExFilterNodeModel> createNodeView(final int viewIndex,
            final RegExFilterNodeModel nodeModel) {
        return new RegExFilterNodeView(nodeModel);
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
        return new RegExFilterNodeDialog();
    }

}

