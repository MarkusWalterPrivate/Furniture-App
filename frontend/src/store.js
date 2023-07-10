import Vue from "vue";
import Vuex from "vuex";


Vue.use(Vuex);

export default new Vuex.Store({
  
    state:{
      id: 0,
      authentication: {
      },
      searchQuerys: [],
      rooms: [],
      loggedIn: false,
      response: "",
      category:"",
      rerunSearchId:0,
      itemId:0
    }, 
    mutations: {
      login (state, user){
        state.loggedIn =true
        state.id =user.id
        state.rooms =user.rooms
        state.searchQuerys = user.searchQuerys
        state.authentication =user.authentication
      },
      logout (state){
        state.loggedIn= false
        state.id =-1
        state.authentication= {}
        state.searchQuerys =[]
        state.rooms=[]
      },
      udateRooms (state, newRooms){
        state.rooms =newRooms
      },
      updateSearchQuerys (state, newSearchQuerys){
        state.searchQuerys= newSearchQuerys
      },
      updateAuthentication (state, newAuthentication){
        state.authentication =newAuthentication
      },
      setResponse (state, response){
        state.response=response
      },
      setCategory (state, category){
        state.category=category
      },
      setSearchId (state, id){
        state.rerunSearchId=id
      },
      setItemId(state, id){
        state.itemId=id
      }
    },
    getters: {
      isLoggedIn: state =>{
        return state.loggedIn
      },
      getAuthenticationData: state => {
        return state.authentication
      },
      getUserId: state => {
        return state.id
      },
      getUserName: state =>{
        return state.authentication.userName
      },
      getResponse: state=>{
        if (state.response=='undefined'){
          return
        }
        return state.response
      },
      getCategory: state=>{
        return state.category
      },
      getSearchId: state=>{
        return state.rerunSearchId
      },
      getItemId: state=>{
        return state.itemId
      }
    }
  
})