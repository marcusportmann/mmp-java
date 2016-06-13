
# mmp-application-aurelia

## Getting started

Before you start, make sure you have a working [NodeJS](http://nodejs.org/) environment, preferably with NPM 3.

Ensure that the  **typings** and **jspm** utilities are installed globally by executing the following commands (this will require *root* or *Administrator* privileges):

```shell
npm install typings -g
npm install jspm -g
```

Create a new Gradle-based JEE web project.

Create the  **src/main/webapp/.gitignore** file with the following contents

```text
/jspm_packages
/node_modules
/typings
npm-debug.log
```
 
From the **src/main/webapp** folder, execute the following command:

```shell
jspm init -y
```




Execute the following command to install the Aurelia framework:

jspm install aurelia-framework 











































## Getting started

Before you start, make sure you have a working [NodeJS](http://nodejs.org/) environment, preferably with NPM 3.

Ensure that the  **typings** utility is installed globally by executing the following command (this will require *root* or *Administrator* privileges):

```shell
npm install typings -g
```

Create a new Gradle-based JEE web project.

Create the  **src/main/webapp/.gitignore** file with the following contents

```text
/node_modules
/typings
npm-debug.log
```
 
From the **src/main/webapp** folder, execute the following command:

```shell
npm init -y
```
 
Update the  **src/main/webapp/package.json** file as required e.g.

```json
  "name": "mmp-application-aurelia",
  "version": "1.0.0",
  "description": "",
  "main": "index.js",
  "scripts": {
    "test": "echo \"Error: no test specified\" && exit 1"
  },
  "author": "Marcus Portmann",
  "license": "Apache-2.0"
}
```
  
Change the **scripts** section of the **src/main/webapp/package.json** file to the following:
 
```javascript
  "scripts": {
    "dev": "webpack-dev-server --config webpack.config.js --hot --inline --progress --devtool eval",
    "build": "webpack --config webpack.config.js --progress --profile",
    "prod": "webpack -p --config webpack.prod.config.js --progress --devtool source-map"
  },
``` 

From the **src/main/webapp** folder, execute the following commands to install the required NPM packages:

```shell
npm install --save aurelia-bootstrapper-webpack
npm install --save aurelia-event-aggregator
npm install --save aurelia-fetch-client
npm install --save aurelia-framework
npm install --save aurelia-history
npm install --save aurelia-history-browser
npm install --save aurelia-loader
npm install --save aurelia-loader-webpack
npm install --save aurelia-logging
npm install --save aurelia-logging-console
npm install --save aurelia-metadata
npm install --save aurelia-pal
npm install --save aurelia-pal-browser
npm install --save aurelia-path
npm install --save aurelia-polyfills
npm install --save aurelia-route-recognizer
npm install --save aurelia-router
npm install --save aurelia-task-queue
npm install --save aurelia-templating
npm install --save aurelia-templating-binding
npm install --save aurelia-templating-resources
npm install --save aurelia-templating-router
npm install --save bootstrap
npm install --save font-awesome
npm install --save jquery
npm install --save jquery-countto
npm install --save jquery-placeholder
npm install --save jquery-scroll-lock
npm install --save jquery-slimscroll
npm install --save jquery.appear
npm install --save moment
``` 

From the **src/main/webapp** folder, execute the following commands to install the required development NPM packages:

```shell
npm install --save-dev aurelia-tools
npm install --save-dev aurelia-webpack-plugin
npm install --save-dev core-js
npm install --save-dev css-loader
npm install --save-dev file-loader
npm install --save-dev html-webpack-plugin
npm install --save-dev raw-loader
npm install --save-dev style-loader
npm install --save-dev ts-loader
npm install --save-dev ts-node
npm install --save-dev typescript
npm install --save-dev url-loader
npm install --save-dev webpack
npm install --save-dev webpack-dev-server
``` 
 
Create the  **src/main/webapp/tsconfig.json** file with the following contents

```text
{
  "compilerOptions": {
    "target": "es5",
    "module": "commonjs",
    "emitDecoratorMetadata": true,
    "experimentalDecorators": true,
    "sourceMap": true
  },
  "exclude": [
    "node_modules"
  ]
}
``` 
 
Create the  **src/main/webapp/webpack.config.js** file with the following contents

```text
/*eslint-disable no-var*/

var path = require('path');
var AureliaWebpackPlugin = require('aurelia-webpack-plugin');
var ProvidePlugin = require('webpack/lib/ProvidePlugin');

module.exports = {
  resolve: {
    extensions: ['', '.js', '.ts']
  },
  devServer: {
    host: 'localhost',
    port: 3000
  },
  entry: {
    main: [
      './src/main'
    ]
  },
  output: {
    path: path.join(__dirname, 'build'),
    filename: 'bundle.js'
  },
  plugins: [
    new AureliaWebpackPlugin(),
    new ProvidePlugin({
      $: 'jquery',
      jQuery: 'jquery',
      'window.jQuery': 'jquery' // this doesn't expose jQuery property for window, but expose it to every module
    })
  ],
  module: {
    loaders: [
      { test: /\.ts$/, loader: 'ts-loader' },
      { test: /\.css?$/, loader: 'style!css' },
      { test: /\.html$/, loader: 'raw' },
      { test: /\.(png|gif|jpg)$/, loader: 'url-loader?limit=8192' },
      { test: /\.woff2(\?v=[0-9]\.[0-9]\.[0-9])?$/, loader: 'url-loader?limit=10000&mimetype=application/font-woff2' },
      { test: /\.woff(\?v=[0-9]\.[0-9]\.[0-9])?$/, loader: 'url-loader?limit=10000&mimetype=application/font-woff' },
      { test: /\.(ttf|eot|svg)(\?v=[0-9]\.[0-9]\.[0-9])?$/, loader: 'file-loader' }
    ]
  }
};
``` 
 
From the **src/main/webapp** folder, execute the following commands to install the Typescript definitions:
 
```shell
typings install --global --save dt~jquery
typings install --global --save dt~es6-promise
typings install --global --save dt~es6-collections
typings install --global --save aurelia-binding=npm:aurelia-binding/dist/commonjs/aurelia-binding.d.ts
typings install --global --save aurelia-bootstrapper-webpack=npm:aurelia-bootstrapper-webpack/dist/commonjs/aurelia-bootstrapper-webpack.d.ts
typings install --global --save aurelia-dependency-injection=npm:aurelia-dependency-injection/dist/commonjs/aurelia-dependency-injection.d.ts
typings install --global --save aurelia-event-aggregator=npm:aurelia-event-aggregator/dist/commonjs/aurelia-event-aggregator.d.ts
typings install --global --save aurelia-fetch-client=npm:aurelia-fetch-client/dist/commonjs/aurelia-fetch-client.d.ts
typings install --global --save aurelia-framework=npm:aurelia-framework/dist/commonjs/aurelia-framework.d.ts
typings install --global --save aurelia-history=npm:aurelia-history/dist/commonjs/aurelia-history.d.ts
typings install --global --save aurelia-history-browser=npm:aurelia-history-browser/dist/commonjs/aurelia-history-browser.d.ts
typings install --global --save aurelia-loader=npm:aurelia-loader/dist/commonjs/aurelia-loader.d.ts
typings install --global --save aurelia-loader-webpack=npm:aurelia-loader-webpack/dist/commonjs/aurelia-loader-webpack.d.ts
typings install --global --save aurelia-logging=npm:aurelia-logging/dist/commonjs/aurelia-logging.d.ts
typings install --global --save aurelia-logging-console=npm:aurelia-logging-console/dist/commonjs/aurelia-logging-console.d.ts
typings install --global --save aurelia-metadata=npm:aurelia-metadata/dist/commonjs/aurelia-metadata.d.ts
typings install --global --save aurelia-pal=npm:aurelia-pal/dist/commonjs/aurelia-pal.d.ts
typings install --global --save aurelia-pal-browser=npm:aurelia-pal-browser/dist/commonjs/aurelia-pal-browser.d.ts
typings install --global --save aurelia-path=npm:aurelia-path/dist/commonjs/aurelia-path.d.ts
typings install --global --save aurelia-polyfills=npm:aurelia-polyfills/dist/commonjs/aurelia-polyfills.d.ts
typings install --global --save aurelia-route-recognizer=npm:aurelia-route-recognizer/dist/commonjs/aurelia-route-recognizer.d.ts
typings install --global --save aurelia-router=npm:aurelia-router/dist/commonjs/aurelia-router.d.ts
typings install --global --save aurelia-task-queue=npm:aurelia-task-queue/dist/commonjs/aurelia-task-queue.d.ts
typings install --global --save aurelia-templating=npm:aurelia-templating/dist/commonjs/aurelia-templating.d.ts
typings install --global --save aurelia-templating-binding=npm:aurelia-templating-binding/dist/commonjs/aurelia-templating-binding.d.ts
typings install --global --save aurelia-templating-resources=npm:aurelia-templating-resources/dist/commonjs/aurelia-templating-resources.d.ts
typings install --global --save aurelia-templating-router=npm:aurelia-templating-router/dist/commonjs/aurelia-templating-router.d.ts
``` 
