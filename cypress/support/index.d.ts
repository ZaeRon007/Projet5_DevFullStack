declare namespace Cypress{
    interface Chainable<Subject> {
        /**
         * log user and save token
         * @param : email 
         * @param : password
         */
        login(pEmail: string, pPassword: string): Chainable<any>
    }
}