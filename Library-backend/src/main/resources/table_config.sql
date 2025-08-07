CREATE TABLE app_user (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('ROLE_USER', 'ROLE_ADMIN')),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT username_length_check CHECK (LENGTH(username) >= 3),
    CONSTRAINT password_length_check CHECK (LENGTH(password) >= 8)
);

CREATE INDEX idx_app_user_username ON app_user(username);

CREATE TABLE book (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    year INTEGER NOT NULL CHECK (year > 0 AND year <= EXTRACT(YEAR FROM CURRENT_DATE)),
    genre VARCHAR(100),
    language VARCHAR(50) DEFAULT 'Russian',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    person_id INTEGER REFERENCES person(id) ON DELETE SET NULL
);

CREATE INDEX idx_book_name ON book(name);
CREATE INDEX idx_book_person_id ON book(person_id);

CREATE TABLE person (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    birth_date DATE NOT NULL CHECK (birth_date > '1900-01-01' AND birth_date <= CURRENT_DATE),
    phone_number VARCHAR(20),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT valid_phone_number CHECK (
        phone_number IS NULL OR 
        (phone_number ~ '^[0-9+\-() ]+$' AND LENGTH(phone_number) BETWEEN 5 AND 20)
    )
);

CREATE INDEX idx_person_full_name ON person(full_name);
CREATE INDEX idx_person_birth_date ON person(birth_date);









