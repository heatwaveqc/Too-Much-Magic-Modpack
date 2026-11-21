package io.github.heatwaveqc.tyr.progression.mxp.core;

/** Fail-closed accounting for one server-side native expenditure. */
public final class ObservedPayment {
    private ObservedPayment(){}
    public static double delta(double before,double after){
        if(!Double.isFinite(before)||!Double.isFinite(after)||before<0||after<0||after>=before)return 0;
        return before-after;
    }
    public static double proven(double before,double after,double reported,double requested){
        if(!PaymentReceiptPolicy.positive(reported)||!PaymentReceiptPolicy.positive(requested))return 0;
        return Math.min(requested,Math.min(reported,delta(before,after)));
    }
    public static boolean funded(double actual,double price){
        return PaymentReceiptPolicy.positive(actual)&&PaymentReceiptPolicy.positive(price)
            &&Math.abs(actual-price)<=Math.max(1e-6,Math.ulp(price)*8);
    }
    public static double credited(double actual,double price){
        return funded(actual,price)?price:0;
    }
}
