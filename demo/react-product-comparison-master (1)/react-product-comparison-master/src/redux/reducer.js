import * as constants from './constants';
import * as R from 'ramda';

const initialState = {
    products: []
};

const reducer = (state = initialState, action) => {
    switch (action.type) {

        case constants.ADD_PRODUCT:
            let products_3 = [...state.products].concat(action.products);
            console.log(products_3)
            if (products_3.length <= 3) {
                if (products_3.length > 0) {
                    let el = document.getElementById("tableID");
                    if (el != null) {
                        el.style.display = "table";
                        window.scrollTo(0, document.body.scrollHeight)
                    }
                    if (el.innerText === "Изберете продукти за споредба!") {
                        el.style.display = "block";
                    }
                }
                console.log(products_3)
                return {...state, products: products_3};
            }
            return {...state, products: state.products};


        case constants.REMOVE_PRODUCT:
            let products_2 = [];
            state.products.forEach(product => action.products.indexOf(product) === -1 ? products_2.push(product) : null);
            console.log('reducer', products_2)
            if (products_2.length <= 1) {
                let el = document.getElementById("tableID");
                el.style.display = "none";
            }
            return {...state, products: products_2};
        default :
            return state;
    }
};

export default reducer;