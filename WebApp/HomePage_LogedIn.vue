<template>
  <div id="app" class="bg">
    <navBar></navBar>
    <carousel></carousel>
    <textImage></textImage>
    <topPercursos msg = "Ver Mais"></topPercursos>
    <monuments></monuments>
    <aboutUs></aboutUs>
  </div>
</template>

<script>
  import carousel from './components/carousel.vue'
  import navBar from './components/navbar_logedIn.vue'
  import textImage from './components/text_image.vue'
  import topPercursos from './components/top_percursos.vue'
  import aboutUs from './components/about_us_sc.vue'
  import monuments from './components/monuments.vue'

  export default {
    name: 'homepage_logedIn',
    components: {
      carousel,
      navBar,
      textImage,
      topPercursos,
      aboutUs,
      monuments,
    }
  }
</script>

<style>
 .bg{
        background: #EEF1F4;
    }
</style>
