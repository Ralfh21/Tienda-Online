import React from 'react';
import { Container, Row, Col, Card, Button, Table, Alert } from 'react-bootstrap';
import { useCart } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import '../styles/Cart.css';

const Cart = () => {
  const { items, removeFromCart, updateQuantity, getTotal, clearCart } = useCart();
  const { user } = useAuth();
  const navigate = useNavigate();

  if (!user) {
    return (
      <Container className="mt-5">
        <Alert variant="warning">
          <Alert.Heading>Inicia sesión para ver tu carrito</Alert.Heading>
          <p>Necesitas iniciar sesión para acceder a tu carrito de compras.</p>
          <Button variant="primary" onClick={() => navigate('/login')}>
            Ir a Login
          </Button>
        </Alert>
      </Container>
    );
  }

  if (items.length === 0) {
    return (
      <Container className="mt-5">
        <Card className="text-center p-5">
          <Card.Body>
            <div className="empty-cart-icon mb-4">
              🛒
            </div>
            <h2>Tu carrito está vacío</h2>
            <p className="text-muted">¡Agrega productos para comenzar a comprar!</p>
            <Button variant="primary" onClick={() => navigate('/productos')}>
              Ver Productos
            </Button>
          </Card.Body>
        </Card>
      </Container>
    );
  }

  return (
    <Container className="mt-4">
      <Row>
        <Col>
          <h2 className="mb-4">🛒 Mi Carrito de Compras</h2>

          <Card>
            <Card.Body>
              <Table responsive hover>
                <thead>
                  <tr>
                    <th>Producto</th>
                    <th>Precio</th>
                    <th>Cantidad</th>
                    <th>Subtotal</th>
                    <th>Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  {items.map((item) => (
                    <tr key={item.id}>
                      <td>
                        <div className="d-flex align-items-center">
                          <img
                            src={item.imagenUrl || 'https://via.placeholder.com/80'}
                            alt={item.nombre}
                            className="cart-product-img me-3"
                          />
                          <div>
                            <strong>{item.nombre}</strong>
                            <div className="text-muted small">
                              {item.talla && `Talla: ${item.talla} - `}
                              {item.color && `Color: ${item.color}`}
                            </div>
                          </div>
                        </div>
                      </td>
                      <td className="align-middle">${item.precio?.toFixed(2)}</td>
                      <td className="align-middle">
                        <div className="quantity-controls">
                          <Button
                            variant="outline-secondary"
                            size="sm"
                            onClick={() => updateQuantity(item.id, item.cantidad - 1)}
                          >
                            -
                          </Button>
                          <span className="mx-3">{item.cantidad}</span>
                          <Button
                            variant="outline-secondary"
                            size="sm"
                            onClick={() => updateQuantity(item.id, item.cantidad + 1)}
                            disabled={item.cantidad >= item.stock}
                          >
                            +
                          </Button>
                        </div>
                      </td>
                      <td className="align-middle">
                        <strong>${(item.precio * item.cantidad).toFixed(2)}</strong>
                      </td>
                      <td className="align-middle">
                        <Button
                          variant="danger"
                          size="sm"
                          onClick={() => removeFromCart(item.id)}
                        >
                          🗑️ Eliminar
                        </Button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </Table>

              <div className="cart-summary mt-4">
                <Row>
                  <Col md={8}>
                    <Button
                      variant="outline-danger"
                      onClick={clearCart}
                      className="me-2"
                    >
                      Vaciar Carrito
                    </Button>
                    <Button
                      variant="outline-primary"
                      onClick={() => navigate('/productos')}
                    >
                      Seguir Comprando
                    </Button>
                  </Col>
                  <Col md={4} className="text-end">
                    <Card className="summary-card">
                      <Card.Body>
                        <h5>Resumen del Pedido</h5>
                        <hr />
                        <div className="d-flex justify-content-between mb-2">
                          <span>Subtotal:</span>
                          <strong>${getTotal().toFixed(2)}</strong>
                        </div>
                        <div className="d-flex justify-content-between mb-2">
                          <span>IVA (12%):</span>
                          <strong>${(getTotal() * 0.12).toFixed(2)}</strong>
                        </div>
                        <hr />
                        <div className="d-flex justify-content-between mb-3">
                          <h5>Total:</h5>
                          <h5 className="text-primary">
                            ${(getTotal() * 1.12).toFixed(2)}
                          </h5>
                        </div>
                        <Button variant="success" size="lg" className="w-100">
                          Proceder al Pago
                        </Button>
                      </Card.Body>
                    </Card>
                  </Col>
                </Row>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default Cart;

