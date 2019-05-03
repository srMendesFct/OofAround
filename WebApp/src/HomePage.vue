<template>
  <div id="app" class="bg">
    <navBar></navBar>
    <carousel></carousel>
    <textImage></textImage>
    <topPercursos></topPercursos>
    <aboutUs></aboutUs>
  </div>
</template>

<script>
  import carousel from './components/carrocel.vue'
  import navBar from './components/navebar.vue'
  import textImage from './components/text_image.vue'
  import topPercursos from './components/top_percursos.vue'
  import aboutUs from './components/about_us_sc.vue'

  export default {
    name: 'homepage',
    components: {
      carousel,
      navBar,
      textImage,
      topPercursos,
      aboutUs,
    }
  }
</script>

<style>
 .bg{
        background: #EEF1F4;
    }
</style>