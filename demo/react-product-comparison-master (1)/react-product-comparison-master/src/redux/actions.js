import * as constants from './constants';

export const addProduct = (product) => ({
    type : constants.ADD_PRODUCT,
    product
});

export const removeProduct = (product) => ({
    type : constants.REMOVE_PRODUCT,
    productId : product.id
});