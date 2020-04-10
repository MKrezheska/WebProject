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
                        <th className="table-light">Url</th>
                        {/*{ products.map(product => <td key={`url-td-${product.id}`} className="text-center table-light">{product.url}</td>) }*/}
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

export default connect(mapStateToProps)(Comparison);