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
        <b-alert
        variant="warning"
        v-model="showDismissibleAlert"
        >Zum bearbeiten der Suche, wählen Sie bitte "Suchparameter zurücksetzen" und führen danach eine neue Suche aus. 
        </b-alert>
        <div>
            <h3>{{this.proCat[1]}}</h3>
            Davon {{this.itemsFound}} gefunden: 
        </div>
        <div style="float:left; width:80%">
            <b-card-group class="mt-4" style="width: 95%">
            <div v-for="item in items" v-bind:key="item.id" style="width: 90%; margin: 0 auto;">
                <b-card class="text-right mt-3" :img-src='item.imageUrls[0]' alt="Image" img-left img-height="200">
                    <b-card-title class="cardContent">
                        {{item.name}} 
                        <div v-if="isLoggedIn">
                            <b-button v-if="!savedItems.includes(item.id)" v-on:click="save(item.id)"><b-icon-star/></b-button>
                            <b-button variant="warning" v-if="savedItems.includes(item.id)" v-on:click="unsave(item.id)"><b-icon-star/></b-button>
                        </div>
                        
                    </b-card-title>
                    <b-card-text class="cardContent">
                        Artikelnumer: {{item.artikelnummer}} <br>
                        <b-button variant="primary" v-on:click="viewDetails(item.id)">Detailansicht in neuem Tab</b-button>
                    </b-card-text>
                    <b-card-footer class="cardContent">Preis: {{item.price.retailPrice.amountInMinorUnits/100}}€</b-card-footer>
                </b-card>
            </div>
            </b-card-group>
            <div class="navigator">
                <b-button-group>
                    <b-button size="sm" justify= "space around" variant="primary" v-if="this.page>1" v-on:click="prevPage()">Vorige Seite</b-button>
                    <div v-if="this.itemsFound!=0" >
                     <b-button disabled>   {{this.page}}</b-button>
                    </div>
                    <b-button size="sm" justify= "space around" variant="primary" v-if="(this.itemsFound/15)>this.page" v-on:click="nextPage()">Nächste Seite</b-button>
                    </b-button-group>
            </div>
        </div>
        <div style="float:right; width:20%; overflow-wrap:break-word;">
            <h4>Sucheparameter</h4>
            <div v-if="rooms.length>0">
                <b>Suche für Raum:</b><br>
                <select v-model="selectedRoom" @change="roomMatching()" v-b-popover.hover="'Die Polsterfarbe und Material werden für Sie passend vorausgewählt, sodass sie zu den im Raum befindlichen Möbeln passen'" title="Hinweis">
                    <option v-for="room in rooms" v-bind:key="room.id">{{room.name}}</option>
                    <option disabled active value="">Bitte Raum auswählen</option>
                </select>
            </div>
            <div v-for="field in enumComboFields" v-bind:key="field.id">
                <b>{{field.fieldName}}</b><br>
                <b-button-group v-if="field.fieldName=='Farbe Bezug'">
                    <b-button  size="sm" variant="primary" @click="$refs.fileInput.click()"
                        v-b-popover.hover="'Bitte möglichst nah an die Farbe ran. Sie können die Farberkennung auch mehrfach laufen lassen.'" title="Achtung">
                    <b-icon-camera/></b-button>
                    <b-button size=sm variant="primary" :disabled="!imgSelected" @click="getColor(field.id)"> Farberkennung</b-button>
                </b-button-group>
                <b-button-group v-if="field.fieldName=='Farbe FußGestell'">
                    <b-button  size="sm" variant="primary" @click="$refs.fileInput.click()"
                        v-b-popover.hover="'Bitte möglichst nah an die Farbe ran. Sie können die Farberkennung auch mehrfach laufen lassen. Funktioniert NICHT mit metallenen Farben'" title="Achtung">
                    <b-icon-camera/></b-button>
                    <b-button size=sm variant="primary" :disabled="!imgSelected" @click="getColor(field.id)"> Farberkennung</b-button>
                </b-button-group>
                <br>
                <select v-model="field.selected" multiple> 
                    <option v-for="option in field.validValues" v-bind:key="option.id" v-bind:value="option.id"> {{option.text}}</option>
                </select>
            </div>
            <div v-for="field in numberFields" v-bind:key="field.id">
                <b>{{field.fieldName}} in mm</b><br>
                Min: <input type="number" 
                @change="() => { 
                    if(field.selectedMin > field.maxValue-1) { field.selectedMin=field.maxValue-1 } 
                    else if( field.selectedMin < field.minValue){field.selectedMin=field.minValue}}"
                :min="field.minValue" 
                :max="field.maxValue-1" 
                :placeholder="field.minValue" 
                v-model="field.selectedMin"> <br>
                Max:<input type="number" 
                 @change="() => { 
                    if(field.selectedMax > field.maxValue) { field.selectedMax=field.maxValue } 
                    else if( field.selectedMax < field.minValue+1){field.selectedMax=field.minValue+1}}"
                :min="field.minValue+1" 
                :max="field.maxValue" 
                :placeholder="field.maxValue" 
                v-model="field.selectedMax">

            </div>
            <b-button-group>
            <b-button size="sm" variant="primary" v-on:click="search()">Suchen</b-button> &nbsp;
            <b-button size="sm" :disabled="disableReset" variant="primary" v-on:click="reset()">Suchparameter zurücksetzen</b-button>
            </b-button-group>
        </div>
        

    </div>
    
