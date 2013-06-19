package org.openphacts;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "OPS_chemical_structure_similarity_search" Node.
 * Returns a set of ChemSpider compound URLs, similar to the input molecule according to the specified algorithm and threshold. Driven by ChemSpider.
 *
 * @author Ronald Siebes - VUA
 */
public class OPS_chemical_structure_similarity_searchNodeFactory 
        extends NodeFactory<OPS_chemical_structure_similarity_searchNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public OPS_chemical_structure_similarity_searchNodeModel createNodeModel() {
        return new OPS_chemical_structure_similarity_searchNodeModel();
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
    public NodeView<OPS_chemical_structure_similarity_searchNodeModel> createNodeView(final int viewIndex,
            final OPS_chemical_structure_similarity_searchNodeModel nodeModel) {
        return new OPS_chemical_structure_similarity_searchNodeView(nodeModel);
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
        return new OPS_chemical_structure_similarity_searchNodeDialog();
    }

}

