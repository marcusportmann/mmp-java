To bootstrap a JEE application that leverages the Aurelia framework complete the following steps:

- Install Node.js and update the node package manager (npm).

    See: https://docs.npmjs.com/getting-started/installing-node


- Install jspm globally by executing the following command:

    OS X: sudo npm install jspm -g


- Run the following command in the .../web/src/main/webapp directory:

    jspm init

        Package.json file does not exist, create it? [yes]: yes
        Would you like jspm to prefix the jspm package.json properties under jspm? [yes]: yes
        Enter server baseURL (public folder path) [./]: ./
        Enter jspm packages folder [./jspm_packages]: ./packages
        Enter config file path [./config.js]: ./config.js
        Configuration file config.js doesn't exist, create it? [yes]: yes
        Enter client baseURL (public folder URL) [/]: /sample/ <-- Context path for the web application (WAR)
        Do you wish to use a transpiler? [yes]: yes
        Which ES6 transpiler would you like to use, Babel, TypeScript or Traceur? [babel]: TypeScript


- Excute the following commands in the .../web/src/main/webapp directory:

    jspm install aurelia-bootstrapper
    jspm install aurelia-framework
    jspm install aurelia-validation

    jspm install aurelia-bootstrapper && jspm install aurelia-framework && jspm install aurelia-validation


- Create a directory named typings under the .../web/src/main/webapp directory.

  Create a file named copy_aurelia_typings.sh with the following contents, mark it as executable and execute it.

    rm -rf aurelia
    mkdir aurelia
    find ../packages -name "aurelia*.d.ts" -exec cp {} aurelia \;

    rm -rf typescript
    mkdir typescript
    find ../packages -name "lib.es6.d.ts" -exec cp {} typescript \;


- Add a file named tsconfig.json to the .../web/src/main/webapp directory with the following contents:

{
    "version": "1.5.0",
    "compilerOptions": {
        "target": "es5",
        "module": "commonjs",
        "declaration": false,
        "noImplicitAny": false,
        "removeComments": true,
        "noLib": false,
        "emitDecoratorMetadata": true,
        "experimentalDecorators": true
    },

    "filesGlob": [
        "./**/*.ts"
    ],

    "files": [
    "./ts/app.ts",
    "./ts/models.ts",
    "./ts/main.ts",
    "./typings/typescript/lib.es6.d.ts",
    "./typings/aurelia/aurelia-framework.d.ts",
    "./typings/aurelia/aurelia-logging.d.ts",
    "./typings/aurelia/aurelia-templating.d.ts",
    "./typings/aurelia/aurelia-path.d.ts",
    "./typings/aurelia/aurelia-dependency-injection.d.ts",
    "./typings/aurelia/aurelia-loader.d.ts",
    "./typings/aurelia/aurelia-pal.d.ts",
    "./typings/aurelia/aurelia-binding.d.ts",
    "./typings/aurelia/aurelia-metadata.d.ts",
    "./typings/aurelia/aurelia-templating.d.ts",
    "./typings/aurelia/aurelia-loader.d.ts",
    "./typings/aurelia/aurelia-task-queue.d.ts",
    "./typings/aurelia/aurelia-validation.d.ts"
    ]
}