</template>
<script>
// import configuration with API url; @ refers to the src directory
import config from "@/config";
// import library for HTTP requests
import axios from "axios";
import analyze from "rgbaster";

export default {
    name: "Home",
    data() {
        return {
            result: {},
            items:[],
            page:0,
            itemsFound:0,
            proCat:"",
            requestData:{},
            enumComboFields:[],
            enumFields:[],
            numberFields:[],
            searchString:{},
            isLoggedIn:false,
            searchId:0,
            showDismissibleAlert:false,
            savedItems:[],
            germerkteMoebelRaumId:0,
            img:"",
            imgSelected:false,
            disableReset:true,
            rooms:[],
            selectedRoom:""



        };
    },
    computed:{
        loggedIn: function() {
            return this.$store.getters.isLoggedIn;
        },
        id: function() {
            return this.$store.getters.getUserId;
        },
        authentication: function() {
            return this.$store.getters.getAuthenticationData;
        }
    },
    methods: {
        // send GET request to API to fetch furniture of categorie
        fetchSearchResult: function () {
                this.requestData= {"page" : 1, "pageSize": 15, "productCategory": this.proCat[0]}
                this.searchString["productCategory"]=this.proCat
                if(this.isLoggedIn){
                    this.saveSearch()
                }
                //calculation login Data for API
                let loginToken= config.apiName+":"+config.apiPassword
                let Authkey=btoa(loginToken)
                axios.post('https://config.itestra.com/ui/items/filter',this.requestData, {"headers": {"Authorization": "Basic "+Authkey}}).then((response)=>{
                    if(response.status==200){
                        //Saving result in several fields for better understanding.
                        this.items=response.data.result.items
                        this.page=response.data.result.currentPage
                        this.itemsFound=response.data.result.total
                        //Adding fields to save the selction in
                        response.data.result.filteringUiFieldMetas.forEach(UiElement => {
                            if(UiElement.type=="ENUM_COMBO"){
                                UiElement["selected"]=[];
                                this.enumComboFields.push(UiElement)
                            }else if(UiElement.type=="NUMBER"){
                                UiElement["selectedMin"]=UiElement.minValue
                                UiElement["selectedMax"]=UiElement.maxValue
                                this.numberFields.push(UiElement)
                            }
                         
                        });
                        //Sorting the fieds according to given ranking
                        this.sortEnumComb()
                    }
                }).catch((error)=>{
                this.showToastMessage("Etwas ist schiefgelaufen, bitt probieren Sie es nocheinmal.",error.message , "danger")
                this.$router.push('Home')
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

    async getColor(fieldId) {
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

        let rgb = result[i].color;
        
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


      this.enumComboFields.forEach((field)=>{
          //look for field that assignes colors
          if(field.id==fieldId){
              field.validValues.forEach((value)=>{
                  //add color to selected colors
                  if(foundColors.includes(value.text)){
                      field.selected.push(value.id)
                  }
              })
          }
      })
    },
        fetchRooms: function() {
            axios.get(`${config.apiBaseUrl}/rooms/`, {
                headers:{
                    'name': this.authentication.userName, 
                    'password': this.authentication.password,
                    'id': this.id
            }}).then((response) => {
                // log response data to the browser console
                if(response.status==200){
                //console.log(response.data);
                // assign response to data variable
                response.data.forEach((room)=>{
                    if(room.name=="Gemerkte Suchergebnisse"){
                        this.germerkteMoebelRaumId=room.id
                        room.furnitureItems.forEach((furniture)=>{
                            this.savedItems.push(furniture.itestraId)
                        })
                    }
                    this.rooms=response.data
                    this.rooms=this.rooms.filter((room)=>room.name!="Gemerkte Suchergebnisse")
                })
                //console.log(this.$store.getters.isLoggedIn)
                }
            }).catch((error)=>{
                this.showToastMessage("Etwas ist beim Anfragen der gemerkten Möbel schiefgelaufen, bitte probieren Sie es noch einmal",error.message , "warning")
            }); 
    },
    roomMatching: function(){
        let materials=["Stoff", "Leder", "Sonstige"]
        let materialOccurance= [0,0,0]
        let colors=["weiß", "schwarz", "grau", "gelb", "blau", "grün", "rot", "braun", "beige", "Sonstige"]
        let colorOccurance=[0,0,0,0,0,0,0,0,0,0]
        this.rooms.forEach((room)=>{
            //find the right room
            if(room.name==this.selectedRoom){
                //calculate occurance of each material and color
                room.furnitureItems.forEach((item)=>{
                    if(item.material=="Stoff"){
                        materialOccurance[0]++;
                    }else if(item.material=="Leder"){
                        materialOccurance[1]++;
                    }else{
                        materialOccurance[2]++;
                    }
                    switch(item.color){
                        case "weiß":
                            colorOccurance[0]++
                            break;
                        case "schwarz":
                            colorOccurance[1]++
                            break;
                        case "grau":
                            colorOccurance[2]++
                            break;
                        case "gelb":
                            colorOccurance[3]++
                            break;
                        case "blau":
                            colorOccurance[4]++
                            break;
                        case "grün":
                            colorOccurance[5]++
                            break;
                        case "rot":
                            colorOccurance[6]++
                            break;
                        case "braun":
                            colorOccurance[7]++
                            break;
                        case "beige":
                            colorOccurance[8]++
                            break;
                        case "Sonstige":
                            colorOccurance[9]++
                            break;
                    }
                })
            }
        })
        //Find the positions of the highes values
        const maxMaterial =Math.max(...materialOccurance)
        const maxColor= Math.max(...colorOccurance)
        const materialPosition=materialOccurance.indexOf(maxMaterial)
        const colorPosition=colorOccurance.indexOf(maxColor)
        this.enumComboFields.forEach((field)=>{
          //look for field that assignes colors
          if(field.fieldName=="Farbe Bezug"){
              field.validValues.forEach((option)=>{
                  //add color to selected colors
                  if(option.text==colors[colorPosition]){
                      field.selected.push(option.id)
                  }
              })
              //do same with material
          }else if(field.fieldName=="Material Sitzfläche/Bezug"){
              field.validValues.forEach((option)=>{
                  if(option.text==materials[materialPosition]){
                      field.selected.push(option.id)
                  }
              })
          }
      })

    },
        save: function(itestraId){
            let data={"userId":this.id, "itestraId":itestraId, "roomId":this.germerkteMoebelRaumId, "authentication":this.authentication}
            axios.post(`${config.apiBaseUrl}/itestraFurnitures/`, data
                ).then((response) => {
                if(response.status==201){
                // assign response to data variable
                    this.savedItems.push(itestraId)
                }
            }).catch((error)=>{
                this.showToastMessage("Etwas ist beim speichern des Möbelstückes schiefgelaufen.",error.message , "warning")

            }); 
            
        },
        unsave: function(itestraId){
            let header={'headers':{"id":this.id, "roomId":this.germerkteMoebelRaumId, "name":this.authentication.userName, "password":this.authentication.password}}
            axios.delete(`${config.apiBaseUrl}/itestraFurnitures/${itestraId}`, header
                ).then((response) => {
                if(response.status==200){
                // assign response to data variable
                    this.savedItems=this.savedItems.filter((item)=>item!==itestraId)
                }
            }).catch((error)=>{
                this.showToastMessage("Etwas ist beim Löschen des gerkten Möbel schiefgelaufen, bitte probieren Sie es noch einmal",error.message , "warning")
            });
            
        },
        rerunOldSearch: function(){
            this.disableReset=false
            let headers={}
            headers["id"]=this.id
            headers["name"]=this.authentication.userName
            headers["password"]=this.authentication.password
            let searchData={}
            //get data from old search
            axios.get(`${config.apiBaseUrl}/searchquerys/${this.searchId}`, {
                headers}).then((response) => {
                if(response.status==200){
                // assign response to data variable
                    searchData=JSON.parse(response.data.searchTerms)
                    this.proCat=searchData.productCategory
                    this.enumComboFields=searchData.selectOptions
                    this.numberFields=searchData.intervalOptions
                     this.search()
                }
            }).catch((error)=>{
                this.showToastMessage("Etwas ist beim Anfragen der vergangenen Suche schiefgelaufen, bitte probieren Sie es noch einmal",error.message , "danger")
            });
            
        },
        viewDetails: function(id){
            let routeData = this.$router.resolve({ name: 'itemAnsicht', params: { id } });
            window.open(routeData.href, '_blank');
        },
        saveSearch: function(){
            //if no old search is edited
            if(this.searchId==0){
                let data={}
                data["userId"]=this.id
                data["authentication"]=this.authentication
                data["searchTerm"]=JSON.stringify(this.searchString)
                axios.post(`${config.apiBaseUrl}/searchquerys/`, data).catch((error)=>{
                this.showToastMessage("Etwas ist beim Speichern Suche schiefgelaufen.",error.message , "warning")

            });
            //save edited search
            }else{
                let data={}
                data["userId"]=this.id
                data["authentication"]=this.authentication
                data["searchTerm"]=JSON.stringify(this.searchString)
                axios.put(`${config.apiBaseUrl}/searchquerys/${this.searchId}`, data).then((response) => {
                if(response.status==201){
                    this.$store.commit("setSearchId", 0)
                }
            }).catch((error)=>{
                this.showToastMessage("Etwas ist beim Speichern der geänderten Suche schiefgelaufen.",error.message , "warning")
                
            });
            }  
        },
        search: function(){
            this.disableReset=false
            this.requestData= {"page" : 1, "pageSize": 15, "productCategory": this.proCat[0]}
            //Add all field with selected data to searchString 
            this.searchString["selectOptions"]=[]
            this.searchString["intervalOptions"]=[]
            this.searchString["productCategory"]=this.proCat
            this.requestData["selectOptions"]={}
            this.requestData["intervalOptions"]={}
            if(typeof this.enumComboFields !='undefined'){
                            this.enumComboFields.forEach(field =>{
                if(field.selected.length!=0){
                    this.searchString.selectOptions.push(field)
                }
            })
            }
            if (typeof this.numberFields !='undefined'){
                            this.numberFields.forEach(field=>{
               if(field.minValue!=field.selectedMin||field.maxValue!=field.selectedMax){
                    this.searchString.intervalOptions.push(field)
                    //Add intervalOptions to requestData
                    this.requestData.intervalOptions[field.validValues[0].id]=[parseInt(field.selectedMin), parseInt(field.selectedMax)]
                }
            })
            }
            //Add selectOptions to requestData
            if(typeof this.searchString !='undefined'){
                    this.searchString.selectOptions.forEach(input =>{
                    input.validValues=input.validValues.filter((value)=> input.selected.includes(value.id))
                    this.requestData.selectOptions[input.id]=input.selected 
            })
            }

            if(this.isLoggedIn){
                this.saveSearch()
            }
            let loginToken= config.apiName+":"+config.apiPassword
            let Authkey=btoa(loginToken)
            axios.post('https://config.itestra.com/ui/items/filter',this.requestData, {"headers": {"Authorization": "Basic "+Authkey}}).then((response)=>{
                if(response.status==200){
                    this.items=response.data.result.items
                    this.page=response.data.result.currentPage
                    this.itemsFound=response.data.result.total
                    this.sortEnumComb()
                    window.scrollTo(0,0);
                }
            }).catch((error)=>{
                this.showToastMessage("Etwas ist beim Suchen schiefgelaufen, bitte probieren Sie es noch einmal",error.message , "danger")
                this.$router.push("/")
            });
        },
        reset: function(){
            this.disableReset=true
            this.requestData= {"page" : 1, "pageSize": 15, "productCategory": this.proCat[0]}
                let loginToken= config.apiName+":"+config.apiPassword
                let Authkey=btoa(loginToken)
                axios.post('https://config.itestra.com/ui/items/filter',this.requestData, {"headers": {"Authorization": "Basic "+Authkey}}).then((response)=>{
                    if(response.status==200){
                        this.items=response.data.result.items
                        this.page=response.data.result.currentPage
                        this.itemsFound=response.data.result.total
                        this.enumComboFields=[]
                        this.enumFields=[]
                        this.numberFields=[]
                        this.searchString={}
                        response.data.result.filteringUiFieldMetas.forEach(UiElement => {
                            if(UiElement.type=="ENUM_COMBO"){
                                UiElement["selected"]=[];
                                this.enumComboFields.push(UiElement)
                            }else if(UiElement.type=="NUMBER"){
                                UiElement["selectedMin"]=UiElement.minValue
                                UiElement["selectedMax"]=UiElement.maxValue
                                this.numberFields.push(UiElement)
                            }
                        });
                        this.sortEnumComb()
                        window.scrollTo(0,0);
                    }                
                }).catch((error)=>{
                this.showToastMessage("Etwas ist beim Anfragen der vergangenen Suche schiefgelaufen, bitte probieren Sie es noch einmal",error.message, "danger")
                this.$router.push("/")
            });
        },
        nextPage: function(){
                this.requestData.page=this.page+1
                let loginToken= config.apiName+":"+config.apiPassword
                let Authkey=btoa(loginToken)
                axios.post('https://config.itestra.com/ui/items/filter',this.requestData, {"headers": {"Authorization": "Basic "+Authkey}}).then((response)=>{
                    if(response.status==200){
                        this.items=response.data.result.items
                        this.page=response.data.result.currentPage
                        this.itemsFound=response.data.result.total
                        window.scrollTo(0,0);
                    }          
                }).catch((error)=>{
                this.showToastMessage("Etwas ist beim Laden der nächsten Seite schiefgelaufen, bitte probieren Sie es noch einmal",error.message , "danger")
            });
        },
        prevPage: function (){
                this.requestData.page=this.page-1
                let loginToken= config.apiName+":"+config.apiPassword
                let Authkey=btoa(loginToken)
                axios.post('https://config.itestra.com/ui/items/filter',this.requestData, {"headers": {"Authorization": "Basic "+Authkey}}).then((response)=>{
                    if(response.status==200){
                        this.items=response.data.result.items
                        this.page=response.data.result.currentPage
                        this.itemsFound=response.data.result.total
                        window.scrollTo(0,0);
                    }                
                }).catch((error)=>{
                this.showToastMessage("Etwas ist beim Laden der vorigen Seite schiefgelaufen, bitte probieren Sie es noch einmal",error.message , "danger")

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
        },
        sortEnumComb: function(){
            this.enumComboFields.forEach(field =>{
                    field.validValues.sort(function(a,b){
                    if(a.sortPosition<0){
                       a.sortPosition=-1*a.sortPosition
                    }else if(b.sortPosition<0){
                       b.sortPosition=-1*b.sortPosition
                    }
                    if(a.sortPosition<b.sortPosition){
                        return -1
                    }if(a.sortPosition>b.sortPosition){
                        return 1
                    }return 0 
                })
            })
        }
    },
    // executed after the component has been started
    created: function () {
        this.proCat = this.$store.getters.getCategory;
        this.searchId= this.$store.getters.getSearchId;
        this.isLoggedIn=this.loggedIn
        //get saved furniture
        if(this.isLoggedIn){
            this.fetchRooms();
        }
        //if redirected from start page
        if (this.proCat!=""){
            this.fetchSearchResult();
        }//If rederected from Search history page to rerun a search
        else if(this.searchId!=0){
            this.showDismissibleAlert=true
            this.rerunOldSearch();
        }
        //If URL crawling or something went wrong
        else{
            this.$router.push('/')
        }
    }
};
</script>

<style>

.itemLogo{
    height: 150px;
}
.navigator{
    justify-content: center;
    display: flex;
}
.classContent{
    justify-content: right;
}

</style>