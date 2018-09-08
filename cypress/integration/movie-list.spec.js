context('Movie list', () => {
  beforeEach(() => {
    cy.visit('http://localhost:3449')
  })

  it('Set list name', () => {
    // https://on.cypress.io/type
    cy.get('div').contains('Name of the list').siblings('input')
      .type('Bestest movies!').should('have.value', 'Bestest movies!')
  })
})
