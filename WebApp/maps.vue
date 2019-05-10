<template>
    <div class="bg">
        <navBar></navBar>
        <searchBar></searchBar>
    </div>
</template>

<script>
    import navBar from './components/navbar_logedIn.vue'
    import searchBar from './components/searchBar.vue'

    export default {
        components: {
            navBar,
            searchBar,
        }
    }
</script>

<style>
    .bg {
        background: #EEF1F4;
    }
</style>
