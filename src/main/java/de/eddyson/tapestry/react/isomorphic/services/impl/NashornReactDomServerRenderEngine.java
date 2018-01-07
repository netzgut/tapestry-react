package de.eddyson.tapestry.react.isomorphic.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.io.IOUtils;
import org.apache.tapestry5.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.eddyson.tapestry.react.isomorphic.services.ModuleLoaderFactory;
import de.eddyson.tapestry.react.isomorphic.services.ReactDomServerRenderEngine;

/**
 * Nashorn base ReactDomServerRenderEngine
 */
public class NashornReactDomServerRenderEngine implements ReactDomServerRenderEngine {

  private static final Logger log = LoggerFactory.getLogger(NashornReactDomServerRenderEngine.class);

  private ScriptEngine _engine;

  private final ModuleLoaderFactory moduleLoaderFactory;

  public NashornReactDomServerRenderEngine(ModuleLoaderFactory moduleLoaderFactory) {
    super();
    this.moduleLoaderFactory = moduleLoaderFactory;
  }

  @Override
  public String renderToString(String moduleName, JSONObject parameters) {
    try {
      ScriptEngine engine = getEngine();
      Object result = ((Invocable) engine).invokeFunction("TAPESTRY_REACT_RENDER", moduleName,
          parameters.toCompactString());
      if (result == null) {
        resetEngine();
        return null;
      }
      String string = result.toString();
      System.out.println("Code: " + string);
      return string;
    } catch (Throwable e) {
      resetEngine();
      throw new RuntimeException(e);
    }
  }

  // when an exception occurs while rendering react component discard the engine
  // require.js set's some internal timeout values for modules that could not be
  // loaded and throws an exception on next invocation of that module
  private void resetEngine() {
    this._engine = null;
  }

  private ScriptEngine getEngine() throws ScriptException, IOException {
    if (this._engine == null) {
      synchronized (this) {
        if (this._engine == null) {
          this._engine = buildEngine();
        }
      }
    }
    return this._engine;
  }

  private ScriptEngine buildEngine() throws ScriptException, IOException {

    ScriptEngine nashorn = new ScriptEngineManager().getEngineByName("nashorn");

    // "__tapestry" is available in JavaScript code to load AMD modules from
    // Tapestry's ModuleManager
    nashorn.getBindings(ScriptContext.ENGINE_SCOPE).put("__tapestry", this.moduleLoaderFactory.build(nashorn));

    // polyfill.js contains some basic polyfills
    nashorn.eval(read(classpathResource("de/eddyson/tapestry/react/services/isomorphic/polyfill.js")));

    // r.js is the server side implementaiton of require.js for
    // Nashorn/Rhino/Node
    // this is a patched version, see r.js-README.md
    nashorn.eval(read(classpathResource("de/eddyson/tapestry/react/services/isomorphic/r.js")));

    // r-config.js injects the tapestry module loading mechanism in the r.js
    // implementation
    nashorn.eval(read(classpathResource("de/eddyson/tapestry/react/services/isomorphic/r-config.js")));

    // run.js is a wrapper around react-dom-server to render html for components
    // on server
    nashorn.eval(read(classpathResource("de/eddyson/tapestry/react/services/isomorphic/run.js")));
    return nashorn;
  }

  private String read(InputStream resource) throws IOException {
    try {
      return IOUtils.toString(resource, Charset.forName("UTF-8"));
    } finally {
      IOUtils.closeQuietly(resource);
    }
  }

  private InputStream classpathResource(String resource) {
    return getClass().getClassLoader().getResourceAsStream(resource);
  }

}