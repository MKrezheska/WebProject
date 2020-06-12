import React, {Component} from 'react';
import './App.css';
import Products from './components/Products';
import Comparison from './components/Comparison';



class App extends Component {




    render() {
        return (
            <div className="container">

                <h1>Спореди продукти</h1>
                <Products/>
                <Comparison/>
            </div>
        );
    }
}

export default App;
