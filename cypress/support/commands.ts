/// <reference path="index.d.ts" />

Cypress.Commands.add("login", (pEmail: string, pPassword: string) => {
    cy.request({
        method: 'POST',
        url: '/api/auth/login',
        body:{
            user:{
                email: pEmail,
                password: pPassword,
            }
        }
    }).then((response) => {
        window.localStorage.setItem('jwt', response.body.user.token)
    })
})

