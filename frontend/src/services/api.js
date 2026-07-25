import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || '/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: { 'Content-Type': 'application/json' },
});

// Request interceptor - attach JWT token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor - handle auth errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// ---- Auth APIs ----
export const authAPI = {
  login: (data) => api.post('/auth/login', data),
  register: (data) => api.post('/auth/register', data),
  forgotPassword: (email) => api.post('/auth/forgot-password', { email }),
  resetPassword: (data) => api.post('/auth/reset-password', data),
  refreshToken: (token) => api.post(`/auth/refresh-token?refreshToken=${token}`),
};

// ---- Farmer APIs ----
export const farmerAPI = {
  getProfile: () => api.get('/farmer/profile'),
  updateProfile: (data) => api.put('/farmer/profile', data),
  getAllFarmers: (page = 0, size = 10) => api.get(`/farmer/all?page=${page}&size=${size}`),
};

// ---- Scheme APIs ----
export const schemeAPI = {
  getAllSchemes: (page = 0, size = 10) => api.get(`/schemes/list?page=${page}&size=${size}`),
  getPublicSchemes: (page = 0, size = 6) => api.get(`/schemes/public/list?page=${page}&size=${size}`),
  searchSchemes: (keyword, page = 0) => api.get(`/schemes/search?keyword=${keyword}&page=${page}`),
  getSchemeById: (id) => api.get(`/schemes/${id}`),
  getSchemeByCode: (code) => api.get(`/schemes/code/${code}`),
  getSchemesByState: (state) => api.get(`/schemes/by-state/${state}`),
  createScheme: (data) => api.post('/schemes', data),
  updateScheme: (id, data) => api.put(`/schemes/${id}`, data),
  deleteScheme: (id) => api.delete(`/schemes/${id}`),
};

// ---- Eligibility APIs ----
export const eligibilityAPI = {
  check: (data) => api.post('/eligibility/check', data),
};

// ---- Application APIs ----
export const applicationAPI = {
  apply: (data) => api.post('/applications/apply', data),
  getMyApplications: (page = 0, size = 10) => api.get(`/applications/my?page=${page}&size=${size}`),
  getById: (id) => api.get(`/applications/${id}`),
  uploadDocument: (id, documentType, file) => {
    const formData = new FormData();
    formData.append('documentType', documentType);
    formData.append('file', file);
    return api.post(`/applications/${id}/documents`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  },
  updateStatus: (id, status, adminRemarks) =>
    api.put(`/applications/${id}/status?status=${status}&adminRemarks=${adminRemarks || ''}`),
  getAllApplications: (page = 0, size = 10) => api.get(`/applications/all?page=${page}&size=${size}`),
};

// ---- Dashboard APIs ----
export const dashboardAPI = {
  getFarmerDashboard: () => api.get('/dashboard/farmer'),
  getAdminDashboard: () => api.get('/dashboard/admin'),
};

// ---- Notification APIs ----
export const notificationAPI = {
  getMyNotifications: (page = 0, size = 10) => api.get(`/notifications/my?page=${page}&size=${size}`),
  getUnreadCount: () => api.get('/notifications/unread-count'),
  markAsRead: (id) => api.put(`/notifications/${id}/read`),
  broadcast: (title, message) => api.post(`/notifications/broadcast?title=${title}&message=${message}`),
};

// ---- Admin APIs ----
export const adminAPI = {
  getDashboard: () => api.get('/admin/dashboard'),
  getAllFarmers: (page = 0, size = 10) => api.get(`/admin/farmers?page=${page}&size=${size}`),
  getFarmer: (id) => api.get(`/admin/farmers/${id}`),
  getAllApplications: (page = 0, size = 10) => api.get(`/admin/applications?page=${page}&size=${size}`),
  createScheme: (data) => api.post('/admin/schemes', data),
  updateScheme: (id, data) => api.put(`/admin/schemes/${id}`, data),
  deleteScheme: (id) => api.delete(`/admin/schemes/${id}`),
  broadcast: (title, message) => api.post(`/admin/notifications/broadcast?title=${title}&message=${message}`),
};

// ---- Profile APIs ----
export const profileAPI = {
  updateName: (fullName) => api.put(`/profile/update-name?fullName=${encodeURIComponent(fullName)}`),
  updatePhone: (phone) => api.put(`/profile/update-phone?phone=${phone}`),
  uploadPicture: (file) => {
    const formData = new FormData();
    formData.append('file', file);
    return api.post('/profile/upload-picture', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  },
};

export default api;
