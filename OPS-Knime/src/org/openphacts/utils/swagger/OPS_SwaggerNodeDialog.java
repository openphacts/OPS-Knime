package org.openphacts.utils.swagger;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettings;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentButton;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;


/**
 * <code>NodeDialog</code> for the "OPS_Swagger" Node.
 * This node loads a swagger web service description file and lets the user select on of the services via the config panel together with setting default parameters. An input table can be added with more parameters and values. The result of executing the node is a URL with the service call. 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Ronald Siebes - VU Amsterdam
 */
public class OPS_SwaggerNodeDialog extends DefaultNodeSettingsPane {

	/**
	 * New pane for configuring OPS_Swagger node dialog. This is just a suggestion
	 * to demonstrate possible default dialog components.
	 */
	protected final SettingsModelString templateSelection = new SettingsModelString(
			OPS_SwaggerNodeModel.TEMPLATE_SELECTION,
			OPS_SwaggerNodeModel.TEMPLATE_SELECTION_DEFAULT);
	protected final SettingsModelString templateSelectionCopy = new SettingsModelString(
			OPS_SwaggerNodeModel.TEMPLATE_SELECTION_COPY,
			OPS_SwaggerNodeModel.TEMPLATE_SELECTION_COPY_DEFAULT);

	protected final SettingsModelString swaggerUrl = new SettingsModelString(
			OPS_SwaggerNodeModel.SWAGGER_URL, OPS_SwaggerNodeModel.SWAGGER_URL_DEFAULT);

	protected final SettingsModelString resultUrl = new SettingsModelString(
			OPS_SwaggerNodeModel.RESULT_URL, OPS_SwaggerNodeModel.RESULT_URL_DEFAULT);
	 protected final SettingsModelStringArray paramValuesModel= new SettingsModelStringArray("paramValues", OPS_SwaggerNodeModel.paramValues); 

	protected final SettingsModelString currentSwagger = new SettingsModelString(
			OPS_SwaggerNodeModel.CURRENT_SWAGGER, OPS_SwaggerNodeModel.CURRENT_SWAGGER_DEFAULT);
	protected NodeLogger logger;
	private String defaultAppID = "15a18100";
	private String defaultAppKey = "528a8272f1cd961d215f318a0315dd3d";
	private LinkedHashMap<String, String> templates = new LinkedHashMap<String, String>();
	private LinkedHashMap<String,LinkedHashMap<String,SettingsModelString>> _settings = new LinkedHashMap<String,LinkedHashMap<String,SettingsModelString>>();
	private LinkedHashMap<String,LinkedHashMap<String,String>> tooltips = new LinkedHashMap<String,LinkedHashMap<String,String>>();
	private String defaultPath = "";
	private String currentTemplateKey="";
	ArrayList<String> ret = new ArrayList<String>();

