import React, {Component} from 'react';
import Loader from './common/Loader';
import {getSimilarToProduct, loadProducts, searchProducts} from '../api/api';
import Product from './Product';
import ReactPaginate from 'react-paginate';
import Header from "./Header";


class Products extends Component {
    constructor(props) {
        super(props);
        this.state = {
            offset: 0,
            products: [],
            loading: true,
            elements: [],
            perPage: 3,
            currentPage: 0,
            similar: []
        };
    }

    componentDidMount() {
        loadProducts().then(data => {
            this.setState({
                products: data,
                loading: false,
                currentPage: 0,
                similar: [],
                pageCount: Math.ceil(data.length / this.state.perPage)
            }, () => this.setElementsForCurrentPage());
        })

    }

    searchData = (display, graphicsCard, internalMemory, memory, processor, resolution) => {

        searchProducts(display, graphicsCard, internalMemory, memory, processor, resolution).then(data => {
            console.log('response', data)
            this.setState({
                products: data,
                loading: false,
                currentPage: 0,
                offset: 0,
                pageCount: Math.ceil(data.length / this.state.perPage)
            }, () => this.setElementsForCurrentPage());
        })
    }




    setElementsForCurrentPage() {
        let elements = this.state.products
            .slice(this.state.offset, this.state.offset + this.state.perPage)
            .map((product) => <Product product={product} key={product.id}/>);

        this.setState({elements: elements});
    }

    handlePageClick = (data) => {
        const selectedPage = data.selected;
        const offset = selectedPage * this.state.perPage;
        this.setState({currentPage: selectedPage, offset: offset}, () => {
            this.setElementsForCurrentPage();
        });
    };

    render() {
        const {products, loading} = this.state;
        let paginationElement;
        if (this.state.pageCount > 1) {
            paginationElement = (
                <ReactPaginate
                    previousLabel={'<'}
                    nextLabel={'>'}
                    breakLabel={'...'}
                    breakClassName={'break-me'}
                    pageCount={this.state.pageCount}
                    marginPagesDisplayed={2}
                    pageRangeDisplayed={4}
                    onPageChange={this.handlePageClick}
                    containerClassName={'pagination'}
                    subContainerClassName={'pages pagination'}
                    activeClassName={'active'}
                    itemClass={"page-item"}
                    linkClass={"page-link"}
                    forcePage={this.state.currentPage}
                />
            );
        }
        if (this.state.elements.length > 0)
            return (
                loading ?
                    <Loader/> :

                    <div className="row">
                        <Header onSearch={this.searchData}/>
                        {this.state.elements}
                        {paginationElement}
                    </div>
            );
        else
            return (
                <div className="row">
                    <Header onSearch={this.searchData}/>
                    <p className="text-info text-center">
                        <i className="fa fa-minus-circle"></i><br/>
                        Нема продукти со избраните спецификации!</p></div>

            );
    }
}

export default Products;