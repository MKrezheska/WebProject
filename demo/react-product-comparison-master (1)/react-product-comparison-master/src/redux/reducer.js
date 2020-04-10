import * as constants from './constants';

const initialState = {
    products : []
};

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case constants.ADD_PRODUCT:
            return {...state, products : [...state.products, action.product]};
        case constants.REMOVE_PRODUCT:
            const idx = state.products.findIndex(product => product.id === action.productId);
            let products = [...state.products];
            products.splice(idx, 1);
            return {...state, products};
        default : return state;
    }
};

export default reducer;