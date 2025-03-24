-- script to set up tables for the db 
-- run/import this before init-db-script
CREATE TABLE IF NOT EXISTS permission (
  id BIGINT PRIMARY KEY,
  permission_type VARCHAR(255) UNIQUE NOT NULL,
  description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS useraccount (
  user_id BIGINT PRIMARY KEY,
  email VARCHAR(255) UNIQUE,
  hashed_password VARCHAR(255),
  first_name VARCHAR(255),
  last_name VARCHAR(255),
  role VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS useraccount_permissions (
  user_id BIGINT,
  permission_id BIGINT,
  PRIMARY KEY (user_id, permission_id),
  FOREIGN KEY (user_id) REFERENCES useraccount(user_id),
  FOREIGN KEY (permission_id) REFERENCES permission(id)
);

CREATE TABLE IF NOT EXISTS employee (
  id BIGINT PRIMARY KEY,
  employee_id VARCHAR(255) UNIQUE,
  user_id BIGINT UNIQUE,
  location_id VARCHAR(255),
  title VARCHAR(255),
  FOREIGN KEY (user_id) REFERENCES useraccount(user_id)
);

CREATE TABLE IF NOT EXISTS client (
  id BIGINT PRIMARY KEY,
  client_id VARCHAR(255) UNIQUE,
  user_id BIGINT UNIQUE,
  assigned_employee_id BIGINT,
  FOREIGN KEY (user_id) REFERENCES useraccount(user_id),
  FOREIGN KEY (assigned_employee_id) REFERENCES employee(id)
);

CREATE TABLE IF NOT EXISTS guest_upgrade_request (
  id BIGINT PRIMARY KEY,
  user_account_id BIGINT,
  request_date DATETIME,
  status VARCHAR(50),
  details VARCHAR(1000),
  FOREIGN KEY (user_account_id) REFERENCES useraccount(user_id)
);
