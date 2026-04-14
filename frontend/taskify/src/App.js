import {
  BrowserRouter,
  Routes,
  Route,
  Navigate,
  Outlet,
} from "react-router-dom";
import "./App.css";
import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Welcome from "./pages/Welcome";
import { useContext, useReducer, useState } from "react";
import cookies from "react-cookies";
import { MyContext } from "./utils/context/MyContext";
import MyUserReducer from "./utils/reducers/MyUserReducer";
import AppHeader from "./components/AppHeader";
import WorkspaceOverview from "./pages/WorkspaceOverview";
import BoardDetail from "./pages/BoardDetail";
import AdminLayout from "./pages/admin/AdminLayout";
import AdminDashboard from "./pages/admin/AdminDashboard";
import AdminUsers from "./pages/admin/AdminUsers";
import AdminWorkspaces from "./pages/admin/AdminWorkspaces";
import AdminWorkspaceDetail from "./pages/admin/AdminWorkspaceDetail";

function App() {
  const storedUser = cookies.load('userdata');
  const [user, dispatch] = useReducer(MyUserReducer, storedUser ? { userdata: storedUser } : null);
  return (
    <MyContext.Provider value={[user, dispatch]}>
      <AppContent />
    </MyContext.Provider>
  );
}

const MainLayout = () => {
  const [refreshKey, setRefreshKey] = useState(0);
  return (
    <div style={{ minHeight: '100vh', background: '#f0f2f9', display: 'flex', flexDirection: 'column' }}>
      <AppHeader onWorkspaceCreated={() => setRefreshKey((k) => k + 1)} />
      <div style={{ flex: 1 }}>
        <Outlet context={{ triggerCreate: refreshKey }} />
      </div>
      <footer style={{ textAlign: 'center', color: '#5E6C84', padding: '16px', fontSize: '13px', borderTop: '1px solid #e8eaf0', background: '#fff' }}>
        KanbanFlow ©2026
      </footer>
    </div>
  );
};

const AppContent = () => {
  const [user] = useContext(MyContext);

  const isAuthenticated = user !== null && user !== undefined;
  const role = user?.userdata?.data?.role;
  const landingPath = role === 'ADMIN' ? '/admin/dashboard' : '/home';
  return (
    <BrowserRouter>
      <Routes>
        <Route
          path="/login"
          element={isAuthenticated ? <Navigate to={landingPath} replace /> : <Login />}
        />
        <Route
          path="/register"
          element={isAuthenticated ? <Navigate to={landingPath} replace /> : <Register />}
        />
        <Route path="/" element={isAuthenticated ? <Navigate to={landingPath} replace /> : <Welcome />} />

        <Route element={<MainLayout />}>
          <Route path="/home" element={isAuthenticated ? <Home /> : <Navigate to="/login" replace />} />
          <Route
            path="/workspace/:workspaceId"
            element={
              isAuthenticated ? (
                <WorkspaceOverview />
              ) : (
                <Navigate to="/login" replace />
              )
            }
          />
          <Route path="/board/:boardId" element={<BoardDetail />}></Route>

          <Route
            path="/admin"
            element={isAuthenticated ? <AdminLayout /> : <Navigate to="/login" replace />}
          >
            <Route index element={<Navigate to="dashboard" replace />} />
            <Route path="dashboard" element={<AdminDashboard />} />
            <Route path="users" element={<AdminUsers />} />
            <Route path="workspaces" element={<AdminWorkspaces />} />
            <Route path="workspaces/:id" element={<AdminWorkspaceDetail />} />
          </Route>
        </Route>

        {/* <Route path="/category" element={<Category />} /> */}

        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  );
};

export default App;
