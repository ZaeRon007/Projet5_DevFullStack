describe('Login test', () => {
  it('should display \"An error occurred\"', () => {
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
    }).as('loginRequest');
  
    cy.visit('/login');
    cy.get('input[formcontrolname="email"]').type('wrong@example.com');
    cy.get('input[formcontrolname="password"]').type('WrongPassword');
    cy.get('button[type="submit"]').click();
  
  
    cy.wait('@loginRequest')
    
    cy.get('p').contains('An error occurred');
  })

  it('Login successfull', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
        statusCode: 200,    
        body: {
            id: 1,
            username: 'userName',
            firstName: 'firstName',
            lastName: 'lastName',
            admin: true,
            token: 'fake-jwt-token'
        },
    }).as('loginRequest')

    cy.intercept('GET', '/api/session', {
        statusCode: 200, 
        body: {
            id: 1,            
        }
      }).as('sessionRequest')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.wait('@loginRequest')

    cy.url().should('include', '/sessions')

    cy.wait('@sessionRequest')

    cy.get('mat-card-title').contains('Rentals available')
  })
});