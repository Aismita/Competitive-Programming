package Templates;

public class RabinKarp {
    public long getPRIME() {
        return PRIME;
    }

    public long getMOD() {
        return MOD;
    }

    public int getN() {
        return N;
    }

    public long[] getHash() {
        return hash;
    }

    public long[] getPow() {
        return pow;
    }

    public long[] getInv() {
        return inv;
    }

    final long PRIME, MOD;

    int N;
    long[] hash, pow, inv;

    public RabinKarp(char[] S, long P, long M) { // M must be a prime number
        PRIME = P;
        MOD = M;
        N = S.length;
        int i;

        pow = new long[Math.max(2, N)];
        pow[0] = 1;
        pow[1] = PRIME;
        for (i = 2; i < pow.length; ++i) pow[i] = pow[i - 1] * pow[1] % MOD;

        inv = new long[Math.max(2, N)];
        inv[0] = 1;
        inv[1] = pow(PRIME, MOD - 2, MOD);
        for (i = 2; i < inv.length; ++i) inv[i] = inv[i - 1] * inv[1] % MOD;

        hash = new long[N];
        for (i = 0; i < N; ++i) {
            hash[i] = hashFunc(S[i], i);
            if (i > 0) {
                hash[i] += hash[i - 1];
                if (hash[i] >= MOD) hash[i] -= MOD;
            }
        }
    }

    public long getHash(int l, int r) {
        long h = hash[r];
        if (l > 0) {
            h -= hash[l - 1];
            if (h < 0) h += MOD;
        }
        h = shiftHash(h, l, 0);
        return h;
    }

    public int size() {
        return N;
    }

    private long hashFunc(char ch, int idx) {
        return shiftHash(ch, 0, idx);
    }

    public long shiftHash(long initialHash, int fromIndex, int toIndex) {
        if (fromIndex < toIndex) {
            return initialHash * pow[toIndex - fromIndex] % MOD;
        } else if (fromIndex > toIndex) {
            return initialHash * inv[fromIndex - toIndex] % MOD;
        } else {
            return initialHash;
        }
    }


    private long pow(long base, long exp, long MOD) {
        base %= MOD;
        long ret = 1;
        while (exp > 0) {
            if ((exp & 1) == 1) ret = ret * base % MOD;
            base = base * base % MOD;
            exp >>= 1;
        }
        return ret;
    }
}
