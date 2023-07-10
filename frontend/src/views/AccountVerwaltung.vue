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
                <a v-if="loggedIn==false" class="nav-link active" href="#">Account erstellen</a>
              </li>
              <li class="nav-item">
                <router-link v-if="loggedIn" class="nav-link" to="DeleteAcc"
                  >Account löschen</router-link
                >
              </li>
              <li class="nav-item">
                <router-link v-if="loggedIn" class="nav-link" to="ChangePassword"
                  >Passwort ändern</router-link
                >
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
                placeholder="Password"
                type="password"
                class="form-control"
                required
              />
              </div>
              <div class="form-group">
                <input
                v-model="newPassword"
                placeholder="Passwort bestätigen"
                type="password"
                class="form-control"
                required
              />
            </div>
            <b-button variant= "primary" v-on:click="accountCreation()" >Account erstellen</b-button>  
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
export default{
  name: "AccountErstellen",
  data() {
    return {
      users :[],
      userName:"",
      password:"",
      newPassword:""
    };
  },
  methods: {
    // send GET request to API to fetch all cats
    
    accountCreation: function() {
        if (this.password != this.newPassword) {
        this.showToastMessage("Fehler!", "Passwörter stimmen nicht überein!", "danger")
      } else if(this.password.length<10||this.userName.length<3){
        this.showToastMessage("Fehler", "Passwort mus mindeestens 10 Zeichen lang sein, Nutzername mindestens 3", "danger")
      }else {
        const data ={'userName': this.userName, 'password': window.btoa(this.password) };
          axios.post(`${config.apiBaseUrl}/users/`, data).then((response) => {
        // log response data to the browser console
        // assign response to data variable
          this.users = response.data;
          this.$store.commit ('login', response.data);
          this.createNewRoom();
          this.$router.push('/')



        }).catch((error)=>{
          if(error.message.includes("409")){
            this.showToastMessage("Fehler!", "Nutzername schon vergeben.", "warning")
          }else{
          this.showToastMessage("Etwas ist schiefgelaufen, bitt probieren Sie es nocheinmal.", error.message, "danger")
          
          }
        
        });
      }
      
    },
    createNewRoom: function(){
      let data={'userId': this.users.id, 'name': "Gemerkte Suchergebnisse", 'imgURL':"", 'authentication': this.users.authentication}
       axios.post(`${config.apiBaseUrl}/rooms/`, data).then((response) => {
                // log response data to the browser console
                if(response.status!=201){
                // assign response to data variable
                //console.log(this.$store.getters.isLoggedIn)
                this.showToastMessage("Fehler!", "Etwas ist schiefgelaufen, bitte probieren Sie es noch einmal", "danger")
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
    loggedIn : function(){
      return this.$store.getters.isLoggedIn
    }
  
    
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
.form-control {
  width: 100%;
}
.button{
  color: blue;
}
</style>
