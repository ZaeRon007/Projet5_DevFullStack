it('Login', () => {        
    cy.intercept('POST', '/api/auth/login', {
        body: {
            id: 1,
            username: 'userName',
            firstName: 'firstName',
            lastName: 'lastName',
            admin: true
        },
        }).as('loginRequest')

    cy.visit('/login')      
        
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type("test!1234")
    cy.get('button[type="submit"]').click();

    cy.wait('@loginRequest').then((interception) => {
        const token = interception.response.body.token
        Cypress.env('userToken', token);
    });

    cy.url().should('include', '/sessions')
})

it('Check user informations', () => {        
    // const token = Cypress.env("userToken")

    // cy.intercept('GET', '/me', (req) => {
    //     req.headers['Authorization'] = `Bearer ${token}`;
    // } ).as('getRequest')

    // cy.visit('/me') 

    // cy.wait('@getRequest')
    cy.login("yoga@studio.com", "test!1234")
})