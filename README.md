# inventario-productos-service

Servicio Spring Boot para consultar stock de productos terminados y registrar entradas, salidas y ajustes dentro del negocio activo. Usa el esquema `rm_detalles` que ya existe; no crea ni elimina tablas. Puerto local: `8085`.

## Operación y reglas

- Toda petición protegida requiere un JWT de Supabase y el encabezado `X-Negocio-Codigo`.
- Todas las consultas y escrituras se filtran por el UUID del negocio resuelto desde ese encabezado y la membresía del usuario.
- Solo se incluyen productos activos con `controla_stock=true`.
- El código del tipo de movimiento y su `factor_stock` se resuelven en `tipos_movimiento_producto` para el negocio actual. El cliente envía una cantidad positiva; el servicio aplica el factor configurado (`1` suma, `-1` resta).
- Stock inicial inexistente se considera `0`. El primer movimiento crea la fila de inventario y el saldo.
- El saldo y el movimiento se guardan en la misma transacción. Se bloquea la fila de inventario para serializar movimientos simultáneos.
- Se rechazan salidas que dejen stock negativo. La tabla impone `stock_actual >= 0`; por eso `permite_negativo` no puede permitir saldos negativos sin cambiar el esquema.
- Cada movimiento conserva usuario, motivo y referencia opcional. La referencia permite enlazar más adelante un movimiento con una venta.
- Los tipos estándar se inicializan con el SQL de recursos. Su configuración administrativa debe manejarse desde `configuracion-service`, no desde este servicio.

## Preparación

1. Usa Java 21 e importa el proyecto como Maven en IntelliJ.
2. Configura `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `SUPABASE_ISSUER_URI` y `SUPABASE_JWK_SET_URI` igual que en los otros microservicios.
3. Ejecuta una vez, en Supabase SQL Editor, `src/main/resources/db/seed_inventario_productos_defaults.sql`. Es idempotente: agrega los tipos que falten por negocio y permisos a roles existentes solo cuando no hay una asignación guardada; no elimina ni reemplaza valores existentes.
4. Inicia `InventarioProductosServiceApplication`. Hibernate usa `ddl-auto: validate`.
5. Para probar, usa un producto activo del negocio con `controla_stock=true`. El producto de prueba previo tenía ese campo en `false`; actualízalo desde catálogo o crea uno de prueba antes.

## Endpoints

| Método | Ruta | Permiso | Función |
|---|---|---|---|
| GET | `/actuator/health` | Público | Salud del servicio |
| GET | `/api/v1/inventario/productos` | `INVENTARIO_CONSULTAR` | Lista productos con stock controlado |
| GET | `/api/v1/inventario/productos/{productoId}` | `INVENTARIO_CONSULTAR` | Consulta el saldo de un producto |
| GET | `/api/v1/inventario/productos/{productoId}/movimientos` | `INVENTARIO_CONSULTAR` | Consulta su historial |
| POST | `/api/v1/inventario/productos/{productoId}/movimientos` | `INVENTARIO_GESTIONAR` | Registra entrada, salida o ajuste |

Tipos iniciales: `ENTRADA`, `SALIDA`, `AJUSTE_POSITIVO`, `AJUSTE_NEGATIVO`. Enviar una cantidad positiva en todos los casos; el factor del tipo determina si suma o resta.

## Prueba en Postman

Configura Bearer Token `{{TOKEN}}` y `X-Negocio-Codigo: RMMODA`.

1. Lista existencias: `GET http://localhost:8085/api/v1/inventario/productos`.
2. Para establecer stock inicial, registra una entrada:

`POST http://localhost:8085/api/v1/inventario/productos/{{PRODUCTO_ID}}/movimientos`

```json
{
  "tipoCodigo": "ENTRADA",
  "cantidad": 10,
  "motivo": "Stock inicial de prueba"
}
```

Debe devolver `201 Created`, `variacionStock: 10` y `stockActual: 10`.

3. Registra una salida de 3 unidades. El saldo debe quedar en 7:

```json
{
  "tipoCodigo": "SALIDA",
  "cantidad": 3,
  "motivo": "Prueba de salida"
}
```

4. Intenta una salida mayor al saldo; debe responder `409 Conflict` y no cambiar el saldo ni guardar movimiento.
5. Consulta `GET /api/v1/inventario/productos/{{PRODUCTO_ID}}/movimientos` y confirma que aparecen la entrada y salida.
6. Repite las consultas con `X-Negocio-Codigo: RMD`; el inventario de RMMODA debe responder como no encontrado en la consulta por ID, y no aparecer en la lista de RMD.

El servicio no descuenta stock automáticamente al crear o cambiar de estado un pedido. La integración de ventas con inventario se hará cuando exista `ventas-service`.
