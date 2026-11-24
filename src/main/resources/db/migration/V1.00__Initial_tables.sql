CREATE TABLE users
(
    id               BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    first_name       VARCHAR(150) NOT NULL,
    last_name        VARCHAR(150) NOT NULL,
    email            VARCHAR(150) NOT NULL,
    username         VARCHAR(50)  NOT NULL,
    password         VARCHAR(100) NOT NULL,
    enabled          BOOLEAN      NOT NULL DEFAULT TRUE,
    created_by       VARCHAR(100) NOT NULL,
    last_modified_by VARCHAR(100) NULL,
    created_at       TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    last_modified_at TIMESTAMP NULL,
    CONSTRAINT uq_username UNIQUE (username),
    CONSTRAINT uq_email UNIQUE (email)
);


CREATE TABLE roles
(
    id               BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    name             VARCHAR(100) NOT NULL,
    created_by       VARCHAR(100) NOT NULL,
    last_modified_by VARCHAR(100) NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_at TIMESTAMP NULL
);

CREATE TABLE user_roles
(
    user_id BIGINT(20) NOT NULL,
    role_id BIGINT(20) NOT NULL,
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_role_id FOREIGN KEY (role_id) REFERENCES roles (id),
    CONSTRAINT pk_user_role PRIMARY KEY (user_id, role_id)
);

CREATE TABLE refresh_tokens
(
    id              BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    user_id         BIGINT(20) NOT NULL,
    token           VARCHAR(255) NOT NULL,
    expiration_date TIMESTAMP    NOT NULL,
    revoked         BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_refresh_token FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT uq_token UNIQUE (token)
)