import React, { useCallback, useEffect, useState } from 'react';
import { Button, Form, Input, Modal, Pagination, Space, Spin, Table, Tag, Typography, message } from 'antd';
import { PlusOutlined, DeleteOutlined, ReloadOutlined } from '@ant-design/icons';
import cookies from 'react-cookies';
import Apis, { authApis, endpoints } from '../../utils/Apis';

const { Title, Text } = Typography;

const AdminUsers = () => {
    const [loading, setLoading] = useState(false);
    const [users, setUsers] = useState([]);

    const [page, setPage] = useState(1);
    const [totalItems, setTotalItems] = useState(0);
    const [pageSize, setPageSize] = useState(10);

    const [searchText, setSearchText] = useState('');
    const [committedKw, setCommittedKw] = useState('');

    const [isCreateOpen, setIsCreateOpen] = useState(false);
    const [createForm] = Form.useForm();
    const [creating, setCreating] = useState(false);

    const load = useCallback(async (kw = '', pageNum = 1) => {
        try {
            setLoading(true);
            const token = cookies.load('token');
            const api = authApis(token);

            const qs = new URLSearchParams();
            qs.set('page', String(pageNum));
            if (kw && String(kw).trim()) {
                qs.set('kw', String(kw).trim());
            }

            let url = endpoints['admin-users'];
            url += `?${qs.toString()}`;

            const res = await api.get(url);

            const pageDto = res.data?.data;
            const items = Array.isArray(pageDto?.items)
                ? pageDto.items
                : (Array.isArray(res.data) ? res.data : []);

            setUsers(items);
            setTotalItems(pageDto?.totalItems ?? items.length ?? 0);
            setPageSize(pageDto?.pageSize ?? 10);
        } catch (err) {
            message.error(
                'Lỗi tải users: ' +
                (err.response?.data?.message || err.response?.data?.error || err.message)
            );
            setUsers([]);
            setTotalItems(0);
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        const timer = setTimeout(() => {
            setPage(1);
            setCommittedKw(searchText.trim());
        }, 500);
        return () => clearTimeout(timer);
    }, [searchText]);

    useEffect(() => {
        load(committedKw, page);
    }, [committedKw, page, load]);

    const openCreate = () => {
        createForm.resetFields();
        createForm.setFieldsValue({ role: 'USER' });
        setIsCreateOpen(true);
    };

    const submitCreate = async () => {
        try {
            const values = await createForm.validateFields();
            setCreating(true);

            const payload = {
                email: values.email,
                username: values.username,
                password: values.password,
            };

            const res = await Apis.post(endpoints['register'], payload);
            if (res.status === 201 || res.status === 200) {
                message.success('Tạo user thành công');
                setIsCreateOpen(false);
                await load(committedKw, page);
            }
        } catch (err) {
            if (err?.errorFields) return;
            message.error(
                'Lỗi tạo user: ' +
                (err.response?.data?.message || err.response?.data?.error || err.message)
            );
        } finally {
            setCreating(false);
        }
    };

    const confirmDelete = (record) => {
        Modal.confirm({
            title: 'Xoá user',
            content: `Bạn có chắc muốn xoá user "${record?.username || ''}"?`,
            okText: 'Xoá',
            okType: 'danger',
            cancelText: 'Hủy',
            onOk: async () => {
                try {
                    const token = cookies.load('token');
                    const api = authApis(token);
                    const res = await api.delete(endpoints['admin-user-detail'](record.id));
                    if (res.status === 204 || res.status === 200) {
                        message.success('Đã xoá user');
                        await load(committedKw, page);
                    }
                } catch (err) {
                    message.error(
                        'Lỗi xoá user: ' +
                        (err.response?.data?.message || err.response?.data?.error || err.message)
                    );
                }
            },
        });
    };

    const columns = [
        { title: 'ID', dataIndex: 'id', key: 'id', width: 80 },
        { title: 'Username', dataIndex: 'username', key: 'username' },
        { title: 'Email', dataIndex: 'email', key: 'email' },
        {
            title: 'Role',
            dataIndex: 'role',
            key: 'role',
            width: 120,
            render: (role) => <Tag color={role === 'ADMIN' ? 'blue' : 'default'}>{role || 'USER'}</Tag>,
        },
        { title: 'Created', dataIndex: 'createdDate', key: 'createdDate', width: 140 },
        {
            title: 'Actions',
            key: 'actions',
            width: 120,
            render: (_, record) => (
                <Button
                    danger
                    icon={<DeleteOutlined />}
                    size="small"
                    onClick={() => confirmDelete(record)}
                >
                    Xoá
                </Button>
            ),
        },
    ];

    return (
        <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-end', marginBottom: 16 }}>
                <div>
                    <Title level={3} style={{ margin: 0 }}>Users</Title>
                    <Text type="secondary">Quản lý người dùng hệ thống</Text>
                </div>

                <Space>
                    <Button icon={<ReloadOutlined />} onClick={() => load(committedKw, page)}>Reload</Button>
                    <Button type="primary" icon={<PlusOutlined />} onClick={openCreate}>Create user</Button>
                </Space>
            </div>

            <div style={{ marginBottom: 12, maxWidth: 420 }}>
                <Input
                    placeholder="Tìm theo username/email..."
                    value={searchText}
                    onChange={(e) => setSearchText(e.target.value)}
                    allowClear
                />
            </div>

            {loading ? (
                <div style={{ display: 'flex', justifyContent: 'center', padding: 40 }}>
                    <Spin size="large" />
                </div>
            ) : (
                <>
                    <Table
                        rowKey={(r) => r.id}
                        columns={columns}
                        dataSource={users}
                        pagination={false}
                    />

                    {totalItems > pageSize && (
                        <div style={{ display: 'flex', justifyContent: 'center', marginTop: 16 }}>
                            <Pagination
                                current={page}
                                pageSize={pageSize}
                                total={totalItems}
                                showSizeChanger={false}
                                showTotal={(total) => `${total} users`}
                                onChange={(nextPage) => setPage(nextPage)}
                            />
                        </div>
                    )}
                </>
            )}

            <Modal
                title="Create user"
                open={isCreateOpen}
                onCancel={() => setIsCreateOpen(false)}
                onOk={submitCreate}
                okText="Tạo"
                cancelText="Hủy"
                confirmLoading={creating}
                destroyOnClose
            >
                <Form form={createForm} layout="vertical" requiredMark={false}>
                    <Form.Item
                        label="Username"
                        name="username"
                        rules={[{ required: true, message: 'Vui lòng nhập username' }]}
                    >
                        <Input placeholder="username" />
                    </Form.Item>

                    <Form.Item
                        label="Password"
                        name="password"
                        rules={[{ required: true, message: 'Vui lòng nhập password' }]}
                    >
                        <Input.Password placeholder="password" />
                    </Form.Item>

                    <Form.Item
                        label="Email"
                        name="email"
                        rules={[
                            { required: true, message: 'Vui lòng nhập email' },
                            { type: 'email', message: 'Email không hợp lệ' },
                        ]}
                    >
                        <Input placeholder="email" />
                    </Form.Item>

                    <Form.Item label="Avatar URL" name="avatar">
                        <Input placeholder="https://..." disabled />
                    </Form.Item>

                    <Form.Item label="Role" name="role" initialValue="USER">
                        <Input placeholder="USER" disabled />
                    </Form.Item>
                </Form>
            </Modal>
        </div>
    );
};

export default AdminUsers;
