-- Añade tipos de movimiento estándar a negocios activos cuando falten.
-- No elimina ni modifica tipos o permisos ya existentes.
BEGIN;

INSERT INTO rm_detalles.tipos_movimiento_producto
    (negocio_id, codigo, nombre, factor_stock, permite_negativo, activo)
SELECT n.id, t.codigo, t.nombre, t.factor_stock, FALSE, TRUE
FROM rm_detalles.negocios n
CROSS JOIN (VALUES
    ('ENTRADA', 'Entrada', 1),
    ('SALIDA', 'Salida', -1),
    ('AJUSTE_POSITIVO', 'Ajuste positivo', 1),
    ('AJUSTE_NEGATIVO', 'Ajuste negativo', -1)
) AS t(codigo, nombre, factor_stock)
WHERE n.activo=TRUE
ON CONFLICT (negocio_id,codigo) DO NOTHING;

-- Asigna consulta y gestión a administradores existentes solo cuando no hay
-- decisión guardada para ese par rol-permiso. Los vendedores conservan consulta.
INSERT INTO rm_detalles.rol_permisos (rol_id,permiso_id,concedido)
SELECT r.id,p.id,TRUE
FROM rm_detalles.roles r
JOIN rm_detalles.permisos p ON p.codigo IN ('INVENTARIO_CONSULTAR','INVENTARIO_GESTIONAR')
WHERE r.activo=TRUE AND r.codigo='ADMINISTRADOR'
ON CONFLICT (rol_id,permiso_id) DO NOTHING;

INSERT INTO rm_detalles.rol_permisos (rol_id,permiso_id,concedido)
SELECT r.id,p.id,TRUE
FROM rm_detalles.roles r
JOIN rm_detalles.permisos p ON p.codigo='INVENTARIO_CONSULTAR'
WHERE r.activo=TRUE AND r.codigo='VENDEDOR'
ON CONFLICT (rol_id,permiso_id) DO NOTHING;

COMMIT;
