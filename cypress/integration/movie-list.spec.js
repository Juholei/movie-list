context('Movie list', () => {
  beforeEach(() => {
    cy.visit('/');
  });

  it('Set list name', () => {
    cy.get('div').contains('Name of the list').siblings('input')
      .type('Bestest movies!').should('have.value', 'Bestest movies!');
  });

  describe('Add movie dialog', () => {
    it("Add a movie to the list", () => {
        describe("Open add movie dialog", () => {
          cy.get("#add-movie").click();
          cy.contains('Add a movie');});

        describe("Search for a movie", () => {
          cy.get("#movie-name-input").type("Hot Fuzz").should('have.value', 'Hot Fuzz');
          cy.get('button').contains('Add movie').should('not.be.disabled');
        });
    });

  });
})
