package org.openphacts.utils.json;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettings;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.config.base.ConfigBase;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentButton;
import org.knime.core.node.defaultnodesettings.DialogComponentLabel;
import org.knime.core.node.defaultnodesettings.DialogComponentOptionalString;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringListSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelOptionalString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.openphacts.utils.swagger.OPS_SwaggerNodeModel;

/**
 * <code>NodeDialog</code> for the "OPS_JSON" Node.
 * Reads a JSON file and transforms it to a KNIME table
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Ronald Siebes - VU Amsterdam
 */
public class OPS_JSONNodeDialog extends DefaultNodeSettingsPane {
	
	protected NodeLogger logger; //very important ;-)
	//some internal global variables (could be neater)
    private static int rowIndex = 0;

	Collection<String> optionsArrayDummy;//{"niks"};
	Collection<String> optionNamesArrayDummy;//{"niks"};
	Collection<String> allOptionsArrayDummy;//{"niks"};
	Collection<String> allOptionNamesArrayDummy;//{"niks"};
	private String[] optionsArray = {"niks"};
	private String[] optionNamesArray = {"niks"};
	// the settingsmodels that persistently store things, even after knime shuts down properly
	private  SettingsModelString json_config_url = new SettingsModelString(
			OPS_JSONNodeModel.JSON_URL, OPS_JSONNodeModel.DEFAULT_JSON_URL);
	private final  SettingsModelStringArray selection_parameters = new SettingsModelStringArray(OPS_JSONNodeModel.SELECTION_PARAMETERS, OPS_JSONNodeModel.DEFAULT_SELECTION_PARAMETERS);
	private final  SettingsModelStringArray selection_customized_names = new SettingsModelStringArray(OPS_JSONNodeModel.SELECTION_CUSTOMIZED_NAMES, OPS_JSONNodeModel.DEFAULT_SELECTION_CUSTOMIZED_NAMES);
	private final  SettingsModelStringArray all_parameters = new SettingsModelStringArray(OPS_JSONNodeModel.ALL_PARAMETERS, OPS_JSONNodeModel.DEFAULT_ALL_PARAMETERS);
	private final  SettingsModelStringArray all_customized_names = new SettingsModelStringArray(OPS_JSONNodeModel.ALL_CUSTOMIZED_NAMES, OPS_JSONNodeModel.DEFAULT_ALL_CUSTOMIZED_NAMES);
    private LinkedHashMap<String,JCheckBox> selections = new LinkedHashMap<String,JCheckBox>();
    private LinkedHashMap<String,JLabel> labels = new  LinkedHashMap<String,JLabel>();
    private LinkedHashMap<String,JTextField> custom_labels = new  LinkedHashMap<String,JTextField>();
	DialogComponentStringListSelection	var_sel = new DialogComponentStringListSelection(selection_parameters,"var_sel",optionsArrayDummy,1,false,0);
    DialogComponentStringListSelection name_sel = new DialogComponentStringListSelection(selection_customized_names,"var_sel_names",optionNamesArrayDummy,1,false,0);
    DialogComponentStringListSelection var_all =  new DialogComponentStringListSelection(all_parameters,"var_all",allOptionsArrayDummy,1,false,0);
    DialogComponentStringListSelection name_all = new DialogComponentStringListSelection(all_customized_names,"var_all_names",allOptionNamesArrayDummy,1,false,0);
    DialogComponentButton fetchExampleButton = null;
	JScrollPane scrollPane;
	DialogComponentString json_config_urlDialog = null;
	JPanel optionPanel;
	LinkedHashSet<String> selectionWords = new LinkedHashSet<String>();
	LinkedHashSet<String> customWords = new LinkedHashSet<String>();
	
	
    protected OPS_JSONNodeDialog() {
    	super();
    	

    	addDialogComponent(var_sel);
    	addDialogComponent(name_sel);
    	addDialogComponent(var_all);
    	addDialogComponent(name_all);
    	
    	
    	
    	
		logger = NodeLogger.getLogger(getClass());
		optionPanel= new JPanel();
		optionPanel.setLayout(new BoxLayout(optionPanel,BoxLayout.PAGE_AXIS));
		optionPanel.setAutoscrolls(true);//setPreferredSize(new Dimension(600, 450));
		optionPanel.setBorder(BorderFactory.createTitledBorder("select JSON Columns and optional new name"));
	    
		scrollPane = new JScrollPane(optionPanel);
	        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        json_config_urlDialog = new DialogComponentString(json_config_url,
				"Example JSON URL: ");
        json_config_urlDialog.setSizeComponents(700, 40);
        
        
        fetchExampleButton = new DialogComponentButton(
				"Fetch example") {

		};
		addDialogComponent(json_config_urlDialog);
		optionPanel.add(json_config_urlDialog.getComponentPanel());
		optionPanel.add(fetchExampleButton.getComponentPanel());

		getPanel().add(scrollPane);
		getPanel().repaint();
		
		fetchExampleButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
		    	resetOptions();
				buildUI();
				
			}
		});
        
                    
    }
    
    private void resetOptions(){
    	JSONObject exampleJSON;
    	System.out.println("resetOptions");
    	selections.clear();
    	labels.clear();
    	custom_labels.clear();
    	//Component dialog = optionPanel.getComponent(0);
    	//Component button = optionPanel.getComponent(1);
    	optionPanel.removeAll();
		optionPanel.add(json_config_urlDialog.getComponentPanel());
		optionPanel.add(fetchExampleButton.getComponentPanel());
    	//optionPanel.add(dialog);
    //	optionPanel.add(button);
    	getPanel().repaint();
    	
    	//first empty the selection array
    	for(int i = 0 ; i<selection_parameters.getStringArrayValue().length;i++){
    		selection_parameters.getStringArrayValue()[i]="";
    	}
    	
    	try {
			exampleJSON = grabSomeJson(buildRequestURL(json_config_url
					.getStringValue()));
			Vector<String> jsonOptions = getJSONOptions(exampleJSON);
			String[] jsonOptionsArray = Arrays.copyOf(jsonOptions.toArray(), jsonOptions.toArray().length, String[].class);
			System.out.println("jsonOptionsArrayLength= "+jsonOptionsArray.length);
			all_parameters.setStringArrayValue(jsonOptionsArray);
			all_customized_names.setStringArrayValue(jsonOptionsArray);
			Vector<String> varSelVector = new Vector<String>();
			varSelVector.add("");
			selection_parameters.setStringArrayValue(new String[0]);
			
			System.out.println("all_parameters lenght= "+all_parameters.getStringArrayValue().length);
			//all_parameters.setStringArrayValue((String[]) varSelVector.toArray()); 
		    for(int i=0;i< jsonOptions.size();i++){
		    	String value = jsonOptions.get(i);
		    	final JCheckBox box = new JCheckBox(value, false);
		    	final JLabel label = new JLabel(value);
		    	final JTextField customLabel = new JTextField(value);
		    	customLabel.setName(value);
		    	selections.put(value,box);
		    	labels.put(value,label);
		    	custom_labels.put(value,customLabel);
		    }
		    var_all.replaceListItems(jsonOptions, new String[0]);
		    name_all.replaceListItems(jsonOptions, new String[0]);
		   // buildUI();
		    	
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showException(e);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showException(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showException(e);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showException(e);
		}
		


    }
    protected void buildUI(){
    	System.out.println("building ui");
  	
    	
    	String[] parameters = all_parameters.getStringArrayValue();
    	String[] custom_parameters = all_customized_names.getStringArrayValue();
    	
    	System.out.println("names length="+all_parameters.getStringArrayValue().length);
    	optionPanel.removeAll();
    	//addDialogComponent(json_config_urlDialog);
		optionPanel.add(json_config_urlDialog.getComponentPanel());
		
		optionPanel.add(fetchExampleButton.getComponentPanel());

		getPanel().add(scrollPane);
		getPanel().repaint();
		
		for(int i = 0;i<parameters.length;i++){
			String name = parameters[i];
			if(!name.equals("")){
				System.out.println("working on "+name +" with custom name "+custom_parameters[i]);
				JCheckBox sel = selections.get(name);
				final String selText = sel.getText();
				JLabel lab = labels.get(name);
				final JTextField customLabel = custom_labels.get(name);
				///final String labelString = customLabel.getText();
				//selections.put(name, sel);
				//labels.put(name, lab);
				//custom_labels.put(name, customLabel);
				
				customLabel.getDocument().addDocumentListener(new DocumentListener() {
		  
		    		
		    		  public void changedUpdate(DocumentEvent e) {
		    			 
		    			
		    		  }
	    			  public void removeUpdate(DocumentEvent e) {
	    				  Iterator<String> it3 = selections.keySet().iterator();
			         		Vector<String> selectionVector = new Vector<String>();
			        	 	Vector<String> selectionNamesVector = new Vector<String>();
			        	 	
			        	 	
			         		while(it3.hasNext()){
			         			JCheckBox currentSelectionBox = selections.get(it3.next());
			         			if(currentSelectionBox.isSelected()){
			         				selectionVector.add(currentSelectionBox.getText());
			         				
			         			
			         			}
			         			
			         		}
			         		if(selectionVector.size()==0){
			         			
			         			JOptionPane.showMessageDialog(getPanel(), "Please select at least one column that you want to include", "Warning",
			         			        JOptionPane.WARNING_MESSAGE);
			         		}else{
			         			updateSettings();
			         		}
	    			  }
	    			  public void insertUpdate(DocumentEvent e) {
	    				  
	    				  Iterator<String> it3 = selections.keySet().iterator();
			         		Vector<String> selectionVector = new Vector<String>();
			        	 	Vector<String> selectionNamesVector = new Vector<String>();
			        	 	
			        	 	
			         		while(it3.hasNext()){
			         			JCheckBox currentSelectionBox = selections.get(it3.next());
			         			if(currentSelectionBox.isSelected()){
			         				selectionVector.add(currentSelectionBox.getText());
			         				
			         			
			         			}
			         			
			         		}
			         		if(selectionVector.size()==0){
			         			
			         			JOptionPane.showMessageDialog(getPanel(), "Please select at least one column that you want to include", "Warning",
			         			        JOptionPane.WARNING_MESSAGE);
			         		}else{
			         			updateSettings();
			         		}
	    			  }
	    			  
		    	});
				sel.addItemListener(new ItemListener() {

		            @Override
		            public void itemStateChanged(ItemEvent e) {
		            	Iterator<String> it3 = selections.keySet().iterator();
		         		Vector<String> selectionVector = new Vector<String>();
		        	 	Vector<String> selectionNamesVector = new Vector<String>();
		        	 	
		        	 	
		         		while(it3.hasNext()){
		         			JCheckBox currentSelectionBox = selections.get(it3.next());
		         			if(currentSelectionBox.isSelected()){
		         				selectionVector.add(currentSelectionBox.getText());
		         				
		         			
		         			}
		         			
		         		}
		         		if(selectionVector.size()==0){
		         			
		         			JOptionPane.showMessageDialog(getPanel(), "Please select at least one column that you want to include", "Warning",
		         			        JOptionPane.WARNING_MESSAGE);
		         		}else{
		         			updateSettings();
		         		}
		                
		            }

				
		        });
				optionPanel.add(sel);
				//optionPanel.add(lab);
				optionPanel.add(customLabel);
			}
			
		}
		optionPanel.repaint();
		optionPanel.updateUI();
    }
    
    private void updateSettings(){
    	Iterator<String> it = labels.keySet().iterator();
	 	Vector<String> updatedLabels = new Vector<String>();
 		while(it.hasNext()){
 			String currentLabel = labels.get(it.next()).getText();
 			updatedLabels.add(currentLabel);
 			System.out.println("label: "+currentLabel);
 			
 		}
 		
 		String[] updatedLabelsArray = new String[updatedLabels.size()];
 		for(int i = 0 ;i<updatedLabelsArray.length;i++){
 			updatedLabelsArray[i]= updatedLabels.get(i);
 		}
 		all_parameters.setStringArrayValue(updatedLabelsArray);
 		var_all.replaceListItems(updatedLabels, updatedLabelsArray);
 		
 		
    	Iterator<String> it2 = custom_labels.keySet().iterator();
	 	Vector<String> updatedCustomLabels = new Vector<String>();
 		while(it2.hasNext()){
 			String currentCustomLabel = custom_labels.get(it2.next()).getText();
 			updatedCustomLabels.add(currentCustomLabel);
 			System.out.println("custom label: "+currentCustomLabel);
 			
 		}
 		String[] updatedCustomLabelsArray = new String[updatedCustomLabels.size()];
 		for(int i = 0 ;i<updatedCustomLabelsArray.length;i++){
 			updatedCustomLabelsArray[i]= updatedCustomLabels.get(i);
 		}
 		all_customized_names.setStringArrayValue(updatedCustomLabelsArray);
 		name_all.replaceListItems(updatedCustomLabels, updatedCustomLabelsArray);
 		
 		Iterator<String> it3 = selections.keySet().iterator();
 		Vector<String> selectionVector = new Vector<String>();
	 	Vector<String> selectionNamesVector = new Vector<String>();
	 	
	 	
 		while(it3.hasNext()){
 			JCheckBox currentSelectionBox = selections.get(it3.next());
 			if(currentSelectionBox.isSelected()){
 				selectionVector.add(currentSelectionBox.getText());
 				int index = updatedLabels.indexOf(currentSelectionBox.getText());
 				String customName = updatedCustomLabels.get(index);
 				selectionNamesVector.add(customName);
 				System.out.println("Selected:"+ currentSelectionBox.getText());
 				System.out.println("Selected custom name:"+ customName);
 			}
 			
 		}
 		
 		String[] updatedSelectedLabelsArray = new String[selectionVector.size()];
 		String[] updatedSelectedCustomLabelsArray = new String[selectionVector.size()];
 		for(int i = 0 ;i<updatedSelectedLabelsArray.length;i++){
 			updatedSelectedLabelsArray[i]= selectionVector.get(i);
 			updatedSelectedCustomLabelsArray[i] = selectionNamesVector.get(i);
 		}
 		selection_parameters.setStringArrayValue(updatedSelectedLabelsArray);
 		selection_customized_names.setStringArrayValue(updatedSelectedCustomLabelsArray);
 		var_sel.replaceListItems(selectionVector, updatedSelectedLabelsArray);
 		name_sel.replaceListItems(selectionNamesVector, updatedSelectedCustomLabelsArray);
 		
    	
    }
	protected URL buildRequestURL(String c_uri) throws URISyntaxException,
		MalformedURLException, UnsupportedEncodingException {
	
		URI uri = new URI(c_uri);
		return uri.toURL();
	}
	
	protected JSONObject grabSomeJson(URL url) throws IOException {
		String str = "";
		URL x = url;
		BufferedReader in = new BufferedReader(new InputStreamReader(
				x.openStream()));

		String inputLine;

		while ((inputLine = in.readLine()) != null)
			str += inputLine + "\n";
		in.close();

		JSONObject jo = (JSONObject) JSONSerializer.toJSON(str);

		return jo;

	}
	
	private Vector<String> getJSONOptions(JSONObject o){
		
		
		Map<String, Map<String, String>> resultTable = new LinkedHashMap<String, Map<String, String>>();
		dim(resultTable, "", o); // recursion start
		
		Vector<String> result = new Vector<String>(resultTable.keySet());
	
		return result;
	}
	boolean loadedParameters = false;
	 public void loadAdditionalSettingsFrom(NodeSettingsRO settings, DataTableSpec[] specs) throws NotConfigurableException {
		 System.out.println("@loadAdditionalSettingsFrom");
		
		 try {
			 if(!loadedParameters){
				 System.out.println("@loadAdditionalSettingsFrom: loading parameters...");
	        	selection_parameters.loadSettingsFrom(settings);
	          
	        	all_parameters.loadSettingsFrom(settings);
	        	all_customized_names.loadSettingsFrom(settings);
				 selection_customized_names.loadSettingsFrom(settings);
			 
	        	 System.out.println("start2 with parameters"+all_parameters.getStringArrayValue().length);
	        	 System.out.println("start2 with parameters"+all_customized_names.getStringArrayValue().length);
	             for (int i=0;i<all_customized_names.getStringArrayValue().length;i++){
	            	 System.out.println("we have custom name:"+all_customized_names.getStringArrayValue()[i] );
	             }
	             for (int i=0;i<selection_customized_names.getStringArrayValue().length;i++){
	            	 System.out.println("we have selected name:"+selection_customized_names.getStringArrayValue()[i] );
	             }
	             selections = new LinkedHashMap<String,JCheckBox>();
	             labels = new  LinkedHashMap<String,JLabel>();
	             custom_labels = new  LinkedHashMap<String,JTextField>();
	             String[] _all_params = all_parameters.getStringArrayValue();
	             String[] _all_names = all_customized_names.getStringArrayValue();
	             String[] _selection_params = selection_parameters.getStringArrayValue();
	             for(int i=0;i< _all_params.length;i++){
	 		    	String value = _all_params[i];
	 		    	
	 		    	boolean inThere = false;
	 		    	for(int j=0;j<_selection_params.length;j++){
	 		    		if(_selection_params[j].equals(value)){
	 		    			inThere= true;
	 		    		}
	 		    	}
	 		    	final JCheckBox box = new JCheckBox(value, inThere);
	 		    	
	 		    	final JLabel label = new JLabel(_all_params[i]);
	 		    	final JTextField customLabel = new JTextField(_all_names[i]);
	 		    	customLabel.setName(value);
	 		    	selections.put(value,box);
	 		    	labels.put(value,label);
	 		    	custom_labels.put(value,customLabel);
	 		    	
	 		    }
			 }
	             
	               
	        } catch (InvalidSettingsException ex) {
	                ex.printStackTrace();
	        }
		    
		    
		 	if(!loadedParameters){
		 		
		 		
		        	//initParamGUI();
		        	buildUI();
		        	updateSettings();
		       		         
		        
		        loadedParameters = true;
		 	}else{
		 		
		 	}
	        
	        
	         
	    }
	   
	 private void updateHiddenGUI(){
		 
	 }

	    @Override
	    public void saveAdditionalSettingsTo(NodeSettingsWO settings) {
	    	System.out.println("@saveAdditionalSettingsTo in dialog");
	    	try {
				var_all.saveSettingsTo(settings);
				name_all.saveSettingsTo(settings);
				var_sel.saveSettingsTo(settings);
				name_sel.saveSettingsTo(settings);
			} catch (InvalidSettingsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	/*
	    	LinkedHashSet<String> lengggth = new LinkedHashSet<String>();
	    	lengggth.add(""+custom_labels.keySet().size());
	    	Variables.jsonCustomLabels.put(super.getNodeContext().hashCode()+""+OPS_JSONNodeModel.ALL_CUSTOMIZED_NAMES.hashCode()+"", lengggth);
	    	 System.out.println("start with parameters"+all_customized_names.getStringArrayValue().length);
	    	 ArrayList<String> currentOptionLabelSet = new ArrayList<String>();
			  Iterator<String> it = custom_labels.keySet().iterator();
			  while(it.hasNext()){
				  JTextField field = custom_labels.get(it.next());
				  currentOptionLabelSet.add(field.getText());
			  }
			  
			  name_sel.replaceListItems(currentOptionLabelSet, new String[0]);
			  String[] array = new String[currentOptionLabelSet.size()];
			  all_customized_names.setStringArrayValue(currentOptionLabelSet.toArray(array));
			  System.out.println("start with parameters again"+all_customized_names.getStringArrayValue().length);
			  //this.getNodeContext().hashCode()
			  settings.addStringArray(OPS_JSONNodeModel.SELECTION_CUSTOMIZED_NAMES, array);
			  
	    	selection_parameters.saveSettingsTo(settings);
	    	selection_customized_names.saveSettingsTo(settings);
	    	all_parameters.saveSettingsTo(settings);
	    	all_customized_names.saveSettingsTo(settings);
	    	
	    	selection_parameters.saveSettingsTo(settings);
	    	selection_customized_names.saveSettingsTo(settings);
	    	all_parameters.saveSettingsTo(settings);
	    	all_customized_names.saveSettingsTo(settings);
	    	
	    	try {
				var_all.saveSettingsTo(settings);
				name_all.saveSettingsTo(settings);
				var_sel.saveSettingsTo(settings);
			} catch (InvalidSettingsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
	    	/*
	    	System.out.println("all param length "+ all_parameters.getStringArrayValue().length);
	    	System.out.println("selection param length "+ selection_parameters.getStringArrayValue().length);
	    	System.out.println("custom param length "+ all_customized_names.getStringArrayValue().length);
	    	 try {
				name_all.saveSettingsTo(settings);
				name_all.getModel().saveSettingsTo(settings);
			} catch (InvalidSettingsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	 System.out.println("all param length "+ all_parameters.getStringArrayValue().length);
		    	System.out.println("selection param length "+ selection_parameters.getStringArrayValue().length);
		    	System.out.println("custom param length "+ all_customized_names.getStringArrayValue().length);
		    	*/
	    }
	    
	private void initParamGUI(){
		System.out.println("@ initParamGUI");
		String[] names =all_parameters.getStringArrayValue(); 
    	for(int i=0;i<names.length;i++){
    		
    		if(!names[i].equals("")){
    			String currentName = names[i];
    			
    			final JCheckBox box = new JCheckBox(currentName, false);
		    	final JLabel label = new JLabel(currentName);
		    	final JTextField customLabel = new JTextField(currentName);
		    	customLabel.setName(currentName);
		    	selections.put(currentName,box);
		    	labels.put(currentName,label);
		    	custom_labels.put(currentName,customLabel);
		    	box.addItemListener(new ItemListener() {

		            @Override
		            public void itemStateChanged(ItemEvent e) {
		            	if(e.getStateChange()==ItemEvent.SELECTED){
		            		System.out.println("wat");
		            		String [] currentSelection = selection_parameters.getStringArrayValue().clone();
		            		boolean insideArray = false;
		            		int emptyPosition = -1;
		            		for(int j = 0;j<currentSelection.length;j++){
		            			if(currentSelection[j].equals("")){
		            				emptyPosition = j;
		            			}else if(currentSelection[j].equals(box.getText())){
		            				insideArray = true;
		            			}
		            		}
		            		if(!insideArray && emptyPosition !=-1){
		            			currentSelection[emptyPosition]=box.getText();
		            			selection_parameters.setStringArrayValue(currentSelection);
		            			System.out.println("added "+currentSelection+" to position"+emptyPosition );
		            		}
		            		
		            	}
		                
		            }

				
		        });
		    	
		    	customLabel.getDocument().addDocumentListener(new DocumentListener() {
		    		 String name = customLabel.getName();
		    		
		    		  public void changedUpdate(DocumentEvent e) {
		    			  String [] optionLabels = all_parameters.getStringArrayValue().clone();
		    			  String [] optionCustomLabels = all_customized_names.getStringArrayValue().clone();
		    			  for(int i = 0 ;i<optionLabels.length;i++){
		    				  if(optionLabels[i].equals(name)){
		    					  optionCustomLabels[i] = customLabel.getText();
		    					  all_customized_names.setStringArrayValue(optionCustomLabels);
		    				  }
		    			  }   	
		    		  }
	    			  public void removeUpdate(DocumentEvent e) {
	    				  String [] optionLabels = all_parameters.getStringArrayValue().clone();
		    			  String [] optionCustomLabels = all_customized_names.getStringArrayValue().clone();
		    			  for(int i = 0 ;i<optionLabels.length;i++){
		    				  if(optionLabels[i].equals(name)){
		    					  optionCustomLabels[i] = customLabel.getText();
		    					  all_customized_names.setStringArrayValue(optionCustomLabels);
		    				  }
		    			  }  
	    			  }
	    			  public void insertUpdate(DocumentEvent e) {
	    				  String [] optionLabels = all_parameters.getStringArrayValue().clone();
		    			  String [] optionCustomLabels = all_customized_names.getStringArrayValue().clone();
		    			  for(int i = 0 ;i<optionLabels.length;i++){
		    				  if(optionLabels[i].equals(name)){
		    					  optionCustomLabels[i] = customLabel.getText();
		    					  all_customized_names.setStringArrayValue(optionCustomLabels);
		    				  }
		    			  }  
	    			  }
	    			  
		    	});
    		}
    	}
		
	}
    protected void showException(Throwable throwable)
    {
        logger.error("Exception", throwable);
        JOptionPane.showMessageDialog(getPanel(), throwable.toString(), "Error",
                JOptionPane.ERROR_MESSAGE);
    }
    
    
    protected static void dim(Map<String, Map<String, String>> resultTable,
			String currentPath, Object currentJSON) {

		String type = currentJSON.getClass().getName();
		if (type.equals("net.sf.json.JSONArray")) {
			JSONArray jArray = (JSONArray) currentJSON;

			for (int i = 0; i < jArray.size(); i++) {

				if (!(jArray.get(i).getClass().getName()
						.equals("net.sf.json.JSONArray"))
						&& (!(jArray.get(i).getClass().getName()
								.equals("net.sf.json.JSONObject")))) {
					String extPath = currentPath;
					if (resultTable.get(extPath) == null) {
						Map<String, String> newCol = new LinkedHashMap<String, String>();
						resultTable.put(extPath, newCol);
					}
					Map<String, String> col = resultTable.get(extPath);
					col.put("" + rowIndex, jArray.get(i).toString());
					rowIndex += 1;
				} else {

					dim(resultTable, currentPath, jArray.get(i));
					rowIndex += 1;
				}

			}
		} else if (type.equals("net.sf.json.JSONObject")) {
			JSONObject jObject = (JSONObject) currentJSON;
			Iterator<String> keys = jObject.keys();
			String key = null;
			while (keys.hasNext()) {
				key = keys.next();

				Object object = jObject.get(key);
				String objectType = object.getClass().getName();
				String extPath = currentPath + ".." + key;
				
				
				if (objectType.equals("net.sf.json.JSONArray")
						|| objectType.equals("net.sf.json.JSONObject")) {
					dim(resultTable, extPath, object);
				} else {
					if (resultTable.get(extPath) == null) {
						Map<String, String> newCol = new LinkedHashMap<String, String>();
						resultTable.put(extPath, newCol);
					}
					Map<String, String> col = resultTable.get(extPath);
					if (object.toString() != null) {
						System.out.println("I am putting on row:"+rowIndex+",value:"+object.toString());
						col.put("" + rowIndex, object.toString());
					} else {
						// error
						System.out
						.println("ERROR: Wanted to add something to Row: "
								+ rowIndex
								+ ", Col: "
								+ extPath
								+ ", val:"
								+ object.getClass().getName());
					}
				}

			}

		}
	}

}

