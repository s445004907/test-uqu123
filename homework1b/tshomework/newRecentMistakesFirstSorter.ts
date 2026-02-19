interface Card {
    question: string;
    wasLastAnswerCorrect(): boolean;
}

interface CardOrganizer {
    organize(cards: Card[]): void;
}

const newRecentMistakesFirstSorter: CardOrganizer = {
    organize(cards: Card[]): void {
        cards.sort((a: Card, b: Card) => {
            const aMistake = !a.wasLastAnswerCorrect();
            const bMistake = !b.wasLastAnswerCorrect();
            if (aMistake && !bMistake) return -1;
            if (!aMistake && bMistake) return 1;
            return 0;
        });
    }
};

class CardImpl implements Card {
    constructor(public question: string, private lastCorrect: boolean) {}
    wasLastAnswerCorrect(): boolean {
        return this.lastCorrect;
    }
}

const myCards: Card[] = [
    new CardImpl("Question 1", true),
    new CardImpl("Question 2", false),
    new CardImpl("Question 3", true),
    new CardImpl("Question 4", false)
];

console.log("Original Order:");
myCards.forEach((c: Card) => console.log(`${c.question}: ${c.wasLastAnswerCorrect()}`));

newRecentMistakesFirstSorter.organize(myCards);

console.log("\nOrder after organizing (Mistakes First):");
myCards.forEach((c: Card) => console.log(`${c.question}: ${c.wasLastAnswerCorrect()}`));

export {};