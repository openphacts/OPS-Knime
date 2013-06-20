package org.openphacts;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "OPS_chemical_structure_substructure_search" Node.
 * Returns a set of ChemSpider compound URLs that contain the specified structure. Driven by ChemSpider.
 *
 * @author Ronald Siebes - VUA
 */
public class OPS_chemical_structure_substructure_searchNodeFactory 
        extends NodeFactory<OPS_chemical_structure_substructure_searchNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public OPS_chemical_structure_substructure_searchNodeModel createNodeModel() {
        return new OPS_chemical_structure_substructure_searchNodeModel();
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
    public NodeView<OPS_chemical_structure_substructure_searchNodeModel> createNodeView(final int viewIndex,
            final OPS_chemical_structure_substructure_searchNodeModel nodeModel) {
        return new OPS_chemical_structure_substructure_searchNodeView(nodeModel);
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
        return new OPS_chemical_structure_substructure_searchNodeDialog();
    }

}

