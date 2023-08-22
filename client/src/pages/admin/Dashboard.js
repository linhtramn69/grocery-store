import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import ProductService from "~/utils/product.service";
import OrderService from "~/utils/order.service";
import CardDashboard from "~/shared/components/CardDashboard/CardDashboard";
import ColumnChart from "~/shared/components/Chart/ColumnChart";
import { icon } from '~/assets/images';
import { Badge, Card, Col, Row, Space, Table, Tag } from "antd";
import dayjs from "dayjs";
import moment from 'moment';

const arrStatus = [
    {
        value: 0,
        text: 'Order Processing',
        color: '#faad14'
    },
    {
        value: 1,
        text: 'Order Delivery',
        color: '#4A55A2'
    },
    {
        value: 2,
        text: 'Order Complete',
        color: 'rgba(1, 147, 118)'
    },
    {
        value: 3,
        text: 'Order Cancelled',
        color: '#8c8c8c'
    },
]
const dataChart = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December']

function Dashboard() {

    
    const columnsOrder = [
        {
            title: 'Phone Number',
            dataIndex: 'phone',
            key: 'phone',
            width: '100pt'
        },
        {
            title: 'Order Date',
            dataIndex: 'createdDate',
            key: 'createdDate',
            defaultSortOrder: 'descend',
            sorter: (a, b) => moment(a.createdDate).unix() - moment(b.createdDate).unix()
        },
        {
            title: 'Total',
            dataIndex: 'total',
            key: 'total',
        },
        {
            title: 'Status',
            dataIndex: 'status',
            key: 'status',
            render: (_, { status }) => {
                return (
                    <Badge count={arrStatus[status].text}
                        showZero
                        color={arrStatus[status].color}
                    />
                )
            }
        }
    ];
    const columnsPopularProduct = [
        {
            title: 'ID',
            dataIndex: 'id',
            key: 'id',
        },
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name',
            width: 400
        },
        {
            title: 'Product Types',
            dataIndex: 'types',
            key: 'types',
            width: 200,
            render: (_, { types }) => (
                <>
                    {types.map((tag) => {
                        let color = 'lime';
                        switch (tag) {
                            case 'F&V':
                            case 'FRUIT':
                            case 'VEGE':
                                color = 'green';
                                break;
                            case 'M&F':
                            case 'FISH':
                            case 'MEAT':
                                color = 'geekblue';
                                break;
                            case 'N&B':
                            case 'SNACK':
                            case 'NOODLE':
                                color = 'gold';
                                break;
                            case 'YOGURT':
                            case 'BUTTER':
                            case 'MILK':
                            case 'DAIRY':
                                color = 'purple';
                                break;
                            case 'S&S':
                            case 'OIL':
                            case 'COOKING':
                                color = 'cyan';
                                break;
                            case 'JAM':
                            case 'CEREAL':
                            case 'BREAKFAST':
                            case 'BREAD':
                                color = 'volcano';
                                break;
                            default:
                                break;
                        }
                        return (
                            <Tag color={color} key={tag}>
                                {tag.toUpperCase()}
                            </Tag>
                        );
                    })}
                </>
            ),
        },
        {
            title: 'Price',
            dataIndex: 'price',
            key: 'price',
            width: 100
        },
        {
            title: 'Quantity',
            dataIndex: 'quantity',
            key: 'quantity',
            defaultSortOrder: 'descend',
            sorter: (a, b) => a.quantity - b.quantity,
        },
        {
            title: 'Status',
            dataIndex: 'status',
            key: 'status',
            width: 130,
            render: (_, { status }) => {
                return (
                    <Badge count={status ? 'Publish' : 'Hidden'}
                        showZero
                        color={status ? 'rgba(1, 147, 118)' : '#8c8c8c'}
                    />
                )
            }
        },
    ]

    const navigate = useNavigate();
    const [dataProduct, setDataProduct] = useState([]);
    const [dataOrder, setDataOrders] = useState([]);
    const [dataSale, setDataSale] = useState([]);
    let arrProduct = [];
    let arrOrders = [];
    let arrProgressOrders = [];
    let arrSale = [];
    let total =0;

    let USDollar = new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD',
    });

    useEffect(() => {
        ProductService.getPopularProductsByYear(2023).then(
            (res) => {
                setDataProduct(res.data);
            },
            (err) => alert(err.response.data.message)
        )
        OrderService.getAllOrders().then(
            (res) => {
                setDataOrders(res.data);
            },
            (err) => alert(err.response.data.message)
        )
        OrderService.getTotalByYear(2023).then(
            (res) => {
                setDataSale(res.data);
            },
            (err) => alert(err.response.data.message)
        )
    }, [])

    dataProduct.map((item) => {
        return (
            arrProduct.push({
                id: item.id,
                name: item.name,
                price: USDollar.format(item.price),
                quantity: item.quantity,
                types: item.types.map((data) => data.id),
                status: item.status
            })
        )
    })

    dataOrder.map((item) => {
        return (
            arrOrders.push({
                id: item.id,
                phone: item.phone,
                createdDate: dayjs(item.createdDate).format('MMMM DD, YYYY'),
                total: USDollar.format(item.total),
                status: item.status
            })
        )
    })

    dataOrder.filter(function (value) {
        if (value.status === 0) {
            return (
                arrProgressOrders.push({
                    id: value.id,
                    phone: value.phone,
                    createdDate: dayjs(value.createdDate).format('MMMM DD, YYYY'),
                    total: USDollar.format(value.total),
                    status: value.status
                })
            )
        }
    });

    dataSale.map((item, index) => {
        return (
            arrSale.push({
                type: dataChart[index],
                value: item.total
            }),
            total += item.total
        )
    })

    const dataCard = [
        {
            iconName: icon.dollar_square,
            title: {
                first: 'Total Revenue',
                second: 'Last 30 Days'
            },
            total: USDollar.format(total),
        },
        {
            iconName: icon.cart_square,
            title: {
                first: 'Total Order',
                second: 'Last 30 Days'
            },
            total: arrOrders.length
        },
        {
            iconName: icon.revenue_square,
            title: {
                first: 'Todays Revenue',
                second: 'Last 30 Days'
            },
            total: 0.00
        },
        {
            iconName: icon.store_square,
            title: {
                first: 'Total Products',
                second: 'Last 30 Days'
            },
            total: arrProduct.length
        }
    ]


    return (
        <>
            <Space direction="vertical" style={{ width: '100%' }} size={50}>
                <Row gutter={18}>
                    {dataCard.map((item, index) => {
                        return (
                            <Col span={6} key={index}>
                                <CardDashboard props={item} />
                            </Col>
                        )
                    })}
                </Row>
                <ColumnChart data={arrSale} title='Sale History' />
                <Row gutter={24}>
                    <Col span={12}>
                        <Card title='Progress Orders' headStyle={{ textAlign: 'center' }} bodyStyle={{ padding: 0 }}>
                            <Table dataSource={arrProgressOrders} columns={columnsOrder}
                                onRow={(record, rowIndex) => {
                                    return {
                                        onClick: (event) => {
                                            navigate(`/admin/orders/${record.id}`)
                                        }, // click row
                                    }
                                }} />
                        </Card>
                    </Col>
                    <Col span={12}>
                        <Card title='Recent Orders' headStyle={{ textAlign: 'center' }} bodyStyle={{ padding: 0 }}>
                            <Table dataSource={arrOrders} columns={columnsOrder}
                                onRow={(record, rowIndex) => {
                                    return {
                                        onClick: (event) => {
                                            navigate(`/admin/orders/${record.id}`)
                                        }, // click row
                                    }
                                }} />
                        </Card>
                    </Col>
                </Row>
                <Card title='Popular Products / 2023' headStyle={{ textAlign: 'center' }} bodyStyle={{ padding: 0 }}>
                    <Table dataSource={arrProduct} columns={columnsPopularProduct} />
                </Card>
            </Space>
        </>
    );
}

export default Dashboard;