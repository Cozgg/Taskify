import React, { useEffect, useState } from 'react';
import { Card, Col, Row, Spin, Statistic, Typography, message } from 'antd';
import cookies from 'react-cookies';
import { authApis, endpoints } from '../../utils/Apis';

const { Title, Text } = Typography;

const AdminDashboard = () => {
    const [loading, setLoading] = useState(false);
    const [data, setData] = useState(null);

    const load = async () => {
        try {
            setLoading(true);
            const token = cookies.load('token');
            const api = authApis(token);
            const res = await api.get(endpoints['admin-dashboard']);
            setData(res.data || null);
        } catch (err) {
            message.error(
                'Lỗi tải dashboard: ' +
                (err.response?.data?.message || err.response?.data?.error || err.message)
            );
            setData(null);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        load();
    }, []);

    return (
        <div>
            <div style={{ marginBottom: 16 }}>
                <Title level={3} style={{ margin: 0 }}>Dashboard</Title>
                <Text type="secondary">Thống kê tổng quan hệ thống</Text>
            </div>

            {loading ? (
                <div style={{ display: 'flex', justifyContent: 'center', padding: 40 }}>
                    <Spin size="large" />
                </div>
            ) : (
                <Row gutter={[16, 16]}>
                    <Col xs={24} md={8}>
                        <Card>
                            <Statistic title="Users" value={Number(data?.countUsers ?? 0)} />
                        </Card>
                    </Col>
                    <Col xs={24} md={8}>
                        <Card>
                            <Statistic title="Workspaces" value={Number(data?.countWorkspaces ?? 0)} />
                        </Card>
                    </Col>
                    <Col xs={24} md={8}>
                        <Card>
                            <Statistic title="Boards" value={Number(data?.countBoards ?? 0)} />
                        </Card>
                    </Col>
                </Row>
            )}
        </div>
    );
};

export default AdminDashboard;
