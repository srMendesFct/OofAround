<template>
    <div class="marosca">
            <b-form @submit="onSubmit1" @reset="onReset1" v-if="show1">
                <b-form-group id="input-group1" label="Username or email:" label-for="input1">
                    <b-form-input id="input1" v-model="form.user" type="text" required placeholder="Username or Email">
                    </b-form-input>
                </b-form-group>

                <b-form-group id="input-group2" label="Password:" label-for="input2">
                    <b-form-input id="input2" v-model="form.password" required placeholder="Password" type="password">
                    </b-form-input>
                </b-form-group>

                <b-form-group id="input-group4">
                    <b-form-checkbox-group v-model="form.checked" id="checkboxes4">
                        <b-form-checkbox value="me">Remember me</b-form-checkbox>
                    </b-form-checkbox-group>
                </b-form-group>

                <b-button type="submit" variant="primary">Submit</b-button>
                <b-button type="reset" variant="danger">Reset</b-button>
            </b-form>
    </div>
</template>

<script>
    export default {
        name: "register",
        components: {
            
        },
        data() {
            return {
                form: {
                    email: '',
                    password: '',
                    checked: []
                },
                show1: true
            }
        },
        methods: {
            onSubmit1(evt) {
                evt.preventDefault()
                console.log(JSON.stringify(this.form))
                alert("Registered with success.")
                window.location.href="http://localhost:8080/HomePage.vue";
            },
            onReset1(evt) {
                evt.preventDefault()
                // Reset our form values
                this.form.user = ''
                this.form.password = ''
                this.form.checked = []
                // Trick to reset/clear native browser form validation state
                this.show1 = false
                this.$nextTick(() => {
                    this.show1 = true
                })
            }
        }
    }
</script>

<style>
    .marosca {
        text-align: left;
    }
</style>

