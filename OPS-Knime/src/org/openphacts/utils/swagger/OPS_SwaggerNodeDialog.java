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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.knime.core.node.NodeLogger;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentButton;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;


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
	private final SettingsModelString templateSelection = new SettingsModelString(
			OPS_SwaggerNodeModel.TEMPLATE_SELECTION,
			OPS_SwaggerNodeModel.TEMPLATE_SELECTION_DEFAULT);

	private final SettingsModelString swaggerUrl = new SettingsModelString(
			OPS_SwaggerNodeModel.SWAGGER_URL, OPS_SwaggerNodeModel.SWAGGER_URL_DEFAULT);

	private final SettingsModelString resultUrl = new SettingsModelString(
			OPS_SwaggerNodeModel.RESULT_URL, OPS_SwaggerNodeModel.RESULT_URL_DEFAULT);


	protected NodeLogger logger;
	private String defaultAppID = "15a18100";
	private String defaultAppKey = "528a8272f1cd961d215f318a0315dd3d";
	private LinkedHashMap<String, String> templates = new LinkedHashMap<String, String>();
	private LinkedHashMap<String,LinkedHashMap<String,SettingsModelString>> settings = new LinkedHashMap<String,LinkedHashMap<String,SettingsModelString>>();
	private LinkedHashMap<String,LinkedHashMap<String,String>> tooltips = new LinkedHashMap<String,LinkedHashMap<String,String>>();
	private String defaultPath = "";
	private String currentTemplateKey="";
	ArrayList<String> ret = new ArrayList<String>();

	DialogComponentStringSelection templateDialog = null;
	DialogComponentString urlDialog = null;
	DialogComponentString resultDialog = null;
	protected OPS_SwaggerNodeDialog() {
		super();
		logger = NodeLogger.getLogger(getClass());
		ret.add(" ");
		
		templateDialog = new DialogComponentStringSelection(templateSelection,
				"Select service", ret);
		templateDialog.getModel().setEnabled(false);
		templateDialog.setSizeComponents(500, 100);
		this.setHorizontalPlacement(true);
		urlDialog = new DialogComponentString(swaggerUrl,
				"Swagger URL: ");
		urlDialog.setSizeComponents(700, 40);
		addDialogComponent(urlDialog);
		
		resultDialog = new DialogComponentString(resultUrl,
				"Result URL: ");
		resultDialog.setSizeComponents(700, 40);
		
		final DialogComponentButton loadSwagger = new DialogComponentButton(
				"Fetch") {

		};
		addDialogComponent(loadSwagger);
		addDialogComponent(resultDialog);
		this.setHorizontalPlacement(false);
		addDialogComponent(templateDialog);
		templateDialog.getModel().setEnabled(false);
		
		templateDialog.getModel().addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				if(getTab("parameters") != null){
					 removeTab("parameters");
				 }
				 addTab("parameters", createTab(settings.get(templateSelection.getStringValue()),tooltips.get(templateSelection.getStringValue())));
				 currentTemplateKey = templateSelection.getStringValue();
				 resultUrl.setStringValue(getDefaultUrl());
				
				// System.out.println("resulturl:"+resultUrl.getStringValue());
				
			}
			
		});
		loadSwagger.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JSONObject swaggerJSON = new JSONObject();
				try {
					swaggerJSON = grabSomeJson(buildRequestURL(swaggerUrl
							.getStringValue()));
					ret = getTemplates(swaggerJSON);
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

	protected URL buildRequestURL(String c_uri) throws URISyntaxException,
			MalformedURLException, UnsupportedEncodingException {

		URI uri = new URI(c_uri);
		return uri.toURL();
	}

	
	protected ArrayList<String> getTemplates(JSONObject swaggerJSON) {
		ArrayList<String> results = new ArrayList<String>();

		String basePath = swaggerJSON.getString("basePath");
		if (basePath.endsWith("/")) {
			basePath = basePath.substring(0, basePath.length() - 1);
		}

		JSONArray servicesJSON = swaggerJSON.getJSONArray("apis");
		JSONObject currentService = null;

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
					JSONArray parameters = operation.getJSONArray("parameters");
					LinkedHashMap<String,SettingsModelString> operationMap = new LinkedHashMap<String,SettingsModelString>();
					LinkedHashMap<String,String> tooltipMap = new LinkedHashMap<String,String>();
					settings.put(description, operationMap);
					tooltips.put(description, tooltipMap);
					operationMap.put("path", new SettingsModelString("path",defaultPath));
					for (int k = 0; k < parameters.size(); k++) {
						JSONObject parameter = parameters.getJSONObject(k);
						if (parameter.has("name")) {
							if(parameter.getString("name").equals("app_id")){
								operationMap.put(parameter.getString("name"),new SettingsModelString(parameter.getString("name"),defaultAppID));
							}else if(parameter.getString("name").equals("app_key")){
								operationMap.put(parameter.getString("name"),new SettingsModelString(parameter.getString("name"),defaultAppKey));

							}else{
								operationMap.put(parameter.getString("name"),new SettingsModelString(parameter.getString("name"),""));
								
								
							}
							tooltipMap.put(parameter.getString("name"),parameter.getString("description"));
							
							
							/*
							if (k + 2 <= parameters.size()) {
								urlTemplate += "&]";
							} else {
								urlTemplate += "]";
							}
							*/
							
						}
					}
					
					results.add(description);
					templates.put(description, urlTemplate);
					
					
					//System.out.println(urlTemplate);
				}
				currentTemplateKey = templates.keySet().iterator().next().toString();//tricky?
				resultUrl.setStringValue(getDefaultUrl());
				//System.out.println("a new result url: "+ resultUrl.getStringValue());
			}
		}
		return results;
	}

	private String getDefaultUrl(){
		//System.out.println("called getDefaultURL:"+ currentTemplateKey);
		
		
		Iterator<String> paramsIt = settings.get(currentTemplateKey).keySet().iterator();
		
		String result =settings.get(currentTemplateKey).get("path").getStringValue();//we assume there is always a path being set
		
		while (paramsIt.hasNext()){
			
			String param = paramsIt.next();
			if(!param.equals("path")){
				//System.out.println("working on: "+param);
				//operationMap.get(parameter.getString("name"),new SettingsModelString(parameter.getString("name"),""));
				
				//String value = params.get(currentTemplateKey).get(param);
				SettingsModelString settingsModel = settings.get(currentTemplateKey).get(param);
				if(!settingsModel.getStringValue().equals("")){
						result += "["+settingsModel.getKey()+"="+settingsModel.getStringValue()+"&]";
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
        panel.add(new JLabel(label + ":"), createFirst());
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
    
    private Component createTab(LinkedHashMap<String,SettingsModelString> operations,LinkedHashMap<String,String>tooltips)
    {
    	
    
	
    	
    	
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(600, 450));
        JPanel connectionPanel = new JPanel(new GridBagLayout());
        connectionPanel.setBorder(BorderFactory.createTitledBorder("Connection Parameters"));
        
    	Iterator<String> operationsIt = operations.keySet().iterator();
    	
		while (operationsIt.hasNext()){
			//getTab("parameters").
			String operation = operationsIt.next();
			if(!operation.equals("path")){
				 final SettingsModelString current = operations.get(operation);
				final JTextField field = new JTextField(20);
				field.setToolTipText(tooltips.get(operation));
				field.setText(operations.get(operation).getStringValue());
				addField(connectionPanel, operation, field);
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
						// TODO Auto-generated method stub
						
					}
					
					
				});
			}
			//addDialogComponent(operations.get(operationsIt.next()));
		}
        
       // urlField = addField(connectionPanel, "openBIS URL", new JTextField(20));
        
        panel.add(connectionPanel, BorderLayout.NORTH);
        

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }
}
