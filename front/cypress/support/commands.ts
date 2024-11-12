/// <reference path="index.d.ts" />

Cypress.Commands.add("loginUser", (pEmail: string, pPassword: string) => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
        body: {
            id: 1,
            username: "username",
            firstName: "firstname",
            lastName: "lastname",
            admin: false
        }
        })
        
        cy.intercept({
            method: 'GET',
            url: '/api/session',
        })

        cy.get('input[formControlName=email]').type(pEmail)
        cy.get('input[formControlName=password]').type(pPassword)

        cy.get('.mat-raised-button').click()
    
})

Cypress.Commands.add("loginUserWithSession", (pEmail: string, pPassword: string) => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
        body: {
            id: 1,
            username: "username",
            firstName: "firstname",
            lastName: "lastname",
            admin: false
        }
        })
        
        cy.intercept(
            {
              method: 'GET',
              url: '/api/session',
            },
            {
              body: [
                {
                  id: 1,
                  name: 'session de yoga',
                  description: 'c\'est une superbe session',
                  date: new Date(2024, 10, 11),
                  teacher_id: 1,
                  users: [],
                },
              ],
        })

        cy.get('input[formControlName=email]').type(pEmail)
        cy.get('input[formControlName=password]').type(pPassword)

        cy.get('.mat-raised-button').click()
    
})

Cypress.Commands.add("loginAdmin", (pEmail: string, pPassword: string) => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
        body: {
            id: 1,
            username: "username",
            firstName: "firstname",
            lastName: "lastname",
            admin: true
        }
        })
        
        cy.intercept({
            method: 'GET',
            url: '/api/session',
        })

        cy.get('input[formControlName=email]').type(pEmail)
        cy.get('input[formControlName=password]').type(pPassword)

        cy.get('.mat-raised-button').click()
    
})