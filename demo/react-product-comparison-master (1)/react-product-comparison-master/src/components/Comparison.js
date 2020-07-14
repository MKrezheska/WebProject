import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

const Comparison = ({products}) =>
    products.length >= -1 ?
        <div className="row">
            <table id="tableID" className="table">
                <thead>
                <tr>
                    <th className="table-light">&nbsp;</th>
                    { products.map(product => <th key={`th-${getId(product)}`} className="text-center table-light">{getName(product)}</th>) }
                </tr>
                </thead>
                <tbody>
                <tr>
                    <th className="table-light">Цена</th>
                    { products.map(product => <td key={`url-td-${getId(product)}`} className="text-center table-light">{getPrice(product)}</td>) }
                </tr>
                <tr>
                    <th className="table-light">Дисплеј</th>
                    { products.map(product => <td id="tab-description" key={`description-td-${getId(product)}`} className="text-center table-light">{getDisplay(product)}</td>) }
                </tr>
                <tr>
                    <th className="table-light">Резолуција</th>
                    { products.map(product => <td id="tab-description" key={`description-td-${getId(product)}`} className="text-center table-light">{getResolution(product)}</td>) }
                </tr>
                <tr>
                    <th className="table-light">Графичка картичка</th>
                    { products.map(product => <td id="tab-description" key={`description-td-${getId(product)}`} className="text-center table-light">{getGCard(product)}</td>) }
                </tr>
                <tr>
                    <th className="table-light">Внатрешна меморија</th>
                    { products.map(product => <td id="tab-description" key={`description-td-${getId(product)}`} className="text-center table-light">{getInternal(product)}</td>) }
                </tr>
                <tr>
                    <th className="table-light">Меморија</th>
                    { products.map(product => <td id="tab-description" key={`description-td-${getId(product)}`} className="text-center table-light">{getMemory(product)}</td>) }
                </tr>
                <tr>
                    <th className="table-light">Процесор</th>
                    { products.map(product => <td id="tab-description" key={`description-td-${getId(product)}`} className="text-center table-light">{getProcessor(product)}</td>) }
                </tr>


                </tbody>
            </table>
        </div> :
        <div id="tableID" className="text-info text-center">Изберете продукти за споредба!</div>

;

Comparison.propTypes = {
    products : PropTypes.array.isRequired
};

const mapStateToProps = state => ({
    products : state.products
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


export default connect(mapStateToProps)(Comparison);