import {Aurelia} from "aurelia-framework";
import {bootstrap} from "aurelia-bootstrapper-webpack";

import "../node_modules/bootstrap/dist/css/bootstrap.css";
import "../node_modules/font-awesome/css/font-awesome.css";
import "../template/css/template.css";

bootstrap((aurelia:Aurelia):void => {
  aurelia.use
      .standardConfiguration()
      .developmentLogging();

  const rootElement = document.body;
  aurelia.start().then(() => aurelia.setRoot('application', rootElement));
  rootElement.setAttribute('aurelia-app', '');
});
