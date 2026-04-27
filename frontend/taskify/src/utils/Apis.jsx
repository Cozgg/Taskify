import axios from 'axios'
import cookies from 'react-cookies'

export let endpoints = {
    'login': '/api/auth/login',
    'register': '/api/auth/register',
    'refreshToken': '/api/auth/refresh-token',

    // admin api
    'admin-dashboard': '/api/admin/dashboard',
    'admin-users': '/api/admin/users',
    'admin-user-detail': (id) => `/api/admin/users/${id}`,
    'admin-workspaces': '/api/admin/workspaces',
    'admin-workspace-detail': (id) => `/api/admin/workspaces/${id}`,
    'admin-workspace-users': (id) => `/api/admin/workspaces/${id}/users`,

    // card api
    'cards': listId => `/api/lists/${listId}/cards`,
    'create-card': listId => `/api/lists/${listId}/cards`,
    'card-detail': cardId => `/api/cards/${cardId}`,
    'update-card': cardId => `/api/cards/${cardId}`,
    'delete-card': cardId => `/api/cards/${cardId}`,
    'assign-card': cardId => `/api/cards/${cardId}/assign`,
    'unassign-card': cardId => `/api/cards/${cardId}/unassign`,
    'move-card': cardId => `/api/cards/${cardId}/move`,
    'card-attachments': cardId => `/api/cards/${cardId}/attachments`,
    'attach-file': cardId => `/api/cards/${cardId}/attach`,
    'delete-attachment': attachmentId => `/api/attachments/${attachmentId}`,
    'add-comment': cardId => `/api/cards/${cardId}/comments`,
    'delete-comment': commentId => `/api/comments/${commentId}`,
    'comments': cardId => `/api/cards/${cardId}/comments`,
    'card-members': cardId => `/api/cards/${cardId}/members`,
    // list api
    'create-list': boardId => `/api/boards/${boardId}/lists`,
    'lists': boardId => `/api/boards/${boardId}/lists`,
    'list-detail': listId => `/api/lists/${listId}`,
    'update-list': listId => `/api/lists/${listId}`,
    'delete-list': listId => `/api/lists/${listId}`,
    // workspace api
    'create-workspace': '/api/workspaces',
    'workspaces': '/api/workspaces/owner',
    'workspace-detail': workspaceId => `/api/workspaces/${workspaceId}`,
    'workspace-member': workspaceId => `/api/workspaces/${workspaceId}/members`,
    'workspace-board': workspaceId => `/api/workspaces/${workspaceId}/boards`,

    'create-board': workspaceId => `/api/workspaces/${workspaceId}/boards`,
    'board-detail': boardId => `/api/boards/${boardId}`,
    'update-board': boardId => `/api/boards/${boardId}`,
    'delete-board': boardId => `/api/boards/${boardId}`,
    'invite-member': workspaceId => `/api/workspaces/${workspaceId}/users`,

    //stat api
    'stat-board': boardId => `/api/stat-board-progress/${boardId}`,
    'stat-member': boardId => `/api/stat-member-progress/${boardId}`
}



export default axios.create({
    baseURL: 'http://localhost:8080',
})

export const authApis = (token) => {
    const instance = axios.create({
        baseURL: 'http://localhost:8080',
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });

    instance.interceptors.response.use(
        (response) => response,
        async (error) => {
            const originalRequest = error.config;

            if (error.response && error.response.status === 401 && !originalRequest._retry) {
                originalRequest._retry = true;

                try {
                    const refreshToken = cookies.load('refreshToken');
                    if (refreshToken) {
                        const res = await axios.post('http://localhost:8080/api/auth/refresh-token', { refreshToken });
                        if (res.status === 200 && res.data.data.accessToken) {
                            const newToken = res.data.data.accessToken;
                            cookies.save('token', newToken);
                            originalRequest.headers.Authorization = `Bearer ${newToken}`;
                            return instance(originalRequest);
                        }
                    }
                } catch (refreshErr) {
                    console.error("Refresh token expired or failed", refreshErr);
                    cookies.remove('token');
                    cookies.remove('refreshToken');
                    cookies.remove('userdata');
                    window.location.href = '/login';
                }
            }

            return Promise.reject(error);
        }
    );

    return instance;
};

// Board API functions
export const updateBoard = async (token, boardId, boardData) => {
    const api = authApis(token);
    return api.put(endpoints['update-board'](boardId), boardData);
};

export const deleteBoard = async (token, boardId) => {
    const api = authApis(token);
    return api.delete(endpoints['delete-board'](boardId));
};

// List API functions
export const updateList = async (token, listId, listData) => {
    const api = authApis(token);
    return api.put(endpoints['update-list'](listId), listData);
};

export const deleteList = async (token, listId) => {
    const api = authApis(token);
    return api.delete(endpoints['delete-list'](listId));
};

// Card API functions
export const createCard = async (token, listId, cardData) => {
    const api = authApis(token);
    return api.post(endpoints['create-card'](listId), cardData);
};

export const updateCard = async (token, cardId, cardData) => {
    const api = authApis(token);
    return api.put(endpoints['update-card'](cardId), cardData);
};

export const deleteCard = async (token, cardId) => {
    const api = authApis(token);
    return api.delete(endpoints['delete-card'](cardId));
};
