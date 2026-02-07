import React, { useState } from "react";
import { Card, Button, Badge } from "react-bootstrap";
import { Link } from "react-router-dom";
import { useCart } from "../context/CartContext";

function ProductCard({ producto }) {
  const { id, nombre, precio, talla, color, stock, imagenUrl } = producto;
  const { addToCart } = useCart();
  const [adding, setAdding] = useState(false);

  const handleAdd = async () => {
    if (stock > 0) {
      setAdding(true);
      addToCart(producto);
      setTimeout(() => setAdding(false), 500);
    }
  };

  return (
    <Card className="product-card h-100">
      <Link to={`/producto/${id}`} className="text-decoration-none">
        <div className="image-container position-relative overflow-hidden">
          <Card.Img
            variant="top"
            src={
              imagenUrl ||
              `https://via.placeholder.com/300x300?text=${encodeURIComponent(nombre)}`
            }
            alt={nombre}
            className="card-img-top"
          />
          {stock === 0 && (
            <div
              className="position-absolute top-0 start-0 w-100 h-100 d-flex align-items-center justify-content-center"
              style={{ backgroundColor: "rgba(0,0,0,0.6)" }}
            >
              <span className="text-white fw-bold">AGOTADO</span>
            </div>
          )}
        </div>
      </Link>

      <Card.Body className="d-flex flex-column">
        <Card.Title className="card-title mb-1">{nombre}</Card.Title>

        <div className="mb-2">
          <Badge bg="light" text="dark" className="me-1 border">
            {talla}
          </Badge>
          <Badge bg="light" text="dark" className="border">
            {color}
          </Badge>
        </div>

        <div className="d-flex justify-content-between align-items-center mb-2">
          <span className="price-tag">${precio?.toFixed(2)}</span>
          <Badge bg={stock > 0 ? "success" : "danger"} className="stock-badge">
            {stock > 0 ? `${stock} uds` : "Agotado"}
          </Badge>
        </div>

        <div className="d-grid gap-2 mt-auto">
          <Button
            variant="primary"
            size="sm"
            disabled={stock === 0 || adding}
            onClick={handleAdd}
          >
            {adding ? "✓ Agregado" : stock > 0 ? "Agregar" : "Sin Stock"}
          </Button>
        </div>
      </Card.Body>
    </Card>
  );
}

export default ProductCard;
