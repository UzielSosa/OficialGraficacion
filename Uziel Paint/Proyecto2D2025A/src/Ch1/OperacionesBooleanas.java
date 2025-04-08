// OperacionesBooleanas.java
import java.awt.Shape;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;

public class OperacionesBooleanas {
    private List<Shape> figurasSeleccionadas = new ArrayList<>();

    public void agregarFigura(Shape figura) {
        if (figura != null) {
            figurasSeleccionadas.add(figura);
        }
    }

    public void limpiarSeleccion() {
        figurasSeleccionadas.clear();
    }

    public Shape union() {
        if (figurasSeleccionadas.size() < 2) return null;
        Area resultado = new Area(figurasSeleccionadas.get(0));
        for (int i = 1; i < figurasSeleccionadas.size(); i++) {
            resultado.add(new Area(figurasSeleccionadas.get(i)));
        }
        return resultado;
    }

    public Shape interseccion() {
        if (figurasSeleccionadas.size() < 2) return null;
        Area resultado = new Area(figurasSeleccionadas.get(0));
        for (int i = 1; i < figurasSeleccionadas.size(); i++) {
            resultado.intersect(new Area(figurasSeleccionadas.get(i)));
        }
        return resultado;
    }

    public Shape diferencia() {
        if (figurasSeleccionadas.size() != 2) return null;
        Area resultado = new Area(figurasSeleccionadas.get(0));
        resultado.subtract(new Area(figurasSeleccionadas.get(1)));
        return resultado;
    }

    public Shape xor() {
        if (figurasSeleccionadas.size() != 2) return null;
        Area resultado = new Area(figurasSeleccionadas.get(0));
        resultado.exclusiveOr(new Area(figurasSeleccionadas.get(1)));
        return resultado;
    }
}