import React, { useState } from 'react';
import { Button, Alert, Card } from 'react-bootstrap';
import { productoService, testConnection } from '../services/api';

function DiagnosticTest() {
    const [result, setResult] = useState(null);
    const [loading, setLoading] = useState(false);

    const testAPI = async () => {
        setLoading(true);
        try {
            console.log('Iniciando test de conexión...');
            
            // Probar con testConnection primero
            const testResult = await testConnection();
            console.log('Resultado testConnection:', testResult);
            
            // Probar directamente con productoService
            const directResult = await productoService.obtenerTodos();
            console.log('Resultado directo:', directResult);
            
            setResult({
                success: true,
                testConnection: testResult,
                directCall: directResult.data,
                message: 'Conexión exitosa! Ver consola para detalles.'
            });
        } catch (error) {
            console.error('Error completo:', error);
            setResult({
                success: false,
                error: error.message,
                details: {
                    name: error.name,
                    code: error.code,
                    response: error.response ? {
                        status: error.response.status,
                        statusText: error.response.statusText,
                        data: error.response.data
                    } : 'No response object'
                }
            });
        } finally {
            setLoading(false);
        }
    };

    return (
        <Card className="m-3">
            <Card.Header>
                <h5>🔧 Test de Conexión Backend</h5>
            </Card.Header>
            <Card.Body>
                <Button 
                    onClick={testAPI} 
                    disabled={loading}
                    variant="primary"
                >
                    {loading ? 'Probando...' : 'Probar Conexión API'}
                </Button>
                
                {result && (
                    <Alert 
                        variant={result.success ? 'success' : 'danger'} 
                        className="mt-3"
                    >
                        <h6>{result.success ? '✅ Éxito' : '❌ Error'}</h6>
                        {result.success ? (
                            <div>
                                <p>{result.message}</p>
                                <small>
                                    Productos encontrados: {result.directCall?.length || 0}
                                </small>
                            </div>
                        ) : (
                            <div>
                                <p><strong>Error:</strong> {result.error}</p>
                                <details>
                                    <summary>Ver detalles técnicos</summary>
                                    <pre>{JSON.stringify(result.details, null, 2)}</pre>
                                </details>
                            </div>
                        )}
                    </Alert>
                )}
            </Card.Body>
        </Card>
    );
}

export default DiagnosticTest;