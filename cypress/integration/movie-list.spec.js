context('Movie list', () => {
  beforeEach(() => {
    cy.visit('/')
  })

  it('Set list name', () => {
    // https://on.cypress.io/type
    cy.get('div').contains('Name of the list').siblings('input')
      .type('Bestest movies!').should('have.value', 'Bestest movies!')
  })
})
