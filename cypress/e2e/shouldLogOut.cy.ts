it('should log out and redirect to login page', () => {
    cy.visit('/sessions');
    cy.get('button.logout').click();
    
    cy.url().should('include', '/login');
  });
  