import React, { useState, useEffect, useRef } from 'react';
import { Modal, Input, Button, Popconfirm, message, Spin, Avatar, Tooltip, Divider, Space } from 'antd';
import cookies from "react-cookies"
import { authApis, endpoints } from '../utils/Apis';
import { AlignLeftOutlined, CommentOutlined, DeleteOutlined, PaperClipOutlined, PlusOutlined, UserOutlined } from '@ant-design/icons';
import AssignMemberModal from './AssignMemberModal';
// Đảm bảo bạn đã import các thư viện authApis, endpoints, cookies ở đây

export const CardDetailModal = ({ open, card, onClose, onUpdate, onDelete, workspaceId }) => {
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [attachments, setAttachments] = useState([]);
    const [uploading, setUploading] = useState(false);
    const [comments, setComments] = useState([]);
    const [newComment, setNewComment] = useState('');
    const [members, setMembers] = useState([]);
    const [isAssignModalOpen, setIsAssignModalOpen] = useState(false);

    // Dùng useRef để "giấu" thẻ input file mặc định xấu xí
    const fileInputRef = useRef(null);

    useEffect(() => {
        if (card) {
            setTitle(card.title || '');
            setDescription(card.description || '');
            loadAttachments();
            loadComments();
            loadMembers();
        } else {
            setAttachments([]);
        }
    }, [card]);

    const handleOpenAssignModal = () => {
        setIsAssignModalOpen(true);
    }
    const loadMembers = async () => {
        if (!card) return;
        try {
            const token = cookies.load('token');
            const res = await authApis(token).get(endpoints['card-members'](card.id));
            setMembers(res.data || []);
        } catch (error) {

        }
    }

    const handleRemoveMember = async (memberId) => {
        if (!card) return;
        try {
            const token = cookies.load('token');
            await authApis(token).delete(endpoints['unassign-card'](card.id), {
                data: {
                    userId: memberId
                }
            });
            message.success("Đã xóa thành công")
            setMembers(members.filter(member => member.id !== memberId));
        } catch (error) {
            console.log(error);
        }
    }


    const loadAttachments = async () => {
        if (!card) return;
        try {
            const token = cookies.load('token');
            const res = await authApis(token).get(endpoints['card-attachments'](card.id));
            setAttachments(res.data || []);
        } catch (error) {
            console.log("Lỗi tải attachments:", error);
        }
    }

    const handleUploadClick = () => {
        fileInputRef.current.click();
    };

    const handleFileChange = async (e) => {
        const file = e.target.files[0];
        if (!file) return;

        setUploading(true);
        const formData = new FormData();
        formData.append('file', file);
        formData.append('name', file.name);

        try {
            const token = cookies.load('token');
            let res = await authApis(token).post(endpoints['attach-file'](card.id), formData);
            message.success('Tải file lên thành công!');
            setAttachments(prev => [...prev, res.data]);
        } catch (error) {
            console.error("Lỗi upload:", error);
            message.error('Lỗi khi tải file lên!');
        } finally {
            setUploading(false);
            if (fileInputRef.current) {
                fileInputRef.current.value = null;
            }
        }
    };

    const handleDeleteAttachment = async (attachId) => {
        try {
            const token = cookies.load('token');
            await authApis(token).delete(endpoints['delete-attachment'](attachId));
            message.success('Đã xóa file đính kèm!');
            setAttachments(prev => prev.filter(a => a.id !== attachId));
        } catch (error) {
            console.error("Lỗi xóa file:", error);
            message.error('Không thể xóa file!');
        }
    };

    const currentUser = cookies.load('userdata');

    const handleDownload = async (url, filename) => {
        try {
            const response = await fetch(url);
            const blob = await response.blob();

            const blobUrl = window.URL.createObjectURL(blob);

            const link = document.createElement('a');
            link.href = blobUrl;

            const extension = url.split('.').pop();
            link.download = filename.includes('.') ? filename : `${filename}.${extension}`;

            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
            window.URL.revokeObjectURL(blobUrl);

        } catch (error) {
            console.error("Lỗi khi tải file:", error);
            window.open(url, '_blank');
        }
    };

    const handleSave = () => {
        if (!title.trim()) {
            message.warning("Tên thẻ không được để trống!");
            return;
        }
        onUpdate(card.id, { title, description });
    };


    const loadComments = async () => {
        try {
            const token = cookies.load('token');
            let res = await authApis(token).get(endpoints['comments'](card.id));
            setComments(res.data.data || res.data || []);
        } catch (error) {
            console.log(error);
            message.warning("Lỗi comments");
        }

    }

    const handleAddComment = async () => {
        if (!newComment.trim()) return;
        try {
            const token = cookies.load('token');
            const res = await authApis(token).post(endpoints['add-comment'](card.id), { content: newComment });
            setComments(prev => [...prev, res.data]);
            setNewComment('');
        } catch (error) {
            console.log(error);
            message.warning("Lỗi thêm comments");
        }
    }

    const deleteComment = async (commentId) => {
        try {
            const token = cookies.load('token');
            await authApis(token).delete(endpoints['delete-comment'](commentId));
            setComments(prev => prev.filter(comment => comment.id !== commentId));
            message.success("Đã xóa bình luận");
        } catch (error) {
            message.warning("Không thể xóa comments người khác");
        }
    }

    return (
        <Modal
            title={<Input value={title} variant="borderless" style={{ fontWeight: 600, fontSize: 18 }} onChange={(e) => setTitle(e.target.value)} />}
            open={open}
            onCancel={onClose}
            footer={null}
            width={700}
        >
            <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>

                <div style={{ marginLeft: 36, display: 'flex', alignItems: 'center', flexWrap: 'wrap', gap: '8px' }}>
                    {/* Hiển thị danh sách thành viên hiện có */}
                    {members.map(m => (
                        <div key={m.id} style={{ position: 'relative', display: 'inline-block' }}>
                            <Tooltip title={m.username}>
                                <Avatar src={m.avatar} icon={!m.avatar && <UserOutlined />} />
                            </Tooltip>

                            {/* Nút xóa thành viên (chỉ hiện nếu là admin hoặc có quyền) */}
                            <Popconfirm
                                title="Gỡ thành viên này?"
                                onConfirm={() => handleRemoveMember(m.id)}
                                okText="Gỡ"
                                cancelText="Hủy"
                            >
                                <div style={{
                                    position: 'absolute',
                                    top: -5,
                                    right: -5,
                                    background: '#ef4444',
                                    color: 'white',
                                    borderRadius: '50%',
                                    width: '16px',
                                    height: '16px',
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'center',
                                    fontSize: '10px',
                                    cursor: 'pointer',
                                    border: '2px solid white'
                                }}>
                                    ✕
                                </div>
                            </Popconfirm>
                        </div>
                    ))}

                    {/* NÚT THÊM THÀNH VIÊN: Luôn hiển thị dù có member hay chưa */}
                    <Tooltip title="Thêm thành viên">
                        <Button
                            shape="circle"
                            icon={<PlusOutlined />}
                            size="small"
                            onClick={handleOpenAssignModal}
                        />
                    </Tooltip>
                    <AssignMemberModal
                        open={isAssignModalOpen}
                        onClose={() => setIsAssignModalOpen(false)}
                        workspaceId={workspaceId}
                        cardId={card?.id}
                        onAssignSuccess={() => {
                            setIsAssignModalOpen(false);
                            loadMembers();
                        }}
                    />
                </div>

                {/* 2. MÔ TẢ */}
                <div>
                    <div style={{ display: 'flex', alignItems: 'center', gap: 12, marginBottom: 8 }}>
                        <AlignLeftOutlined style={{ fontSize: 18 }} />
                        <span style={{ fontWeight: 600, fontSize: 16 }}>Mô tả</span>
                    </div>
                    <div style={{ marginLeft: 36 }}>
                        <Input.TextArea
                            rows={3}
                            placeholder="Thêm mô tả chi tiết hơn..."
                            value={description}
                            onChange={(e) => setDescription(e.target.value)}
                            style={{ backgroundColor: '#091e420a', border: 'none' }}
                        />
                    </div>
                </div>

                {/* 3. TỆP ĐÍNH KÈM */}
                <div>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 8 }}>
                        <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
                            <PaperClipOutlined style={{ fontSize: 18 }} />
                            <span style={{ fontWeight: 600, fontSize: 16 }}>Tệp đính kèm</span>
                        </div>
                        <Button size="small" onClick={handleUploadClick} loading={uploading}>Thêm</Button>
                        <input type="file" ref={fileInputRef} style={{ display: 'none' }} onChange={handleFileChange} />
                    </div>
                    <div style={{ marginLeft: 36, display: 'flex', flexDirection: 'column', gap: 8 }}>
                        {attachments.map(file => (
                            <div key={file.id} style={{ display: 'flex', gap: 12, padding: 8, borderRadius: 8, backgroundColor: '#091e420a' }}>
                                <div style={{ flex: 1 }}>
                                    <div style={{ fontWeight: 600, fontSize: 14 }}>{file.filename}</div>
                                    <div style={{ fontSize: 12, color: '#5e6c84' }}>Đã thêm {file.updateDate}</div>
                                    <Space split={<Divider type="vertical" />} style={{ marginTop: 4 }}>
                                        <Button type="link" size="small" style={{ padding: 0 }} onClick={() => handleDownload(file.url, file.filename)}>Tải về</Button>
                                        <Popconfirm title="Xóa tệp này?" onConfirm={() => handleDeleteAttachment(file.id)}>
                                            <Button type="link" danger size="small" style={{ padding: 0 }}>Xóa</Button>
                                        </Popconfirm>
                                    </Space>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>

                {/* 4. BÌNH LUẬN */}
                <div>
                    <div style={{ display: 'flex', alignItems: 'center', gap: 12, marginBottom: 16 }}>
                        <CommentOutlined style={{ fontSize: 18 }} />
                        <span style={{ fontWeight: 600, fontSize: 16 }}>Hoạt động</span>
                    </div>

                    {/* Ô nhập bình luận */}
                    <div style={{ display: 'flex', gap: 12, marginLeft: 8, marginBottom: 24 }}>
                        <Avatar src={currentUser?.avatar} icon={!currentUser?.avatar && <UserOutlined />} />
                        <div style={{ flex: 1 }}>
                            <Input.TextArea
                                placeholder="Viết bình luận..."
                                autoSize={{ minRows: 1 }}
                                value={newComment}
                                onChange={(e) => setNewComment(e.target.value)}
                                style={{ boxShadow: '0 1px 1px #091e4240, 0 0 1px #091e424f' }}
                            />
                            {newComment && (
                                <Button type="primary" size="small" style={{ marginTop: 8 }} onClick={handleAddComment}>Lưu</Button>
                            )}
                        </div>
                    </div>

                    {/* Danh sách bình luận */}
                    <div style={{ marginLeft: 8, display: 'flex', flexDirection: 'column', gap: 20 }}>
                        {comments.map(c => (
                            <div key={c.id} style={{ display: 'flex', gap: 12 }}>
                                <Avatar src={c.user?.avatar} icon={!c.user?.avatar && <UserOutlined />} />
                                <div style={{ flex: 1 }}>
                                    <Space style={{ marginBottom: 4 }}>
                                        <span style={{ fontWeight: 700, fontSize: 14 }}>{c.user?.username}</span>
                                        <span style={{ color: '#5e6c84', fontSize: 12 }}>{c.createdDate}</span>
                                    </Space>
                                    <div style={{ backgroundColor: '#fff', padding: '8px 12px', borderRadius: 8, border: '1px solid #dfe1e6', fontSize: 14 }}>
                                        {c.comment}
                                    </div>
                                    {currentUser && currentUser.data.userId === c.user?.id && (
                                        <Popconfirm title="Xóa bình luận?" onConfirm={() => deleteComment(c.id)}>
                                            <Button type="link" size="small" style={{ padding: 0, fontSize: 12, color: '#5e6c84' }}>Xóa</Button>
                                        </Popconfirm>
                                    )}
                                </div>
                            </div>
                        ))}
                    </div>
                </div>

                <Divider style={{ margin: '8px 0' }} />

                {/* NÚT THAO TÁC CUỐI CÙNG */}
                <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                    <Popconfirm title="Xóa thẻ này?" onConfirm={() => onDelete(card.id, card.listId)}>
                        <Button danger icon={<DeleteOutlined />}>Xóa thẻ</Button>
                    </Popconfirm>
                    <Space>
                        <Button onClick={onClose}>Đóng</Button>
                        <Button type="primary" onClick={() => handleSave(card.id, { title, description })}>Lưu cập nhật</Button>
                    </Space>
                </div>
            </div>
        </Modal>
    );
};