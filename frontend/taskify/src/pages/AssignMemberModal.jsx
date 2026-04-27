import { Avatar, Button, List, message, Modal, Spin } from "antd";
import { useEffect, useState } from "react";
import cookies from "react-cookies"
import { authApis, endpoints } from "../utils/Apis";
import { UserOutlined } from "@ant-design/icons";

const AssignMemberModal = ({ open, onClose, workspaceId, cardId, onAssignSuccess }) => {
    const [members, setMembers] = useState([]);
    const [loading, setLoading] = useState(false);

    const handleAssignUser = async (userId) => {
        try {
            setLoading(true);
            const token = cookies.load('token');
            await authApis(token).post(endpoints['assign-card'](cardId), { userId });
            message.success("Giao việc thành công");
            if (onAssignSuccess) {
                onAssignSuccess();
            }
        } catch (error) {
            console.log(error);
            message.error("Người dùng đã tồn tại, không thể thêm");
        } finally {
            setLoading(false);
        }
    }
    const loadMemberWorkspace = async () => {
        try {
            setLoading(true);
            const token = cookies.load('token');
            let res = await authApis(token).get(endpoints['workspace-member'](workspaceId));
            setMembers(res.data);
        } catch (error) {
            console.log(error);
        } finally {
            setLoading(false);
        }
    }
    useEffect(() => {
        if (open && workspaceId) {
            loadMemberWorkspace();
        } else {
            setMembers([]);
        }
    }, [open, workspaceId]);

    return (
        <Modal
            title="Thêm thành viên vào thẻ"
            open={open}
            onCancel={onClose}
            footer={null}
            width={400}
        >
            <Spin spinning={loading}>
                <List
                    itemLayout="horizontal"
                    dataSource={members}
                    rowKey={(member) => member.id}
                    renderItem={(member) => (
                        <List.Item
                            actions={[
                                <Button
                                    size="small"
                                    type="primary"
                                    onClick={() => { handleAssignUser(member.id) }}
                                >
                                    Thêm
                                </Button>
                            ]}
                        >
                            <List.Item.Meta
                                avatar={<Avatar src={member.avatar} icon={!member.avatar && <UserOutlined />} />}
                                title={member.username}
                                description={member.email}
                            />
                        </List.Item>
                    )}
                />
            </Spin>
        </Modal>
    )
}

export default AssignMemberModal;