import Vue from "vue";

import VueRouter from "vue-router";
import Home from "../views/Home.vue";
import Login from "../views/Login.vue";
import AccountVerwaltung from "../views/AccountVerwaltung.vue";
import DeleteAcc from "../views/DeleteAcc.vue";
import ChangePassword from "../views/ChangePassword.vue";
import Suche from "../views/Suche.vue";
import SuchHistorie from "../views/SucheHistorie.vue"
import PageNotFound from "../views/404.vue"
import itemAnsicht from "../views/itemAnsicht.vue"
import RaumVerwaltung from "../views/RaumVerwaltung.vue"
import GemerkteMöbelAnsicht from "../views/GemerkteMöbelAnsicht"
import RaumAnsicht from "../views/RaumAnsicht"
import About from "../views/About"


Vue.use(VueRouter);




// all frontend routes of the app, i.e. bind a path to a Vue component
const routes = [
  {
    path: "/Suche",
    name: "Suche",
    component: Suche
  },
  {
    path: "/About",
    name: "About",
    component: About
  },
  {
    path: "/itemAnsicht/:id",
    name: "itemAnsicht",
    component: itemAnsicht
  },
  {
    path: "/RaumVerwaltung",
    name: "RaumVerwaltung",
    component: RaumVerwaltung
  },
  {
    path: "/RaumAnsicht/:id",
    name: "RaumAnsicht",
    component: RaumAnsicht
  },
  {
    path: "/GemerkteMoebelAnsicht",
    name: "GemerkteMöbelAnsicht",
    component: GemerkteMöbelAnsicht
  },
  {
    path: "/Login",
    name: "Login",
    component: Login
  },
  {
    path: "/",
    name: "Home",
    component: Home
  },
  {
    path: "/AccountVerwaltung",
    name: "AccountVerwaltung",
    component: AccountVerwaltung
  },
  {
    path: "/DeleteAcc",
    name: "DeleteAcc",
    component: DeleteAcc
  },
  {
    path: "/ChangePassword",
    name: "ChangePassword",
    component: ChangePassword
  },
  {
    path: "/Suchanfragen",
    name: "SuchHistorie",
    component: SuchHistorie
  },
  {
    path: '/:catchAll(.*)*',
    name: "PageNotFound",
    component: PageNotFound,
  },
];

const router = new VueRouter({
  routes
});



export default router;
