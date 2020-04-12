import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { addProduct, removeProduct } from '../redux/actions';
import image1 from '../setec-logo.png';
import image2 from '../neptun_logo.png'

const isBeingCompared = (product, comparedProducts) => {
    return comparedProducts.filter(
            comparedProduct => comparedProduct.id === product.id
        ).length;
};

const logoPicker = (product) => {
    if (product.url.includes("setec"))
        return image1;
    else return image2;
};

const Product = ({product, onAddToComparison, onRemoveFromComparison, comparedProducts}) =>
    <div className="col-md-6 col-lg-3 col-sm-6">
        <div className="product">
            <div className="card" >
                <div className="image-container">
                    <img className="card-img-top" src={product.imgUrl} alt={product.name} style={{width: 228 + "px", height: 228 + "px", objectFit: "scale-down" }}/>
                </div>
                <div className="overlay"></div>
                <div className="middle">
                    {isBeingCompared(product, comparedProducts) ?
                        <button className="btn btn-lg" onClick={() => onRemoveFromComparison(product)}>Remove</button> :
                        <button className="btn btn-lg" onClick={() => onAddToComparison(product)}>Compare</button>
                    }
                </div>
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
    onAddToComparison : (product) => dispatch(addProduct(product)),
    onRemoveFromComparison : (product) => dispatch(removeProduct(product))
});

export default connect(mapStateToProps, mapDispatchToProps)(Product);