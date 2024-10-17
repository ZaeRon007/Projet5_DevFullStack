describe('functional test for session interface', () => {
    
    it('should create a session', () => {
        cy.intercept(
            {
              method: 'GET',
              url: '/api/teacher',
            },
            [
              {
                id: 1,
                lastName: 'DELAHAYE',
                firstName: 'Margot',
                createdAt: new Date(2020, 1, 1),
                updatedAt: new Date(2021, 1, 1)
              },
              {
                id: 1,
                lastName: 'THIERCELIN',
                firstName: 'Hélène',
                createdAt: new Date(2020, 1, 1),
                updatedAt: new Date(2021, 1, 1)
              }
        ])
        
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

        cy.intercept(
            {
              method: 'POST',
              url: '/api/session',
            },
            {
                id: 1,
                name: 'session de yoga',
                description: 'c\'est une superbe session',
                date: new Date(2024, 10, 11),
                teacher_id: 1,
                createdAt: new Date(2023, 9, 25)
        })

        cy.loginAdmin("yoga@studio.com", "test!1234")

        cy.url().should('include', '/sessions')

        cy.get('[fxlayout="row"] > .mat-focus-indicator').click();

        cy.url().should('include', '/sessions/create')

        cy.get('input[formControlName=name]').type("session de yoga")
        cy.get('input[formControlName=date]').type("2024-10-11")
        cy.get('mat-select[formControlname=teacher_id]').click().get('mat-option').contains('DELAHAYE').click()
        cy.get('textarea[formControlName=description]').type("c'est une superbe session")

        cy.get('.mt2 > [fxlayout="row"] > .mat-focus-indicator').click();

        cy.url().should('include', '/sessions')

        cy.contains('Session created !').should('be.visible')
        cy.wait(3500)

        cy.contains('session de yoga').should('be.visible')
    })

    it('should delete session', () => {
      
      cy.intercept(
        {
          method: 'GET',
          url: '/api/session',
        },
        {
          body: [
            {
              id: 1,
              name: 'Session de Yogaaaaa',
              description: 'c\'est une superbe session',
              date: new Date(2024, 10, 11),
              createdAt: new Date(2023, 9, 25),
              updatedAt: new Date(2023, 11, 25),
              teacher_id: 1,
              users: [],
            },
          ],
    })

      cy.intercept(
        {
          method: 'GET',
          url: '/api/teacher',
        },
        [
          {
            id: 1,
            lastName: 'DELAHAYE',
            firstName: 'Margot',
            createdAt: new Date(2020, 1, 1),
            updatedAt: new Date(2021, 1, 1)
          },
          {
            id: 1,
            lastName: 'THIERCELIN',
            firstName: 'Hélène',
            createdAt: new Date(2020, 1, 1),
            updatedAt: new Date(2021, 1, 1)
          }
      ])

      cy.intercept(
        {
          method: 'GET',
          url: '/api/session/1',
        },
        {
          body:
            {
              id: 1,
              name: 'session de yoga',
              description: 'c\'est une superbe session',
              date: new Date(2024, 10, 11),
              teacher_id: 1,
              users: [],
            }
      })

      cy.intercept(
        {
          method: 'PUT',
          url: '/api/session/1',
        },
        {
            id: 1,
            name: 'Session de Yogaaaaa',
            description: 'c\'est une superbe session',
            date: new Date(2024, 10, 11),
            teacher_id: 1,
            createdAt: new Date(2023, 9, 25)
    })

      cy.get('.mat-card-actions > .ng-star-inserted').click()

      cy.get('#mat-input-5').clear().type('Session de Yogaaaaa')

      cy.get('.mt2 > [fxlayout="row"] > .mat-focus-indicator').click()

      cy.url().should('include', '/sessions')

      cy.contains('Session updated !').should('be.visible')
      cy.wait(3500)
    })

    it('should delete sesion', () => {
      cy.intercept(
        {
          method: 'GET',
          url: '/api/teacher/1',
        },
          {
            id: 1,
            lastName: 'DELAHAYE',
            firstName: 'Margot',
            createdAt: new Date(2020, 1, 1),
            updatedAt: new Date(2021, 1, 1)
          }
      )

      cy.intercept(
        {
          method: 'GET',
          url: '/api/session',
        },
        {
          body: [
            {
              id: 1,
              name: 'Session de Yogaaaaa',
              description: 'c\'est une superbe session',
              date: new Date(2024, 10, 11),
              createdAt: new Date(2023, 9, 25),
              updatedAt: new Date(2023, 11, 25),
              teacher_id: 1,
              users: [],
            },
          ],
    })

      cy.intercept(
        {
          method: 'GET',
          url: '/api/session/1',
        },
        {
          body:
            {
              id: 1,
              name: 'Session de Yogaaaaa',
              description: 'c\'est une superbe session',
              date: new Date(2024, 10, 11),
              createdAt: new Date(2023, 9, 25),
              updatedAt: new Date(2023, 11, 25),
              teacher_id: 1,
              users: [],
            }
      })

      cy.contains('span.ml1', 'Detail').click()
      
      cy.intercept('DELETE', '/api/session/1', {
            statusCode:200
          })

      cy.get(':nth-child(2) > .mat-focus-indicator').click()


      cy.url().should('include', '/sessions')

      cy.contains('Session deleted !').should('be.visible')
    })
  })