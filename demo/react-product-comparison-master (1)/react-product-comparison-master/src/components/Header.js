import React from 'react';
import FormSearch from "./formSearch";

const Header = (props) => {
    return (
        <header>
            <nav className="navbar navbar-expand-md" style={{paddingTop: 0 + "px"}}>
                <FormSearch onSearch={props.onSearch}/>
            </nav>
        </header>
    )
}
export default Header;