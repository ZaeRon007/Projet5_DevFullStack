declare namespace Cypress{
    interface Chainable<Subject> {
        /**
         * log user and save token
         * @param : email 
         * @param : password
         */
        login(email: string, password: string): Chainable<any>
    }
}