import React, {useState} from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import {setMostSimilarProduct} from "../api/api";
import {updateProduct} from "../redux/actions";


const Comparison = ({products, onUpdateSimilar}) => {
    const [count, setCount] = useState(0)
    const onClickHandler = e  => {
        setCount(prevCount => prevCount + 1)
    }

    if(products.length >= -1) {
        let currentProduct = products[0];
        let displayChooseMostSimilarButton = false;
        if (currentProduct !== undefined) {
            displayChooseMostSimilarButton = currentProduct.mostSimilarId === -1;}
        console.log("current", currentProduct)
        return(<div className="row">
            <table id="tableID" className="table">
                <thead>
                <tr>
                    <th className="table-light">&nbsp;</th>
                    {products.map(product => product.product.id !== currentProduct.mostSimilarId ? <th key={`th-${getId(product.product)}`}
                                                 className="text-center table-light">{getName(product.product)}</th>:
                        <th key={`th-${getId(product.product)}`}
                            className="text-center table-active">{getName(product.product)}</th>
                    )}
                </tr>
                </thead>
                <tbody>
                <tr>
                    <th className="table-light">Цена</th>
                    {products.map(product => <td key={`url-td-${getId(product.product)}`}
                                                 className="text-center table-light">{getPrice(product.product)}</td>)}
                </tr>
                <tr>
                    <th className="table-light">Дисплеј</th>
                    {products.map(product => <td id="tab-description" key={`description-td-${getId(product.product)}`}
                                                 className="text-center table-light">{getDisplay(product.product)}</td>)}
                </tr>
                <tr>
                    <th className="table-light">Резолуција</th>
                    {products.map(product => <td id="tab-description" key={`description-td-${getId(product.product)}`}
                                                 className="text-center table-light">{getResolution(product.product)}</td>)}
                </tr>
                <tr>
                    <th className="table-light">Графичка картичка</th>
                    {products.map(product => <td id="tab-description" key={`description-td-${getId(product.product)}`}
                                                 className="text-center table-light">{getGCard(product.product)}</td>)}
                </tr>
                <tr>
                    <th className="table-light">Внатрешна меморија</th>
                    {products.map(product => <td id="tab-description" key={`description-td-${getId(product.product)}`}
                                                 className="text-center table-light">{getInternal(product.product)}</td>)}
                </tr>
                <tr>
                    <th className="table-light">Меморија</th>
                    {products.map(product => <td id="tab-description" key={`description-td-${getId(product.product)}`}
                                                 className="text-center table-light">{getMemory(product.product)}</td>)}
                </tr>
                <tr>
                    <th className="table-light">Процесор</th>
                    {products.map(product => <td id="tab-description" key={`description-td-${getId(product.product)}`}
                                                 className="text-center table-light">{getProcessor(product.product)}</td>)}
                </tr>

                {displayChooseMostSimilarButton ?
                    <tr>
                        <th className="table-light">Избери најсличен продукт</th>
                        {products.map(product => <td id="tab-description"
                                                     key={`description-td-${getId(product.product)}`}
                                                     className="text-center table-light">
                            {   currentProduct.product.id !== product.product.id ?
                                <button type="button" className="btn btn-info"
                                        onClick={() => setMostSimilarProduct(currentProduct.product.id, product.product.id).then(data => data) && onUpdateSimilar(product.product.id) && onClickHandler()}>Избери</button>
                                : null
                            }
                        </td>)}
                    </tr>: <tr><td colSpan={4} className="text-info small"><i>Потенцираниот производ е избран за најсличен.</i></td></tr>
                }

                </tbody>
            </table>
        </div>) } else {
        return(<div id="tableID" className="text-info text-center">Изберете продукти за споредба!</div>)}

};

Comparison.propTypes = {
    products : PropTypes.array.isRequired,
    onUpdateSimilar : PropTypes.func.isRequired,
};

const mapStateToProps = state => ({
    products : state.products
});
const mapDispatchToProps = dispatch => ({
    onUpdateSimilar: (mostSimilarId) => dispatch(updateProduct(mostSimilarId))
});

