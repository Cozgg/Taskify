import React, { useContext, useMemo } from 'react';
import { Layout, Menu, Typography, Alert } from 'antd';
import { Outlet, useLocation, useNavigate } from 'react-router-dom';
import { BarChartOutlined, TeamOutlined, AppstoreOutlined } from '@ant-design/icons';
import { MyContext } from '../../utils/context/MyContext';

const { Sider, Content } = Layout;
const { Title, Text } = Typography;

const AdminLayout = () => {
    const [user] = useContext(MyContext);
    const role = user?.userdata?.data?.role;
    const isAdmin = role === 'ADMIN';

    const location = useLocation();
    const nav = useNavigate();

    const selectedKey = useMemo(() => {
        const path = location.pathname;
        if (path.includes('/admin/users')) return 'users';
        if (path.includes('/admin/workspaces')) return 'workspaces';
        return 'dashboard';
    }, [location.pathname]);

    if (!isAdmin) {
        return (
            <div style={{ padding: 24 }}>
                <Title level={3} style={{ marginTop: 0 }}>Admin</Title>
                <Alert
                    type="error"
                    showIcon
                    message="Bạn không có quyền truy cập khu vực Admin"
                    description={
                        <div>
                            <Text>API /api/admin/** yêu cầu quyền ADMIN.</Text>
                        </div>
                    }
                />
            </div>
        );
    }

    return (
        <Layout style={{ minHeight: 'calc(100vh - 56px)' }}>
            <Sider width={240} theme="light" style={{ borderRight: '1px solid #e8eaf0' }}>
                <div style={{ padding: 16, borderBottom: '1px solid #e8eaf0' }}>
                    <Title level={4} style={{ margin: 0 }}>Admin</Title>
                    <Text type="secondary" style={{ fontSize: 12 }}>Quản trị hệ thống</Text>
                </div>
                <Menu
                    mode="inline"
                    selectedKeys={[selectedKey]}
                    items={[
                        {
                            key: 'dashboard',
                            icon: <BarChartOutlined />,
                            label: 'Dashboard',
                            onClick: () => nav('/admin/dashboard'),
                        },
                        {
                            key: 'users',
                            icon: <TeamOutlined />,
                            label: 'Users',
                            onClick: () => nav('/admin/users'),
                        },
                        {
                            key: 'workspaces',
                            icon: <AppstoreOutlined />,
                            label: 'Workspaces',
                            onClick: () => nav('/admin/workspaces'),
                        },
                    ]}
                />
            </Sider>
            <Content style={{ padding: 24 }}>
                <Outlet />
            </Content>
        </Layout>
    );
};

export default AdminLayout;
