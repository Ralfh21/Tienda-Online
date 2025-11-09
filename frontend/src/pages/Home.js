import React, { useState, useEffect, useCallback } from 'react';
import { Container, Row, Col, Button, Card } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { productoService, categoriaService } from '../services/api';
import ProductCard from '../components/ProductCard';

function Home() {
    const [productosDestacados, setProductosDestacados] = useState([]);
    const [categorias, setCategorias] = useState([]);
    const [loading, setLoading] = useState(true);

    const cargarDatos = useCallback(async () => {
        try {
            setLoading(true);
            const [productosResponse, categoriasResponse] = await Promise.all([
                productoService.obtenerTodos(),
                categoriaService.obtenerTodos(),
            ]);

            // ✅ Validación segura contra undefined o respuestas vacías
            const productos = productosResponse?.data || [];
            const categoriasList = categoriasResponse?.data || [];

            // Filtrar solo productos con stock > 0 y mostrar los primeros 6
            const productosDisponibles = productos.filter(p => p.stock > 0);
            setProductosDestacados(productosDisponibles.slice(0, 6));

            // Usar objetos completos de categorías 
            setCategorias(categoriasList);
        } catch (error) {
            console.error('Error al cargar datos:', error);
            setProductosDestacados([]);
            setCategorias([]);
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        cargarDatos();
    }, [cargarDatos]); // ✅ Añadido como dependencia para cumplir con react-hooks/exhaustive-deps

    return (
        <Container>
            {/* Hero Section */}
            <div className="bg-primary text-white rounded p-5 mb-5">
                <Row className="align-items-center">
                    <Col md={8}>
                        <h1 className="display-4">¡Bienvenido a nuestra Tienda de Ropa!</h1>
                        <p className="lead">
                            Descubre las últimas tendencias en moda. Calidad, estilo y los mejores precios
                            en un solo lugar.
                        </p>
                        <Link to="/productos">
                            <Button variant="light" size="lg">
                                Explorar Productos
                            </Button>
                        </Link>
                    </Col>
                    <Col md={4} className="text-center">
                        <div style={{ fontSize: '8rem' }}>🛍️</div>
                    </Col>
                </Row>
            </div>

            {/* Categorías */}
            <Row className="mb-5">
                <Col>
                    <h2 className="text-center mb-4">Categorías</h2>
                    <Row>
                        {categorias.length > 0 ? (
                            categorias.map((categoria) => (
                                <Col md={3} sm={6} key={categoria.id} className="mb-3">
                                    <Card className="text-center h-100">
                                        <Card.Body>
                                            <Card.Title>{categoria.nombre}</Card.Title>
                                            <Link to={`/productos?categoria=${categoria.nombre}`}>
                                                <Button variant="outline-primary">
                                                    Ver {categoria.nombre}
                                                </Button>
                                            </Link>
                                        </Card.Body>
                                    </Card>
                                </Col>
                            ))
                        ) : (
                            <Col className="text-center text-muted">No hay categorías disponibles.</Col>
                        )}
                    </Row>
                </Col>
            </Row>

            {/* Productos Destacados */}
            <Row>
                <Col>
                    <h2 className="text-center mb-4">Productos Destacados</h2>
                    {loading ? (
                        <div className="text-center">
                            <div className="spinner-border text-primary" role="status">
                                <span className="visually-hidden">Cargando...</span>
                            </div>
                        </div>
                    ) : productosDestacados.length > 0 ? (
                        <Row>
                            {productosDestacados.map((producto) => (
                                <Col lg={4} md={6} key={producto.id}>
                                    <ProductCard producto={producto} />
                                </Col>
                            ))}
                        </Row>
                    ) : (
                        <Row>
                            <Col className="text-center">
                                <p className="text-muted">No hay productos disponibles en este momento.</p>
                                <Link to="/admin">
                                    <Button variant="primary">Agregar Productos</Button>
                                </Link>
                            </Col>
                        </Row>
                    )}
                </Col>
            </Row>
        </Container>
    );
}

export default Home;
