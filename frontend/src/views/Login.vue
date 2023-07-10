<template>
  <div class="container">
    <div class="row">
      <div class="col-md-3"></div>
      <div class="col-md-6">
        <form>
          <div class="form-group">
            
            <input
              v-model="userName"
              placeholder="Benutzername"
              class="form-control"
              required
            />
          </div>
          <div class="form-group">
            <input
              v-model="password"
              placeholder="Passwort"
              type="password"
              class="form-control"
              required
            />
          </div>
          <b-button variant= "primary" v-on:click="login()">Login</b-button>
        </form>
        <div class="col-md-3"></div>
      </div>
      
    </div>
  </div>
  
</template>

<script>
import axios from "axios";
import config from "@/config";
export default{
  name: "Home",
  data() {
    return {
      id: "0",
      authentication: {
      },
      password: "",
      userName: "",
      searchQuerys: [],
      rooms: [] 
    };
  },
  methods: {
    // send GET request to API to fetch all cats
    login: function() {
      axios.get(`${config.apiBaseUrl}/authentications/`, {
        headers:{
          'name': this.userName, 
          'password': window.btoa(this.password)
          }}).then((response) => {
        // log response data to the browser console
        if(response.status==200){
          //console.log(response.data);
          // assign response to data variable
          this.users = response.data;
          this.$store.commit ('login', response.data);
          this.$router.push('/')
          //console.log(this.$store.getters.isLoggedIn)
        }

      }).catch((error)=>{
          this.showToastMessage("Etwas ist schiefgelaufen, bitt probieren Sie es nocheinmal.",error.message , "danger")
        
        });
    },
    showToastMessage(title, msg, variant) {
            this.$bvToast.toast(msg, {
                title: title,
                variant: variant,
                solid: true,
                toaster: "b-toaster-top-center",
                autoHideDelay: 4000,
                appendToast: true
            });
        }

  }
}

</script>

<style scoped>
.userBox {
  padding: 10px;
  margin: 3px;
  border: 1px solid #42b983;
  float: left;
  min-width: 250px;
  text-align: center;
}</style>
