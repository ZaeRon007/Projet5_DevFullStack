it('should navigate to the detail page', () => {
    cy.visit('/');
    cy.get('span').contains('Yoga app');  // Vérification du titre de la page
    cy.get('span').contains('Login');  // Vérification du titre de la page
    cy.get('span').contains('Register');  // Vérification du titre de la page
  });
  