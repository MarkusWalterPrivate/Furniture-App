# Vue.js Frontend

This is the Vue.js front end for the "SoPra WS21/22: Möbel App".
The frontend is devided into diffrent views:

- Home.vue functions as the main view where the user can send search requests.
- Suche.vue is where the user sees the results, and can specify his search criteria.
- SucheHistorie.vue shows the users search history.
- itemAnsicht.vue shows the details of one furniture pice.
- AccountVerwaltung.vue is where the user can create an Account.
- Login.vue is where the User can log into an existing Account.
- ChangePassword.vue and DeleteAcc.vue will show as soon as the User is logged in.
- RaumVerwaltung.vue is where the User can manage his rooms.
- RaumAnsicht.vue is where the User can add or remove furniture in the selected room.

## Prerequisites

Install [Node.js](https://nodejs.org) and ensure that the root folder of its installation is added to your `PATH`.

## Usage

```bash
# install dependencies
npm install

# start with hot-reload for development --> http://localhost:8000
npm run serve

# compile and minify for production --> ./dist folder
npm run build

# lint and fix files
npm run lint
```

## Documentation

- General Vue.js documentation: https://vuejs.org/v2/guide
- Vue CLI documentation: https://cli.vuejs.org/guide
- Bootstrap CSS framework: https://getbootstrap.com/docs/4.5/layout/overview
- BootstrapVue UI components: https://bootstrap-vue.org/docs/components
