# 🚀 NorthPay - Contractor Onboarding Portal

Plataforma centralizada para la gestión y automatización del alta de contratistas internacionales, optimizando los tiempos de cumplimiento y activación.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-green)
![React](https://img.shields.io/badge/React-19.2.5-blue)
![Next.js](https://img.shields.io/badge/Next.js-16.2.4-black)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18.3-blue)
![Docker](https://img.shields.io/badge/Docker-Ready-blue)

## 📋 Problema
Actualmente, el proceso de activación ocurre vía canales dispersos (email, formularios offline y WhatsApp), resultando en un tiempo promedio de 12 días, donde 8 se pierden en trámites burocráticos.

**Nuestra Solución:** Centralizar el onboarding en un portal web que reduce el tiempo de activación a menos de 3 días, brindando visibilidad operativa total al equipo interno.

---

## ✨ Características

### 👤 Flujo del Contratista (5 Pasos)
* **Datos Personales**: Registro completo de perfil e información de contacto.
* **Carga de Documentos**: Subida segura de identificaciones y certificaciones requeridas.
* **Firma de Contrato**: Integración para la firma digital de acuerdos legales.
* **Configuración de Pagos**: Configuración de métodos de cobro internacionales.
* **Verificación de Identidad**: Validación final de seguridad antes de la activación.

### 👥 Panel de Administración (Ops Team)
* **Monitoreo en Tiempo Real**: Visualización del progreso y estado de cada contratista.
* **Gestión Documental**: Revisión, aprobación o solicitud de correcciones de archivos.
* **Notificaciones Automáticas**: Alertas al usuario ante cada cambio de estado en el flujo.

---

## 🛠️ Stack Tecnológico

| Componente | Tecnología |
|------------|------------|
| **Frontend** | Next.js 16.2.4, React 19.2.5, TailwindCSS, shadcn/ui |
| **Backend** | Spring Boot 4.0.6 (Java 21), Spring Security + JWT |
| **Base de Datos** | PostgreSQL 18.3 |
| **Infraestructura** | Docker, WebSockets |

---

## 🔄 Flujo de Trabajo (Workflow)
- Invitación: El contratista ingresa al portal mediante un link único.

- Carga: Completa secuencialmente los 5 pasos obligatorios.

- Revisión: El operador interno visualiza el proceso en estado "Pending Verification".

- Resolución: El operador aprueba el onboarding o solicita correcciones específicas.

- Activación: Se dispara una notificación y la cuenta queda habilitada para operar.

---

## 🚀 Instalación Rápida (Docker)

### Prerrequisitos
* Docker Desktop (incluye Docker Compose).
* Java 21 JDK (para desarrollo local).
* Node.js (versión 20 o superior para Next.js 16).

### 1. Clonar el repositorio
```bash
git clone https://github.com/No-Country-simulation/S04-26-Equipo-06-NorthPay.git
```

### 2. Configurar variables de entorno
```bash
cp .env.example .env
```

### 3. Ejecutar con Docker
```bash
# Desarrollo (con hot-reload)
sudo docker compose up --build
```

```bash
# Producción
sudo docker compose -f docker-compose.prod.yml up --build
```

### 4. Acceder
* Frontend: http://localhost:3000

* Backend API: http://localhost:8080/api

* Swagger: http://localhost:8080/swagger-ui.html

---

## 🤝 Contribuir
1. Fork el repositorio.

2. Crea una rama (git checkout -b feature/nueva-funcionalidad).

3. Commit tus cambios (git commit -m 'feat: agregar nueva funcionalidad').

4. Push a la rama (git push origin feature/nueva-funcionalidad).

5. Abre un Pull Request.

---

## 👥 Equipo
Desarrollado para No Country - Simulación laboral tech.

- Camila Barreiro. - Product Owner
- Jorge Subeldia. - DevOps
- Nahuel Gómez Gahn. - Backend
- Matías Almaraz. - Backend
- Marcos Fernández. - Backend
- Martín Kun. - Frontend
- Damián Benítez - QA

---

📄 Licencia
Este proyecto está bajo la Licencia MIT


