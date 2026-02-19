
    import java.util.Comparator;
    import java.util.List;
    import java.util.stream.Collectors;
    import java.util.ArrayList;
    
    public class RecentMistakesFirstSorter implements CardOrganizer {
        @Override
        public List<Card> organize(List<Card> cards) {
            List<Card> incorrect = new ArrayList<>();
            List<Card> correct = new ArrayList<>();
    
            for (Card card : cards) {
                
                if (!card.wasLastAnswerCorrect()) {
                    incorrect.add(card);
                } else {
                    correct.add(card);
                }
            }
    
            
            incorrect.addAll(correct);
            return incorrect;
        }
    }





