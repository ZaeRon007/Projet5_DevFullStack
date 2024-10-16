describe('scenario', () => {
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
            const token = interception.response.body.token;
            Cypress.env('userToken', token);
        });

        cy.url().should('include', '/sessions')
    })

    it('should create a rental', () => {
        const token = Cypress.env("userToken");

        cy.url().should('include', '/sessions')
        cy.get('button[ng-reflect-router-link="create"]').click();

        cy.url().should('include', '/sessions/create')

        cy.get('input[formControlName=name]').type("session de yoga")
        cy.get('input[formControlName=date]').type("2024-10-11")

        cy.get('#mat-select-0').click()
        // cy.get('mat-option .mat-option-text').contains(' Margot DELAHAYE ').click();
        cy.get('mat-option-text span').contains(' Margot DELAHAYE ').click();

        cy.get('textarea[formControlName=description]').type("c'est une superbe description")

        cy.get('mat-select').should('contain', 'Option 2');

    })



  });