import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import AuthService from "~/utils/auth.service";
import { isLogin } from "~/store/product/product.action";
import 'src/assets/style/layout/user/Header.scss';
import { logo } from '~/assets/images';
import { Avatar, Col, Divider, Dropdown, Row, Space } from "antd";
import { LogoutOutlined, SettingOutlined, UserOutlined } from '@ant-design/icons';

function HeaderAdmin() {

    const navigate = useNavigate();
    const dispatch = useDispatch();

    const handleSignOut = () => {
        AuthService.logout();
        navigate("/");
        dispatch(isLogin(false));
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
    return (
        <>
            <div className="header">
                <Row>
                    <Col span={3}>
                        <img className='logo' src={logo.logo_header} alt='' />
                    </Col>
                    <Col span={3} push={18}>
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
                                    <div className="user-fullname">Nguyen Linh Tram</div>
                                </Space>
                            </a>
                        </Dropdown>
                    </Col>
                </Row>
            </div>
        </>
    );
}

export default HeaderAdmin;