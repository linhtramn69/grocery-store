import React from 'react';
import { Link } from 'react-router-dom';
import '~/assets/style/layout/user/Sidebar.scss';
import { icon } from "~/assets/images";
import { Menu } from 'antd';
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
    width: 22
}

function SidebarAdmin() {
    const items = [
        getItem(<Link to={'/admin'}>Dashboard</Link>, '0', <img style={imgStyle} src={icon.dashboard} alt='' />),
        getItem(<Link to={'/admin/products'}>Products</Link>, '1', <img style={imgStyle} src={icon.product} alt='' />),
        getItem(<Link to={'/admin/product-types'}>Type Product</Link>, '3', <img style={imgStyle} src={icon.type} alt='' />),
        getItem(<Link to={'/admin/orders'}>Orders</Link>, '4', <img style={imgStyle} src={icon.order} alt='' />),
        getItem(<Link to={'/admin/users'}>Users</Link>, '2', <img style={imgStyle} src={icon.user} alt='' />),
        getItem('Coupons', '5', <img style={imgStyle} src={icon.coupon} alt='' />),
        getItem('Shipping', '6', <img style={imgStyle} src={icon.delivery} alt='' />),
        getItem('Settings', '7', <img style={imgStyle} src={icon.setting} alt='' />)
    ];

    return (
        <div className='sidebar'>
            <Menu
                defaultSelectedKeys={['0']}
                defaultOpenKeys={['sub1']}
                mode="inline"
                items={items}
            />
        </div>
    );
}

export default SidebarAdmin;