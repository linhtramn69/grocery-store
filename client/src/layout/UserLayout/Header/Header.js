import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import InputSearch from '~/shared/components/InputSearch/InputSearch';
import AuthService from '~/utils/auth.service';
import ProductService from '~/utils/product.service';
import { getAllProducts, getCart, getProductById, getProductFromCart, isLogin } from '~/store/product/product.action';
import 'src/assets/style/layout/user/Header.scss';
import { logo } from '~/assets/images';
import { Avatar, Col, Divider, Dropdown, Menu, Row, Space } from 'antd';
import { LogoutOutlined, SettingOutlined, UserOutlined } from '@ant-design/icons';

const styleButton = {
    padding: '10px 15px',
    border: 'none',
    borderRadius: 5,
    color: '#fff',
    fontSize: '14px',
    fontWeight: 600,
    backgroundColor: `rgba(var(--color-accent), var(--tw-bg-opacity))`
}

function HeaderUser() {

    const navigate = useNavigate();
    const dispatch = useDispatch();

    const handleReload = () => {
        navigate("/");
        ProductService.getAllProducts().then(
            (res) => {
                dispatch(getAllProducts(res.data));
                dispatch(getProductById({}));
            }
        )
    }
    const handleSignOut = () => {
        AuthService.logout();
        navigate("/");
        dispatch(isLogin(false));
        dispatch(getProductFromCart([]));
        dispatch(getCart({}));
    }
    const handleLogin = () => {
        navigate("/login");
    }
    const handleRegister = () => {
        navigate("/register");
    }
    const items = [
        {
            label: <a href="https://www.antgroup.com">
                Profile User
                <UserOutlined />
            </a>,
            key: '0',
        },
        {
            label: <a href="https://www.antgroup.com">
                Settings
                <SettingOutlined />
            </a>,
            key: '1',
        },
        {
            key: '3',
            label: <>
                <Divider style={{ margin: '5px' }} />
                <p onClick={handleSignOut}>
                    Log out
                    <LogoutOutlined />
                </p>
            </>
        },
    ];
    const menuHeader = [
        {
            label: 'Shop',
            key: 'shop',
        },
        {
            label: 'Offers',
            key: 'offers',
        },
        {
            label: 'FAQ',
            key: 'faq',
        },
        {
            label: 'Contact',
            key: 'contact',
        },
        {
            label:
                useSelector((state) => !state.product.isLogin) ?
                    <Space>
                        <button style={styleButton} onClick={handleLogin}>
                            Login
                        </button>
                        <button style={styleButton} onClick={handleRegister}>
                            Register
                        </button>
                    </Space>
                    :
                    <Dropdown
                        menu={{
                            items,
                        }}
                        trigger={['click']}
                    >
                        <a onClick={(e) => e.preventDefault()}>
                            <Space>
                                <Avatar
                                    style={{
                                        backgroundColor: `rgba(var(--color-accent), var(--tw-bg-opacity))`,
                                    }}>
                                    LT
                                </Avatar>
                                {JSON.parse(localStorage.getItem("user")).username}
                            </Space>
                        </a>
                    </Dropdown>,
            key: 'btn-2',
        },
    ]

    return (
        <>
            <div className='header'>
                <Row >
                    <Col span={3} >
                        <img className='logo' src={logo.logo_header} alt='' onClick={handleReload} />
                    </Col>
                    {/* <Col span={3}>
                        <Select
                            defaultValue="vegetables"
                            style={{
                                width: '90%',
                            }}
                            options={dataDropdown}
                        />
                    </Col> */}
                    <Col span={8} push={1}>
                        <InputSearch />
                    </Col>
                    <Col span={10} push={1}>
                        <Menu mode="horizontal" className='menu-header' defaultSelectedKeys={['2']} items={menuHeader} />
                    </Col>
                </Row>
                {/* <CarouselBanner/>  */}
            </div>

        </>
    );
}

export default HeaderUser;