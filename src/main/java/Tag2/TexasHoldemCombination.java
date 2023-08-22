package Tag2;

import Tag2.TexasHoldemHand;
import Tag2.tools.CardDeck52;
import Tag2.tools.CardDeck52.Card.Sign;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import java.util.List;
import java.util.function.Supplier;

public final class TexasHoldemCombination implements Comparable<TexasHoldemCombination> {
    public enum CombinationType {
        HighCard,
        OnePair,
        TwoPair,
        ThreeOfAKind,
        Straight,
        Flush,
        FullHouse,
        FourOfAKind,
        StraightFlush,
        RoyalFlush
    }

    CombinationType combinationType;
    List<CardDeck52.Card> combinationCards;
    List<CardDeck52.Card> hand;
    List<CardDeck52.Card> tableCards;
    List<CardDeck52.Card> kicker = new ArrayList<>();
    // a)
    TexasHoldemCombination(List<CardDeck52.Card> tableCards, TexasHoldemHand hand) {
        this.hand = new ArrayList<>(Arrays.stream(hand.get()).toList());

        this.combinationCards = new ArrayList<>();
        List<CardDeck52.Card> list = new ArrayList<>(tableCards);
        for (CardDeck52.Card card : hand.get()) {
            list.add(card);
        }
        //comparator for card
        list.sort(( a, b)-> a.value-b.value);

        //count flush
        Map<Sign,Integer> flushMap = new HashMap<>();
        boolean isFlush = false;
        Sign flushSign = null;
        for (CardDeck52.Card card : list) {
            flushMap.put(card.sign, flushMap.getOrDefault(card.sign, 0) + 1);
        }
        for (Map.Entry<CardDeck52.Card.Sign, Integer> entry : flushMap.entrySet()) {
            if (entry.getValue() >= 5) {
                isFlush = true;
                flushSign = entry.getKey();
                break;
            }
        }
    int straightCounter = 0;
    List<CardDeck52.Card> straightList = new ArrayList<>();
    CardDeck52.Card straightHighCard = null;
    for (int i = 0; i < list.size()-1; i++) {
        if(list.get(i).value+1 == list.get(i+1).value){
            straightCounter++;
            if(straightCounter == 1){
                straightList.add(list.get(i));
            }
            straightList.add(list.get(i+1));
            if (straightList.size()>=6) {
                straightList.remove(0);
            }
        }else if(list.get(i).value != list.get(i+1).value){
            straightCounter = 0;
            if (straightList.size()>=5) {
                break;
            }
            straightList.clear();
        }
        if (straightCounter>3) {
            straightHighCard=list.get(i+1);
        }
    }
    boolean isStraight = straightHighCard!=null && straightList.size()>=5;
    //count values
    Map<Integer,Integer> count = new HashMap<>();
        for (CardDeck52.Card card: list){
        if (!count.containsKey(card.value)) {
            count.put(card.value, 1);
        } else{
            count.put(card.value, count.get(card.value)+1);
        }
    }

    //Royal Flush
        if (isFlush && isStraight) {
            boolean hasAce = false;
            for (CardDeck52.Card card : list) {
                if (card.sign == flushSign && card.value == 14) {
                    hasAce = true;
                    break;
                }
            }
            if (hasAce) {
                combinationType = CombinationType.RoyalFlush;
                for (CardDeck52.Card card : list) {
                    if (card.sign == flushSign && card.value <= 10) {
                        combinationCards.add(card);
                    }
                }
                return;
            }
        }

    //StraightFlush
        if (isFlush && isStraight) {
            combinationType = CombinationType.StraightFlush;
            kicker.add(straightHighCard);
            for (CardDeck52.Card card : list) {
                if (card.sign == flushSign && card.value >= straightHighCard.value - 4 && card.value <= straightHighCard.value) {
                    combinationCards.add(card);
                }
            }
            return;
        }

    //Vierling
        if (count.containsValue(4)){
        int fourKey = -1;
        for (int key:count.keySet()){
            if (count.get(key)==4) {
                fourKey = key;
                break;
            }
        }
        combinationType = CombinationType.FourOfAKind;
        for (CardDeck52.Card card : list) {
            if (card.value==fourKey) {
                combinationCards.add(card);
                if (kicker.size()==0) {
                    kicker.add(card);
                }
            }
        }
        for (int i =list.size()-1; i>=0; i--){
            if (list.get(i).value!=fourKey) {
                kicker.add(0,list.get(i));
                combinationCards.add(list.get(i));
                break;
            }
        }
        return;
    }


    //fullhouse
    int tripletKey = -1;
    int pairKey = -1;
        for (int i = list.size()-1;i>=0;i--){
        if (count.get(list.get(i).value)==3&&tripletKey==-1) {
            tripletKey = list.get(i).value;
        }
        if (count.get(list.get(i).value)>=2&&tripletKey!=list.get(i).value) {
            pairKey = list.get(i).value;
        }
    }
        if (tripletKey!=-1&&pairKey!=-1) {
        combinationType = CombinationType.FullHouse;
        for (CardDeck52.Card card: list){
            if (card.value==tripletKey||card.value==pairKey) {
                combinationCards.add(card);
            }
            if (card.value==tripletKey) {
                kicker.add(card);
            }
            if (card.value==pairKey) {
                kicker.add(0,card);
            }
        }
        return;
    }

    //flush
        if (isFlush){
        combinationType = CombinationType.Flush;
        for (CardDeck52.Card card: list){
            if (card.sign==flushSign&&combinationCards.size()<5) {
                combinationCards.add(card);
            }
        }
        return;
    }

    //straight
        if (isStraight) {
        combinationType = CombinationType.Straight;
        kicker.add(straightHighCard);
        combinationCards = straightList;
        return;
    }

    //Drilling
        if (tripletKey!=-1) {
        int freeKicker= 2;
        combinationType = CombinationType.ThreeOfAKind;
        for (int i = list.size()-1;i>=0;i--){
            if (list.get(i).value==tripletKey) {
                combinationCards.add(list.get(i));
                if (kicker.size()+freeKicker==2) {
                    kicker.add(0,list.get(i));
                }
            }
            if (list.get(i).value!=tripletKey&&freeKicker>0) {
                kicker.add(list.get(i));
                combinationCards.add(list.get(i));
                freeKicker--;
            }
        }
        return;
    }

    //Zwei Paare
    int pairCounter = 0;
    int highestPair = -1;
    int secondHighestPair = -1;
        for (int key:count.keySet()){
        if (count.get(key)==2) {
            pairCounter++;
            if (key>highestPair) {
                secondHighestPair = highestPair;
                highestPair = key;
            }
            else if (key>secondHighestPair) {
                secondHighestPair = key;
            }
        }
    }
        if (pairCounter>=2) {
        combinationType = CombinationType.TwoPair;
        CardDeck52.Card addedKicker = null;
        boolean addedhighestPair = false;
        boolean addedsecondHighestPair = false;
        for (int i =list.size()-1; i>=2; i--){
            if (list.get(i).value==highestPair) {
                combinationCards.add(list.get(i));
                if (!addedhighestPair) {
                    kicker.add(0,list.get(i));
                    addedhighestPair = true;
                }
            } else if (list.get(i).value==secondHighestPair) {
                combinationCards.add(list.get(i));
                if (!addedsecondHighestPair) {
                    kicker.add(list.get(i));
                    addedsecondHighestPair = true;
                }
            } else if (addedKicker==null) {
                addedKicker = list.get(i);
            }
        }
        kicker.add(addedKicker);
        combinationCards.add(addedKicker);
        return;
    }

    //Ein Paar
        if (pairCounter==1) {
        combinationType = CombinationType.OnePair;
        for (int i =list.size()-1; i>=0; i--){
            if (list.get(i).value==highestPair) {
                combinationCards.add(list.get(i));
                if (kicker.size()==0) {
                    kicker.add(list.get(i));
                }
            }
        }
        for (int i =list.size()-1; i>=0; i--){
            if (list.get(i).value!=highestPair&&combinationCards.size()<5) {
                kicker.add(0,list.get(i));
                combinationCards.add(list.get(i));
                break;
            }
        }
        return;
    }

    //High Card
    combinationType= CombinationType.HighCard;
        for (int i =list.size()-1; i>=list.size()-5&&i>=0; i--){
        kicker.add(list.get(i));
        combinationCards.add(list.get(i));
    }

}


