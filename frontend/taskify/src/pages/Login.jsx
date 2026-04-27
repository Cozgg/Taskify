import { Button, Form, Input, message } from "antd";
import { LockOutlined, UserOutlined } from "@ant-design/icons";
import { useContext, useState } from "react";
import Apis, { endpoints } from "../utils/Apis";
import cookies from "react-cookies";
import { Link, useNavigate } from "react-router-dom";
import { MyContext } from "../utils/context/MyContext";
import "./Login.css";


const Login = () => {
    const [loading, setLoading] = useState(false);
    const [, dispatch] = useContext(MyContext);

    const nav = useNavigate();
    const login = async (values) => {
        try {
            setLoading(true);
            const respone = await Apis.post(endpoints['login'], {
                'username': values.username,
                'password': values.password,
            })

            if (respone.status === 200) {
                cookies.save('token', respone.data.data.token);
                console.log('Token saved:', respone.data.data.token);
                cookies.save('refreshToken', respone.data.data.refreshToken);
                cookies.save('userdata', respone.data);
                console.log('Userdata saved:', respone.data);

                dispatch({
                    'type': 'login',
                    'payload': {
                        'userdata': respone.data,
                    }
                })
                message.success('Đăng nhập thành công');
                const role = respone?.data?.data?.role;
                nav(role === 'ADMIN' ? '/admin/dashboard' : '/home');
            }

        } catch (err) {
            message.error('Sai tài khoản hoặc mật khẩu!');
            console.error(err);
        }
        finally {
            setLoading(false);
        }
    }

    return (
        <div className="kanban-login-container">
            <div className="login-card">
                <div className="brand">
                    <span className="logo-icon">📊</span>
                    <h1>KanbanFlow</h1>
                </div>

                <h2 className="login-title">Đăng nhập để tiếp tục</h2>

                <Form
                    name="login_form"
                    layout="vertical"
                    onFinish={login}
                    requiredMark={false}
                >
                    <Form.Item
                        label={<span className="trello-label">Tên đăng nhập</span>}
                        name="username"
                        rules={[{ required: true, message: 'Vui lòng nhập tên đăng nhập!' }]}
                    >
                        <Input
                            prefix={<UserOutlined style={{ color: '#bfbfbf' }} />}
                            placeholder="Nhập username"
                            size="large"
                            className="trello-input"
                        />
                    </Form.Item>

                    <Form.Item
                        label={<span className="trello-label">Mật khẩu</span>}
                        name="password"
                        rules={[{ required: true, message: 'Vui lòng nhập mật khẩu!' }]}
                    >
                        <Input.Password
                            prefix={<LockOutlined style={{ color: '#bfbfbf' }} />}
                            placeholder="Nhập mật khẩu"
                            size="large"
                            className="trello-input"
                        />
                    </Form.Item>

                    <Form.Item style={{ marginBottom: 0, marginTop: 24 }}>
                        <Button
                            type="primary"
                            htmlType="submit"
                            loading={loading}
                            block
                            size="large"
                            className="trello-btn"
                        >
                            Đăng nhập
                        </Button>
                    </Form.Item>

                    <div style={{ textAlign: 'center', fontSize: '14px', marginTop: 12 }}>
                        <Link to="/register" style={{ color: '#0052CC', fontWeight: 500 }}>
                            Chưa có tài khoản? Đăng ký ngay
                        </Link>
                    </div>
                </Form>
            </div>
        </div>
    );
}

export default Login;