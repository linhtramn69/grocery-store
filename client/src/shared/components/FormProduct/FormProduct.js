import { useForm } from "react-hook-form";
import { useDispatch, useSelector } from "react-redux";
import { useEffect, useState } from "react";
import ProductService from "~/utils/product.service";
import TypeProductService from "~/utils/typeProduct.service";
import { getAllProductTypes } from "~/store/product/product.action";
import './FormProduct.scss';
import { Card, Col, Divider, message, Row, Select, Space, Switch } from "antd";
import { ExclamationCircleOutlined, InboxOutlined } from '@ant-design/icons';
import Dragger from "antd/es/upload/Dragger";

function FormProduct({ props }) {

    const options = [];
    const { register, handleSubmit, formState: { errors } } = useForm();
    const dispatch = useDispatch();
    let [arrType, setArrType] = useState(props?.types.map((item) => { return item.id }));
    let [img, setImg] = useState(props?.image);
    let [checked, setChecked] = useState(props?.status);
    const [messageApi, contextHolderMess] = message.useMessage();

    const types = useSelector((state) => state.product.types);

    const errOptions = {
        name: {
            required: 'Name is not empty!'
        },
        price: {
            required: 'Price is not empty!'
        },
        description: {
            required: 'Description is not empty!'
        },
        quantity: {
            required: 'Quantity is not empty!'
        }
    };
    const file = {
        name: 'file',
        multiple: true,
        action: 'https://www.mocky.io/v2/5cc8019d300000980a055e76',
        onChange(info) {
            const { status } = info.file;
            if (status !== 'uploading') {
                console.log(info.file, info.fileList);
            }
            if (status === 'done') {
                setImg(info.fileList[0].originFileObj);
                message.success(`${info.file.name} file uploaded successfully.`);
            } else if (status === 'error') {
                message.error(`${info.file.name} file upload failed.`);
            }
        },
        onDrop(e) {
            console.log('Dropped files', e.dataTransfer.files);
        },
    };

    useEffect(() => {
        TypeProductService.getAllTypes().then(
            (res) => {
                dispatch(getAllProductTypes(res.data));
            },
            (err) => alert(err.response.data.message)
        )
    }, [dispatch])

    types.map((item) => {
        return (
            options.push({
                label: item.name,
                value: item.id,
            })
        )
    })

    const onFinish = (data) => {
        const form = new FormData();
        form.append('image', new Blob([img], {
            type: "multipart/form-data"
        }), img.name);
        form.append('product', new Blob([JSON.stringify({
            name: data.name,
            description: data.description,
            price: data.price,
            quantity: data.quantity,
            status: checked,
            types:
                arrType.map((item) => {
                    return ({
                        id: item
                    })
                })
        })], {
            type: "application/json"
        }));
        props ? handleEdit(form) : handleAdd(form);
    }
    const handleAdd = (data) => {
        ProductService.createProduct(data).then(
            (res) => {
                messageApi.open({
                    type: 'success',
                    content: 'Successfully added product!',
                });
                // navigate(`/admin/products`);
            },
            (err) => {
                messageApi.open({
                    type: 'error',
                    content: 'Failed to add product!',
                });
            }
        )
    }
    const handleEdit = (data) => {
        ProductService.updateProductById(props.id, data).then(
            (res) => {
                messageApi.open({
                    type: 'success',
                    content: 'Successfully updated product!',
                });
                // navigate(`/admin/products`);
            },
            (err) => {
                messageApi.open({
                    type: 'error',
                    content: 'Failed to update product!',
                });
            }
        )
    }

    return (
        <>
            {contextHolderMess}
            <form className="form-add-product" onSubmit={handleSubmit(onFinish)}>
                <Row>
                    <Col span={6} className="title-col-left">
                        <p className="title">
                            Featured Image
                        </p>
                        <p className="text">
                            Upload your product featured image here
                        </p>
                    </Col>
                    <Col span={15} push={2}>
                        <Card className="card-col-right" >
                            <Dragger {...file}
                            >
                                <p className="ant-upload-drag-icon">
                                    <InboxOutlined />
                                </p>

                                <p className="ant-upload-text">Click or drag file to this area to upload</p>
                                <p className="ant-upload-hint">
                                    Support for a single or bulk upload. Strictly prohibited from uploading company data or other
                                    banned files.
                                </p>
                            </Dragger>
                            <Space className="list-card-img" size={10}>
                                {props && img ?
                                    <Card className="card-img">
                                        <img src={`http://localhost:8080/api/uploads/products/${props.image}`} alt="" />
                                    </Card>
                                    : <></>
                                }
                            </Space>
                        </Card>
                    </Col>
                </Row>
                <Divider dashed />
                <Row>
                    <Col span={6} className="title-col-left">
                        <p className="title">
                            Description
                        </p>
                        <p className="text">
                            Edit your product description and necessary information from here
                        </p>
                    </Col>
                    <Col span={15} push={2}>
                        <Card className="card-col-right">
                            <div className="form-group">
                                <label>Name</label>
                                {
                                    errors?.name && errors.name.message
                                        ? <div className='text-error'>
                                            <small style={{ color: "red" }}>
                                                <ExclamationCircleOutlined style={{ marginRight: 5 }} />
                                                {errors.name.message}
                                            </small>
                                        </div> : <></>
                                }
                                <input
                                    defaultValue={props?.name}
                                    {...register('name', errOptions.name)}
                                />
                            </div>
                            <div className="form-group">
                                <label>Price</label>
                                {
                                    errors?.price && errors.price.message
                                        ? <div className='text-error'>
                                            <small style={{ color: "red" }}>
                                                <ExclamationCircleOutlined style={{ marginRight: 5 }} />
                                                {errors.price.message}
                                            </small>
                                        </div> : <></>
                                }
                                <input
                                    defaultValue={props?.price}
                                    {...register('price', errOptions.price)}
                                />
                            </div>
                            <div className="form-group">
                                <label>Quantity</label>
                                {
                                    errors?.quantity && errors.quantity.message
                                        ? <div className='text-error'>
                                            <small style={{ color: "red" }}>
                                                <ExclamationCircleOutlined style={{ marginRight: 5 }} />
                                                {errors.quantity.message}
                                            </small>
                                        </div> : <></>
                                }
                                <input
                                    defaultValue={props?.quantity}
                                    {...register('quantity', errOptions.quantity)}
                                />
                            </div>
                            <div className="form-group" >
                                <label>Type Product</label>
                                <Select
                                    mode="multiple"
                                    allowClear
                                    style={{
                                        width: '100%',
                                    }}
                                    placeholder="Please select"
                                    defaultValue={props?.types.map((item) => {
                                        return {
                                            label: item.name,
                                            value: item.id
                                        }
                                    })}
                                    options={options}
                                    onChange={(e) => {
                                        setArrType(e);
                                    }}
                                />
                            </div>
                            <div className="form-group">
                                <label>Description</label>
                                {
                                    errors?.description && errors.description.message
                                        ? <div className='text-error'>
                                            <small style={{ color: "red" }}>
                                                <ExclamationCircleOutlined style={{ marginRight: 5 }} />
                                                {errors.description.message}
                                            </small>
                                        </div> : <></>
                                }
                                <textarea
                                    defaultValue={props?.description}
                                    {...register('description', errOptions.description)}
                                />
                            </div>
                            <div className="form-group">
                                <label>Status</label>
                                <Switch style={{ marginLeft: '10pt' }}
                                    defaultValue={props?.status}
                                    defaultChecked={props?.status}
                                    checkedChildren="Publish" unCheckedChildren="Hidden"
                                    onChange={(value) => setChecked(value)}
                                />
                            </div>
                        </Card>
                    </Col>
                </Row>
                <Divider dashed />
                <Space className="btn-group">
                    <button className="btn-back">
                        Back
                    </button>
                    {props ?
                        <button className="btn-submit">
                            Edit Product
                        </button>
                        :
                        <button className="btn-submit" >
                            Add Product
                        </button>
                    }
                </Space>
            </form>

        </>
    );
}

export default FormProduct;