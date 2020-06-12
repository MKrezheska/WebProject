import React, {Component} from 'react';

class FormSearch extends Component{

    onSearch = (e)=>{
        e.preventDefault();
        this.props.onSearch(e.target["display"].value, e.target["graphicsCard"].value, e.target["internalMemory"].value, e.target["memory"].value, e.target["processor"].value, e.target["resolution"].value);
    }
    render () {
        return (
            <div>
            <form onSubmit={this.onSearch} className="form-check mt-2 mt-md-0">

                <div className="form-group">
                    <select className="browser-default custom-select " name={"display"}>
                        <option value="">Дисплеј</option>
                        <option value="11.6">11.6</option>
                        <option value="13.3">13.3</option>
                        <option value="13.9">13.9</option>
                        <option value="14">14</option>
                        <option value="14.1">14.1</option>
                        <option value="15.6">15.6</option>
                        <option value="16.0">16</option>
                        <option value="17.3">17.3</option>
                        <option value="23.8">23.8</option>
                    </select>
                </div>
                <div className="form-group">
                    <select className="browser-default custom-select " name={"internalMemory"}>
                        <option value="">Внатрешна меморија</option>
                        <option value="2">2GB</option>
                        <option value="4">4GB</option>
                        <option value="6">6GB</option>
                        <option value="8">8GB</option>
                        <option value="12">12GB</option>
                        <option value="16">16GB</option>
                    </select>
                </div>
                <div className="form-group">
                    <select className="browser-default custom-select " name={"memory"}>
                        <option value="">Меморија</option>
                        <option value="128">128GB SSD</option>
                        <option value="256">256GB SSD</option>
                        <option value="500">500GB HDD</option>
                        <option value="512">512GB SSD</option>
                        <option value="1TB">1TB HDD or SSD</option>
                        <option value="2TB">2TB HDD</option>
                        <option value="Two">2 hard drives</option>
                    </select>
                </div>
                <div className="form-group">
                    <select className="browser-default custom-select " name={"resolution"}>
                        <option value="">Резолуција</option>
                        <option value="1140">1140x900</option>
                        <option value="1360">1360x768</option>
                        <option value="1366">1366x768</option>
                        <option value="1920">1920x1080</option>
                        <option value="2560">2560x1600</option>
                        <option value="3072">3072x1920</option>
                        <option value="3840">3840x2160</option>
                    </select>
                </div>
                <div className="form-group">
                    <select className="browser-default custom-select " name={"processor"}>
                        <option value="">Процесор</option>
                        <option value="pentium">Intel Pentium</option>
                        <option value="celeron">Intel Celeron</option>
                        <option value="atom">Intel Atom</option>
                        <option value="i3">Intel Core i3</option>
                        <option value="i5">Intel Core i5</option>
                        <option value="i7">Intel Core i7</option>
                        <option value="amd">AMD</option>
                    </select>
                </div>
                <div className="form-group">
                    <select className="browser-default custom-select " name={"graphicsCard"}>
                        <option value="">Графичка картичка</option>
                        <option value="Integrated">Integrated</option>
                        <option value="Dedicated">Dedicated</option>
                    </select>
                </div>
                <button className="btn  my-2 my-sm-0 form-group colorclass" type="submit">Пребарај</button>
            </form>
                <p></p>
                <button className="btn  my-2 my-sm-0  form-group colorclass" onClick={_onButtonClick}>Види споредба</button>
            </div>

        );
    }
}

const _onButtonClick = () => {
    let el = document.getElementById("tableID");
    if(el != null){
        el.style.display = "table";
        window.scrollTo(0, document.body.scrollHeight)
    }
    if(el.innerText === "Изберете продукти за споредба!"){
        el.style.display = "block";
    }

}

export default FormSearch;