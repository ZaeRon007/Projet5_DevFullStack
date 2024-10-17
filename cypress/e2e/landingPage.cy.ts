describe('home page loading test', () => {
  it('should navigate to the detail page', () => {
      cy.visit('/');
      cy.get('span').contains('Yoga app');
      cy.get('span').contains('Login');
      cy.get('span').contains('Register');
  });
})
  