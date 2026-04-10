import axios from 'axios'

export let endpoints = {
    'login': '/login',
    'register': '/register',
    'refreshToken': '/refresh-token',
    // card api
    'cards': listId => `/api/lists/${listId}/cards`,
    'card-detail': cardId => `api/cards/${cardId}`,
    'assign-card': cardId => `api/cards/${cardId}/assign`,
    'move-card': cardId => `api/cards/${cardId}/move`,
    // list api
    'lists': boardId => `/api/boards/${boardId}/lists`,
    'list-detail': listId => `api/lists/${listId}`,
    // workspace api
    'workspaces': userId => `/api/workspace/owner/${userId}`
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

    //   instance.interceptors.response.use(
    //     (response) => response,
    //     async (error) => {
    //       const originalRequest = error.config;

    //       if (error.response && error.response.status === 401 && !originalRequest._retry) {
    //         originalRequest._retry = true;
    //         const newToken = await refreshAccessToken();

    //         if (newToken) {
    //           originalRequest.headers.Authorization = `Bearer ${newToken}`;
    //           return instance(originalRequest);
    //         }
    //       }

    //       return Promise.reject(error);
    //     }
    //   );

    return instance;
};