import React, { useState, useEffect } from 'react';
import { Modal, Input, Button, Popconfirm, message, Avatar, Tag, Typography } from 'antd';
const { Text } = Typography;

export const CardDetailModal = ({ open, card, onClose, onUpdate, onDelete }) => {
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');

    useEffect(() => {
        if (card) {
            setTitle(card.title || '');
            setDescription(card.description || '');
        }
    }, [card]);

    const handleSave = () => {
        if (!title.trim()) {
            message.warning("Tên thẻ không được để trống!");
            return;
        }
        onUpdate(card.id, { title, description });
    };

    return (
        <Modal
            title="Chi tiết thẻ"
            open={open}
            onCancel={onClose}
            footer={null}
        >
            <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                <div>
                    <div style={{ marginBottom: 4, fontWeight: 500 }}>Tên thẻ</div>
                    <Input 
                        value={title} 
                        onChange={(e) => setTitle(e.target.value)} 
                    />
                </div>
                <div>
                    <div style={{ marginBottom: 4, fontWeight: 500 }}>Mô tả</div>
                    <Input.TextArea 
                        rows={4} 
                        value={description} 
                        onChange={(e) => setDescription(e.target.value)} 
                    />
                </div>
                {card?.user && (
                    <div>
                        <div style={{ marginBottom: 4, fontWeight: 500 }}>Người phụ trách</div>
                        <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                            <Avatar size="small">{card.user.username?.charAt(0).toUpperCase()}</Avatar>
                            <Text>{card.user.username}</Text>
                        </div>
                    </div>
                )}
                <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: 16 }}>
                    <Popconfirm
                        title="Xóa thẻ này?"
                        description="Hành động này không thể hoàn tác."
                        onConfirm={() => onDelete(card.id, card.listId)}
                        okText="Xóa"
                        cancelText="Hủy"
                        okButtonProps={{ danger: true }}
                    >
                        <Button danger>Xóa thẻ</Button>
                    </Popconfirm>
                    <div style={{ display: 'flex', gap: '8px' }}>
                        <Button onClick={onClose}>Hủy</Button>
                        <Button type="primary" onClick={handleSave}>Lưu cập nhật</Button>
                    </div>
                </div>
            </div>
        </Modal>
    );
};
