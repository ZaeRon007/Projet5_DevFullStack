describe('logOut test', () => {
  it('should log out and redirect to login page', () => {
      cy.loginUser("yoga@studio.com", "test!1234")
      
      cy.get('.ng-star-inserted > :nth-child(3)').click();
  });
})
  