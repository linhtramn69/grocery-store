import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useParams } from "react-router-dom";
import { getOrder, getProductFromOrder } from "~/store/product/product.action";
import OrderService from "~/utils/order.service";
import OrderProductService from "~/utils/orderProduct.service";
import '~/assets/style/page/StatusOrderPage.scss';
import { Card, Col, Row, Space, Steps, Table } from "antd";
import Title from "antd/es/typography/Title";
import dayjs from "dayjs";

const columns = [
    {
        title: '',
        dataIndex: 'image',
        key: 'image',
        width: 100
    },
    {
        title: 'Item',
        dataIndex: 'name',
        key: 'name',
        width: 250
    },
    {
        title: 'Quantity',
        dataIndex: 'quantity',
        key: 'quantity',
    },
    {
        title: 'Price',
        dataIndex: 'price',
        key: 'price',
    },
];

function StatusOrder() {

    let { id } = useParams();
    const dispatch = useDispatch();
    let dataFromOrder = [];
    let amount = 0;

    let USDollar = new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD',
    });

    let order = useSelector((state) => state.product.order);
    let productFromOrder = useSelector((state) => state.product.productFromOrder);

    useEffect(() => {
        OrderService.getOrderById(id).then(
            (res) => {
                dispatch(getOrder(res.data));
            },
            (err) => alert(err.response.data.message)
        )
        OrderProductService.getOrderDetails(id).then(
            (res) => {
                dispatch(getProductFromOrder(res.data));
            },
            (err) => alert(err.response.data.message)
        )
    }, [id, dispatch])

    productFromOrder.map((item) => {
        return (
            dataFromOrder.push({
                key: item.product.id,
                id: item.product.id,
                name: <div style={{ textAlign: "left", marginLeft: "40%"}}>
                    <div>{item.product.name}</div>
                    <span style={{color: "rgba(var(--color-accent-500))", fontSize: "13px", fontWeight: "600"}}>{USDollar.format(item.product.price)}</span>
                </div>,
                price: USDollar.format(item.product.price * item.quantity),
                image: <img style={{ width: "100%"}} alt="" src={`http://localhost:8080/api/uploads/products/${item.product.image}`} />,
                quantity: item.quantity
            }),
            amount += item.quantity
        )
    })


    return (
        <div className="status-order-page">
            <Card className="card-info"
                title={
                    <div className="badge-status">
                        <Space>
                            <b>Order Status :</b>
                            {order.status === 0 ?
                                <button className="btn-status btn-warning">Order Processing</button>
                                : order.status === 1 ?
                                    <button className="btn-status btn-load">Order Out For Delivery</button>
                                    : <button className="btn-status btn-success">Order Completed</button>
                            }
                        </Space>
                        <Space>
                            <b>Payment Status :</b>
                            <button className="btn-status btn-success">Payment success</button>
                        </Space>
                    </div>
                }>
                <Row gutter={24} className="list-card-children">
                    <Col span={6}>
                        <Card title='Order Number' className="card-children">
                            {order.id}
                        </Card>
                    </Col>
                    <Col span={6}>
                        <Card title='Date' className="card-children">
                            {dayjs(order.createdDate).format('MMMM DD, YYYY')}
                        </Card>
                    </Col>
                    <Col span={6}>
                        <Card title='Total' className="card-children">
                            {USDollar.format(order.total)}
                        </Card>
                    </Col>
                    <Col span={6}>
                        <Card title='Payment Method' className="card-children">
                            BANK
                        </Card>
                    </Col>
                </Row>
                <Steps
                    current={order.status}
                    labelPlacement="vertical"
                    style={{ marginBottom: 30 }}
                    size="large"
                    items={[
                        {
                            title: 'In Progress',
                        },
                        {
                            title: 'Delivery',
                        },
                        {
                            title: 'Finished',
                        }
                    ]}
                />
                <Row>
                    <Col span={5} push={2}>
                        <Title level={4} style={{ fontWeight: 700, marginBottom: 35 }}>Total Amount</Title>
                        <div className="item-info">
                            <p className="title">Sub Total</p>
                            <p className="text-info">{USDollar.format(order.total)}</p>
                        </div>
                        <div className="item-info">
                            <p className="title">Shipping Charge</p>
                            <p>Estimate</p>
                        </div>
                        <div className="item-info">
                            <p className="title">Tax</p>
                            <p>Estimate</p>
                        </div>
                        <div className="item-info">
                            <p className="title">Discount</p>
                            <p>Estimate</p>
                        </div>
                        <div className="item-info">
                            <p className="title">Total</p>
                            <p>{USDollar.format(order.total)}</p>
                        </div>
                    </Col>
                    <Col span={8} push={9}>
                        <Title level={4} style={{ fontWeight: 700, marginBottom: 35 }}>Order Details</Title>
                        <div className="item-info">
                            <p className="title">Full Name</p>
                            <p>{order.fullName}</p>
                        </div>
                        <div className="item-info">
                            <p className="title">Phone Contact</p>
                            <p>{order.phone}</p>
                        </div>
                        <div className="item-info">
                            <p className="title">Shipping Address</p>
                            <p>{order.address}</p>
                        </div>
                        <div className="item-info">
                            <p className="title">Total Item</p>
                            <p className="text-info">{amount} Items</p>
                        </div>
                        <div className="item-info">
                            <p className="title">Delivery Times</p>
                            <p>{order.received}</p>
                        </div>
                    </Col>
                </Row>
                <Table scroll={{
                    y: 200,
                }} pagination={false} dataSource={dataFromOrder} columns={columns} />
            </Card>
        </div>

    );
}

export default StatusOrder;