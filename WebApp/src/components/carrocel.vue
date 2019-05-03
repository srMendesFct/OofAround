<template>
    <div class="div">
            <b-carousel id="carousel-1" v-model="slide" :interval="4000" controls indicators fade
                style="text-shadow: 1px 1px 2px #333;" @sliding-start="onSlideStart" @sliding-end="onSlideEnd">
                <!-- Text slides with image -->
                <b-carousel-slide class="carousel" caption="First slide" text="Texto 1"
                    img-src="http://www.holidogtimes.com/fr/wp-content/uploads/sites/2/2018/01/koala-visse-poteau-australie-2.jpg?2e4e73">
                </b-carousel-slide>

                <!-- Slides with custom text -->
                <b-carousel-slide class="carousel"
                    img-src="https://www.wilder.pt/wp-content/uploads/2015/07/Wilder_Esquilos_Paulo_Loureiro_3.jpg">
                    <h1>Texto 2</h1>
                </b-carousel-slide>
            </b-carousel>
        <div>
            perguntar como querem por o texto e a imagem
        </div>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                slide: 0,
                sliding: null
            }
        },
        methods: {
            onSlideStart(slide) {
                this.sliding = true
            },
            onSlideEnd(slide) {
                this.sliding = false
            }
        }
    }
</script>

<style>
   .carousel{
       height: 520px;
    }
    .div {
        height: 530px;
    }
</style>
