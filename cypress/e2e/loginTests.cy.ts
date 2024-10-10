describe('Login spec', () => {
  it('should display \"An error occurred\"', () => {
    cy.intercept('POST', '/api/auth/login').as('loginRequest');
  
    cy.visit('/login');
    cy.get('input[formcontrolname="email"]').type('wrong@example.com');
    cy.get('input[formcontrolname="password"]').type('WrongPassword');
    cy.get('button[type="submit"]').click();
  
  
    cy.wait('@loginRequest').then((interceptor) => {
      expect(interceptor.response.statusCode).to.eq(401);
      cy.get('p').contains('An error occurred');
    });
  })

  it('Login successfull', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
  })
});