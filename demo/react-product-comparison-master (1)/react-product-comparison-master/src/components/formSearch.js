import React from 'react';
import FormControl from '@material-ui/core/FormControl';
import FormLabel from "@material-ui/core/FormLabel";
import RadioGroup from "@material-ui/core/RadioGroup";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Radio from "@material-ui/core/Radio";
import { MDBSelect } from "mdbreact";


const FormSearch = (props) => {

    const onSearch = (e)=>{
        e.preventDefault();
        props.onSearch(e.target["display"].value, e.target["graphicsCard"].value, e.target["internalMemory"].value, e.target["memory"].value, e.target["processor"].value, e.target["resolution"].value);
    }

    return (
        <form onSubmit={onSearch} className="form-check mt-2 mt-md-0">

            <div className="form-group">
                <select className="browser-default custom-select " name={"display"}>
                    <option value="">Display</option>
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
                    <option value="">Internal Memory</option>
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
                    <option value="">Memory</option>
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
                    <option value="">Resolution</option>
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
                    <option value="">Processor</option>
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
                    <option value="">Graphics Card</option>
                    <option value="Integrated">Integrated</option>
                    <option value="Dedicated">Dedicated</option>
                </select>
            </div>
            {/*<FormControl component="fieldset">*/}
            {/*    <FormLabel component="legend">Display</FormLabel>*/}
            {/*    <RadioGroup aria-label="display" name="display">*/}
            {/*        <FormControlLabel value="11.6" control={<Radio />} label="11.6" />*/}
            {/*        <FormControlLabel value="13.3" control={<Radio />} label="13.3" />*/}
            {/*        <FormControlLabel value="13.9" control={<Radio />} label="13.9" />*/}
            {/*        <FormControlLabel value="14" control={<Radio />} label="14" />*/}
            {/*        <FormControlLabel value="14.1" control={<Radio />} label="14.1" />*/}
            {/*        <FormControlLabel value="15.6" control={<Radio />} label="15.6" />*/}
            {/*        <FormControlLabel value="16" control={<Radio />} label="16" />*/}
            {/*        <FormControlLabel value="17.3" control={<Radio />} label="17.3" />*/}
            {/*        <FormControlLabel value="23.8" control={<Radio />} label="23.8" />*/}

            {/*    </RadioGroup>*/}
            {/*</FormControl>*/}
            {/*<div>*/}
            {/*    <input type="radio" value="11.6" className="form-control mr-sm-2" name={"display"}/> 11.6*/}
            {/*    <input type="radio" value="13.3" className="form-control mr-sm-2" name={"display"}/> 13.3*/}
            {/*    <input type="radio" value="13.9" className="form-control mr-sm-2" name={"display"}/> 13.9*/}
            {/*    <input type="radio" value="14" className="form-control mr-sm-2" name={"display"}/> 14*/}
            {/*    <input type="radio" value="14.1" className="form-control mr-sm-2" name={"display"}/> 14.1*/}
            {/*    <input type="radio" value="15.6" className="form-control mr-sm-2" name={"display"}/> 15.6*/}
            {/*    <input type="radio" value="16" className="form-control mr-sm-2" name={"display"}/> 16*/}
            {/*    <input type="radio" value="17.3" className="form-control mr-sm-2" name={"display"}/> 17.3*/}
            {/*    <input type="radio" value="23.8" className="form-control mr-sm-2" name={"display"}/> 23.8*/}
            {/*</div>*/}
            {/*<input className="form-control mr-sm-2" name={"display"} type="text" placeholder="Search" aria-label="Search"/>*/}
            {/*<input className="form-control mr-sm-2" name={"graphicsCard"} type="text" placeholder="Search" aria-label="Search"/>*/}
            {/*<input className="form-control mr-sm-2" name={"internalMemory"} type="text" placeholder="Search" aria-label="Search"/>*/}
            {/*<input className="form-control mr-sm-2" name={"memory"} type="text" placeholder="Search" aria-label="Search"/>*/}
            {/*<input className="form-control mr-sm-2" name={"processor"} type="text" placeholder="Search" aria-label="Search"/>*/}
            {/*<input className="form-control mr-sm-2" name={"resolution"} type="text" placeholder="Search" aria-label="Search"/>*/}
            <button className="btn btn-info my-2 my-sm-0 form-group colorclass" type="submit">Search</button>
        </form>
    )
}

export default FormSearch;
