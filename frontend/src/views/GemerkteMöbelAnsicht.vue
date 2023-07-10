<template>
    <div>
        <b-card-group>
            <div v-for="item in items" v-bind:key="item.id" style="width: 90%; margin: 0 auto;">
                <b-card class="text-right mt-3" :img-src='item.result.imageUrls[0]' alt="Image" img-left img-height="200">
                    <b-card-title class="cardContent">
                        {{item.result.name}} 
                            
                            <b-button variant="warning"  v-on:click="unsave(item.result.id)"><b-icon-star/></b-button>
                        
                        
                    </b-card-title>
                    <b-card-text class="cardContent">
                        Artikelnumer: {{item.result.artikelnummer}} <br>
                        <b-button variant="primary" v-on:click="viewDetails(item.result.id)">Detailansicht in neuem Tab</b-button>
                    </b-card-text>
                </b-card>
            </div>
        </b-card-group>
    </div>
</template>

<script>
import config from "@/config";
import axios from "axios";

export default {
  name: "Home",
  data() {
    return {
      room: {},
      items:[],
      RaumName:"",
      userId:0,
      userName:"",
      password:""

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
                response.data.forEach(room => {
                    if(room.name=="Gemerkte Suchergebnisse"){
                        this.room=room
                        //console.log(room)
                    }
                    this.room.furnitureItems.forEach((item)=>{
                        //console.log(item)
                        this.fetchItem(item.itestraId)
                    })
                });
                // assign response to data variable
                //console.log(this.$store.getters.isLoggedIn)
                }
            }).catch((error)=>{
          this.showToastMessage("Etwas ist schiefgelaufen, bitt probieren Sie es nocheinmal.", error.message, "danger")
        
        });
    },
    fetchItem: function(id){
        let loginToken= config.apiName+":"+config.apiPassword
            let Authkey=btoa(loginToken)
            axios.get(`https://config.itestra.com/ui/items/${id}`, {headers: {"Authorization": "Basic "+Authkey}}).then((response)=>{
                if(response.status==200){ 
                    this.items.push(response.data)
                }
            }).catch((error)=>{
                this.showToastMessage("Etwas ist schiefgelaufen, bitt probieren Sie es nocheinmal.", error.message, "danger")
        
        });
    },
    unsave: function(itestraId){
            let header={'headers':{"id":this.id, "roomId":this.germerkteMoebelRaumId, "name":this.authentication.userName, "password":this.authentication.password}}
            axios.delete(`${config.apiBaseUrl}/itestraFurnitures/${itestraId}`, header
                ).then((response) => {
                if(response.status==200){
                // assign response to data variable
                    this.items=this.items.filter((item)=>item.result.id!==itestraId)
                }
            }).catch((error)=>{
                this.showToastMessage("Etwas ist schiefgelaufen, bitt probieren Sie es nocheinmal.", error.message, "danger")
        
             });
            
        },
        viewDetails: function(id){
            let routeData = this.$router.resolve({ name: 'itemAnsicht', params: { id } });
            window.open(routeData.href, '_blank');
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