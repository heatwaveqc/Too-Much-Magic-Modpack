package io.github.heatwaveqc.tmm.progression.mxp.core;

/** Actual bounded native practice debit, never a requested or simulated price. */
public final class MahouPracticeReceipt {
    private final int requested;
    private long paid;
    private boolean valid;
    public MahouPracticeReceipt(int requested){this.requested=requested;valid=requested>0;}
    public void invalidate(){valid=false;}
    public void observe(int before,int after){
        if(!valid)return;
        if(before<0||after<0||after>before){valid=false;return;}
        add((long)before-after);
    }
    public void add(long amount){
        if(!valid)return;
        if(amount<0||amount>requested-paid){valid=false;return;}
        paid+=amount;
    }
    /** Partial payments count as practice, but the native report must match measured debit. */
    public long accepted(int nativeResult){return valid&&paid>0&&nativeResult==paid?paid:0;}
}
