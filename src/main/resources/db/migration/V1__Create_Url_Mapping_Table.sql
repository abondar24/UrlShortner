CREATE TABLE url_mapping (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              long_url VARCHAR(2048) NOT NULL,
                              short_url VARCHAR(50) NOT NULL,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);