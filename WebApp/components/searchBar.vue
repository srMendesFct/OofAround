<template>
    <div class="size">
        <b-row class="pos">
            <b-col>
            </b-col>
                <b-dropdown id="dropdown-1" text="--Escolha um distrito--" class="m-md-2">
                    <b-dropdown-item>Aveiro</b-dropdown-item>
                    <b-dropdown-item>Beja</b-dropdown-item>
                    <b-dropdown-item>Braga</b-dropdown-item>
                    <b-dropdown-item>Bragança</b-dropdown-item>
                    <b-dropdown-item>Castelo Branco</b-dropdown-item>
                    <b-dropdown-item>Coimbra</b-dropdown-item>
                    <b-dropdown-item>Évora</b-dropdown-item>
                    <b-dropdown-item>Faro</b-dropdown-item>
                    <b-dropdown-item>Guarda</b-dropdown-item>
                    <b-dropdown-item>Leiria</b-dropdown-item>
                    <b-dropdown-item>Lisboa</b-dropdown-item>
                    <b-dropdown-item>Portalegre</b-dropdown-item>
                    <b-dropdown-item>Porto</b-dropdown-item>
                    <b-dropdown-item>Santarém</b-dropdown-item>
                    <b-dropdown-item>Setúbal</b-dropdown-item>
                    <b-dropdown-item>Viana do Castelo</b-dropdown-item>
                    <b-dropdown-item>Vila Real</b-dropdown-item>
                    <b-dropdown-item>Viseu</b-dropdown-item>
                </b-dropdown>
                <b-dropdown id="dropdown-1" text="--Categorias--" class="m-md-2">
                    <b-dropdown-item>Lazer</b-dropdown-item>
                    <b-dropdown-item>NightOut</b-dropdown-item>
                </b-dropdown>
        </b-row>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                text: ''
            }
        }
    }
</script>

<style>
    .size {
        width: 800px;
        height: 100px;
    }
    .pos {
        margin-top: 35px;
    }

</style>
