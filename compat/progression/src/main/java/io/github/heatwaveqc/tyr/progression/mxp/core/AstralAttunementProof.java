package io.github.heatwaveqc.tyr.progression.mxp.core;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/** One personal altar milestone, not a first reward for each constellation. */
public final class AstralAttunementProof {
    private AstralAttunementProof(){}
    public static final String ID="astralsorcery:player_attunement";
    public static RitualRewards.Receipt receipt(UUID actor,boolean first,boolean actual){
        if(actor==null||!first||!actual)return null;
        UUID operation=UUID.nameUUIDFromBytes((ID+":"+actor).getBytes(StandardCharsets.UTF_8));
        return new RitualRewards.Receipt(ID,operation,actor,true,false);
    }
}
