import React, { useState, useEffect } from 'react';
import cookies from 'react-cookies';
import { authApis, endpoints } from '../utils/Apis';
import { Spin, Empty } from 'antd';
import { useParams } from 'react-router-dom';
import {
    Chart as ChartJS, CategoryScale, LinearScale, BarElement,
    Title, Tooltip, Legend, ArcElement
} from 'chart.js';
import { Bar, Doughnut } from 'react-chartjs-2';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, ArcElement);

export const BoardStatistics = () => {
    const { boardId } = useParams();
    const [loading, setLoading] = useState(false);
    const [boardChartData, setBoardChartData] = useState(null);
    const [memberChartData, setMemberChartData] = useState(null);

    const getColorForStatus = (index) => {
        const palette = [
            "#0079bf",
            "#ff9f1a",
            "#61bd4f",
            "#eb5a46",
            "#c377e0",
            "#00c2e0",
            "#f2d600",
            "#51e898",
            "#ff78cb",
            "#344563"
        ];

        return palette[index % palette.length];
    };

    useEffect(() => {
        if (boardId) fetchStatistics();
    }, [boardId]);

    const fetchStatistics = async () => {
        setLoading(true);
        try {
            const token = cookies.load('token');
            const [boardRes, memberRes] = await Promise.all([
                authApis(token).get(endpoints['stat-board'](boardId)),
                authApis(token).get(endpoints['stat-member'](boardId))
            ]);

            const bData = boardRes.data?.data || boardRes.data;
            const mData = memberRes.data?.data || memberRes.data;

            let dynamicStatuses = [];

            if (Array.isArray(bData)) {
                dynamicStatuses = bData.map(item => item[0]);
                processBoardData(bData, dynamicStatuses);
            }

            if (Array.isArray(mData)) {
                processMemberData(mData, dynamicStatuses);
            }

        } catch (error) {
            console.error("Lỗi tải thống kê:", error);
        } finally {
            setLoading(false);
        }
    };

    const processBoardData = (data, dynamicStatuses) => {
        setBoardChartData({
            labels: dynamicStatuses,
            datasets: [{
                data: data.map(item => item[1]),
                backgroundColor: dynamicStatuses.map((status, i) => getColorForStatus(i)),
                hoverOffset: 4
            }]
        });
    };

    const processMemberData = (data, dynamicStatuses) => {
        const users = Array.from(new Set(data.map(item => item[0])));

        const datasets = dynamicStatuses.map((status, index) => ({
            label: status,
            data: users.map(user => {
                const record = data.find(item => item[0] === user && item[1] === status);
                return record ? record[2] : 0;
            }),
            backgroundColor: getColorForStatus(index),
        }));

        setMemberChartData({ labels: users, datasets });
    };

    return (
        <Spin spinning={loading}>
            <div style={{
                maxHeight: '70vh',
                overflowY: 'auto',
                padding: '10px'
            }}>
                <div style={{ marginBottom: '40px' }}>
                    <h3 style={{ textAlign: 'center', marginBottom: '20px' }}>Tỷ lệ trạng thái công việc</h3>
                    <div style={{ height: '300px', position: 'relative' }}>
                        {boardChartData ? (
                            <Doughnut
                                data={boardChartData}
                                options={{ maintainAspectRatio: false }}
                            />
                        ) : <Empty description="Chưa có dữ liệu bảng" />}
                    </div>
                </div>

                <div style={{ borderTop: '1px solid #f0f0f0', paddingTop: '20px' }}>
                    <h3 style={{ textAlign: 'center', marginBottom: '20px' }}>Tiến độ thành viên (Số lượng thẻ)</h3>
                    <div style={{ height: '350px', position: 'relative' }}>
                        {memberChartData ? (
                            <Bar
                                data={memberChartData}
                                options={{
                                    maintainAspectRatio: false,
                                    scales: {
                                        x: { stacked: true },
                                        y: { stacked: true, beginAtZero: true, ticks: { stepSize: 1 } }
                                    },
                                    plugins: { legend: { position: 'bottom' } }
                                }}
                            />
                        ) : <Empty description="Chưa có dữ liệu thành viên" />}
                    </div>
                </div>
            </div>
        </Spin>
    );
};