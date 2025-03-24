
-- initialize dev values for the db for testing, etc

-- clear existing data (if needed)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE permission;
TRUNCATE TABLE useraccount;
TRUNCATE TABLE employee;
TRUNCATE TABLE client;
TRUNCATE TABLE useraccount_permissions;
TRUNCATE TABLE guest_upgrade_request;
SET FOREIGN_KEY_CHECKS = 1;

-- set up permissions
INSERT INTO permission (id, permission_type, description) VALUES
(1, 'VIEW_ACCOUNT', 'View the details of you UserAccount.'),
(2, 'VIEW_ACCOUNTS', 'View all UserAccounts and details.'),
(3, 'EDIT_MY_DETAILS', 'Edits the account details of this specific UserAccount instance.'),
(4, 'UPDATE_MY_PASSWORD', 'Updates the stored password for this UserAccount''s own password.'),
(5, 'CREATE_USER', 'Creates a new UserAccount.'),
(6, 'EDIT_USER', 'Edits the details of a specific UserAccount.'),
(7, 'DELETE_USER', 'Removes a UserAccount from the system.'),
(8, 'EDIT_EMPLOYEE', 'Edit the details of a specific Employee account instance.'),
(9, 'CREATE_EMPLOYEE', 'Creates a new Employee account instance.'),
(10, 'UPDATE_OTHER_PASSWORD', 'Updates the stored password for the target UserAccount.'),
(11, 'CREATE_CLIENT', 'Creates a client account from by upgrading an existing Guest account.'),
(12, 'EDIT_CLIENT', 'Edits the details of an existing Client account.'),
(13, 'VIEW_CLIENT', 'Views the details of a specific Client account instance.'),
(14, 'VIEW_CLIENTS', 'Lists the details of all Client account instances'),
(15, 'ASSIGN_CLIENT', 'Assigns an individual Client account instance to an Employeee partner.'),
(16, 'CREATE_INVESTMENT', 'Creates a new investment for a specific Client account instance.'),
(17, 'EDIT_INVESTMENT', 'Edits an existing investment for a specific Client account instance.'),
(18, 'VIEW_EMPLOYEES', 'Lists the details of all Employee account instances.'),
(19, 'VIEW_EMPLOYEE', 'Lists the details of a specific Employee account instance.'),
(20, 'VIEW_INVESTMENT', 'Views the details of a specific investment for this Client account instance.'),
(21, 'REQUEST_CLIENT_ACCOUNT', 'Request upgrade to Client account status with the firm.');

-- set up user accounts
-- ACCOUNT PASSWORDS: are bcrypt-encoded string 'password'
INSERT INTO useraccount (user_id, email, hashed_password, first_name, last_name, role) VALUES
(1, 'admin@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Admin', 'User', 'admin'),
(2, 'employee@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'John', 'Smith', 'employee'),
(3, 'client@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Jane', 'Doe', 'client'),
(4, 'guest@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Guest', 'User', 'guest');

-- admin has all 
INSERT INTO useraccount_permissions (user_id, permission_id)
SELECT 1, id FROM permission;

-- employee permissions
INSERT INTO useraccount_permissions (user_id, permission_id) VALUES
(2, 1), -- VIEW_ACCOUNT
(2, 3), -- EDIT_MY_DETAILS
(2, 4), -- UPDATE_MY_PASSWORD
(2, 5), -- CREATE_USER
(2, 11), -- CREATE_CLIENT
(2, 12), -- EDIT_CLIENT
(2, 13), -- VIEW_CLIENT
(2, 14), -- VIEW_CLIENTS
(2, 15), -- ASSIGN_CLIENT
(2, 16), -- CREATE_INVESTMENT
(2, 17), -- EDIT_INVESTMENT
(2, 20); -- VIEW_INVESTMENT

-- client permissions
INSERT INTO useraccount_permissions (user_id, permission_id) VALUES
(3, 1), -- VIEW_ACCOUNT
(3, 3), -- EDIT_MY_DETAILS
(3, 4), -- UPDATE_MY_PASSWORD
(3, 5), -- CREATE_USER
(3, 20); -- VIEW_INVESTMENT

-- guest permissions
INSERT INTO useraccount_permissions (user_id, permission_id) VALUES
(4, 1), -- VIEW_ACCOUNT
(4, 3), -- EDIT_MY_DETAILS
(4, 4), -- UPDATE_MY_PASSWORD
(4, 5), -- CREATE_USER
(4, 21); -- REQUEST_CLIENT_ACCOUNT

-- creating test records:
INSERT INTO employee (id, employee_id, user_id, location_id, title) VALUES
(1, 'E10001', 2, 'USA', 'Financial Advisor');

INSERT INTO client (id, client_id, user_id, assigned_employee_id) VALUES
(1, 'C10001', 3, 1);

INSERT INTO guest_upgrade_request (id, user_account_id, request_date, status, details) VALUES
(1, 4, NOW(), 'PENDING', 'I would like to become a client to manage my investments.');
