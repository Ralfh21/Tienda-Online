import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { CartProvider } from './context/CartContext';
import { AuthProvider, useAuth } from './context/AuthContext';
import Navigation from './components/Navigation';
import Home from './pages/Home';
import ProductList from './pages/ProductList';
import ProductDetail from './pages/ProductDetail';
import AdminPanel from './pages/AdminPanel';
import Login from './pages/Login';
import Cart from './pages/Cart';
import Footer from './components/Footer';
import './App.css';

// 🔒 Componente para proteger rutas de admin
const ProtectedAdminRoute = ({ children }) => {
    const { user, isAdmin, loading } = useAuth();

    if (loading) {
        return <div className="text-center mt-5">Cargando...</div>;
    }

    if (!user || !isAdmin()) {
        return <Navigate to="/login" replace />;
    }

    return children;
};

function AppContent() {
    return (
        <div className="App">
            <Navigation />
            <main className="container-fluid py-4">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/productos" element={<ProductList />} />
          <Route path="/producto/:id" element={<ProductDetail />} />
          <Route path="/login" element={<Login />} />
          <Route path="/carrito" element={<Cart />} />
          <Route
            path="/admin"
            element={
              <ProtectedAdminRoute>
                <AdminPanel />
              </ProtectedAdminRoute>
            }
          />
        </Routes>
            </main>
            <Footer />
        </div>
    );
}

function App() {
    return (
        <AuthProvider>
            <CartProvider>
                <Router>
                    <AppContent />
                </Router>
            </CartProvider>
        </AuthProvider>
    );
}

export default App;
