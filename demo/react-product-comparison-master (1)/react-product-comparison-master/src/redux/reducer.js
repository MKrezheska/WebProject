import * as constants from './constants';

const initialState = {
    products : []
};

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case constants.ADD_PRODUCT:
            let products_1 = [...state.products, action.product];
            if(products_1.length <= 2) {
                if (products_1.length > 1) {
                    let el = document.getElementById("tableID");
                    if (el != null) {
                        el.style.display = "table";
                        window.scrollTo(0, document.body.scrollHeight)
                    }
                    if (el.innerText === "Изберете продукти за споредба!") {
                        el.style.display = "block";
                    }
                }
                return {...state, products: products_1};
            }
            return {...state, products: state.products};

        case constants.REMOVE_PRODUCT:
            const idx = state.products.findIndex(product => product.id === action.productId);
            let products = [...state.products];
            if (products.length <= 1) {
                let el = document.getElementById("tableID");
                    el.style.display = "none";
            }
            products.splice(idx, 1);
            return {...state, products};
        default : return state;
    }
};

export default reducer;