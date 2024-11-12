describe('redirection', () => {
    it('should be redirected to login page', () => {
        cy.visit('/sessions');
        cy.url().should('include', '/login');
    })    
})