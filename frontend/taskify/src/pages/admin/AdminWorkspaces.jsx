import React, { useCallback, useEffect, useState } from 'react';
import { Button, Form, Input, Modal, Pagination, Space, Spin, Table, Typography, message } from 'antd';
import { PlusOutlined, DeleteOutlined, EyeOutlined, ReloadOutlined } from '@ant-design/icons';
import cookies from 'react-cookies';
import { authApis, endpoints } from '../../utils/Apis';
import { useNavigate } from 'react-router-dom';

const { Title, Text } = Typography;

const AdminWorkspaces = () => {
    const [loading, setLoading] = useState(false);
    const [workspaces, setWorkspaces] = useState([]);

    const [page, setPage] = useState(1);
    const [totalItems, setTotalItems] = useState(0);
    const [pageSize, setPageSize] = useState(10);

    const [searchText, setSearchText] = useState('');
    const [committedKw, setCommittedKw] = useState('');

    const [isCreateOpen, setIsCreateOpen] = useState(false);
    const [createForm] = Form.useForm();
    const [creating, setCreating] = useState(false);

    const nav = useNavigate();

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
            let url = endpoints['admin-workspaces'];
            url += `?${qs.toString()}`;

            const res = await api.get(url);

            const pageDto = res.data?.data;
            console.log('Loaded workspaces page:', res);
            const items = Array.isArray(pageDto?.items)
                ? pageDto.items
                : (Array.isArray(res.data) ? res.data : []);

            setWorkspaces(items);
            setTotalItems(pageDto?.totalItems ?? items.length ?? 0);
            setPageSize(pageDto?.pageSize ?? 10);
        } catch (err) {
            message.error(
                'Lỗi tải workspaces: ' +
                (err.response?.data?.message || err.response?.data?.error || err.message)
            );
            setWorkspaces([]);
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
        setIsCreateOpen(true);
    };

    const submitCreate = async () => {
        try {
            const values = await createForm.validateFields();
            setCreating(true);

            const token = cookies.load('token');
            const api = authApis(token);

            const payload = {
                name: values.name,
                ownerId: values.ownerId ? Number(values.ownerId) : null,
            };

            const res = await api.post(endpoints['admin-workspaces'], payload);
            if (res.status === 201 || res.status === 200) {
                message.success('Tạo workspace thành công');
                setIsCreateOpen(false);
                await load(committedKw, page);
            }
        } catch (err) {
            if (err?.errorFields) return;
            message.error(
                'Lỗi tạo workspace: ' +
                (err.response?.data?.message || err.response?.data?.error || err.message)
            );
        } finally {
            setCreating(false);
        }
    };

    const confirmDelete = (record) => {
        Modal.confirm({
            title: 'Xoá workspace',
            content: `Bạn có chắc muốn xoá workspace "${record?.name || ''}"?`,
            okText: 'Xoá',
            okType: 'danger',
            cancelText: 'Hủy',
            onOk: async () => {
                try {
                    const token = cookies.load('token');
                    const api = authApis(token);
                    const res = await api.delete(endpoints['admin-workspace-detail'](record.id));
                    if (res.status === 204 || res.status === 200) {
                        message.success('Đã xoá workspace');
                        await load(committedKw, page);
                    }
                } catch (err) {
                    message.error(
                        'Lỗi xoá workspace: ' +
                        (err.response?.data?.message || err.response?.data?.error || err.message)
                    );
                }
            },
        });
    };

    const columns = [
        { title: 'ID', dataIndex: 'id', key: 'id', width: 80 },
        { title: 'Name', dataIndex: 'name', key: 'name' },
        {
            title: 'Owner',
            key: 'owner',
            render: (_, r) => r?.owner?.username || '-',
        },
        { title: 'Members', dataIndex: 'memberCount', key: 'memberCount', width: 110 },
        { title: 'Boards', dataIndex: 'boardCount', key: 'boardCount', width: 100 },
        {
            title: 'Actions',
            key: 'actions',
            width: 200,
            render: (_, record) => (
                <Space>
                    <Button
                        icon={<EyeOutlined />}
                        size="small"
                        onClick={() => nav(`/admin/workspaces/${record.id}`)}
                    >
                        Detail
                    </Button>
                    <Button
                        danger
                        icon={<DeleteOutlined />}
                        size="small"
                        onClick={() => confirmDelete(record)}
                    >
                        Xoá
                    </Button>
                </Space>
            ),
        },
    ];

    return (
        <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-end', marginBottom: 16 }}>
                <div>
                    <Title level={3} style={{ margin: 0 }}>Workspaces</Title>
                    <Text type="secondary">Quản lý workspace hệ thống</Text>
                </div>

                <Space>
                    <Button icon={<ReloadOutlined />} onClick={() => load(committedKw, page)}>Reload</Button>
                    <Button type="primary" icon={<PlusOutlined />} onClick={openCreate}>Create workspace</Button>
                </Space>
            </div>

            <div style={{ marginBottom: 12, maxWidth: 420 }}>
                <Input
                    placeholder="Tìm theo name/owner..."
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
                        dataSource={workspaces}
                        pagination={false}
                    />

                    {totalItems > pageSize && (
                        <div style={{ display: 'flex', justifyContent: 'center', marginTop: 16 }}>
                            <Pagination
                                current={page}
                                pageSize={pageSize}
                                total={totalItems}
                                showSizeChanger={false}
                                showTotal={(total) => `${total} workspaces`}
                                onChange={(nextPage) => setPage(nextPage)}
                            />
                        </div>
                    )}
                </>
            )}

            <Modal
                title="Create workspace"
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
                        label="Name"
                        name="name"
                        rules={[{ required: true, message: 'Vui lòng nhập tên workspace' }]}
                    >
                        <Input placeholder="Workspace name" />
                    </Form.Item>

                    <Form.Item label="Owner ID (optional)" name="ownerId">
                        <Input placeholder="vd: 1" />
                    </Form.Item>
                </Form>
            </Modal>
        </div>
    );
};

export default AdminWorkspaces;
