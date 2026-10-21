INSERT INTO support_orders (external_reference, customer_id, category, description, channel, priority, status, created_at, updated_at) VALUES
('MOB-1001', 'CUST-001', 'PAYMENT', 'El pago aparece como pendiente después de varios minutos.', 'MOBILE_APP', 'HIGH', 'OPEN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MOB-1002', 'CUST-002', 'LOGIN', 'No puedo iniciar sesión desde ayer en la aplicación.', 'MOBILE_APP', 'MEDIUM', 'IN_PROGRESS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MOB-1003', 'CUST-003', 'DELIVERY', 'El pedido llegó incompleto y falta un artículo.', 'MOBILE_APP', 'LOW', 'RESOLVED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MOB-1004', 'CUST-001', 'OTHER', 'Solicitud duplicada, se cancela por el usuario.', 'WEB', 'LOW', 'CANCELLED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MOB-1005', 'CUST-004', 'PAYMENT', 'Me cobraron dos veces la misma suscripción mensual.', 'MOBILE_APP', 'HIGH', 'OPEN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
