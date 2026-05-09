# NorthPay Onboarding Portal

Portal de onboarding para contratistas remotos y panel operativo de NorthPay.

## Qué incluye

- Flujo de onboarding en 5 pasos:
  1. Datos personales
  2. Subida de documentos
  3. Firma de contrato
  4. Configuración de método de pago
  5. Verificación de identidad
- Panel de administración con visibilidad de estados y acciones de aprobación / corrección
- Notificaciones en pantalla para cada cambio de estado

## Estructura principal

- `app/page.tsx`: página de inicio del portal
- `app/onboarding/page.tsx`: flujo de onboarding para nuevos contratistas
- `app/admin/page.tsx`: panel de operaciones y lista de onboardings
- `app/layout.tsx`: configuración global del portal

## Ejecutar localmente

```bash
npm install
npm run dev
```

Después, abre `http://localhost:3000`.

## Próximos pasos

- Integrar backend para persistir datos de contratistas
- Añadir autenticación para contratistas y operadores
- Conectar notificaciones reales por email / SMS
- Implementar subida segura de documentos y firma digital real
