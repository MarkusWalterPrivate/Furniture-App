<template>
    <div>
        <h3>Vergangene Suchanfragen</h3>
        <div class="column left" style="float:left; width:90%; overflow-wrap:break-word;">
            <b-card-group  class="mt-4" style= "width: 95%" :interval="0">
            <div v-for="search in searches" v-bind:key="search.id" style="width: 95%; margin: 0 auto; ">
                <b-card class="m-3" border-variant="primary">
                    <b-card-title class="text-center bg-primary" style=" margin: 0 auto; border-radius: 5px;">
                        <b>{{search.searchTerms.productCategory[1]}}</b>
                    </b-card-title>
                    <b-card-text>
                        <li v-for="selection in search.searchTerms.selectOptions" v-bind:key="selection.id">
                            <b>{{selection.fieldName}}:</b> 
                            <div class="selectionField" v-for="(value, index) of selection.validValues" v-bind:key="value.id"> 

                                {{value.text}}<div class="selectionField" v-if="index!=selection.selected.length-1">,</div> 

                            </div>
                        </li>
                        <li v-for="input in search.searchTerms.intervalOptions" v-bind:key=" input.id">
                            <b>{{input.fieldName}}</b>
                            Minimum {{input.selectedMin}}mm, Maximum {{input.selectedMax}}mm
                        </li>
                       
                    </b-card-text>
                    <b-card-footer class="text-center">
                          &nbsp;
                        <b-button
                            size="sm"
                            variant="primary"
                            v-on:click=" saveId(search.id); "
                            justify="space around"
                            >Suche wiederholen oder bearbeiten</b-button>&nbsp;
                        <b-button 
                            size="sm"
                            variant="outline-danger"
                            v-on:click="deleteSearch(search.id)"
                            justify= "space around"
                        ><b-icon-trash></b-icon-trash> Löschen</b-button>
                    </b-card-footer>
                </b-card>
            </div>
        
            </b-card-group>
        </div>
        <div class="column right " style="float:right; width:10%; overflow-wrap:break-word;"> 
            <b-button variant="primary" v-on:click="sortSearches()"><b-icon-arrow-down-up/> Reihengolge</b-button><br><br>
            <b-button v-if="searches.length!=0" variant="outline-danger" v-on:click="deleteAllSearches()"><b-icon-trash></b-icon-trash>Alle Suchen löschen</b-button>
            
        </div>

        
    </div>
</template>
<script>
import axios from "axios";
import config from "@/config";
export default {

    data(){
        return{
            searches:[],
            userId:0,
            userName:"",
            password:"",
            lastSearchFirst:false

        }
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
        getSearches: function(){
            axios.get(`${config.apiBaseUrl}/searchquerys/`, {
                headers:{
                    'name': this.userName, 
                    'password': this.password,
                    'id': this.userId
            }}).then((response) => {
                // log response data to the browser console
                if(response.status==200){
                //console.log(response.data);
                // assign response to data variable
                    this.searches=response.data
                    this.searches.forEach(search => {
                        search.searchTerms=JSON.parse(search.searchTerms)
                        //console.log(search.searchTerms.productCategory)
                    });
                     //console.log(this.$store.getters.isLoggedIn)
                }
            }).catch((error)=>{
                this.showToastMessage("Etwas ist beim Anfragen der vergangenen Suchen schiefgelaufen, bitte probieren Sie es noch einmal",error.message , "danger")
            });
        },
        deleteAllSearches: function(){
            axios.delete(`${config.apiBaseUrl}/searchquerys/`,{
                headers:{
                    'name': this.userName, 
                    'password': this.password,
                    'id': this.userId
            }}).then((response) => {
                if(response.status==200){
                    this.searches = []
                    this.showToastMessage("Erfolg",
                    "Suchanfragen wurden gelöscht!",
                    "success"
                    );
                }
            }).catch((error)=>{
                this.showToastMessage("Etwas ist beim Löschen der aller Suchen schiefgelaufen, bitte probieren Sie es noch einmal",error.message , "danger")
                
            });
        },
        sortSearches: function(){
            if(this.lastSearchFirst==false){
                this.searches.sort((a,b)=> b.id-a.id);
                this.lastSearchFirst=true
            }else{
                this.searches.sort((a,b)=> a.id-b.id);
                this.lastSearchFirst=false;
            }
        },
        saveId: function(id){
            this.$store.commit("setCategory","")
            this.$store.commit("setSearchId", id)
            this.$router.push("Suche")
        },
        deleteSearch: function(id){
            axios.delete(`${config.apiBaseUrl}/searchquerys/${id}`,{
                headers:{
                    'name': this.userName, 
                    'password': this.password,
                    'id': this.userId
            }}).then((response) => {
                if(response.status==200){
                    this.searches = this.searches.filter((search) => search.id !== id);
                    this.showToastMessage("Erfolg",
                    "Suchanfrage wurde gelöscht!",
                    "success"
        );
                }
            }).catch((error)=>{
                this.showToastMessage("Etwas ist beim Löschen der vergangenen Suche schiefgelaufen, bitte probieren Sie es noch einmal",error.message , "danger")
                this.$router.push("SuchHistorie")
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
    created: function() {
        this.userId=this.id
        this.password=this.authentication.password
        this.userName=this.authentication.userName
        this.getSearches()

    }
    
}
</script>
<style>
.selectionField{
    display:inline;
}
.left {
    float: left;
    width: 95%;
}
.right {
    float: right;
    width: 5%;
    overflow-wrap:break-word;
}

</style>