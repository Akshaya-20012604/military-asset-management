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

-- seed minimal roles and a demo user (password: demo123)
INSERT INTO roles (name) VALUES ('ROLE_ADMIN'), ('ROLE_BASE_COMMANDER'), ('ROLE_LOGISTICS_OFFICER');

INSERT INTO bases (code, name, location) VALUES ('BASE001','Alpha Base','Unknown');

INSERT INTO equipment_types (code, name, serialized) VALUES ('AMMO_9MM','9mm Ammunition', false), ('VEH_4x4','4x4 Utility Vehicle', true);

-- bcrypt for "demo123" (example dev hash – keep as-is for now)
INSERT INTO users (username, name, password_hash, base_id)
VALUES ('admin','Administrator','$2a$10$7Qm7G7a8dD8j9gS/2vJbIuVYpVJH2xkYvRC8m1oCI0a6KZq1QeE7W', 1);

INSERT INTO user_roles (user_id, role_id) VALUES (1,1);
