import { useDispatch } from 'react-redux';
import ProductService from '~/utils/product.service';
import { getAllProducts } from '~/store/product/product.action';
import './InputSearch.scss';
import { SearchOutlined } from '@ant-design/icons';

function InputSearch() {
    const dispatch = useDispatch();

    const onSearch = (e) => {
        if (e.target.value) {
            setTimeout(() => {
                ProductService.search(e.target.value).then(
                    (res) => {
                        dispatch(getAllProducts(res.data));
                    },
                    (err) => alert(err.response.data.message)
                )
            }, 500)
        }
    }

    return (
        <>
            <form>
                <div className="form-input-search">
                    <input type="text" placeholder="Search" required onChange={onSearch} />
                    <button type="submit">
                        <SearchOutlined />
                    </button>
                </div>
            </form>
        </>
    );
}

export default InputSearch;