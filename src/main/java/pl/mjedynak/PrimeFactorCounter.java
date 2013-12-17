package pl.mjedynak;

public class PrimeFactorCounter {

    public long primeFactors(long number) {
        long n = number > 0 ? number : -number;
        long counter = 0;
        for (int i = 2; i <= n / i; i++) {
            while (n % i == 0) {
                counter++;
                n /= i;
            }
        }
        if (n > 1) {
            counter++;
        }
        return counter;
    }
}
