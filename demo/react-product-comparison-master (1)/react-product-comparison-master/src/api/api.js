export const loadProducts = () => {
    return fetch('http://localhost:8080/products').then(res => res.json());
};
export const searchProducts = (display, graphicCard, internalMemory, memory, processor, resolution) => {
    return fetch(`http://localhost:8080/products/filter?display=${display}&graphicsCard=${graphicCard}&internalMemory=${internalMemory}&memory=${memory}&processor=${processor}&resolution=${resolution}`).then(res => res.json());
}
export const getSimilarToProduct = (id) => {
    return fetch(`http://localhost:8080/products/similar?id=${id}`).then(res => res.json());
}

export const setMostSimilarProduct = (id, s) => {
    return fetch(`http://localhost:8080/products/update?id=${id}&s=${s}`,{
            method: 'PATCH',
    }).then(res => res.json());
}



