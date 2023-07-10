<template>
        <div>


            <input
              style="display: none"
              type="file"
              accept=".png, .jpg, .jpeg"
              id="imgField"
              @change="onFileSelected"
              ref="fileInput"
            />
            <div  style="float: left; width: 75%">
                <h1>Räume</h1>
                <div>
                <b-card-group>
                  <b-card v-for="item in furniture" v-bind:key="item.id"
                  :img-src="item.imgURL"
                  img-alt="Image"
                  img-top
                  style="max-width: 200px;">
                   <b-card-title> {{item.name}}
                   </b-card-title>
                   <b-card-text>
                     <b>Material: </b>{{item.material}}<br>
                     <b>Farbe: </b>{{item.color}}<br>
                      <b-button  variant="outline-danger" v-on:click="deleteFurniture(item.id)">
                            <b-icon-trash></b-icon-trash>Löschen
                      </b-button>
                   </b-card-text>
                  </b-card >
                </b-card-group>
            </div>
        </div>
        <div  style="float: right; width: 25%; overflow-wrap:break-word;">
            <h1> Neues Möbelstück hinzufügen:</h1>
            <input v-model="furnitureName" placeholder="Möbelname"> <br>
            <b>Anzeige Bild:</b> kein Pflichtfeld <br>
            <input v-model="imgURL" placeholder="Bild URL " 
            v-b-popover.hover="'Sie können Bild-Upload-Dienste wie imgur.com verwenden und die Bild URL hier eingeben'" title="Hinweis"> <br>
            <b>Farbe:</b><br>
            <b-button-group >
                    <b-button  size="sm" variant="primary" @click="$refs.fileInput.click()"
                        v-b-popover.hover="'Bitte möglichst nah an die Farbe ran. Sie können die Farberkennung auch mehrfach laufen lassen.'" title="Achtung">
                    <b-icon-camera/></b-button>
                    <b-button size=sm variant="primary" :disabled="!imgSelected" @click="getColor()"> Farberkennung</b-button>
            </b-button-group><br>
            <select v-model="color"> 
              <option disabled active value="">Primäre Farbe</option>
              <option v-for="(option, index) in possibleColors" v-bind:key="index" v-bind:value="option"> {{option}}</option>
            </select><br>
            <b>Material:</b><br>
            <select v-model="material"> 
              <option disabled active value="">Polsterbezugsmaterial</option>
              <option v-for="(option, index) in possibleMaterial" v-bind:key="index" v-bind:value="option"> {{option}}</option>
            </select><br>
            <b-button size="sm" variant="primary" v-on:click="createNewFurniture()" :disabled="color==''||material==''||furnitureName==''"> Möbel erstellen</b-button>
        </div>
    </div>
</template>

<script>
import config from "@/config";
import axios from "axios";
import analyze from "rgbaster";

