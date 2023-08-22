import { useNavigate } from "react-router-dom";
import { useEffect, useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import InputSearch from "~/shared/components/InputSearch/InputSearch";
import OrderService from "~/utils/order.service";
import { getAllOrders } from "~/store/product/product.action";
import '~/assets/style/page/manager/OrderManager.scss';
import { Badge, Button, Card, Col, Input, Row, Space, Table } from "antd";
import { SearchOutlined } from '@ant-design/icons';
import dayjs from "dayjs";
import Highlighter from "react-highlight-words";

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

function OrderManager() {

    const navigate = useNavigate();
    const dispatch = useDispatch();
    const searchInput = useRef(null);
    const [searchText, setSearchText] = useState('');
    const [searchedColumn, setSearchedColumn] = useState('');
    let dataOrders = [];
    let USDollar = new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD',
    });

    const orders = useSelector((state) => state.product.orders);

    useEffect(() => {
        OrderService.getAllOrders().then(
            (res) => {
                dispatch(getAllOrders(res.data));
            },
            (err) => alert(err.response.data.message)
        )
    }, [dispatch])

    orders.map((item) => {
        return (
            dataOrders.push({
                id: item.id,
                name: item.fullName,
                phone: item.phone,
                address: item.address,
                createdDate: dayjs(item.createdDate).format('MMMM DD, YYYY'),
                received: item.received,
                total: USDollar.format(item.total),
                status: item.status
            })
        )
    })

    const handleSearch = (selectedKeys, confirm, dataIndex) => {
        confirm();
        setSearchText(selectedKeys[0]);
        setSearchedColumn(dataIndex);
    };
    const handleReset = (clearFilters) => {
        clearFilters();
        setSearchText('');
    };
    const getColumnSearchProps = (dataIndex) => ({
        filterDropdown: ({ setSelectedKeys, selectedKeys, confirm, clearFilters }) => (
            <div
                style={{
                    padding: 8,
                }}
                onKeyDown={(e) => e.stopPropagation()}
            >
                <Input
                    ref={searchInput}
                    placeholder={`Search by phone number`}
                    value={selectedKeys[0]}
                    onChange={(e) => setSelectedKeys(e.target.value ? [e.target.value] : [])}
                    onPressEnter={() => handleSearch(selectedKeys, confirm, dataIndex)}
                    style={{
                        marginBottom: 8,
                        display: 'block',
                    }}
                />
                <Space>
                    <Button
                        type="primary"
                        onClick={() => handleSearch(selectedKeys, confirm, dataIndex)}
                        icon={<SearchOutlined />}
                        size="small"
                        style={{
                            width: 90,
                        }}
                    >
                        Search
                    </Button>
                    <Button
                        onClick={() => clearFilters && handleReset(clearFilters)}
                        size="small"
                        style={{
                            width: 90,
                        }}
                    >
                        Reset
                    </Button>
                </Space>
            </div>
        ),
        filterIcon: (filtered) => (
            <SearchOutlined
                style={{
                    color: filtered ? '#1890ff' : undefined,
                }}
            />
        ),
        onFilter: (value, record) =>
            record[dataIndex].toString().toLowerCase().includes(value.toLowerCase()),
        onFilterDropdownOpenChange: (visible) => {
            if (visible) {
                setTimeout(() => searchInput.current?.select(), 100);
            }
        },
        render: (text) =>
            searchedColumn === dataIndex ? (
                <Highlighter
                    highlightStyle={{
                        backgroundColor: '#ffc069',
                        padding: 0,
                    }}
                    searchWords={[searchText]}
                    autoEscape
                    textToHighlight={text ? text.toString() : ''}
                />
            ) : (
                text
            ),
    });

    
    const columns = [
        {
            title: 'ID',
            dataIndex: 'id',
            key: 'id',
            width: '10px'
        },
        {
            title: 'Full Name',
            dataIndex: 'name',
            key: 'name'
        },
        {
            title: 'Phone Number',
            dataIndex: 'phone',
            key: 'phone',
            width: '110pt',
            ...getColumnSearchProps('phone'),
        },
        {
            title: 'Address',
            dataIndex: 'address',
            key: 'address',
            width: '120pt'
        },
        {
            title: 'Order Date',
            dataIndex: 'createdDate',
            key: 'createdDate',
        },
        {
            title: 'Delivery Schedule',
            dataIndex: 'received',
            key: 'received',
            width: '120pt'
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
            },
            filters: arrStatus,
            onFilter: (value, record) => record.status === value
        }
    ];


    return (
        <>
            <Card className="card-filter">
                <Row>
                    <Col span={4}  className="label">
                        Orders
                    </Col>
                    <Col span={14} push={6} className='input-search'>
                        <InputSearch />
                    </Col>
                </Row>
            </Card>
            <Card style={{ marginTop: '3%' }} headStyle={{ textAlign: 'center' }} bodyStyle={{ padding: 0 }}>
                <Table dataSource={dataOrders} columns={columns}
                    onRow={(record, rowIndex) => {
                        return {
                            onClick: (event) => {
                                navigate(`/admin/orders/${record.id}`)
                            }, // click row
                        }
                    }} />
            </Card>
        </>
    );
}

export default OrderManager;