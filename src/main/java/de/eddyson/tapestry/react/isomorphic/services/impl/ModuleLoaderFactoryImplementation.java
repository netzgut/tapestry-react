package de.eddyson.tapestry.react.isomorphic.services.impl;

import javax.script.ScriptEngine;

import org.apache.tapestry5.internal.services.assets.ResourceChangeTracker;
import org.apache.tapestry5.services.assets.StreamableResourceSource;
import org.apache.tapestry5.services.javascript.ModuleManager;

import de.eddyson.tapestry.react.isomorphic.services.ModuleLoader;
import de.eddyson.tapestry.react.isomorphic.services.ModuleLoaderFactory;

/**
 * Default ModuleLoaderFactoryImplementation, builds instances of
 * {@link NashornModuleLoader}
 */
public class ModuleLoaderFactoryImplementation implements ModuleLoaderFactory {

  private final ModuleManager moduleManager;
  private final StreamableResourceSource srs;
  private final ResourceChangeTracker tracker;

  public ModuleLoaderFactoryImplementation(ModuleManager moduleManager, StreamableResourceSource srs,
      ResourceChangeTracker tracker) {
    super();
    this.moduleManager = moduleManager;
    this.srs = srs;
    this.tracker = tracker;
  }

  @Override
  public ModuleLoader build(ScriptEngine engine) {
    return new NashornModuleLoader(this.moduleManager, this.srs, this.tracker, engine);
  }

}