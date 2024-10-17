describe('register test', () => {
  it('should submit the registration form', () => {
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 200,
    }).as('registerRequest');

    cy.visit('/register');
    cy.get('input[formcontrolname="firstName"]').type('john');
    cy.get('input[formcontrolname="lastName"]').type('doe');
    cy.get('input[formcontrolname="email"]').type('john.doe@gmail.com');
    cy.get('input[formcontrolname="password"]').type('Password123!');
    cy.get('button[type="submit"]').click();
    
    cy.wait('@registerRequest')
  
    cy.visit('/login');
  });

  it('should not submit registration form because user is already registered', () => {
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 400,
    }).as('registerRequest');

    cy.visit('/register');
    cy.get('input[formcontrolname="firstName"]').type('john');
    cy.get('input[formcontrolname="lastName"]').type('doe');
    cy.get('input[formcontrolname="email"]').type('john.doe@gmail.com');
    cy.get('input[formcontrolname="password"]').type('Password123!');
    cy.get('button[type="submit"]').click();
    
    cy.wait('@registerRequest')
  
    cy.visit('/login');
  });
})

describe('should not found url', () => {
  it('should redirect to landing page', () => {
      cy.visit('/la-cuisine-du-pere-ducrass')

      cy.url().should('include', '404')
      cy.contains('h1','Page not found').should('be.visible');
  })
})

describe('redirection test', () => {
  it('should be redirected to login page', () => {
      cy.visit('/sessions');
      cy.url().should('include', '/login');
  })    
})

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
})

describe('logOut test', () => {
  it('should log out and redirect to login page', () => {
      cy.loginUser("yoga@studio.com", "test!1234")
      
      cy.get('.ng-star-inserted > :nth-child(3)').click();
  });
})
  

describe('home page loading test', () => {
  it('should navigate to the detail page', () => {
      cy.visit('/');
      cy.get('span').contains('Yoga app');
      cy.get('span').contains('Login');
      cy.get('span').contains('Register');
  });
})

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