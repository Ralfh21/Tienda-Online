import React, { useState, useEffect, useCallback } from "react";
import { Container, Button } from "react-bootstrap";
import { Link } from "react-router-dom";
import { productoService, categoriaService } from "../services/api";
import ProductCard from "../components/ProductCard";
import "../App.css";

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

      const productos = productosResponse?.data || [];
      const categoriasList = categoriasResponse?.data || [];

      // ✅ Filtrar solo productos con stock > 0 y mostrar los primeros 6
      const productosDisponibles = productos.filter((p) => p.stock > 0);
      setProductosDestacados(productosDisponibles.slice(0, 6));

      // ✅ Eliminar categorías duplicadas por nombre
      const categoriasUnicas = Array.from(
        new Map(categoriasList.map((cat) => [cat.nombre, cat])).values(),
      );

      setCategorias(categoriasUnicas);
    } catch (error) {
      console.error("Error al cargar datos:", error);
      setProductosDestacados([]);
      setCategorias([]);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    cargarDatos();
  }, [cargarDatos]);

  return (
    <Container fluid className="home-container px-3 py-4">
      {/* HERO */}
      <section className="hero-section text-center mb-4">
        <h1 className="fw-bold mb-2">Bienvenido a StyleHub</h1>
        <p className="text-muted mb-3">Moda premium con los mejores precios</p>
        <Link to="/productos">
          <Button variant="primary" className="me-2">
            Explorar Productos
          </Button>
        </Link>
        <Link to="/productos">
          <Button variant="outline-primary">Ver Colección</Button>
        </Link>
      </section>

      {/* CATEGORÍAS */}
      <section className="categories-section mb-4">
        <h2 className="fw-bold text-center mb-3">Categorías</h2>
        {categorias.length > 0 ? (
          <div className="categories-grid">
            {categorias.map((categoria) => (
              <Link
                key={categoria.id}
                to={`/productos?categoria=${categoria.nombre}`}
                className="text-decoration-none"
              >
                <div className="category-card">
                  <h5>{categoria.nombre}</h5>
                  <p className="text-muted">
                    Explorar {categoria.nombre.toLowerCase()}
                  </p>
                </div>
              </Link>
            ))}
          </div>
        ) : (
          <p className="text-center text-muted">
            No hay categorías disponibles
          </p>
        )}
      </section>

      {/* PRODUCTOS DESTACADOS */}
      <section className="featured-section">
        <div className="text-center mb-3">
          <h2 className="fw-bold mb-1">Productos Destacados</h2>
          <p className="text-muted small">
            Los más populares de nuestra tienda
          </p>
        </div>

        {loading ? (
          <div className="text-center py-4">
            <div className="spinner-border text-secondary" role="status">
              <span className="visually-hidden">Cargando...</span>
            </div>
          </div>
        ) : productosDestacados.length > 0 ? (
          <div className="products-grid">
            {productosDestacados.map((producto) => (
              <div key={producto.id} className="product-cell">
                <ProductCard producto={producto} />
              </div>
            ))}
          </div>
        ) : (
          <div className="text-center py-4">
            <p className="text-muted">No hay productos disponibles</p>
            <Link to="/admin">
              <Button variant="outline-primary" size="sm">
                Administrar Productos
              </Button>
            </Link>
          </div>
        )}
      </section>
    </Container>
  );
}

export default Home;
