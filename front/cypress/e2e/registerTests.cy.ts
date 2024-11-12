describe('register test', () => {
    it('should submit the registration form', () => {
      cy.intercept('POST', '/api/auth/register', {
        statusCode: 200,
      }).as('registerRequest');
  
      cy.visit('/register');
      cy.get('input[formcontrolname="firstName"]').type('john');
      cy.get('input[formcontrolname="lastName"]').type('doe');
      cy.get('input[formcontrolname="email"]').type('john.doe@gmail.com');
      cy.get('input[formcontrolname="password"]').type('Password123!');
      cy.get('button[type="submit"]').click();
      
      cy.wait('@registerRequest')
    
      cy.visit('/login');
    });
  
    it('should not submit registration form because user is already registered', () => {
      cy.intercept('POST', '/api/auth/register', {
        statusCode: 400,
      }).as('registerRequest');
  
      cy.visit('/register');
      cy.get('input[formcontrolname="firstName"]').type('john');
      cy.get('input[formcontrolname="lastName"]').type('doe');
      cy.get('input[formcontrolname="email"]').type('john.doe@gmail.com');
      cy.get('input[formcontrolname="password"]').type('Password123!');
      cy.get('button[type="submit"]').click();
      
      cy.wait('@registerRequest')
    
      cy.visit('/login');
    });
  })