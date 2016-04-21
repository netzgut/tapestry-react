var Babel = require("babel-core");
var react = require("babel-preset-react");
var es2015 = require("babel-preset-es2015");
var amd = require("babel-plugin-transform-es2015-modules-amd")

compileJSX = function(input, filename, outputamd, useColoredOutput, loadReactPreset) {
    try {
        var plugins = [];
        if (outputamd){
          plugins.push(amd);
        }
        var presets = [es2015];
        if (loadReactPreset){
          presets.push(react);
        }
        var config = {filename: filename,
                      compact: false,
                      ast: false,
                      presets: presets,
                      plugins: plugins,
                      highlightCode: useColoredOutput};
        return { output: Babel.transform(input, config).code };
    }
    catch (err) {
        return { exception: err.toString() };
    }
};