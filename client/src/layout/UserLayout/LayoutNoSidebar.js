import { Layout } from "antd";
import HeaderUser from "../UserLayout/Header/Header";
import '../../assets/style/layout/user/LayoutUser.scss';
const { Header, Content } = Layout;
function LayoutNoSidebar({ children }) {
    return (
        <>
            <Layout>
                <Header className="bg-light shadow-sm">
                    <HeaderUser />
                </Header>
                <Content className="content-no-sidebar bg-light">
                    {children}
                </Content>
            </Layout>

        </>
    );
}

export default LayoutNoSidebar;