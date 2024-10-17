declare namespace Cypress{
    interface Chainable<Subject> {
        /**
         * log user
         * @param : email 
         * @param : password
         */
        loginUser(pEmail: string, pPassword: string): Chainable<any>
        /**
         * log user and load a session
         * @param : email 
         * @param : password
         */
        loginUserWithSession(pEmail: string, pPassword: string): Chainable<any>
        /**
         * log user 
         * @param : email 
         * @param : password
         */
        loginAdmin(pEmail: string, pPassword: string): Chainable<any>

        /**
         * handle all authenticated request
         * @param : method (ex : GET)
         * @param : url
         */
        AuthRequest(inputMethod: string, url: string): Chainable<any>
    }
}