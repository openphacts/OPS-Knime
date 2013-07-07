package org.openphacts.utils.swagger;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "SWAGGER_ServiceTemplates" Node.
 * 
 *
 * @author Ronald Siebes - VU Amsterdam
 */
public class SWAGGER_ServiceTemplatesNodeFactory 
        extends NodeFactory<SWAGGER_ServiceTemplatesNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public SWAGGER_ServiceTemplatesNodeModel createNodeModel() {
        return new SWAGGER_ServiceTemplatesNodeModel();
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
    public NodeView<SWAGGER_ServiceTemplatesNodeModel> createNodeView(final int viewIndex,
            final SWAGGER_ServiceTemplatesNodeModel nodeModel) {
        return new SWAGGER_ServiceTemplatesNodeView(nodeModel);
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
        return new SWAGGER_ServiceTemplatesNodeDialog();
    }

}

