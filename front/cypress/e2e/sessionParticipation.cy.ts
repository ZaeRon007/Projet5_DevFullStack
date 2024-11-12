describe('functional test for participation', () => {
    it('should participate then unparticipate to session', () => {
        cy.loginUserWithSession("yoga@studio.com", "test!1234")

        cy.contains('session de yoga').should('be.visible')
        
        cy.intercept(
            {
              method: 'GET',
              url: '/api/session/1',
            },
            {
              body: {
                  id: 1,
                  name: 'session de yoga',
                  description: 'c\'est une superbe session',
                  date: new Date(2024, 10, 11),
                  teacher_id: 1,
                  users: [],
                },
        })

        cy.intercept(
            {
                method: 'GET',
                url: '/api/teacher/1',
            },
            [
                {
                    id: 1,
                    lastName: 'DELAHAYE',
                    firstName: 'Margot',
                    createdAt: new Date(2020, 1, 1),
                    updatedAt: new Date(2021, 1, 1)
                }
        ])

        cy.get('.mat-card-actions > .mat-focus-indicator').click()

        cy.contains('span.ml1', 'Participate').should('be.visible')

        cy.intercept('POST', '/api/session/1/participate/1', {
            statusCode: 200
        })

        cy.intercept(
            {
                method: 'GET',
                url: '/api/session/1'
            },
            {
                body: {
                    id: 1,
                    name: 'session de yoga',
                    description: 'c\'est une superbe session',
                    date: new Date(2024, 10, 11),
                    createdAt: '2023-09-25T00:00:00.000+00:00',
                    teacher_id: 1,
                    users: [
                        1
                    ]
                },
        })

        cy.get('div.ng-star-inserted > .mat-focus-indicator').click()

        cy.contains('span.ml1', 'Do not participate').should('be.visible')

        cy.intercept('DELETE', '/api/session/1/participate/1', {
            statusCode: 200
        })

        cy.intercept(
            {
                method: 'GET',
                url: '/api/session/1'
            },
            {
                body: {
                    id: 1,
                    name: 'session de yoga',
                    description: 'c\'est une superbe session',
                    date: new Date(2024, 10, 11),
                    createdAt: '2023-09-25T00:00:00.000+00:00',
                    teacher_id: 1,
                    users: []
                },
        })

        cy.get('div.ng-star-inserted > .mat-focus-indicator').click()

        cy.contains('span.ml1', 'Participate').should('be.visible')


    })
})