import { useEffect, useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import InputSearch from "~/shared/components/InputSearch/InputSearch";
import ProductService from "~/utils/product.service";
import TypeProductService from "~/utils/typeProduct.service";
import { getAllProducts, getAllProductTypes } from "~/store/product/product.action";
import '~/assets/style/components/table.scss';
import '~/assets/style/page/manager/ProductManager.scss';
import { Badge, Button, Card, Col, Input, Row, Space, Table, Tag } from "antd";
import { PlusCircleOutlined, SearchOutlined } from '@ant-design/icons';
import Highlighter from 'react-highlight-words';

function ProductManager() {

    let allProducts = [];
    const [searchText, setSearchText] = useState('');
    const [searchedColumn, setSearchedColumn] = useState('');
    const searchInput = useRef(null);
    const dispatch = useDispatch();
    let navigate = useNavigate();
    let USDollar = new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD',
    });

    let products = useSelector((state) => state.product.products);
    let types = useSelector((state) => state.product.types);

    const arrType = types.map((value) => {
        return {
            value: value.id,
            text: value.name
        }
    })

    useEffect(() => {
        ProductService.getAllProducts().then(
            (res) => {
                dispatch(getAllProducts(res.data))
            },
            (err) => alert(err.response.data.message)
        )
        TypeProductService.getAllTypes().then(
            (res) => {
                dispatch(getAllProductTypes(res.data))
            },
            (err) => alert(err.response.data.message)
        )
    }, [dispatch])

    products.map((item) => {
        return (
            allProducts.push({
                id: item.id,
                name: item.name,
                price: USDollar.format(item.price),
                image: <img style={{ width: "100%" }} alt="" src={`http://localhost:8080/api/uploads/products/${item.image}`} />,
                types: item.types.map((data) => data.id),
                status: item.status,
                quantity: item.quantity
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
                    placeholder={`Search by name product...`}
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
            width: 30,
            defaultSortOrder: 'ascend',
            sorter: (a, b) => a.id - b.id,
        },
        {
            title: 'Images',
            dataIndex: 'image',
            key: 'img',
            width: 120
        },
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name',
            width: 350,
            ...getColumnSearchProps('name'),
        },
        {
            title: 'Product Types',
            dataIndex: 'types',
            key: 'types',
            width: 300,
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
            filters: arrType,
            onFilter: (value, record) => record.types.includes(value)
        },
        {
            title: 'Price',
            dataIndex: 'price',
            key: 'price',
        },
        {
            title: 'Quantity',
            dataIndex: 'quantity',
            key: 'quantity',
        },
        {
            title: 'Status',
            dataIndex: 'status',
            key: 'status',
            width: 100,
            render: (_, { status }) => {
                return (
                    <Badge count={ status ? 'Publish' : 'Hidden' } 
                        showZero 
                        color={ status ? 'rgba(1, 147, 118)' : '#8c8c8c' } 
                    />
                )
            }
        },
    ]


    return (
        <>
            <Card className="card-filter">
                <Row>
                    <Col span={4}  className="label">
                        Product
                    </Col>
                    <Col span={14} push={3} className='input-search'>
                        <InputSearch />
                    </Col>
                    <Col span={4} push={2} >
                        <Button className='btn-filter' onClick={() => navigate(`/admin/product/add`)}><PlusCircleOutlined /> Create Product</Button>
                        {/* <Button className='btn-filter'>Filter <FilterOutlined /></Button> */}
                    </Col>
                </Row>
            </Card>
            <Card style={{ marginTop: '3%' }} headStyle={{ textAlign: 'center' }} bodyStyle={{ padding: 0 }}>
                <Table dataSource={allProducts} columns={columns}
                onRow={(record, rowIndex) => {
                    return {
                        onClick: (event) => {
                            navigate(`/admin/product/edit/${record.id}`)
                        }, // click row
                    }
                }}
                />
            </Card>
        </>
    );
}

export default ProductManager;