const getName = (product) => {
    if(product.name !== undefined)
        return product.name;
    else return product.split('|').map((text, index) => (
        index === 1 ?
            <React.Fragment key={`${text}-${index}`}>
                {text}
                <br/>
            </React.Fragment> : null

    ));

};

const getId = (product) => {
    console.log(product.id + " " + product.similarId)
    if(product.id !== undefined)
        return product.id;
    else return product.similarId;

};

const getPrice = (product) => {
    if(product.price !== undefined)
        return product.price;
    else return product.split('|').map((text, index) => (
        index === 2 ?
            <React.Fragment key={`${text}-${index}`}>
                {text}
                <br/>
            </React.Fragment> : null

    ));

};

const getDisplay = (product) => {
    if(product.price !== undefined) {
        return product.description.split(',').map((text, index) => (
            index === 0 ?
                <React.Fragment key={`${text}-${index}`}>
                    {text}
                    <br/>
                </React.Fragment> : null

        ));
    } else {
        let description = product.split('|')[3];
        return description.split(',').map((text, index) => (
            index === 0 ?
                <React.Fragment key={`${text}-${index}`}>
                    {text}
                    <br/>
                </React.Fragment> : null

        ));
    }

};

const getProcessor = (product) => {
    if(product.price !== undefined) {
        return product.description.split(',').map((text, index) => (
            index === 4 ?
                <React.Fragment key={`${text}-${index}`}>
                    {text}
                    <br/>
                </React.Fragment> : null

        ));
    } else {
        let description = product.split('|')[3];
        return description.split(',').map((text, index) => (
            index === 4 ?
                <React.Fragment key={`${text}-${index}`}>
                    {text}
                    <br/>
                </React.Fragment> : null

        ));
    }
};
const getResolution = (product) => {
    if(product.price !== undefined) {
        return product.description.split(',').map((text, index) => (
            index === 5 ?
                <React.Fragment key={`${text}-${index}`}>
                    {text}
                    <br/>
                </React.Fragment> : null

        ));
    } else {
        let description = product.split('|')[3];
        return description.split(',').map((text, index) => (
            index === 5 ?
                <React.Fragment key={`${text}-${index}`}>
                    {text}
                    <br/>
                </React.Fragment> : null

        ));
    }
};

const getMemory = (product) => {
    if(product.price !== undefined) {
        return product.description.split(',').map((text, index) => (
            index === 3 ?
                <React.Fragment key={`${text}-${index}`}>
                    {text}
                    <br/>
                </React.Fragment> : null

        ));
    } else {
        let description = product.split('|')[3];
        return description.split(',').map((text, index) => (
            index === 3 ?
                <React.Fragment key={`${text}-${index}`}>
                    {text}
                    <br/>
                </React.Fragment> : null

        ));
    }
};
const getGCard = (product) => {
    if(product.price !== undefined) {
        return product.description.split(',').map((text, index) => (
            index === 1 ?
                <React.Fragment key={`${text}-${index}`}>
                    {text}
                    <br/>
                </React.Fragment> : null

        ));
    } else {
        let description = product.split('|')[3];
        return description.split(',').map((text, index) => (
            index === 1 ?
                <React.Fragment key={`${text}-${index}`}>
                    {text}
                    <br/>
                </React.Fragment> : null

        ));
    }
};
const getInternal = (product) => {
    if(product.price !== undefined) {
        return product.description.split(',').map((text, index) => (
            index === 2 ?
                <React.Fragment key={`${text}-${index}`}>
                    {text}
                    <br/>
                </React.Fragment> : null

        ));
    } else {
        let description = product.split('|')[3];
        return description.split(',').map((text, index) => (
            index === 2 ?
                <React.Fragment key={`${text}-${index}`}>
                    {text}
                    <br/>
                </React.Fragment> : null

        ));
    }
};


export default connect(mapStateToProps, mapDispatchToProps)(Comparison);