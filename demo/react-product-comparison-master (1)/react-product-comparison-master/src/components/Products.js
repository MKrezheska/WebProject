import React, { Component } from 'react';
import Loader from './common/Loader';
import { loadProducts } from '../api/api';
import Product from './Product';
import ReactPaginate from 'react-paginate';

class Products extends Component {
    constructor(props) {
        super(props);
        this.state = {
            offset: 0,
            products : [],
            loading : true,
            elements: [],
            perPage: 4,
            currentPage: 0
        };
    }

    componentDidMount() {
        loadProducts().then(data => {
            this.setState({
                products: data,
                loading: false,
                pageCount: Math.ceil(data.length / this.state.perPage)
            }, () => this.setElementsForCurrentPage());
        })
    }

    setElementsForCurrentPage() {
        let elements = this.state.products
            .slice(this.state.offset, this.state.offset + this.state.perPage)
            .map((product) => <Product product={product} key={product.id} />);

        this.setState({ elements: elements });
    }

    handlePageClick = (data) => {
        const selectedPage = data.selected;
        const offset = selectedPage * this.state.perPage;
        this.setState({ currentPage: selectedPage, offset: offset }, () => {
            this.setElementsForCurrentPage();
        });
    };

    render() {
        const { products, loading } = this.state;
        let paginationElement;
        if (this.state.pageCount > 1) {
            paginationElement = (
                <ReactPaginate
                    previousLabel={"← Previous"}
                    nextLabel={"Next →"}
                    breakLabel={<span className="gap">...</span>}
                    pageCount={this.state.pageCount}
                    onPageChange={this.handlePageClick}
                    forcePage={this.state.currentPage}
                    containerClassName={"pagination"}
                    previousLinkClassName={"previous_page"}
                    nextLinkClassName={"next_page"}
                    disabledClassName={"disabled"}
                    activeClassName={"active"}
                />
            );
        }
        return (
            loading ?
                <Loader /> :
                <div className="row">
                    {this.state.elements}
                    {paginationElement}
                </div>
        );
    }
}

export default Products;