import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
    Typography,
    Button,
    Avatar,
    Tabs,
    Card,
    Row,
    Col,
    Modal,
    Input,
    List,
    Tag,
    Spin,
    Pagination,
    Empty,
    message,
} from 'antd';
import {
    UserAddOutlined,
    TableOutlined,
    TeamOutlined,
    SettingOutlined,
    PlusOutlined,
    SearchOutlined,
} from '@ant-design/icons';
import cookies from 'react-cookies';
import { authApis, endpoints } from '../utils/Apis';
import './WorkspaceOverview.css';

const { Title, Text } = Typography;
const BOARD_PAGE_SIZE = 8;
const BOARD_COLORS = ['#0079BF', '#D29034', '#519839', '#B04632', '#89609E', '#CD5A91', '#00B0D0'];

const getBoardColor = (id) => BOARD_COLORS[(id || 0) % BOARD_COLORS.length];

const WorkspaceOverview = () => {
    const { workspaceId } = useParams();
    const nav = useNavigate();

    const [workspace, setWorkspace] = useState(null);
    const [members, setMembers] = useState([]);
    const [boards, setBoards] = useState([]);
    const [loadingWorkspace, setLoadingWorkspace] = useState(true);
    const [loadingBoards, setLoadingBoards] = useState(true);
    const [boardSearchText, setBoardSearchText] = useState('');
    const [committedBoardKw, setCommittedBoardKw] = useState('');
    const [boardPage, setBoardPage] = useState(1);
    const [boardTotalItems, setBoardTotalItems] = useState(0);
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [inviteValue, setInviteValue] = useState('');

    const loadWorkspaceContext = async () => {
        try {
            setLoadingWorkspace(true);
            const token = cookies.load('token');
            const api = authApis(token);

            const [workspaceRes, memberRes] = await Promise.all([
                api.get(endpoints['workspace-detail'](workspaceId)),
                api.get(endpoints['workspace-member'](workspaceId)),
            ]);

            setWorkspace(workspaceRes.data?.data || null);
            setMembers(Array.isArray(memberRes.data) ? memberRes.data : []);
        } catch (err) {
            message.error(
                'Lỗi tải workspace: ' +
                (err.response?.data?.error || err.response?.data?.message || err.message)
            );
        } finally {
            setLoadingWorkspace(false);
        }
    };

    const loadBoards = async (pageNum = 1, kw = committedBoardKw) => {
        try {
            setLoadingBoards(true);
            const token = cookies.load('token');
            const api = authApis(token);

            let url = `${endpoints['workspace-board'](workspaceId)}?page=${pageNum}`;
            if (kw) url += `&kw=${encodeURIComponent(kw)}`;

            const res = await api.get(url);
            const raw = res.data?.data;

            if (raw?.items) {
                setBoards(raw.items);
                setBoardTotalItems(Number(raw.totalItems ?? raw.items.length));
            } else if (Array.isArray(raw)) {
                setBoards(raw);
                setBoardTotalItems(raw.length);
            } else {
                setBoards([]);
                setBoardTotalItems(0);
            }
        } catch (err) {
            setBoards([]);
            setBoardTotalItems(0);
            message.error(
                'Lỗi tải board: ' +
                (err.response?.data?.error || err.response?.data?.message || err.message)
            );
        } finally {
            setLoadingBoards(false);
        }
    };

    useEffect(() => {
        setBoardPage(1);
        setBoardSearchText('');
        setCommittedBoardKw('');
        loadWorkspaceContext();
        loadBoards(1, '');
    }, [workspaceId]);

    useEffect(() => {
        const timer = setTimeout(() => {
            setBoardPage(1);
            setCommittedBoardKw(boardSearchText.trim());
        }, 400);

        return () => clearTimeout(timer);
    }, [boardSearchText]);

    useEffect(() => {
        loadBoards(boardPage, committedBoardKw);
    }, [boardPage, committedBoardKw]);

    const handleInvite = () => {
        message.info(`Chức năng mời thành viên sẽ xử lý sau: ${inviteValue || 'chưa nhập dữ liệu'}`);
        setIsModalVisible(false);
        setInviteValue('');
    };

    const boardsTab = (
        <div className="workspace-section">
            <div className="workspace-section-toolbar">
                <div>
                    <Title level={4} style={{ margin: 0 }}>Boards</Title>
                    <Text type="secondary">
                        {committedBoardKw
                            ? `Kết quả tìm kiếm cho "${committedBoardKw}"`
                            : 'Tất cả board trong workspace này'}
                    </Text>
                </div>

                <div className="workspace-search-wrap">
                    <SearchOutlined className="workspace-search-icon" />
                    <input
                        className="workspace-search-input"
                        placeholder="Tìm board..."
                        value={boardSearchText}
                        onChange={(e) => setBoardSearchText(e.target.value)}
                    />
                    {boardSearchText && (
                        <button
                            type="button"
                            className="workspace-search-clear"
                            onClick={() => setBoardSearchText('')}
                        >
                            ×
                        </button>
                    )}
                </div>
            </div>

            {loadingBoards ? (
                <div className="workspace-section-loading">
                    <Spin size="large" />
                </div>
            ) : boards.length === 0 ? (
                <Empty
                    className="workspace-empty"
                    description={
                        committedBoardKw
                            ? `Không tìm thấy board nào cho "${committedBoardKw}".`
                            : 'Workspace này chưa có board nào.'
                    }
                />
            ) : (
                <Row gutter={[16, 16]} style={{ marginTop: '16px' }}>
                    {boards.map((board) => (
                        <Col xs={24} sm={12} md={8} lg={6} key={board.id}>
                            <Card
                                hoverable
                                className="board-card"
                                style={{ backgroundColor: getBoardColor(board.id) }}
                                onClick={() => nav(`/board/${board.id}`)}
                            >
                                <h3 className="board-title">{board.name}</h3>
                            </Card>
                        </Col>
                    ))}

                    {!committedBoardKw && (
                        <Col xs={24} sm={12} md={8} lg={6}>
                            <Card hoverable className="board-card create-board-card">
                                <div className="create-board-content">
                                    <Text><PlusOutlined /> Tạo bảng mới</Text>
                                </div>
                            </Card>
                        </Col>
                    )}
                </Row>
            )}

            {boardTotalItems > BOARD_PAGE_SIZE && (
                <div className="workspace-pagination">
                    <Pagination
                        current={boardPage}
                        pageSize={BOARD_PAGE_SIZE}
                        total={boardTotalItems}
                        showSizeChanger={false}
                        showTotal={(total) => `${total} boards`}
                        onChange={(nextPage) => setBoardPage(nextPage)}
                    />
                </div>
            )}
        </div>
    );

    const membersTab = (
        <div className="workspace-section members-section">
            <div className="workspace-members-header">
                <Title level={4}>Thành viên của workspace ({members.length})</Title>
                <Text type="secondary">Danh sách thành viên hiện có trong workspace.</Text>
            </div>

            <List
                itemLayout="horizontal"
                dataSource={members}
                locale={{ emptyText: 'Chưa có thành viên nào.' }}
                renderItem={(item) => (
                    <List.Item>
                        <List.Item.Meta
                            avatar={<Avatar>{item.username?.charAt(0)?.toUpperCase() || 'U'}</Avatar>}
                            title={<b>{item.username}</b>}
                            description={
                                <>
                                    <div>{item.email}</div>
                                    <Tag color={item.role === 'ADMIN' ? 'blue' : 'default'}>
                                        {item.role || 'MEMBER'}
                                    </Tag>
                                </>
                            }
                        />
                    </List.Item>
                )}
            />
        </div>
    );

    const tabItems = [
        { key: '1', label: <span><TableOutlined /> Bảng</span>, children: boardsTab },
        { key: '2', label: <span><TeamOutlined /> Thành viên</span>, children: membersTab },
        { key: '3', label: <span><SettingOutlined /> Cài đặt</span>, children: <p>Nội dung cài đặt workspace sẽ được bổ sung sau.</p> },
    ];

    if (loadingWorkspace) {
        return (
            <div className="workspace-detail-container">
                <div className="workspace-section-loading">
                    <Spin size="large" />
                </div>
            </div>
        );
    }

    return (
        <div className="workspace-detail-container">
            <div className="workspace-detail-header">
                <div className="workspace-info-block">
                    <Avatar shape="square" size={64} style={{ backgroundColor: '#0052CC', fontSize: '24px', fontWeight: 'bold' }}>
                        {workspace?.name?.charAt(0)?.toUpperCase() || 'W'}
                    </Avatar>
                    <div>
                        <Title level={2} style={{ margin: 0 }}>{workspace?.name || 'Workspace'}</Title>
                        <Text type="secondary">
                            Workspace ID: {workspaceId} • {boardTotalItems} boards • {members.length} members
                        </Text>
                    </div>
                </div>

                <div className="workspace-actions">
                    <Avatar.Group maxCount={3} size="large" maxStyle={{ color: '#f56a00', backgroundColor: '#fde3cf' }}>
                        {members.map((member) => (
                            <Avatar key={member.id}>{member.username?.charAt(0)?.toUpperCase() || 'U'}</Avatar>
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

            <Tabs defaultActiveKey="1" items={tabItems} className="workspace-tabs" />

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
                    value={inviteValue}
                    onChange={(e) => setInviteValue(e.target.value)}
                    size="large"
                />
            </Modal>
        </div>
    );
};

export default WorkspaceOverview;
