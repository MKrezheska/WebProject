export const loadProducts = () => {
    return fetch('http://localhost:8080/products').then(res => res.json());
};