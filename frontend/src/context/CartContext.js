import React, { createContext, useContext, useState, useEffect } from 'react';

const CartContext = createContext();

export const useCart = () => {
  const context = useContext(CartContext);
  if (!context) {
    throw new Error('useCart debe ser usado dentro de un CartProvider');
  }
  return context;
};

export const CartProvider = ({ children }) => {
  // Función para obtener el ID del usuario actual
  const getCurrentUserId = () => {
    const user = localStorage.getItem('user');
    if (user) {
      const userData = JSON.parse(user);
      return userData.userId || userData.clienteId || 'guest';
    }
    return 'guest';
  };

  const [currentUserId, setCurrentUserId] = useState(getCurrentUserId());

  const [items, setItems] = useState(() => {
    // Cargar carrito específico del usuario desde localStorage
    const userId = getCurrentUserId();
    const savedCart = localStorage.getItem(`carrito_${userId}`);
    return savedCart ? JSON.parse(savedCart) : [];
  });

  // Verificar cambios en el usuario y resetear carrito si cambió
  useEffect(() => {
    const checkUserChange = () => {
      const newUserId = getCurrentUserId();
      if (newUserId !== currentUserId) {
        setCurrentUserId(newUserId);
        // Cargar carrito del nuevo usuario
        const savedCart = localStorage.getItem(`carrito_${newUserId}`);
        setItems(savedCart ? JSON.parse(savedCart) : []);
      }
    };

    // Verificar cada segundo si cambió el usuario
    const interval = setInterval(checkUserChange, 1000);

    // También verificar inmediatamente
    checkUserChange();

    return () => clearInterval(interval);
  }, [currentUserId]);

  // Guardar en localStorage cada vez que cambie el carrito
  useEffect(() => {
    const userId = getCurrentUserId();
    if (items.length > 0) {
      localStorage.setItem(`carrito_${userId}`, JSON.stringify(items));
    } else {
      // Si el carrito está vacío, eliminar del localStorage
      localStorage.removeItem(`carrito_${userId}`);
    }
  }, [items]);

  const addToCart = (producto, cantidad = 1) => {
    setItems(currentItems => {
      const existingItem = currentItems.find(item => item.id === producto.id);
      
      if (existingItem) {
        // Si ya existe, incrementar cantidad
        return currentItems.map(item =>
          item.id === producto.id
            ? { ...item, cantidad: item.cantidad + cantidad }
            : item
        );
      } else {
        // Si no existe, agregar nuevo item
        return [...currentItems, { ...producto, cantidad }];
      }
    });
  };

  const removeFromCart = (productoId) => {
    setItems(currentItems => currentItems.filter(item => item.id !== productoId));
  };

  const updateQuantity = (productoId, cantidad) => {
    if (cantidad <= 0) {
      removeFromCart(productoId);
      return;
    }
    
    setItems(currentItems =>
      currentItems.map(item =>
        item.id === productoId ? { ...item, cantidad } : item
      )
    );
  };

  const clearCart = () => {
    setItems([]);
  };

  const getItemCount = () => {
    return items.reduce((total, item) => total + item.cantidad, 0);
  };

  const getTotal = () => {
    return items.reduce((total, item) => total + (item.precio * item.cantidad), 0);
  };

  const value = {
    items,
    addToCart,
    removeFromCart,
    updateQuantity,
    clearCart,
    getItemCount,
    getTotal
  };

  return (
    <CartContext.Provider value={value}>
      {children}
    </CartContext.Provider>
  );
};