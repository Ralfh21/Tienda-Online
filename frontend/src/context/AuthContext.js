import React, { createContext, useState, useContext, useEffect } from 'react';
import axios from 'axios';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Verificar si hay un usuario guardado en localStorage
    const storedUser = localStorage.getItem('user');
    const token = localStorage.getItem('token');

    if (storedUser && token) {
      setUser(JSON.parse(storedUser));
      // Configurar el token en axios
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    }
    setLoading(false);
  }, []);

  const login = async (email, password) => {
    try {
      const response = await axios.post('http://localhost:8080/api/auth/login', {
        email,
        password
      });

      const { token, ...userData } = response.data;

      // Guardar en localStorage
      localStorage.setItem('token', token);
      localStorage.setItem('user', JSON.stringify(userData));

      // Configurar el token en axios
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;

      setUser(userData);
      return { success: true, data: response.data };
    } catch (error) {
      console.error('Error en login:', error);
      return {
        success: false,
        error: error.response?.data?.error || 'Error al iniciar sesión'
      };
    }
  };

  const register = async (nombre, email, password, rol = 'ROLE_USER') => {
    try {
      const response = await axios.post('http://localhost:8080/api/auth/register', {
        nombre,
        email,
        password,
        rol
      });

      return { success: true, data: response.data };
    } catch (error) {
      console.error('Error en registro:', error);
      return {
        success: false,
        error: error.response?.data?.error || 'Error al registrar usuario'
      };
    }
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    delete axios.defaults.headers.common['Authorization'];
    setUser(null);
  };

  const isAdmin = () => {
    return user?.roles?.includes('ROLE_ADMIN');
  };

  const isCliente = () => {
    return user?.roles?.includes('ROLE_USER');
  };

  const value = {
    user,
    login,
    register,
    logout,
    isAdmin,
    isCliente,
    loading
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth debe ser usado dentro de un AuthProvider');
  }
  return context;
};

