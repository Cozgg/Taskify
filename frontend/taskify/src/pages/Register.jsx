import { Button, Form, Input, message, Upload } from "antd";
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Apis, { endpoints } from "../utils/Apis";
import { MailOutlined, LockOutlined, UploadOutlined, UserOutlined } from "@ant-design/icons";

const Register = () => {

    const [loading, setLoading] = useState(false);

    const nav = useNavigate();

    const register = async (values) => {
        try {
            setLoading(true);
            const res = await Apis.post(endpoints['register'], {
                email: values.email,
                username: values.username,
                password: values.password,
            });

            if (res.status === 201) {
                message.success('Đăng ký thành công!');
                nav('/login');
            }

        } catch (err) {
            message.error('Đăng ký thất bại!');
            console.error(err);
        } finally {
            setLoading(false);
        }
    }
    // chặn upload ảnh ngay
    // const normFile = (e) => {
    //     if (Array.isArray(e)) return e;
    //     return e?.fileList;
    // };

    return (
        <div className="kanban-login-container">
            <div className="login-card" style={{ maxWidth: '500px' }}> {/* Nới rộng xíu cho form ĐK */}
                <div className="brand">
                    <span className="logo-icon">📊</span>
                    <h1>KanbanFlow</h1>
                </div>

                <h2 className="login-title">Đăng ký tài khoản mới</h2>

                <Form
                    name="register_form"
                    layout="vertical"
                    onFinish={register}
                    requiredMark={false}
                >
                    <Form.Item
                        label={<span className="trello-label">Nhập Họ và Tên</span>}
                        name="name"
                        style={{ flex: 1 }}
                        rules={[{ required: true, message: 'Nhập tên!' }]}
                    >
                        <Input className="trello-input" size="large" />
                    </Form.Item>

                    <Form.Item
                        label={<span className="trello-label">Tên đăng nhập</span>}
                        name="username"
                        rules={[{ required: true, message: 'Vui lòng nhập tên đăng nhập!' }]}
                    >
                        <Input prefix={<UserOutlined style={{ color: '#bfbfbf' }} />} className="trello-input" size="large" />
                    </Form.Item>

                    <Form.Item
                        label={<span className="trello-label">Email</span>}
                        name="email"
                        rules={[
                            { required: true, message: 'Vui lòng nhập email!' },
                            { type: 'email', message: 'Email không hợp lệ!' }
                        ]}
                    >
                        <Input prefix={<MailOutlined style={{ color: '#bfbfbf' }} />} className="trello-input" size="large" />
                    </Form.Item>

                    <Form.Item
                        label={<span className="trello-label">Mật khẩu</span>}
                        name="password"
                        rules={[{ required: true, message: 'Vui lòng nhập mật khẩu!' }]}
                    >
                        <Input.Password prefix={<LockOutlined style={{ color: '#bfbfbf' }} />} className="trello-input" size="large" />
                    </Form.Item>

                    {/* Logic so sánh khớp mật khẩu tích hợp sẵn của Antd */}
                    <Form.Item
                        label={<span className="trello-label">Xác nhận mật khẩu</span>}
                        name="confirm"
                        dependencies={['password']}
                        rules={[
                            { required: true, message: 'Vui lòng xác nhận mật khẩu!' },
                            ({ getFieldValue }) => ({
                                validator(_, value) {
                                    if (!value || getFieldValue('password') === value) {
                                        return Promise.resolve();
                                    }
                                    return Promise.reject(new Error('Mật khẩu xác nhận không khớp!'));
                                },
                            }),
                        ]}
                    >
                        <Input.Password prefix={<LockOutlined style={{ color: '#bfbfbf' }} />} className="trello-input" size="large" />
                    </Form.Item>

                    {/* <Form.Item
                        label={<span className="trello-label">Ảnh đại diện (Avatar)</span>}
                        name="avatar"
                        valuePropName="fileList"
                        getValueFromEvent={normFile}
                    >
                        <Upload beforeUpload={() => false} maxCount={1} listType="picture">
                            <Button icon={<UploadOutlined />}>Chọn ảnh</Button>
                        </Upload>
                    </Form.Item> */}

                    <Form.Item style={{ marginBottom: 12 }}>
                        <Button type="primary" htmlType="submit" loading={loading} block size="large" className="trello-btn">
                            Đăng ký
                        </Button>
                    </Form.Item>

                    <div style={{ textAlign: 'center', fontSize: '14px' }}>
                        <Link to="/login" style={{ color: '#0052CC', fontWeight: 500 }}>Đã có tài khoản? Đăng nhập ngay</Link>
                    </div>
                </Form>
            </div>
        </div>
    );
}

export default Register;