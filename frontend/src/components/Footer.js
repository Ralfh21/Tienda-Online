import React from "react";
import { Container, Row, Col } from "react-bootstrap";

function Footer() {
    return (
        <footer className="footer mt-5">
            <Container>

                <Row className="mb-4">

                    {/* CONTACTO */}
                    <Col lg={4} className="mb-4">
                        <center><h5>Contacto</h5></center>

                        <div className="contact-grid">
                            <div className="item">
                                <span className="label">Correo:</span>
                                <a href="mailto:info@tiendaropa.com">info@tiendaropa.com</a>
                            </div>

                            <div className="item center">
                                <span className="label">Tel:</span>
                                <span>099 162 3861</span>
                            </div>

                            <div className="item right">
                                <span>Quito, Ecuador</span>
                            </div>
                        </div>
                    </Col>


                </Row>

                <div className="copyright">
                    © {new Date().getFullYear()} Tienda de Ropa — Todos los derechos reservados.
                </div>

            </Container>
        </footer>
    );
}

export default Footer;
