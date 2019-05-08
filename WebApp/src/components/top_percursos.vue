<template>
    <div>
        <div>
            <b-container>
                <b-row md="3" class="ml-md-auto p-3">
                    <b-col  >
                        <b-card title="Quer criar um percurso?" img-src="https://picsum.photos/600/300/?image=25"
                            img-alt="Image" img-top tag="article" style="max-width: 20rem;" class="mb-2">
                            <b-card-text>
                                Aproveite já!
                            </b-card-text>
                            <b-button href="#" variant="primary">Novo Percurso</b-button>
                        </b-card>
                    </b-col>
                    <b-col align="center">
                        <h1>Percursos Recomendados</h1>
                        <b-table hover :items="items" :fields="fields" :sort-by.sync="sortBy" :sort-desc.sync="sortDesc"></b-table>
                        <b-button v-b-toggle.collapse-1 variant="primary">{{msg}}</b-button>
                        <b-collapse id="collapse-1" class="mt-2">
                            <b-table hover :items="items2" :fields="fields" :sort-by.sync="sortBy" :sort-desc.sync="sortDesc"></b-table>
                        </b-collapse>
                    </b-col>
                </b-row>
            </b-container>
        </div>
        <div>
            perguntar ao mimoso o que vem a seguir disto e antes do about us. mudar o ver mais para ver menos na tabela.
        </div>
    </div>
</template>

<script>
    export default {
        props: {
            msg : String,
        },
        methods: {
            
        },
        data() {
            return {
                sortBy: 'Posição',
                sortDesc: false,
                fields: [{
                        key: 'Posição',
                        sortable: true
                    },
                    {
                        key: 'Nome',
                        sortable: true
                    },
                    {
                        key: 'Distrito',
                        sortable: true
                    },
                    {
                        key: 'Percorrido',
                        sortable: false
                    }
                ],
                items: [{
                        Posição: 1,
                        Nome: 'Belem Tour',
                        Distrito: 'Lisboa',
                        Percorrido: '100 vezes',
                        _rowVariant: 'danger'
                    },
                    {
                        Posição: 2,
                        Nome: 'Margens do Douro',
                        Distrito: 'Porto',
                        Percorrido: '52 vezes',
                        _rowVariant: 'warning'
                    },
                    {
                        Posição: 3,
                        Nome: 'Cidade Berço',
                        Distrito: 'Guimarães',
                        Percorrido: '30 vezes',
                        _rowVariant: 'success'
                    },
                ],
                items2: [{
                        Posição: 4,
                        Nome: 'Praias da Costa',
                        Distrito: 'Setúbal',
                        Percorrido: '15 vezes',
                        _rowVariant: 'danger'
                    },
                    {
                        Posição: 5,
                        Nome: 'Inferno da Luz',
                        Distrito: 'Lisboa',
                        Percorrido: '7 vezes',
                        _rowVariant: 'warning'
                    },
                ]
            }
        }
    }
</script>