CREATE TABLE IF NOT EXISTS furniture (

    id INT NOT NULL,
    owner VARCHAR(255) Not NULL,
    type VARCHAR(255),
    material VARCHAR(255),
    years_in_storage INT,
    warehouse_output BOOLEAN,
    CONSTRAINT pk_id PRIMARY KEY (id)
);