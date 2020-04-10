import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { addProduct, removeProduct } from '../redux/actions';

const isBeingCompared = (product, comparedProducts) => {
    return comparedProducts.filter(
            comparedProduct => comparedProduct.id === product.id
        ).length;
};

const Product = ({product, onAddToComparison, onRemoveFromComparison, comparedProducts}) =>
    <div className="col-12 col-md-6 col-lg-3">
        <div className="product">
            <div className="card">
                <img className="card-img-top" src={product.imgUrl} alt={product.name} />
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
                    <div className="col-8">
                        <h5 className="card-title">
                            {product.name}<br />
                        </h5>
                    </div>

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