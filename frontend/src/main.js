// main JS file for instantiating the application

import Vue from "vue";
import App from "./App.vue"; //./MöbelApp.vue in ./App.vue ändern für Cat-project
import router from "./router";
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue/dist/bootstrap-vue.css";
import { BootstrapVue, BootstrapVueIcons } from "bootstrap-vue";
import store from "./store.js"


// BootstrapVue for UI components and icons
Vue.use(BootstrapVue);
Vue.use(BootstrapVueIcons)


Vue.config.productionTip = false;



new Vue({
  store,
  router,
  render: (h) => h(App)
}).$mount("#app");