	DialogComponentStringSelection templateDialog = null;
	DialogComponentString urlDialog = null;
	DialogComponentString resultDialog = null;
	DialogComponentString swaggerDialog = null;
	DialogComponentString templateCopyDialog = null;
	protected OPS_SwaggerNodeDialog() {
		super();
		resultUrl.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				//resultUrl.setStringValue(getDefaultUrl());
				System.out.println("change:"+resultUrl.getStringValue());
			}
			
		});
		logger = NodeLogger.getLogger(getClass());
		
		ret.add(" ");
		templateCopyDialog = new DialogComponentString(templateSelectionCopy,"");
		templateCopyDialog.setSizeComponents(0, 0);
		addDialogComponent(templateCopyDialog);
	
		templateDialog = new DialogComponentStringSelection(templateSelection,
				"Select service", ret);
		templateDialog.getModel().setEnabled(false);
		templateDialog.setSizeComponents(500, 100);
		this.setHorizontalPlacement(true);
		
		urlDialog = new DialogComponentString(swaggerUrl,
				"Swagger URL: ");
		swaggerDialog = new DialogComponentString(currentSwagger,
				"");
		swaggerDialog.setSizeComponents(0, 0);
		addDialogComponent(swaggerDialog);
		
		urlDialog.setSizeComponents(700, 40);
		urlDialog.getComponentPanel().setEnabled(false);
		//urlDialog.setSizeComponents(500, 100);
		addDialogComponent(urlDialog);
		
		
		resultDialog = new DialogComponentString(resultUrl,
				"");
		resultDialog.setSizeComponents(700, 40);
		resultDialog.getComponentPanel().setSize(700, 40);
		resultDialog.setSizeComponents(0, 0);
		final DialogComponentButton loadSwagger = new DialogComponentButton(
				"Fetch") {

		};
		addDialogComponent(loadSwagger);
		this.setHorizontalPlacement(false);
		addDialogComponent(resultDialog);
		
		addDialogComponent(templateDialog);
		templateDialog.getModel().setEnabled(false);
		
		templateDialog.getModel().addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				
				if(getTab("parameters") != null){
					 removeTab("parameters");
				 }
				 addTab("parameters", createTab(_settings.get(templateSelection.getStringValue()),tooltips.get(templateSelection.getStringValue())));
				 getTab("parameters").addFocusListener(new FocusListener(){

					@Override
					public void focusGained(FocusEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void focusLost(FocusEvent arg0) {
						
						// TODO Auto-generated method stub
						
					}
					 
				 });
				 currentTemplateKey = templateSelection.getStringValue();
				 resultUrl.setStringValue(getDefaultUrl());
				 templateSelectionCopy.setStringValue(templateSelection.getStringValue());
				// templateSelection.setStringValue(selectedTemplateString);
				 System.out.println("resulturl:"+resultUrl.getStringValue());
				
			}
			
		});
		
		loadSwagger.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JSONObject swaggerJSON = new JSONObject();
				try {
					swaggerJSON = grabSomeJson(buildRequestURL(swaggerUrl
							.getStringValue()));
					ret = getTemplates(swaggerJSON,true);
					
					resultUrl.setStringValue(getDefaultUrl());
					templateDialog.replaceListItems(ret, null);
					templateDialog.getModel().setEnabled(true);

				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// bla.
				// TODO Auto-generated method stub

			}
		});
		

	}


	boolean neverOpened = true;
	String selectedTemplateString= "";
	public void onOpen() {
		
		// TODO Auto-generated method stub
		super.onOpen();
		if(neverOpened){
			JSONObject swaggerJSON;
			try{
			 swaggerJSON  = (JSONObject) JSONSerializer.toJSON(currentSwagger.getStringValue());
			}catch (JSONException e){
				neverOpened = false;
				return;
			}
			if(swaggerJSON!=null){
				String savedSettings = resultUrl.getStringValue();
				System.out.println("before:"+ resultUrl.getStringValue());
				
				System.out.println("before_template"+templateSelectionCopy.getStringValue());
				selectedTemplateString = templateSelectionCopy.getStringValue();
				
				ret = getTemplates(swaggerJSON,false);
			
				
				//resultUrl.setStringValue(getDefaultUrl());
				//
				//templateDialog.
				System.out.println("after:"+ resultUrl.getStringValue());
				
				templateDialog.replaceListItems(ret, null);
				templateDialog.getModel().setEnabled(true);
			}
			templateSelection.setStringValue(selectedTemplateString);
		}
		neverOpened=false;
		//
		
	}
	protected URL buildRequestURL(String c_uri) throws URISyntaxException,
			MalformedURLException, UnsupportedEncodingException {

		URI uri = new URI(c_uri);
		return uri.toURL();
	}

	
	protected ArrayList<String> getTemplates(JSONObject swaggerJSON, boolean override) {
		ArrayList<String> results = new ArrayList<String>();

		String basePath = swaggerJSON.getString("basePath");
		if (basePath.endsWith("/")) {
			basePath = basePath.substring(0, basePath.length() - 1);
		}

		JSONArray servicesJSON = swaggerJSON.getJSONArray("apis");
		JSONObject currentService = null;

		if(!override){
			
			System.out.println("deze parsen:"+templateSelectionCopy.getStringValue());
			
		}
		for (int i = 0; i < servicesJSON.size(); i++) {
			currentService = servicesJSON.getJSONObject(i);
			if (currentService.has("path")) {
				String path = currentService.getString("path");
				JSONArray operations = currentService
						.getJSONArray("operations");
				

				for (int j = 0; j < operations.size(); j++) {
					String urlTemplate = basePath + path + "?";
					defaultPath=urlTemplate.toString();
					
					JSONObject operation = operations.getJSONObject(j);
					String summary = operation.getString("summary");
					String description = summary+"  ("+path+")";
					currentTemplateKey = description;
					JSONArray parameters = operation.getJSONArray("parameters");
					LinkedHashMap<String,SettingsModelString> operationMap = new LinkedHashMap<String,SettingsModelString>();
					LinkedHashMap<String,String> tooltipMap = new LinkedHashMap<String,String>();
					_settings.put(description, operationMap);
					tooltips.put(description, tooltipMap);
					operationMap.put("path", new SettingsModelString("path",defaultPath));
					if(!override){
						System.out.println("currentTemplatekey="+currentTemplateKey+", currentDescription="+description);
					}
					for (int k = 0; k < parameters.size(); k++) {
						JSONObject parameter = parameters.getJSONObject(k);
					    
						if (parameter.has("name")) {
							if(!override && templateSelectionCopy.getStringValue().equals(description)){
								LinkedHashMap<String,String>savedVals = getSavedParams();
								if(savedVals.get(parameter.getString("name"))==null){
									savedVals.put(parameter.getString("name"), "");
								}
								operationMap.put(parameter.getString("name"),new SettingsModelString(currentTemplateKey+parameter.getString("name"),savedVals.get(parameter.getString("name"))));
								System.out.println("parameter.getString(name)="+parameter.getString("name")+", "+savedVals.get(parameter.getString("name")));
							}else{
								if(parameter.getString("name").equals("app_id")){
									operationMap.put(parameter.getString("name"),new SettingsModelString(currentTemplateKey+parameter.getString("name"),defaultAppID));
								}else if(parameter.getString("name").equals("app_key")){
									operationMap.put(parameter.getString("name"),new SettingsModelString(currentTemplateKey+parameter.getString("name"),defaultAppKey));
	
								}else{
									operationMap.put(parameter.getString("name"),new SettingsModelString(currentTemplateKey+parameter.getString("name"),""));
									
									
								}
							}
							//}else{
						//		operationMap.put(parameter.getString("name"),new SettingsModelString(parameter.getString("name"),""));
								
							//}
							tooltipMap.put(parameter.getString("name"),parameter.getString("description"));
							
							System.out.println(parameter.getString("name"));
							/*
							if (k + 2 <= parameters.size()) {
								urlTemplate += "&]";
							} else {
								urlTemplate += "]";
							}
							*/
							
						}
					}
					System.out.println("hier1");
					results.add(description);
					System.out.println("hier2");
					templates.put(description, urlTemplate);
					System.out.println("hier3");
					
					
					//System.out.println(urlTemplate);
				}
				if(override){
					System.out.println("hier4");
					currentTemplateKey = templates.keySet().iterator().next().toString();//tricky?
					System.out.println("hier5");
				}
				System.out.println("hier6");
				if(neverOpened && currentTemplateKey.equals(templateSelectionCopy.getStringValue())){
					resultUrl.setStringValue(getDefaultUrl());
					System.out.println("hier7");
					System.out.println("a new result url: "+ resultUrl.getStringValue());
				}else if(!neverOpened){
					resultUrl.setStringValue(getDefaultUrl());
					System.out.println("hier15");
					System.out.println("a new result url: "+ resultUrl.getStringValue());
				}
			}
		}
		return results;
	}

	private String getDefaultUrl(){
		//System.out.println("called getDefaultURL:"+ currentTemplateKey);
		
		System.out.println("hier8");
		Iterator<String> paramsIt = _settings.get(currentTemplateKey).keySet().iterator();
		System.out.println("hier9");
		String result =_settings.get(currentTemplateKey).get("path").getStringValue();//we assume there is always a path being set
		System.out.println("hier10");
		while (paramsIt.hasNext()){
			
			String param = paramsIt.next();
			
			if(!param.equals("path")){
				System.out.println("working on: "+currentTemplateKey+","+param);
				//operationMap.get(parameter.getString("name"),new SettingsModelString(parameter.getString("name"),""));
				
				//String value = params.get(currentTemplateKey).get(param);
				SettingsModelString settingsModel = _settings.get(currentTemplateKey).get(param);
				System.out.println("hier12");
				if(!settingsModel.getStringValue().equals("")){
					System.out.println("hier13");
						try {
							
							result += "["+param+"="+URLEncoder.encode(settingsModel.getStringValue(),"UTF-8")+"&]";
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							showException(e);
						}
						//System.out.println("NOT EMPTY PARAM: "+ param+", value ="+result);
				}
			}
			
		}
		if(result.endsWith("&]")){
			result= result.substring(0,result.length()-2)+"]";
		}
		/*
		if(parameter
				.getString("name").equals("")){
				System.out.println("EMPTY IN CONFIG: "+parameter.getString("name")+", so we don't add it to the uri (yet)");
				
			} else {
				operationMap.put(parameter.getString("name"),new SettingsModelString(parameter.getString("name"),operationMap.get(parameter
						.getString("name")).getStringValue()));
			}
			*/
		System.out.println("hier11");
		return result;
		
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

		// System.out.println(str);
		currentSwagger.setStringValue(str);
		
		JSONObject jo = (JSONObject) JSONSerializer.toJSON(str);

		return jo;

	}
	
    protected void showException(Throwable throwable)
    {
        logger.error("Exception", throwable);
        JOptionPane.showMessageDialog(getPanel(), throwable.toString(), "Error",
                JOptionPane.ERROR_MESSAGE);
    }
    
    
    protected <T extends JComponent> T addField(JPanel panel, String label, T field)
    {
        panel.add(new JLabel(label ), createFirst());
        panel.add(field, createLast());
        return field;
    }

    protected GridBagConstraints createLast()
    {
        GridBagConstraints last = createFirst();
        last.gridwidth = GridBagConstraints.REMAINDER;
        return last;
    }

    protected GridBagConstraints createFirst()
    {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(2, 3, 2, 3);
        return constraints;
    }
    
    private LinkedHashMap getSavedParams(){
    	LinkedHashMap<String,String> result = new LinkedHashMap<String,String>();
    	//LinkedHashMap<String,SettingsModelString> operations = _settings.get(currentTemplateKey);
    	System.out.println("here we do it:"+resultUrl.getStringValue());
    	String url = resultUrl.getStringValue();
    	String[] urlParts = url.split("\\?");
    	if(urlParts.length==2){
    		System.out.println("part2:"+urlParts[1]);
    		String relevantPart = urlParts[1];
    		//remove first and last bracket
    		relevantPart = relevantPart.substring(1,relevantPart.length()-1);
    		System.out.println("without brackets:"+relevantPart);
    		if(relevantPart.indexOf("][")>-1){//we have more elements
	    		String[] elements = relevantPart.split("\\]\\[");
	    		for(int i=0;i<elements.length;i++){
	    			String[] keyValuePair = elements[i].split("=");
	    			if(keyValuePair.length==2){
	    				//if the value is not the last one in the array, it will have an extra & symbol at the end, which we need to remove
	    				if(i<elements.length-1){
	    					keyValuePair[1] = keyValuePair[1].substring(0,keyValuePair[1].length()-1);
	    				}
	    				result.put(keyValuePair[0], keyValuePair[1]);
	    			//	System.out.println("we set this parameter: key="+keyValuePair[0]+", value="+keyValuePair[1]);
	    		
	    			}
	    		}
	    			
    		}else{//we only have one pair
    			String[] keyValuePair = urlParts[1].split("=");
    			if(keyValuePair.length==2){
    				//System.out.println("we set this single parameter: key="+keyValuePair[0]+", value="+keyValuePair[1]);
    				result.put(keyValuePair[0], keyValuePair[1]);
    			}
    		}
    		
    	}
    	return result;
    }
    public void onClose(){
    	
    	//remove first and last bracket
    	
    	//
    	resultUrl.setStringValue(getDefaultUrl());
    	//System.out.println("just closed "+ getDefaultUrl());
    
    	System.out.println("just closed "+ resultUrl.getStringValue());
    	
    }
    
    
    private Component createTab(LinkedHashMap<String,SettingsModelString> operations,LinkedHashMap<String,String>tooltips)
    {
    	
    
	
    	
    	
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(600, 450));
        JPanel connectionPanel = new JPanel(new GridBagLayout());
        connectionPanel.setBorder(BorderFactory.createTitledBorder("Default URL parameters"));
        
    	Iterator<String> operationsIt = operations.keySet().iterator();
    	final JTextField dummyField = new JTextField(1);
		while (operationsIt.hasNext()){
			//getTab("parameters").
			String operation = operationsIt.next();
			if(!operation.equals("path")){
				 final SettingsModelString current = operations.get(operation);
				final JTextField field = new JTextField(20);
				field.setToolTipText(tooltips.get(operation));
				field.setText(operations.get(operation).getStringValue());
				
				addField(connectionPanel, operation, field);
				field.addMouseMotionListener(new MouseMotionListener(){

					@Override
					public void mouseDragged(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mouseMoved(MouseEvent arg0) {
						// TODO Auto-generated method stub
						if(arg0.getComponent()==field &&field.isFocusOwner()){
						//System.out.println("mouse moved");
						}
					}
					
				});
				field.addKeyListener(new KeyListener(){

					@Override
					public void keyPressed(KeyEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void keyReleased(KeyEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void keyTyped(KeyEvent arg0) {
						// TODO Auto-generated method stub
						dummyField.grabFocus();
						field.grabFocus();
						
						System.out.println("typed");
						field.select(field.getText().length()-1, field.getText().length());
						int end = field.getSelectionEnd();
						field.setSelectionStart(end);
						field.setSelectionEnd(end);System.out.println("lost focus:"+resultUrl.getStringValue());
					}
					
				});
				field.addFocusListener(new FocusListener(){
	
					@Override
					public void focusGained(FocusEvent arg0) {
						// TODO Auto-generated method stub
						
					}
	
					@Override
					public void focusLost(FocusEvent arg0) {
						//JTextField lostFocus = (JTextField)arg0.getComponent();
						
						current.setStringValue(field.getText());
						resultUrl.setStringValue(getDefaultUrl());
						//int end = field.getSelectionEnd();
						//field.setSelectionStart(end);
						//field.setSelectionEnd(end);
						//System.out.println("lost focus:"+resultUrl.getStringValue());
						//field.select(field.getText().length(), field.getText().length());
						// TODO Auto-generated method stub
						
					}
					
					
				});
			}
			
			//addField(connectionPanel, "", dummyField);
			
			
			//addDialogComponent(operations.get(operationsIt.next()));
		}
        
       // urlField = addField(connectionPanel, "openBIS URL", new JTextField(20));
        
       // panel.add(connectionPanel, BorderLayout.NORTH);
        

        JScrollPane scrollPane = new JScrollPane(connectionPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        return scrollPane;
    }
    
  
  
}
