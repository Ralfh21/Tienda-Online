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

    // --- 1) FUNCIÓN PARA SABER QUIÉN ES EL USUARIO ---
    const getCurrentUser = () => {
        const user = localStorage.getItem('user');
        return user ? JSON.parse(user) : null;
    };

    const getCurrentUserId = () => {
        const u = getCurrentUser();
        return u?.userId || u?.clienteId || 'guest';
    };

    // --- 2) DETECTAR SI ES ADMIN ---
    const userIsAdmin = () => {
        const u = getCurrentUser();
        return u?.roles?.includes("ROLE_ADMIN");
    };

    const [currentUserId, setCurrentUserId] = useState(getCurrentUserId());

    // --- 3) CARGAR CARRITO SOLO SI NO ES ADMIN ---
    const [items, setItems] = useState(() => {
        if (userIsAdmin()) return []; // Admin no tiene carrito

        const userId = getCurrentUserId();
        const savedCart = localStorage.getItem(`carrito_${userId}`);
        return savedCart ? JSON.parse(savedCart) : [];
    });

    // --- 4) DETECTAR CUANDO EL USUARIO CAMBIA Y RECARGAR SU CARRITO ---
    useEffect(() => {
        const checkUserChange = () => {
            const newUserId = getCurrentUserId();

            if (newUserId !== currentUserId) {
                setCurrentUserId(newUserId);

                if (userIsAdmin()) {
                    setItems([]); // Admin → carrito vacío siempre
                } else {
                    const savedCart = localStorage.getItem(`carrito_${newUserId}`);
                    setItems(savedCart ? JSON.parse(savedCart) : []);
                }
            }
        };

        const interval = setInterval(checkUserChange, 1000);

        checkUserChange();

        return () => clearInterval(interval);
    }, [currentUserId]);

    // --- 5) GUARDAR CARRITO SOLO SI NO ES ADMIN ---
    useEffect(() => {
        if (userIsAdmin()) return;

        const userId = getCurrentUserId();

        if (items.length > 0) {
            localStorage.setItem(`carrito_${userId}`, JSON.stringify(items));
        } else {
            localStorage.removeItem(`carrito_${userId}`);
        }
    }, [items]);

    // --- 6) FUNCIONES DEL CARRITO ---
    const addToCart = (producto, cantidad = 1) => {
        if (userIsAdmin()) return; // Admin no puede agregar

        setItems(currentItems => {
            const existingItem = currentItems.find(item => item.id === producto.id);

            if (existingItem) {
                return currentItems.map(item =>
                    item.id === producto.id
                        ? { ...item, cantidad: item.cantidad + cantidad }
                        : item
                );
            } else {
                return [...currentItems, { ...producto, cantidad }];
            }
        });
    };

    const removeFromCart = (productoId) => {
        if (userIsAdmin()) return;

        setItems(currentItems =>
            currentItems.filter(item => item.id !== productoId)
        );
    };

    const updateQuantity = (productoId, cantidad) => {
        if (userIsAdmin()) return;

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
        if (userIsAdmin()) return;
        setItems([]);
    };

    const getItemCount = () => {
        return userIsAdmin() ? 0 : items.reduce((total, item) => total + item.cantidad, 0);
    };

    const getTotal = () => {
        return userIsAdmin() ? 0 : items.reduce((total, item) => total + (item.precio * item.cantidad), 0);
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
