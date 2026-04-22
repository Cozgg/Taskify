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
    'card-detail': cardId => `/api/cards/${cardId}`,
    'assign-card': cardId => `/api/cards/${cardId}/assign`,
    'move-card': cardId => `/api/cards/${cardId}/move`,
    // list api
    'lists': boardId => `/api/boards/${boardId}/lists`,
    'list-detail': listId => `/api/lists/${listId}`,
    // workspace api
    'create-workspace': '/api/workspace',
    'workspaces': userId => `/api/workspace/owner/${userId}`,
    'workspace-detail': workspaceId => `/api/workspace/${workspaceId}`,
    'workspace-member': workspaceId => `/api/workspace/${workspaceId}/members`,
    'workspace-board': workspaceId => `/api/workspaces/${workspaceId}/boards`,
    'invite-member': workspaceId => `/api/workspace/${workspaceId}/users`,
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
