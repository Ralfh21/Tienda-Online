// src/pages/AdminPanel.js
import React, { useState, useEffect, useCallback } from 'react';
import { Container, Row, Col, Card, Button, Table, Form, Modal, Alert, Badge } from 'react-bootstrap';
import { productoService, categoriaService } from '../services/api';
import { useAuth } from '../context/AuthContext';

function AdminPanel() {
    const { user } = useAuth();
    const [productos, setProductos] = useState([]);
    const [categorias, setCategorias] = useState([]);
    const [showModal, setShowModal] = useState(false);
    const [editingProduct, setEditingProduct] = useState(null);
    const [loading, setLoading] = useState(true);
    const [alert, setAlert] = useState({ show: false, message: '', variant: '' });

    const [formData, setFormData] = useState({
        nombre: '',
        descripcion: '',
        precio: '',
        categoriaId: '',
        talla: '',
        color: '',
        stock: '',
        imagenUrl: ''
    });

    const cargarProductos = useCallback(async () => {
        try {
            setLoading(true);
            const [productosResponse, categoriasResponse] = await Promise.all([
                productoService.obtenerTodos(),
                categoriaService.obtenerTodos()
            ]);

            setProductos(productosResponse?.data || []);

            const categoriasList = categoriasResponse?.data || [];
            const categoriasUnicas = Array.from(
                new Map(categoriasList.map(cat => [cat.nombre, cat])).values()
            );

            setCategorias(categoriasUnicas);
        } catch (error) {
            mostrarAlerta('Error al cargar datos', 'danger');
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        cargarProductos();
    }, [cargarProductos]);

    const mostrarAlerta = (message, variant = 'success') => {
        setAlert({ show: true, message, variant });
        setTimeout(() => setAlert({ show: false, message: '', variant: '' }), 2500);
    };

    const handleInputChange = e => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async e => {
        e.preventDefault();
        try {
            if (editingProduct) {
                await productoService.actualizar(editingProduct.id, formData);
                mostrarAlerta('Producto actualizado correctamente');
            } else {
                await productoService.crear(formData);
                mostrarAlerta('Producto creado correctamente');
            }
            handleCloseModal();
            cargarProductos();
        } catch {
            mostrarAlerta('Error al guardar producto', 'danger');
        }
    };

    const handleEdit = (producto) => {
        setEditingProduct(producto);
        setFormData({
            nombre: producto.nombre,
            descripcion: producto.descripcion || '',
            precio: producto.precio?.toString() || '',
            categoriaId: producto.categoriaId?.toString() || '',
            talla: producto.talla || '',
            color: producto.color || '',
            stock: producto.stock?.toString() || '',
            imagenUrl: producto.imagenUrl || ''
        });
        setShowModal(true);
    };

    const handleDeactivate = async (id) => {
        if (!window.confirm("¿Deseas desactivar este producto?")) return;
        await productoService.desactivar(id);
        mostrarAlerta("Producto desactivado");
        cargarProductos();
    };

    const handleDelete = async (id) => {
        if (!window.confirm("¿Eliminar producto permanentemente?")) return;
        await productoService.eliminar(id);
        mostrarAlerta("Producto eliminado");
        cargarProductos();
    };

    const handleCloseModal = () => {
        setShowModal(false);
        setEditingProduct(null);
        setFormData({
            nombre: '',
            descripcion: '',
            precio: '',
            categoriaId: '',
            talla: '',
            color: '',
            stock: '',
            imagenUrl: ''
        });
    };

    return (
        <Container className="py-4 admin-panel-container">

            <Alert variant="light" className="shadow-sm p-4 border rounded-4 admin-header">
                <h4 className="fw-bold mb-0">
                    🔧 Bienvenido, {user?.nombre}
                    <Badge bg="dark" className="ms-2">ADMIN</Badge>
                </h4>
                <p className="mt-2 mb-0 text-muted">
                    Gestiona productos y categorías de tu tienda.
                </p>
            </Alert>

            <div className="d-flex justify-content-between align-items-center mb-4">
                <h2 className="fw-bold">Panel de Administración</h2>
                <Button variant="primary" onClick={() => setShowModal(true)}>
                    + Agregar producto
                </Button>
            </div>

            {alert.show && (
                <Alert variant={alert.variant} className="rounded-3 shadow-sm">
                    {alert.message}
                </Alert>
            )}

            {/* Listado */}
            <Card className="shadow-sm border-0 rounded-4">
                <Card.Body>
                    <h5 className="fw-bold">Productos Registrados ({productos.length})</h5>

                    {loading ? (
                        <div className="text-center py-4">
                            <div className="spinner-border text-warning"></div>
                        </div>
                    ) : (
                        <div className="table-responsive mt-3">
                            <Table hover className="align-middle">
                                <thead className="table-light">
                                <tr>
                                    <th>ID</th>
                                    <th>Nombre</th>
                                    <th>Precio</th>
                                    <th>Categoría</th>
                                    <th>Talla</th>
                                    <th>Color</th>
                                    <th>Stock</th>
                                    <th>Acciones</th>
                                </tr>
                                </thead>

                                <tbody>
                                {productos.map(producto => (
                                    <tr key={producto.id}>
                                        <td>{producto.id}</td>
                                        <td>{producto.nombre}</td>
                                        <td>${producto.precio}</td>
                                        <td>{producto.categoriaNombre}</td>
                                        <td>{producto.talla}</td>
                                        <td>{producto.color}</td>
                                        <td>{producto.stock}</td>
                                        <td className="text-nowrap">
                                            <Button size="sm" variant="outline-primary" className="me-2" onClick={() => handleEdit(producto)}>Editar</Button>
                                            <Button size="sm" variant="outline-warning" className="me-2" onClick={() => handleDeactivate(producto.id)}>Desactivar</Button>
                                            <Button size="sm" variant="outline-danger" onClick={() => handleDelete(producto.id)}>Eliminar</Button>
                                        </td>
                                    </tr>
                                ))}
                                </tbody>

                            </Table>
                        </div>
                    )}

                </Card.Body>
            </Card>

            {/* Modal */}
            <Modal
                show={showModal}
                onHide={handleCloseModal}
                size="lg"
                centered
                backdrop="static"
            >
                <Modal.Header closeButton>
                    <Modal.Title className="fw-bold">
                        {editingProduct ? "Editar Producto" : "Agregar Nuevo Producto"}
                    </Modal.Title>
                </Modal.Header>

                <Form className="admin-modal-form" onSubmit={handleSubmit}>
                    <Modal.Body>

                        <Row>
                            <Col md={6}>
                                <Form.Group className="mb-3">
                                    <Form.Label>Nombre *</Form.Label>
                                    <Form.Control required name="nombre" value={formData.nombre} onChange={handleInputChange}/>
                                </Form.Group>
                            </Col>

                            <Col md={6}>
                                <Form.Group className="mb-3">
                                    <Form.Label>Precio *</Form.Label>
                                    <Form.Control type="number" step="0.01" required name="precio" value={formData.precio} onChange={handleInputChange}/>
                                </Form.Group>
                            </Col>
                        </Row>

                        <Form.Group className="mb-3">
                            <Form.Label>Descripción</Form.Label>
                            <Form.Control as="textarea" rows={3} name="descripcion" value={formData.descripcion} onChange={handleInputChange}/>
                        </Form.Group>

                        <Row>
                            <Col md={4}>
                                <Form.Group className="mb-3">
                                    <Form.Label>Categoría *</Form.Label>
                                    <Form.Select required name="categoriaId" value={formData.categoriaId} onChange={handleInputChange}>
                                        <option value="">Seleccionar</option>
                                        {categorias.map(c => <option key={c.id} value={c.id}>{c.nombre}</option>)}
                                    </Form.Select>
                                </Form.Group>
                            </Col>

                            <Col md={4}>
                                <Form.Group>
                                    <Form.Label>Talla *</Form.Label>
                                    <Form.Select required name="talla" value={formData.talla} onChange={handleInputChange}>
                                        <option value="">Seleccionar</option>
                                        {["XS","S","M","L","XL","XXL"].map(t => (
                                            <option key={t} value={t}>{t}</option>
                                        ))}
                                    </Form.Select>
                                </Form.Group>
                            </Col>

                            <Col md={4}>
                                <Form.Group>
                                    <Form.Label>Color *</Form.Label>
                                    <Form.Control required name="color" placeholder="Rojo, Azul, Negro..." value={formData.color} onChange={handleInputChange}/>
                                </Form.Group>
                            </Col>
                        </Row>

                        <Row className="mt-3">
                            <Col md={6}>
                                <Form.Group>
                                    <Form.Label>Stock *</Form.Label>
                                    <Form.Control required type="number" min="0" name="stock" value={formData.stock} onChange={handleInputChange}/>
                                </Form.Group>
                            </Col>

                            <Col md={6}>
                                <Form.Group>
                                    <Form.Label>URL de Imagen</Form.Label>
                                    <Form.Control name="imagenUrl" value={formData.imagenUrl} onChange={handleInputChange}/>
                                </Form.Group>
                            </Col>
                        </Row>

                    </Modal.Body>

                    <Modal.Footer>
                        <Button variant="secondary" onClick={handleCloseModal}>Cancelar</Button>
                        <Button variant="primary" type="submit">{editingProduct ? "Actualizar" : "Crear"} Producto</Button>
                    </Modal.Footer>
                </Form>

            </Modal>

        </Container>
    );
}

export default AdminPanel;
