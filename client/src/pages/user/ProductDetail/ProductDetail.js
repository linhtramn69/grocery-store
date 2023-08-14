import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import HomePage from "../HomePage/HomePage.js";
import { getAllProducts, getProductById, getProductFromCart } from "~/store/product/product.action.js";
import ProductService from "~/utils/product.service.js";
import CartProductService from "~/utils/cartProduct.service.js";
import '~/assets/style/page/DetailPage.scss';
import { Button, Col, Divider, message, Row, Space, Typography } from "antd";
const { Paragraph } = Typography;

function ProductDetail() {

    let { id } = useParams();
    const [change, setChange] = useState();
    const [messageApi, contextHolderMess] = message.useMessage();
    const navigate = useNavigate();
    const dispatch = useDispatch();
    let product = [];
    let USDollar = new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD',
    });

    const isLogin = useSelector((state) => state.product.isLogin);
    const cart = useSelector((state) => state.product.cart);
    product = useSelector((state) => state.product.product);

    useEffect(() => {
        ProductService.getProductById(id).then(
            (res) => {
                dispatch(getProductById(res.data))
                res.data.types.map((item) => {
                    if (item.icon) {
                        ProductService.getProductsByType(item.id).then(
                            (res) => {
                                dispatch(getAllProducts(res.data))
                            }
                        )
                    }
                })
            },
            (err) => alert(err.response.data.message)
        )
    }, [id, dispatch])

    useEffect(() => {
        if (isLogin) {
            CartProductService.getProductsFromCart(cart.id).then(
                (res) => {
                    dispatch(getProductFromCart(res.data));
                },
                (err) => alert(err.response.data.message)
            )
        }
    }, [change, isLogin, dispatch, cart.id])

    const handleCart = (data) => {
        if (isLogin) {
            const newData = {
                cart: cart,
                product: data,
                quantity: 1
            }
            CartProductService.addProductToCart(newData).then(
                (res) => {
                    setChange(res);
                    messageApi.open({
                        type: 'success',
                        content: 'Successfully added to cart!',
                    });
                },
                (err) => {
                    messageApi.open({
                        type: 'error',
                        content: 'Failed to add product!',
                    });
                }
            )
        }
        else {
            navigate("/login");
        }
    }


    return (
        <>
            {contextHolderMess}
            <div className='detail-page'>
                <Row className='first'>
                    <Col span={12} pull={1} className='col-right'>
                        <img alt="example" src={`http://localhost:8080/api/uploads/products/${product.image}`} />
                    </Col>
                    <Col span={10} push={1} className='col-left'>
                        <Space direction='vertical'>
                            <h3 className='title-product'>
                                {product.name}
                            </h3>
                            <Paragraph
                                className='description-product'
                                ellipsis={{
                                    rows: 3,
                                    expandable: true,
                                }}
                                title={`${product.description}`}
                            >
                                {product.description}
                            </Paragraph>
                            <p className='price-product'>{USDollar.format(product.price)}</p>
                            <Button className='btn-add-card-detail' onClick={() => { handleCart(product) }}>
                                Add To Shopping Cart
                            </Button>
                        </Space>
                        <Divider />
                        <Space direction='vertical' size={20}>
                            <Space size={30}>
                                <b>Categories</b>
                                {product.types ? product.types.map((item) => {
                                    return <Space size={15}>
                                        <Button>{item.name}</Button>
                                    </Space>
                                }) : <></>}
                            </Space>
                            <Space size={65}>
                                <b>Seller</b>
                                <Link to='/'>Linh Tram shop</Link>
                            </Space>
                        </Space>
                    </Col>
                </Row>
                <Divider style={{ margin: '60px 0 30px 0' }} />
                <div className='second'>
                    <h3 className='title-second'>Related Products</h3>
                    <HomePage />
                </div>
            </div>

        </>

    );
}

export default ProductDetail;