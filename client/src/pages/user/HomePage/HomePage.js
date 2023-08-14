import { useDispatch, useSelector } from "react-redux";
import { Link, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import CartProductService from "~/utils/cartProduct.service";
import CartService from "~/utils/cart.service";
import { getCart, getProductFromCart } from "~/store/product/product.action";
import '~/assets/style/page/HomePage.scss';
import { Avatar, Button, Card, Col, Drawer, FloatButton, List, message, Pagination, Row, Skeleton, Space, Typography } from "antd";
import Meta from "antd/es/card/Meta";
import {
    ShoppingCartOutlined,
    CloseOutlined,
    PlusOutlined,
    MinusOutlined
} from '@ant-design/icons';

function HomePage() {

    const { Paragraph } = Typography;
    let total = 0.0;
    const numEachPage = 12;
    let product = [];
    let productFromCart = [];
    const data = [];
    const dataFromCart = [];
    let USDollar = new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD',
    });
    const [ellipsis, setEllipsis] = useState(true);
    const [minValue, setMinValue] = useState(0);
    const [maxValue, setMaxValue] = useState(12);
    const [current, setCurrent] = useState(1);
    const [open, setOpen] = useState(false);
    const [change, setChange] = useState();
    const [messageApi, contextHolderMess] = message.useMessage();
    const navigate = useNavigate();
    const dispatch = useDispatch();

    const isLogin = useSelector((state) => state.product.isLogin);
    const cart = useSelector((state) => state.product.cart);
    product = useSelector((state) => state.product.products);
    productFromCart = useSelector((state) => state.product.productFromCart);

    useEffect(() => {
        if (isLogin) {
            CartService.getCartByUser().then(
                (res) => {
                    dispatch(getCart(res.data));
                    CartProductService.getProductsFromCart(res.data.id).then(
                        (res) => {
                            dispatch(getProductFromCart(res.data));
                        },
                        (err) => alert(err.response.data.message)
                    )
                },
                (err) => alert(err.response.data.message)
            )
        }
    }, [change, isLogin, dispatch])

    product.map((item) => {
        if (item.status) {
            return (
                data.push({
                    key: item.id,
                    id: item.id,
                    name: item.name,
                    price: item.price,
                    image: item.image,
                    description: item.description
                })
            )
        }
    })

    productFromCart.map((item) => {
        return (
            dataFromCart.push({
                key: item.product.id,
                id: item.product.id,
                name: item.product.name,
                price: item.product.price,
                image: item.product.image,
                quantity: item.quantity
            }),
            total += (item.quantity * item.product.price)
        )
    })

    const showDrawer = () => {
        setOpen(true);
    }
    const onClose = () => {
        setOpen(false);
    }
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
    const handleDeleteProductFromCart = (product, amount) => {
        const dataNew = {
            product: { id: product.id },
            cart: { id: cart.id },
            quantity: amount
        }
        CartProductService.deleteProductFromCart(dataNew).then(
            (res) => {
                setChange(res);
                messageApi.open({
                    type: 'success',
                    content: 'Successfully delete product from cart!',
                });
            },
            (err) => {
                messageApi.open({
                    type: 'error',
                    content: 'Failed to delete product from cart!',
                });
            }
        )
    }
    const handleCheckout = () => {
        navigate('/checkout')
    }
    const handleChange = value => {
        setMinValue((value - 1) * numEachPage);
        setMaxValue(value * numEachPage);
        setCurrent(value);
    };

    return (
        <>
            <Row gutter={[20, 20]} className='list-product'>
                {data.slice(minValue, maxValue).map((item, index) => {
                    return <Col span={6} key={index}>
                        <Card
                            className="card-product"
                            hoverable
                            bordered={false}
                            cover={
                                <Link to={`/product/${item.key}`}>
                                    <img alt="example" src={`http://localhost:8080/api/uploads/products/${item.image}`} />
                                </Link>
                            }
                        >
                            <Meta title={USDollar.format(item.price)} />
                            <Paragraph className="label-product" ellipsis={ellipsis} >
                                {item.name}
                            </Paragraph>
                            <Button className="btn-add-card bg-gray-100" onClick={() => { handleCart(item) }} >
                                Add to card
                            </Button>
                        </Card>
                    </Col>
                })}

            </Row>
            <Pagination className="pagination"
                current={current}
                defaultPageSize={numEachPage}
                onChange={handleChange}
                total={product.length}
                showSizeChanger={false}
            />
            <FloatButton
                shape="square"
                className='float-btn'
                onClick={showDrawer}
                description={
                    <Space direction="vertical" className='float-btn-content'>
                        <Space direction="horizontal">
                            <ShoppingCartOutlined style={{ fontWeight: 600 }} />
                            <p>{productFromCart.length} Items</p>
                        </Space>
                        <Button className='float-btn-total'>
                            {USDollar.format(total)}
                        </Button>
                    </Space>
                }>
            </FloatButton>
            {contextHolderMess}
            <Drawer
                width={450}
                title={
                    <Space className='title-drawer' direction="horizontal">
                        <ShoppingCartOutlined style={{ fontWeight: 600 }} />
                        <p>{productFromCart.length} Items</p>
                    </Space>
                }
                placement='right'
                closable={false}
                onClose={onClose}
                open={open}
                key='right'
            >
                <List
                    style={{ height: '92%' }}
                    itemLayout="horizontal"
                    dataSource={dataFromCart}
                    renderItem={(item, index) => (
                        <List.Item actions={[<Button className='btn-delete-item' onClick={() => handleDeleteProductFromCart(item, 0)}><CloseOutlined /></Button>]}>
                            <Skeleton avatar title={false} loading={item.loading} active>
                                <List.Item.Meta
                                    avatar={<Avatar shape="square" className='avatar-item' src={`http://localhost:8080/api/uploads/products/${item.image}`} />}
                                    title={<a href="https://ant.design">{item.name}</a>}
                                    description={
                                        <Space direction="vertical">
                                            <p className='price-item'>{USDollar.format(item.price)}</p>
                                            <Space className='btn-set-total'>
                                                <Button className='btn-icon'>
                                                    <MinusOutlined onClick={() => handleDeleteProductFromCart(item, 1)} />
                                                </Button>
                                                <input value={item.quantity} />
                                                <Button className='btn-icon'>
                                                    <PlusOutlined onClick={() => { handleCart(item) }} />
                                                </Button>
                                            </Space>
                                        </Space>
                                    }
                                />
                                <p style={{ fontWeight: 600 }}>{USDollar.format(item.quantity * item.price)}</p>
                            </Skeleton>
                        </List.Item>
                    )}
                />
                <Button className='btn-checkout' onClick={handleCheckout}>
                    <p style={{ margin: 0 }}>Check out</p>
                    <Button className='btn-checkout-total'>{USDollar.format(total)}</Button>
                </Button>
            </Drawer>
        </>
    );
}

export default HomePage;