package pl.krakow.uek.centrumWolontariatu.web.rest.util;

import java.util.Optional;


public final class ParserRSQLUtil {
    public static Optional<String> parse(Optional<String> string){
        String query = null;
        if(string.isPresent()) query = string.get().replace("true","1").replace("false","0").replace("&", ";");
        return Optional.ofNullable(query);
    }

    public static com.google.common.base.Optional<String> parseGuavaOptional(Optional<String> string){
        String query = null;
        if(string.isPresent()) query = string.get().replace("true","1").replace("false","0").replace("&", ";");
        else if(!string.isPresent()) query="id>0";
        return com.google.common.base.Optional.fromNullable(query);
    }

    public static byte parse(boolean bool){
        return (byte) (bool ? 1 : 0);
    }


}
