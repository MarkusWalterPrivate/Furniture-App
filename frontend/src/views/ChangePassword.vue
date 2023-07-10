<template>
  <div class="container">
    <div class="row">
      <div class="col-md-3"></div>
      <div class="col-md-6">
        <div class="AccountVerwaltung">
          <h1 class="headline">Account Verwaltung</h1>
          <div class="content">
            <ul class="nav nav-pills">
              <li class="nav-item">
                <router-link class="nav-link" to="/DeleteAcc"
                  >Account löschen</router-link
                >
              </li>
              <li class="nav-item">
                <a class="nav-link active" href="#">Passwort ändern </a>
              </li>
            </ul>
          </div>
          <div class="innerContent">
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
                placeholder="Altes Password"
                type="password"
                class="form-control"
                required
              />
              </div>
              <div class="form-group">
                <input
                v-model="newPassword"
                placeholder="Neues Password"
                type="password"
                class="form-control"
                required
              />
            </div>
            <b-button variant= "primary" v-on:click="accountCreation()" >Passwort ändern</b-button>  
            </form>
          </div>
        </div>
      </div>
      <div class="col-md-3"></div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import config from "@/config";
export default {
  data() {
    return {
      userName: "",
      password: "",
      newPassword: "",
    };
  },
  methods: {
    accountCreation: function() {
        const data ={'oldAuthentication' : {'userName': this.userName, 'password': window.btoa(this.password)}, 'newAuthentication' :{'userName': this.userName, 'password': window.btoa(this.newPassword)} };
        if(this.newPassword.length<10){
          this.showToastMessage("Fehler", "Das neue Passwort muss mindestens 10 Zeichen lang sein!", "warning")
          return
        }
          axios.put(`${config.apiBaseUrl}/authentications/${this.id}`, data).then((response) => {
            //Have to add user ID to end of
        // log response data to the browser console
        //console.log(response.data);
        // assign response to data variable
        
        if(response.status==200){
          this.users = response.data;
          this.$store.commit('updateAuthentication', response.data)
        }

        }).catch((error)=>{
          this.showToastMessage("Etwas ist schiefgelaufen, bitt probieren Sie es nocheinmal.", error.message, "danger")
        
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
  },
  computed:{
        id : function(){
      return this.$store.getters.getUserId;
    },
  }
  };
</script>

<style scoped>
.content {
  display: flex;
  justify-content: center;
  margin: 20px;
}
.right {
  display: flex;
}
.left {
  display: flex;
  flex-direction: column;
}
.headline {
  text-align: center;
}
.divTable {
  display: flex;
}
.divTableRow {
  display: flex;
}
.innerContent {
  display: flex;
  justify-content: center;
  margin-top: 10%;
}
</style>
