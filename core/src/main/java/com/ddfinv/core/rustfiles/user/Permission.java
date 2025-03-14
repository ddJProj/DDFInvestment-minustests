// package com.ddfinv.core.rustfiles.user;

// public class Permission {

// }

/*
 * 
 * 
 * // Permissions for users
 * #[derive(Debug, Clone, Eq, PartialEq, Serialize, Deserialize, Hash)] // Added
 * Hash trait
 * pub enum Permission {
 * ManageUsers, // admin+ permission -
 * ManageSystem, // admin + permission -
 * ManageUserByUserId, // admin + permission -
 * 
 * ViewClientByUserId, // employee+ permission - / db:
 * get_client_by_user_account_id()
 * ViewClientByClientId, // employee+ permission - get_client() / db:
 * ViewAllClients, // employee+ permission - / db: get_clients()
 * 
 * ModifyClientService, // Employee+ permission - update_client() / db:
 * ManageClientRole, // admin+ permission - / db:
 * ManageClientById, // employee+ permission - update_client() / db:
 * update_client()
 * DeleteClientById, // admin+ permission - remove_client() / db:
 * remove_client()
 * AssignClientRole, // admin+ permission - new_client() / db: new_client
 * 
 * ViewAllEmployees, // employee+ permission - / db: get_employees()
 * ViewEmployeeByEmployeeId, // Employee+ permission - get_employee_by_id() /
 * db: get_employee()
 * ViewEmployeeClientPairs, // employee+ permission - get_clients_for_employee()
 * / db:
 * ManageEmployeeRole, // admin+ permission -
 * ManageEmployeeById, // employee+ permission - update_employee() / db:
 * DeleteEmployeeById, // admin+ permission - delete_employee() / db:
 * AssignEmployeeRole, // admin+ permission - add_new_employee() / db:
 * new_employee()
 * 
 * ViewSelfAccountData, // restricted+ permission
 * UpdateProfile, // restricted+ permission
 * 
 * RequestService, //
 * }
 * 
 * 
 */