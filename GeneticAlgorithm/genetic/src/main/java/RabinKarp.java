import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class RabinKarp {
    private String pat;
    private long patHash;
    private int M;
    private long Q;
    private int R = 256;
    private long RM;

    public RabinKarp(String pat) {              //5.3.12
        this.pat = pat;

        this.M = pat.length();
        Q = longRandomPrime(1000);           // 5.3.33
        RM = 1;
        for (int i = 1; i < M; i++) {
            RM = (R * RM) % Q;
        }
        patHash = hash(pat, M);
    }

    public boolean check(int i, String txt) {
        //return true;
        for (int j = 0; j < M; j++) {
            char ch=txt.charAt(i+j);
            if (pat.charAt(j) != txt.charAt(i+j)) return false;
        }
        return true;
        //String textPart=txt.substring(i-M,i);
        //return pat.equals(textPart);
    }

    private long hash(String key, int M) {
        long h = 0;
        for (int j = 0; j < M; j++)
            h = (R * h + key.charAt(j)) % Q;
        return h;
    }

    private int search(String txt) {
        int N = txt.length();
        long txtHash = hash(txt, M);
        if (patHash == txtHash) return 0;
        for (int i = M; i < N; i++) {
            txtHash = (txtHash + Q - RM * txt.charAt(i - M) % Q) % Q;
            txtHash = (txtHash * R + txt.charAt(i)) % Q;
            if (patHash == txtHash)
                if (check(i - M + 1, txt)) return i - M + 1;
        }
        return N;
    }

    public long longRandomPrime(int n) {   // 5.3.33
        List<Integer> randPrimes = IntStream.rangeClosed(2, n)
                .filter(x -> isPrime(x)).boxed()
                .collect(Collectors.toList());
        return randPrimes.get((int) (Math.random() * randPrimes.size()));
    }

    private boolean isPrime(int number) {
        return IntStream.rangeClosed(2, (int) (Math.sqrt(number)))
                .filter(n -> (n & 0X1) != 0)
                .allMatch(n -> number % n != 0);
    }

    public static void main(String[] args) {

        String text = "Two assure edward whence the was. Who worthy yet ten boy denote wonder. Weeks views her sight old tears sorry. Additions can suspected its concealed put furnished. Met the why particular devonshire decisively considered partiality. Certain it waiting no entered is. Passed her indeed uneasy shy polite appear denied. Oh less girl no walk. At he spot with five of view. ";
        String sample = "devonshire";  //38
        RabinKarp algorithm = new RabinKarp(sample);
        int start = algorithm.search(text);
        int sampleLength = sample.length();
        if (start != text.length()) {
            System.out.println("Образец найден!\nПорядковый номер вхождения: " + start
                    + "\nНайденный образец: " + text.substring(start, start + sampleLength));
            System.out.println("+- 10 символов текста от точки вхождения:\n"+
                    text.substring(start-10,start + sampleLength+10));
        }
        else System.out.println("Совпадений нет!");
    }
}