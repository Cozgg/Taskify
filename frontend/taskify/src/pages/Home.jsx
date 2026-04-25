import React, { useCallback, useContext, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button, Pagination, Spin, Modal, Form, Input, message, Dropdown } from 'antd';
import {
    PlusOutlined,
    AppstoreOutlined,
    TeamOutlined,
    EllipsisOutlined,
    SearchOutlined,
    EditOutlined,
    DeleteOutlined,
} from '@ant-design/icons';
import './Home.css';
import { authApis, endpoints } from '../utils/Apis';
import { MyContext } from '../utils/context/MyContext';
import cookies from 'react-cookies';

const AVATAR_COLORS = [
    '#5aac44', '#0052cc', '#e4842d', '#cd5a91',
    '#00b0d0', '#6b4fbb', '#e35e5e', '#00875a',
];
const getColor = (id) => AVATAR_COLORS[(id || 0) % AVATAR_COLORS.length];
const getInitials = (name = '') =>
    name.split(' ').filter(Boolean).slice(0, 2).map((w) => w[0].toUpperCase()).join('');

const Home = () => {
    const [user] = useContext(MyContext);
    const [workspaces, setWorkspaces] = useState([]);
    const [loading, setLoading] = useState(false);

    const [searchText, setSearchText] = useState('');
    const [committedQ, setCommittedQ] = useState('');

    const [page, setPage] = useState(1);
    const [totalItems, setTotalItems] = useState(0);
    const [pageSize, setPageSize] = useState(0);

    const [isModalVisible, setIsModalVisible] = useState(false);
    const [modalMode, setModalMode] = useState('create');
    const [editingWorkspace, setEditingWorkspace] = useState(null);
    const [form] = Form.useForm();

    const nav = useNavigate();

    const loadWorkspaces = useCallback(async (q = '', pageNum = 1) => {
        if (!user) return;
        try {
            setLoading(true);
            const token = cookies.load('token');
            let url = `${endpoints['workspaces']}?page=${pageNum}`;
            if (q) url += `&kw=${encodeURIComponent(q)}`;
            const res = await authApis(token).get(url);

            console.log("res data: ", res.data);

            if (res.status === 200) {
                const raw = res.data?.data;
                setPageSize(raw?.pageSize);
                const items = Array.isArray(raw) ? raw : (Array.isArray(raw?.items) ? raw.items : []);
                setWorkspaces(items);
                setTotalItems(raw?.totalItems ?? 0);
            }
        } catch (err) {
            if (err.response?.status === 404) {
                setWorkspaces([]);
                setTotalItems(0);
            } else {
                message.error(
                    'Lỗi tải workspace: ' +
                    (err.response?.data?.error || err.response?.data?.message || err.message)
                );
            }
        } finally {
            setLoading(false);
        }
    }, [user]);

    useEffect(() => {
        console.log(workspaces);
    }, [workspaces]);

    useEffect(() => {
        console.log(totalItems, pageSize);
    }, [totalItems, pageSize]);


    useEffect(() => {
        const timer = setTimeout(() => {
            setPage(1);
            setCommittedQ(searchText.trim());
        }, 500);
        return () => clearTimeout(timer);
    }, [searchText]);

    useEffect(() => {
        if (!user) return;
        loadWorkspaces(committedQ, page);
    }, [committedQ, page, user, loadWorkspaces]);

    const openCreateModal = () => {
        setModalMode('create');
        setEditingWorkspace(null);
        form.resetFields();
        setIsModalVisible(true);
    };

    const openEditModal = (e, ws) => {
        e.domEvent?.stopPropagation?.();
        setModalMode('edit');
        setEditingWorkspace(ws);
        form.setFieldsValue({ name: ws.name });
        setIsModalVisible(true);
    };

    const handleModalSubmit = async () => {
        try {
            const values = await form.validateFields();
            const token = cookies.load('token');
            const api = authApis(token);

            if (modalMode === 'create') {
                const res = await api.post(endpoints['create-workspace'], values);
                if (res.status === 201 || res.status === 200) {
                    message.success('Tạo workspace thành công!');
                    setIsModalVisible(false);
                    setPage(1);
                    setSearchText('');
                    setCommittedQ('');
                    loadWorkspaces('');
                }
            } else if (modalMode === 'edit') {
                const res = await api.put(endpoints['workspace-detail'](editingWorkspace.id), values);
                if (res.status === 200) {
                    message.success('Cập nhật workspace thành công!');
                    setIsModalVisible(false);
                    loadWorkspaces(committedQ, page);
                }
            }
        } catch (err) {
            if (!err.errorFields) {
                message.error('Có lỗi xảy ra, vui lòng thử lại!');
                console.error(err);
            }
        }
    };

    const handleDeleteWorkspace = (e, ws) => {
        e?.domEvent?.stopPropagation?.();

        Modal.confirm({
            title: 'Xoá workspace',
            content: `Bạn có chắc chắn muốn xoá workspace "${ws?.name || ''}"?`,
            okText: 'Xoá',
            okType: 'danger',
            cancelText: 'Hủy',
            onOk: async () => {
                const token = cookies.load('token');
                const api = authApis(token);
                try {
                    const res = await api.delete(endpoints['workspace-detail'](ws.id));
                    if (res.status === 204 || res.status === 200) {
                        message.success('Đã xoá workspace.');
                        if (workspaces.length === 1 && page > 1) {
                            setPage((p) => Math.max(1, p - 1));
                        } else {
                            loadWorkspaces(committedQ, page);
                        }
                    }
                } catch (err) {
                    message.error(
                        'Lỗi xoá workspace: ' +
                        (err.response?.data?.error || err.response?.data?.message || err.message)
                    );
                }
            },
        });
    };

    const cardMenu = (ws) => ({
        items: [
            {
                key: 'edit',
                icon: <EditOutlined />,
                label: 'Chỉnh sửa workspace',
                onClick: (e) => openEditModal(e, ws),
            },
            { type: 'divider' },
            {
                key: 'delete',
                icon: <DeleteOutlined />,
                label: 'Xoá workspace',
                danger: true,
                onClick: (e) => handleDeleteWorkspace(e, ws),
            },
        ],
    });

    return (
        <div className="home-page">
            <div className="home-content">
                <div className="home-section-head">
                    <div>
                        <h1 className="home-section-title">Workspaces</h1>
                        <p className="home-section-sub">
                            Select a workspace to start managing your projects.
                        </p>
                    </div>

                    <div className="home-section-actions">
                        <div className="home-subnav-search-wrap">
                            <SearchOutlined className="home-search-icon" />
                            <input
                                className="home-search-input"
                                placeholder="Tìm workspace..."
                                value={searchText}
                                onChange={(e) => setSearchText(e.target.value)}
                            />
                            {searchText && (
                                <span
                                    className="home-search-clear"
                                    onClick={() => setSearchText('')}
                                >
                                    ×
                                </span>
                            )}
                        </div>
                    </div>
                </div>

                {loading ? (
                    <div className="home-loading"><Spin size="large" /></div>
                ) : (
                    <>
                        {workspaces.length === 0 && (
                            <div className="home-empty">
                                {committedQ
                                    ? `Không tìm thấy workspace nào cho "${committedQ}".`
                                    : 'Bạn chưa có workspace nào. Hãy tạo mới!'}
                            </div>
                        )}

                        <div className="workspace-grid">
                            {workspaces.map((ws, idx) => (
                                <div
                                    key={ws.id}
                                    className="ws-card"
                                    style={{ animationDelay: `${idx * 50}ms` }}
                                    onClick={() => nav(`/workspace/${ws.id}`)}
                                >
                                    <div className="ws-card-top">
                                        <div
                                            className="ws-avatar"
                                            style={{ backgroundColor: ws.color || getColor(ws.id) }}
                                        >
                                            {getInitials(ws.name)}
                                        </div>
                                        <Dropdown
                                            menu={cardMenu(ws)}
                                            trigger={['click']}
                                            placement="bottomRight"
                                        >
                                            <Button
                                                className="ws-menu-btn"
                                                icon={<EllipsisOutlined style={{ fontSize: 18 }} />}
                                                onClick={(e) => e.stopPropagation()}
                                            />
                                        </Dropdown>
                                    </div>

                                    <p className="ws-card-name">{ws.name}</p>

                                    <div className="ws-card-meta">
                                        <span className="ws-card-meta-item">
                                            <AppstoreOutlined /> {ws.boardCount ?? 0} Boards
                                        </span>
                                        <span className="ws-card-meta-item">
                                            <TeamOutlined /> {ws.memberCount ?? 0} Members
                                        </span>
                                    </div>
                                </div>
                            ))}

                            {!committedQ && (
                                <div className="ws-card-create" onClick={openCreateModal}>
                                    <div className="ws-create-icon"><PlusOutlined /></div>
                                    <span className="ws-create-label">Create new workspace</span>
                                </div>
                            )}
                        </div>

                        {totalItems > pageSize && (
                            <div className="home-pagination">
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

            </div>

            <Modal
                title={modalMode === 'create' ? 'Tạo không gian làm việc mới' : 'Chỉnh sửa không gian làm việc'}
                open={isModalVisible}
                onOk={handleModalSubmit}
                onCancel={() => { setIsModalVisible(false); form.resetFields(); }}
                okText="Xác nhận"
                cancelText="Hủy"
                okButtonProps={{ style: { backgroundColor: '#0052cc', borderColor: '#0052cc' } }}
            >
                <Form form={form} layout="vertical" style={{ marginTop: 16 }}>
                    <Form.Item
                        name="name"
                        label="Tên Không gian làm việc"
                        rules={[{ required: true, message: 'Vui lòng nhập tên Workspace!' }]}
                    >
                        <Input placeholder="Ví dụ: Công ty TNHH Bách Khoa" size="large" />
                    </Form.Item>
                </Form>
            </Modal>
        </div>
    );
};

export default Home;
