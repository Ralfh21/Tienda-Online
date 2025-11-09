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
  const [items, setItems] = useState(() => {
    // Cargar carrito desde localStorage al inicializar
    const savedCart = localStorage.getItem('carrito');
    return savedCart ? JSON.parse(savedCart) : [];
  });

  // Guardar en localStorage cada vez que cambie el carrito
  useEffect(() => {
    localStorage.setItem('carrito', JSON.stringify(items));
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