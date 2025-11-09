import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { CartProvider } from './context/CartContext';
import Navigation from './components/Navigation';
import Home from './pages/Home';
import ProductList from './pages/ProductList';
import ProductDetail from './pages/ProductDetail';
import AdminPanel from './pages/AdminPanel';
import Footer from './components/Footer';
import './App.css';

function App() {
  return (
    <CartProvider>
      <Router>
        <div className="App">
          <Navigation />
          <main className="container-fluid py-4">
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/productos" element={<ProductList />} />
              <Route path="/producto/:id" element={<ProductDetail />} />
              <Route path="/admin" element={<AdminPanel />} />
            </Routes>
          </main>
          <Footer />
        </div>
      </Router>
    </CartProvider>
  );
}

export default App;
