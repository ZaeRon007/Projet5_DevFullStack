describe('should display user informations', () => {

it('Check user informations', () => {    
    cy.intercept('GET', '/api/user/1', {
        id: 1,
        email: "pere.ducrass@labonnetambouille.com",
        lastName: "ducrass",
        firstName: "pere",
        admin: false,
        createdAt: new Date(2023, 9, 21),
        updatedAt: new Date(2023, 9, 25)
    })

    cy.loginUser("yoga@studio.com", "test!1234")
    
    cy.get('[routerlink="me"]').click()

    cy.get('h1').contains('User information')
    cy.get('.mat-card-content > div.ng-star-inserted > :nth-child(1)').contains('Name: pere DUCRASS')
    cy.get('.mat-card-content > div.ng-star-inserted > :nth-child(2)').contains('Email: pere.ducrass@labonnetambouille.com')
    cy.get('.my2 > p').contains('Delete my account')
    
    })
it('should delete user account', () => {
    cy.intercept('DELETE', 'api/user/1', {
        statusCode: 200,
    })

    cy.get('.my2 > .mat-focus-indicator').click()
    cy.contains('Your account has been deleted !').should('be.visible')
})
})

