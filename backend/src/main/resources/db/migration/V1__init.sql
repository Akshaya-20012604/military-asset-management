-- BASES
CREATE TABLE bases (
  id BIGSERIAL PRIMARY KEY,
  code VARCHAR(32) UNIQUE NOT NULL,
  name TEXT NOT NULL,
  location TEXT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- EQUIPMENT TYPES
CREATE TABLE equipment_types (
  id BIGSERIAL PRIMARY KEY,
  code VARCHAR(64) UNIQUE NOT NULL,
  name TEXT NOT NULL,
  serialized BOOLEAN DEFAULT false,
  unit_of_measure VARCHAR(16) DEFAULT 'qty',
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- ASSET UNITS
CREATE TABLE asset_units (
  id BIGSERIAL PRIMARY KEY,
  equipment_type_id BIGINT NOT NULL REFERENCES equipment_types(id) ON DELETE CASCADE,
  serial VARCHAR(128) NOT NULL,
  status VARCHAR(32) DEFAULT 'available',
  base_id BIGINT REFERENCES bases(id),
  metadata JSONB,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- INVENTORY MOVEMENTS
CREATE TABLE inventory_movements (
  id BIGSERIAL PRIMARY KEY,
  kind VARCHAR(32) NOT NULL,
  equipment_type_id BIGINT NOT NULL REFERENCES equipment_types(id),
  quantity INT NOT NULL CHECK (quantity >= 0),
  from_base_id BIGINT REFERENCES bases(id),
  to_base_id BIGINT REFERENCES bases(id),
  asset_unit_id BIGINT REFERENCES asset_units(id),
  reference TEXT,
  notes TEXT,
  timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  created_by BIGINT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- ROLES
CREATE TABLE roles (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(64) UNIQUE NOT NULL
);

-- USERS
CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  username VARCHAR(128) UNIQUE NOT NULL,
  name TEXT,
  password_hash TEXT NOT NULL,
  base_id BIGINT REFERENCES bases(id),
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- USER_ROLES
CREATE TABLE user_roles (
  user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
  role_id BIGINT REFERENCES roles(id) ON DELETE CASCADE,
  PRIMARY KEY (user_id, role_id)
);

-- AUDIT LOGS
CREATE TABLE audit_logs (
  id BIGSERIAL PRIMARY KEY,
  table_name TEXT,
  record_id BIGINT,
  action TEXT,
  payload JSONB,
  performed_by BIGINT,
  performed_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- ============================================
-- SEED DATA (FIXED)
-- ============================================

-- Fix sequences explicitly
ALTER SEQUENCE bases_id_seq RESTART WITH 2;
ALTER SEQUENCE roles_id_seq RESTART WITH 4;
ALTER SEQUENCE users_id_seq RESTART WITH 2;

-- Insert base with explicit ID=1
INSERT INTO bases (id, code, name, location)
VALUES (1, 'BASE001','Alpha Base','Unknown');

-- Equipment
INSERT INTO equipment_types (id, code, name, serialized)
VALUES (1, 'AMMO_9MM','9mm Ammunition', false),
       (2, 'VEH_4x4','4x4 Utility Vehicle', true);

-- Roles
INSERT INTO roles (id, name)
VALUES (1,'ROLE_ADMIN'),
       (2,'ROLE_BASE_COMMANDER'),
       (3,'ROLE_LOGISTICS_OFFICER');

-- Admin user (password demo123)
INSERT INTO users (id, username, name, password_hash, base_id)
VALUES (1,'admin','Administrator','$2a$10$7Qm7G7a8dD8j9gS/2vJbIuVYpVJH2xkYvRC8m1oCI0a6KZq1QeE7W', 1);

INSERT INTO user_roles (user_id, role_id)
VALUES (1,1);
