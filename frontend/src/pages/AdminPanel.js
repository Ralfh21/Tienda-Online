// src/pages/AdminPanel.js
import React, { useState, useEffect, useCallback } from 'react';
import { Container, Row, Col, Card, Button, Table, Form, Alert, Modal } from 'react-bootstrap';
import { productoService, categoriaService } from '../services/api';
import { useAuth } from '../context/AuthContext';
import "../styles/AdminPanel.css";

function AdminPanel() {
    const { user } = useAuth();
    const [productos, setProductos] = useState([]);
    const [categorias, setCategorias] = useState([]);
    const [showForm, setShowForm] = useState(false);
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

    const coloresDisponibles = ["Blanco","Negro","Rojo","Azul","Verde","Amarillo","Naranja","Morado","Rosa","Gris","Marrón"];

    // Estado para modal de confirmación de eliminación
    const [confirmDelete, setConfirmDelete] = useState({ show: false, producto: null });

    // Carga productos y categorías
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
        setTimeout(() => setAlert({ show: false, message: '', variant: '' }), 3500);
    };

    const handleInputChange = e => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const validarFormulario = () => {
        const nombreRegex = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$/;
        if (!formData.nombre.trim()) return "El nombre del producto es obligatorio.";
        if (!nombreRegex.test(formData.nombre)) return "El nombre solo puede contener letras y espacios.";
        if (!formData.precio || Number(formData.precio) <= 0) return "El precio debe ser mayor a 0.";
        if (!formData.categoriaId) return "Debe seleccionar una categoría.";
        if (!formData.talla) return "Debe seleccionar una talla.";
        if (!formData.color) return "Debe seleccionar un color.";
        if (Number(formData.stock) < 0) return "El stock no puede ser negativo.";
        if (formData.descripcion.length < 10) return "La descripción debe tener al menos 10 caracteres.";
        if (formData.imagenUrl.trim()) {
            const urlRegex = /^https?:\/\/.+\.(jpg|jpeg|png|webp)$/i;
            if (!urlRegex.test(formData.imagenUrl)) return "La URL de la imagen no es válida. Debe terminar en jpg, jpeg, png o webp.";
        }
        return null;
    };

    const handleSubmit = async e => {
        e.preventDefault();
        const error = validarFormulario();
        if (error) return mostrarAlerta(error, "danger");

        try {
            if (editingProduct) {
                await productoService.actualizar(editingProduct.id, formData);
                mostrarAlerta('Producto actualizado correctamente', 'success');
            } else {
                await productoService.crear(formData);
                mostrarAlerta('Producto creado correctamente', 'success');
            }

            handleCancel();
            cargarProductos();

        } catch (err) {
            mostrarAlerta("Error al guardar el producto", "danger");
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
        setShowForm(true);
    };

    const handleDeactivate = async (id) => {
        if (!window.confirm("¿Deseas desactivar este producto?")) return;
        await productoService.desactivar(id);
        mostrarAlerta("Producto desactivado", "warning");
        cargarProductos();
    };

    // Abrir modal de confirmación
    const openConfirmDelete = (producto) => {
        setConfirmDelete({ show: true, producto });
    };

    // Cerrar modal
    const closeConfirmDelete = () => {
        setConfirmDelete({ show: false, producto: null });
    };

    // Confirmar eliminación desde modal
    const handleConfirmDelete = async () => {
        if (confirmDelete.producto) {
            await productoService.eliminar(confirmDelete.producto.id);
            mostrarAlerta(`Camiseta "${confirmDelete.producto.nombre}" eliminada`, "danger");
            cargarProductos();
        }
        closeConfirmDelete();
    };

    const handleCancel = () => {
        setShowForm(false);
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

    const handleAddNew = () => {
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
        setShowForm(true);
    }

    return (
        <Container className="py-4 admin-panel-container">

            <center>
            <Alert variant="light" className="shadow-sm p-4 border rounded-4 admin-header text-center">
                <h4 className="fw-bold mb-0">
                    Bienvenido, Administrador
                </h4>
            </Alert>


            <div className="d-flex justify-content-between align-items-center mb-4 mt-4">
                <h2 className="fw-bold">Panel de Administración</h2>
                {!showForm && (
                    <Button variant="primary" onClick={handleAddNew}>
                        + Agregar producto
                    </Button>
                )}
            </div>
                </center>

            {alert.show && (
                <Alert variant={alert.variant} className="rounded-3 shadow-sm text-center">
                    {alert.message}
                </Alert>
            )}

            <center>
            <Row className="justify-content-center">
                <Col lg={8} md={10}>
                    <Card className="shadow-sm border-0 rounded-4">
                        <Card.Body>
                            {showForm ? (
                                <>
                                    <h5 className="fw-bold mb-3 text-center">{editingProduct ? "Editar Producto" : "Agregar Nuevo Producto"}</h5>
                                    <Form onSubmit={handleSubmit} className="mx-auto">
                                        <Row>
                                            <Col md={6}>
                                                <Form.Group className="mb-3">
                                                    <Form.Label>Nombre *</Form.Label>
                                                    <Form.Control
                                                        placeholder="Nombre del producto"
                                                        required
                                                        name="nombre"
                                                        value={formData.nombre}
                                                        onChange={handleInputChange}
                                                    />
                                                </Form.Group>
                                            </Col>

                                            <Col md={6}>
                                                <Form.Group className="mb-3">
                                                    <Form.Label>Precio *</Form.Label>
                                                    <Form.Control
                                                        type="number"
                                                        min="0.01"
                                                        step="0.01"
                                                        placeholder="0.00"
                                                        required
                                                        name="precio"
                                                        value={formData.precio}
                                                        onChange={handleInputChange}
                                                    />
                                                </Form.Group>
                                            </Col>
                                        </Row>

                                        <Form.Group className="mb-3">
                                            <Form.Label>Descripción *</Form.Label>
                                            <Form.Control
                                                as="textarea"
                                                rows={3}
                                                placeholder="Descripción del producto (mínimo 10 caracteres)"
                                                name="descripcion"
                                                value={formData.descripcion}
                                                onChange={handleInputChange}
                                                required
                                            />
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
                                                <Form.Group className="mb-3">
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
                                                <Form.Group className="mb-3">
                                                    <Form.Label>Color *</Form.Label>
                                                    <Form.Select required name="color" value={formData.color} onChange={handleInputChange}>
                                                        <option value="">Seleccionar</option>
                                                        {coloresDisponibles.map(c => (
                                                            <option key={c} value={c}>{c}</option>
                                                        ))}
                                                    </Form.Select>
                                                </Form.Group>
                                            </Col>
                                        </Row>

                                        <Row>
                                            <Col md={6}>
                                                <Form.Group className="mb-3">
                                                    <Form.Label>Stock *</Form.Label>
                                                    <Form.Control
                                                        type="number"
                                                        min="0"
                                                        placeholder="Cantidad disponible"
                                                        required
                                                        name="stock"
                                                        value={formData.stock}
                                                        onChange={handleInputChange}
                                                    />
                                                </Form.Group>
                                            </Col>

                                            <Col md={6}>
                                                <Form.Group className="mb-3">
                                                    <Form.Label>URL de Imagen</Form.Label>
                                                    <Form.Control
                                                        placeholder="https://ejemplo.com/imagen.png"
                                                        name="imagenUrl"
                                                        value={formData.imagenUrl}
                                                        onChange={handleInputChange}
                                                    />
                                                    <Form.Text className="text-muted">
                                                        Solo imágenes válidas: jpg, jpeg, png, webp
                                                    </Form.Text>
                                                </Form.Group>
                                            </Col>
                                        </Row>

                                        <div className="d-flex justify-content-center mt-3">
                                            <Button variant="secondary" onClick={handleCancel} className="me-3">Cancelar</Button>
                                            <Button variant="primary" type="submit">{editingProduct ? "Actualizar" : "Crear"} Producto</Button>
                                        </div>
                                    </Form>
                                </>
                            ) : (
                                <>
                                    <h5 className="fw-bold mb-3 text-center">Productos Registrados ({productos.length})</h5>
                                    {loading ? (
                                        <div className="text-center py-4">
                                            <div className="spinner-border text-warning"></div>
                                        </div>
                                    ) : (
                                        <div className="table-responsive">
                                            <Table hover className="align-middle text-center">
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
                                                            <Button size="sm" variant="outline-danger" onClick={() => openConfirmDelete(producto)}>Eliminar</Button>
                                                        </td>
                                                    </tr>
                                                ))}
                                                </tbody>
                                            </Table>
                                        </div>
                                    )}
                                </>
                            )}
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
            </center>

            {/* Modal de confirmación de eliminación */}
            <Modal show={confirmDelete.show} onHide={closeConfirmDelete} centered>
                <Modal.Header closeButton>
                    <Modal.Title>Confirmar Eliminación</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {confirmDelete.producto &&
                        `¿Estás seguro de eliminar la camiseta "${confirmDelete.producto.nombre}"? Esta acción es irreversible.`}
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={closeConfirmDelete}>Cancelar</Button>
                    <Button variant="danger" onClick={handleConfirmDelete}>Eliminar</Button>
                </Modal.Footer>
            </Modal>
        </Container>
    );
}

export default AdminPanel;