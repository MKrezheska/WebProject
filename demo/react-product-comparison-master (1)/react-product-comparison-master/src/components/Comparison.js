import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

const Comparison = ({products}) =>
    products.length >= 2 ?
        <div className="row">
            <table className="table">
                <thead>
                    <tr>
                        <th className="table-light">&nbsp;</th>
                        { products.map(product => <th key={`th-${product.id}`} className="text-center table-light">{product.name}</th>) }
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <th className="table-light">Price</th>
                        { products.map(product => <td key={`url-td-${product.id}`} className="text-center table-light">{product.price}</td>) }
                    </tr>
                    <tr>
                        <th className="table-light">Description</th>
                        { products.map(product => <td id="tab-description" key={`description-td-${product.id}`} className="text-center table-light">{addLineBreaks(product)}</td>) }
                    </tr>

                </tbody>
            </table>
        </div> :
        null
;

Comparison.propTypes = {
    products : PropTypes.array.isRequired
};

const mapStateToProps = state => ({
    products : state.products
});

const addLineBreaks = (product) => {
    if (product.description.includes(product.name))
        return product.description.split('|').slice(1).map((text, index) => (
            <React.Fragment key={`${text}-${index}`}>
                {text}
                <br/>
            </React.Fragment>
        ));
    else
     return product.description.split('|').map((text, index) => (
        <React.Fragment key={`${text}-${index}`}>
            {text}
            <br/>
        </React.Fragment>
    ));
};


export default connect(mapStateToProps)(Comparison);