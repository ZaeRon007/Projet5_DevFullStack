  it('should submit the registration form', () => {
    cy.intercept('POST', '/api/auth/register').as('registerRequest');

    cy.visit('/register');
    cy.get('input[formcontrolname="firstName"]').type('john');
    cy.get('input[formcontrolname="lastName"]').type('doe');
    cy.get('input[formcontrolname="email"]').type('john.doe@gmail.com');
    cy.get('input[formcontrolname="password"]').type('Password123!');
    cy.get('button[type="submit"]').click();
    
    cy.wait('@registerRequest').then((interception) => {
      expect(interception.response.statusCode).to.eq(200);
    })
  
    cy.visit('/login');
  });

  it('should not submit registration form because user is already registered', () => {
    cy.intercept('POST', '/api/auth/register').as('registerRequest');

    cy.visit('/register');
    cy.get('input[formcontrolname="firstName"]').type('john');
    cy.get('input[formcontrolname="lastName"]').type('doe');
    cy.get('input[formcontrolname="email"]').type('john.doe@gmail.com');
    cy.get('input[formcontrolname="password"]').type('Password123!');
    cy.get('button[type="submit"]').click();
    
    cy.wait('@registerRequest').then((interception) => {
      expect(interception.response.statusCode).to.eq(400);
    })
  
    cy.visit('/login');
  });