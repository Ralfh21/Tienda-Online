import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import '../styles/Login.css';

const Login = () => {
    const [isLogin, setIsLogin] = useState(true);
    const [formData, setFormData] = useState({
        nombre: '',
        email: '',
        password: '',
        confirmPassword: ''
    });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);

    const { login, register } = useAuth();
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
        setError('');
    };

    const validarNombre = (nombre) => {
        // Solo letras y espacios
        const regex = /^[A-Za-zÀ-ÿ\s]+$/;
        return regex.test(nombre);
    };

    const validarEmail = (email) => {
        // Validación básica de email
        const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return regex.test(email);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        if (!isLogin) {
            // Validación de nombre
            if (!validarNombre(formData.nombre)) {
                setError('El nombre solo puede contener letras y espacios');
                setLoading(false);
                return;
            }

            // Validación de email
            if (!validarEmail(formData.email)) {
                setError('Por favor, introduce un email válido.');
                setLoading(false);
                return;
            }

            // Validación de contraseñas
            if (formData.password !== formData.confirmPassword) {
                setError('Las contraseñas no coinciden');
                setLoading(false);
                return;
            }

            if (formData.password.length < 6) {
                setError('La contraseña debe tener al menos 6 caracteres');
                setLoading(false);
                return;
            }

            // Registro
            const result = await register(formData.nombre, formData.email, formData.password);
            if (result.success) {
                setError('');
                alert('Usuario registrado correctamente. Por favor, inicia sesión.');
                setIsLogin(true);
                setFormData({ nombre: '', email: '', password: '', confirmPassword: '' });
            } else {
                setError(result.error);
            }

        } else {
            // Login
            if (!validarEmail(formData.email)) {
                setError('Por favor, introduce un email válido.');
                setLoading(false);
                return;
            }

            const result = await login(formData.email, formData.password);
            if (result.success) {
                if (result.data.roles.includes('ROLE_ADMIN')) {
                    navigate('/admin');
                } else {
                    navigate('/');
                }
            } else {
                setError(result.error);
            }
        }

        setLoading(false);
    };

    return (
        <div className="login-container">
            <div className="login-card">
                <h2>{isLogin ? 'Iniciar Sesión' : 'Registrarse'}</h2>

                {error && <div className="error-message">{error}</div>}

                <form onSubmit={handleSubmit}>
                    {!isLogin && (
                        <div className="form-group">
                            <label htmlFor="nombre">Nombre Completo</label>
                            <input
                                type="text"
                                id="nombre"
                                name="nombre"
                                value={formData.nombre}
                                onChange={handleChange}
                                required
                                placeholder="Juan Pérez"
                            />
                        </div>
                    )}

                    <div className="form-group">
                        <label htmlFor="email">Email</label>
                        <input
                            type="email"
                            id="email"
                            name="email"
                            value={formData.email}
                            onChange={handleChange}
                            required
                            placeholder="ejemplo@correo.com"
                        />
                    </div>

                    <div className="form-group password-group">
                        <label htmlFor="password">Contraseña</label>
                        <div className="password-wrapper">
                            <input
                                type={showPassword ? 'text' : 'password'}
                                id="password"
                                name="password"
                                value={formData.password}
                                onChange={handleChange}
                                required
                                placeholder="••••••••"
                            />
                            <button
                                type="button"
                                className="show-password-btn"
                                onClick={() => setShowPassword(!showPassword)}
                            >
                                {showPassword ? 'Ocultar' : 'Mostrar'}
                            </button>
                        </div>
                    </div>

                    {!isLogin && (
                        <div className="form-group password-group">
                            <label htmlFor="confirmPassword">Confirmar Contraseña</label>
                            <div className="password-wrapper">
                                <input
                                    type={showConfirmPassword ? 'text' : 'password'}
                                    id="confirmPassword"
                                    name="confirmPassword"
                                    value={formData.confirmPassword}
                                    onChange={handleChange}
                                    required
                                    placeholder="••••••••"
                                />
                                <button
                                    type="button"
                                    className="show-password-btn"
                                    onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                                >
                                    {showConfirmPassword ? 'Ocultar' : 'Mostrar'}
                                </button>
                            </div>
                        </div>
                    )}

                    <button type="submit" className="btn-submit" disabled={loading}>
                        {loading ? 'Cargando...' : (isLogin ? 'Iniciar Sesión' : 'Registrarse')}
                    </button>
                </form>

                <div className="toggle-form">
                    <p>
                        {isLogin ? '¿No tienes cuenta? ' : '¿Ya tienes cuenta? '}
                        <button
                            type="button"
                            onClick={() => {
                                setIsLogin(!isLogin);
                                setError('');
                                setFormData({ nombre: '', email: '', password: '', confirmPassword: '' });
                            }}
                            className="link-button"
                        >
                            {isLogin ? 'Regístrate' : 'Inicia Sesión'}
                        </button>
                    </p>
                </div>
            </div>
        </div>
    );
};

export default Login;