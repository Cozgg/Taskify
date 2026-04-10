import React, { useContext, useEffect, useState } from 'react';
import { Link, useNavigate } from "react-router-dom";
import { Card, Button, Row, Col, Typography, Spin } from 'antd';
import { PlusOutlined, TeamOutlined, SettingOutlined } from '@ant-design/icons';
import './Home.css';
import { authApis, endpoints } from '../utils/Apis';
import { MyContext } from '../utils/context/MyContext';
import cookies from 'react-cookies';
import Logout from './Logout';
const { Title, Text } = Typography;


const Home = () => {
    const [user] = useContext(MyContext);
    const [workspaces, setWorkspaces] = useState([]);
    const [loading, setLoading] = useState(false);

    const nav = useNavigate();
    const loadWorkspaces = async () => {
        try {
            setLoading(true);
            const token = cookies.load('token');
            const res = await authApis(token).get(endpoints['workspaces'](user.userdata.data.userId));
            if (res.status === 200) {
                setWorkspaces([res.data.data]);
                // console.log(res.data);

            }
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        loadWorkspaces();
    }, [user])


    return (
        <div className="kanban-home-container">
            {/* Nội dung chính */}
            <main className="kanban-content">
                <div className="workspace-header">
                    <Title level={3} style={{ color: '#172b4d', margin: 0 }}>Các Không gian làm việc</Title>
                    <Button type="primary" icon={<PlusOutlined />} className="trello-btn">
                        Tạo Workspace
                    </Button>
                </div>

                {/* Hiển thị vòng xoay loading khi đang gọi API */}
                {loading ? (
                    <div style={{ textAlign: 'center', marginTop: '50px' }}><Spin size="large" /></div>
                ) : (
                    <Row gutter={[24, 24]} style={{ marginTop: '24px' }}>
                        {workspaces ? (
                            workspaces.map(ws => (
                                <Col xs={24} sm={12} md={8} lg={6} key={ws.id}>
                                    <Card hoverable className="workspace-card" style={{ padding: '0', cursor: 'pointer' }}
                                        onClick={() => nav(`/workspace/${ws.id}`)}>
                                        <div className="workspace-cover" style={{ backgroundColor: ws.color || '#0052CC' }}>
                                            <h3>{ws.name}</h3>
                                        </div>
                                        <div className="workspace-info">
                                            {/* Giả sử API trả về số lượng member trong biến memberCount */}
                                            <Text type="secondary"><TeamOutlined /> {ws.memberCount || 0} thành viên</Text>
                                            <Button type="text" icon={<SettingOutlined />} size="small"
                                                onClick={(e) => {
                                                    e.stopPropagation();
                                                    console.log("Mở popup cài đặt");
                                                }}>Cài đặt</Button>
                                        </div>
                                    </Card>
                                </Col>
                            ))
                        ) :
                            (


                                <Col span={24} style={{ textAlign: 'center', padding: '40px 0' }}>
                                    <Text type="secondary">Bạn chưa có không gian làm việc nào. Hãy tạo một workspace để bắt đầu!</Text>
                                </Col>

                            )
                        }

                    </Row>
                )}
            </main>
        </div>
    );
}

export default Home;