context("Movie list", () => {
  beforeEach(() => {
    cy.visit("/");
  });

  it("Set list name", () => {
    cy.get("div").contains("Name of the list").siblings("input")
      .type("Bestest movies!").should("have.value", "Bestest movies!");
  });

  describe("Add movie dialog", () => {
    it("Add a movie to the list", () => {
        describe("Open add movie dialog", () => {
          cy.get("#open-add-movie-dialog-btn").click();
          cy.contains("Add a movie");
        });

        describe("Search for a movie", () => {
          cy.get("#add-movie-btn").should("be.disabled");
          cy.get("#movie-name-input").type("Hot Fuzz").should("have.value", "Hot Fuzz");
          cy.get("#add-movie-btn").should("not.be.disabled");
          cy.get("div").contains("Hot Fuzz");
        });

        describe("Add the movie to the list", () => {
          cy.get("#add-movie-btn").click();
          cy.get("#movie-name-input").should("not.exist");
          cy.contains("Hot Fuzz");
          cy.contains("2007");
        });
    });

      describe("Dragging a movie", () => {
          beforeEach(() => {
            cy.get("#open-add-movie-dialog-btn").click();
            cy.get("#movie-name-input").type("Hot Fuzz");
            cy.get("#add-movie-btn").click();

            cy.get("#open-add-movie-dialog-btn").click();
            cy.get("#movie-name-input").type("Scott Pilgrim vs. the World");
              cy.get("#add-movie-btn").click();
              cy.wait(1000);
          });
          it("can be deleted", () => {
            cy.contains("Hot Fuzz").parent().trigger("dragstart");
            cy.get("#delete-movie-btn").trigger("drop");
            cy.contains("Hot Fuzz").should("not.exist");
          });
      });
  });
})
