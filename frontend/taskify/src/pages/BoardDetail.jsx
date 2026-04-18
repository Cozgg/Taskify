import React, { useCallback, useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Button, Card, Typography, Select, Input, Modal, Tag, Spin, message } from 'antd';
import { PlusOutlined, MoreOutlined, ArrowLeftOutlined } from '@ant-design/icons';
import cookies from 'react-cookies';
import { authApis, endpoints } from '../utils/Apis';
import './BoardDetail.css';

const { Title, Text } = Typography;

// Mock Data
const MOCK_STATUSES = [
    { value: 'TODO', label: 'Cần làm (To Do)' },
    { value: 'IN_PROGRESS', label: 'Đang làm (In Progress)' },
    { value: 'REVIEW', label: 'Chờ duyệt (Review)' },
    { value: 'DONE', label: 'Hoàn thành (Done)' },
];

const BoardDetail = () => {
    const { boardId } = useParams();
    const nav = useNavigate();

    const [board, setBoard] = useState(null);
    const [columns, setColumns] = useState([]);
    const [loading, setLoading] = useState(true);
    const [isColModalOpen, setIsColModalOpen] = useState(false);
    const [selectedStatus, setSelectedStatus] = useState(null);
    const [isCardModalOpen, setIsCardModalOpen] = useState(false);
    const [activeColId, setActiveColId] = useState(null);
    const [newCardTitle, setNewCardTitle] = useState('');

    const loadBoardAndLists = useCallback(async () => {
        try {
            setLoading(true);
            const token = cookies.load('token');
            const api = authApis(token);

            const [boardRes, listsRes] = await Promise.all([
                api.get(endpoints['board-detail'](boardId)),
                api.get(endpoints['lists'](boardId)),
            ]);

            const boardData = boardRes.data?.data ?? boardRes.data;
            const listsData = listsRes.data?.data ?? listsRes.data;

            setBoard(boardData);

            const listColumns = Array.isArray(listsData)
                ? listsData.map((list) => ({
                    id: list.id,
                    title: list.name,
                    status: list.status,
                    cards: [],
                }))
                : [];

            setColumns(listColumns);

            const cardsByList = await Promise.all(
                listColumns.map(async (list) => {
                    try {
                        const cardRes = await api.get(endpoints['cards'](list.id));
                        const cardData = cardRes.data?.data ?? cardRes.data;
                        return {
                            listId: list.id,
                            cards: Array.isArray(cardData)
                                ? cardData.map((card) => ({
                                    id: card.id,
                                    title: card.name,
                                    description: card.description || '',
                                }))
                                : [],
                        };
                    } catch (err) {
                        return { listId: list.id, cards: [] };
                    }
                })
            );

            setColumns((prev) => prev.map((col) => ({
                ...col,
                cards: cardsByList.find((item) => item.listId === col.id)?.cards || [],
            })));
        } catch (err) {
            message.error(
                'Lỗi tải dữ liệu bảng: ' +
                (err.response?.data?.error || err.response?.data || err.message)
            );
        } finally {
            setLoading(false);
        }
    }, [boardId]);

    useEffect(() => {
        if (boardId) {
            loadBoardAndLists();
        }
    }, [boardId, loadBoardAndLists]);

    const handleAddColumn = async () => {
        if (!selectedStatus) return;
        const statusObj = MOCK_STATUSES.find((s) => s.value === selectedStatus);

        if (columns.some((col) => col.status === selectedStatus)) {
            message.warning('Danh sách này đã tồn tại trong bảng.');
            return;
        }

        try {
            const token = cookies.load('token');
            const api = authApis(token);
            const payload = {
                name: statusObj.label,
                status: statusObj.value,
                position: columns.length + 1,
            };
            const res = await api.post(endpoints['create-list'](boardId), payload);
            const created = res.data?.data ?? res.data;
            const newCol = {
                id: created.id,
                title: created.name || statusObj.label,
                status: created.status || statusObj.value,
                cards: [],
            };
            setColumns([...columns, newCol]);
            setIsColModalOpen(false);
            setSelectedStatus(null);
            message.success('Đã tạo danh sách mới.');
        } catch (err) {
            message.error(
                'Lỗi tạo danh sách: ' +
                (err.response?.data?.error || err.response?.data || err.message)
            );
        }
    };

    const handleAddCard = async () => {
        if (!newCardTitle.trim() || !activeColId) return;
        const currentList = columns.find((col) => col.id === activeColId);
        if (!currentList) return;

        if (currentList.cards.some((card) => card.title.trim().toLowerCase() === newCardTitle.trim().toLowerCase())) {
            message.warning('Thẻ này đã tồn tại trong danh sách.');
            return;
        }

        try {
            const token = cookies.load('token');
            const api = authApis(token);
            const payload = { name: newCardTitle.trim() };
            const res = await api.post(endpoints['create-card'](activeColId), payload);
            const created = res.data?.data ?? res.data;
            const newCard = {
                id: created.id,
                title: created.name,
                description: created.description || '',
            };
            setColumns(columns.map((col) => col.id === activeColId ? {
                ...col,
                cards: [...col.cards, newCard],
            } : col));
            setIsCardModalOpen(false);
            setNewCardTitle('');
            message.success('Đã tạo thẻ mới.');
        } catch (err) {
            message.error(
                'Lỗi tạo thẻ: ' +
                (err.response?.data?.error || err.response?.data || err.message)
            );
        }
    };

    return (
        <div className="board-detail-wrapper">
            {/* Header Bảng */}
            <div className="board-header">
                <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
                    <Button icon={<ArrowLeftOutlined />} type="text" onClick={() => nav(-1)} />
                    <Title level={4} style={{ margin: 0, color: 'white' }}>
                        {board?.name || `Kanban Board (ID: ${boardId})`}
                    </Title>
                </div>
            </div>

            <div className="board-canvas">
                {loading ? (
                    <div className="board-loading">
                        <Spin size="large" />
                    </div>
                ) : columns.length === 0 ? (
                    <div className="board-empty">
                        Hiện chưa có danh sách nào. Thêm danh sách mới để bắt đầu.
                    </div>
                ) : (
                    <>
                        {columns.map((col) => (
                            <div className="kanban-column" key={col.id}>
                                <div className="kanban-column-header">
                                    <Text strong>{col.title}</Text>
                                    <Button type="text" icon={<MoreOutlined />} size="small" />
                                </div>

                                <div className="kanban-cards-list">
                                    {col.cards.map((card) => (
                                        <Card key={card.id} className="kanban-card" size="small" hoverable>
                                            {card.description && (
                                                <Tag color="blue" style={{ marginBottom: 8 }}>
                                                    {card.description}
                                                </Tag>
                                            )}
                                            <div className="card-title">{card.title}</div>
                                        </Card>
                                    ))}
                                </div>

                                <div className="kanban-column-footer">
                                    <Button
                                        type="text"
                                        icon={<PlusOutlined />}
                                        block
                                        style={{ textAlign: 'left', color: '#5e6c84' }}
                                        onClick={() => {
                                            setActiveColId(col.id);
                                            setIsCardModalOpen(true);
                                        }}
                                    >
                                        Thêm thẻ
                                    </Button>
                                </div>
                            </div>
                        ))}

                        <div className="add-column-wrapper">
                            <Button
                                type="primary"
                                ghost
                                icon={<PlusOutlined />}
                                className="add-column-btn"
                                onClick={() => setIsColModalOpen(true)}
                            >
                                Thêm danh sách khác
                            </Button>
                        </div>
                    </>
                )}
            </div>

            <Modal title="Thêm danh sách mới" open={isColModalOpen} onOk={handleAddColumn} onCancel={() => setIsColModalOpen(false)} okText="Thêm" cancelText="Hủy">
                <p>Chọn trạng thái cho cột mới:</p>
                <Select
                    style={{ width: '100%' }}
                    placeholder="Chọn trạng thái..."
                    options={MOCK_STATUSES}
                    value={selectedStatus}
                    onChange={setSelectedStatus}
                />
            </Modal>

            <Modal title="Thêm thẻ mới" open={isCardModalOpen} onOk={handleAddCard} onCancel={() => setIsCardModalOpen(false)} okText="Thêm thẻ" cancelText="Hủy">
                <Input.TextArea
                    placeholder="Nhập tiêu đề cho thẻ này..."
                    autoSize={{ minRows: 2, maxRows: 6 }}
                    value={newCardTitle}
                    onChange={(e) => setNewCardTitle(e.target.value)}
                />
            </Modal>
        </div>
    );
};

export default BoardDetail;