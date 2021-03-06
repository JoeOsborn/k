package org.kframework.krun.api;

public class KRunProofResult<T> extends KRunResult<T> {
    private boolean proven;

    public KRunProofResult(boolean proven, T result) {
        super(result);
        this.proven = proven;
    }

    public boolean isProven() {
        return proven;
    }

    @Override
    public String toString() {
        if (proven) {
            return "true";
        } else {
            return super.toString();
        }
    }
}
