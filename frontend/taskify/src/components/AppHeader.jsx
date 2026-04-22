import React, { useContext } from 'react';
import { Link } from 'react-router-dom';
import { Button } from 'antd';
import { MyContext } from '../utils/context/MyContext';
import Logout from '../pages/Logout';
import './AppHeader.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTrello } from '@fortawesome/free-brands-svg-icons';

const AppHeader = () => {
    const [user] = useContext(MyContext);
    const logout = Logout();

    const role = user?.userdata?.data?.role;
    const isAdmin = role === 'ADMIN';

    return (
        <header className="app-header">
            <div className="header-left">
                <Link to={user ? '/home' : '/'} className="header-brand">
                    <FontAwesomeIcon icon={faTrello} flip="vertical" size="lg" style={{ color: "#0052cc", }} />
                    <h2 className="brand-name">KanbanFlow</h2>
                </Link>
            </div>

            <div className="header-right">
                {user ? (
                    <div className="header-user">
                        {isAdmin && (
                            <Link to="/admin/dashboard" className="header-signin-link">
                                Admin
                            </Link>
                        )}
                        <span className="header-username">
                            👋 {user.userdata?.data?.username || 'Người dùng'}
                        </span>
                        <Button
                            onClick={logout}
                            danger
                            className="header-logout-btn"
                        >
                            Đăng xuất
                        </Button>
                    </div>
                ) : (
                    <>
                        <Link to="/login" className="header-signin-link">Sign In</Link>
                        <Link to="/register">
                            <Button type="primary" className="header-signup-btn">Sign Up</Button>
                        </Link>
                    </>
                )}
            </div>
        </header>
    );
};

export default AppHeader;