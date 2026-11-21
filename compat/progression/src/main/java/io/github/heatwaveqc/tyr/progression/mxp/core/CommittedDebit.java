package io.github.heatwaveqc.tyr.progression.mxp.core;

/** A receipt for a measured payment, never for a preview, transfer or infinite resource. */
public final class CommittedDebit {
    private CommittedDebit(){}
    public static double measured(double beforePersonal,double afterPersonal,double beforeReserve,double afterReserve,double quote){
        if(!Double.isFinite(beforePersonal)||!Double.isFinite(afterPersonal)||!Double.isFinite(beforeReserve)||
            !Double.isFinite(afterReserve)||!Double.isFinite(quote)||quote<=0||
            beforePersonal<0||beforeReserve<0||afterReserve<0)return 0;
        // Resources may overdraw personal Psi into overflow. Only count funds that actually existed.
        double personal=Math.max(0,beforePersonal-Math.max(0,afterPersonal));
        double reserve=Math.max(0,beforeReserve-afterReserve);
        double actual=personal+reserve;
        return actual>0&&actual<=quote+1e-6?actual:0;
    }
}
