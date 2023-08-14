import { useForm } from "react-hook-form";
import AuthService from "~/utils/auth.service";
import '../../assets/style/auth/AuthPage.scss';
import { Link, useNavigate } from "react-router-dom";
import { ExclamationCircleOutlined } from '@ant-design/icons';
import { Menu } from "antd";

function RegisterPage() {

    const { register, getValues, handleSubmit, formState: { errors } } = useForm();
    let navigate = useNavigate();

    const errOptions = {
        phone: {
            minLength: {
                value: 10,
                message: "Số điện thoại chưa chính xác"
            }
        },
        email: {
            required: "Email không được trống",
            pattern: {
                value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                message: "Email không hợp lệ"
            }
        },
        username: { required: "Username không được trống" },
        password: {
            required: "Password không được trống",
            // minLength: {
            //     value: 8,
            //     message: "Password tối thiểu ít nhất 8 kí tự"
            // }
        },
        confirmPassword: {
            validate: (value) => {
                const pw = getValues("password");
                if (pw !== value) {
                    return "Password không trùng khớp"
                }
            }
        }
    };

    const onSubmit = async (data) => {
        AuthService.register(data).then(
            () => {
                alert("Register successfully");
                navigate(`/login`);
            },
            (err) => alert(err.response.data.message)
        )
    };

    return (
        <>
            <div className="auth-page">
                <div className="auth-header">
                    <h3>Đăng ký</h3>
                    <Menu
                        defaultSelectedKeys={"2"}
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
                        <label>Họ tên</label>
                        <div className='input-group'>
                            <input type="text" name="fullname" {...register("fullname")} />
                        </div>

                    </div>
                    <div className="form-group">
                        <label>Số điện thoại</label>
                        <div className='input-group'>
                            <input type="text" name="phone" {...register("phone", errOptions.phone)} />
                            {
                                errors?.phone && errors.phone.message
                                    ? <div className='text-error'>
                                        <small>
                                            <ExclamationCircleOutlined style={{ marginRight: 5 }} />
                                            {errors.phone.message}
                                        </small>
                                    </div> : <></>
                            }
                        </div>

                    </div>
                    <div className="form-group">
                        <label>Email</label>
                        <div className='input-group'>
                            <input type="email" name="email" {...register("email", errOptions.email)} />
                            {
                                errors?.email && errors.email.message
                                    ? <div className='text-error'>
                                        <small>
                                            <ExclamationCircleOutlined style={{ marginRight: 5 }} />
                                            {errors.email.message}
                                        </small>
                                    </div> : <></>
                            }
                        </div>

                    </div>
                    <div className="form-group">
                        <label>Username</label>
                        <div className='input-group'>
                            <input type="text" name="username" {...register("username", errOptions.username)} />
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
                            <input type="password" name="password" {...register("password", errOptions.password)} />
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
                    <div className="form-group">
                        <label>Confirm password</label>
                        <div className='input-group'>
                            <input type="password" name="confirm-password" {...register("confirmPassword", errOptions.confirmPassword)} />
                            {
                                errors?.confirmPassword && errors.confirmPassword.message
                                    ? <div className='text-error'>
                                        <small>
                                            <ExclamationCircleOutlined style={{ marginRight: 5 }} />
                                            {errors.confirmPassword.message}
                                        </small>
                                    </div> : <></>
                            }
                        </div>

                    </div>
                    <button className="btn-submit">Đăng ký</button>
                </form>
            </div>

        </>

    );
}

export default RegisterPage;