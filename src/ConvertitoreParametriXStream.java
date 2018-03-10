import com.thoughtworks.xstream.converters.*;
import com.thoughtworks.xstream.io.*;

public class ConvertitoreParametriXStream implements Converter{ //(01)
    
    @Override
        public boolean canConvert(Class cl) {   //(02)
                return cl.equals(ParametroInIntervallo.class);
        }

        @Override
        public void marshal(Object valore, HierarchicalStreamWriter scrittore,//(03)
                        MarshallingContext contesto) {
                ParametroInIntervallo pii = (ParametroInIntervallo) valore;
                scrittore.startNode("default");
                scrittore.setValue(pii.def.toString());
                scrittore.endNode();
                scrittore.startNode("minimo");
                scrittore.setValue(pii.min.toString());
                scrittore.endNode();
                scrittore.startNode("massimo");
                scrittore.setValue(pii.max.toString());
                scrittore.endNode();
        }

        @Override
        public Object unmarshal(HierarchicalStreamReader lettore,    //(04)
                        UnmarshallingContext context) {
                ParametroInIntervallo pii = new ParametroInIntervallo<>();
                String tipoValori = lettore.getAttribute("valori");
                switch (tipoValori) {
                    case "interi":
                        lettore.moveDown();
                        pii.def = Integer.parseInt(lettore.getValue());
                        lettore.moveUp();
                        lettore.moveDown();
                        pii.min = Integer.parseInt(lettore.getValue());
                        lettore.moveUp();
                        lettore.moveDown();
                        pii.max = Integer.parseInt(lettore.getValue());
                        lettore.moveUp();
                        break;
                    case "reali":
                        lettore.moveDown();
                        pii.def = Double.parseDouble(lettore.getValue());
                        lettore.moveUp();
                        lettore.moveDown();
                        pii.min = Double.parseDouble(lettore.getValue());
                        lettore.moveUp();
                        lettore.moveDown();
                        pii.max = Double.parseDouble(lettore.getValue());
                        lettore.moveUp();
                }
                return pii;
        }
}

/*
(01)
L'interfaccia Converter permette di ridefinire o specificare il
comportamento di XStream nel convertire determinati oggetti in XML e viceversa.
http://x-stream.github.io/converters.html

(02)
Il metodo canConvert() specifica quali classi il convertitore è in grado di
gestire. In questo caso, l'unica comportamento da definire è quello di
ParametriInIntervallo.
http://x-stream.github.io/converter-tutorial.html

(03)
Il metodo marshal() viene invocato in occasione di una conversione da oggetto
verso XML.
http://x-stream.github.io/converter-tutorial.html

(04)
Il metodo unmarshal() viene invocato in occasione di una conversione da XML
verso oggetto. Restituisce l'oggetto convertito.
http://x-stream.github.io/converter-tutorial.html
*/