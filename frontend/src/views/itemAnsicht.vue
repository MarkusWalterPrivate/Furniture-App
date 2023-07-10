<template>
    <div class="item">
    <div v-if="display">
        <b-carousel  controls indicators class="tales">
            <b-carousel-slide class="carousel-inner" v-for="(image, index) in this.item.result.imageUrls" v-bind:key="index" :img-src="image">
            </b-carousel-slide>
        </b-carousel>
        <h3>{{item.result.name}}</h3>
        <div class="configurations">
            <li v-for="field in configurations" v-bind:key="field.id">
                <b>{{field.fieldName}}:</b> 
                <div class="selectionField" v-for="(value, index) of field.validValues" v-bind:key="value.id"> 
                    {{value.text}}<div class="selectionField" v-if="index!=field.validValues.length-1">,</div> 

                </div>
            </li>
            <li>
                <b>Hersteller-Homepage: </b>
                    <div v-if="item.result.name.includes('erpo')" class="selectionField">
                        <b-link target="_blank" href="https://www.erpo.de/de/">erpo</b-link>
                    </div>
                    <div v-if="item.result.name.includes('Musterring')" class="selectionField">
                        <b-link target="_blank" href="https://www.musterring.com/de-DE/home/">Musterring</b-link>
                    </div>
                    <div v-if="item.result.name.includes('Candy')" class="selectionField">
                        <b-link target="_blank" href="https://www.candy-polstermoebel.de/">Candy Polstermöbel GmbH</b-link>
                    </div>
                    <div v-if="item.result.name.includes('Koinor')" class="selectionField">
                        <b-link target="_blank" href="https://www.koinor.com/">Koinor Polstermöbel GmbH &amp; Co.</b-link>
                    </div>
                    <div v-if="item.result.name.includes('Rolf Benz')" class="selectionField">
                        <b-link target="_blank" href="https://www.rolf-benz.com/de_DE/">Rolf Benz AG &amp; Co.</b-link>
                    </div>
                    <div v-if="item.result.name.includes('W.Schillig')" class="selectionField">
                        <b-link target="_blank" href="https://www.schillig.de/">W.Schillig</b-link>
                    </div>
            </li>
        </div>
    </div>
    </div>
</template>


<script>
import axios from "axios";
import config from "@/config";
export default {
    data() {
        return{
            item:{},
            display:false,
            configurations:[]
        }
        
    },
    methods:{
        getItem: function(id){
            let loginToken= config.apiName+":"+config.apiPassword
            let Authkey=btoa(loginToken)
            axios.get(`https://config.itestra.com/ui/items/${id}`, {headers: {"Authorization": "Basic "+Authkey}}).then((response)=>{
                if(response.status==200){ 
                    this.item=response.data
                    this.item.uiMetaEditable.fieldMeta.forEach(field => {
                        //check if field has user-readable name
                        if('fieldName' in field){
                            this.configurations.push(field)
                        }
                    });
                    this.display=true
                }
            }).catch(error=>{
          this.showToastMessage("Etwas ist schiefgelaufen, bitt probieren Sie es nocheinmal.", error.response.data.errors, "danger")
        
        });
        }

    },
    created: function(){
        this.getItem(this.$route.params.id)
    }
}
</script>
<style>
.item{
    display: flex;
    justify-content: center;
}
.tales {
  max-width: 50% ;
 
}
.carousel-inner{
  width:100%;
  max-height: 500px !important;
}
.selectionField{
    display:inline;
}
</style>
