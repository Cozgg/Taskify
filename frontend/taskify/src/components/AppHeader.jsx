import React, { useContext } from 'react';
import { Link } from "react-router-dom";
import { Button, Layout } from 'antd';
import { MyContext } from '../utils/context/MyContext';
import Logout from '../pages/Logout';
const { Header } = Layout;

const AppHeader = () => {
    const [user] = useContext(MyContext);
    const logout = Logout();

    return (
        <Header className="kanban-header" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', background: '#fff', padding: '0 24px', borderBottom: '1px solid #DFE1E6' }}>
            <div className="brand" style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                <span className="logo-icon" style={{ fontSize: '24px' }}>📊</span>
                <h2 style={{ margin: 0, color: '#0052CC', fontWeight: 'bold' }}>KanbanFlow</h2>
            </div>
            <div>
                {user ? (
                    <Button onClick={logout} type="primary" danger>Đăng xuất</Button>
                ) : (
                    <div className="header-actions" style={{ display: 'flex', gap: '10px' }}>
                        <Link to="/login"><Button type="text">Đăng nhập</Button></Link>
                        <Link to="/register"><Button type="primary" style={{ backgroundColor: '#0052CC' }}>Đăng ký</Button></Link>
                    </div>
                )}
            </div>
        </Header>
    );
};

export default AppHeader;