package io.github.heatwaveqc.tyr.progression.foundation;

import java.util.function.Predicate;
import java.util.function.Supplier;

/** Native attacker identity is decisive; only entirely ownerless damage may use an exact approved scope. */
public final class HunterAttackerPolicy {
    private HunterAttackerPolicy(){}

    public static <T> T resolve(T causing,T direct,Predicate<? super T> eligiblePlayer,
            Supplier<? extends T> trustedScope){
        T nativeAttacker=causing!=null?causing:direct;
        if(nativeAttacker!=null)return eligiblePlayer.test(nativeAttacker)?nativeAttacker:null;
        T scoped=trustedScope.get();
        return scoped!=null&&eligiblePlayer.test(scoped)?scoped:null;
    }
}
