import React, { useState, useEffect, useCallback } from 'react';
import { Container, Row, Col, Form, Button, Card } from 'react-bootstrap';
import { productoService, categoriaService } from '../services/api';
import ProductCard from '../components/ProductCard';

function ProductList() {
    const [productos, setProductos] = useState([]);
    const [categorias, setCategorias] = useState([]);
    const [tallas, setTallas] = useState([]);
    const [colores, setColores] = useState([]);
    const [loading, setLoading] = useState(true);

    // Filtros
    const [busqueda, setBusqueda] = useState('');
    const [categoriaSeleccionada, setCategoriaSeleccionada] = useState('');
    const [tallaSeleccionada, setTallaSeleccionada] = useState('');
    const [colorSeleccionado, setColorSeleccionado] = useState('');

    // ✅ Se usa useCallback para que el efecto no genere advertencias
    const cargarDatos = useCallback(async () => {
        try {
            setLoading(true);
            const [
                productosResponse,
                categoriasResponse
            ] = await Promise.all([
                productoService.obtenerTodos(),
                categoriaService.obtenerTodos()
            ]);

            const productosData = productosResponse?.data || [];
            const categoriasData = categoriasResponse?.data || [];
            
            setProductosOriginales(productosData);
            setProductos(productosData);

            // Usar objetos completos de categorías
            setCategorias(categoriasData);

            // Extraer tallas y colores únicos de los productos
            const tallasUnicas = [...new Set(productosData.map(p => p.talla).filter(Boolean))];
            const coloresUnicos = [...new Set(productosData.map(p => p.color).filter(Boolean))];

            setTallas(tallasUnicas);
            setColores(coloresUnicos);
        } catch (error) {
            console.error('Error al cargar productos:', error);
            setProductos([]);
            setCategorias([]);
            setTallas([]);
            setColores([]);
        } finally {
            setLoading(false);
        }
    }, []);

    // Agregar estado para productos originales
    const [productosOriginales, setProductosOriginales] = useState([]);

    const filtrarProductos = useCallback(() => {
        try {
            let productosFiltrados = [...productosOriginales];

            // Filtrar por búsqueda (nombre)
            if (busqueda) {
                productosFiltrados = productosFiltrados.filter(p =>
                    p.nombre?.toLowerCase().includes(busqueda.toLowerCase()) ||
                    p.descripcion?.toLowerCase().includes(busqueda.toLowerCase())
                );
            }

            // Filtrar por categoría
            if (categoriaSeleccionada) {
                productosFiltrados = productosFiltrados.filter(p =>
                    p.categoriaNombre === categoriaSeleccionada
                );
            }

            // Filtrar por talla
            if (tallaSeleccionada) {
                productosFiltrados = productosFiltrados.filter(p =>
                    p.talla === tallaSeleccionada
                );
            }

            // Filtrar por color
            if (colorSeleccionado) {
                productosFiltrados = productosFiltrados.filter(p =>
                    p.color === colorSeleccionado
                );
            }

            setProductos(productosFiltrados);
        } catch (error) {
            console.error('Error al filtrar productos:', error);
        }
    }, [productosOriginales, busqueda, categoriaSeleccionada, tallaSeleccionada, colorSeleccionado]);

    // ✅ Se llaman los efectos con dependencias estables
    useEffect(() => {
        cargarDatos();
    }, [cargarDatos]);

    useEffect(() => {
        filtrarProductos();
    }, [filtrarProductos]);

    const limpiarFiltros = () => {
        setBusqueda('');
        setCategoriaSeleccionada('');
        setTallaSeleccionada('');
        setColorSeleccionado('');
    };

    return (
        <Container>
            <Row>
                <Col md={3}>
                    <Card className="filter-sidebar mb-4">
                        <Card.Body>
                            <h5>Filtros</h5>

                            {/* Búsqueda */}
                            <Form.Group className="mb-3">
                                <Form.Label>Buscar</Form.Label>
                                <Form.Control
                                    type="text"
                                    placeholder="Nombre del producto..."
                                    value={busqueda}
                                    onChange={(e) => setBusqueda(e.target.value)}
                                />
                            </Form.Group>

                            {/* Categoría */}
                            <Form.Group className="mb-3">
                                <Form.Label>Categoría</Form.Label>
                                <Form.Select
                                    value={categoriaSeleccionada}
                                    onChange={(e) => setCategoriaSeleccionada(e.target.value)}
                                >
                                    <option value="">Todas las categorías</option>
                                    {categorias.map((categoria) => (
                                        <option key={categoria.id} value={categoria.nombre}>
                                            {categoria.nombre}
                                        </option>
                                    ))}
                                </Form.Select>
                            </Form.Group>

                            {/* Talla */}
                            <Form.Group className="mb-3">
                                <Form.Label>Talla</Form.Label>
                                <Form.Select
                                    value={tallaSeleccionada}
                                    onChange={(e) => setTallaSeleccionada(e.target.value)}
                                >
                                    <option value="">Todas las tallas</option>
                                    {tallas.map((talla) => (
                                        <option key={talla} value={talla}>
                                            {talla}
                                        </option>
                                    ))}
                                </Form.Select>
                            </Form.Group>

                            {/* Color */}
                            <Form.Group className="mb-3">
                                <Form.Label>Color</Form.Label>
                                <Form.Select
                                    value={colorSeleccionado}
                                    onChange={(e) => setColorSeleccionado(e.target.value)}
                                >
                                    <option value="">Todos los colores</option>
                                    {colores.map((color) => (
                                        <option key={color} value={color}>
                                            {color}
                                        </option>
                                    ))}
                                </Form.Select>
                            </Form.Group>

                            <Button
                                variant="outline-secondary"
                                onClick={limpiarFiltros}
                                className="w-100"
                            >
                                Limpiar Filtros
                            </Button>
                        </Card.Body>
                    </Card>
                </Col>

                <Col md={9}>
                    <div className="d-flex justify-content-between align-items-center mb-4">
                        <h2>Productos ({productos.length})</h2>
                    </div>

                    {loading ? (
                        <div className="text-center">
                            <div className="spinner-border text-primary" role="status">
                                <span className="visually-hidden">Cargando...</span>
                            </div>
                        </div>
                    ) : productos.length > 0 ? (
                        <Row>
                            {productos.map((producto) => (
                                <Col lg={4} md={6} key={producto.id}>
                                    <ProductCard producto={producto} />
                                </Col>
                            ))}
                        </Row>
                    ) : (
                        <div className="text-center">
                            <p className="text-muted">
                                No se encontraron productos con los filtros seleccionados.
                            </p>
                        </div>
                    )}
                </Col>
            </Row>
        </Container>
    );
}

export default ProductList;
