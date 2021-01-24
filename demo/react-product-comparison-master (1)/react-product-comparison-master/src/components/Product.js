import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { addProduct, removeProduct } from '../redux/actions';
import image1 from '../setec-logo.png';
import image2 from '../neptun_logo.png'
import {getSimilarToProduct} from "../api/api";



const isBeingCompared = (product, comparedProducts) => {
    console.log(product)
    console.log(comparedProducts)
    return comparedProducts.filter(
            comparedProduct => comparedProduct.product.id === product
        ).length;
};
const areBeingCompared = (product, comparedProducts) => {
    console.log("Glegaj:" + product + " " + product.similarId);
    console.log(comparedProducts)
    console.log(comparedProducts.filter(
        comparedProduct => comparedProduct.id === product
    ).length )
    console.log(comparedProducts.filter(
        comparedProduct => comparedProduct.id === product.similarId
    ).length)
    return comparedProducts.filter(
        comparedProduct => comparedProduct.id === product
    ).length >= 1 && comparedProducts.filter(
        comparedProduct => comparedProduct.id === product.similarId
    ).length >= 0;
};
const appendDescription = (id, name, price, description,url) => {
    return id + "|" + name+"|"+price+"|"+description+"|"+url;
}



const logoPicker = (product) => {
    if (product.url.includes("setec"))
        return image1;
    else return image2;
};

const Product = ({product, onAddToComparison, onRemoveFromComparison, comparedProducts}) =>
    <div className="col-md-7 col-lg-3 col-sm-6">
        <div className="product">
            <div className="card" >
                <div className="image-container">
                    <img className="card-img-top" src={product.imgUrl} alt={product.name} style={{width: 228 + "px", height: 228 + "px", objectFit: "scale-down" }}/>
                </div>
                {/*<div className="overlay"></div>*/}
                {/*<div className="middle">*/}
                {/*    {isBeingCompared(product.id, comparedProducts) ?*/}
                {/*        <button className="btn btn-lg" onClick={() => onRemoveFromComparison(product)}>Отстрани</button> :*/}
                {/*        <button className="btn btn-lg" onClick={() => onAddToComparison(product)}>Спореди</button>*/}
                {/*    }*/}
                {/*</div>*/}
            </div>

            <div className="card-body">
                <div className="row">
                    <ul className="list-group list-group-flush">
                        <li className="list-group-item image-container">
                            <img className="card-img-top" src={logoPicker(product)} alt={product.name} style={{maxWidth: 228 + "px"}}/>
                        </li>
                        <li className="list-group-item">
                            <h5 className="card-title" style={{fontSize: 1.07+"rem"}}>
                                <a href={product.url}>{product.name}</a><br />
                            </h5>
                        </li>
                        <li className="list-group-item">{product.clubPrice}</li>
                        <li className="list-group-item font-weight-bold">{product.price}</li>
                        <li className="list-group-item "><small className="text-muted">{product.pricePartial}</small></li>
                        {/*<li className="list-group-item"><a href={product.similarUrl}>{product.similarName}</a><br /></li>*/}
                        {/*<li className="list-group-item"><button className="btn btn-lg" onClick={() => onAddToComparison(appendDescription(product.similarName, product.similarPrice, product.similarDescription)) &&*/}
                        {/*    onAddToComparison(product) && _onButtonClick()}>Спореди</button></li>*/}
                        {/*<li className="list-group-item">*/}
                        {/*    <h5 className="card-title" style={{fontSize: 1+"rem"}}>*/}
                        {/*        <a href={product.similarUrl}>{product.similarName}</a><br />*/}
                        {/*    </h5>*/}
                        {/*</li>*/}
                        <li className="list-group-item">{comparedProducts.length === 0 ?
                            <button className="btn btn-lg" onClick={() =>  getSimilarToProduct(product.id).then(data => onAddToComparison(data))}>Спореди со најсличниот продукт од другата продавница</button>:
                            <div>
                                <button className="btn btn-lg" disabled={true}>Спореди со најсличниот продукт од другата продавница</button>
                                {isBeingCompared(product.id, comparedProducts)?
                                            <button className="btn btn-lg"
                                                    onClick={() => onRemoveFromComparison(comparedProducts)}>Отстрани
                                                ги продуктите од споредбата</button>

                                            :null

                                    // <button className="btn btn-lg"
                                    //         disabled={true}>Отстрани
                                    //     ги продуктите од споредбата</button>

                                }
                            </div>
                        }</li>
                    </ul>

                </div>
            </div>
        </div>

    </div>
;

Product.propTypes = {
    product : PropTypes.object.isRequired,
    onAddToComparison : PropTypes.func.isRequired,
    onRemoveFromComparison : PropTypes.func.isRequired,
    comparedProducts : PropTypes.array.isRequired
};

const mapStateToProps = state => ({
    comparedProducts : state.products
});

const mapDispatchToProps = dispatch => ({
    onAddToComparison : (products) => dispatch(addProduct(products)),
    onRemoveFromComparison : (product) => dispatch(removeProduct(product))
});



// const _onButtonClick = () => {
//     let el = document.getElementById("tableID");
//     if(el != null){
//         el.style.display = "table";
//         window.scrollTo(0, document.body.scrollHeight)
//     }
//     if(el.innerText === "Изберете продукти за споредба!"){
//         el.style.display = "block";
//     }
//
// }

export default connect(mapStateToProps, mapDispatchToProps)(Product);