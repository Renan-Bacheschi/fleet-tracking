CREATE TABLE vehicles (
    id UUID NOT NULL,
    license_plate VARCHAR(7) NOT NULL,
    fleet_code VARCHAR(50) NOT NULL,
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    model_year INTEGER NOT NULL,
    type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT pk_vehicles PRIMARY KEY (id),
    CONSTRAINT uk_vehicles_license_plate UNIQUE (license_plate),
    CONSTRAINT uk_vehicles_fleet_code UNIQUE (fleet_code),
    CONSTRAINT ck_vehicles_model_year CHECK (model_year >= 1900),
    CONSTRAINT ck_vehicles_type CHECK (type IN ('TRUCK', 'VAN', 'CAR', 'MOTORCYCLE')),
    CONSTRAINT ck_vehicles_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'MAINTENANCE'))
);
