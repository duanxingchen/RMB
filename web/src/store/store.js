import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)
const state ={
  name: '',
  code: ''
}
const mutations ={
  setName (state, name) {
    state.name = name
  },
  setCode (state, code) {
    state.code = code
  },
}
export default new Vuex.Store({
  state,
  mutations
})

