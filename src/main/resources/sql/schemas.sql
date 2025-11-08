BEGIN;

-- =================================================================
-- == DESTRUCCIÓN DE ESTRUCTURA ANTERIOR (para reinicio)
-- =================================================================
-- Borra todas las tablas en orden inverso de dependencia
-- CASCADE elimina automáticamente las dependencias (FKs, índices)

DROP TABLE IF EXISTS product_images CASCADE;
DROP TABLE IF EXISTS gallery_images CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS negocios CASCADE;
DROP TABLE IF EXISTS artesano_details CASCADE;
DROP TABLE IF EXISTS artesano_profiles CASCADE; -- (Tabla antigua por si existe)
DROP TABLE IF EXISTS users CASCADE;

-- Borra los tipos ENUM
DROP TYPE IF EXISTS user_role;
DROP TYPE IF EXISTS region_huila;

-- =================================================================
-- == CREACIÓN DE NUEVA ESTRUCTURA
-- =================================================================

-- 1. Definir Tipos Enum (Listas controladas)
-- **Rol 'LOGISTICA' añadido**

CREATE TYPE user_role AS ENUM (
  'CLIENTE',
  'ARTESANO',
  'LOGISTICA'
);

CREATE TYPE region_huila AS ENUM (
  'NORTE',
  'SUR',
  'ESTE',
  'OESTE'
);

-- 2. Tabla de Categorías (Módulo 4)
-- (Sin cambios)

CREATE TABLE categories (
    category_id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    created_at TIMESTAMPTZ DEFAULT now()
);

-- 3. Tabla de Usuarios (Módulo 1)
-- Tabla principal de autenticación
-- ** 'role' actualizado con 'LOGISTICA' **

CREATE TABLE users (
    user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    telefono VARCHAR(20),
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    nombre VARCHAR(150) NOT NULL,

    -- Campos añadidos --
    fecha_nacimiento DATE,
    lugar_residencia VARCHAR(255),
    direccion TEXT,

    -- Campos existentes --
    foto_perfil_url TEXT,
    role user_role NOT NULL,
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ DEFAULT now()
);

-- 4. Detalles Personales del Artesano (Módulo 3)
-- **NUEVA TABLA** (1-a-1 con users)
-- Almacena la "Historia de Vida", que es personal del artesano, no del negocio.

CREATE TABLE artesano_details (
    artesano_detail_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL UNIQUE REFERENCES users(user_id) ON DELETE CASCADE,
    historia_vida TEXT, -- RF009 (El Alma)
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ DEFAULT now()
);

-- 5. Tabla de Negocios (Módulo 2)
-- **NUEVA TABLA** (Reemplaza a 'artesano_profiles')
-- Un ARTESANO puede tener varios NEGOCIOS (1-a-N)
-- Un LOGISTICA puede gestionar varios NEGOCIOS (1-a-N)

CREATE TABLE negocios (
    negocio_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    artesano_user_id UUID NOT NULL REFERENCES users(user_id) ON DELETE CASCADE, -- El Artesano dueño
    logistica_user_id UUID REFERENCES users(user_id) ON DELETE SET NULL, -- El gestor (opcional)
    nombre_negocio VARCHAR(255) NOT NULL,
    descripcion_taller TEXT,
    metodos_pago_info TEXT,
    whatsapp VARCHAR(20),
    email_publico VARCHAR(255),
    instagram_url VARCHAR(255),
    facebook_url VARCHAR(255),
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ DEFAULT now()
);

-- 6. Galería del Taller/Negocio (Módulo 2)
-- **Actualizado** para apuntar a 'negocios' en lugar de 'artesano_profiles'

CREATE TABLE gallery_images (
    gallery_image_id BIGSERIAL PRIMARY KEY,
    negocio_id UUID NOT NULL REFERENCES negocios(negocio_id) ON DELETE CASCADE,
    image_url TEXT NOT NULL,
    caption VARCHAR(255),
    uploaded_at TIMESTAMPTZ DEFAULT now()
);

-- 7. Tabla de Productos (Módulo 4)
-- **Actualizado** para apuntar a 'negocios' en lugar de 'artesano_profiles'

CREATE TABLE products (
    product_id BIGSERIAL PRIMARY KEY,
    negocio_id UUID NOT NULL REFERENCES negocios(negocio_id) ON DELETE CASCADE,
    category_id BIGINT REFERENCES categories(category_id) ON DELETE SET NULL,
    nombre VARCHAR(255) NOT NULL,
    descripcion_detallada TEXT,
    precio NUMERIC(10, 2) NOT NULL, -- Precio referencial
    region region_huila, -- RF011
    ofrece_logistica_opcional BOOLEAN DEFAULT false, -- RF011
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ DEFAULT now()
);

-- 8. Imágenes de Productos (Módulo 4)
-- (Sin cambios, depende de 'products')

CREATE TABLE product_images (
    product_image_id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES products(product_id) ON DELETE CASCADE,
    image_url TEXT NOT NULL,
    is_primary BOOLEAN DEFAULT false, -- Para la foto de portada del producto
    uploaded_at TIMESTAMPTZ DEFAULT now()
);

-- 9. Índices para mejorar rendimiento
-- **Actualizados** para la nueva estructura

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_artesano_details_user_id ON artesano_details(user_id);
CREATE INDEX idx_negocios_artesano_user_id ON negocios(artesano_user_id);
CREATE INDEX idx_negocios_logistica_user_id ON negocios(logistica_user_id);
CREATE INDEX idx_gallery_images_negocio_id ON gallery_images(negocio_id);
CREATE INDEX idx_products_negocio_id ON products(negocio_id);
CREATE INDEX idx_products_category_id ON products(category_id);
CREATE INDEX idx_products_region ON products(region);
CREATE INDEX idx_product_images_product_id ON product_images(product_id);

COMMIT;