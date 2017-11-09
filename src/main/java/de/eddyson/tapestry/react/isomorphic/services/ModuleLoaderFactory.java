package de.eddyson.tapestry.react.isomorphic.services;

import javax.script.ScriptEngine;

/**
 * ModuleLoaderFactory is responsible for building a ModuleLoader for a
 * ScriptEngine instance
 */
public interface ModuleLoaderFactory {

  ModuleLoader build(ScriptEngine engine);

}