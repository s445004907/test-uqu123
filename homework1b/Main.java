import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Card> myCards = Arrays.asList(
            new Card("question1", true),
            new Card("question2", false), 
            new Card("question3", true)
    
          
        );

        CardOrganizer sorter = new RecentMistakesFirstSorter();
        List<Card> result = sorter.organize(myCards);

        System.out.println("The order after organizing ");
        result.forEach(System.out::println);
       
    }
}
