package domain;

import java.util.*;

public class MainApp {
    static final int MIN_VALID_MONEY_TO_BUY_LOTTO = 1000;
    static final int LOTTO_PRICE = 1000;
    static final String MESSAGE_WRONG_MONEY_TO_BUY_LOTTO = "잘못된 입력입니다. 1000으로 나누어 떨어지는 양의 정수를 입력해 주세요.";
    static final String MESSAGE_WRONG_LOTTO_NUMBERS = "잘못된 입력입니다. 1~45 사이의 서로 다른 정수 6개를 콤마(,)로 구분하여 입력해 주세요. (e.g., \"1,2,3,4,5,6\")";
    static final int MIN_LOTTO_NUMBER = 1;
    static final int MAX_LOTTO_NUMBER = 45;


    public static void main(String[] args) {
        getWinningNumbers();
    }


    /**
     * 사용자가 입력한 로또 구입 금액이 적절한지 판단하는 메소드
     */
    public static boolean isValidMoneyToBuyLotto(int money) {
        return ((money >= MIN_VALID_MONEY_TO_BUY_LOTTO) && (money % LOTTO_PRICE == 0));
    }

    /**
     * 사용자에게 정수를 입력받아 반환하는 메소드
     */
    public static int getIntegerFromUser() {
        Scanner rd = new Scanner(System.in);
        while(true) {
            try {
                return rd.nextInt();
            } catch (Exception e) {
                System.out.println(MESSAGE_WRONG_MONEY_TO_BUY_LOTTO);
                rd.nextLine();
            }
        }
    }

    /**
     * 사용자에게 로또 구입 금액을 입력받는 메소드
     */
    public static int getMoneyToBuyLotto() {
        int money;

        while (!isValidMoneyToBuyLotto(money = getIntegerFromUser())) {
            System.out.println(MESSAGE_WRONG_MONEY_TO_BUY_LOTTO);
        }
        return money;
    }

    /**
     * 로또 한개를 무작위로 발급하는 메소드
     */
    public static Lotto createRandomLotto() {
        TreeSet<Integer> lottoNumbers = new TreeSet<Integer>();                     // 중복을 허용하지 않는 TreeSet 컬렉션을 사용한다.
        int randomNumber;

        while (lottoNumbers.size() < 6) {
            randomNumber = (int)(Math.random() * 45) + 1;                           // 1~45 사이의 정수 하나를 무작위로 생성한다.
            lottoNumbers.add(randomNumber);
        }

        List<Integer> arrLottoNumbers = new ArrayList<Integer>(lottoNumbers);       // TreeSet 을 ArrayList 로 변환한다.
        return new Lotto(arrLottoNumbers);
    }

    /**
     * 로또 구입 금액에 해당하는 수의 로또를 발급하는 메소드
     */
    public static List<Lotto> createLottosWorth(int money) {
        int numberOfLottos = money / LOTTO_PRICE;
        List<Lotto> listOfLottos = new ArrayList<Lotto>();

        for (int i=0; i<numberOfLottos; i++) {
            listOfLottos.add(createRandomLotto());
        }
        return listOfLottos;
    }

    /**
     * 로또 번호들을 출력하는 메소드
     */
    public static void printLottos(List<Lotto> lottoList) {
        Iterator<Lotto> it = lottoList.iterator();

        while(it.hasNext()) {
            System.out.println(it.next());
        }
    }

    /**
     * 당첨 번호가 적절한 지 검사하는 메소드
     */
    public static boolean areValidLottoNumbers(List<Integer> winningNumbers) {
        if (winningNumbers.size() != 6) {                                            // 숫자는 총 6개 이어야 한다.
            return false;
        }

        Collections.sort(winningNumbers);
        if (!isValidLottoNumber(winningNumbers.get(0)) || !isValidLottoNumber(winningNumbers.get(winningNumbers.size()-1))) {
            return false;                                                            // 리스트의 최솟값과 최댓값이 적절한 로또 번호라면 그 사이에 있는 값은 적절함이 자명하다.
        }

        return isSet(winningNumbers);                                                // 숫자들 간의 중복이 없어야 한다.
    }

    /**
     * 적절한 로또 번호인지 확인하는 메소드
     */
    public static boolean isValidLottoNumber(int lottoNumber) {
        return (lottoNumber >= MIN_LOTTO_NUMBER) && (lottoNumber <= MAX_LOTTO_NUMBER);
    }

    /**
     * 주어진 정수 리스트에 중복이 있는 지 확인하는 메소드
     */
    public static boolean isSet(List<Integer> listNumbers) {
        HashSet<Integer> setNumbers = new HashSet<Integer>(listNumbers);

        return setNumbers.size() == listNumbers.size();
    }

    /**
     *  사용자로부터 당첨번호를 입력받는 메소드.
     */
    public static List<Integer> getWinningNumbers() {
        List<Integer> winningNumbers;

        while (!areValidLottoNumbers(winningNumbers = getNumbersFromUser())) {
            System.out.println(MESSAGE_WRONG_LOTTO_NUMBERS);
        }

        return winningNumbers;
    }

    /**
     * 사용자로부터 정수들의 문자열을 입력받아 정수들의 배열로 바꾸는 메소드 (e.g., "1,2,3,4,5" -> [1, 2, 3, 4, 5])
     */
    public static List<Integer> getNumbersFromUser() {
        Scanner rd = new Scanner(System.in);
        String userInput;
        String[] strings;

        do {
            userInput = rd.nextLine();
            strings = userInput.split(",");
        } while (!canAllStringsBeIntegers(strings));
        List<Integer> numbers = stringsToIntegers(strings);
        return numbers;
    }

    /**
     * 정수 문자열들의 배열을 정수들의 배열로 바꾸는 메소드 (e.g., ["1", "2", "3"] -> [1, 2, 3])
     */
    public static List<Integer> stringsToIntegers(String[] integerStrings) {
        List<Integer> integers = new ArrayList<Integer>();

        for (String s : integerStrings) {
            integers.add(Integer.parseInt(s));
        }

        return integers;
    }

    /**
     * 문자열 배열의 모든 문자열이 정수로 변환될 수 있는지 검사하는 메소드
     */
    public static boolean canAllStringsBeIntegers(String[] strings) {
        for (String s: strings) {
            try {
                int i = Integer.parseInt(s);
            } catch (Exception e) {
                System.out.println(MESSAGE_WRONG_LOTTO_NUMBERS);
                return false;
            }
        }
        return true;
    }
}
