Asset Management System - Starter Projects
=========================================

This zip contains two projects:

1. backend - Spring Boot (Maven) skeleton
2. frontend - React + Vite (Tailwind) skeleton

Quick start (development):
- Start PostgreSQL and backend using Docker Compose:
  $ docker-compose up --build

Notes:
- Backend will run on http://localhost:8080
- Frontend Vite server will run on http://localhost:5173
- A demo user 'admin' with password 'demo123' is seeded (development only).
- JWT secret is set in application.yml; change it for production.

This skeleton is intentionally minimal. It implements core data model, example endpoints for auth/purchases/transfers, and a small dashboard page. Use it as a starting point and extend with full validation, RBAC enforcement, and tests.