export default {
  name: "Home",
  data() {
    return {
      furniture: [],
      furnitureName:"",
      color:"",
      material:"",
      possibleMaterial:["Leder", "Stoff", "Sonstige"],
      possibleColors:["weiß", "schwarz", "rot", "grün", "blau", "gelb", "grau", "braun", "beige", "Sonstige"],
      userId:0,
      userName:"",
      password:"",
      imgURL:"",
      imgSelected:false

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
    fetchFurniture: function(id) {
      axios.get(`${config.apiBaseUrl}/rooms/${id}`, {
                headers:{
                    'name': this.userName, 
                    'password': this.password,
                    'id': this.userId
            }}).then((response) => {
                // log response data to the browser console
                if(response.status==200){
                console.log(response.data)
                // assign response to data variable
                this.furniture=response.data.furnitureItems
                }
            }).catch((error)=>{
          this.showToastMessage("Etwas ist schiefgelaufen, bitt probieren Sie es nocheinmal.",error.message , "danger")
        
        });
    },
    createNewFurniture: function(){
      let data={
        'userId': this.userId, 
        'roomId': this.$route.params.id, 
        'name': this.furnitureName, 
        'material': this.material, 
        'color': this.color,
        'imgURL': this.imgURL, 
        'authentication': {'userName': this.userName, 'password':this.password}
        }
       axios.post(`${config.apiBaseUrl}/furnitures/`, data).then((response) => {
                // log response data to the browser console
                if(response.status==201){
                console.log(response.data)
                // assign response to data variable
                this.furniture.push(response.data)
                this.furnitureName=""
                this.imgURL=""
                this.color=""
                this.material=""
                //console.log(this.$store.getters.isLoggedIn)
                }
            }).catch((error)=>{
          this.showToastMessage("Etwas ist schiefgelaufen, bitt probieren Sie es nocheinmal.",error.message , "danger")
        
        });
    },
    deleteFurniture: function(id){
            axios.delete(`${config.apiBaseUrl}/furnitures/${id}`,{
                headers:{
                    'name': this.userName, 
                    'password': this.password,
                    'id': this.userId
            }}).then((response) => {
                if(response.status==200){
                    this.furniture = this.furniture.filter((room) => room.id !== id);
                    this.showToastMessage("Erfolg",
                    "Raum wurde gelöscht!",
                    "success"
        );
                }
            }).catch((error)=>{
          this.showToastMessage("Etwas ist schiefgelaufen, bitt probieren Sie es nocheinmal.",error.message , "danger")
        
        });
    },
    onFileSelected(event) {
      //console.log(event);
      this.imgSelected = true;
      var reader,
        files = event.target.files;
      if (files.length === 0) {
        console.log("empty");
      }
      reader = new FileReader();

      reader.onload = (event) => {
        this.img = event.target.result;
      };

      reader.readAsDataURL(files[0]);
      //console.log(this.img+"test")
      //this.getColor()
    },
    async getColor() {
      const imageElement = document.createElement("img");
      imageElement.src = this.img;
      imageElement.width=200
      imageElement.height=400

      //todo: fix color bug (img crop)

      const result = await analyze(this.img, {
        ignore: ["rgb(255,255,255)", "rgb(0,0,0)"], scale:0.1 //ignore Black/ weißen Hintergrund
      });



      //categories by color
      let colors = []
      let i=0
      while(i<=result.length-1&&i<1000){
        let rgb = result[0].color;
        let reg = /\d+/g;
        let numbers = rgb.match(reg);
        colors.push(numbers)
        i++
      }
    let foundColors=[]
    colors.forEach((color)=>{
        let rValue = parseInt(color[0]);
        let gValue = parseInt(color[1]);
        let bValue = parseInt(color[2]);
         //log color as colorname
       if (rValue < 30 && gValue < 30 && bValue < 30) {
          //very dark
        foundColors.push("schwarz");
      } else if (rValue > 220 && gValue > 220 && bValue > 220) {
          //very light
        foundColors.push("weiß");
      } else if (bValue > 3*rValue && bValue > 3*gValue) {
          //blue dominant
        foundColors.push("blau");
      } else if (bValue>150 && (gValue-bValue)>20 && (gValue-bValue)<30 && (rValue-gValue)>20 && (rValue-gValue)<30) {
          //trial and error...
          foundColors.push("beige")
      } else if (gValue > 3*rValue && gValue > 3*bValue) {
          //green dominant
        foundColors.push("grün");
      }else if ( rValue > 50 && rValue < 150 && rValue > (2*gValue) && bValue <20) {
          //r dominant, but not too strong
        foundColors.push("braun");
      } else if (rValue > 3*gValue && rValue > 3*bValue) {
          //red dominent
        foundColors.push("rot");
      } else if ((255-rValue) > 3*(255-gValue) && (255-rValue) > 3*(255-bValue)) {
          //similar to rgb but transfered to mcy
        foundColors.push("gelb");
      } else if (
          ((rValue>bValue&&(rValue-10)<bValue)||(rValue<bValue&&(rValue+10)>bValue))&&  //r and b similar
          ((rValue>gValue&&(rValue-10)<gValue)||(rValue<bValue&&(rValue+10)>gValue))&&  //r and g similar
          ((gValue>bValue&&(gValue-10)<bValue)||(gValue<bValue&&(gValue+10)>bValue))   //b and g similar
      ) {
        foundColors.push("grau");

      }  else {
        foundColors.push("Sonstige");
      }
      
    })
    //Remove Doublicates
      const mySet = new Set(foundColors)
      foundColors=Array.from(mySet)
      this.color=foundColors[0] //use only most dominant color

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
        this.fetchFurniture(this.$route.params.id)
  }
};
</script>

<style>

</style>
