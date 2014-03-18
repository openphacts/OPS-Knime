package org.openphacts.utils.swagger;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "OPS_Swagger" Node.
 * This node loads a swagger web service description file and lets the user select on of the services via the config panel together with setting default parameters. An input table can be added with more parameters and values. The result of executing the node is a URL with the service call. 
 *
 * @author Ronald Siebes - VU Amsterdam
 */
public class OPS_SwaggerNodeFactory 
        extends NodeFactory<OPS_SwaggerNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public OPS_SwaggerNodeModel createNodeModel() {
        return new OPS_SwaggerNodeModel();
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
    public NodeView<OPS_SwaggerNodeModel> createNodeView(final int viewIndex,
            final OPS_SwaggerNodeModel nodeModel) {
        return new OPS_SwaggerNodeView(nodeModel);
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
        return new OPS_SwaggerNodeDialog();
    }

}

