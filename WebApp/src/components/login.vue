<template>
    <div>
        <div>
            <b-form @submit="onSubmit" @reset="onReset" v-if="show">
                <b-form-group id="input-group-1" label="Username or email:" label-for="input-1">
                    <b-form-input id="input-1" v-model="form.user" type="text" required placeholder="Username or Email">
                    </b-form-input>
                </b-form-group>

                <b-form-group id="input-group-2" label="Password:" label-for="input-2">
                    <b-form-input id="input-2" v-model="form.password" required placeholder="Password" type="password">
                    </b-form-input>
                </b-form-group>

                <b-form-group id="input-group-4">
                    <b-form-checkbox-group v-model="form.checked" id="checkboxes-4">
                        <b-form-checkbox value="me">Remember me</b-form-checkbox>
                    </b-form-checkbox-group>
                </b-form-group>

                <b-button type="submit" variant="primary">Submit</b-button>
                <b-button type="reset" variant="danger">Reset</b-button>
            </b-form>
        </div>
        <div class="register">
            Ainda não tens conta?
            <div>
                <b-button v-b-modal.modal>Regista-te</b-button>
                <b-modal id="modal" title="Registo" hide-footer>
                    <register></register>
                </b-modal>
            </div>
        </div>
    </div>
</template>


<script>
    import register from "./register.vue";

    export default {
        name: "login",
        components: {
            register,
        },
        data() {
            return {
                form: {
                    user: '',
                    password: '',
                    checked: []
                },
                show: true
            }
        },
        methods: {
            onSubmit(evt) {
                $.each($('form[name="change_pass"]').serializeArray(), function (i, field) {
                    this.form[field.name] = field.value;
                });
                console.log(JSON.stringify(values));
                $.ajax({
                    type: "POST",
                    url: "https://oofaround.appspot.com/login",
                    contentType: "application/json;charset=utf-8",
                    dataType: 'json', // data type        
                    crossDomain: true,
                    success: function (Response) {
                        alert("Loged in successfully.");
                    },
                    error: function (Response) {
                        console.log(Response);
                        if (Response.status == 200) {
                            alert("Loged in successfully.");
                            window.location.href =
                            "http://localhost:8080/HomePage.vue";
                        } else {
                            alert("Login failed.");
                        }
                    },
                    data: JSON.stringify(this.form) // post data || get data
                });
                event.preventDefault();

            },
            onReset(evt) {
                evt.preventDefault()
                this.form.user = ''
                this.form.password = ''
                this.form.checked = []
                this.show = false
                this.$nextTick(() => {
                    this.show = true
                })
            },
        }
    }
</script>

<style>
    .register {
        text-align: center;
    }
</style>
