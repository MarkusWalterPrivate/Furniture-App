<template>
        <div>



            <div style="float: left; width: 80%">
                <h1>Räume</h1>
                <div>
                <b-card-group>
                  <b-card v-for="room in rooms" v-bind:key="room.id"
                  :img-src="room.imgURL"
                  img-alt="Image"
                  img-top
                  style="max-width: 200px;">
                   <b-card-title> {{room.name}}
                   </b-card-title>
                   <b-card-text>
                     Anzahl an Möbeln: {{room.furnitureItems.length}}
                      <b-button v-if="room.name!='Gemerkte Suchergebnisse'" variant="outline-danger" v-on:click="deleteRoom(room.id)">
                            <b-icon-trash></b-icon-trash>Löschen
                      </b-button>
                      <router-link  v-if="room.name!='Gemerkte Suchergebnisse'" class="nav link" :to="{ name: 'RaumAnsicht', params: { id: room.id } }">Raum anschauen</router-link>
                      <router-link  v-if="room.name=='Gemerkte Suchergebnisse'" class="nav link" to="/GemerkteMoebelAnsicht">Raum anschauen</router-link>
                   </b-card-text>
                  </b-card >
                </b-card-group>
            </div>
        </div>
        <div style="float: right; width: 20%">
            <h1> Neuen Raum erstellen</h1>
            <input v-model="RaumName" placeholder="Raum name"> <br>
            <input v-model="imgURL" placeholder="Bild URL " 
            v-b-popover.hover="'Sie können Bild-Upload-Dienste wie imgur.com verwenden und die Bild URL hier eingeben'" title="Hinweis"> <br>
            <b-button size="sm" variant="primary" v-on:click="createNewRoom()" :disabled="RaumName==''" > Raum erstellen</b-button>
        </div>
    </div>
</template>

<script>
import config from "@/config";
import axios from "axios";

export default {
  name: "Home",
  data() {
    return {
      rooms: [],
      RaumName:"",
      userId:0,
      userName:"",
      password:"",
      imgURL:""
    };
  },
      computed:{
        id: function() {
            return this.$store.getters.getUserId;
        },
        authentication: function() {
            return this.$store.getters.getAuthenticationData;
        }
    },
  methods: {
    // send GET request to API to fetch all cats
    fetchRooms: function() {
      axios.get(`${config.apiBaseUrl}/rooms/`, {
                headers:{
                    'name': this.userName, 
                    'password': this.password,
                    'id': this.userId
            }}).then((response) => {
                // log response data to the browser console
                if(response.status==200){
                
                // assign response to data variable
                this.rooms=response.data
                //console.log(this.$store.getters.isLoggedIn)
                }
            }).catch((error)=>{
          this.showToastMessage("Etwas ist schiefgelaufen, bitt probieren Sie es nocheinmal.",error.message , "danger")
        
        });
    },
    createNewRoom: function(){
      let data={'userId': this.userId, 'imgURL': this.imgURL, 'name': this.RaumName, 'authentication': {'userName': this.userName, 'password':this.password}}
       axios.post(`${config.apiBaseUrl}/rooms/`, data).then((response) => {
                // log response data to the browser console
                if(response.status==201){
                
                // assign response to data variable
                this.rooms=response.data
                this.RaumName=""
                this.imgURL=""
                //console.log(this.$store.getters.isLoggedIn)
                }
            }).catch((error)=>{
          this.showToastMessage("Etwas ist schiefgelaufen, bitt probieren Sie es nocheinmal.",error.message, "danger")
        
        });
    },
     viewDetails: function(id){
        this.$router.push({ name: 'RaumAnsicht', params: { id } });
    },
    deleteRoom: function(id){
            axios.delete(`${config.apiBaseUrl}/rooms/${id}`,{
                headers:{
                    'name': this.userName, 
                    'password': this.password,
                    'id': this.userId
            }}).then((response) => {
                if(response.status==200){
                    this.rooms = this.rooms.filter((room) => room.id !== id);
                    this.showToastMessage("Erfolg",
                    "Raum wurde gelöscht!",
                    "success"
        );
                }
            }).catch((error)=>{
          this.showToastMessage("Etwas ist schiefgelaufen, bitt probieren Sie es nocheinmal.", error.message , "danger")
        
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
  // executed after the component has been started
  created: function() {
        this.userId=this.id
        this.password=this.authentication.password
        this.userName=this.authentication.userName
        this.fetchRooms()
  }
};
</script>

<style>
</style>
