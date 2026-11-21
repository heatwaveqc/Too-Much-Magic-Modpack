package io.github.heatwaveqc.tyr.progression.data;
import com.google.gson.JsonObject;

/** Human-facing node metadata, separate from ownership and mechanical identity. */
public record PresentationSpec(String title,String description,String lane) {
    public static PresentationSpec parse(JsonObject j){
        String title=read(j,"title",120),description=read(j,"description",1200),lane=read(j,"lane",120);
        return new PresentationSpec(title,description,lane);
    }
    private static String read(JsonObject j,String field,int max){
        String s=j.has(field)?j.get(field).getAsString():"";
        if(s.length()>max)throw new IllegalArgumentException("Oversize "+field);return s;
    }
    public static PresentationSpec empty(){return new PresentationSpec("","","");}
}
