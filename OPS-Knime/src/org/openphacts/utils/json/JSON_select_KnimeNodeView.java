package org.openphacts.utils.json;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "JSON_select_Knime" Node.
 * Generates a table representing the result from a json url according to a specified list of keys-headers
 *
 * @author Ronald Siebes
 */
public class JSON_select_KnimeNodeView extends NodeView<JSON_select_KnimeNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link JSON_select_KnimeNodeModel})
     */
    protected JSON_select_KnimeNodeView(final JSON_select_KnimeNodeModel nodeModel) {
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

