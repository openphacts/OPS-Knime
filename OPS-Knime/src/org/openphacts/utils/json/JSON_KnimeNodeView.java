package org.openphacts.utils.json;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "JSON_Knime" Node.
 * Reads a JSON string, converts its hierarchical structure into a flat 2D matrix and exports it as a Knime table. 
 *
 * @author Ronald Siebes - VU Amsterdam
 */
public class JSON_KnimeNodeView extends NodeView<JSON_KnimeNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link JSON_KnimeNodeModel})
     */
    protected JSON_KnimeNodeView(final JSON_KnimeNodeModel nodeModel) {
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

