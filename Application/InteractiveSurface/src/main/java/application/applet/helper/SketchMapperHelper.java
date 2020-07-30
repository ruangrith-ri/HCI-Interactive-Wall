package application.applet.helper;

import ixagon.surface.mapper.SurfaceMapper;
import jto.processing.sketch.mapper.SketchMapper;

import java.lang.reflect.Field;

public class SketchMapperHelper{

    public static SurfaceMapper getSurfaceMapper(SketchMapper obj)  {
        try {
            Field sketchMapperField = obj.getClass().getDeclaredField("surfaceMapper");
            sketchMapperField.setAccessible(true);
            return (SurfaceMapper) sketchMapperField.get(obj);
        } catch (Exception ignore){
        }

        return null;
    }
}
