import {
  BrowserRouter,
  Routes,
  Link,
  Route,
  Navigate,
  Outlet,
} from "react-router-dom";
import "./App.css";
import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import {useContext, useReducer} from "react";
import {MyContext} from "./utils/context/MyContext";
import MyUserReducer from "./utils/reducers/MyUserReducer";
import {Layout} from "antd";
import AppHeader from "./components/AppHeader";
import {Content, Footer} from "antd/es/layout/layout";
import WorkspaceDetail from "./pages/WorkspaceDetail";
import BoardDetail from "./pages/BoardDetail";

function App() {
  const [user, dispatch] = useReducer(MyUserReducer, null);
  return (
    <MyContext.Provider value={[user, dispatch]}>
      <AppContent />
    </MyContext.Provider>
  );
}

const MainLayout = () => {
  return (
    <Layout style={{minHeight: "100vh"}}>
      <AppHeader />

      <Content style={{background: "#F4F5F7"}}>
        <Outlet />
      </Content>

      <Footer style={{textAlign: "center", color: "#5E6C84"}}>
        KanbanFlow ©2026
      </Footer>
    </Layout>
  );
};

const AppContent = () => {
  const [user] = useContext(MyContext);

  const isAuthenticated = user !== null && user !== undefined;
  return (
    <BrowserRouter>
      <Routes>
        <Route
          path="/login"
          element={isAuthenticated ? <Navigate to="/" replace /> : <Login />}
        />
        <Route
          path="/register"
          element={isAuthenticated ? <Navigate to="/" replace /> : <Register />}
        />
        <Route element={<MainLayout />}>
          <Route path="/" element={<Home />} />
          <Route
            path="/workspace/:workspaceId"
            element={
              isAuthenticated ? (
                <WorkspaceDetail />
              ) : (
                <Navigate to="/login" replace />
              )
            }
          />
          <Route path="/board/:boardId" element={<BoardDetail />}></Route>
        </Route>

        {/* <Route path="/category" element={<Category />} />
        <Route path="/admin/*" element={<Admin />} /> */}

        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  );
};

export default App;