    // b)
    @Override
    public final int compareTo(TexasHoldemCombination that) {
        if (this.combinationType.ordinal()>that.combinationType.ordinal()) {
            return 1;
        } else if (this.combinationType.ordinal()<that.combinationType.ordinal()) {
            return -1;
        } else {
            for (int i=0; i<Math.min(this.kicker.size(),that.kicker.size()); i++) {
                if (this.kicker.get(i).value>that.kicker.get(i).value) {
                    return 1;
                } else if (this.kicker.get(i).value<that.kicker.get(i).value) {
                    return -1;
                }
            }
            return 0;
        }
    }

    // c)
    private static class CombSup implements Supplier{
        CardDeck52 deck = new CardDeck52();
        Random r = new Random();
        int[] sizes = {/*0,3,4,*/5};
        /**
         * Gets a result.
         * @return a result
         */
        @Override
        public Object get() {
            int size = sizes[r.nextInt(sizes.length)];
            List<CardDeck52.Card> tableCards = new ArrayList<>();
            for (int i=0; i<size; i++) {
                tableCards.add(deck.deal());
            }
            TexasHoldemHand hand = new TexasHoldemHand();
            hand.takeDeal(deck.deal());
            hand.takeDeal(deck.deal());
            deck.shuffle();
            return new TexasHoldemCombination(tableCards, hand);
        }
    }

        public static Stream<TexasHoldemCombination> generate() {
            CombSup combSup = new CombSup();
            return Stream.generate(combSup);
        }

        public static void main(String[] args) {
            CardDeck52 deck = new CardDeck52();
            boolean hasTable = Math.random() >= 0.5;
            List<CardDeck52.Card> tableCards = hasTable ?
                    deck.deal(ThreadLocalRandom.current().nextInt(3, 5 + 1)) :
                    Collections.emptyList();

            TexasHoldemHand hand = new TexasHoldemHand();
            TexasHoldemHand hand2 = new TexasHoldemHand();
            hand.takeDeal(deck.deal());
            hand2.takeDeal(deck.deal());
            hand.takeDeal(deck.deal());
            hand2.takeDeal(deck.deal());

            Stream.of(hand.eval(tableCards), hand2.eval(tableCards))
                    .sorted(Comparator.reverseOrder())
                    .forEach(combination -> System.out.println(
                            "CombinationType: " + combination.combinationType +
                                    ", CombinationCards: " + combination.combinationCards));

            TexasHoldemCombination combination = hand.eval(tableCards);
            System.out.println("Table Cards (" + tableCards.size() + "): " + tableCards);
            System.out.println("Hand: " + Arrays.toString(hand.get()));
            System.out.println("Combination (" + combination.combinationCards.size() + "): " + combination.combinationType + " -> " + combination.combinationCards);
        }

        @Override
        public String toString() {
            return "TexasHoldemCombination{" +
                    "hand=" + hand +
                    ", tableCards=" + tableCards +
                    ", combinationType=" + combinationType +
                    ", combinationCards=" + combinationCards +
                    '}';
        }
    }
