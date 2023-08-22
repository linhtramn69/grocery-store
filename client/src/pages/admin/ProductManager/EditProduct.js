import { useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { useParams } from "react-router-dom";
import FormProduct from "~/shared/components/FormProduct/FormProduct";
import ProductService from "~/utils/product.service";
import { getProductById } from "~/store/product/product.action";

function EditProduct() {

    let { id } = useParams();
    const dispatch = useDispatch();
    const [product, setProduct] = useState();

    useEffect(() => {
        ProductService.getProductById(id).then(
            (res) => {
                setProduct(res.data);
                dispatch(getProductById(res.data));
            },
            (err) => alert(err.response.data.message)
        )
    }, [dispatch, id])

    return (
        <>
            {product ?
                <FormProduct props={product} />
                : <></>
            }

        </>
    );
}

export default EditProduct;