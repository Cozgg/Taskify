import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Typography, Button, Avatar, Tabs, Card, Row, Col, Modal, Input, List, Tag } from 'antd';
import { UserAddOutlined, TableOutlined, TeamOutlined, SettingOutlined, PlusOutlined } from '@ant-design/icons';
import './WorkspaceDetail.css'; // Nhớ import file CSS

const { Title, Text } = Typography;

// --- MOCK DATA ---
const mockWorkspaceInfo = {
    name: "Dự án Tốt nghiệp",
    description: "Không gian làm việc cho nhóm đồ án học kỳ này.",
};

const mockBoards = [
    { id: 101, title: "Thiết kế Database", color: "#0079BF" },
    { id: 102, title: "Phát triển Backend", color: "#D29034" },
    { id: 103, title: "Giao diện React", color: "#519839" },
];

const mockMembers = [
    { id: 1, name: "Trần Văn A", role: "Quản trị viên", color: '#f56a00' },
    { id: 2, name: "Nguyễn Thị B", role: "Thành viên", color: '#7265e6' },
    { id: 3, name: "Lê Hoàng C", role: "Thành viên", color: '#ffbf00' },
    { id: 4, name: "Phạm D", role: "Thành viên", color: '#00a2ae' },
];
// -----------------

const WorkspaceDetail = () => {
    const { workspaceId } = useParams();
    const nav = useNavigate();
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [inviteEmail, setInviteEmail] = useState('');

    // Xử lý Modal Mời thành viên
    const handleInvite = () => {
        console.log("Đang mời user với email/ID:", inviteEmail);
        setIsModalVisible(false);
        setInviteEmail('');
        // Gắn gọi API invite vào đây sau
    };

    // --- TAB 1: GIAO DIỆN CÁC BẢNG (BOARDS) ---
    const BoardsTab = () => (
        <Row gutter={[16, 16]} style={{ marginTop: '16px' }}>
            {mockBoards.map(board => (
                <Col xs={24} sm={12} md={8} lg={6} key={board.id}>
                    <Card
                        hoverable
                        className="board-card"
                        style={{ backgroundColor: board.color }}
                        onClick={() => nav(`/board/${board.id}`)} // Mở trang chi tiết Board
                    >
                        <h3 className="board-title">{board.title}</h3>
                    </Card>
                </Col>
            ))}

            {/* Thẻ tạo bảng mới */}
            <Col xs={24} sm={12} md={8} lg={6}>
                <Card hoverable className="board-card create-board-card">
                    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100%' }}>
                        <Text><PlusOutlined /> Tạo bảng mới</Text>
                    </div>
                </Card>
            </Col>
        </Row>
    );

    // --- TAB 2: GIAO DIỆN QUẢN LÝ THÀNH VIÊN ---
    const MembersTab = () => (
        <div style={{ marginTop: '16px', background: '#fff', padding: '24px', borderRadius: '8px' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '16px' }}>
                <Title level={4}>Thành viên của Không gian làm việc ({mockMembers.length})</Title>
                <Text type="secondary">Chỉ quản trị viên mới có thể xóa thành viên.</Text>
            </div>
            <List
                itemLayout="horizontal"
                dataSource={mockMembers}
                renderItem={item => (
                    <List.Item
                        actions={[<Button type="link" danger>Xóa</Button>]}
                    >
                        <List.Item.Meta
                            avatar={<Avatar style={{ backgroundColor: item.color }}>{item.name.charAt(0)}</Avatar>}
                            title={<b>{item.name}</b>}
                            description={
                                item.role === 'Quản trị viên'
                                    ? <Tag color="blue">{item.role}</Tag>
                                    : <Tag>{item.role}</Tag>
                            }
                        />
                    </List.Item>
                )}
            />
        </div>
    );

    // --- CẤU HÌNH CÁC TABS ---
    const tabItems = [
        { key: '1', label: <span><TableOutlined /> Bảng</span>, children: <BoardsTab /> },
        { key: '2', label: <span><TeamOutlined /> Thành viên</span>, children: <MembersTab /> },
        { key: '3', label: <span><SettingOutlined /> Cài đặt</span>, children: <p>Nội dung cài đặt workspace...</p> },
    ];

    return (
        <div className="workspace-detail-container">
            {/* Header Workspace */}
            <div className="workspace-detail-header">
                <div className="workspace-info-block">
                    <Avatar shape="square" size={64} style={{ backgroundColor: '#0052CC', fontSize: '24px', fontWeight: 'bold' }}>
                        {mockWorkspaceInfo.name.charAt(0)}
                    </Avatar>
                    <div>
                        <Title level={2} style={{ margin: 0 }}>{mockWorkspaceInfo.name}</Title>
                        <Text type="secondary">{mockWorkspaceInfo.description} (ID: {workspaceId})</Text>
                    </div>
                </div>

                <div className="workspace-actions">
                    <Avatar.Group maxCount={3} size="large" maxStyle={{ color: '#f56a00', backgroundColor: '#fde3cf' }}>
                        {mockMembers.map(m => (
                            <Avatar key={m.id} style={{ backgroundColor: m.color }}>{m.name.charAt(0)}</Avatar>
                        ))}
                    </Avatar.Group>
                    <Button
                        type="primary"
                        icon={<UserAddOutlined />}
                        onClick={() => setIsModalVisible(true)}
                        className="trello-btn"
                        style={{ marginLeft: '16px' }}
                    >
                        Mời thành viên
                    </Button>
                </div>
            </div>

            {/* Nội dung Tabs */}
            <Tabs defaultActiveKey="1" items={tabItems} className="workspace-tabs" />

            {/* Modal Mời thành viên */}
            <Modal
                title="Mời thành viên vào Workspace"
                open={isModalVisible}
                onOk={handleInvite}
                onCancel={() => setIsModalVisible(false)}
                okText="Gửi lời mời"
                cancelText="Hủy"
            >
                <p>Nhập email hoặc ID của người dùng bạn muốn mời:</p>
                <Input
                    placeholder="VD: user@gmail.com hoặc user_id"
                    value={inviteEmail}
                    onChange={(e) => setInviteEmail(e.target.value)}
                    size="large"
                />
            </Modal>
        </div>
    );
}

export default WorkspaceDetail;