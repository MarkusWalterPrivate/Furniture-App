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
                <router-link class="nav-link active" to="/DeleteAcc"
                  >Account löschen</router-link
                >
              </li>
              <li class="nav-item">
                <router-link class="nav-link" to="/ChangePassword"
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
                />
              </div>    
              <div class="form-group">
                <input
                v-model="password"
                placeholder="Password"
                type="password"
                class="form-control"
              />
              </div>
              <div class="form-group">
                <input
                v-model="passwordConfirm"
                placeholder="Passwort bestätigen"
                type="password"
                class="form-control"
              />
            </div>
            <b-button variant= "primary" v-on:click="accountDeletion()" >Account löschen</b-button>  
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
      passwordConfirm: "",
    };
  },
  methods: {
    accountDeletion: function() {
        if (this.password != this.passwordConfirm) {
        this.showToastMessage("Fehler!", "Passwörter stimmen nicht überein!", "danger")
      } else if(window.btoa(this.password)==this.$store.state.authentication.password){
        
          axios.delete(`${config.apiBaseUrl}/users/${this.id}`,{
             headers:{
            'name': this.userName, 
            'password': window.btoa(this.password)
            }}).then((response) => {
            //Have to add userId to end of URL
          // log response data to the browser console
          if(response.status==200){
              this.$store.commit('logout')
              this.$router.push('/')
            }
        }).catch((error)=>{
          this.showToastMessage("Etwas ist schiefgelaufen, bitt probieren Sie es nocheinmal.", error.message, "danger")
        
        });
      }else{
        this.showToastMessage("Fehler!", "Das Passworter stimmen nicht!", "danger")
      }
      
      
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
  computed: {
      id : function() {
      return this.$store.getters.getUserId;
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

</style>
