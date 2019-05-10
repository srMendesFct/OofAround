import {https, http} from './config.js'

export default {

    list:() => {
        return http.get('products')
    }
}