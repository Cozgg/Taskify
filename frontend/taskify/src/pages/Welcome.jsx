import React from 'react';
import { Link } from 'react-router-dom';
import { Button } from 'antd';
import AppHeader from '../components/AppHeader';
import './Welcome.css';

const Welcome = () => {
    return (
        <div className="welcome-container">
            <AppHeader />

            <main className="welcome-main">
                <div className="hero-section">
                    <div className="hero-badge">HỢP TÁC KHÔNG GIỚI HẠN</div>
                    <h1 className="hero-title">
                        KanbanFlow giúp các nhóm thúc đẩy <br />
                        <span className="text-highlight">công việc tiến lên.</span>
                    </h1>
                    <p className="hero-subtitle">
                        Tập hợp các nhiệm vụ, thành viên và công cụ của bạn lại một nơi. Giúp mọi
                        việc luôn có tổ chức và tiến triển đồng bộ, ngay cả khi nhóm của bạn làm
                        việc từ xa.
                    </p>
                    <div className="hero-actions">
                        <Link to="/register">
                            <Button type="primary" className="explore-btn">Khám phá ngay</Button>
                        </Link>
                    </div>
                </div>
            </main>
        </div>
    );
};

export default Welcome;
