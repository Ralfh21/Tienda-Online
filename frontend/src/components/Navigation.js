import React from 'react';
import { Navbar, Nav, Container, Badge } from 'react-bootstrap';
import { LinkContainer } from 'react-router-bootstrap';
import { useCart } from '../context/CartContext';

function Navigation() {
  const { getItemCount } = useCart();
  const itemCount = getItemCount();

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
            <LinkContainer to="/admin">
              <Nav.Link>Admin</Nav.Link>
            </LinkContainer>
          </Nav>

          <Nav className="ms-auto">
            <Nav.Link href="#" className="position-relative">
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
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}

export default Navigation;
