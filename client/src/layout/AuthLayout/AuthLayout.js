import { Layout } from "antd";
import HeaderUser from "../UserLayout/Header/Header";
import '~/assets/style/auth/AuthLayout.scss';
const { Header, Content } = Layout;
function AuthLayout({ children }) {
    return (
        <>
            <Layout>
                <Header className="bg-light shadow-sm">
                    <HeaderUser />
                </Header>
                <Content className="content-auth">
                    {children}
                </Content>
            </Layout>

        </>
    );
}

export default AuthLayout;