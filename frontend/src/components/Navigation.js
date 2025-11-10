import React from 'react';
import { Navbar, Nav, Container, Badge, NavDropdown } from 'react-bootstrap';
import { LinkContainer } from 'react-router-bootstrap';
import { useNavigate } from 'react-router-dom';
import { useCart } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';

function Navigation() {
  const { getItemCount } = useCart();
  const { user, logout, isAdmin, isCliente } = useAuth();
  const itemCount = getItemCount();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <Navbar bg="dark" variant="dark" expand="lg" className="mb-4">
      <Container>
        <LinkContainer to="/">
          <Navbar.Brand>🛍️ Tienda de Ropa</Navbar.Brand>
        </LinkContainer>

        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <LinkContainer to="/">
              <Nav.Link>Inicio</Nav.Link>
            </LinkContainer>
            <LinkContainer to="/productos">
              <Nav.Link>Productos</Nav.Link>
            </LinkContainer>

            {/* Mostrar Admin solo si es administrador */}
            {user && isAdmin() && (
              <LinkContainer to="/admin">
                <Nav.Link>Admin Panel</Nav.Link>
              </LinkContainer>
            )}
          </Nav>

          <Nav className="ms-auto align-items-center">
            {/* Carrito solo para clientes autenticados */}
            {user && isCliente() && (
              <LinkContainer to="/carrito">
                <Nav.Link className="position-relative me-3">
                  🛒 Carrito
                  {itemCount > 0 && (
                    <Badge
                      bg="danger"
                      className="position-absolute top-0 start-100 translate-middle"
                      style={{ fontSize: '0.7em' }}
                    >
                      {itemCount}
                    </Badge>
                  )}
                </Nav.Link>
              </LinkContainer>
            )}

            {/* Mostrar login o usuario */}
            {user ? (
              <NavDropdown
                title={`👤 ${user.nombre}`}
                id="user-nav-dropdown"
                align="end"
              >
                <NavDropdown.Item disabled>
                  <small className="text-muted">
                    {isAdmin() ? '🔧 Administrador' : '👥 Cliente'}
                  </small>
                </NavDropdown.Item>
                <NavDropdown.Divider />
                <NavDropdown.Item onClick={handleLogout}>
                  🚪 Cerrar Sesión
                </NavDropdown.Item>
              </NavDropdown>
            ) : (
              <LinkContainer to="/login">
                <Nav.Link className="btn btn-outline-light btn-sm">
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
