export const loadProducts = () => {
    return fetch('http://localhost:8080/products').then(res => res.json());
};
export const searchProducts = (display, graphicCard, internalMemory, memory, processor, resolution) => {
    return fetch(`http://localhost:8080/products/filter?display=${display}&graphicsCard=${graphicCard}&internalMemory=${internalMemory}&memory=${memory}&processor=${processor}&resolution=${resolution}`).then(res => res.json());
}
