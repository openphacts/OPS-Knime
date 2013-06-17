package org.openphacts;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "OPS_compound_pharmacology_pages" Node.
 * A page of items corresponding to acitivity values in the LDC for a given compound
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author openphacts
 */
public class OPS_compound_pharmacology_pagesNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the OPS_compound_pharmacology_pages node.
     */
    protected OPS_compound_pharmacology_pagesNodeDialog() {
    	  createNewGroup("Group 1:");
          addDialogComponent(new DialogComponentString(new 
          		SettingsModelString(OPS_compound_pharmacology_pagesNodeModel.API_URL, 
          				OPS_compound_pharmacology_pagesNodeModel.DEFAULT_API_URL), "Server:", true, 30));
          addDialogComponent(new DialogComponentString(new SettingsModelString(
          		OPS_compound_pharmacology_pagesNodeModel.APP_ID, OPS_compound_pharmacology_pagesNodeModel.APP_ID_DEFAULT), "Your application ID:"));
          addDialogComponent(new DialogComponentString(new SettingsModelString(
          		OPS_compound_pharmacology_pagesNodeModel.APP_KEY, OPS_compound_pharmacology_pagesNodeModel.APP_KEY_DEFAULT), "Your application key:"));
          addDialogComponent(new DialogComponentString(new SettingsModelString(
          		OPS_compound_pharmacology_pagesNodeModel.URI, OPS_compound_pharmacology_pagesNodeModel.URI_DEFAULT), "A compound URI. e.g.: http://www.conceptwiki.org/concept/38932552-111f-4a4e-a46a-4ed1d7bdf9d5"));
          addDialogComponent(new DialogComponentString(new SettingsModelString(
            		OPS_compound_pharmacology_pagesNodeModel.ASSAY_ORGANISM, OPS_compound_pharmacology_pagesNodeModel.ASSAY_ORGANISM_DEFAULT), " A literal organism in ChEMBL. "));
          addDialogComponent(new DialogComponentString(new SettingsModelString(
            		OPS_compound_pharmacology_pagesNodeModel.TRAY_ORGANISM, OPS_compound_pharmacology_pagesNodeModel.TRAY_ORGANISM_DEFAULT), " A literal target organism in ChEMBL. "));
          addDialogComponent(new DialogComponentString(new SettingsModelString(
            		OPS_compound_pharmacology_pagesNodeModel.ACTIVITY_TYPE, OPS_compound_pharmacology_pagesNodeModel.ACTIVITY_TYPE_DEFAULT), "One of the activity types listed at /pharmacology/filters/activities "));
          addDialogComponent(new DialogComponentString(new SettingsModelString(
            		OPS_compound_pharmacology_pagesNodeModel.ACTIVITY_VALUE, OPS_compound_pharmacology_pagesNodeModel.ACTIVITY_VALUE_DEFAULT), "Return activity values equal to this number. "));
          addDialogComponent(new DialogComponentString(new SettingsModelString(
            		OPS_compound_pharmacology_pagesNodeModel.MIN_ACTIVITY_VALUE, OPS_compound_pharmacology_pagesNodeModel.MIN_ACTIVITY_VALUE_DEFAULT), "Return activity values equal to this number. "));
          addDialogComponent(new DialogComponentString(new SettingsModelString(
            		OPS_compound_pharmacology_pagesNodeModel.MIN_EX_ACTIVITY_VALUE, OPS_compound_pharmacology_pagesNodeModel.MIN_EX_ACTIVITY_VALUE_DEFAULT), "Return activity values greater than this number. "));
          addDialogComponent(new DialogComponentString(new SettingsModelString(
            		OPS_compound_pharmacology_pagesNodeModel.MAX_ACTIVITY_VALUE, OPS_compound_pharmacology_pagesNodeModel.MAX_ACTIVITY_VALUE_DEFAULT), "Return activity values less than or equal to this number. "));
          addDialogComponent(new DialogComponentString(new SettingsModelString(
            		OPS_compound_pharmacology_pagesNodeModel.MAX_EX_ACTIVITY_VALUE, OPS_compound_pharmacology_pagesNodeModel.MAX_EX_ACTIVITY_VALUE_DEFAULT), "Return activity values less than this number. "));
          addDialogComponent(new DialogComponentString(new SettingsModelString(
            		OPS_compound_pharmacology_pagesNodeModel.ACTIVITY_UNIT, OPS_compound_pharmacology_pagesNodeModel.ACTIVITY_UNIT_DEFAULT), " The unit in which {activity_value} is given. See /pharmacology/filters/units/{activity_type} for allowed valued. For e.g. IC50: /pharmacology/filters/units/IC50 "));
          addDialogComponent(new DialogComponentNumber(new SettingsModelInteger(
            		OPS_compound_pharmacology_pagesNodeModel.PAGE, 3), " A number; the page that should be viewed ", 1));       
          addDialogComponent(new DialogComponentString(new SettingsModelString(
            		OPS_compound_pharmacology_pagesNodeModel.PAGE_SIZE, OPS_compound_pharmacology_pagesNodeModel.PAGE_SIZE_DEFAULT), "The desired page size. Set to all to retrieve all results in a single page. "));
          addDialogComponent(new DialogComponentStringSelection(
                  new SettingsModelString(OPS_compound_pharmacology_pagesNodeModel.ORDER_BY, OPS_compound_pharmacology_pagesNodeModel.ORDER_BY_DEFAULT),
                  "Branch:", "?activity_value","DESC(?activity_value)","?assay_description","DESC(?assay_description)","?assay_organism","DESC(?assay_organism)","?assay_uri","DESC(?assay_uri)","?bNode1","DESC(?bNode1)","?chembl_uri","DESC(?chembl_uri)","?compound_name","DESC(?compound_name)","?cs_uri","DESC(?cs_uri)","?cw_uri","DESC(?cw_uri)","?db_uri","DESC(?db_uri)","?doi","DESC(?doi)","?doi_internal","DESC(?doi_internal)","?drugType","DESC(?drugType)","?drugType_uri","DESC(?drugType_uri)","?drug_name","DESC(?drug_name)","?equiv_assay","DESC(?equiv_assay)","?equiv_compound","DESC(?equiv_compound)","?equiv_target","DESC(?equiv_target)","?inchi","DESC(?inchi)","?inchiKey","DESC(?inchiKey)","?item","DESC(?item)","?molweight","DESC(?molweight)","?num_ro5_violations","DESC(?num_ro5_violations)","?ops_item","DESC(?ops_item)","?pmid","DESC(?pmid)","?relation","DESC(?relation)","?smiles","DESC(?smiles)","?std_type","DESC(?std_type)","?std_unit","DESC(?std_unit)","?std_value","DESC(?std_value)","?target_name","DESC(?target_name)","?target_organism","DESC(?target_organism)","?target_uri","DESC(?target_uri)" ));


    }
}

