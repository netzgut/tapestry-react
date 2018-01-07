package de.eddyson.tapestry.react.isomorphic.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.FactoryDefaults;
import org.apache.tapestry5.ioc.services.SymbolProvider;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.javascript.JavaScriptModuleConfiguration;
import org.apache.tapestry5.services.javascript.ModuleManager;

import de.eddyson.tapestry.react.ReactSymbols;
import de.eddyson.tapestry.react.isomorphic.services.impl.ModuleLoaderFactoryImplementation;
import de.eddyson.tapestry.react.isomorphic.services.impl.NashornReactDomServerRenderEngine;

public class ReactIsomorphicModule {

  public static void bind(ServiceBinder binder) {
    binder.bind(ReactDomServerRenderEngine.class, NashornReactDomServerRenderEngine.class);
    binder.bind(ModuleLoaderFactory.class, ModuleLoaderFactoryImplementation.class);
  }

  @FactoryDefaults
  @Contribute(SymbolProvider.class)
  public static void setupDefaultConfiguration(final MappedConfiguration<String, Object> configuration) {
    configuration.add(ReactSymbols.REACT_DOM_SERVER_ASSET_PATH,
        "classpath:de/eddyson/tapestry/react/services/react-dom-server.browser.development.js");
    configuration.add(ReactSymbols.REACT_DOM_SERVER_ASSET_PATH_PRODUCTION,
        "classpath:de/eddyson/tapestry/react/services/react-dom-server.browser.production.min.js");
  }

  @Contribute(ModuleManager.class)
  public static void setupJSModules(final MappedConfiguration<String, JavaScriptModuleConfiguration> configuration,
      final AssetSource assetSource, @Symbol(SymbolConstants.PRODUCTION_MODE) final boolean productionMode,
      @Symbol(ReactSymbols.REACT_DOM_SERVER_ASSET_PATH) final String reactDomServerAssetPath,
      @Symbol(ReactSymbols.REACT_DOM_SERVER_ASSET_PATH_PRODUCTION) final String reactDomServerAssetPathProduction) {

    configuration.add("react-dom-server", new JavaScriptModuleConfiguration(
        assetSource.resourceForPath(productionMode ? reactDomServerAssetPathProduction : reactDomServerAssetPath)));
  }
}
