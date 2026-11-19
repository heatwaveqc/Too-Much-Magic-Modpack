package io.github.heatwaveqc.tmm.progression.mxp.core;

/** A paid personal Ink operation; each native helper reports its committed same-color debit. */
public final class InkPaymentReceipt {
    private final long price;
    private long paid;
    private boolean valid;
    public InkPaymentReceipt(long price){this.price=price;valid=price>0&&price<=9_007_199_254_740_991L;}
    public void invalidate(){valid=false;}
    public void observe(long requested,long reported,long before,long after){
        if(!valid)return;
        if(requested<=0||requested!=price-paid||reported<0||reported>requested){valid=false;return;}
        // Non-storage, empty or machine-only stacks permitted to report nothing.
        if(reported==0){
            if(before>=0&&after>=0&&before!=after)valid=false;
            return;
        }
        if(before<0||after<0||after>before||before-after!=reported){valid=false;return;}
        paid+=reported;
    }
    public boolean complete(boolean accepted){return valid&&accepted&&price>0&&paid==price;}
    public long paid(){return paid;}
}
