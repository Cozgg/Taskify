import React, { useEffect, useMemo, useState } from 'react';
import { Button, Card, Descriptions, Space, Spin, Table, Tabs, Tag, Typography, message } from 'antd';
import { ArrowLeftOutlined, ReloadOutlined } from '@ant-design/icons';
import { useNavigate, useParams } from 'react-router-dom';
import cookies from 'react-cookies';
import { authApis, endpoints } from '../../utils/Apis';

const { Title, Text } = Typography;

const AdminWorkspaceDetail = () => {
    const { id } = useParams();
    const nav = useNavigate();

    const [loading, setLoading] = useState(false);
    const [detail, setDetail] = useState(null);

    const api = useMemo(() => {
        const token = cookies.load('token');
        return authApis(token);
    }, []);

    const load = async () => {
        try {
            setLoading(true);
            const res = await api.get(endpoints['admin-workspace-detail'](id));
            setDetail(res.data || null);
        } catch (err) {
            message.error(
                'Lỗi tải workspace detail: ' +
                (err.response?.data?.message || err.response?.data?.error || err.message)
            );
            setDetail(null);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        load();
    }, [id]);

    const workspace = detail?.workspace;
    const members = Array.isArray(detail?.members) ? detail.members : [];
    const boards = Array.isArray(detail?.boards) ? detail.boards : [];

    const memberColumns = [
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
    ];

    const boardColumns = [
        { title: 'ID', dataIndex: 'id', key: 'id', width: 80 },
        { title: 'Name', dataIndex: 'name', key: 'name' },
        {
            title: 'Public',
            dataIndex: 'isPublic',
            key: 'isPublic',
            width: 110,
            render: (v) => (v ? <Tag color="green">Public</Tag> : <Tag>Private</Tag>),
        },
        { title: 'Created', dataIndex: 'createdDate', key: 'createdDate', width: 140 },
    ];

    return (
        <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-end', marginBottom: 16 }}>
                <div>
                    <Title level={3} style={{ margin: 0 }}>Workspace detail</Title>
                    <Text type="secondary">/api/admin/workspaces/{id}</Text>
                </div>
                <Space>
                    <Button icon={<ArrowLeftOutlined />} onClick={() => nav('/admin/workspaces')}>Back</Button>
                    <Button icon={<ReloadOutlined />} onClick={load}>Reload</Button>
                </Space>
            </div>

            {loading ? (
                <div style={{ display: 'flex', justifyContent: 'center', padding: 40 }}>
                    <Spin size="large" />
                </div>
            ) : (
                <>
                    <Card style={{ marginBottom: 16 }}>
                        <Descriptions title="Workspace" column={1} size="middle">
                            <Descriptions.Item label="ID">{workspace?.id ?? '-'}</Descriptions.Item>
                            <Descriptions.Item label="Name">{workspace?.name ?? '-'}</Descriptions.Item>
                            <Descriptions.Item label="Owner">{workspace?.owner?.username ?? '-'}</Descriptions.Item>
                            <Descriptions.Item label="Members">{detail?.totalMembers ?? members.length}</Descriptions.Item>
                            <Descriptions.Item label="Boards">{detail?.totalBoards ?? boards.length}</Descriptions.Item>
                        </Descriptions>
                    </Card>

                    <Tabs
                        defaultActiveKey="members"
                        items={[
                            {
                                key: 'members',
                                label: `Members (${detail?.totalMembers ?? members.length})`,
                                children: (
                                    <Table
                                        rowKey={(r) => r.id}
                                        columns={memberColumns}
                                        dataSource={members}
                                        pagination={{ pageSize: 10, showSizeChanger: true }}
                                    />
                                ),
                            },
                            {
                                key: 'boards',
                                label: `Boards (${detail?.totalBoards ?? boards.length})`,
                                children: (
                                    <Table
                                        rowKey={(r) => r.id}
                                        columns={boardColumns}
                                        dataSource={boards}
                                        pagination={{ pageSize: 10, showSizeChanger: true }}
                                    />
                                ),
                            },
                        ]}
                    />
                </>
            )}
        </div>
    );
};

export default AdminWorkspaceDetail;
