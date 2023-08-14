import AdminLayout from "~/layout/AdminLayout/AdminLayout";
import AuthLayout from "~/layout/AuthLayout/AuthLayout";
import LayoutNoSidebar from "~/layout/UserLayout/LayoutNoSidebar";
import LayoutUser from "~/layout/UserLayout/UserLayout";
import Dashboard from "~/pages/admin/Dashboard";
import OrderDetail from "~/pages/admin/OrderManager/OrderDetail";
import OrderManager from "~/pages/admin/OrderManager/OrderManager";
import AddProduct from "~/pages/admin/ProductManager/AddProduct";
import EditProduct from "~/pages/admin/ProductManager/EditProduct";
import ProductManager from "~/pages/admin/ProductManager/ProductManager";
import TypeProductManager from "~/pages/admin/TypeProductManager/TypeProductManager";
import UserManager from "~/pages/admin/UserManager/UserManager";
import LoginPage from "~/pages/auth/LoginPage";
import RegisterPage from "~/pages/auth/RegisterPage";
import Checkout from "~/pages/user/Checkout";
import HomePage from "~/pages/user/HomePage/HomePage";
import ProductDetail from "~/pages/user/ProductDetail/ProductDetail";
import StatusOrder from "~/pages/user/StatusOrder";

const publicRoutes = [
    { path: '/', component: HomePage, layout: LayoutUser },
    { path: '/login', component: LoginPage, layout: AuthLayout },
    { path: '/register', component: RegisterPage, layout: AuthLayout },
    { path: '/product/:id', component: ProductDetail, layout: LayoutNoSidebar },
    { path: '/checkout', component: Checkout, layout: AuthLayout },
    { path: '/status-order/:id', component: StatusOrder, layout: AuthLayout },
    //Admin
    { path: '/admin', component: Dashboard, layout: AdminLayout },
    { path: '/admin/products', component: ProductManager, layout: AdminLayout },
    { path: '/admin/product/add', component: AddProduct, layout: AdminLayout },
    { path: '/admin/product/edit/:id', component: EditProduct, layout: AdminLayout },
    { path: '/admin/product-types', component: TypeProductManager, layout: AdminLayout },
    { path: '/admin/orders', component: OrderManager, layout: AdminLayout },
    { path: '/admin/orders/:id', component: OrderDetail, layout: AdminLayout },
    { path: '/admin/users', component: UserManager, layout: AdminLayout },


]

const privateRoutes = []
export { publicRoutes, privateRoutes }