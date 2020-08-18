import React from 'react';
import { Route } from 'react-router-dom';
import { useKeycloak } from '@react-keycloak/web';

// Route only accessible for authenticated user
export const AuthenticatedRoute = (props) => {
    // Getting Keycloak instance
    const { keycloak } = useKeycloak();

    // Props destructing
    const { authenticatedRoles, is, not, ...rest } = props;

    // If no roles are given then return true as they thus sould have access in that regard else
    // check if they have one of the required roles
    const roleAccess = authenticatedRoles
        ? authenticatedRoles?.reduce((acc, cur) => keycloak.hasRealmRole(cur) || acc, false)
        : true;

    // Check whether a component is a function or not. Execute and return if it is a function else only return
    const isFunction = (component) => {
        return component instanceof Function ? component() : component;
    };

    return (
        <Route
            {...rest}
            render={(props) => {
                /* Check if the user is authenticated and has the required role for the route.
                 * If they have access then return is, if not return not
                 *
                 * Check whether 'is' and 'not' is a function or an Element that can be placed in DOM.
                 *  If it is a function then execute and return it, if not then return it.
                 */
                return keycloak.authenticated && roleAccess ? isFunction(is) : isFunction(not);
            }}
        />
    );
};