public class Card {
    private String question;
    private boolean lastAnswerCorrect;

    public Card(String question, boolean lastAnswerCorrect) {
        this.question = question;
        this.lastAnswerCorrect = lastAnswerCorrect;
    }

    public boolean wasLastAnswerCorrect() {
        return lastAnswerCorrect;
    }

    @Override
    public String toString() {
        return "Card{question='" + question + "', lastAnswer=" + (lastAnswerCorrect ? "Correct" : "Mistake") + "}";
    }

}
