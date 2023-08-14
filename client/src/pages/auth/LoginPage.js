import { Link, useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import { useDispatch } from "react-redux";
import { isLogin } from "~/store/product/product.action";
import AuthService from "~/utils/auth.service";
import '../../assets/style/auth/AuthPage.scss';
import { Menu, notification } from "antd";
import { ExclamationCircleOutlined } from '@ant-design/icons';

function LoginPage() {

    const { register, handleSubmit, formState: { errors } } = useForm();
    const [api, contextHolder] = notification.useNotification();
    const dispatch = useDispatch();
    let navigate = useNavigate();

    const errOptions = {
        username: { required: 'Username không được trống' },
        password: {
            required: 'Password không được trống',
            // minLength: {
            //     value: 8,
            //     message: 'Password tối thiểu ít nhất 8 kí tự'
            // }
        }
    };

    const onSubmit = async (data) => {
        AuthService.login(data)
            .then(
                (res) => {
                    dispatch(isLogin(true));
                    if (res.role === '[ROLE_ADMIN]')
                        navigate(`/admin`);
                    else {
                        navigate(`/`);
                    }
                },
                (err) => {
                    api.error({
                        message: `Notification Login`,
                        description: `${err.response.data}`,
                        placement: 'topRight'
                    });
                }
            )
    };

    return (
        <>
        {contextHolder}
            <div className="auth-page">
                <div className="auth-header">
                    <h3>Đăng nhập</h3>
                    <Menu
                        defaultSelectedKeys={"1"}
                        className="auth-menu"
                        mode="horizontal"
                    >
                        <Menu.Item key="1">
                            <Link to="/login">Đăng nhập</Link>
                        </Menu.Item>
                        <Menu.Item key="2">
                            <Link to="/register">Đăng ký</Link>
                        </Menu.Item>
                    </Menu>

                </div>

                <form onSubmit={handleSubmit(onSubmit)} className="form-auth">
                    <div className="form-group">
                        <label className="label-form">Username</label>
                        <div className='input-group'>
                            <input
                                type="text"
                                name="username"
                                {...register('username', errOptions.username)} />
                            {
                                errors?.username && errors.username.message
                                    ? <div className='text-error'>
                                        <small>
                                            <ExclamationCircleOutlined style={{ marginRight: 5 }} />
                                            {errors.username.message}
                                        </small>
                                    </div> : <></>
                            }

                        </div>

                    </div>
                    <div className="form-group">
                        <label>Password</label>
                        <div className='input-group'>
                            <input type="password" name="password" {...register('password', errOptions.password)} />
                            {
                                errors?.password && errors.password.message
                                    ? <div className='text-error'>
                                        <small>
                                            <ExclamationCircleOutlined style={{ marginRight: 5 }} />
                                            {errors.password.message}
                                        </small>
                                    </div> : <></>
                            }
                        </div>
                    </div>
                    <div className="forgot-password">
                        <Link to='/'>Forgot password ?</Link>
                    </div>
                    <button className="btn-submit">Đăng nhập</button>
                </form>
            </div>


        </>
    );
}

export default LoginPage;