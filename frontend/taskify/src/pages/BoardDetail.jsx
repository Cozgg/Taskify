import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Button, Card, Typography, Select, Input, Modal, Dropdown, Tag } from 'antd';
import { PlusOutlined, MoreOutlined, ArrowLeftOutlined } from '@ant-design/icons';
import './BoardDetail.css';

const { Title, Text } = Typography;

// Mock Data
const MOCK_STATUSES = [
    { value: 'TODO', label: 'Cần làm (To Do)' },
    { value: 'IN_PROGRESS', label: 'Đang làm (In Progress)' },
    { value: 'REVIEW', label: 'Chờ duyệt (Review)' },
    { value: 'DONE', label: 'Hoàn thành (Done)' },
];

const INITIAL_BOARD_DATA = [
    {
        id: 'col-1',
        title: 'Cần làm (To Do)',
        cards: [
            { id: 'card-1', title: 'Thiết kế ERD Database', label: 'Backend' },
            { id: 'card-2', title: 'Setup dự án ReactJS', label: 'Frontend' }
        ]
    },
    {
        id: 'col-2',
        title: 'Đang làm (In Progress)',
        cards: [
            { id: 'card-3', title: 'Tạo UI Board Detail', label: 'Frontend' }
        ]
    }
];

const BoardDetail = () => {
    const { boardId } = useParams();
    const nav = useNavigate();

    const [columns, setColumns] = useState(INITIAL_BOARD_DATA);

    // State Modal Thêm Cột
    const [isColModalOpen, setIsColModalOpen] = useState(false);
    const [selectedStatus, setSelectedStatus] = useState(null);

    // State Modal Thêm Thẻ (Card)
    const [isCardModalOpen, setIsCardModalOpen] = useState(false);
    const [activeColId, setActiveColId] = useState(null);
    const [newCardTitle, setNewCardTitle] = useState('');

    // Xử lý thêm Cột
    const handleAddColumn = () => {
        if (!selectedStatus) return;
        const statusObj = MOCK_STATUSES.find(s => s.value === selectedStatus);

        const newCol = {
            id: `col-${Date.now()}`,
            title: statusObj.label,
            cards: []
        };
        setColumns([...columns, newCol]);
        setIsColModalOpen(false);
        setSelectedStatus(null);
    };

    // Xử lý thêm Card
    const handleAddCard = () => {
        if (!newCardTitle.trim()) return;

        const newColumns = columns.map(col => {
            if (col.id === activeColId) {
                return {
                    ...col,
                    cards: [...col.cards, { id: `card-${Date.now()}`, title: newCardTitle }]
                };
            }
            return col;
        });

        setColumns(newColumns);
        setIsCardModalOpen(false);
        setNewCardTitle('');
    };

    return (
        <div className="board-detail-wrapper">
            {/* Header Bảng */}
            <div className="board-header">
                <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
                    <Button icon={<ArrowLeftOutlined />} type="text" onClick={() => nav(-1)} />
                    <Title level={4} style={{ margin: 0, color: 'white' }}>Kanban Board (ID: {boardId})</Title>
                </div>
            </div>

            {/* Canvas chứa các cột (Cuộn ngang) */}
            <div className="board-canvas">
                {columns.map(col => (
                    <div className="kanban-column" key={col.id}>
                        {/* Tiêu đề Cột */}
                        <div className="kanban-column-header">
                            <Text strong>{col.title}</Text>
                            <Button type="text" icon={<MoreOutlined />} size="small" />
                        </div>

                        {/* Danh sách Thẻ */}
                        <div className="kanban-cards-list">
                            {col.cards.map(card => (
                                <Card key={card.id} className="kanban-card" size="small" hoverable>
                                    {card.label && <Tag color="blue" style={{ marginBottom: 8 }}>{card.label}</Tag>}
                                    <div className="card-title">{card.title}</div>
                                </Card>
                            ))}
                        </div>

                        {/* Nút thêm Thẻ */}
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

                {/* Nút thêm Cột mới */}
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
            </div>

            {/* Modal Thêm Cột */}
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

            {/* Modal Thêm Thẻ */}
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