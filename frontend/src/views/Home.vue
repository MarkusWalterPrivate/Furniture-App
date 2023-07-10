<template>
  <div class="container">
    <div class="row">
      <div class="col-md-3"></div>
      <div class="col-md-6">
        <div class="home">
          <div class="greeting" v-if="loggedIn">Hallo {{ name }}</div>
          <p></p>
          <h1 class="headline">Suche:</h1>
          <div class="content">
            <select v-model="selectedType" variant="primary">
              <option disabled active value="">Bitte eine Produktkategorie aussuchen</option>
              <option v-for="type in this.typeList" :key="type.type">{{type}}</option>
            </select>
            <br />
            <b-button
              class="searchButton"
              variant="primary"
              size="sm"
              v-on:click="submit()"
              >Suchen</b-button
            >
            <input
              style="display: none"
              type="file"
              accept=".png, .jpg, .jpeg"
              id="imgField"
              @change="onFileSelected"
              ref="fileInput"
            />
            <b-button-group>
              <b-button
              @click="$refs.fileInput.click()"
              class="searchButton"
              variant="primary"
              size="sm"
              >Bild auswählen</b-button>
            <b-button
              :disabled="!imgSelected"
              @click="getColor()"
              class="searchButton"
              variant="primary"
              size="sm"
              >Möbelstück erkennen</b-button>
            </b-button-group>
            
            <b-icon  center v-if="running" icon="three-dots" animation="cylon" font-scale="4"></b-icon>
            <!---
            <div class="previewImgContainer">
              <img v-bind:src="img" class="previewImg" v-if="imgSelected > 0" />
            </div>
          --->
          {{output}}

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
import "@tensorflow/tfjs-backend-cpu";
import "@tensorflow/tfjs-backend-webgl";
import * as cocoSsd from "@tensorflow-models/coco-ssd";
export default {
  name: "Home",
  components: {},
  data() {
    return {
      imgSelected: false,
      img: "",
      furnitureTypes:{},
      typeList:[],
      selectedType:"",
      colors: [],
      running:false,
      output:""
    };
  },
  computed: {
    name: function() {
      return this.$store.getters.getUserName;
    },
    id: function() {
      return this.$store.getters.getUserId;
    },
    loggedIn: function() {
      return this.$store.getters.isLoggedIn;
    },
  },
  methods: {
    // this function is assigned to the search button
    submit: function() {
      for(const [key, value] of Object.entries(this.furnitureTypes)){
        if (value==this.selectedType){
          this.$store.commit("setCategory", [key,value])
        }
      }
      this.$router.push('Suche')
    },
    getCategories: function(){
      let loginToken= config.apiName+":"+config.apiPassword
      let key=btoa(loginToken)
      axios.get('https://config.itestra.com/references/productCategories', {"headers": {"Authorization": "Basic "+key}}).then((response)=>{
        this.furnitureTypes=response.data
        for(const [key, value] of Object.entries(this.furnitureTypes)){
          this.typeList.push(value)
          //console.log(key) //just so the console looks clean
        }
      }).catch((error)=>{
        
          this.showToastMessage("Etwas ist schiefgelaufen, bitt probieren Sie es nocheinmal.", error.message , "danger")
        
        });
    },
    onFileSelected(event) {
      try{
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
      //this.getColor()
      } catch(error){
        console.log("I am an Opra user;)") //Weird error only occuring on opra
      }
      
    },

    async getColor() {
      this.running=true
      

      //categories by furniture
      try {
        const imageElement = document.createElement("img");
        imageElement.src = this.img;
        imageElement.width=200
        imageElement.height=400
        const model = await cocoSsd.load({});
        const predictions = await model.detect(imageElement, 5);
        if (predictions[0].class === "bench") {
          this.selectedType="Sitzbank, Essplatzbank, Dinnersofa, ... (249)"
        } else if (predictions[0].class === "chair") {
          this.selectedType="Stühle (152)"
        } else if (predictions[0].class === "bed") {
          this.output="Betten werden nicht unterstützt, bitte versuchen Sie ein anderes Möbelstück."
        } else if (predictions[0].class === "dining table") {
          this.output="Tische werden nicht unterstützt, bitte versuchen Sie ein anderes Möbelstück."
        } else if (predictions[0].class === "couch") {
          this.selectedType="Sofa / Canapé (993)"
        } else {
          this.output="Möbelstück nicht erkannt, bitte wiederholen Sie den vorgang, oder wählen Sie manuell."
          
        }
      } catch (e) {
        this.output="Bei der Erkennung ist etwas schiefgelaufen, bitte wiederholen Sie den vorgang, oder wählen Sie manuell."
        
      }

      this.running=false
    },
  },
  created: function(){
    this.getCategories();
    this.$store.commit('setResponse', "")
  }
};
</script>

<style scoped>
.content {
  display: flex;
  justify-content: center;
  flex-direction: column;
}
.headline {
  text-align: center;
  margin: 20px;
}
.greeting {
  text-align: center;
}
#searchInput {
  margin: 10px;
}
.searchButton {
  align-self: center;
  margin-top: 20px;
  width: 30%;
}
.previewImgContainer {
  display: block;
  margin-top: 20px;
  width: fit-content;
  max-height: 600px;
}
.previewImg {
  max-width: 100%;
  max-height: 600px;
}
</style>
