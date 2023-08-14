import { Layout } from 'antd';
import React from 'react';
import HeaderAdmin from './Header';
import SidebarAdmin from './Sidebar';
const { Header, Content, Sider } = Layout;
function AdminLayout({ children }) {
    return (
        <Layout className='wrapper'>
            <Header className='bg-light shadow-sm'>
                <HeaderAdmin />
            </Header>
            <Layout className="container">
                <Sider>
                    <SidebarAdmin />
                </Sider>
                <Content>
                    <div className="content">
                        {children}
                    </div>
                </Content>
            </Layout>
        </Layout>
    );
}

export default AdminLayout;