package de.eddyson.tapestry.react.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@SupportsInformalParameters
public class ReactComponent implements ClientElement {

  @Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
  private String module;

  @Parameter(allowNull = false)
  private String elementName = "div";

  @Inject
  private JavaScriptSupport javaScriptSupport;

  @Inject
  private ComponentResources componentResources;

  private String clientId;

  void setupRender() {
    clientId = javaScriptSupport.allocateClientId(componentResources);
  }

  boolean beginRender(final MarkupWriter writer) {
    writer.element(elementName, "id", clientId);
    return false;
  }

  void afterRender(final MarkupWriter writer) {
    writer.end();
    JSONObject parameters = new JSONObject();
    for (String informalParameterName : componentResources.getInformalParameterNames()) {
      parameters.put(informalParameterName,
          componentResources.getInformalParameter(informalParameterName, Object.class));
    }
    javaScriptSupport.require("eddyson/react/components/reactcomponent").with(module, clientId, parameters);
  }

  @Override
  public String getClientId() {
    return clientId;
  }
}
