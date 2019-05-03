<template>
    <div>
        <div>
            <b-navbar toggleable="lg" type="dark" variant="info">
            <b-navbar-brand href="#">
                <img src="https://placekitten.com/g/30/30" img-width="50">
            </b-navbar-brand>

            <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

            <b-collapse id="nav-collapse" is-nav>
                <b-navbar-nav>
                    <b-nav-item href="#">Home</b-nav-item>
                    <b-nav-item href="#">Percursos</b-nav-item>
                    <b-nav-item href="#">Mapas</b-nav-item>
                    <b-nav-item href="#">Sobre nós</b-nav-item>
                </b-navbar-nav>

                <!-- Right aligned nav items -->
                <b-navbar-nav class="ml-auto">
                    <b-nav-form>
                        <b-form-input size="sm" class="mr-sm-2" placeholder="Search"></b-form-input>
                        <b-button size="sm" class="my-2 my-sm-0" type="submit">Search</b-button>
                    </b-nav-form>

                    <b-nav-item-dropdown text="Idioma" right>
                        <b-dropdown-item href="#">PT</b-dropdown-item>
                        <b-dropdown-item href="#">EN</b-dropdown-item>
                    </b-nav-item-dropdown>

                    <b-nav-item-dropdown right>
                        <!-- Using 'button-content' slot -->
                        <template slot="button-content"><em>Login</em></template>
                        <b-dropdown-item href="#">Login</b-dropdown-item>
                        <b-dropdown-item href="#">Registar</b-dropdown-item>
                    </b-nav-item-dropdown>
                </b-navbar-nav>
            </b-collapse>
        </b-navbar>
        </div>
        <div>
            ajustar slider
        </div>
    </div>
</template>