import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import InputSearch from "~/shared/components/InputSearch/InputSearch";
import TypeProductService from "~/utils/typeProduct.service";
import { getAllProductTypes } from "~/store/product/product.action";
import '~/assets/style/components/table.scss';
import '~/assets/style/page/manager/ProductManager.scss';
import { Button, Card, Col, Row, Table } from "antd";
import { PlusCircleOutlined } from '@ant-design/icons';

const columns = [
    {
        title: 'No',
        dataIndex: 'index',
        key: 'idex',
        width: '10%'
    },
    {
        title: 'Name',
        dataIndex: 'name',
        key: 'name',
        width: '35%'
    },
    {
        title: 'ID',
        dataIndex: 'id',
        key: 'id',
        width: '25%'
    },
    {
        title: 'Icon',
        dataIndex: 'icon',
        key: 'icon'
    },
]

function TypeProductManager() {

    let dataTypes = [];
    const dispatch = useDispatch();
    let navigate = useNavigate();

    let types = useSelector((state) => state.product.types);

    useEffect(() => {
        TypeProductService.getAllTypes().then(
            (res) => {
                dispatch(getAllProductTypes(res.data))
            },
            (err) => alert(err.response.data.message)
        )
    }, [dispatch])

    types.map((item, index) => {
        return (
            dataTypes.push({
                index: index + 1,
                id: item.id,
                name: item.name,
                icon: <img style={{ width: "10%" }} alt="" src={`http://localhost:8080/api/uploads/products/${item.icon}`} />,
            })
        )
    })


    return (
        <>
            <Card className="card-filter">
                <Row>
                    <Col span={4} className="label">
                        Type Product
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
                <Table dataSource={dataTypes} columns={columns}
                onRow={(record, rowIndex) => {
                    return {
                        onClick: (event) => {
                            // navigate(`/admin/product/edit/${yrecord.id}`)
                        }, // click row
                    }
                }}
                />
            </Card>
        </>
    );
}

export default TypeProductManager;