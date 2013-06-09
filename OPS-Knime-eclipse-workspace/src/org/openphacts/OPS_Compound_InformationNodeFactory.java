package org.openphacts;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "OPS_Compound_Information" Node.
 * 
 *
 * @author Ronald Siebes
 */
public class OPS_Compound_InformationNodeFactory 
        extends NodeFactory<OPS_Compound_InformationNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public OPS_Compound_InformationNodeModel createNodeModel() {
        return new OPS_Compound_InformationNodeModel();
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
    public NodeView<OPS_Compound_InformationNodeModel> createNodeView(final int viewIndex,
            final OPS_Compound_InformationNodeModel nodeModel) {
        return new OPS_Compound_InformationNodeView(nodeModel);
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
        return new OPS_Compound_InformationNodeDialog();
    }

}

