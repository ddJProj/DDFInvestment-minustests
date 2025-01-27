package com.ddfinv.core.model;

public class FirmModels {
}




/*
// firm_models.rs

//! Defines the core objects used within the application.
//! These are the Employee struct, and the Client struct,
//! as well as the values and implemented functions
//! required for various data operations.

// imports public Authenticator items from the auth module
use crate::auth::*;
// imports all public items from the errors module
use crate::errors::ApplicationError;

use serde::{Deserialize, Serialize};
use std::fmt;
use std::str::FromStr;

//
// ********************************************
// firm_models.rs module definitions begin here:
// ********************************************
//

// Roles in the system
#[derive(Debug, Clone, Eq, PartialEq, Serialize, Deserialize, Hash)]
pub enum Role {
    Admin,
    Employee,
    Client,
    Restricted,
}

// Display implementation for Role
impl fmt::Display for Role {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        // Match each variant and write its string representation
        match self {
            Role::Admin => write!(f, "Admin"),
            Role::Employee => write!(f, "Employee"),
            Role::Client => write!(f, "Client"),
            Role::Restricted => write!(f, "Restricted"),
        }
    }
}

impl FromStr for Role {
    type Err = ();

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        match s {
            "Admin" => Ok(Role::Admin),
            "Employee" => Ok(Role::Employee),
            "Client" => Ok(Role::Client),
            "Restricted" => Ok(Role::Restricted),
            _ => Err(()),
        }
    }
}

// Permissions for users
#[derive(Debug, Clone, Eq, PartialEq, Serialize, Deserialize, Hash)] // Added Hash trait
pub enum Permission {
    ManageUsers,        // admin+ permission -
    ManageSystem,       // admin + permission -
    ManageUserByUserId, // admin + permission -

    ViewClientByUserId, // employee+ permission - / db: get_client_by_user_account_id()
    ViewClientByClientId, // employee+ permission - get_client() / db:
    ViewAllClients,     // employee+ permission - / db: get_clients()

    ModifyClientService, // Employee+ permission - update_client() / db:
    ManageClientRole,    // admin+ permission - / db:
    ManageClientById,    // employee+ permission - update_client() / db: update_client()
    DeleteClientById,    // admin+ permission - remove_client() / db: remove_client()
    AssignClientRole,    // admin+ permission - new_client() / db: new_client

    ViewAllEmployees,         // employee+ permission - / db: get_employees()
    ViewEmployeeByEmployeeId, // Employee+ permission - get_employee_by_id() / db: get_employee()
    ViewEmployeeClientPairs,  // employee+ permission - get_clients_for_employee() / db:
    ManageEmployeeRole,       // admin+ permission -
    ManageEmployeeById,       // employee+ permission - update_employee() / db:
    DeleteEmployeeById,       // admin+ permission - delete_employee() / db:
    AssignEmployeeRole,       // admin+ permission - add_new_employee() / db: new_employee()

    ViewSelfAccountData, // restricted+ permission
    UpdateProfile,       // restricted+ permission

    RequestService, //
}
// Display implementation for Permission
impl fmt::Display for Permission {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        match self {
            Permission::ManageUsers => write!(f, "ManageUsers"),
            Permission::ManageSystem => write!(f, "ManageSystem"),
            Permission::ManageUserByUserId => write!(f, "ManageUserByUserId"),
            Permission::ViewClientByUserId => write!(f, "ViewClientByUserId"),
            Permission::ViewClientByClientId => write!(f, "ViewClientByClientId"),
            Permission::ViewAllClients => write!(f, "ViewAllClients"),
            Permission::ModifyClientService => write!(f, "ModifyClientService"),
            Permission::ManageClientRole => write!(f, "ManageClientRole"),
            Permission::ManageClientById => write!(f, "ManageClientById"),
            Permission::DeleteClientById => write!(f, "DeleteClientById"),
            Permission::AssignClientRole => write!(f, "AssignClientRole"),
            Permission::ViewAllEmployees => write!(f, "ViewAllEmployees"),
            Permission::ViewEmployeeByEmployeeId => write!(f, "ViewEmployeeByEmployeeId"),
            Permission::ViewEmployeeClientPairs => write!(f, "ViewEmployeeClientPairs"),
            Permission::ManageEmployeeRole => write!(f, "ManageEmployeeRole"),
            Permission::ManageEmployeeById => write!(f, "ManageEmployeeById"),
            Permission::DeleteEmployeeById => write!(f, "DeleteEmployeeById"),
            Permission::AssignEmployeeRole => write!(f, "AssignEmployeeRole"),
            Permission::ViewSelfAccountData => write!(f, "ViewSelfAccountData"),
            Permission::UpdateProfile => write!(f, "UpdateProfile"),
            Permission::RequestService => write!(f, "RequestService"),
        }
    }
}

// Represents a user account
#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct UserAccount {
    pub id: i64,
    pub email: String,
    pub hashed_password: String,
    pub first_name: String,
    pub last_name: String,
    pub role: Role,
    pub permissions: Vec<Permission>,
}

/// represents an employee within the system
///
/// contains all required information for a firm employee
/// within the system: id, name, and hashed password
///
///# Fields
///
///* `employee_id` - i32 integr value, unique employee identifier
///* `employee_name` - string, name of the employee
///* `hashed_password` - string, hashed password that was input
// declare and define employee struct
#[derive(Clone, Debug, PartialEq)]
pub struct Employee {
    pub employee_id: i32,      // Unique employee identifier
    pub employee_name: String, // Employee's name
    pub user_account_id: i64,  // Foreign key to associated UserAccount
}

impl Employee {
    /// Creates a new instance of the Employee struct
    ///
    /// initializes new instance of Employee struct.  containing
    /// values required of an Employee object
    ///
    ///# Arguments
    ///
    ///* 'employee_id' - i32 integer value of an employee id
    ///* 'name' - reference to employee name string
    ///
    ///# Returns
    ///
    ///* 'Self' - returns static Employee object
    ///
    ///
    pub fn new(employee_id: i32, name: &str, user_account_id: i64) -> Self {
        Employee {
            employee_id,
            employee_name: name.to_string(),
            user_account_id,
        }
    }

    // accessor method to return employee id value
    ///
    /// returns i32 integer value for the employee_id
    ///
    ///# Arguments
    ///
    ///* '&self' - reference to self
    ///
    ///# Returns
    ///
    ///* 'i32' - the 32-bit integer value of self.employee_id
    ///
    pub fn get_employee_id(&self) -> i32 {
        self.employee_id
    }

    /// accessor method to return ref to employee name
    ///
    /// returns reference to the string data stored by the
    /// String employee_id
    ///
    ///# Arguments
    ///
    ///* '&self' - a reference to self
    ///
    ///# Returns
    ///
    ///* '&str' - a reference to string data (self.name)
    ///
    pub fn get_employee_name(&self) -> &str {
        &self.employee_name
    }
}

// trait to allow access of id/key from AVL tree
pub trait Identification {
    fn get_key(&self) -> i32;
}

impl Identification for Client {
    /// get / accessor method to retrieve a key value
    ///
    /// key value for the AVL_tree. Uses unique 32-bit integer
    /// for each new client instance. used to sort the AVL tree
    /// key value for tree, is the client_id value of a Client
    ///
    ///
    ///# Arguments
    ///
    ///* '&self' - a reference to self
    ///
    ///# Returns
    ///
    ///* 'i32' - 32-bit integer value, the "key"/ client_id number
    ///
    fn get_key(&self) -> i32 {
        self.get_client_id()
    }
}

/// represents a Client within the application
///
/// Client structure encapsulates the data that the application
/// requires from each client in order to perform operations for
/// that client
///
///# Fields
///
///* `client_id` - i32 unique client_id integer value
///* `client_name` - owned String, this Client's name
///* `client_service` - i32 client_service integer value
///* `asn_employee_id` - i32 asn_employee_id integer value
///
/// # Examples
///
/// * 'Abraham James' - sample names taken from: https://homepage.net/name_generator/
///
///
#[derive(Clone, Debug, PartialEq, Eq, PartialOrd, Ord)]
pub struct Client {
    pub client_id: i32,       // Unique client ID
    pub client_name: String,  // Client's name
    pub client_service: i32,  // integer
    pub asn_employee_id: i32, // Assigned employee ID
    pub user_account_id: i64,
}

// implement our client structure
impl Client {
    // public function to instantiate the client struct
    // similar to constructor in other OOP langs
    pub fn new(
        client_id: i32,
        client_name: String,
        client_service: i32,
        asn_employee_id: i32,
        user_account_id: i64,
    ) -> Self {
        Client {
            client_id,
            client_name,
            client_service,
            asn_employee_id,
            user_account_id,
        }
    }
    /// set / mutator function for a client service
    ///
    /// used to set local client_service value
    ///
    ///# Arguments
    ///
    ///* '&self' - a reference to self
    ///
    ///# Returns
    ///
    ///* 'i32' - a 32-bit integer value (self.client_service)
    ///
    pub fn change_client_service(&mut self, service: i32) {
        self.client_service = service;
    }
    /// get / accessor method for Client.client_service
    ///
    /// returns the integer value stored in the
    /// 32-bit integer, self.client_service
    ///
    ///
    ///# Arguments
    ///
    ///* '&self' - a reference to self
    ///
    ///# Returns
    ///
    ///* 'i32' - a 32-bit integer value (self.client_service)
    ///
    pub fn get_client_service(&self) -> i32 {
        self.client_service
    }

    /// get / accessor method for Client.get_client_id
    ///
    /// returns the integer value stored in the
    /// 32-bit integer, self.get_client_id
    ///
    ///
    ///# Arguments
    ///
    ///* '&self' - a reference to self
    ///
    ///# Returns
    ///
    ///* 'i32' - a 32-bit integer value (self.get_client_id)
    ///
    pub fn get_client_id(&self) -> i32 {
        self.client_id
    }

    // get method like accessor in other OOP langs
    // returns i32 integer
    ///
    ///
    ///
    /// returns reference to the string data stored by
    /// owned String hashed_password
    ///
    ///# Arguments
    ///
    ///* '&self' - a reference to self
    ///
    ///# Returns
    ///
    ///* '&str' - a reference to string data (self.hashed_password)
    ///
    pub fn get_client_name(&self) -> &str {
        &self.client_name
    }

    /// get / accessor method for Client.asn_employee_id
    ///
    /// returns the integer value stored in the
    /// 32-bit integer, self.asn_employee_id
    ///
    ///# Arguments
    ///
    ///* '&self' - a reference to self
    ///
    ///# Returns
    ///
    ///* 'i32' - a 32-bit integer value (self.asn_employee_id)
    ///
    pub fn get_asn_employee(&self) -> i32 {
        self.asn_employee_id
    }
    /// set / mutator function for a assigned employee id
    ///
    /// used to set value of an employee id for a client/employee
    /// pairing.
    ///
    ///# Arguments
    ///
    ///* '&mut self' - a mutable reference to self
    ///
    ///# Returns
    ///
    ///* 'i32' - a 32-bit integer value (self.asn_employee_id)
    ///
    pub fn change_client_employee_pair(&mut self, new_employee_id: i32) {
        self.asn_employee_id = new_employee_id;
    }
    /// mutator / set method for client_id
    ///
    /// sets local id value for a client to value provided from db.
    /// ids are auto generated by db on creation of instance & addition to the database.
    ///
    ///# Arguments
    ///
    ///* '&mut self' - a mutable reference to self
    ///* 'id' - a 32-bit integer value used to set self.client_id
    ///
    pub fn set_client_id(&mut self, id: i32) {
        self.client_id = id;
    }
}
 */