<template>
  <div id="app">
    <b-navbar toggleable="lg" type="dark" variant="primary">
      <b-navbar-brand to="/">
        <img alt="Vue logo" src="./assets/logo.png" class="logo" />
        Home
      </b-navbar-brand>

      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

      <b-collapse id="nav-collapse" is-nav>
        <b-navbar-nav>
          <b-nav-item v-if="loggedIn == false" to="/AccountVerwaltung"
            >Account erstellen</b-nav-item
          >
          <b-nav-item v-if="loggedIn" to="/ChangePassword"
            >Accountverwaltung</b-nav-item
          >
          <b-nav-item v-if="loggedIn" to="/RaumVerwaltung"
            >Raumverwaltung</b-nav-item
          ><!--  -->

          <b-nav-item v-if="loggedIn" to="/Suchanfragen"
            >Vergangene Suchanfragen</b-nav-item
          >
        </b-navbar-nav>

        <!-- Right aligned nav items -->
        <b-navbar-nav class="ml-auto">
          <b-nav-item to="/About">About</b-nav-item>
          <b-nav-item variant="success" v-if="loggedIn == false" to="/Login">Login</b-nav-item>
          <b-button variant="danger" v-if="loggedIn" v-on:click="logout()"> Logout </b-button>
        </b-navbar-nav>
      </b-collapse>
    </b-navbar>

    <div class="main">
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
export default {
  name: "App",
  data() {
    return {};
  },
  computed: {
    loggedIn: function() {
      return this.$store.getters.isLoggedIn;
    },
  },
  methods: {
    logout: function() {
      this.$store.commit("logout");
    },
  },
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  color: #2c3e50;
}
.logo {
  height: 35px;
  margin: 0 3px;
}

.main {
  padding: 10px 20px;
}
</style>
