"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const newRecentMistakesFirstSorter = {
    organize(cards) {
        cards.sort((a, b) => {
            const aMistake = !a.wasLastAnswerCorrect();
            const bMistake = !b.wasLastAnswerCorrect();
            if (aMistake && !bMistake)
                return -1;
            if (!aMistake && bMistake)
                return 1;
            return 0;
        });
    }
};
class CardImpl {
    constructor(question, lastCorrect) {
        this.question = question;
        this.lastCorrect = lastCorrect;
    }
    wasLastAnswerCorrect() {
        return this.lastCorrect;
    }
}
const myCards = [
    new CardImpl("Question 1", true),
    new CardImpl("Question 2", false),
    new CardImpl("Question 3", true),
    new CardImpl("Question 4", false)
];
console.log("Original Order:");
myCards.forEach((c) => console.log(`${c.question}: ${c.wasLastAnswerCorrect()}`));
newRecentMistakesFirstSorter.organize(myCards);
console.log("\nOrder after organizing (Mistakes First):");
myCards.forEach((c) => console.log(`${c.question}: ${c.wasLastAnswerCorrect()}`));
