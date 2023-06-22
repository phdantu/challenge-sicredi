CREATE TABLE pauta (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
    	status ENUM('ABERTA','ENCERRADA'),
    	finishes_at DATETIME,
    	created_at DATETIME NOT NULL,
    	updated_at DATETIME
)