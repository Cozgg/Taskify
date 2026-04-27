import React, { useCallback, useEffect, useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { Button, Card, Typography, Select, Input, Modal, Tag, Spin, message, Dropdown, Popconfirm } from 'antd';
import { PlusOutlined, MoreOutlined, ArrowLeftOutlined, CloseOutlined, BarChartOutlined } from '@ant-design/icons';
import { DragDropContext, Droppable, Draggable } from '@hello-pangea/dnd';
import { CardDetailModal } from './CardDetailModal';
import cookies from 'react-cookies';
import { authApis, endpoints } from '../utils/Apis';
import './BoardDetail.css';
import { useSearchParams } from 'react-router-dom';
import { BoardStatistics } from './BoardStatistics';
const { Title, Text } = Typography;
const BoardDetail = () => {
    const { boardId } = useParams();
    const nav = useNavigate();

    const [board, setBoard] = useState(null);
    const [columns, setColumns] = useState([]);
    const [loading, setLoading] = useState(true);
    const [isAddingList, setIsAddingList] = useState(false);
    const [newListName, setNewListName] = useState('');
    const [isCardModalOpen, setIsCardModalOpen] = useState(false);
    const [activeColId, setActiveColId] = useState(null);
    const [newCardTitle, setNewCardTitle] = useState('');
    const [editingListId, setEditingListId] = useState(null);
    const [listNameEdit, setListNameEdit] = useState('');
    const [selectedCard, setSelectedCard] = useState(null);
    const [isCardDetailModalOpen, setIsCardDetailModalOpen] = useState(false);
    const [searchParams] = useSearchParams();
    const workspaceId = searchParams.get('workspaceId');
    const [isStatModalOpen, setIsStatModalOpen] = useState(false);
    const statusColors = {
        "To do": "#ebecf0",
        "Doing": "#0079bf",
        "Done": "#61bd4f"
    };
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
                                    user: card.user,
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
        if (!newListName.trim()) {
            setIsAddingList(false);
            return;
        }

        try {
            const token = cookies.load('token');
            const api = authApis(token);
            const payload = {
                name: newListName.trim(),
                status: 'TODO',
                position: columns.length + 1,
            };
            const res = await api.post(endpoints['create-list'](boardId), payload);
            const created = res.data?.data ?? res.data;
            const newCol = {
                id: created.id,
                title: created.name,
                status: created.status,
                cards: [],
            };
            setColumns([...columns, newCol]);
            setNewListName('');
            setIsAddingList(false);
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

    const onDragEnd = async (result) => {
        const { destination, source, type, draggableId } = result;

        if (!destination) return;
        if (destination.droppableId === source.droppableId && destination.index === source.index) return;

        if (type === 'card') {
            const sourceCol = columns.find(col => col.id.toString() === source.droppableId);
            const destCol = columns.find(col => col.id.toString() === destination.droppableId);
            const cardId = parseInt(draggableId);
            const newListId = parseInt(destination.droppableId);

            if (sourceCol === destCol) {
                const newCards = Array.from(sourceCol.cards);
                const [removed] = newCards.splice(source.index, 1);
                newCards.splice(destination.index, 0, removed);

                setColumns(columns.map(col => col.id === sourceCol.id ? { ...col, cards: newCards } : col));
            } else {
                const sourceCards = Array.from(sourceCol.cards);
                const destCards = Array.from(destCol.cards);
                const [removed] = sourceCards.splice(source.index, 1);
                destCards.splice(destination.index, 0, removed);

                setColumns(columns.map(col => {
                    if (col.id === sourceCol.id) return { ...col, cards: sourceCards };
                    if (col.id === destCol.id) return { ...col, cards: destCards };
                    return col;
                }));
            }

            try {
                const token = cookies.load('token');
                const api = authApis(token);
                const payload = {
                    newListId: newListId,
                    newPosition: destination.index
                };
                await api.patch(endpoints['move-card'](cardId), payload);
            } catch (err) {
                message.error("Lỗi khi lưu vị trí thẻ: " + (err.response?.data?.error || err.response?.data || err.message));
            }
        }
    };

    const handleRenameList = async (listId) => {
        try {
            const currentList = columns.find(col => col.id === listId);
            if (!currentList) return;
            const token = cookies.load('token');
            const api = authApis(token);

            const payload = {
                name: listNameEdit,
                status: currentList.status,
            };

            await api.put(endpoints['update-list'](listId), payload);

            setColumns(columns.map(col => col.id === listId ? { ...col, title: listNameEdit } : col));
            setEditingListId(null);
            message.success("Đổi tên danh sách thành công!");
        } catch (err) {
            message.error("Lỗi đổi tên danh sách: " + (err.response?.data?.error || err.message));
        }
    };

    const handleDeleteList = async (listId) => {
        try {
            const token = cookies.load('token');
            const api = authApis(token);
            await api.delete(endpoints['delete-list'](listId));

            setColumns(columns.filter(col => col.id !== listId));
            message.success("Đã xóa danh sách!");
        } catch (err) {
            message.error("Lỗi xóa danh sách: " + (err.response?.data?.error || err.message));
        }
    };

    const handleUpdateCard = async (cardId, newData) => {
        try {
            const token = cookies.load('token');
            const api = authApis(token);
            const payload = {
                name: newData.title,
                description: newData.description
            };
            await api.put(endpoints['update-card'](cardId), payload);

            setColumns(columns.map(col => ({
                ...col,
                cards: col.cards.map(card => card.id === cardId ? { ...card, ...newData } : card)
            })));
            setIsCardDetailModalOpen(false);
            message.success("Cập nhật thẻ thành công!");
        } catch (err) {
            message.error("Lỗi cập nhật thẻ: " + (err.response?.data?.error || err.message));
        }
    };



    const handleDeleteCard = async (cardId, listId) => {
        try {
            const token = cookies.load('token');
            const api = authApis(token);
            await api.delete(endpoints['delete-card'](cardId));

            setColumns(columns.map(col => col.id === listId ? {
                ...col,
                cards: col.cards.filter(c => c.id !== cardId)
            } : col));
            setIsCardDetailModalOpen(false);
            message.success("Đã xóa thẻ!");
        } catch (err) {
            message.error("Lỗi xóa thẻ: " + (err.response?.data?.error || err.message));
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
                <Button
                    type="primary"
                    icon={<BarChartOutlined />}
                    onClick={() => setIsStatModalOpen(true)}
                >
                    Thống kê
                </Button>
            </div>

            <div className="board-canvas">
                {loading ? (
                    <div className="board-loading">
                        <Spin size="large" />
                    </div>
                ) : (
                    <DragDropContext onDragEnd={onDragEnd}>
                        <div style={{ display: 'flex', gap: '16px', alignItems: 'flex-start', height: '100%' }}>
                            {columns.map((col) => (
                                <div className="kanban-column" key={col.id}>
                                    <div className="kanban-column-header">
                                        {editingListId === col.id ? (
                                            <Input
                                                autoFocus
                                                value={listNameEdit}
                                                onChange={(e) => setListNameEdit(e.target.value)}
                                                onBlur={() => handleRenameList(col.id)}
                                                onPressEnter={() => handleRenameList(col.id)}
                                            />
                                        ) : (
                                            <>
                                                <Text strong className="kanban-column-header-title">{col.title}</Text>
                                                <Dropdown
                                                    menu={{
                                                        items: [
                                                            {
                                                                key: 'rename',
                                                                label: 'Đổi tên',
                                                                onClick: () => { setEditingListId(col.id); setListNameEdit(col.title); }
                                                            },
                                                            {
                                                                key: 'delete',
                                                                label: (
                                                                    <Popconfirm title="Xóa danh sách này?" onConfirm={() => handleDeleteList(col.id)} okText="Xóa" cancelText="Hủy">
                                                                        <span style={{ color: 'red' }}>Xóa danh sách</span>
                                                                    </Popconfirm>
                                                                )
                                                            }
                                                        ]
                                                    }}
                                                    trigger={['click']}
                                                >
                                                    <Button type="text" icon={<MoreOutlined />} size="small" />
                                                </Dropdown>
                                            </>
                                        )}
                                    </div>

                                    <Droppable droppableId={col.id.toString()} type="card">
                                        {(provided, snapshot) => (
                                            <div
                                                className="kanban-cards-list"
                                                ref={provided.innerRef}
                                                {...provided.droppableProps}
                                                style={{
                                                    minHeight: col.cards.length === 0 ? '80px' : undefined,
                                                    backgroundColor: snapshot.isDraggingOver ? 'rgba(0,121,191,0.08)' : undefined,
                                                    borderRadius: '4px',
                                                    transition: 'background-color 0.15s ease',
                                                }}
                                            >
                                                {col.cards.map((card, cardIndex) => (
                                                    <Draggable draggableId={card.id.toString()} index={cardIndex} key={card.id}>
                                                        {(provided) => (
                                                            <div
                                                                ref={provided.innerRef}
                                                                {...provided.draggableProps}
                                                                {...provided.dragHandleProps}
                                                                style={{
                                                                    ...provided.draggableProps.style,
                                                                    marginBottom: '8px'
                                                                }}
                                                            >
                                                                <Card
                                                                    className="kanban-card"
                                                                    size="small"
                                                                    hoverable
                                                                    onClick={() => {
                                                                        setSelectedCard({ ...card, listId: col.id });
                                                                        setIsCardDetailModalOpen(true);
                                                                    }}
                                                                >
                                                                    {card.description && (
                                                                        <Tag color="blue" style={{ marginBottom: 8 }}>
                                                                            {card.description}
                                                                        </Tag>
                                                                    )}
                                                                    <div className="card-title">{card.title}</div>
                                                                </Card>
                                                            </div>
                                                        )}
                                                    </Draggable>
                                                ))}
                                                {provided.placeholder}
                                            </div>
                                        )}
                                    </Droppable>

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
                                {isAddingList ? (
                                    <div className="add-column-form">
                                        <Input
                                            autoFocus
                                            placeholder="Nhập tiêu đề danh sách..."
                                            value={newListName}
                                            onChange={(e) => setNewListName(e.target.value)}
                                            onPressEnter={handleAddColumn}
                                        />
                                        <div style={{ marginTop: '8px', display: 'flex', gap: '8px', alignItems: 'center' }}>
                                            <Button type="primary" onClick={handleAddColumn}>Thêm danh sách</Button>
                                            <Button icon={<CloseOutlined />} type="text" onClick={() => { setIsAddingList(false); setNewListName(''); }} />
                                        </div>
                                    </div>
                                ) : (
                                    <Button
                                        className="add-column-btn"
                                        icon={<PlusOutlined />}
                                        onClick={() => setIsAddingList(true)}
                                    >
                                        {columns.length === 0 ? "Thêm danh sách" : "Thêm danh sách khác"}
                                    </Button>
                                )}
                            </div>
                        </div>
                    </DragDropContext>
                )}
            </div>



            <Modal title="Thêm thẻ mới" open={isCardModalOpen} onOk={handleAddCard} onCancel={() => setIsCardModalOpen(false)} okText="Thêm thẻ" cancelText="Hủy">
                <Input.TextArea
                    placeholder="Nhập tiêu đề cho thẻ này..."
                    autoSize={{ minRows: 2, maxRows: 6 }}
                    value={newCardTitle}
                    onChange={(e) => setNewCardTitle(e.target.value)}
                />
            </Modal>

            <CardDetailModal
                open={isCardDetailModalOpen}
                card={selectedCard}
                onClose={() => { setIsCardDetailModalOpen(false); setSelectedCard(null); }}
                onUpdate={handleUpdateCard}
                onDelete={handleDeleteCard}
                workspaceId={workspaceId}
            />
            <Modal
                title={`Thống kê bảng: ${board?.name || ''}`}
                open={isStatModalOpen}
                onCancel={() => setIsStatModalOpen(false)}
                footer={null}
                width={700}
            >
                <BoardStatistics boardId={boardId} />
            </Modal>
        </div>
    );
};

export default BoardDetail;