import React, { useState, useEffect, useCallback } from "react";
import { Container, Row, Col, Card, Button, Badge, Alert } from "react-bootstrap";
import { useParams, useNavigate } from "react-router-dom";
import { productoService } from "../services/api";
import { useCart } from "../context/CartContext";

function ProductDetail() {
    const { id } = useParams();
    const navigate = useNavigate();
    const { addToCart } = useCart();

    const [producto, setProducto] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [adding, setAdding] = useState(false);

    // ⚡ Cargar producto por ID
    const cargarProducto = useCallback(async () => {
        try {
            setLoading(true);
            setError(null);
            const response = await productoService.obtenerPorId(id);
            setProducto(response?.data || null);
        } catch (error) {
            console.error("Error al cargar producto:", error);
            setError("No se pudo cargar el producto");
        } finally {
            setLoading(false);
        }
    }, [id]);

    useEffect(() => {
        cargarProducto();
    }, [cargarProducto]);

    // 🌀 Mostrar spinner mientras carga
    if (loading) {
        return (
            <Container className="text-center py-5">
                <div className="spinner-border text-primary" role="status">
                    <span className="visually-hidden">Cargando...</span>
                </div>
            </Container>
        );
    }

    // ❌ Mostrar error
    if (error) {
        return (
            <Container className="py-4">
                <Alert variant="danger">
                    {error}
                    <div className="mt-2">
                        <Button variant="primary" onClick={() => navigate("/productos")}>
                            Volver a Productos
                        </Button>
                    </div>
                </Alert>
            </Container>
        );
    }

    // 🚫 Producto no existe
    if (!producto) {
        return (
            <Container className="py-4">
                <Alert variant="warning">
                    Producto no encontrado
                    <div className="mt-2">
                        <Button variant="primary" onClick={() => navigate("/productos")}>
                            Volver a Productos
                        </Button>
                    </div>
                </Alert>
            </Container>
        );
    }

    return (
        <Container className="py-4">
            <Row>
                <Col md={6}>
                    <Card>
                        <Card.Img
                            variant="top"
                            src={
                                producto.imagenUrl ||
                                `https://via.placeholder.com/400x400?text=${encodeURIComponent(
                                    producto.nombre
                                )}`
                            }
                            alt={producto.nombre}
                            style={{ height: "400px", objectFit: "cover" }}
                        />
                    </Card>
                </Col>

                <Col md={6}>
                    <div className="ps-md-4">
                        {/* BREADCRUMB */}
                        <nav aria-label="breadcrumb">
                            <ol className="breadcrumb">
                                <li className="breadcrumb-item">
                                    <Button variant="link" className="p-0" onClick={() => navigate("/")}>
                                        Inicio
                                    </Button>
                                </li>
                                <li className="breadcrumb-item">
                                    <Button
                                        variant="link"
                                        className="p-0"
                                        onClick={() => navigate("/productos")}
                                    >
                                        Productos
                                    </Button>
                                </li>
                                <li className="breadcrumb-item active" aria-current="page">
                                    {producto.nombre}
                                </li>
                            </ol>
                        </nav>

                        <h1 className="mb-3">{producto.nombre}</h1>

                        {/* Badges */}
                        <div className="mb-3">
                            <Badge bg="secondary" className="me-2">
                                {producto.categoriaNombre}
                            </Badge>
                            <Badge bg="info" className="me-2">
                                Talla {producto.talla}
                            </Badge>
                            <Badge bg="warning" text="dark">
                                {producto.color}
                            </Badge>
                        </div>

                        <h2 className="text-success mb-3">${producto.precio}</h2>

                        {/* DESCRIPCIÓN */}
                        <Card className="mb-4">
                            <Card.Body>
                                <h5>Descripción</h5>
                                <p>{producto.descripcion || "Sin descripción disponible"}</p>
                            </Card.Body>
                        </Card>

                        {/* INFORMACIÓN */}
                        <Card className="mb-4">
                            <Card.Body>
                                <h5>Información del Producto</h5>
                                <Row>
                                    <Col sm={6}>
                                        <strong>Categoría:</strong> {producto.categoriaNombre}
                                    </Col>
                                    <Col sm={6}>
                                        <strong>Talla:</strong> {producto.talla}
                                    </Col>
                                    <Col sm={6}>
                                        <strong>Color:</strong> {producto.color}
                                    </Col>
                                    <Col sm={6}>
                                        <strong>Stock:</strong>
                                        <Badge
                                            bg={producto.stock > 0 ? "success" : "danger"}
                                            className="ms-2"
                                        >
                                            {producto.stock > 0
                                                ? `${producto.stock} disponible(s)`
                                                : "Agotado"}
                                        </Badge>
                                    </Col>
                                </Row>
                            </Card.Body>
                        </Card>

                        {/* BOTONES */}
                        <div className="d-grid gap-2">
                            <Button
                                variant="success"
                                size="lg"
                                disabled={producto.stock === 0 || adding}
                                onClick={() => {
                                    if (producto.stock > 0) {
                                        setAdding(true);
                                        addToCart(producto);
                                        setTimeout(() => setAdding(false), 1000);
                                    }
                                }}
                            >
                                {adding
                                    ? "✓ ¡Agregado al Carrito!"
                                    : producto.stock > 0
                                        ? "Agregar al Carrito"
                                        : "Sin Stock"}
                            </Button>

                            <Button variant="outline-primary" onClick={() => navigate("/productos")}>
                                Seguir Comprando
                            </Button>
                        </div>
                    </div>
                </Col>
            </Row>
        </Container>
    );
}

export default ProductDetail;
