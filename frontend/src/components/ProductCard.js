import React, { useState } from 'react';
import { Card, Button, Badge } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { useCart } from '../context/CartContext';

function ProductCard({ producto }) {
  const { id, nombre, descripcion, precio, categoriaNombre, talla, color, stock, imagenUrl } = producto;
  const { addToCart } = useCart();
  const [adding, setAdding] = useState(false);

  return (
    <Card className="h-100 mb-4">
      <Card.Img
        variant="top"
        src={imagenUrl || `https://via.placeholder.com/300x200?text=${nombre}`}
        className="product-image"
        alt={nombre}
      />
      <Card.Body className="d-flex flex-column">
        <Card.Title>{nombre}</Card.Title>
        <Card.Text className="text-muted small">
          {descripcion || 'Sin descripción disponible'}
        </Card.Text>

        <div className="mb-2">
          <Badge bg="secondary" className="me-2">{categoriaNombre}</Badge>
          <Badge bg="info" className="me-2">Talla {talla}</Badge>
          <Badge bg="warning" text="dark">{color}</Badge>
        </div>

        <div className="d-flex justify-content-between align-items-center mb-2">
          <span className="price-tag">${precio}</span>
          <Badge
            bg={stock > 0 ? 'success' : 'danger'}
            className="stock-badge"
          >
            {stock > 0 ? `Stock: ${stock}` : 'Agotado'}
          </Badge>
        </div>

        <div className="mt-auto">
          <div className="d-grid gap-2">
            <Link to={`/producto/${id}`}>
              <Button variant="primary" className="w-100">
                Ver Detalles
              </Button>
            </Link>
            <Button
              variant="success"
              disabled={stock === 0 || adding}
              className="w-100"
              onClick={async () => {
                if (stock > 0) {
                  setAdding(true);
                  addToCart(producto);
                  // Pequeña pausa para feedback visual
                  setTimeout(() => setAdding(false), 500);
                }
              }}
            >
              {adding ? '✓ Agregado!' : stock > 0 ? 'Agregar al Carrito' : 'Sin Stock'}
            </Button>
          </div>
        </div>
      </Card.Body>
    </Card>
  );
}

export default ProductCard;
