import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import OrderService from "~/utils/order.service";
import { getOrder, getProductFromCart } from "~/store/product/product.action";
import 'src/assets/style/page/CheckoutPage.scss';
import { Avatar, Card, Col, Divider, Row, Space } from "antd";
import Meta from "antd/es/card/Meta";
import { ExclamationCircleOutlined } from "@ant-design/icons";

const dataDate = [
    {
        id: '0',
        title: 'Express Delivery',
        time: '90 min express'
    },
    {
        id: '1',
        title: 'Morning',
        time: '8.00 AM - 11.00 AM'
    },
    {
        id: '2',
        title: 'Noon',
        time: '11.00 AM - 2.00 PM'
    },
    {
        id: '3',
        title: 'Afternoon',
        time: '2.00 PM - 5.00 PM'
    },
    {
        id: '4',
        title: 'Evening',
        time: '5.00 PM - 8.00 PM'
    }
]

function Checkout() {

    const { register, handleSubmit, formState: { errors } } = useForm();
    const [time, setTime] = useState();

    let total = 0.0;
    let amount = 0;
    let productFromCart = useSelector((state) => state.product.productFromCart);
    const cart = useSelector((state) => state.product.cart);
    const dispatch = useDispatch();
    const navigate = useNavigate();

    let USDollar = new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD',
    });
    const errOptions = {
        fullName: {
            required: 'Fullname is not empty!'
        },
        phone: {
            required: 'Phone is not empty!',
            minLength: {
                value: 10,
                message: "Phone is unavailable!"
            }
        },
        address: {
            required: 'Address is not empty!'
        },
    };

    const handleOrder = (data) => {
        const newData = {
            ...data,
            received: time,
            amount: amount,
            total: total,
            createdDate: new Date(),
            cart: cart
        }
        OrderService.checkout(newData).then(
            (res) => {
                navigate(`/status-order/${res.data.id}`);
                dispatch(getOrder(res.data));
                dispatch(getProductFromCart([]));
            },
            (err) => alert(err.response.data.message)
        )
    }


    return (
        <>
            <div className="checkout-page">
                <form onSubmit={handleSubmit(handleOrder)} >
                    <Row>
                        <Col span={12} push={3}>
                            <Card className="card-option p-5 shadow-700"
                                title={
                                    <Space size={340}>
                                        <Space>
                                            <Avatar className="avt-number" size='default'>1</Avatar>
                                            <div className="title-number">Fullname</div>
                                        </Space>
                                    </Space>
                                }>
                                {
                                    errors?.fullName && errors.fullName.message
                                        ? <div className='text-error'>
                                            <small style={{ color: "red" }}>
                                                <ExclamationCircleOutlined style={{ marginRight: 5 }} />
                                                {errors.fullName.message}
                                            </small>
                                        </div> : <></>
                                }
                                <input placeholder="Ex: Mr/Mrs Joe" name="fullName"
                                    {...register('fullName', errOptions.fullName)} />
                            </Card>
                            <Card className="card-option p-5 shadow-700"
                                title={
                                    <Space size={340}>
                                        <Space>
                                            <Avatar className="avt-number" size='default'>2</Avatar>
                                            <div className="title-number">Contact Number</div>
                                        </Space>
                                    </Space>
                                }>
                                {
                                    errors?.phone && errors.phone.message
                                        ? <div className='text-error'>
                                            <small style={{ color: "red" }}>
                                                <ExclamationCircleOutlined style={{ marginRight: 5 }} />
                                                {errors.phone.message}
                                            </small>
                                        </div> : <></>
                                }
                                <input placeholder="Ex: 0123456789" name="phone"
                                    {...register('phone', errOptions.phone)} />
                            </Card>
                            <Card
                                className="card-option p-5 shadow-700"
                                title={
                                    <Space size={340}>
                                        <Space>
                                            <Avatar className="avt-number" size='default'>3</Avatar>
                                            <div className="title-number">Shipping Address</div>
                                        </Space>
                                    </Space>
                                }>
                                {
                                    errors?.address && errors.address.message
                                        ? <div className='text-error'>
                                            <small style={{ color: "red" }}>
                                                <ExclamationCircleOutlined style={{ marginRight: 5 }} />
                                                {errors.address.message}
                                            </small>
                                        </div> : <></>
                                }
                                <textarea placeholder='Ex: VietNam' name="address"
                                    {...register('address', errOptions.address)} />
                            </Card>

                            <Card className="card-option p-5 shadow-700"
                                title={
                                    <Space size={340}>
                                        <Space>
                                            <Avatar className="avt-number" size='default'>4</Avatar>
                                            <div className="title-number">Delivery Schedule</div>
                                        </Space>
                                    </Space>
                                }>
                                <Row gutter={[10, 10]}>
                                    {dataDate.map((item, index) => {
                                        return (
                                            <Col span={8}>
                                                <Card key={index} className='card-children' onClick={() => setTime(item.time)}>
                                                    <Meta title={item.title} description={item.time} />
                                                </Card>
                                            </Col>)
                                    })}
                                </Row>
                            </Card>
                        </Col>
                        <Col span={5} push={4} className='product-order'>
                            <div className="title-order">Your Order</div>
                            {productFromCart.map((item, index) => {
                                total += (item.quantity * item.product.price)
                                amount += item.quantity
                                return (
                                    <div className="product-item">
                                        <Space>
                                            <p className="quantity">{item.quantity}</p>
                                            <p>x</p>
                                            <p>{item.product.name}</p>
                                        </Space>
                                        <p>{USDollar.format(item.quantity * item.product.price)}</p>
                                    </div>
                                )
                            })}
                            <Divider />
                            <div className="product-item">
                                <p>Total Item</p>
                                <p>{amount}</p>
                            </div>
                            <div className="product-item">
                                <p>Sub Total</p>
                                <p>{USDollar.format(total)}</p>
                            </div>
                            <div className="product-item">
                                <p>Tax</p>
                                <p>Calculated at checkout</p>
                            </div>
                            <div className="product-item">
                                <p>Estimated Shipping</p>
                                <p>Calculated at checkout</p>
                            </div>
                            <Divider />
                            <div className="product-item">
                                <p className="quantity">Total</p>
                                <p>{USDollar.format(total)}</p>
                            </div>
                            <Divider />
                            <div className="product-item">
                                <p>Please click Place order to make order and payment</p>
                            </div>
                            <button className="btn-order" onClick={() => navigate()}>
                                Place Order
                            </button>
                        </Col>
                    </Row>
                </form>
            </div>
        </>
    );
}

export default Checkout;