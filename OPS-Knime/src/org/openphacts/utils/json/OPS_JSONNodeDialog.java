package org.openphacts.utils.json;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
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
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettings;
import org.knime.core.node.config.base.ConfigBase;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentButton;
import org.knime.core.node.defaultnodesettings.DialogComponentLabel;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
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
	private static Map<Object, Map<String, Set<Object>>> jsonSet = null;
	private static String jsonKey = null;
	private static Object currentJsonKey = null;
	Collection<String> optionsArrayDummy;//{"niks"};
	Collection<String> optionNamesArrayDummy;//{"niks"};
	private String[] optionsArray = {"niks"};
	// the settingsmodels that persistently store things, even after knime shuts down properly
	private final SettingsModelString json_config_url = new SettingsModelString(
			OPS_JSONNodeModel.JSON_URL, OPS_JSONNodeModel.DEFAULT_JSON_URL);
	private final SettingsModelStringArray selection_parameters = new SettingsModelStringArray(OPS_JSONNodeModel.SELECTION_PARAMETERS, OPS_JSONNodeModel.DEFAULT_SELECTION_PARAMETERS);
	private final SettingsModelStringArray selection_customized_names = new SettingsModelStringArray(OPS_JSONNodeModel.SELECTION_CUSTOMIZED_NAMES, OPS_JSONNodeModel.DEFAULT_SELECTION_CUSTOMIZED_NAMES);
    DialogComponentStringListSelection var_sel = null;
    DialogComponentStringListSelection name_sel = null;
	JScrollPane scrollPane;
	DialogComponentString json_config_urlDialog = null;
	JPanel optionPanel;
	
    protected OPS_JSONNodeDialog() {
    	super();
    	optionsArrayDummy = new Vector<String>();
    	var_sel = new DialogComponentStringListSelection(selection_parameters,"var_sel",optionsArrayDummy,1,false,0);
    	optionNamesArrayDummy = new Vector<String>();
    	name_sel = new DialogComponentStringListSelection(selection_customized_names,"var_sel",optionNamesArrayDummy,1,false,0);
    	addDialogComponent(var_sel);
    	addDialogComponent(name_sel);
    	final JScrollPane scrollPane;
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
        
        
        DialogComponentButton fetchExampleButton = new DialogComponentButton(
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
				
				 
				JSONObject exampleJSON = new JSONObject();
				
				try {
					exampleJSON = grabSomeJson(buildRequestURL(json_config_url
							.getStringValue()));
					
					Vector<String> jsonOptions = getJSONOptions(exampleJSON);
					
					
					 optionsArray = new String[jsonOptions.size()];
					jsonOptions.copyInto(optionsArray);
					selection_parameters.setStringArrayValue(optionsArray);
					var_sel.replaceListItems(jsonOptions, optionsArray);
					
					String[] optionNamesArray = new String[jsonOptions.size()];
					jsonOptions.copyInto(optionNamesArray);
					selection_customized_names.setStringArrayValue(optionNamesArray);
					
					Iterator<String> jsonOptionsIt = jsonOptions.iterator();
					int index = 0;
					
				
				    
				   final Vector<SettingsModelOptionalString> userPrefs = new Vector<SettingsModelOptionalString>();  
				   
					while(jsonOptionsIt.hasNext()){
						
						
						String jsonOption = jsonOptionsIt.next();
						//selection_parameters.
						final SettingsModelOptionalString model = new SettingsModelOptionalString(jsonOption,jsonOption,false);
						DialogComponentOptionalString dm1 = new DialogComponentOptionalString(model, jsonOption);
						userPrefs.add( model);
						model.setEnabled(true);
						model.setIsActive(true);
						model.setIsActive(false);
						
						optionPanel.add(dm1.getComponentPanel());
						
						model.addChangeListener(new ChangeListener(){

							@Override
							public void stateChanged(ChangeEvent e) {
								
								System.out.println("model"+model.getStringValue());
								//model.setIsActive(false);
								// TODO Auto-generated method stub
								
							}
							
							
						});
						dm1.getComponentPanel().addFocusListener(new FocusListener(){

							@Override
							public void focusGained(FocusEvent arg0) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void focusLost(FocusEvent arg0) {
								
							
							}
							
							
						});
						dm1.getComponentPanel().setVerifyInputWhenFocusTarget(false);
						dm1.getComponentPanel().addKeyListener(new KeyListener() {
							
							@Override
							public void keyTyped(KeyEvent arg0) {
								// TODO Auto-generated method stub
								java.awt.Robot r;
								try {
									System.out.println("pressed key");
									if(arg0.getKeyCode()!=13){
									r = new java.awt.Robot();
									r.keyPress(13);
									}
								} catch (AWTException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								
							}
							
							@Override
							public void keyReleased(KeyEvent arg0) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void keyPressed(KeyEvent arg0) {
								// TODO Auto-generated method stub
								
							}
						});
						dm1.getComponentPanel().addMouseListener(new MouseListener(){

							@Override
							public void mouseClicked(MouseEvent arg0) {
								
							}

							@Override
							public void mouseEntered(MouseEvent arg0) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void mouseExited(MouseEvent arg0) {
								System.out.println(arg0.getComponent().getClass().getName());
								Iterator<SettingsModelOptionalString> prefIt = userPrefs.iterator();
								final Vector<String> userVars = new Vector<String>();
								final Vector<String> userVarNames = new Vector<String>();
								while(prefIt.hasNext()){
									SettingsModelOptionalString curPref = prefIt.next();
									if(curPref.isActive()){
										
										userVars.add(curPref.getKey());
										userVarNames.add(curPref.getStringValue());
										System.out.println("woot"+curPref.getKey()+",.."+ model.getStringValue());
									}
								}
								String[] userVarsArray = new String[userVars.size()];
								String[] userVarNamesArray = new String[userVarNames.size()];
								userVars.copyInto(userVarsArray);
								userVarNames.copyInto(userVarNamesArray);
								selection_parameters.setStringArrayValue(userVarsArray);
								selection_customized_names.setStringArrayValue(userVarNamesArray);
								if(userVarsArray.length!=0){
									var_sel.replaceListItems(userVars, userVarsArray);
									name_sel.replaceListItems(userVarNames, userVarNamesArray);
								}
								
							}

							@Override
							public void mousePressed(MouseEvent arg0) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void mouseReleased(MouseEvent arg0) {
								// TODO Auto-generated method stub
								
							}
							
							
							
						});
					
						
					}
					
					
				       // getPanel().add(scrollPane);
				        getPanel().updateUI();
				       
					//ret = getTemplates(swaggerJSON);
					//resultUrl.setStringValue(getDefaultUrl());
					//templateDialog.replaceListItems(ret, null);
					//templateDialog.getModel().setEnabled(true);

				} catch (MalformedURLException e) {
					showException(e);

				} catch (UnsupportedEncodingException e) {
					showException(e);
				} catch (IOException e) {
					showException(e);
				} catch (URISyntaxException e) {
					showException(e);
				}


			}
		});
        
                    
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
				try {
					if (jsonKey != null) {
						if (jsonKey.equals(extPath)) {

							Map<String, Set<Object>> newMap = new LinkedHashMap<String, Set<Object>>();
							jsonSet.put(object, newMap);
							currentJsonKey = object;
						} 
						/*else if (paramSet.containsKey(extPath)) {
							if (!jsonSet.get(currentJsonKey).containsKey(
									extPath)) {
								Set<Object> agrObjects = new LinkedHashSet<Object>();
								jsonSet.get(currentJsonKey).put(extPath,
										agrObjects);
							}
							jsonSet.get(currentJsonKey).get(extPath)
							.add(object);

						}*/
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
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

