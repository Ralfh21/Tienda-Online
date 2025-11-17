import React, { useState, useEffect } from "react";
import { Navbar, Nav, Container } from "react-bootstrap";
import { LinkContainer } from "react-router-bootstrap";
import { useNavigate } from "react-router-dom";
import { useCart } from "../context/CartContext";
import { useAuth } from "../context/AuthContext";

function Navigation() {
    const { user, logout, isAdmin } = useAuth();
    const { getItemCount } = useCart();
    const navigate = useNavigate();

    const itemCount = getItemCount();

    const handleLogout = () => {
        logout();
        navigate("/");
    };

    return (
        <Navbar bg="light" className="shadow-sm px-4" expand="lg">
            <Container>

                {/* Logo */}
                <LinkContainer to="/">
                    <Navbar.Brand className="fw-bold fs-4">
                        🛍️ StyleHub
                    </Navbar.Brand>
                </LinkContainer>

                <Navbar.Toggle />

                <Navbar.Collapse className="justify-content-end">

                    <Nav className="align-items-center">

                        {/* 🟣 ADMIN NAV — en una sola fila */}
                        {user && isAdmin() && (
                            <>
                                <span className="me-3 fw-semibold">👤 Administrador</span>

                                <LinkContainer to="/admin">
                                    <Nav.Link className="me-3">Panel Admin</Nav.Link>
                                </LinkContainer>

                                <Nav.Link
                                    className="text-danger fw-semibold"
                                    onClick={handleLogout}
                                >
                                    🚪 Cerrar Sesión
                                </Nav.Link>
                            </>
                        )}

                        {/* 🟡 USUARIO CLIENTE */}
                        {user && !isAdmin() && (
                            <>
                                {/* Carrito */}
                                <LinkContainer to="/carrito">
                                    <Nav.Link className="me-3">
                                        🛒 Carrito {itemCount > 0 && `(${itemCount})`}
                                    </Nav.Link>
                                </LinkContainer>

                                <span className="me-3 fw-semibold">👤 {user.nombre}</span>

                                <Nav.Link
                                    className="text-danger fw-semibold"
                                    onClick={handleLogout}
                                >
                                    🚪 Cerrar Sesión
                                </Nav.Link>
                            </>
                        )}

                        {/* 🟢 Visitante / No logueado */}
                        {!user && (
                            <LinkContainer to="/login">
                                <Nav.Link className="btn btn-outline-dark rounded-pill px-3">
                                    🔐 Iniciar Sesión
                                </Nav.Link>
                            </LinkContainer>
                        )}
                    </Nav>

                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}

export default Navigation;
