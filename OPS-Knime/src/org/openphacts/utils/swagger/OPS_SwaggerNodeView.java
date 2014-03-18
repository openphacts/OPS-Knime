package org.openphacts.utils.swagger;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "OPS_Swagger" Node.
 * This node loads a swagger web service description file and lets the user select on of the services via the config panel together with setting default parameters. An input table can be added with more parameters and values. The result of executing the node is a URL with the service call. 
 *
 * @author Ronald Siebes - VU Amsterdam
 */
public class OPS_SwaggerNodeView extends NodeView<OPS_SwaggerNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link OPS_SwaggerNodeModel})
     */
    protected OPS_SwaggerNodeView(final OPS_SwaggerNodeModel nodeModel) {
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
        OPS_SwaggerNodeModel nodeModel = 
            (OPS_SwaggerNodeModel)getNodeModel();
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

