CREATE TABLE vote (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        user_id_cpf VARCHAR(11) NOT NULL,
        vote ENUM('SIM', 'NAO') NOT NULL,
    	pauta_id BIGINT NOT NULL,
    	created_at DATETIME NOT NULL,
    	FOREIGN KEY (pauta_id) REFERENCES pauta(id)
);

CREATE INDEX user_id_idx ON vote(user_id_cpf);