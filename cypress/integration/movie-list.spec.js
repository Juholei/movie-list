context('Movie list', () => {
  beforeEach(() => {
    cy.visit('/');
  });

  it('Set list name', () => {
    cy.get('div').contains('Name of the list').siblings('input')
      .type('Bestest movies!').should('have.value', 'Bestest movies!');
  });

  it('Open add movie dialog', () => {
    cy.get("#add-movie").click();
    cy.contains('Add a movie');
  });
})
