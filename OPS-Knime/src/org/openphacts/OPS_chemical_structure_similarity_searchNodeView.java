package org.openphacts;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "OPS_chemical_structure_similarity_search" Node.
 * Returns a set of ChemSpider compound URLs, similar to the input molecule according to the specified algorithm and threshold. Driven by ChemSpider.
 *
 * @author Ronald Siebes - VUA
 */
public class OPS_chemical_structure_similarity_searchNodeView extends NodeView<OPS_chemical_structure_similarity_searchNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link OPS_chemical_structure_similarity_searchNodeModel})
     */
    protected OPS_chemical_structure_similarity_searchNodeView(final OPS_chemical_structure_similarity_searchNodeModel nodeModel) {
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
        OPS_chemical_structure_similarity_searchNodeModel nodeModel = 
            (OPS_chemical_structure_similarity_searchNodeModel)getNodeModel();
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

