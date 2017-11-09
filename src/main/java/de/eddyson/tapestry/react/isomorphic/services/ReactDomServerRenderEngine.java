package de.eddyson.tapestry.react.isomorphic.services;

import org.apache.tapestry5.json.JSONObject;

public interface ReactDomServerRenderEngine {

  /**
   * Renders a react component to a string on the server
   * 
   * @param moduleName
   *          the module name of the component
   * @param parameters
   *          informal parameters
   * @return string representation of component
   */
  String renderToString(String moduleName, JSONObject parameters);
}
