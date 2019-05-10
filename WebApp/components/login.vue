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
  import product from "../services/products.js";
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
    /*mounted() {
      product.list().then(responde => {
        console.log(response)
      })
    },*/
    methods: {
      onSubmit(evt) {
        event.preventDefault();
        console.log(JSON.stringify(this.form));
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
