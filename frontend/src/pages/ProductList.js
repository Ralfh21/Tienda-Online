import React, { useState, useEffect, useCallback } from 'react';
import { Container, Col, Form, Button, Card } from 'react-bootstrap';
import { productoService, categoriaService } from '../services/api';
import ProductCard from '../components/ProductCard';
import '../App.css';

function ProductList() {
    const [productos, setProductos] = useState([]);
    const [productosOriginales, setProductosOriginales] = useState([]);
    const [categorias, setCategorias] = useState([]);
    const [tallas, setTallas] = useState([]);
    const [loading, setLoading] = useState(true);

    const [busqueda, setBusqueda] = useState('');
    const [categoriaSeleccionada, setCategoriaSeleccionada] = useState('');
    const [tallaSeleccionada, setTallaSeleccionada] = useState('');
    const [colorSeleccionado, setColorSeleccionado] = useState('');

    // Colores fijos para el filtro
    const coloresDisponibles = ["Blanco","Negro","Rojo","Azul","Verde","Amarillo","Naranja","Morado","Rosa","Gris","Marrón"];

    const cargarDatos = useCallback(async () => {
        try {
            setLoading(true);
            const [productosResponse, categoriasResponse] = await Promise.all([
                productoService.obtenerTodos(),
                categoriaService.obtenerTodos(),
            ]);

            const productosData = productosResponse?.data || [];
            const categoriasData = categoriasResponse?.data || [];

            setProductosOriginales(productosData);
            setProductos(productosData);
            setCategorias(categoriasData);

            const tallasUnicas = [...new Set(productosData.map((p) => p.talla).filter(Boolean))];
            setTallas(tallasUnicas);
        } catch (error) {
            console.error('Error al cargar productos:', error);
        } finally {
            setLoading(false);
        }
    }, []);

    const filtrarProductos = useCallback(() => {
        let productosFiltrados = [...productosOriginales];

        if (busqueda) {
            const nombreRegex = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$/;
            if (nombreRegex.test(busqueda)) {
                productosFiltrados = productosFiltrados.filter(
                    (p) =>
                        p.nombre?.toLowerCase().includes(busqueda.toLowerCase()) ||
                        p.descripcion?.toLowerCase().includes(busqueda.toLowerCase())
                );
            }
            // Si no coincide con regex, no filtra y mantiene los productos previos
        }

        if (categoriaSeleccionada) {
            productosFiltrados = productosFiltrados.filter(
                (p) => p.categoriaNombre === categoriaSeleccionada
            );
        }

        if (tallaSeleccionada) {
            productosFiltrados = productosFiltrados.filter((p) => p.talla === tallaSeleccionada);
        }

        if (colorSeleccionado) {
            productosFiltrados = productosFiltrados.filter((p) => p.color === colorSeleccionado);
        }

        setProductos(productosFiltrados);
    }, [productosOriginales, busqueda, categoriaSeleccionada, tallaSeleccionada, colorSeleccionado]);

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

    // Función que evita escribir números en el input de búsqueda
    const handleBusquedaChange = (e) => {
        const valor = e.target.value;
        const soloLetras = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$/;
        if (soloLetras.test(valor)) {
            setBusqueda(valor);
        }
    };

    return (
        <Container fluid className="product-list-container py-5">
            {/* CATEGORÍAS */}
            {categorias.length > 0 && (
                <div className="categories-section mb-5 text-center">
                    <h2 className="fw-bold mb-4">Categorías</h2>
                    <div className="d-flex flex-wrap justify-content-center gap-3">
                        {categorias.map((categoria) => (
                            <Button
                                key={categoria.id}
                                variant="outline-dark"
                                className="rounded-pill px-4 py-2 category-btn"
                                onClick={() => setCategoriaSeleccionada(categoria.nombre)}
                            >
                                {categoria.nombre}
                            </Button>
                        ))}
                    </div>
                </div>
            )}

            <div className="shop-layout d-flex flex-wrap gap-4">
                {/* SIDEBAR */}
                <Col xs={12} md={3} className="sidebar mb-4">
                    <Card className="filter-sidebar shadow-sm border-0 p-3">
                        <Card.Body>
                            <h5 className="fw-bold mb-4 text-center">Filtros</h5>

                            <Form.Group className="mb-3">
                                <Form.Label>Buscar</Form.Label>
                                <Form.Control
                                    type="text"
                                    placeholder="Nombre del producto..."
                                    value={busqueda}
                                    onChange={handleBusquedaChange}
                                />
                            </Form.Group>

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

                            <Form.Group className="mb-4">
                                <Form.Label>Color</Form.Label>
                                <Form.Select
                                    value={colorSeleccionado}
                                    onChange={(e) => setColorSeleccionado(e.target.value)}
                                >
                                    <option value="">Todos los colores</option>
                                    {coloresDisponibles.map((color) => (
                                        <option key={color} value={color}>
                                            {color}
                                        </option>
                                    ))}
                                </Form.Select>
                            </Form.Group>

                            <Button
                                variant="dark"
                                onClick={limpiarFiltros}
                                className="w-100 rounded-pill"
                            >
                                Limpiar Filtros
                            </Button>
                        </Card.Body>
                    </Card>
                </Col>

                {/* PRODUCTOS */}
                <div className="products-area flex-grow-1">
                    <h2 className="fw-bold mb-4 text-center text-md-start">
                        Productos ({productos.length})
                    </h2>

                    {loading ? (
                        <div className="text-center py-5">
                            <div className="spinner-border text-dark" role="status">
                                <span className="visually-hidden">Cargando...</span>
                            </div>
                        </div>
                    ) : productos.length > 0 ? (
                        <div className="products-grid d-flex flex-wrap justify-content-center gap-4">
                            {productos.map((producto) => (
                                <div key={producto.id} className="product-cell" style={{ maxWidth: '350px' }}>
                                    <ProductCard producto={producto} />
                                </div>
                            ))}
                        </div>
                    ) : (
                        <div className="text-center text-muted mt-4">
                            <p>No se encontraron productos con los filtros seleccionados.</p>
                        </div>
                    )}
                </div>
            </div>
        </Container>
    );
}

export default ProductList;
