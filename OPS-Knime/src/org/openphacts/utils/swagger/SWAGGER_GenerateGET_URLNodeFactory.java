package org.openphacts.utils.swagger;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "SWAGGER_GenerateGET_URL" Node.
 * 
 *
 * @author Ronald Siebes - VUA
 */
public class SWAGGER_GenerateGET_URLNodeFactory 
        extends NodeFactory<SWAGGER_GenerateGET_URLNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public SWAGGER_GenerateGET_URLNodeModel createNodeModel() {
        return new SWAGGER_GenerateGET_URLNodeModel();
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
    public NodeView<SWAGGER_GenerateGET_URLNodeModel> createNodeView(final int viewIndex,
            final SWAGGER_GenerateGET_URLNodeModel nodeModel) {
        return new SWAGGER_GenerateGET_URLNodeView(nodeModel);
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
        return new SWAGGER_GenerateGET_URLNodeDialog();
    }

}

