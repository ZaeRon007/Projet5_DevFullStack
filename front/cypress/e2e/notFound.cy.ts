describe('should not found url', () => {
    it('should redirect to landing page', () => {
        cy.visit('/la-cuisine-du-pere-ducrass')

        cy.url().should('include', '404')
        cy.contains('h1','Page not found').should('be.visible');
    })
})