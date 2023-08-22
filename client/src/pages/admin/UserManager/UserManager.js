import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import InputSearch from "~/shared/components/InputSearch/InputSearch";
import UserService from "~/utils/user.service";
import { getAllUsers } from "~/store/product/product.action";
import '~/assets/style/components/table.scss';
import '~/assets/style/page/manager/ProductManager.scss';
import { Card, Col, Row, Table, Tag } from "antd";

function UserManager() {

    let dataUsers = [];
    const dispatch = useDispatch();

    let users = useSelector((state) => state.product.users);

    useEffect(() => {
        UserService.getAllUsers().then(
            (res) => {
                dispatch(getAllUsers(res.data))
            },
            (err) => alert(err.response.data.message)
        )
    }, [dispatch])

    users.map((item) => {
        return (
            dataUsers.push({
                id: item.id,
                name: item.fullName,
                phone: item.phone,
                email: item.email,
                username: item.username,
                role: item.roles
            })
        )
    })

    const columns = [
        {
            title: 'ID',
            dataIndex: 'id',
            key: 'id',
        },
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name',
            width: '30%'
        },
        {
            title: 'Phone number',
            dataIndex: 'phone',
            key: 'phone',
            width: '15%'
        },
        {
            title: 'Username',
            dataIndex: 'username',
            key: 'username',
            width: '15%'
        },
        {
            title: 'Email',
            dataIndex: 'email',
            key: 'email',
        },
        {
            title: 'Role',
            dataIndex: 'role',
            key: 'role',
            render: (_, { role }) => {
                return (
                    <Tag color={role.includes('ROLE_ADMIN') ? 'red' : 'geekblue'}
                        key={role.includes('ROLE_ADMIN') ? 'ROLE_ADMIN' : 'ROLE_USER'}>
                        {role.map((item) => item.toUpperCase())}
                    </Tag>
                );
            },
        }
    ]

    return (
        <>
            <Card className="card-filter">
                <Row>
                    <Col span={4} push={1} className="label">
                        Users
                    </Col>
                    <Col span={14} push={7} className='input-search'>
                        <InputSearch />
                    </Col>
                    {/* <Col span={4} push={2} >
                        <Button className='btn-filter' onClick={() => navigate(`/admin/product/add`)}><PlusCircleOutlined /> Create Product</Button>
                        <Button className='btn-filter'>Filter <FilterOutlined /></Button>
                    </Col> */}
                </Row>
            </Card>
            <Card style={{ marginTop: '3%' }} headStyle={{ textAlign: 'center' }} bodyStyle={{ padding: 0 }}>
                <Table dataSource={dataUsers} columns={columns}
                    onRow={(record, rowIndex) => {
                        return {
                            onClick: (event) => {
                                // navigate(`/admin/product/edit/${record.id}`)
                            }, // click row
                        }
                    }}
                />
            </Card>
        </>
    );
}

export default UserManager;