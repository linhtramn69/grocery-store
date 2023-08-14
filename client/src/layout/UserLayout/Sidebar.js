import React from 'react';
import { useEffect, useState } from "react";
import { useDispatch } from 'react-redux';
import ProductService from '~/utils/product.service';
import { getAllProducts } from '~/store/product/product.action';
import '~/assets/style/layout/user/Sidebar.scss';
import { Menu } from 'antd';
import { icon } from "~/assets/images";

function getItem(label, key, icon, children, type) {
    return {
        key,
        icon,
        children,
        label,
        type,
    };
}
const imgStyle = {
    width: 20
}

function Sidebar() {
    const [type, setType] = useState('');
    const dispatch = useDispatch();

    useEffect(() => {
        ProductService.getAllProducts().then(
            (res) => {
                dispatch(getAllProducts(res.data))
            },
            (err) => alert(err.response.data.message)
        )
    }, [dispatch])

    useEffect(() => {
        if (type !== '') {
            ProductService.getProductsByType(type).then(
                (res) => {
                    dispatch(getAllProducts(res.data));
                },
                (err) => alert(err.response.data.message)
            )
        }
    }, [type, dispatch])

    const items = [
        getItem(<p onClick={() => { setType('F&V') }}>Fruits & Vegetables</p>, 'F&V', <img style={imgStyle} src={icon.apple} alt='' />, [
            getItem(<p onClick={() => { setType('FRUIT') }}>Fruits</p>, 'FRUIT'),
            getItem(<p onClick={() => { setType('VEGE') }}>Vegetables</p>, '11'),
        ]),
        getItem(<p onClick={() => { setType('M&F') }}>Meat & Fish</p>, '3', <img style={imgStyle} src={icon.meat} alt='' />, [
            getItem(<p onClick={() => { setType('MEAT') }}>Meat</p>, '12'),
            getItem(<p onClick={() => { setType('FISH') }}>Fish</p>, '13'),
        ]),
        getItem(<p onClick={() => { setType('SNACK') }}>Snacks</p>, '4', <img style={imgStyle} src={icon.coffee_cup} alt='' />, [
            getItem(<p onClick={() => { setType('N&B') }}>Nuts & Biscuits</p>, '14'),
            getItem(<p onClick={() => { setType('NOODLE') }}>Noodles</p>, '15'),
        ]),
        getItem(<p onClick={() => { setType('DAIRY') }}>Dairy</p>, '5', <img style={imgStyle} src={icon.milk} alt='' />, [
            getItem(<p onClick={() => { setType('MILK') }}>Milk</p>, '16'),
            getItem(<p onClick={() => { setType('BUTTER') }}>Butter</p>, '17'),
            getItem(<p onClick={() => { setType('YOGURT') }}>Yogurt</p>, '18'),
        ]),
        getItem(<p onClick={() => { setType('COOKING') }}>Cooking</p>, '6', <img style={imgStyle} src={icon.cooking} alt='' />, [
            getItem(<p onClick={() => { setType('OIL') }}>Oil</p>, '19'),
            getItem(<p onClick={() => { setType('S&S') }}>Salt & Sugar</p>, '20'),
        ]),
        getItem(<p onClick={() => { setType('BREAKFAST') }}>Breakfast</p>, '7', <img style={imgStyle} src={icon.pie} alt='' />, [
            getItem(<p onClick={() => { setType('BREAD') }}>Bread</p>, '21'),
            getItem(<p onClick={() => { setType('CEREAL') }}>Cereal</p>, '22'),
            getItem(<p onClick={() => { setType('JAM') }}>Jam</p>, '23'),
        ]),
        getItem(<p onClick={() => { setType('BEVERAGE') }}>Beverage</p>, '8', <img style={imgStyle} src={icon.drink} alt='' />, [
            getItem(<p onClick={() => { setType('COFFEE') }}>Coffee</p>, '24'),
            getItem(<p onClick={() => { setType('TEA') }}>Tea</p>, '25'),
        ]),
        getItem('Health & Beauty', '9', <img style={imgStyle} src={icon.mirror} alt='' />),
    ];
    


    return (
        <div className='sidebar'>
            <Menu
                defaultSelectedKeys={['1']}
                defaultOpenKeys={['sub1']}
                mode="inline"
                items={items}
            />
        </div>

    );
}


export default Sidebar;