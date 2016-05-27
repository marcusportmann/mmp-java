import {Router, RouterConfiguration} from 'aurelia-router';
import {TemplateApplication} from "../template/ts/template";

export class Application extends TemplateApplication {
  router: Router;

  configureRouter(config: RouterConfiguration, router: Router) {
    config.title = 'evaluate-frontend';
    config.map([
      { route: ['', 'home'], name: 'home', moduleId: 'views/home', nav: true, title: 'Home' }
    ]);

    this.router = router;
  }
}
