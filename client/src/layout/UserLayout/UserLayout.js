import { Layout } from 'antd';
import React from 'react';
import HeaderUser from "./Header/Header";
import Sidebar from "./Sidebar";
import '../../assets/style/layout/user/LayoutUser.scss'
const { Header, Content, Sider } = Layout;
function LayoutUser({ children }) {
    return (
        <>
            <Layout className='wrapper'>
                <Header className='bg-light shadow-sm'>
                    <HeaderUser />
                </Header>
                <Layout className="container">
                    <Sider>
                        <Sidebar />
                    </Sider>
                    <Content>
                        <div className="content">
                            {children}
                        </div>
                    </Content>
                </Layout>
            </Layout>

        </>
    );
}

export default LayoutUser;