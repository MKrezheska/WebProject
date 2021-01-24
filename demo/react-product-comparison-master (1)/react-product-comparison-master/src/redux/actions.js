import * as constants from './constants';

export const addProduct = (products) => ({
    type : constants.ADD_PRODUCT,
    products
});

export const removeProduct = (products) => ({
    type : constants.REMOVE_PRODUCT,
    products
});

export const updateProduct = (mostSimilarId) => ({
    type : constants.UPDATE_PRODUCT,
    mostSimilarId
});